package com.sinsa.sinsa_payments.persistence.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import com.sinsa.sinsa_payments.persistence.entity.QFreePointEntity.Companion.freePointEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class FreePointRepositoryImpl(
    private val queryFactory : JPAQueryFactory
) : QuerydslRepositorySupport(FreePointEntity::class.java), FreePointRepositoryCustom {
    override fun findPointByMemberId(memberId: Long, expiredDateTime: LocalDateTime): List<FreePointEntity> {
        return queryFactory.select(freePointEntity)
            .from(freePointEntity)
            .where(freePointEntity.memberId.eq(memberId).and(freePointEntity.expiredDate.gt(expiredDateTime)))
            .orderBy(freePointEntity.expiredDate.asc())
            .setLockMode(LockModeType.PESSIMISTIC_READ)
            .fetch()
    }
}