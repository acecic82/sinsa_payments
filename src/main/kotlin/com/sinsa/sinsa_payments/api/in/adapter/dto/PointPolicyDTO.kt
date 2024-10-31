package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.PointPolicyVO
import java.math.BigDecimal

data class PointPolicyDTO (
    var maxAccumulatedPoint: BigDecimal,
    var maxHeldPoint: BigDecimal
) {
    companion object {
        fun from(pointPolicy: PointPolicyVO) = PointPolicyDTO (
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint
        )
    }
}
