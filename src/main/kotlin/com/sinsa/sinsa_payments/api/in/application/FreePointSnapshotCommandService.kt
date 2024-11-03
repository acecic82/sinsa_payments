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
    override fun save(freePointUseVO: FreePointTransactionVO) {
        val now = LocalDateTime.now()

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

        freePointSnapshots.forEach {
            val currentPoint = it.point

            if(currentPoint < initPoint) {
                initPoint = initPoint.minus(currentPoint)

                it.setApprovalKey(it.id!!)
                saveFreePointSnapshotPort.save(it)

                saveFreePointAndSnapshotOnlyCancel(it, currentPoint)
            }
            else {
                val newRemainPoint = currentPoint.minus(initPoint)
                it.setApprovalKey(it.id!!)

                if(BigDecimal.ZERO < newRemainPoint) {
                    newApprovalFreePointSnapshot(it, newRemainPoint)
                    it.setFreePoint(initPoint)
                }

                saveFreePointSnapshotPort.save(it)
                saveFreePointAndSnapshotOnlyCancel(it, initPoint)

                return BigDecimal.ZERO
            }
        }

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

        freePoints.forEach {
            val currentPoint = it.point

            if(currentPoint < initTotalPoint) {
                initTotalPoint = initTotalPoint.minus(currentPoint)

                saveFreePointAndSnapshotOnlyApproval(it, orderId, currentPoint)

            } else {
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