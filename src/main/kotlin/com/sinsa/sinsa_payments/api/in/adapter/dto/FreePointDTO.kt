package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO
import java.math.BigDecimal

data class FreePointDTO(
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean
) {
    companion object {
        fun from(freePoint: FreePointVO) = FreePointDTO(
            memberId = freePoint.memberId,
            point = freePoint.point,
            manual = freePoint.manual
        )
    }
}
