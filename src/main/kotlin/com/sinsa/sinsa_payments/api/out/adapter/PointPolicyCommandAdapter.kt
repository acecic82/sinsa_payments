package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.SavePointPolicyPort
import com.sinsa.sinsa_payments.domain.PointPolicy
import com.sinsa.sinsa_payments.persistence.entity.PointPolicyEntity
import com.sinsa.sinsa_payments.persistence.repository.PointPolicyRepository
import org.springframework.stereotype.Component

@Component
class PointPolicyCommandAdapter (
    val pointPolicyRepository: PointPolicyRepository
) : SavePointPolicyPort {
    override fun save(pointPolicy: PointPolicy) : PointPolicy {
        return pointPolicyRepository.save(PointPolicyEntity.from(pointPolicy)).toDomain()
    }
}