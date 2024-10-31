package com.sinsa.sinsa_payments.api.out.port

import com.sinsa.sinsa_payments.domain.PointPolicy

interface SavePointPolicyPort {
    fun save(pointPolicy: PointPolicy)
}