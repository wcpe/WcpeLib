package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.Message
import top.wcpe.wcpelib.common.command.v2.extend.parentCommand

/**
 * 由 WCPE 在 2023/7/26 14:20 创建
 * <p>
 * Created by WCPE on 2023/7/26 14:20
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class ParentCommandBuilder @JvmOverloads constructor(
    val name: String,
    description: String = "",
    val aliases: List<String> = listOf(),
    val playerOnly: Boolean = false,
    playerOnlyMessage: String = "",
    val opOnly: Boolean = false,
    opOnlyMessage: String = "",
    usageMessage: String = "",
    permission: String = "",
    permissionMessage: String = "",
) {

    val description: String = description
        get() {
            return field.ifEmpty {
                "$name Commands"
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
    val usageMessage: String = usageMessage
        get() {
            return field.ifEmpty {
                Message.UsageMessageFormat.toLocalization(
                    "%command_name%" to name, "%arguments%" to "[help]", "%argument_tip%" to ""
                )
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

    fun build(): ParentCommand {
        return parentCommand(this)
    }
}