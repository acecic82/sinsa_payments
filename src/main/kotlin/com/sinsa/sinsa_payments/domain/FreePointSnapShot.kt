package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class FreePointSnapShot(
    val id: Long? = null,
    val pointId: Long,
    val orderId: String,
    val point: BigDecimal
)