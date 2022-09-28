package top.wcpe.wcpelib.bungeecord

import top.wcpe.wcpelib.bungeecord.adapter.ConfigAdapterBungeeCordImpl
import top.wcpe.wcpelib.bungeecord.adapter.LoggerAdapterBungeeCordImpl
import top.wcpe.wcpelib.common.WcpeLibCommon
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
class WcpeLib : BungeeCordPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: WcpeLib
            private set

        @JvmStatic
        lateinit var wcpeLibCommon: WcpeLibCommon
            private set
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        wcpeLibCommon = WcpeLibCommon(
            LoggerAdapterBungeeCordImpl(),
            ConfigAdapterBungeeCordImpl(File(dataFolder, "mysql.yml"), "mysql.yml"),
            ConfigAdapterBungeeCordImpl(File(dataFolder, "redis.yml"), "redis.yml"),
            ConfigAdapterBungeeCordImpl(File(dataFolder, "ktor.yml"), "ktor.yml")
        )
        WcpeLibCommands()
    }

}