package kr.co._29cm.homework.common.redis.service

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisServiceImp<T>(
    val redissonClient : RedissonClient
) : RedisService<T> {
    override fun get(key: String): T {
        val bucket = redissonClient.getBucket<T>(key)
        return bucket.get()
    }

    override fun set(key: String, value: T, duration: Duration) {
        val bucket = redissonClient.getBucket<T>(key)
        bucket.set(value, duration)
    }

    override fun getRLock(key: String): RLock {
        return redissonClient.getLock(key)
    }
}