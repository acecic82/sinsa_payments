package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointTransactionVO
import com.sinsa.sinsa_payments.api.`in`.port.FindRedisUseCase
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.out.port.FindFreePointPort
import com.sinsa.sinsa_payments.api.out.port.FindFreePointSnapshotPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointSnapshotPort
import com.sinsa.sinsa_payments.common.exception.FreePointException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.domain.FreePoint
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class FreePointSnapshotCommandService (
    private val findFreePointPort: FindFreePointPort,
    private val saveFreePointPort: SaveFreePointPort,
    private val findFreePointSnapshotPort: FindFreePointSnapshotPort,
    private val saveFreePointSnapshotPort: SaveFreePointSnapshotPort,
    private val findRedisUseCase: FindRedisUseCase
) : SaveFreePointSnapshotUseCase {

    @Transactional
    override fun use(freePointUseVO: FreePointTransactionVO) {
        //0원 이하 사용은 이상한 사용으로 판단한다.
        if (freePointUseVO.point <= BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO,
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO.message
            )
        }

        val now = LocalDateTime.now()

        // 수기 지급건부터 가져온다.
        val manualFreePoints =
            findFreePointPort.findFreePointsByMemberIdAndManual(freePointUseVO.memberId, true, now)

        // 수기로 추가된 포인트 부터 사용한다.
        val remainManualPoint = useFreePoint(manualFreePoints, freePointUseVO.point, freePointUseVO.orderId)

        // 수기 지금건 만으로도 포인트 사용이 가능하다면 끝낸다.
        if (remainManualPoint == BigDecimal.ZERO) {
            return
        }

        // 수기 지급으로는 포인트 사용이 모자르다면 수기 포인트가 아닌 포인트로 포인트 사용을 한다.
        val noManualFreePoints =
            findFreePointPort.findFreePointsByMemberIdAndManual(freePointUseVO.memberId, false, now)

        val remainNoManualPoint = useFreePoint(noManualFreePoints, remainManualPoint, freePointUseVO.orderId)

        // 총 지급된 포인트 보다 사용하려는 포인트가 많은 경우 (없을 것이라 생각되지만 예외 처리)
        if (remainNoManualPoint > BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_APPROVAL_TOO_MUCH,
                ExceptionCode.FREE_POINT_APPROVAL_TOO_MUCH.message
            )
        }
    }

    @Transactional
    override fun cancel(freePointTransactionVO: FreePointTransactionVO) {
        if (freePointTransactionVO.point <= BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO,
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO.message
            )
        }

        // 사용한것들 중 취소가 없는 건에 대해서만 가져온다 즉, 취소할 수 있는 모든 케이스를 memberId, OrderId 기준으로 가져온다.
        val freePointSnapshots = findFreePointSnapshotPort.findOnlyApprovalByMemberIdAndOrderId(
            freePointTransactionVO.memberId,
            freePointTransactionVO.orderId
        )

        val remainPoint = cancelFreePoint(freePointSnapshots, freePointTransactionVO.point)

        if (BigDecimal.ZERO < remainPoint) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_CANCEL_TOO_MUCH,
                ExceptionCode.FREE_POINT_CANCEL_TOO_MUCH.message
            )
        }
    }

    private fun cancelFreePoint(freePointSnapshots: List<FreePointSnapshot>, totalPoint: BigDecimal) : BigDecimal {
        var initPoint = totalPoint

        // 취소하려는 금액을 기준으로 취소 가능한 건들의 금액을 차례로 뺴가면서 진행한다.
        freePointSnapshots.forEach {
            val currentPoint = it.point

            //아직 취소할 돈이 남아 있는 경우
            if(currentPoint < initPoint) {
                initPoint = initPoint.minus(currentPoint)

                it.setApprovalKey(it.id!!)
                saveFreePointSnapshotPort.save(it)

                saveFreePointAndSnapshotOnlyCancel(it, currentPoint)
            }
            //취소할 돈이 현재 승인 건보다 적은 경우 혹은 딱 맞는 경우(마지막 케이스)
            else {
                val newRemainPoint = currentPoint.minus(initPoint)
                it.setApprovalKey(it.id!!)

                // 만약 취소하려는 돈이 승인된 건보다 작은 경우 새로운 approval 을 만든다
                // 예시 승인건 500원 취소하려는 금액 300원인 경우 300원으로 만들고 200원을 새로 쌓는다
                if(BigDecimal.ZERO < newRemainPoint) {
                    newApprovalFreePointSnapshot(it, newRemainPoint)
                    it.setFreePoint(initPoint)
                }

                saveFreePointSnapshotPort.save(it)
                saveFreePointAndSnapshotOnlyCancel(it, initPoint)

                return BigDecimal.ZERO
            }
        }

        // 모든 취소 가능한 승인건들에 대해서 취소했지만, 취소하려는 금액이 더 큰 경우(Exception Case)
        return initPoint
    }

    private fun newApprovalFreePointSnapshot(freePointSnapshot: FreePointSnapshot, point: BigDecimal) {
        val newApprovalFreePointSnapshot  = FreePointSnapshot(
            memberId = freePointSnapshot.memberId,
            point = point,
            pointId = freePointSnapshot.pointId,
            orderId = freePointSnapshot.orderId,
            approvalKey = null,
            status = FreePointSnapshotStatus.APPROVAL
        )

        saveFreePointSnapshotPort.save(newApprovalFreePointSnapshot)
    }

    private fun saveFreePointAndSnapshotOnlyCancel(freePointSnapshot: FreePointSnapshot, point: BigDecimal) {

        saveFreePointSnapshotPort.save(
            FreePointSnapshot(
                memberId = freePointSnapshot.memberId,
                pointId = freePointSnapshot.pointId,
                orderId = freePointSnapshot.orderId,
                point = -point,
                approvalKey = freePointSnapshot.id,
                status = FreePointSnapshotStatus.CANCEL
            )
        )

        val freePoint = findFreePointPort.findByIdWithLock(freePointSnapshot.pointId) ?: throw FreePointException(
            ExceptionCode.FREE_POINT_NOT_FOUND,
            "${ExceptionCode.FREE_POINT_NOT_FOUND}${freePointSnapshot.point}"
        )

        recoverFreePoint(freePoint, point)

    }

    private fun recoverFreePoint(freePoint: FreePoint, point: BigDecimal) {
        val now = LocalDateTime.now()

        if (freePoint.expiredDate.isBefore(now)) {
            val newFreePoint = FreePoint(
                memberId = freePoint.memberId,
                point = point,
                manual = freePoint.manual,
                expiredDate = now.plusDays(findRedisUseCase.findExpiredDate())
            )

            saveFreePointPort.save(newFreePoint)
        }
        else {
            freePoint.recoverPoint(point)

            saveFreePointPort.save(freePoint)
        }
    }

    private fun useFreePoint(freePoints: List<FreePoint>, totalPoint: BigDecimal, orderId: String) : BigDecimal {
        var initTotalPoint = totalPoint

        //포인트의 건수마다 차감하면서 사용한다.
        freePoints.forEach {
            val currentPoint = it.point

            //차감할 포인트가 더 크다면 전체를 차감한다.
            if(currentPoint < initTotalPoint) {
                initTotalPoint = initTotalPoint.minus(currentPoint)

                saveFreePointAndSnapshotOnlyApproval(it, orderId, currentPoint)

            }
            // 차감할 포인트가 같거나 더 적다면 마지막으로 차감해야할 포인트를 차감하고 끝낸다.
            else {
                saveFreePointAndSnapshotOnlyApproval(it, orderId, initTotalPoint)

                return BigDecimal.ZERO
            }
        }

        return initTotalPoint
    }

    private fun saveFreePointAndSnapshotOnlyApproval(freePoint: FreePoint, orderId: String, point: BigDecimal) {
        saveFreePointSnapshotPort.save(
            FreePointSnapshot(
                memberId = freePoint.memberId,
                pointId = freePoint.id!!,
                orderId = orderId,
                point = point,
                status = FreePointSnapshotStatus.APPROVAL
            )
        )

        freePoint.usePoint(point)

        saveFreePointPort.save(freePoint)
    }
}