package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.SaveFreePointSnapshotPort
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.persistence.entity.FreePointSnapshotEntity
import com.sinsa.sinsa_payments.persistence.repository.FreePointSnapshotRepository
import org.springframework.stereotype.Component

@Component
class FreePointSnapshotCommandAdapter (
    private val freePointSnapshotRepository: FreePointSnapshotRepository
) : SaveFreePointSnapshotPort {
    override fun save(freePointSnapshot: FreePointSnapshot): FreePointSnapshot {
        return freePointSnapshotRepository.save(FreePointSnapshotEntity.from(freePointSnapshot)).toDomain()
    }
}