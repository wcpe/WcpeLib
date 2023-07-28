package top.wcpe.wcpelib.bungeecord.command

import top.wcpe.wcpelib.common.command.Command
import top.wcpe.wcpelib.common.command.CommandArgument
import top.wcpe.wcpelib.common.command.CommandExecute

/**
 * 由 WCPE 在 2022/9/12 13:58 创建
 *
 * Created by WCPE on 2022/9/12 13:58
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
@Deprecated("replace common command v2")
class Command(
    override val name: String,
    override val describe: String,
    override val args: List<CommandArgument>,
    override val hideNoPermissionHelp: Boolean,
    override val permission: String?,
    override val noPermissionMessage: String,
    override val onlyPlayerUse: Boolean,
    override val noPlayerMessage: String,
    override val executeComponent: CommandExecute?,
    override val tabCompleter: TabCompleter?
) : BungeeCordCommand {

    class Builder(private val name: String, private val describe: String) {
        private val args = mutableListOf<CommandArgument>()
        private var hideNoPermissionHelp = false
        private var permission: String? = null
        private var noPermissionMessage: String = "§c你莫得 §6%permission% §c权限!"
        private var onlyPlayerUse: Boolean = false
        private var noPlayerMessage: String = "§c该指令只能玩家使用!"
        private var executeComponent: CommandExecute? = null
        private var tabCompleter: TabCompleter? = null


        fun args(vararg args: CommandArgument): Builder {
            this.args.addAll(args)
            return this
        }

        fun hideNoPermissionHelp(hideNoPermissionHelp: Boolean): Builder {
            this.hideNoPermissionHelp = hideNoPermissionHelp
            return this
        }

        fun permission(permission: String?): Builder {
            this.permission = permission
            return this
        }

        fun noPermissionMessage(noPermissionMessage: String?): Builder {
            this.noPermissionMessage = noPermissionMessage!!
            return this
        }

        fun onlyPlayerUse(onlyPlayerUse: Boolean): Builder {
            this.onlyPlayerUse = onlyPlayerUse
            return this
        }

        fun noPlayerMessage(noPlayerMessage: String?): Builder {
            this.noPlayerMessage = noPlayerMessage!!
            return this
        }

        fun executeComponent(executeComponent: CommandExecute): Builder {
            this.executeComponent = executeComponent
            return this
        }

        fun tabCompleter(tabCompleter: TabCompleter): Builder {
            this.tabCompleter = tabCompleter
            return this
        }

        fun build(): Command {
            return Command(
                name,
                describe,
                args,
                hideNoPermissionHelp,
                permission,
                noPermissionMessage,
                onlyPlayerUse,
                noPlayerMessage,
                executeComponent,
                tabCompleter
            )
        }
    }
}