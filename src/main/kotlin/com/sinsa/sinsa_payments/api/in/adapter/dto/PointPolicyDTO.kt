package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.PointPolicyVO
import java.math.BigDecimal

data class PointPolicyDTO (
    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal,
    val dayOfExpiredDate: Long
) {
    companion object {
        fun from(pointPolicy: PointPolicyVO) = PointPolicyDTO (
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint,
            dayOfExpiredDate = pointPolicy.dayOfExpiredDate
        )
    }
}
