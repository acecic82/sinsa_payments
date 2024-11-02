package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO
import java.math.BigDecimal

data class FreePointDTO(
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean
) {
    fun toVO() = FreePointVO(
        memberId = this.memberId,
        point = this.point,
        manual = this.manual
    )
    companion object {
        fun from(freePoint: FreePointVO) = FreePointDTO(
            memberId = freePoint.memberId,
            point = freePoint.point,
            manual = freePoint.manual
        )
    }
}
