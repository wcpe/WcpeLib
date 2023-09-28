package top.wcpe.wcpelib.bungeecord.command.v2

import net.md_5.bungee.api.connection.ProxiedPlayer
import top.wcpe.wcpelib.common.command.v2.CommandSender

/**
 * 由 WCPE 在 2023/7/24 16:34 创建
 * <p>
 * Created by WCPE on 2023/7/24 16:34
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@JvmInline
value class BungeeCordCommandSender(private val commandSender: net.md_5.bungee.api.CommandSender) :
    CommandSender<net.md_5.bungee.api.CommandSender> {
    override fun getCommandSender(): net.md_5.bungee.api.CommandSender {
        return commandSender
    }

    override fun getName(): String {
        return commandSender.name
    }

    override fun isPlayer(): Boolean {
        return commandSender is ProxiedPlayer
    }

    override fun isOp(): Boolean {
        return false
    }

    override fun sendMessage(message: String) {
        commandSender.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return commandSender.hasPermission(permission)
    }
}