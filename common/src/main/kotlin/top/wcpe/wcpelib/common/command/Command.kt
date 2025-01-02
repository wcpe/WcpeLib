package top.wcpe.wcpelib.common.command

/**
 * 由 WCPE 在 2022/9/6 19:21 创建
 *
 * Created by WCPE on 2022/9/6 19:21
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-3
 */
interface Command {
    val name: String
    val describe: String
    val args: List<CommandArgument>
    val hideNoPermissionHelp: Boolean
    val permission: String?
    val noPermissionMessage: String
    val onlyPlayerUse: Boolean
    val noPlayerMessage: String
    val executeComponent: CommandExecute?
}