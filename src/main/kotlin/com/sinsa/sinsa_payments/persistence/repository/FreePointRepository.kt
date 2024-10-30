package com.sinsa.sinsa_payments.persistence.repository

import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.time.LocalDateTime

interface FreePointRepository : JpaRepository<FreePointEntity, Long>, FreePointRepositoryCustom {
}

interface  FreePointRepositoryCustom {
    //ExpiredDate 를 기준으로 오래된 순서로 가져오는 메서드
    @Lock(LockModeType.PESSIMISTIC_READ)
    fun findPointByMemberId(memberId: Long, expiredDateTime: LocalDateTime): List<FreePointEntity>
}