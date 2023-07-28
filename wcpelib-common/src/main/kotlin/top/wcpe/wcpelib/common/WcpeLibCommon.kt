package top.wcpe.wcpelib.common

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.commands.wcpelib.WcpeLibParentCommand
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
object WcpeLibCommon {

    var platformAdapter: PlatformAdapter? = null

    var messageConfigAdapter: ConfigAdapter? = null

    private var loggerAdapter: LoggerAdapter = object : LoggerAdapter {
        override fun info(msg: String) {
            println("[WcpeLibCommon] $msg")
        }
    }

    private var mysqlConfigAdapter: ConfigAdapter? = null
    private var redisConfigAdapter: ConfigAdapter? = null
    private var ktorConfigAdapter: ConfigAdapter? = null

    fun init(platformAdapter: PlatformAdapter) {
        this.platformAdapter = platformAdapter
        loggerAdapter.info("开始初始化 WcpeLibCommon...")
        loggerAdapter = platformAdapter.createLoggerAdapter()
        mysqlConfigAdapter = platformAdapter.createConfigAdapter("mysql.yml")
        redisConfigAdapter = platformAdapter.createConfigAdapter("redis.yml")
        ktorConfigAdapter = platformAdapter.createConfigAdapter("ktor.yml")
        messageConfigAdapter = platformAdapter.createConfigAdapter("message.yml")
        createCompose()

        WcpeLibParentCommand().register(platformAdapter)

        loggerAdapter.info("初始化 WcpeLibCommon 完成...")
    }

    fun reload() {
        mysqlConfigAdapter?.reloadConfig()
        redisConfigAdapter?.reloadConfig()
        ktorConfigAdapter?.reloadConfig()
        messageConfigAdapter?.reloadConfig()
        createCompose()
    }

    var mybatis: Mybatis? = null
    var redis: Redis? = null
    var ktor: Ktor? = null

    private fun createCompose() {
        createMyBatis()
        createRedis()
        createKtor()
    }

    private fun createMyBatis() {
        val configAdapter = mysqlConfigAdapter ?: return
        if (!configAdapter.getBoolean("mysql.enable")) {
            loggerAdapter.info("Mybatis 未开启! 无法连接数据库!")
            return
        }
        loggerAdapter.info("Mybatis 开启! 开始连接数据库!")
        val start = System.currentTimeMillis()
        try {
            mybatis = Mybatis(
                url = configAdapter.getString("mysql.url"),
                port = configAdapter.getInt("mysql.port"),
                database = configAdapter.getString("mysql.database"),
                user = configAdapter.getString("mysql.user"),
                password = configAdapter.getString("mysql.password"),
                parameter = configAdapter.getString("mysql.parameter"),
                filters = configAdapter.getString("mysql.filters"),
                maxActive = configAdapter.getInt("mysql.maxActive"),
                initialSize = configAdapter.getInt("mysql.initialSize"),
                maxWait = configAdapter.getLong("mysql.maxWait"),
                minIdle = configAdapter.getInt("mysql.minIdle"),
                timeBetweenEvictionRunsMillis = configAdapter.getLong("mysql.timeBetweenEvictionRunsMillis"),
                minEvictableIdleTimeMillis = configAdapter.getLong("mysql.minEvictableIdleTimeMillis"),
                validationQuery = configAdapter.getString("mysql.validationQuery"),
                testWhileIdle = configAdapter.getBoolean("mysql.testWhileIdle"),
                testOnBorrow = configAdapter.getBoolean("mysql.testOnBorrow"),
                testOnReturn = configAdapter.getBoolean("mysql.testOnReturn"),
                poolPreparedStatements = configAdapter.getBoolean("mysql.poolPreparedStatements"),
                maxOpenPreparedStatements = configAdapter.getInt("mysql.maxOpenPreparedStatements"),
                removeAbandoned = configAdapter.getBoolean("mysql.removeAbandoned"),
                removeAbandonedTimeout = configAdapter.getInt("mysql.removeAbandonedTimeout"),
                logAbandoned = configAdapter.getBoolean("mysql.logAbandoned"),
                asyncInit = configAdapter.getBoolean("mysql.asyncInit")
            )
            loggerAdapter.info("Mybatis 链接成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
        } catch (e: Exception) {
            loggerAdapter.info("无法链接数据库! 请确认数据库开启，并且 WcpeLib/mysql.yml 配置文件中的数据配置填写正确!")
            e.printStackTrace()
        }
    }

    private fun createRedis() {
        redis?.close()
        val configAdapter = redisConfigAdapter ?: return
        if (!configAdapter.getBoolean("redis.enable")) {
            return
        }
        loggerAdapter.info("Redis 开启! 开始链接!")
        val start = System.currentTimeMillis()
        try {
            val url = configAdapter.getString("redis.url")
            val port = configAdapter.getInt("redis.port")
            redis = Redis(
                url,
                port,
                configAdapter.getInt("redis.time-out"),
                configAdapter.getString("redis.password").ifEmpty { null },
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
            loggerAdapter.info("Redis 链接成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
            loggerAdapter.info("host-> $url port-> $port")
        } catch (e: Exception) {
            loggerAdapter.info("无法链接 Redis ! 请确认 Redis 开启, 并且 WcpeLib/redis.yml 配置文件中的 Redis 配置填写正确!")
            e.printStackTrace()
        }
    }

    private fun createKtor() {
        val configAdapter = ktorConfigAdapter ?: return
        ktor = Ktor(configAdapter.getInt("ktor.port"))
    }

    init {
        createMyBatis()
        createRedis()
        createKtor()
    }

}