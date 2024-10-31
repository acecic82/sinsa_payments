package com.sinsa.sinsa_payments.api.`in`.application.vo

import com.sinsa.sinsa_payments.domain.FreePoint
import java.math.BigDecimal
import java.time.LocalDateTime

data class FreePointVO (
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean,
    val expiredDate: LocalDateTime
) {
    companion object {
        fun from(freePoint: FreePoint) = FreePointVO(
            memberId = freePoint.memberId,
            point = freePoint.point,
            manual = freePoint.manual,
            expiredDate = freePoint.expiredDate
        )
    }
}
