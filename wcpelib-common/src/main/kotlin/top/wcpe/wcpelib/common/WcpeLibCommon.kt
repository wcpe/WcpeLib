package top.wcpe.wcpelib.common

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.command.v2.CommandManager
import top.wcpe.wcpelib.common.commands.WcpeLibCommands
import top.wcpe.wcpelib.common.ktor.Ktor
import top.wcpe.wcpelib.common.mail.Mail
import top.wcpe.wcpelib.common.mail.MailConfig
import top.wcpe.wcpelib.common.mybatis.Mybatis
import top.wcpe.wcpelib.common.mybatis.MybatisInstance
import top.wcpe.wcpelib.common.mysql.MySQL
import top.wcpe.wcpelib.common.mysql.MySQLDataSourceConfig
import top.wcpe.wcpelib.common.mysql.MySQLDataSourceInstance
import top.wcpe.wcpelib.common.redis.Redis
import top.wcpe.wcpelib.common.redis.RedisConfig
import top.wcpe.wcpelib.common.redisson.Redisson
import top.wcpe.wcpelib.common.redisson.RedissonInstance

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
    private var mailConfigAdapter: ConfigAdapter? = null

    fun init(platformAdapter: PlatformAdapter) {
        this.platformAdapter = platformAdapter
        loggerAdapter = platformAdapter.createLoggerAdapter()
        loggerAdapter.info("开始初始化 WcpeLibCommon...")
        mysqlConfigAdapter = platformAdapter.createConfigAdapter("mysql.yml")
        redisConfigAdapter = platformAdapter.createConfigAdapter("redis.yml")
        ktorConfigAdapter = platformAdapter.createConfigAdapter("ktor.yml")
        mailConfigAdapter = platformAdapter.createConfigAdapter("mail.yml")
        messageConfigAdapter = platformAdapter.createConfigAdapter("message.yml")
        createCompose()

        CommandManager.registerCommand(WcpeLibCommands::class.java, platformAdapter)

        loggerAdapter.info("初始化 WcpeLibCommon 完成...")
    }

    fun reloadOnlyConfig() {
        platformAdapter?.reloadAllConfig()
        mysqlConfigAdapter?.reloadConfig()
        redisConfigAdapter?.reloadConfig()
        ktorConfigAdapter?.reloadConfig()
        mailConfigAdapter?.reloadConfig()
        messageConfigAdapter?.reloadConfig()
    }

    fun reload() {
        reloadOnlyConfig()
        createCompose()
    }

    var mysql: MySQL? = null
    var mybatis: Mybatis? = null
    var redis: Redis? = null
    var redisson: Redisson? = null
    var ktor: Ktor? = null
    var mail: Mail? = null

    private fun createCompose() {
        createMySQL()
        createMyBatis()
        createRedis()
        createRedisson()
        createKtor()
        createMail()
    }

    private fun createMySQL() {
        val mysqlDataSourceInstance = createMySQLDataSourceConfig() ?: return
        try {
            mysql = MySQL.init(mysqlDataSourceInstance)
            loggerAdapter.info("MySQL 链接成功!")
        } catch (e: Exception) {
            loggerAdapter.info("无法链接数据库! 请确认数据库开启，并且 WcpeLib/mysql.yml 配置文件中的数据配置填写正确!")
            e.printStackTrace()
        }
    }

    private fun createMySQLDataSourceConfig(): MySQLDataSourceInstance? {
        val configAdapter = mysqlConfigAdapter ?: return null
        if (!configAdapter.getBoolean("mysql.enable")) {
            loggerAdapter.info("MySQL 未开启! 无法连接数据库!")
            return null
        }
        val load = MySQLDataSourceConfig.load(configAdapter)

        return try {
            MySQLDataSourceInstance(load, load.build())
        } catch (e: Exception) {
            loggerAdapter.info("链接数据源出现错误!")
            e.printStackTrace()
            null
        }
    }

    private fun createMyBatis() {
        val currentMySQL = mysql
        if (currentMySQL == null) {
            loggerAdapter.info("MySQL 无法连接, Mybatis 客户端创建失败!")
            return
        }
        loggerAdapter.info("Mybatis 开启! 开始连接数据库!")
        val start = System.currentTimeMillis()
        try {
            val mybatisInstance = MybatisInstance(currentMySQL.mysqlDataSourceInstance)
            mybatis = Mybatis.init(mybatisInstance)
            loggerAdapter.info("Mybatis 客户端创建成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
        } catch (e: Exception) {
            loggerAdapter.info("创建 Mybatis 客户端出现错误! 请确认数据库开启，并且 WcpeLib/mysql.yml 配置文件中的数据配置填写正确!")
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
            redis = Redis.init(RedisConfig.load(configAdapter))
            loggerAdapter.info("Redis 链接成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
        } catch (e: Exception) {
            loggerAdapter.info("无法链接 Redis ! 请确认 Redis 开启, 并且 WcpeLib/redis.yml 配置文件中的 Redis 配置填写正确!")
            e.printStackTrace()
        }
    }


    private fun createRedisson() {
        val currentRedis = redis
        if (currentRedis == null) {
            loggerAdapter.info("Redis 无法连接, Redisson 客户端创建失败!")
            return
        }
        loggerAdapter.info("Redisson 开启! 开始连接!")
        val start = System.currentTimeMillis()
        try {
            val redissonInstance = RedissonInstance(currentRedis.redisConfig)
            redisson = Redisson.init(redissonInstance)
            loggerAdapter.info("Redisson 客户端创建成功! 共耗时:${(System.currentTimeMillis() - start)}Ms")
        } catch (e: Exception) {
            loggerAdapter.info("创建 Redisson 客户端出现错误! 请确认 Redis 开启，并且 WcpeLib/redis.yml  配置文件中的 Redis 配置填写正确!")
            e.printStackTrace()
        }
    }


    private fun createKtor() {
        val configAdapter = ktorConfigAdapter ?: return
        ktor = Ktor(configAdapter.getInt("ktor.port"))
    }

    private fun createMail() {
        val configAdapter = mailConfigAdapter ?: return
        val mailSection = configAdapter.getSection("mail") ?: return
        loggerAdapter.info("Mail 开启! 开始创建邮件配置文件!")
        val mailConfig = MailConfig.load(mailSection) ?: return
        loggerAdapter.info("创建邮件配置文件成功!")
        mail = Mail(mailConfig)
    }

}