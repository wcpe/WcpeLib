package top.wcpe.wcpelib.common

/**
 * 由 WCPE 在 2023/7/22 16:03 创建
 * <p>
 * Created by WCPE on 2023/7/22 16:03
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
enum class Message(val path: String) {
    ShouldPermission("should-permission"),
    PlayerOnly("player-only"),
    OpOnly("op-only"),
    OptionalFormat("optional-format"),
    RequiredFormat("required-format"),
    UsageMessageFormat("usage-message-format"),
    AliasFormat("alias-format"),
    CommandHelpFormat("command-help-format"),
    CommandArgsError("command-args-error"),
    CommandHelpTop("command-help-top"),
    CommandHelpBottom("command-help-bottom"),
    ArgumentTip("argument-tip"),
    CommandNotExist("command-not-exist");

    fun toLocalization(vararg vars: Pair<String, Any>): String {
        val messageConfigAdapter = WcpeLibCommon.messageConfigAdapter ?: return ""
        var message = messageConfigAdapter.getStringList(path).joinToString("\n").ifEmpty {
            messageConfigAdapter.getString(path)
        }
        for (pair in vars) {
            message = message.replace(pair.first, "${pair.second}")
        }
        return message
    }
}