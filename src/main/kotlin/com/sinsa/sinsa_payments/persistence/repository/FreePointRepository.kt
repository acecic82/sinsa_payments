package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface FreePointRepository : JpaRepository<FreePointEntity, Long>, FreePointRepositoryCustom {
}

interface  FreePointRepositoryCustom {
    //ExpiredDate 를 기준으로 오래된 순서로 가져오는 메서드
    fun findPointByMemberIdWithLock(memberId: Long, expiredDateTime: LocalDateTime): List<FreePointEntity>

    fun findByIdWithLock(pointId: Long) : FreePointEntity?

    fun findPointByMemberIdAndManualWithLock(
        memberId: Long,
        manual: Boolean,
        expiredDateTime: LocalDateTime
    ): List<FreePointEntity>
}