package top.wcpe.wcpelib.common.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import java.util.function.Consumer

/**
 * 由 WCPE 在 2021/12/22 18:08 创建
 *
 * Created by WCPE on 2021/12/22 18:08
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.7-alpha-dev-1
 */
object Redis {
    private lateinit var jedisPool: JedisPool


    fun init(redisInstance: RedisInstance): Redis {
        this.redisInstance = redisInstance
        jedisPool = redisInstance.build()
        return this
    }

    private lateinit var redisInstance: RedisInstance

    fun close() {
        jedisPool.close()
    }

    fun getResource(): Jedis {
        return jedisPool.resource.apply {
            select(redisInstance.index)
        }
    }

    fun getResource(index: Int): Jedis {
        return jedisPool.resource.apply {
            select(index)
        }
    }

    fun getResourceProxy(): ResourceProxy {
        return ResourceProxy(jedisPool.resource.apply {
            select(redisInstance.index)
        }, redisInstance.expire)
    }

    fun getResourceProxy(index: Int): ResourceProxy {
        return ResourceProxy(jedisPool.resource.apply {
            select(index)
        }, redisInstance.expire)
    }


    fun useRedisResource(callBack: Consumer<Jedis>) {
        getResource().use {
            callBack.accept(it)
        }
    }

    fun useRedisResource(callBack: Consumer<Jedis>, select: Int) {
        getResource(select).use {
            callBack.accept(it)
        }
    }

    fun useRedisResourceProxy(callBack: Consumer<ResourceProxy>) {
        getResourceProxy().use {
            callBack.accept(it)
        }
    }

    fun useRedisResourceProxy(callBack: Consumer<ResourceProxy>, select: Int) {
        getResourceProxy(select).use {
            callBack.accept(it)
        }
    }

}