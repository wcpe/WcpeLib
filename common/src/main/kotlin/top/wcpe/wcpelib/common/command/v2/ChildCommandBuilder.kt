package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.Message
import top.wcpe.wcpelib.common.command.v2.extend.childCommand
import top.wcpe.wcpelib.common.command.v2.extend.simpleJoinToString

/**
 * 由 WCPE 在 2023/7/26 14:53 创建
 * <p>
 * Created by WCPE on 2023/7/26 14:53
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class ChildCommandBuilder @JvmOverloads constructor(
    private val parentCommand: ParentCommand,
    val name: String,
    description: String = "",
    val aliases: List<String> = listOf(),
    val arguments: List<Argument> = listOf(),
    val playerOnly: Boolean = false,
    playerOnlyMessage: String = "",
    val opOnly: Boolean = false,
    opOnlyMessage: String = "",
    usageMessage: String = "",
    permission: String = "",
    permissionMessage: String = "",
    val commandExecutor: CommandExecutor? = null,
    val tabCompleter: TabCompleter? = null,
) {

    val description: String = description
        get() {
            return field.ifEmpty {
                "$name Commands"
            }
        }

    val playerOnlyMessage: String = playerOnlyMessage
        get() {
            return field.ifEmpty {
                Message.PlayerOnly.toLocalization()
            }
        }

    val opOnlyMessage: String = opOnlyMessage
        get() {
            return field.ifEmpty {
                Message.OpOnly.toLocalization()
            }
        }

    val usageMessage: String = usageMessage
        get() {
            return field.ifEmpty {
                Message.UsageMessageFormat.toLocalization(
                    "%command_name%" to parentCommand.name,
                    "%arguments%" to "$name ${arguments.simpleJoinToString()}",
                    "%argument_tip%" to Message.ArgumentTip.toLocalization()
                )
            }
        }

    val permission: String = permission
        get() {
            return field.ifEmpty {
                "${parentCommand.name}.$name.use"
            }
        }
    val permissionMessage: String = permissionMessage
        get() {
            return field.ifEmpty {
                Message.ShouldPermission.toLocalization("%permission%" to permission)
            }
        }

    fun build(): ChildCommand {
        return parentCommand.childCommand(this)
    }
}