package top.wcpe.wcpelib.common.commands

import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.command.v2.CommandExecutor
import top.wcpe.wcpelib.common.command.v2.CommandSender
import top.wcpe.wcpelib.common.command.v2.annotation.ChildCommand
import top.wcpe.wcpelib.common.command.v2.annotation.ParentCommand

/**
 * 由 WCPE 在 2024/9/26 17:55 创建
 * <p>
 * Created by WCPE on 2024/9/26 17:55
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@ParentCommand("wcpelib", "WcpeLib Commands", aliases = ["wcpelib", "wcpe", "wlib"])
class WcpeLibCommands {

    @ChildCommand("reloadAll", aliases = ["rall"], description = "重载 WcpeLib 中的 MySQL, Redis, Ktor, 语言文件等...")
    class ReloadAllChildCommand : CommandExecutor {
        override fun execute(commandSender: CommandSender<*>, args: Array<String?>) {
            WcpeLibCommon.reload()
            commandSender.sendMessage("重载完成!")
        }
    }

    @ChildCommand("reload", aliases = ["r"], description = "重载 WcpeLib中的 语言文件等配置文件...")
    class ReloadChildCommand : CommandExecutor {
        override fun execute(commandSender: CommandSender<*>, args: Array<String?>) {
            WcpeLibCommon.reloadOnlyConfig()
            commandSender.sendMessage("重载完成!")
        }
    }

}