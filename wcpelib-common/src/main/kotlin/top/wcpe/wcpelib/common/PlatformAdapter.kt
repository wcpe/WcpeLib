package top.wcpe.wcpelib.common

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.common.command.v2.AbstractCommand
import java.io.File

/**
 * 由 WCPE 在 2023/7/10 13:40 创建
 * <p>
 * Created by WCPE on 2023/7/10 13:40
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
interface PlatformAdapter {
    fun reloadAllConfig(): Boolean
    fun createLoggerAdapter(): LoggerAdapter
    fun getDataFolderFile(): File
    fun createConfigAdapter(fileName: String): ConfigAdapter

    /**
     * 注册命令
     */
    fun registerCommand(abstractCommand: AbstractCommand, pluginInstance: Any): Boolean

}