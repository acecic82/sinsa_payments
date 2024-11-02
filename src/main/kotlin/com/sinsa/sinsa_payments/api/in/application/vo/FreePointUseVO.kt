package com.sinsa.sinsa_payments.api.`in`.application.vo

import java.math.BigDecimal

data class FreePointUseVO(
    val memberId: Long,
    val point: BigDecimal,
    val orderId: String
)
