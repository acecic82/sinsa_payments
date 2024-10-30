package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class FreePointRepositoryImpl : QuerydslRepositorySupport(FreePointEntity::class.java), FreePointRepositoryCustom {
}