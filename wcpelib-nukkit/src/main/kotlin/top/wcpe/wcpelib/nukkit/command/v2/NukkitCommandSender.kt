package top.wcpe.wcpelib.nukkit.command.v2

import cn.nukkit.Player
import top.wcpe.wcpelib.common.command.v2.CommandSender

/**
 * 由 WCPE 在 2023/7/24 17:28 创建
 * <p>
 * Created by WCPE on 2023/7/24 17:28
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@JvmInline
value class NukkitCommandSender(private val commandSender: cn.nukkit.command.CommandSender) :
    CommandSender<cn.nukkit.command.CommandSender> {
    override fun getCommandSender(): cn.nukkit.command.CommandSender {
        return commandSender
    }

    override fun getName(): String {
        return commandSender.name
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