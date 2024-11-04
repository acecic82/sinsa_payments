package com.sinsa.sinsa_payments.api.`in`.application.vo

import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import java.math.BigDecimal

data class FreePointSnapshotVO(
    val pointId: Long,
    val orderId: String? = null,
    var point: BigDecimal,
    val status: String
) {
    companion object {
        fun from(freePointSnapshot: FreePointSnapshot) = FreePointSnapshotVO(
            pointId = freePointSnapshot.pointId,
            orderId = freePointSnapshot.orderId,
            point = freePointSnapshot.point,
            status = freePointSnapshot.status.toString()
        )
    }
}