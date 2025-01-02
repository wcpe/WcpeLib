package top.wcpe.wcpelib.bungeecord.command.v2

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import top.wcpe.wcpelib.common.command.v2.AbstractCommand

/**
 * 由 WCPE 在 2023/7/24 14:24 创建
 * <p>
 * Created by WCPE on 2023/7/24 14:24
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class BungeeCordCommand(private val abstractCommand: AbstractCommand) : Command(
    abstractCommand.name,
    abstractCommand.permission,
    *abstractCommand.aliases.toTypedArray()
), TabExecutor {
    init {
        permissionMessage = abstractCommand.permission
    }

    override fun execute(commandSender: CommandSender, args: Array<String?>) {
        abstractCommand.handleExecute(BungeeCordCommandSender(commandSender), args)
    }

    override fun onTabComplete(commandSender: CommandSender, args: Array<String>): Iterable<String> {
        return abstractCommand.handleTabComplete(BungeeCordCommandSender(commandSender), args)
    }
}