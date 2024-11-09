package com.sinsa.sinsa_payments.persistence.entity

import com.sinsa.sinsa_payments.domain.PointPolicy
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "point_policy")
data class PointPolicyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "max_accumulated_point")
    val maxAccumulatedPoint: BigDecimal,
    @Column(name = "max_held_point")
    val maxHeldPoint: BigDecimal,
    @Column(name = "day_of_expired_date")
    val dayOfExpiredDate: Long
) {
    fun toDomain() = PointPolicy(
        id = this.id,
        maxAccumulatedPoint = this.maxAccumulatedPoint,
        maxHeldPoint = this.maxHeldPoint,
        dayOfExpiredDate = this.dayOfExpiredDate
    )
    companion object {
        fun from(pointPolicy: PointPolicy) = PointPolicyEntity(
            id = pointPolicy.id,
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint,
            dayOfExpiredDate = pointPolicy.dayOfExpiredDate
        )
    }
}
