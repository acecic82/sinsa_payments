package com.sinsa.sinsa_payments.api.`in`.port

import com.sinsa.sinsa_payments.api.`in`.application.vo.FreePointVO

interface SaveFreePointUseCase {
    fun save(freePoint: FreePointVO) : FreePointVO
}