package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.PointPolicyEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import com.sinsa.sinsa_payments.persistence.entity.QPointPolicyEntity.Companion.pointPolicyEntity

class PointPolicyRepositoryImpl : QuerydslRepositorySupport(PointPolicyEntity::class.java),
    PointPolicyRepositoryCustom {
    override fun findLatestPointPolicy(): PointPolicyEntity? {
        val result = from(pointPolicyEntity)
            .orderBy(pointPolicyEntity.id.desc())
            .fetch()

        if (result.isEmpty()) return null

        return result[0]
    }
}