package com.sinsa.sinsa_payments.domain

import java.math.BigDecimal

data class FreePointSnapshot(
    val id: Long? = null,
    val memberId: Long,
    val pointId: Long,
    val orderId: String? = null,
    var point: BigDecimal,
    var approvalKey: Long? = null,
    val status: FreePointSnapshotStatus
) {
    fun setApprovalKey(approvalKey: Long) {
        this.approvalKey = approvalKey
    }

    fun setFreePoint(point: BigDecimal) {
        this.point = point
    }

    companion object {
        fun from(
            memberId: Long,
            freePointId: Long,
            point: BigDecimal,
            orderId: String? = null,
            status: FreePointSnapshotStatus
        ) =
            FreePointSnapshot(
                memberId = memberId,
                pointId = freePointId,
                orderId = orderId,
                point = point,
                approvalKey = null,
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
