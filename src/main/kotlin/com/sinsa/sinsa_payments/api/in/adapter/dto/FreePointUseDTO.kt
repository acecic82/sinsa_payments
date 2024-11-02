package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointUseVO
import java.math.BigDecimal

data class FreePointUseDTO (
    val memberId: Long,
    val point: String,
    val orderId: String
) {
    fun toVO() = FreePointUseVO (
        memberId = this.memberId,
        point = BigDecimal(this.point),
        orderId = this.orderId,
    )
}