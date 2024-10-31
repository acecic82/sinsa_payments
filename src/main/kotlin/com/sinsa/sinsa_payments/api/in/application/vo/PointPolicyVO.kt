package com.sinsa.sinsa_payments.api.`in`.application.vo

import com.sinsa.sinsa_payments.domain.PointPolicy
import java.math.BigDecimal

data class PointPolicyVO (
    var maxAccumulatedPoint: BigDecimal,
    var maxHeldPoint: BigDecimal
) {
    companion object {
        fun from(pointPolicy: PointPolicy) = PointPolicyVO (
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint
        )
    }
}
