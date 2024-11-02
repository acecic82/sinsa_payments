package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class FreePointSnapshot(
    val id: Long? = null,
    val memberId: Long,
    val pointId: Long,
    val orderId: String? = null,
    val point: BigDecimal,
    val status: FreePointSnapshotStatus
) {
    companion object {
        fun from(memberId: Long, freePointId: Long, point: BigDecimal, orderId: String? = null, status: FreePointSnapshotStatus) =
            FreePointSnapshot(
                memberId = memberId,
                pointId = freePointId,
                orderId = orderId,
                point = point,
                status = status
            )
    }
}

enum class FreePointSnapshotStatus {
    ACCUMULATED,
    ACCUMULATED_CANCEL,
    APPROVAL,
    CANCEL,
    EXPIRE
}
