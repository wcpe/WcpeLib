package top.wcpe.wcpelib.common.command.v2

/**
 * 由 WCPE 在 2023/7/21 14:04 创建
 * <p>
 * Created by WCPE on 2023/7/21 14:04
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
abstract class ChildCommand @JvmOverloads constructor(
    parentCommand: ParentCommand,
    name: String,
    description: String,
    aliases: List<String> = listOf(),
    arguments: List<Argument> = listOf(),
    playerOnly: Boolean = false,
    playerOnlyMessage: String = "",
    usageMessage: String = "",
    permission: String = "",
    permissionMessage: String = "",
    childCommandBuilder: ChildCommandBuilder = ChildCommandBuilder(
        parentCommand,
        name,
        description,
        aliases,
        arguments,
        playerOnly,
        playerOnlyMessage,
        usageMessage,
        permission,
        permissionMessage
    ),
    val shouldDisplay: Boolean = false
) : AbstractCommand(
    name = childCommandBuilder.name,
    description = childCommandBuilder.description,
    permission = childCommandBuilder.permission,
    permissionMessage = childCommandBuilder.permissionMessage,
    aliases = childCommandBuilder.aliases,
    arguments = childCommandBuilder.arguments,
    usageMessage = childCommandBuilder.usageMessage,
    playerOnly = childCommandBuilder.playerOnly,
    playerOnlyMessage = childCommandBuilder.playerOnlyMessage
)