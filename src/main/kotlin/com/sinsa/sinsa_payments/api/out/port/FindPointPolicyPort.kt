package com.sinsa.sinsa_payments.api.out.port

import com.sinsa.sinsa_payments.domain.PointPolicy

interface FindPointPolicyPort {
    fun findLatestPointPolicy() : PointPolicy
}