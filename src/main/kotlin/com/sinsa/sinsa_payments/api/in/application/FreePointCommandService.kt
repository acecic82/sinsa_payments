package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointUseCase
import com.sinsa.sinsa_payments.api.out.adapter.*
import com.sinsa.sinsa_payments.common.exception.FreePointException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.common.redis.service.RedisService
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import com.sinsa.sinsa_payments.domain.PointPolicy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class FreePointCommandService (
    private val freePointCommandAdapter: FreePointCommandAdapter,
    private val freePointInquiryAdapter: FreePointInquiryAdapter,
    private val pointPolicyInquiryAdapter: PointPolicyInquiryAdapter,
    private val freePointSnapShotInquiryAdapter: FreePointSnapshotInquiryAdapter,
    private val freePointSnapshotCommandAdapter: FreePointSnapshotCommandAdapter,
    private val redisService: RedisService<String>
) : SaveFreePointUseCase {

    @Transactional
    override fun save(freePoint: FreePointVO): FreePointVO {
        checkMaxPoint(freePoint.point)
        val now = LocalDateTime.now()

        val freePoints = freePointInquiryAdapter.findFreePointsByMemberId(freePoint.memberId, now)
        val totalHeldPoint = freePoints.sumOf { it.point }

        checkMaxHeldPoint(totalHeldPoint + freePoint.point)
        val expiredDate = now.plusDays(getExpiredDateFromPolicy())
        val savedFreePoint = freePointCommandAdapter.save(freePoint.toDomain(expiredDate))

        freePointSnapshotCommandAdapter.save(
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
        val snapShots = freePointSnapShotInquiryAdapter.findByPointIdWithApprovalAndCancel(pointId)
        checkValidFreePointSnapshot(snapShots)

        val freePoint = freePointInquiryAdapter.findByIdWithLock(pointId) ?:
            throw FreePointException(
                ExceptionCode.FREE_POINT_NOT_FOUND,
                "${ExceptionCode.FREE_POINT_NOT_FOUND}$pointId"
            )
        val targetPoint = freePoint.point

        // 다 사용하여 취소 처리를 한다.
        freePoint.usePoint(targetPoint)
        val savedFreePoint = freePointCommandAdapter.save(freePoint)

        // snapshot 에 적립취소 상태를 저장한다.
        freePointSnapshotCommandAdapter.save(
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

    private fun getExpiredDateFromPolicy() : Long {
        var redisValue = redisService.get(PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = pointPolicyInquiryAdapter.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME,
                pointPolicy.dayOfExpiredDate.toString()
            )

            redisValue = pointPolicy.dayOfExpiredDate.toString()
        }

        return redisValue.toLong()
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
        var redisValue = redisService.get(PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = pointPolicyInquiryAdapter.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME,
                pointPolicy.maxHeldPoint.toString()
            )

            redisValue = pointPolicy.maxHeldPoint.toString()
        }

        if (BigDecimal.valueOf(redisValue.toLong()) < totalPoint) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_HELD_TOO_MUCH,
                ExceptionCode.FREE_POINT_HELD_TOO_MUCH.message
            )
        }
    }

    private fun checkMaxPoint(point: BigDecimal) {
        var redisValue = redisService.get(PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = pointPolicyInquiryAdapter.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME,
                pointPolicy.maxAccumulatedPoint.toString()
            )

            redisValue = pointPolicy.maxAccumulatedPoint.toString()
        }

        if (BigDecimal.valueOf(redisValue.toLong()) < point) {
            throw FreePointException(
                ExceptionCode.FREE_POINT_ACCUMULATED_TOO_MUCH,
                "${ExceptionCode.FREE_POINT_ACCUMULATED_TOO_MUCH.message}${point}"
            )
        }
    }
}