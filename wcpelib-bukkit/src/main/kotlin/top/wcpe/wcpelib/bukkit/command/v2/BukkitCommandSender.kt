package top.wcpe.wcpelib.bukkit.command.v2

import org.bukkit.entity.Player
import top.wcpe.wcpelib.common.command.v2.CommandSender

/**
 * 由 WCPE 在 2023/7/22 10:23 创建
 * <p>
 * Created by WCPE on 2023/7/22 10:23
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@JvmInline
value class BukkitCommandSender(private val commandSender: org.bukkit.command.CommandSender) :
    CommandSender<org.bukkit.command.CommandSender> {
    override fun getCommandSender(): org.bukkit.command.CommandSender {
        return commandSender
    }

    override fun getName(): String {
        return commandSender.name
    }

    override fun isPlayer(): Boolean {
        return commandSender is Player
    }

    override fun isOp(): Boolean {
        return commandSender.isOp
    }

    override fun sendMessage(message: String) {
        commandSender.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return commandSender.hasPermission(permission)
    }
}