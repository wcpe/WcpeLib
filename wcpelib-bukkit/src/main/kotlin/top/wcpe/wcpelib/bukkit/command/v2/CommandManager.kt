package top.wcpe.wcpelib.bukkit.command.v2

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.wcpelib.common.command.v2.AbstractCommand
import java.lang.reflect.Field

/**
 * 由 WCPE 在 2023/7/21 16:24 创建
 * <p>
 * Created by WCPE on 2023/7/21 16:24
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
object CommandManager {
    private var COMMAND_MAP_FIELD: Field

    init {
        try {
            COMMAND_MAP_FIELD = SimplePluginManager::class.java.getDeclaredField("commandMap")
            COMMAND_MAP_FIELD.isAccessible = true
        } catch (e: NoSuchFieldException) {
            throw ExceptionInInitializerError(e)
        }
    }

    private fun getCommandMap(server: Server): CommandMap? {
        return try {
            COMMAND_MAP_FIELD[server.pluginManager] as CommandMap
        } catch (e: Exception) {
            throw RuntimeException("Could not get CommandMap", e)
        }
    }

    private val bukkitCommandMap = mutableMapOf<String, BukkitCommand>()

    @JvmStatic
    fun registerCommand(abstractCommand: AbstractCommand, instance: JavaPlugin): Boolean {
        val commandMap = getCommandMap(Bukkit.getServer()) ?: return false
        bukkitCommandMap[instance.name]?.unregister(commandMap)
        val bukkitCommand = BukkitCommand(abstractCommand)
        bukkitCommandMap[instance.name] = bukkitCommand
        return commandMap.register(instance.name, bukkitCommand)
    }
}