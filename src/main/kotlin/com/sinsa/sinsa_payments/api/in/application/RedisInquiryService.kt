package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.port.FindRedisUseCase
import com.sinsa.sinsa_payments.api.out.port.FindPointPolicyPort
import com.sinsa.sinsa_payments.common.redis.service.RedisService
import com.sinsa.sinsa_payments.domain.PointPolicy
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class RedisInquiryService(
    private val findPointPolicyPort: FindPointPolicyPort,
    private val redisService: RedisService<String>
) : FindRedisUseCase {
    override fun findMaxAccumulatedPoint(): BigDecimal {
        var redisValue = redisService.get(PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = findPointPolicyPort.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME,
                pointPolicy.maxAccumulatedPoint.toString()
            )

            redisValue = pointPolicy.maxAccumulatedPoint.toString()
        }

        return BigDecimal(redisValue)
    }

    override fun findMaxHeldPoint(): BigDecimal {
        var redisValue = redisService.get(PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = findPointPolicyPort.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME,
                pointPolicy.maxHeldPoint.toString()
            )

            redisValue = pointPolicy.maxHeldPoint.toString()
        }

        return BigDecimal(redisValue)
    }

    override fun findExpiredDate(): Long {
        var redisValue = redisService.get(PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME)

        if (redisValue.isNullOrBlank()) {
            val pointPolicy = findPointPolicyPort.findLatestPointPolicy()

            redisService.set(
                PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME,
                pointPolicy.dayOfExpiredDate.toString()
            )

            redisValue = pointPolicy.dayOfExpiredDate.toString()
        }

        return redisValue.toLong()
    }
}