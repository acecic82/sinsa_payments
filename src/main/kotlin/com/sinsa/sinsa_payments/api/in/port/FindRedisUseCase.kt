package com.sinsa.sinsa_payments.api.`in`.port

import java.math.BigDecimal

interface FindRedisUseCase {
    fun findMaxAccumulatedPoint(): BigDecimal
    fun findMaxHeldPoint(): BigDecimal
    fun findExpiredDate(): Long
}