package com.sinsa.sinsa_payments.persistence.entity

import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "free_point_snapshot")
data class FreePointSnapshotEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val pointId: Long,
    val orderId: String,
    val point: BigDecimal
) {
    companion object {
        fun from(freePointSnapShot: FreePointSnapshot) = FreePointSnapshotEntity(
            id = freePointSnapShot.id,
            pointId = freePointSnapShot.pointId,
            orderId = freePointSnapShot.orderId,
            point = freePointSnapShot.point
        )
    }
}
