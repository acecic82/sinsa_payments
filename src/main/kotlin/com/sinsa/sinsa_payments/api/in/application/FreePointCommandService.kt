package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO
import com.sinsa.sinsa_payments.api.`in`.port.SaveFreePointUseCase
import com.sinsa.sinsa_payments.api.out.adapter.FreePointCommandAdapter
import com.sinsa.sinsa_payments.api.out.adapter.FreePointInquiryAdapter
import com.sinsa.sinsa_payments.api.out.adapter.PointPolicyInquiryAdapter
import com.sinsa.sinsa_payments.common.exception.FreePointException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.common.redis.service.RedisService
import com.sinsa.sinsa_payments.domain.PointPolicy
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class FreePointCommandService (
    private val freePointCommandAdapter: FreePointCommandAdapter,
    private val freePointInquiryAdapter: FreePointInquiryAdapter,
    private val pointPolicyInquiryAdapter: PointPolicyInquiryAdapter,
    private val redisService: RedisService<String>
) : SaveFreePointUseCase {
    override fun save(freePoint: FreePointVO): FreePointVO {
        checkMaxPoint(freePoint.point)
        val expiredDate = LocalDateTime.now()

        val freePoints = freePointInquiryAdapter.findFreePointsByMemberId(freePoint.memberId, expiredDate)
        val totalHeldPoint = freePoints.sumOf { it.point }

        checkMaxHeldPoint(totalHeldPoint + freePoint.point)

        return FreePointVO.from(freePointCommandAdapter.save(freePoint.toDomain(expiredDate)))
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