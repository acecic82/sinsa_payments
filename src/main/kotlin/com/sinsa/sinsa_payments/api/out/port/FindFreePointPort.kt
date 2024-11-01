package com.sinsa.sinsa_payments.api.out.port

import com.sinsa.sinsa_payments.domain.FreePoint
import java.time.LocalDateTime

interface FindFreePointPort {
    fun findFreePointsByMemberId(memberId: Long, expiredDate: LocalDateTime) : List<FreePoint>
}