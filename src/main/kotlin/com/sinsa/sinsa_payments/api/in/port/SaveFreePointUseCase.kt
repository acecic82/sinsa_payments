package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO

interface SaveFreePointUseCase {
    fun save(freePoint: FreePointVO) : FreePointVO

    fun cancel(pointId: Long) : FreePointVO

    fun checkValidFreePointSnapshot(pointId: Long)
}