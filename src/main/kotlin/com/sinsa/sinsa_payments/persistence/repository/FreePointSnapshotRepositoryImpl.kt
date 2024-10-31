package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointSnapshotEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class FreePointSnapshotRepositoryImpl : QuerydslRepositorySupport(FreePointSnapshotEntity::class.java),
    FreePointSnapshotRepositoryCustom {
}