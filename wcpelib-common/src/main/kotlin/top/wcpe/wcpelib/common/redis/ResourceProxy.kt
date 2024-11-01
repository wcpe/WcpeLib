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

    fun keys(pattern: String): Set<String> {
        return jedis.keys(pattern)
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

    /**
     * TTL key
     *
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     *
     * 可用版本：
     * >= 1.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以秒为单位，返回 key 的剩余生存时间。
     * 在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1 。
     */
    fun ttl(key: String): Long {
        return jedis.ttl(key)
    }

    /**
     * EXPIRE key seconds
     *
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     *
     * 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
     *
     * 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
     *
     * 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
     *
     * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
     *
     * RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，这时旧的 another_key (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
     *
     * 使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久的』(persistent) key 。
     *
     * 更新生存时间
     *
     * 可以对一个已经带有生存时间的 key 执行 EXPIRE 命令，新指定的生存时间会取代旧的生存时间。
     *
     * 过期时间的精确度
     *
     * 在 Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内 —— 也即是，就算 key 已经过期，但它还是可能在过期之后一秒钟之内被访问到，而在新的 Redis 2.6 版本中，延迟被降低到 1 毫秒之内。
     *
     * Redis 2.1.3 之前的不同之处
     *
     * 在 Redis 2.1.3 之前的版本中，修改一个带有生存时间的 key 会导致整个 key 被删除，这一行为是受当时复制(replication)层的限制而作出的，现在这一限制已经被修复。
     *
     * 可用版本：
     * >= 1.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 设置成功返回 1 。
     * 当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 0 。
     */
    fun expire(key: String, value: Long): Long {
        return jedis.expire(key, value)
    }

    /**
     * EXPIREAT key timestamp
     *
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。
     *
     * 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     *
     * 可用版本：
     * >= 1.2.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 如果生存时间设置成功，返回 1 。
     * 当 key 不存在或没办法设置生存时间，返回 0 。
     */
    fun expireAt(key: String, unixTime: Long): Long {
        return jedis.expireAt(key, unixTime)
    }


    /**
     * PERSIST key
     *
     * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
     *
     * 可用版本：
     * >= 2.2.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 当生存时间移除成功时，返回 1 .
     * 如果 key 不存在或 key 没有设置生存时间，返回 0 。
     */
    fun persist(key: String): Long {
        return jedis.persist(key)
    }

    /**
     * PTTL key
     *
     * 这个命令类似于 TTL 命令，但它以毫秒为单位返回 key 的剩余生存时间，而不是像 TTL 命令那样，以秒为单位。
     *
     * 可用版本：
     * >= 2.6.0
     * 复杂度：
     * O(1)
     * 返回值：
     * 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则，以毫秒为单位，返回 key 的剩余生存时间。
     * 在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1 。
     */
    fun pttl(key: String): Long {
        return jedis.pttl(key)
    }

    /**
     * PEXPIRE key milliseconds
     *
     * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位。
     *
     * 可用版本：
     * >= 2.6.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 设置成功，返回 1
     * key 不存在或设置失败，返回 0
     */
    fun pexpire(key: String, value: Long): Long {
        return jedis.pexpire(key, value)
    }

    /**
     * PEXPIREAT key milliseconds-timestamp
     *
     * 这个命令和 EXPIREAT 命令类似，但它以毫秒为单位设置 key 的过期 unix 时间戳，而不是像 EXPIREAT 那样，以秒为单位。
     *
     * 可用版本：
     * >= 2.6.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 如果生存时间设置成功，返回 1 。
     * 当 key 不存在或没办法设置生存时间时，返回 0 。(查看 EXPIRE 命令获取更多信息)
     */
    fun pexpireAt(key: String, value: Long): Long {
        return jedis.pexpireAt(key, value)
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

    /**
     * HDEL key field [field ...]
     *
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * 在Redis2.4以下的版本里， HDEL 每次只能删除单个域，如果你需要在一个原子时间内删除多个域，请将命令包含在 MULTI / EXEC 块内。
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度:
     * O(N)， N 为要删除的域的数量。
     * 返回值:
     * 被成功移除的域的数量，不包括被忽略的域。
     */
    fun hdel(key: String, vararg fields: String): Long {
        return jedis.hdel(key, * fields)
    }

    /**
     * HLEN key
     *
     * 返回哈希表 key 中域的数量。
     *
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 哈希表中域的数量。
     * 当 key 不存在时，返回 0 。
     */
    fun hlen(key: String): Long {
        return jedis.hlen(key)
    }

    /**
     * HKEYS key
     *
     * 返回哈希表 key 中的所有域。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(N)， N 为哈希表的大小。
     * 返回值：
     * 一个包含哈希表中所有域的表。
     * 当 key 不存在时，返回一个空表。
     */
    fun hkeys(key: String): Set<String> {
        return jedis.hkeys(key)
    }

    /**
     * HVALS key
     *
     * 返回哈希表 key 中所有域的值。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(N)， N 为哈希表的大小。
     * 返回值：
     * 一个包含哈希表中所有值的表。
     * 当 key 不存在时，返回一个空表。
     */
    fun hvals(key: String): List<String> {
        return jedis.hvals(key)
    }

    /**
     * HGETALL key
     *
     * 返回哈希表 key 中，所有的域和值。
     *
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(N)， N 为哈希表的大小。
     * 返回值：
     * 以列表形式返回哈希表的域和域的值。
     * 若 key 不存在，返回空列表。
     */
    fun hgetAll(key: String): Map<String, String> {
        return jedis.hgetAll(key)
    }

    fun hrandfield(key: String): String {
        return jedis.hrandfield(key)
    }

    fun hrandfield(key: String, count: Long): List<String> {
        return jedis.hrandfield(key, count)
    }

    fun hrandfieldWithValues(key: String?, count: Long): List<Map.Entry<String, String>> {
        return jedis.hrandfieldWithValues(key, count)
    }

    /**
     * HSET key field value
     *
     * 将哈希表 key 中的域 field 的值设为 value 。
     *
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     *
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。
     * 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
     */
    fun hset(key: String, field: String, value: String?): Long {
        return jedis.hset(key, field, value)
    }

    fun hset(key: String, hash: Map<String, String?>): Long {
        return jedis.hset(key, hash)
    }

    /**
     * HGET key field
     *
     * 返回哈希表 key 中给定域 field 的值。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 给定域的值。
     * 当给定域不存在或是给定 key 不存在时，返回 nil 。
     */
    fun hget(key: String, field: String): String? {
        return jedis.hget(key, field)
    }

    /**
     * HSETNX key field value
     *
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     *
     * 若域 field 已经存在，该操作无效。
     *
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 设置成功，返回 1 。
     * 如果给定域已经存在且没有操作被执行，返回 0 。
     */
    fun hsetnx(key: String, field: String, value: String?): Long {
        return jedis.hsetnx(key, field, value)
    }

    /**
     * HMSET key field value [field value ...]
     *
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     *
     * 此命令会覆盖哈希表中已存在的域。
     *
     * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(N)， N 为 field-value 对的数量。
     * 返回值：
     * 如果命令执行成功，返回 OK 。
     * 当 key 不是哈希表(hash)类型时，返回一个错误。
     */
    fun hmset(key: String, hash: Map<String, String?>): String {
        return jedis.hmset(key, hash)
    }

    /**
     * HMGET key field [field ...]
     *
     * 返回哈希表 key 中，一个或多个给定域的值。
     *
     * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
     *
     * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
     *
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(N)， N 为给定域的数量。
     * 返回值：
     * 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
     */
    fun hmget(key: String, vararg fields: String): List<String> {
        return jedis.hmget(key, *fields)
    }


    /**
     * SETNX key value
     *
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     *
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     *
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
     *
     * 可用版本：
     * >= 1.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 设置成功，返回 1 。
     * 设置失败，返回 0 。
     * redis> EXISTS job                # job 不存在
     * (integer) 0
     *
     * redis> SETNX job "programmer"    # job 设置成功
     * (integer) 1
     *
     * redis> SETNX job "code-farmer"   # 尝试覆盖 job ，失败
     * (integer) 0
     *
     * redis> GET job                   # 没有被覆盖
     * "programmer"
     */
    fun setnx(key: String, value: String): Long {
        return jedis.setnx(key, value)
    }

}