package top.wcpe.wcpelib.nukkit.command.v2

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import top.wcpe.wcpelib.common.command.v2.AbstractCommand

/**
 * 由 WCPE 在 2023/7/24 17:26 创建
 * <p>
 * Created by WCPE on 2023/7/24 17:26
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */

class NukkitCommand(private val abstractCommand: AbstractCommand) : Command(
    abstractCommand.name,
    abstractCommand.description,
    abstractCommand.usageMessage,
    abstractCommand.aliases.toTypedArray()
) {
    init {
        permissionMessage = abstractCommand.permission
    }

    override fun execute(commandSender: CommandSender, commandLabel: String, args: Array<String?>): Boolean {
        abstractCommand.handleExecute(NukkitCommandSender(commandSender), args)
        return false
    }

    override fun tabComplete(commandSender: CommandSender, alias: String, args: Array<String>): List<String> {
        return abstractCommand.handleTabComplete(NukkitCommandSender(commandSender), args)
    }

}