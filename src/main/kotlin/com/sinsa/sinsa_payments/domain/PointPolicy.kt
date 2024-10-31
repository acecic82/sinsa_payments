package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class PointPolicy(
    val id: Long? = null,
    var maxAccumulatedPoint: BigDecimal,
    var maxHeldPoint: BigDecimal
) {
    fun checkValidationMaxAccumulatedPoint(point: Long) : Boolean {
        return point in 1..DEFAULT_MAX_ACCUMULATED_POINT
    }

    fun checkValidationMaxHeldPoint(point: Long) : Boolean {
        return point in 1..DEFAULT_MAX_HELD_POINT
    }

    fun changMaxAccumulatedPoint(point: BigDecimal) {
        this.maxAccumulatedPoint = point
    }

    fun changMaxHeldPoint(point: BigDecimal) {
        this.maxAccumulatedPoint = point
    }

    companion object {
        const val DEFAULT_MAX_ACCUMULATED_POINT = 100_000L
        const val DEFAULT_MAX_HELD_POINT = 1_000_000L

        const val REDIS_MAX_ACCUMULATED_POINT_KEY_NAME = "PointPolicyMaxAccumulatedPointKey"
        const val REDIS_MAX_HELD_POINT_KEY_NAME = "PointPolicyMaxHeldPointKey"
    }
}
