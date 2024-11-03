package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class PointPolicy(
    val id: Long? = null,
    var maxAccumulatedPoint: BigDecimal,
    var maxHeldPoint: BigDecimal,
    var dayOfExpiredDate: Long
) {
    fun checkValidationMaxAccumulatedPoint(point: Long) : Boolean {
        return point in 1..DEFAULT_MAX_ACCUMULATED_POINT
    }

    fun checkValidationMaxHeldPoint(point: Long) : Boolean {
        return point in 1..DEFAULT_MAX_HELD_POINT
    }

    fun checkValidationDayOfExpiredDate(days: Long): Boolean {
        return days in 1L..<DEFAULT_MAX_DAY_OF_EXPIRED_DATE
    }

    fun changMaxAccumulatedPoint(point: BigDecimal) {
        this.maxAccumulatedPoint = point
    }

    fun changMaxHeldPoint(point: BigDecimal) {
        this.maxHeldPoint = point
    }

    fun changDayOfExpiredDate(days: Long) {
        this.dayOfExpiredDate = days
    }

    companion object {
        const val DEFAULT_MAX_ACCUMULATED_POINT = 100_000L
        const val DEFAULT_MAX_HELD_POINT = 1_000_000L
        const val DEFAULT_MAX_DAY_OF_EXPIRED_DATE = 365L * 5
        const val DEFAULT_DAY_OF_EXPIRED_DATE = 365L

        const val REDIS_MAX_ACCUMULATED_POINT_KEY_NAME = "PointPolicyMaxAccumulatedPointKey"
        const val REDIS_MAX_HELD_POINT_KEY_NAME = "PointPolicyMaxHeldPointKey"
        const val REDIS_DAY_OR_EXPIRED_DATE_KEY_NAME = "PointPolicyDayOfExpiredDateKey"
    }
}
