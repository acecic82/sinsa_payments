package com.sinsa.sinsa_payments.api.`in`.application

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointSnapshotVO
import com.sinsa.sinsa_payments.api.`in`.port.FindFreePointSnapshotUseCase
import com.sinsa.sinsa_payments.api.out.port.FindFreePointSnapshotPort
import org.springframework.stereotype.Service

@Service
class FreePointSnapshotInquiryService(
    private val findFreePointSnapshotPort: FindFreePointSnapshotPort
): FindFreePointSnapshotUseCase {
    override fun findByPointId(pointId: Long): List<FreePointSnapshotVO> {
        return findFreePointSnapshotPort.findByPointId(pointId).map {
            FreePointSnapshotVO.from(it)
        }
    }
}