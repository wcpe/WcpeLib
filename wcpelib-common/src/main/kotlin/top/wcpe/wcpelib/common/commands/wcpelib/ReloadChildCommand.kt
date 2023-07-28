package top.wcpe.wcpelib.common.commands.wcpelib

import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.command.v2.ChildCommand
import top.wcpe.wcpelib.common.command.v2.CommandExecutor
import top.wcpe.wcpelib.common.command.v2.CommandSender
import top.wcpe.wcpelib.common.command.v2.ParentCommand

/**
 * 由 WCPE 在 2023/7/24 14:16 创建
 * <p>
 * Created by WCPE on 2023/7/24 14:16
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class ReloadChildCommand(parentCommand: ParentCommand) : ChildCommand(parentCommand, name = "reload", description = "重载 WcpeLib 中的 MySQL, Redis, Ktor, 语言文件等..."), CommandExecutor {
    override fun execute(commandSender: CommandSender<*>, args: Array<String?>) {
        WcpeLibCommon.reload()
        commandSender.sendMessage("重载完成!")
    }

}