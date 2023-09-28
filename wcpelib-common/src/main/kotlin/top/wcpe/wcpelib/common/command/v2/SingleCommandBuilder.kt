package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.Message
import top.wcpe.wcpelib.common.command.v2.extend.singleCommand

/**
 * 由 WCPE 在 2023/7/27 22:05 创建
 * <p>
 * Created by WCPE on 2023/7/27 22:05
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class SingleCommandBuilder @JvmOverloads constructor(
    val name: String,
    val description: String = "",
    val aliases: List<String> = listOf(),
    val arguments: List<Argument>,
    val playerOnly: Boolean = false,
    playerOnlyMessage: String = "",
    val opOnly: Boolean = false,
    opOnlyMessage: String = "",
    usageMessage: String = "",
    permission: String = "",
    permissionMessage: String = "",
    val commandExecutor: CommandExecutor? = null,
    val tabCompleter: TabCompleter? = null
) {

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
                    "%command_name%" to name, "%arguments%" to arguments.joinToString(" ") {
                        if (it.required) {
                            Message.RequiredFormat.toLocalization("%command_name%" to it.name)
                        } else {
                            Message.OptionalFormat.toLocalization("%command_name%" to it.name)
                        }
                    }, "%argument_tip%" to Message.ArgumentTip.toLocalization()
                )
            }
        }

    val permission: String = permission
        get() {
            return field.ifEmpty {
                "$name.use"
            }
        }
    val permissionMessage: String = permissionMessage
        get() {
            return field.ifEmpty {
                Message.ShouldPermission.toLocalization("%permission%" to permission)
            }
        }

    fun build(): SingleCommand {
        return singleCommand(this)
    }
}