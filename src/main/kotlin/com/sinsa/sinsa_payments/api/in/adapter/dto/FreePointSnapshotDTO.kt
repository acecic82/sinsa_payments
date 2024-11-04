package com.sinsa.sinsa_payments.api.`in`.adapter.dto

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointSnapshotVO
import java.math.BigDecimal

data class FreePointSnapshotDTO(
    val pointId: Long,
    val orderId: String? = null,
    var point: BigDecimal,
    val status: String
) {
    companion object {
        fun from(vo: FreePointSnapshotVO) = FreePointSnapshotDTO(
            vo.pointId, vo.orderId, vo.point, vo.status
        )
    }
}
