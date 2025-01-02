package top.wcpe.wcpelib.bungeecord.command

import top.wcpe.wcpelib.common.command.Command

/**
 * 由 WCPE 在 2022/9/27 22:44 创建
 *
 * Created by WCPE on 2022/9/27 22:44
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
@Deprecated("replace common command v2")
interface BungeeCordCommand : Command {
    val tabCompleter: TabCompleter?
}