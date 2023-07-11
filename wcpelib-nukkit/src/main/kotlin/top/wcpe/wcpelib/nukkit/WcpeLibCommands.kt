package top.wcpe.wcpelib.nukkit

import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.nukkit.command.CommandPlus
import top.wcpe.wcpelib.nukkit.command.entity.Command

/**
 * 由 WCPE 在 2022/1/2 17:07 创建
 *
 * Created by WCPE on 2022/1/2 17:07
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
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
                    reloadOtherConfig()
                }
                WcpeLibCommon.reload()
                sender.sendMessage("重载完成")
                logger.info("${sender.name}重载了配置文件")
            }.build()
        )
        cp.registerThis()

    }
}