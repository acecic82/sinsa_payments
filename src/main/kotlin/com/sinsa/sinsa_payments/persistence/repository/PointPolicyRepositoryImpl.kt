package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.PointPolicyEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PointPolicyRepositoryImpl : QuerydslRepositorySupport(PointPolicyEntity::class.java), PointPolicyRepositoryCustom {
}