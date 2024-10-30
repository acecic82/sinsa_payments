package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointSnapshotEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FreePointSnapshotRepository : JpaRepository<FreePointSnapshotEntity, Long>, FreePointRepositoryCustom {
}

interface  FreePointSnapshotRepositoryCustom {
}