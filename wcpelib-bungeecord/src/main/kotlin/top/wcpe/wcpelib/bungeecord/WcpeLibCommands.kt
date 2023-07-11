package top.wcpe.wcpelib.bungeecord

import top.wcpe.wcpelib.bungeecord.command.Command
import top.wcpe.wcpelib.bungeecord.command.CommandPlus
import top.wcpe.wcpelib.common.WcpeLibCommon

/**
 * 由 WCPE 在 2022/9/12 13:52 创建
 *
 * Created by WCPE on 2022/9/12 13:52
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class WcpeLibCommands {

    init {

        val cp = CommandPlus.Builder("WcpeLib", WcpeLib.instance).aliases("wcpelib", "wclib", "wl").build()
        cp.registerSubCommand(
            Command.Builder("reload", "").executeComponent { sender, _ ->
                sender.sendMessage("开始重载所有配置文件!")
                WcpeLib.instance.run {
                    reloadConfig()
                }
                WcpeLibCommon.reload()
                sender.sendMessage("重载完成")
            }.build()
        )
        cp.registerThis()

    }
}