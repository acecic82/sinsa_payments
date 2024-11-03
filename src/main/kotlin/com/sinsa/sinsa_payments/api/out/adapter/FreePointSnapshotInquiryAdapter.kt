package com.sinsa.sinsa_payments.api.out.adapter

import com.sinsa.sinsa_payments.api.out.port.FindFreePointSnapshotPort
import com.sinsa.sinsa_payments.domain.FreePointSnapshot
import com.sinsa.sinsa_payments.persistence.repository.FreePointSnapshotRepository
import org.springframework.stereotype.Component

@Component
class FreePointSnapshotInquiryAdapter (
    private val freePointSnapshotRepository: FreePointSnapshotRepository
) : FindFreePointSnapshotPort {
    override fun findByPointId(pointId: Long) : List<FreePointSnapshot> {
        return freePointSnapshotRepository.findByPointId(pointId).map {
            it.toDomain()
        }
    }

    override fun findByPointIdWithApprovalAndCancel(pointId: Long): List<FreePointSnapshot> {
        return freePointSnapshotRepository.findByPointIdAndApprovalOrCancelWithLock(pointId).map {
            it.toDomain()
        }
    }

    override fun findOnlyApprovalByMemberIdAndOrderId(memberId: Long, orderId: String): List<FreePointSnapshot> {
        return freePointSnapshotRepository.findOnlyApprovalByMemberIdAndOrderIdWithLock(memberId, orderId).map {
            it.toDomain()
        }
    }
}