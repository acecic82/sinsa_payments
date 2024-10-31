package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.PointPolicyVO
import com.sinsa.sinsa_payments.api.`in`.port.SavePointPolicyUseCase
import com.sinsa.sinsa_payments.api.out.adapter.PointPolicyCommandAdapter
import com.sinsa.sinsa_payments.api.out.adapter.PointPolicyInquiryAdapter
import com.sinsa.sinsa_payments.common.exception.PointPolicyException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import com.sinsa.sinsa_payments.common.redis.service.RedisService
import com.sinsa.sinsa_payments.domain.PointPolicy
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PointPolicyCommandService (
    private val pointPolicyInquiryAdapter: PointPolicyInquiryAdapter,
    private val pointPolicyCommandAdapter: PointPolicyCommandAdapter,
    private val redisService: RedisService<String>
) : SavePointPolicyUseCase {

    override fun saveMaxAccumulatedPoint(point: Long) : PointPolicyVO {
        val pointPolicy = findLatestPointPolicy()

        require(pointPolicy.checkValidationMaxAccumulatedPoint(point)) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_INVALID_ACCUMULATED,
                "${ExceptionCode.POINT_POLICY_INVALID_ACCUMULATED.message}$point"
            )
        }

        pointPolicy.changMaxAccumulatedPoint(BigDecimal(point))

        val pointPolicyVO = PointPolicyVO.from(pointPolicyCommandAdapter.save(pointPolicy))
        redisService.set(PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME, point.toString())
        val afterSetRedis = redisService.get(PointPolicy.REDIS_MAX_ACCUMULATED_POINT_KEY_NAME)

        require(afterSetRedis.compareTo(point.toString()) == 0) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL,
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL.message
            )
        }

        return pointPolicyVO
    }

    override fun saveMaxHeldPoint(point: Long) : PointPolicyVO {
        val pointPolicy = findLatestPointPolicy()

        require(pointPolicy.checkValidationMaxHeldPoint(point)) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_INVALID_HELD,
                "${ExceptionCode.POINT_POLICY_INVALID_HELD.message}$point"
            )
        }

        pointPolicy.changMaxHeldPoint(BigDecimal(point))

        val pointPolicyVO = PointPolicyVO.from(pointPolicyCommandAdapter.save(pointPolicy))
        redisService.set(PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME, point.toString())
        val afterSetRedis = redisService.get(PointPolicy.REDIS_MAX_HELD_POINT_KEY_NAME)

        require(afterSetRedis.compareTo(point.toString()) == 0) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL,
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL.message
            )
        }

        return pointPolicyVO
    }

    override fun saveExpiredDateOfDay(days: Long): PointPolicyVO {
        val pointPolicy = findLatestPointPolicy()

        require(pointPolicy.checkValidationDayOfExpiredDate(days)) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_INVALID_DAY_OF_EXPIRED_DATE,
                "${ExceptionCode.POINT_POLICY_INVALID_DAY_OF_EXPIRED_DATE.message}$days"
            )
        }

        pointPolicy.changDayOfExpiredDate(days)

        val pointPolicyVO = PointPolicyVO.from(pointPolicyCommandAdapter.save(pointPolicy))
        redisService.set(PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME, days.toString())
        val afterSetRedis = redisService.get(PointPolicy.REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME)

            require(afterSetRedis.compareTo(days.toString()) == 0) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL,
                ExceptionCode.POINT_POLICY_REDIS_SETTING_FAIL.message
            )
        }

        return pointPolicyVO
    }

    private fun findLatestPointPolicy() : PointPolicy {
        return pointPolicyInquiryAdapter.findLatestPointPolicy()
    }
}