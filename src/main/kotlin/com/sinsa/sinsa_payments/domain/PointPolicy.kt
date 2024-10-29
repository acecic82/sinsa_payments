package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class PointPolicy(
    val id: Long? = null,
    var maxAccumulatedPoint: BigDecimal,
    var maxHeldPoint: BigDecimal
) {
    fun changMaxAccumulatedPoint(point: BigDecimal) {
        this.maxAccumulatedPoint = point
    }

    fun changMaxHeldPoint(point: BigDecimal) {
        this.maxAccumulatedPoint = point
    }
}
