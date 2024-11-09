package top.wcpe.wcpelib.bukkit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.wcpelib.bukkit.adapter.ConfigAdapterBukkitImpl
import top.wcpe.wcpelib.bukkit.adapter.LoggerAdapterBukkitImpl
import top.wcpe.wcpelib.bukkit.command.v2.CommandManager
import top.wcpe.wcpelib.bukkit.data.IDataManager
import top.wcpe.wcpelib.bukkit.data.impl.MySQLDataManager
import top.wcpe.wcpelib.bukkit.data.impl.NullDataManager
import top.wcpe.wcpelib.bukkit.hook.PlaceholderAPIHook
import top.wcpe.wcpelib.bukkit.version.VersionManager.versionInfo
import top.wcpe.wcpelib.common.PlatformAdapter
import top.wcpe.wcpelib.common.WcpeLibCommon.init
import top.wcpe.wcpelib.common.WcpeLibCommon.ktor
import top.wcpe.wcpelib.common.WcpeLibCommon.mybatis
import top.wcpe.wcpelib.common.WcpeLibCommon.redis
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.command.v2.AbstractCommand
import top.wcpe.wcpelib.common.ktor.Ktor
import top.wcpe.wcpelib.common.mybatis.Mybatis
import top.wcpe.wcpelib.common.redis.Redis
import java.io.File

/**
 * 由 WCPE 在 2023/7/30 13:52 创建
 * <p>
 * Created by WCPE on 2023/7/30 13:52
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.1
 */
class WcpeLib : JavaPlugin(), PlatformAdapter {
    companion object {
        @JvmStatic
        lateinit var instance: WcpeLib
            private set

        @JvmStatic
        lateinit var dataManager: IDataManager
            private set

        @JvmStatic
        @Deprecated("", ReplaceWith("mybatis != null", "top.wcpe.wcpelib.common.WcpeLibCommon.mybatis"))
        fun isEnableMysql(): Boolean {
            return mybatis != null
        }

        @JvmStatic
        @Deprecated("", ReplaceWith("redis != null", "top.wcpe.wcpelib.common.WcpeLibCommon.redis"))
        fun isEnableRedis(): Boolean {
            return redis != null
        }

        @JvmStatic
        @Deprecated("", ReplaceWith("ktor != null", "top.wcpe.wcpelib.common.WcpeLibCommon.ktor"))
        fun isEnableKtor(): Boolean {
            return ktor != null
        }

        @JvmStatic
        @Deprecated("", ReplaceWith("mybatis", "top.wcpe.wcpelib.common.WcpeLibCommon.mybatis"))
        fun getMybatis(): Mybatis? {
            return mybatis
        }

        @JvmStatic
        @Deprecated("", ReplaceWith("redis", "top.wcpe.wcpelib.common.WcpeLibCommon.redis"))
        fun getRedis(): Redis? {
            return redis
        }

        @JvmStatic
        @Deprecated("", ReplaceWith("ktor", "top.wcpe.wcpelib.common.WcpeLibCommon.ktor"))
        fun getKtor(): Ktor? {
            return ktor
        }

        @JvmStatic
        fun getServerName(): String {
            return instance.config.getString("server-name")
        }

        @JvmStatic
        fun getDisplayName(): String {
            return instance.config.getString("display-name")
        }

        @JvmStatic
        val pluginScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    }


    override fun onLoad() {
        instance = this
        init(this)
    }

    override fun onEnable() {
        val start = System.currentTimeMillis()
        initDefaultMapper()
        saveDefaultConfig()
        WcpeLibPlaceholder().register()
        server.pluginManager.registerEvents(WcpeLibListener(), this)
        logger.info("load time: ${System.currentTimeMillis() - start} ms")
        server.consoleSender.sendMessage("§a  _       __                          __     _     __  ")
        server.consoleSender.sendMessage("§a | |     / /  _____    ____   ___    / /    (_)   / /_ ")
        server.consoleSender.sendMessage("§a | | /| / /  / ___/   / __ \\ / _ \\  / /    / /   / __ \\")
        server.consoleSender.sendMessage("§a | |/ |/ /  / /__    / /_/ //  __/ / /___ / /   / /_/ /")
        server.consoleSender.sendMessage("§a |__/|__/   \\___/   / .___/ \\___/ /_____//_/   /_.___/ ")
        server.consoleSender.sendMessage("§a                   /_/                                 ")
        val versionInfo = versionInfo
        logger.info("load version: ${server.version}")
        logger.info("nms version: ${versionInfo.nmsClassPath}")
        logger.info("obc version: ${versionInfo.obcClassPath}")
        logger.info("Hook PlaceholderAPI: ${PlaceholderAPIHook.getPlugin()}")
    }

    private fun initDefaultMapper() {
        val start = System.currentTimeMillis()
        logger.info("开始初始化默认 Mapper")
        val mybatis = mybatis
        if (mybatis == null) {
            dataManager = NullDataManager()
            logger.info("MySQL 未连接 Mybatis 初始化失败!")
            return
        }
        dataManager = MySQLDataManager(mybatis)
        logger.info("始化默认 Mapper 完成 耗时:${(System.currentTimeMillis() - start)} Ms")
    }

    override fun reloadAllConfig(): Boolean {
        return try {
            reloadConfig()
            PlaceholderAPIHook.getPlugin()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun createLoggerAdapter(): LoggerAdapter {
        return LoggerAdapterBukkitImpl()
    }

    override fun getDataFolderFile(): File {
        return dataFolder
    }

    override fun createConfigAdapter(fileName: String): ConfigAdapter {
        return ConfigAdapterBukkitImpl(File(dataFolder, fileName), fileName)
    }

    override fun registerCommand(abstractCommand: AbstractCommand, pluginInstance: Any): Boolean {
        return if (pluginInstance !is JavaPlugin) {
            false
        } else CommandManager.registerCommand(
            abstractCommand,
            pluginInstance
        )
    }

    override fun onDisable() {
        pluginScope.cancel()
    }
}