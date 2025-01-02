package top.wcpe.wcpelib.bungeecord

import net.md_5.bungee.api.plugin.Plugin
import top.wcpe.wcpelib.bungeecord.adapter.ConfigAdapterBungeeCordImpl
import top.wcpe.wcpelib.bungeecord.adapter.LoggerAdapterBungeeCordImpl
import top.wcpe.wcpelib.bungeecord.command.v2.BungeeCordCommand
import top.wcpe.wcpelib.common.PlatformAdapter
import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.command.v2.AbstractCommand
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
    }

    override fun reloadAllConfig(): Boolean {
        return try {
            reloadConfig()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun createLoggerAdapter(): LoggerAdapter {
        return LoggerAdapterBungeeCordImpl()
    }

    override fun getDataFolderFile(): File {
        return dataFolder
    }

    override fun createConfigAdapter(fileName: String): ConfigAdapter {
        return ConfigAdapterBungeeCordImpl(File(dataFolder, fileName), fileName)
    }


    override fun registerCommand(abstractCommand: AbstractCommand, pluginInstance: Any): Boolean {
        if (pluginInstance !is Plugin) {
            return false
        }
        pluginInstance.proxy.pluginManager.registerCommand(pluginInstance, BungeeCordCommand(abstractCommand))
        return true
    }


}