package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointSnapshotVO

interface FindFreePointSnapshotUseCase {
    fun findByPointId(pointId: Long) : List<FreePointSnapshotVO>
}