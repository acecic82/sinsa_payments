package com.sinsa.sinsa_payments.api.`in`.port

interface SavePointPolicyUseCase {
    fun saveMaxAccumulatedPoint(point: Long)
    fun saveMaxHeldPoint(point: Long)
}