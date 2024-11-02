package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointUseVO

interface SaveFreePointSnapshotUseCase {
    fun save(freePointUseVO: FreePointUseVO)
}