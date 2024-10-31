package com.sinsa.sinsa_payments.api.out.port

import com.sinsa.sinsa_payments.domain.FreePoint

interface SaveFreePointPort {
    fun save(freePoint: FreePoint) : FreePoint
}