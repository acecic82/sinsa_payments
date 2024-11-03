package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointTransactionVO
import java.math.BigDecimal

data class FreePointTransactionDTO (
    val memberId: Long,
    val point: String,
    val orderId: String
) {
    fun toVO() = FreePointTransactionVO (
        memberId = this.memberId,
        point = BigDecimal(this.point),
        orderId = this.orderId,
    )
}