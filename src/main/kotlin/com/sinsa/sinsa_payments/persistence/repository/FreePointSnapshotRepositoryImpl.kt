package com.sinsa.sinsa_payments.persistence.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.sinsa.sinsa_payments.domain.FreePointSnapshotStatus
import com.sinsa.sinsa_payments.persistence.entity.FreePointSnapshotEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import com.sinsa.sinsa_payments.persistence.entity.QFreePointSnapshotEntity.Companion.freePointSnapshotEntity
import jakarta.persistence.LockModeType
import org.springframework.stereotype.Repository

@Repository
class FreePointSnapshotRepositoryImpl(
    private val queryFactory : JPAQueryFactory
) : QuerydslRepositorySupport(FreePointSnapshotEntity::class.java),
    FreePointSnapshotRepositoryCustom {
    override fun findByPointId(pointId: Long): List<FreePointSnapshotEntity> {
        return queryFactory.select(freePointSnapshotEntity)
            .from(freePointSnapshotEntity)
            .where(freePointSnapshotEntity.pointId.eq(pointId))
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetch()
    }

    override fun findByPointIdAndApprovalOrCancel(pointId: Long): List<FreePointSnapshotEntity> {
        return queryFactory.select(freePointSnapshotEntity)
            .from(freePointSnapshotEntity)
            .where(
                freePointSnapshotEntity.pointId.eq(pointId).and(
                    freePointSnapshotEntity.status.eq(FreePointSnapshotStatus.APPROVAL).or(
                        freePointSnapshotEntity.status.eq(FreePointSnapshotStatus.CANCEL)
                    )
                )
            )
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetch()
    }
}