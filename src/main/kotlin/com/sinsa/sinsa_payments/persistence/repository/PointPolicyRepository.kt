package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.PointPolicyEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointPolicyRepository : JpaRepository<PointPolicyEntity, Long>, PointPolicyRepositoryCustom {
}

interface PointPolicyRepositoryCustom {
    fun findLatestPointPolicy() : PointPolicyEntity
}