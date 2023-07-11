package top.wcpe.wcpelib.bukkit

import top.wcpe.wcpelib.bukkit.command.CommandPlus
import top.wcpe.wcpelib.bukkit.command.entity.Command
import top.wcpe.wcpelib.common.WcpeLibCommon

/**
 * 由 WCPE 在 2022/7/14 4:37 创建
 *
 * Created by WCPE on 2022/7/14 4:37
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.16-alpha-dev-1
 */
class WcpeLibCommands {

    private val logger = WcpeLib.getInstance().logger

    init {
        val cp = CommandPlus.Builder("WcpeLib", WcpeLib.getInstance()).aliases("wcpelib", "wclib", "wl").build()

        cp.registerSubCommand(
            Command.Builder("reload", "重载配置文件").executeComponent { sender, _ ->
                sender.sendMessage("开始重载所有配置文件!")
                WcpeLib.getInstance().run {
                    reloadConfig()
                }
                WcpeLibCommon.reload()
                sender.sendMessage("重载完成")
                logger.info("${sender.name}重载了配置文件")
            }.build()
        )
        cp.registerThis()

    }
}