package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointSnapshotEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FreePointSnapshotRepository : JpaRepository<FreePointSnapshotEntity, Long>, FreePointSnapshotRepositoryCustom {
}

interface  FreePointSnapshotRepositoryCustom {
    fun findByPointId(pointId: Long) : List<FreePointSnapshotEntity>

    fun findByPointIdAndApprovalOrCancelWithLock(pointId: Long) : List<FreePointSnapshotEntity>

    fun findOnlyApprovalByMemberIdAndOrderIdWithLock(memberId: Long, orderId: String) : List<FreePointSnapshotEntity>
}