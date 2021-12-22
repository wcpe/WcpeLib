package top.wcpe.wcpelib.common.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.util.function.Consumer

/**
 * 由 WCPE 在 2021/12/22 18:08 创建
 *
 * Created by WCPE on 2021/12/22 18:08
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class Redis {
    private val jedisPool: JedisPool

    constructor(url: String, port: Int) {
        this.jedisPool = JedisPool(url, port)
    }

    constructor(
        url: String,
        port: Int,
        timeOut: Int,
        maxTotal: Int,
        maxIdle: Int,
        minIdle: Int,
        jmxEnabled: Boolean,
        testOnCreate: Boolean,
        blockWhenExhausted: Boolean,
        maxWaitMillis: Int,
        testOnBorrow: Boolean,
        testOnReturn: Boolean
    ) : this(
        url,
        port,
        timeOut,
        null,
        maxTotal,
        maxIdle,
        minIdle,
        jmxEnabled,
        testOnCreate,
        blockWhenExhausted,
        maxWaitMillis,
        testOnBorrow,
        testOnReturn
    )

    constructor(
        url: String,
        port: Int,
        timeOut: Int,
        password: String?,
        maxTotal: Int,
        maxIdle: Int,
        minIdle: Int,
        jmxEnabled: Boolean,
        testOnCreate: Boolean,
        blockWhenExhausted: Boolean,
        maxWaitMillis: Int,
        testOnBorrow: Boolean,
        testOnReturn: Boolean
    ) {
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
        this.jedisPool = JedisPool(jedisPoolConfig, url, port, timeOut, password)
    }

    fun getResource(): Jedis {
        return jedisPool.resource
    }

    fun useRedisResource(callBack: Consumer<Jedis>) {
        jedisPool.run {
            resource?.use {
                callBack.accept(it)
            }
        }
    }

    fun useRedisResource(callBack: Consumer<Jedis>, select: Int) {
        jedisPool.run {
            resource?.use {
                it.select(select)
                callBack.accept(it)
            }
        }
    }

}