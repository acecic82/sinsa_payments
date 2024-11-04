package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.FindFreePointPort
import com.sinsa.sinsa_payments.domain.FreePoint
import com.sinsa.sinsa_payments.persistence.repository.FreePointRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class FreePointInquiryAdapter (
    private val freePointRepository: FreePointRepository
) : FindFreePointPort {
    override fun findFreePointsByMemberId(memberId: Long, expiredDate: LocalDateTime): List<FreePoint> {
        return freePointRepository.findPointByMemberIdWithLock(memberId, expiredDate).map {
            it.toDomain()
        }
    }

    override fun findFreePointsByMemberIdAndManual(
        memberId: Long,
        manual: Boolean,
        expiredDate: LocalDateTime
    ): List<FreePoint> {
        return freePointRepository.findPointByMemberIdAndManualWithLock(memberId, manual, expiredDate).map {
            it.toDomain()
        }
    }

    override fun findByIdWithLock(pointId: Long): FreePoint? {
        return freePointRepository.findByIdWithLock(pointId)?.toDomain()
    }

    override fun findExpiredFreePoint(expiredDate: LocalDateTime): List<FreePoint> {
        return freePointRepository.findExpiredFreePoint(expiredDate).map {
            it.toDomain()
        }
    }
}