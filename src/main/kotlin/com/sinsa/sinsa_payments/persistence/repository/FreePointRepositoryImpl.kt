package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import com.sinsa.sinsa_payments.persistence.entity.QFreePointEntity.Companion.freePointEntity
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class FreePointRepositoryImpl : QuerydslRepositorySupport(FreePointEntity::class.java), FreePointRepositoryCustom {
    override fun findPointByMemberId(memberId: Long, expiredDateTime: LocalDateTime): List<FreePointEntity> {
        return from(freePointEntity)
            .where(freePointEntity.memberId.eq(memberId).and(freePointEntity.expiredDate.loe(expiredDateTime)))
            .orderBy(freePointEntity.expiredDate.asc())
            .fetch()
    }
}