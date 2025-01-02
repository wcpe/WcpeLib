package top.wcpe.wcpelib.common.command.v2

/**
 * 由 WCPE 在 2023/7/21 13:55 创建
 * <p>
 * Created by WCPE on 2023/7/21 13:55
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
abstract class SingleCommand @JvmOverloads constructor(
    name: String,
    description: String,
    aliases: List<String> = listOf(),
    arguments: List<Argument> = listOf(),
    playerOnly: Boolean = false,
    playerOnlyMessage: String = "",
    opOnly: Boolean = false,
    opOnlyMessage: String = "",
    usageMessage: String = "",
    permission: String = "",
    permissionMessage: String = "",
    singleCommandBuilder: SingleCommandBuilder = SingleCommandBuilder(
        name,
        description,
        aliases,
        arguments,
        playerOnly,
        playerOnlyMessage,
        opOnly,
        opOnlyMessage,
        usageMessage,
        permission,
        permissionMessage
    )
) : AbstractCommand(
    name = singleCommandBuilder.name,
    description = singleCommandBuilder.description,
    permission = singleCommandBuilder.permission,
    permissionMessage = singleCommandBuilder.permissionMessage,
    aliases = singleCommandBuilder.aliases,
    arguments = singleCommandBuilder.arguments,
    usageMessage = singleCommandBuilder.usageMessage,
    playerOnly = singleCommandBuilder.playerOnly,
    playerOnlyMessage = singleCommandBuilder.playerOnlyMessage,
    opOnly = singleCommandBuilder.opOnly,
    opOnlyMessage = singleCommandBuilder.opOnlyMessage
)
