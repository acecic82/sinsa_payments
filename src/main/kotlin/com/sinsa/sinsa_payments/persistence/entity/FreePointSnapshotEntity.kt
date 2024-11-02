package com.sinsa.sinsa_payments.persistence.entity

import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "free_point_snapshot")
data class FreePointSnapshotEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val memberId: Long,
    val pointId: Long,
    val orderId: String? = null,
    val point: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "free_point_snapshot_status")
    val status: FreePointSnapshotStatus
) {
    fun toDomain() = FreePointSnapshot(
        id = this.id,
        memberId = this.memberId,
        pointId = this.pointId,
        orderId = this.orderId,
        point = this.point,
        status = this.status
    )
    companion object {
        fun from(freePointSnapShot: FreePointSnapshot) = FreePointSnapshotEntity(
            id = freePointSnapShot.id,
            memberId = freePointSnapShot.memberId,
            pointId = freePointSnapShot.pointId,
            orderId = freePointSnapShot.orderId,
            point = freePointSnapShot.point,
            status = freePointSnapShot.status
        )
    }
}
