package top.wcpe.wcpelib.common

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.ktor.Ktor
import top.wcpe.wcpelib.common.mybatis.Mybatis
import top.wcpe.wcpelib.common.redis.Redis

/**
 * 由 WCPE 在 2022/1/3 22:16 创建
 *
 * Created by WCPE on 2022/1/3 22:16
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class WcpeLibCommon(
    private val loggerAdapter: LoggerAdapter,
    mySQLConfigAdapter: ConfigAdapter,
    redisConfigAdapter: ConfigAdapter,
    ktorConfigAdapter: ConfigAdapter
) {

    val mybatis: Mybatis? = mySQLConfigAdapter.run {
        if (getBoolean("mysql.enable")) {
            loggerAdapter.info("Mybatis 开启! 开始连接数据库")
            val start = System.currentTimeMillis()
            try {
                return@run Mybatis(
                    url = getString("mysql.url"),
                    port = getInt("mysql.port"),
                    database = getString("mysql.database"),
                    user = getString("mysql.user"),
                    password = getString("mysql.password"),
                    parameter = getString("mysql.parameter"),
                    filters = getString("mysql.filters"),
                    maxActive = getInt("mysql.maxActive"),
                    initialSize = getInt("mysql.initialSize"),
                    maxWait = getLong("mysql.maxWait"),
                    minIdle = getInt("mysql.minIdle"),
                    timeBetweenEvictionRunsMillis = getLong("mysql.timeBetweenEvictionRunsMillis"),
                    minEvictableIdleTimeMillis = getLong("mysql.minEvictableIdleTimeMillis"),
                    validationQuery = getString("mysql.validationQuery"),
                    testWhileIdle = getBoolean("mysql.testWhileIdle"),
                    testOnBorrow = getBoolean("mysql.testOnBorrow"),
                    testOnReturn = getBoolean("mysql.testOnReturn"),
                    poolPreparedStatements = getBoolean("mysql.poolPreparedStatements"),
                    maxOpenPreparedStatements = getInt("mysql.maxOpenPreparedStatements"),
                    removeAbandoned = getBoolean("mysql.removeAbandoned"),
                    removeAbandonedTimeout = getInt("mysql.removeAbandonedTimeout"),
                    logAbandoned = getBoolean("mysql.logAbandoned"),
                    asyncInit = getBoolean("mysql.asyncInit")
                ).let {
                    loggerAdapter.info("Mybatis 链接成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
                    it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loggerAdapter.info("无法链接数据库! 请确认数据库开启，并且 WcpeLib/mysql.yml 配置文件中的数据配置填写正确!")
            }
        }
        null
    }

    val redis: Redis? = redisConfigAdapter.run {
        if (getBoolean("redis.enable")) {
            loggerAdapter.info("Redis 开启! 开始链接!")
            val start = System.currentTimeMillis()
            try {
                return@run Redis(
                    getString("redis.url"),
                    getInt("redis.port"),
                    getInt("redis.time-out"),
                    getString("redis.password")
                        .let { password -> return@let password.ifEmpty { null } },
                    getInt("redis.max-total"),
                    getInt("redis.max-idle"),
                    getInt("redis.min-idle"),
                    getBoolean("redis.jmx-enabled"),
                    getBoolean("redis.test-on-create"),
                    getBoolean("redis.block-when-exhausted"),
                    getInt("redis.max-wait-millis"),
                    getBoolean("redis.test-on-borrow"),
                    getBoolean("redis.test-on-return")
                ).let {
                    loggerAdapter.info("Redis 链接成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
                    loggerAdapter.info("host-> ${getString("redis.url")} port-> ${getInt("redis.port")}")
                    it
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loggerAdapter.info("无法链接 Redis ! 请确认 Redis 开启, 并且 WcpeLib/redis.yml 配置文件中的 Redis 配置填写正确!")
            }
        }
        null
    }

    val ktor: Ktor = ktorConfigAdapter.run {
        Ktor(getInt("ktor.port"))
    }

}