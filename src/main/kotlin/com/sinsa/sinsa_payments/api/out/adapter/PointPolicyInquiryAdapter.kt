package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.FindPointPolicyPort
import com.sinsa.sinsa_payments.domain.PointPolicy
import com.sinsa.sinsa_payments.persistence.repository.PointPolicyRepository
import org.springframework.stereotype.Component

@Component
class PointPolicyInquiryAdapter (
    val pointPolicyRepository: PointPolicyRepository
) : FindPointPolicyPort {
    override fun findLatestPointPolicy(): PointPolicy? {
        return pointPolicyRepository.findLatestPointPolicy()?.toDomain()
    }

}