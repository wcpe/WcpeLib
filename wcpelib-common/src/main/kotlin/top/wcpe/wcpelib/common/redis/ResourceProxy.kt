package top.wcpe.wcpelib.common.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.params.ScanParams
import java.io.Closeable


/**
 * 由 WCPE 在 2022/10/2 12:52 创建
 *
 * Created by WCPE on 2022/10/2 12:52
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.1-alpha-dev-2
 */
class ResourceProxy(private val jedis: Jedis, val expire: Long) : Closeable {

    fun select(index: Int): String? {
        return jedis.select(index)
    }

    fun exists(vararg keys: String): Long {
        return jedis.exists(*keys)
    }

    fun exists(key: String): Boolean {
        return jedis.exists(key)
    }

    operator fun get(key: String): String? {
        return jedis.get(key)
    }

    operator fun set(key: String, value: String): String? {
        val result = jedis.set(key, value)
        jedis.expire(key, expire)
        return result
    }

    fun del(vararg keys: String): Long {
        return jedis.del(*keys)
    }

    fun del(key: String): Long {
        return jedis.del(key)
    }

    fun expire(key: String, value: Long): Long {
        return jedis.expire(key, value)
    }

    fun lpush(key: String, vararg strings: String): Long {
        return jedis.lpush(key, *strings)
    }

    fun lrem(key: String, count: Long, value: String): Long {
        return jedis.lrem(key, count, value)
    }

    fun llen(key: String): Long {
        return jedis.llen(key)
    }

    override fun close() {
        jedis.close()
    }

    fun deleteKeysWithPattern(pattern: String) {
        // 设置游标初始值
        var cursor = ScanParams.SCAN_POINTER_START
        // 设置匹配模式
        val scanParams = ScanParams().match(pattern)
        do {
            // 执行模糊查询
            val scanResult = jedis.scan(cursor, scanParams)
            for (key in scanResult.result) {
                // 删除匹配到的 key
                jedis.del(key)
            }
            // 获取新的游标值
            cursor = scanResult.cursor
        } while (cursor != ScanParams.SCAN_POINTER_START)
    }

}