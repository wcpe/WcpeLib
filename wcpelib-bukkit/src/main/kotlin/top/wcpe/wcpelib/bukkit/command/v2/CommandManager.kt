package top.wcpe.wcpelib.bukkit.command.v2

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.wcpelib.bukkit.WcpeLib
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
    private var KNOWN_COMMANDS_FIELD: Field

    init {
        try {
            COMMAND_MAP_FIELD = SimplePluginManager::class.java.getDeclaredField("commandMap")
            COMMAND_MAP_FIELD.isAccessible = true

            KNOWN_COMMANDS_FIELD = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            KNOWN_COMMANDS_FIELD.isAccessible = true
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

    private fun getKnownCommands(commandMap: CommandMap): MutableMap<String, Command>? {
        val any = KNOWN_COMMANDS_FIELD[commandMap]
        if (any !is Map<*, *>) {
            return null
        }
        val map = mutableMapOf<String, Command>()
        for ((key, value) in any) {
            if (key !is String) continue
            if (value !is Command) continue
            map[key.toString()] = value
        }
        KNOWN_COMMANDS_FIELD[commandMap] = map
        return try {
            map
        } catch (e: Exception) {
            throw RuntimeException("Could not get knownCommands", e)
        }
    }

    private val logger = WcpeLib.getInstance().logger
    private val bukkitCommandMap = mutableMapOf<String, BukkitCommand>()

    @JvmStatic
    fun registerCommand(abstractCommand: AbstractCommand, instance: JavaPlugin): Boolean {
        val commandMap = getCommandMap(Bukkit.getServer()) ?: return false
        val bukkitCommand = BukkitCommand(abstractCommand)
        val knownCommands = getKnownCommands(commandMap) ?: return false
        val commandName = abstractCommand.name

        val coverBukkitCommand = bukkitCommandMap[commandName]
        if (coverBukkitCommand != null) {
            logger.info("检测到已注册的指令: ${coverBukkitCommand.name}")
            if (unregisterCommand(coverBukkitCommand, instance)) {
                logger.info("注销指令: ${coverBukkitCommand.name} 成功!")
            } else {
                logger.info("注销指令: ${coverBukkitCommand.name} 失败! 您的指令可能并不会生效")
            }
        }

        knownCommands[commandName] = bukkitCommand
        knownCommands[commandName.lowercase()] = bukkitCommand
        knownCommands["${instance.name.lowercase()}:$commandName"] = bukkitCommand
        for (alias in abstractCommand.aliases) {
            knownCommands[alias] = bukkitCommand
            knownCommands["${commandName.lowercase()}:$alias"] = bukkitCommand
        }
        bukkitCommandMap[commandName] = bukkitCommand
        return bukkitCommand.register(commandMap)
    }

    private fun unregisterCommand(bukkitCommand: BukkitCommand, instance: JavaPlugin): Boolean {
        val commandMap = getCommandMap(Bukkit.getServer()) ?: return false
        val knownCommands = getKnownCommands(commandMap) ?: return false
        val commandName = bukkitCommand.name
        knownCommands.remove(commandName)
        knownCommands.remove(commandName.lowercase())
        knownCommands.remove("${instance.name.lowercase()}:$commandName")
        for (alias in bukkitCommand.aliases) {
            knownCommands.remove(alias)
            knownCommands.remove("${commandName.lowercase()}:$alias")
        }
        bukkitCommandMap.remove(commandName)
        return bukkitCommand.unregister(commandMap)
    }


}