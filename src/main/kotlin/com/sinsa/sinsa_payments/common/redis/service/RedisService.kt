package kr.co._29cm.homework.common.redis.service

import org.redisson.api.RLock
import java.time.Duration

interface RedisService<T> {
    fun get(key: String) : T

    fun set(key: String, value: T, duration: Duration = Duration.ofMinutes(10L))

    fun getRLock(key: String): RLock
}