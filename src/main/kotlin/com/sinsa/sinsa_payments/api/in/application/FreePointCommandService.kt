package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO
import com.sinsa.sinsa_payments.api.`in`.port.FindRedisUseCase
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointUseCase
import com.sinsa.sinsa_payments.api.out.port.FindFreePointPort
import com.sinsa.sinsa_payments.api.out.port.FindFreePointSnapshotPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointPort
import com.sinsa.sinsa_payments.api.out.port.SaveFreePointSnapshotPort
import com.sinsa.sinsa_payments.common.exception.FreePointException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class FreePointCommandService (
    private val saveFreePointPort: SaveFreePointPort,
    private val findFreePointPort: FindFreePointPort,
    private val findFreePointSnapshotPort: FindFreePointSnapshotPort,
    private val saveFreePointSnapshotPort: SaveFreePointSnapshotPort,
    private val findRedisUseCase: FindRedisUseCase
) : SaveFreePointUseCase {

    @Transactional
    override fun save(freePoint: FreePointVO): FreePointVO {
        checkMaxPoint(freePoint.point)
        val now = LocalDateTime.now()

        //만료 되지 않은 적립 금액만 가져온다
        val freePoints = findFreePointPort.findFreePointsByMemberId(freePoint.memberId, now)
        val totalHeldPoint = freePoints.sumOf { it.point }

        //요청 금액 + 현재 적립금액이 정책에 설정된 보유 금액을 넘는지 확인한다.
        checkMaxHeldPoint(totalHeldPoint + freePoint.point)
        //정책에 설정된 만료일을 토대로 만료일을 설정하고 저장한다.
        val expiredDate = now.plusDays(findRedisUseCase.findExpiredDate())
        val savedFreePoint = saveFreePointPort.save(freePoint.toDomain(expiredDate))

        //snapshot 에 ACCUMULATED 로 저장한다.
        saveFreePointSnapshotPort.save(
            FreePointSnapshot.from(
                savedFreePoint.memberId,
                savedFreePoint.id!!,
                savedFreePoint.point,
                null,
                FreePointSnapshotStatus.ACCUMULATED
            )
        )

        return FreePointVO.from(savedFreePoint)
    }

    @Transactional
    override fun cancel(pointId: Long) : FreePointVO {
        val snapShots = findFreePointSnapshotPort.findByPointIdWithApprovalAndCancel(pointId)
        checkValidFreePointSnapshot(snapShots)

        val freePoint = findFreePointPort.findByIdWithLock(pointId) ?:
            throw FreePointException(
                ExceptionCode.FREE_POINT_NOT_FOUND,
                "${ExceptionCode.FREE_POINT_NOT_FOUND}$pointId"
            )

        if (LocalDateTime.now().isAfter(freePoint.expiredDate)) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_ALREADY_EXPIRE,
                ExceptionCode.FREE_POINT_ALREADY_EXPIRE.message
            )
        }

        val targetPoint = freePoint.point

        // 다 사용하여 취소 처리를 한다.
        freePoint.usePoint(targetPoint)
        val savedFreePoint = saveFreePointPort.save(freePoint)

        // snapshot 에 적립취소 상태를 저장한다.
        saveFreePointSnapshotPort.save(
            FreePointSnapshot.from(
                freePoint.memberId,
                freePoint.id!!,
                -targetPoint,
                null,
                FreePointSnapshotStatus.ACCUMULATED_CANCEL
            )
        )

        return FreePointVO.from(savedFreePoint)
    }

    private fun checkValidFreePointSnapshot(snapShots : List<FreePointSnapshot>) {
        val totalPoint = snapShots.sumOf { it.point }

        if (totalPoint != BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_ALREADY_USED,
                ExceptionCode.FREE_POINT_ALREADY_USED.message
            )
        }
    }

    private fun checkMaxHeldPoint(totalPoint: BigDecimal) {
        val redisValue = findRedisUseCase.findMaxHeldPoint()

        if (redisValue < totalPoint) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_HELD_TOO_MUCH,
                ExceptionCode.FREE_POINT_HELD_TOO_MUCH.message
            )
        }
    }

    private fun checkMaxPoint(point: BigDecimal) {
        // 0 이하의 요청은 이상한 요청으로 간주한다.
        if (point <= BigDecimal.ZERO) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO,
                ExceptionCode.FREE_POINT_LESS_OR_EQUAL_ZERO.message
            )
        }

        val redisValue = findRedisUseCase.findMaxAccumulatedPoint()

        //정책에서 설정한 1회 최대 적립금액보다 많다면 exception 을 발생시킨다.
        if (redisValue < point) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_ACCUMULATED_TOO_MUCH,
                "${ExceptionCode.FREE_POINT_ACCUMULATED_TOO_MUCH.message}${point}"
            )
        }
    }
}