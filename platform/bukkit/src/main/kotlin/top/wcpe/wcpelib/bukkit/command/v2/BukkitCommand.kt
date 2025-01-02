package top.wcpe.wcpelib.bukkit.command.v2

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin
import top.wcpe.wcpelib.common.command.v2.AbstractCommand

/**
 * 由 WCPE 在 2023/7/21 10:32 创建
 * <p>
 * Created by WCPE on 2023/7/21 10:32
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class BukkitCommand(private val abstractCommand: AbstractCommand, private val plugin: Plugin) : Command(
    abstractCommand.name,
    abstractCommand.description,
    abstractCommand.usageMessage,
    abstractCommand.aliases
), PluginIdentifiableCommand {
    init {
        permissionMessage = abstractCommand.permission
    }

    override fun execute(commandSender: CommandSender, commandLabel: String, args: Array<String?>): Boolean {
        if (args.isNotEmpty()) {
            val arg0 = args[0]
            if (arg0 == "silent") {
                abstractCommand.handleExecute(SilentBukkitCommandSender(commandSender), args.drop(1).toTypedArray())
                return false
            }
        }
        abstractCommand.handleExecute(BukkitCommandSender(commandSender), args)
        return false
    }

    override fun tabComplete(commandSender: CommandSender, alias: String, args: Array<String>): List<String> {
        return abstractCommand.handleTabComplete(BukkitCommandSender(commandSender), args)
    }

    override fun getPlugin(): Plugin {
        return plugin
    }

}