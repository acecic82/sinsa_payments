package com.sinsa.sinsa_payments.persistence.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import com.sinsa.sinsa_payments.persistence.entity.QFreePointEntity.Companion.freePointEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
class FreePointRepositoryImpl(
    private val queryFactory : JPAQueryFactory
) : QuerydslRepositorySupport(FreePointEntity::class.java), FreePointRepositoryCustom {
    override fun findPointByMemberIdWithLock(memberId: Long, expiredDateTime: LocalDateTime): List<FreePointEntity> {
        return queryFactory.select(freePointEntity)
            .from(freePointEntity)
            .where(freePointEntity.memberId.eq(memberId).and(freePointEntity.expiredDate.gt(expiredDateTime).and(
                freePointEntity.point.gt(BigDecimal.ZERO))))
            .orderBy(freePointEntity.expiredDate.asc())
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetch()
    }

    override fun findByIdWithLock(pointId: Long): FreePointEntity? {
        val result = queryFactory.select(freePointEntity)
            .from(freePointEntity)
            .where(freePointEntity.id.eq(pointId))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetch()

        if (result.isEmpty()) return null

        return result[0]
    }

    override fun findPointByMemberIdAndManualWithLock(
        memberId: Long,
        manual: Boolean,
        expiredDateTime: LocalDateTime
    ): List<FreePointEntity> {
        return queryFactory.select(freePointEntity)
            .from(freePointEntity)
            .where(freePointEntity.memberId.eq(memberId).and(
                freePointEntity.manual.eq(manual)).and(freePointEntity.expiredDate.gt(expiredDateTime).and(
                freePointEntity.point.gt(BigDecimal.ZERO))))
            .orderBy(freePointEntity.expiredDate.asc())
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetch()
    }

    override fun findExpiredFreePoint(expiredDateTime: LocalDateTime): List<FreePointEntity> {
        return queryFactory.select(freePointEntity)
            .from(freePointEntity)
            .where(freePointEntity.expiredDate.lt(expiredDateTime).and(freePointEntity.point.gt(BigDecimal.ZERO)))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetch()
    }
}