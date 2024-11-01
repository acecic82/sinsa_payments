package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.FindFreePointPort
import com.sinsa.sinsa_payments.domain.FreePoint
import com.sinsa.sinsa_payments.persistence.repository.FreePointRepository
import java.time.LocalDateTime

class FreePointInquiryAdapter (
    private val freePointRepository: FreePointRepository
) : FindFreePointPort {
    override fun findFreePointsByMemberId(memberId: Long, expiredDate: LocalDateTime): List<FreePoint> {
        return freePointRepository.findPointByMemberId(memberId, expiredDate).map {
            it.toDomain()
        }
    }
}