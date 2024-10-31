package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.port.SavePointPolicyUseCase
import com.sinsa.sinsa_payments.api.out.adapter.PointPolicyCommandAdapter
import com.sinsa.sinsa_payments.api.out.adapter.PointPolicyInquiryAdapter
import com.sinsa.sinsa_payments.domain.PointPolicy
import com.sinsa.sinsa_payments.common.exception.PointPolicyException
import com.sinsa.sinsa_payments.common.exception.enum.ExceptionCode
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PointPolicyCommandService (
    private val pointPolicyInquiryAdapter: PointPolicyInquiryAdapter,
    private val pointPolicyCommandAdapter: PointPolicyCommandAdapter
) : SavePointPolicyUseCase{
    override fun saveMaxAccumulatedPoint(point: Long) {
        val pointPolicy = findLatestPointPolicy()

        require(pointPolicy.checkValidationMaxAccumulatedPoint(point)) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_INVALID_ACCUMULATED,
                "${ExceptionCode.POINT_POLICY_INVALID_ACCUMULATED.message}$point"
            )
        }

        pointPolicy.changMaxAccumulatedPoint(BigDecimal(point))

        pointPolicyCommandAdapter.save(pointPolicy)
    }

    override fun saveMaxHeldPoint(point: Long) {
        val pointPolicy = findLatestPointPolicy()

        require(pointPolicy.checkValidationMaxHeldPoint(point)) {
            throw PointPolicyException(
                ExceptionCode.POINT_POLICY_INVALID_HELD,
                "${ExceptionCode.POINT_POLICY_INVALID_HELD.message}$point"
            )
        }

        pointPolicy.changMaxHeldPoint(BigDecimal(point))

        pointPolicyCommandAdapter.save(pointPolicy)
    }

    private fun findLatestPointPolicy() : PointPolicy {
        return pointPolicyInquiryAdapter.findLatestPointPolicy() ?: PointPolicy(
            maxAccumulatedPoint = BigDecimal(PointPolicy.DEFAULT_MAX_ACCUMULATED_POINT),
            maxHeldPoint = BigDecimal(PointPolicy.DEFAULT_MAX_HELD_POINT)
        )
    }
}