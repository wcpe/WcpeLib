package top.wcpe.wcpelib.bukkit.command

import org.bukkit.entity.Player
import top.wcpe.wcpelib.common.command.CommandSender

/**
 * 由 WCPE 在 2022/9/6 22:10 创建
 *
 * Created by WCPE on 2022/9/6 22:10
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-3
 */
class BukkitCommandSenderImpl(private val commandSender: org.bukkit.command.CommandSender) :
    CommandSender<org.bukkit.command.CommandSender> {

    override fun getAdapter(): org.bukkit.command.CommandSender {
        return commandSender
    }

    override fun isPlayer(): Boolean {
        return commandSender is Player
    }

    override fun sendMessage(message: String) {
        commandSender.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return commandSender.hasPermission(permission)
    }
}