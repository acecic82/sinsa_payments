package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.SaveFreePointPort
import com.sinsa.sinsa_payments.domain.FreePoint
import com.sinsa.sinsa_payments.persistence.entity.FreePointEntity
import com.sinsa.sinsa_payments.persistence.repository.FreePointRepository
import org.springframework.stereotype.Component

@Component
class FreePointCommandAdapter (
    private val freePointRepository: FreePointRepository
) : SaveFreePointPort {
    override fun save(freePoint: FreePoint): FreePoint {
        return freePointRepository.save(FreePointEntity.from(freePoint)).toDomain()
    }
}