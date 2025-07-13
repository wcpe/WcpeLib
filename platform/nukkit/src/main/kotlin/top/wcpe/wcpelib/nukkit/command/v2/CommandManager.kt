package top.wcpe.wcpelib.nukkit.command.v2

import cn.nukkit.Server
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.plugin.PluginBase
import top.wcpe.wcpelib.common.command.v2.AbstractCommand
import top.wcpe.wcpelib.nukkit.WcpeLib

/**
 * 由 WCPE 在 2023/7/24 17:32 创建
 * <p>
 * Created by WCPE on 2023/7/24 17:32
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
object CommandManager {
    private val commandMap: SimpleCommandMap = Server.getInstance().commandMap

    private val logger = WcpeLib.getInstance().logger

    @JvmStatic
    fun registerCommand(abstractCommand: AbstractCommand, instance: PluginBase): Boolean {
        val nukkitCommand = NukkitCommand(abstractCommand)
        try {
            commandMap.unregister(nukkitCommand)
        } catch (e: NoSuchMethodError) {
            logger.warning("卸载命令 ${abstractCommand.name} 失败: ${e.message}")
            return false
        }
        return commandMap.register(instance.name, nukkitCommand)
    }
}