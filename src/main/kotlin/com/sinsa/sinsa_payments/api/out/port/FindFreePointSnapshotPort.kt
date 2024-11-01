package com.sinsa.sinsa_payments.api.out.port

import com.sinsa.sinsa_payments.domain.FreePointSnapshot

interface FindFreePointSnapshotPort {
    fun findByPointId(pointId: Long) : List<FreePointSnapshot>
    fun findByPointIdWithApprovalAndCancel(pointId: Long) : List<FreePointSnapshot>
}