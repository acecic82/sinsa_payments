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

    val maxAccumulatedPoint: BigDecimal,
    val maxHeldPoint: BigDecimal
) {
    companion object {
        fun from(pointPolicy: PointPolicy) = PointPolicy(
            id = pointPolicy.id,
            maxAccumulatedPoint = pointPolicy.maxAccumulatedPoint,
            maxHeldPoint = pointPolicy.maxHeldPoint
        )
    }
}