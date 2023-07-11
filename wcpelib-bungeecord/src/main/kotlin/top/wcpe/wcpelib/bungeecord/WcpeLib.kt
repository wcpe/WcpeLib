package top.wcpe.wcpelib.bungeecord

import top.wcpe.wcpelib.bungeecord.adapter.ConfigAdapterBungeeCordImpl
import top.wcpe.wcpelib.bungeecord.adapter.LoggerAdapterBungeeCordImpl
import top.wcpe.wcpelib.common.PlatformAdapter
import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import java.io.File

/**
 * 由 WCPE 在 2022/9/12 11:06 创建
 *
 * Created by WCPE on 2022/9/12 11:06
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class WcpeLib : BungeeCordPlugin(), PlatformAdapter {

    companion object {
        @JvmStatic
        lateinit var instance: WcpeLib
            private set

        @Deprecated("replace object class...")
        @JvmStatic
        val wcpeLibCommon: WcpeLibCommon = WcpeLibCommon
    }

    override fun onLoad() {
        instance = this
        WcpeLibCommon.init(this)
    }

    override fun onEnable() {
        saveDefaultConfig()
        WcpeLibCommands()
    }

    private fun createConfigAdapter(fileName: String): ConfigAdapter {
        return ConfigAdapterBungeeCordImpl(File(dataFolder, fileName), fileName)
    }

    override fun createLoggerAdapter(): LoggerAdapter {
        return LoggerAdapterBungeeCordImpl()
    }

    override fun createMySQLConfigAdapter(): ConfigAdapter {
        return createConfigAdapter("mysql.yml")
    }

    override fun createRedisConfigAdapter(): ConfigAdapter {
        return createConfigAdapter("redis.yml")
    }

    override fun createKtorConfigAdapter(): ConfigAdapter {
        return createConfigAdapter("ktor.yml")
    }

}