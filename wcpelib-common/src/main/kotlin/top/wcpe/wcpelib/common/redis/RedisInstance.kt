package top.wcpe.wcpelib.common.redis

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import top.wcpe.wcpelib.common.adapter.ConfigAdapter

/**
 * 由 WCPE 在 2024/8/9 13:32 创建
 * <p>
 * Created by WCPE on 2024/8/9 13:32
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
data class RedisInstance(
    val url: String,
    val port: Int,
    val timeOut: Int,
    val password: String?,
    val index: Int,
    val expire: Long,
    val maxTotal: Int,
    val maxIdle: Int,
    val minIdle: Int,
    val jmxEnabled: Boolean,
    val testOnCreate: Boolean,
    val blockWhenExhausted: Boolean,
    val maxWaitMillis: Int,
    val testOnBorrow: Boolean,
    val testOnReturn: Boolean,
) {
    companion object {

        @JvmStatic
        fun load(configAdapter: ConfigAdapter): RedisInstance {

            val url = configAdapter.getString("redis.url")
            val port = configAdapter.getInt("redis.port")
            return RedisInstance(
                url,
                port,
                configAdapter.getInt("redis.time-out"),
                configAdapter.getString("redis.password").ifEmpty { null },
                configAdapter.getInt("redis.index"),
                configAdapter.getLong("redis.expire"),
                configAdapter.getInt("redis.max-total"),
                configAdapter.getInt("redis.max-idle"),
                configAdapter.getInt("redis.min-idle"),
                configAdapter.getBoolean("redis.jmx-enabled"),
                configAdapter.getBoolean("redis.test-on-create"),
                configAdapter.getBoolean("redis.block-when-exhausted"),
                configAdapter.getInt("redis.max-wait-millis"),
                configAdapter.getBoolean("redis.test-on-borrow"),
                configAdapter.getBoolean("redis.test-on-return")
            )
        }
    }

    fun build(): JedisPool {
        val jedisPoolConfig = JedisPoolConfig()
        jedisPoolConfig.maxTotal = maxTotal
        jedisPoolConfig.maxIdle = maxIdle
        jedisPoolConfig.minIdle = minIdle
        jedisPoolConfig.jmxEnabled = jmxEnabled
        jedisPoolConfig.testOnCreate = testOnCreate
        jedisPoolConfig.blockWhenExhausted = blockWhenExhausted
        jedisPoolConfig.maxWaitMillis = maxWaitMillis.toLong()
        jedisPoolConfig.testOnBorrow = testOnBorrow
        jedisPoolConfig.testOnReturn = testOnReturn
        return JedisPool(jedisPoolConfig, url, port, timeOut, password)
    }
}