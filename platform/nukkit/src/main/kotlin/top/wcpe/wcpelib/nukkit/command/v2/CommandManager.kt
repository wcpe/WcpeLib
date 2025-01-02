package top.wcpe.wcpelib.nukkit.command.v2

import cn.nukkit.Server
import cn.nukkit.command.SimpleCommandMap
import cn.nukkit.plugin.PluginBase
import top.wcpe.wcpelib.common.command.v2.AbstractCommand

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


    @JvmStatic
    fun registerCommand(abstractCommand: AbstractCommand, instance: PluginBase): Boolean {
        val nukkitCommand = NukkitCommand(abstractCommand)
        commandMap.unregister(nukkitCommand)
        return commandMap.register(instance.name, nukkitCommand)
    }
}