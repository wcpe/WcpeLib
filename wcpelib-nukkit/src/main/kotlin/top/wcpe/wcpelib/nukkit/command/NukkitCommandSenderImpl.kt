package top.wcpe.wcpelib.nukkit.command

import cn.nukkit.Player
import top.wcpe.wcpelib.common.command.CommandSender

/**
 * 由 WCPE 在 2022/9/7 19:56 创建
 *
 * Created by WCPE on 2022/9/7 19:56
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
class NukkitCommandSenderImpl(private val commandSender: cn.nukkit.command.CommandSender) :
    CommandSender<cn.nukkit.command.CommandSender> {

    override fun getAdapter(): cn.nukkit.command.CommandSender {
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