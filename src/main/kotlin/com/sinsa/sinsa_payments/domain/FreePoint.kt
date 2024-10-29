package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class FreePoint(
    val id: Long? = null,
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean,
    val expiredDate: LocalDateTime = LocalDateTime.now().plusDays(DEFAULT_EXPIRED_DAY)
) {

    companion object {
        const val DEFAULT_EXPIRED_DAY = 365L
    }
}
