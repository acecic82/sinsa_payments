package com.sinsa.sinsa_payments.persistence.entity

import com.sinsa.sinsa_payments.domain.FreePoint
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "free_point")
data class FreePointEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "member_id")
    val memberId: Long,
    val point: BigDecimal,
    val manual: Boolean,
    @Column(name = "expired_date")
    val expiredDate: LocalDateTime
) {
    fun toDomain() = FreePoint(
        id = this.id,
        memberId = this.memberId,
        point = this.point,
        manual = this.manual,
        expiredDate = this.expiredDate
    )

    companion object {
        fun from(freePoint: FreePoint) = FreePointEntity(
            id = freePoint.id,
            memberId = freePoint.memberId,
            point = freePoint.point,
            manual = freePoint.manual,
            expiredDate = freePoint.expiredDate
        )
    }
}
