package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointSnapshotVO
import com.sinsa.sinsa_payments.api.`in`.port.FindFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.out.port.FindFreePointSnapshotPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FreePointSnapshotInquiryService(
    private val findFreePointSnapshotPort: FindFreePointSnapshotPort
): FindFreePointSnapshotUseCase {
    @Transactional(readOnly = true)
    override fun findByPointId(pointId: Long): List<FreePointSnapshotVO> {
        return findFreePointSnapshotPort.findByPointId(pointId).map {
            FreePointSnapshotVO.from(it)
        }
    }
}