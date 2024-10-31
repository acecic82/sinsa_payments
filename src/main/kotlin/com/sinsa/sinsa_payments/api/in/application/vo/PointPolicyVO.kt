package com.sinsa.sinsa_payments.api.`in`.application.vo

import com.sinsa.sinsa_payments.domain.PointPolicy
import java.math.BigDecimal

data class PointPolicyVO (
    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal,
    val dayOfExpiredDate: Long
) {
    companion object {
        fun from(pointPolicy: PointPolicy) = PointPolicyVO (
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint,
            dayOfExpiredDate = pointPolicy.dayOfExpiredDate
        )
    }
}
