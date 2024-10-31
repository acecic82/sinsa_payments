package com.sinsa.sinsa_payments.common.redis.service

import org.redisson.api.RLock
import java.time.Duration

interface RedisService<T> {
    fun get(key: String) : T

    fun set(key: String, value: T, duration: Duration = Duration.ofMinutes(60L))

    fun getRLock(key: String): RLock
}