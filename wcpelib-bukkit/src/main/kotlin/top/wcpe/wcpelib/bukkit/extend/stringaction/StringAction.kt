package top.wcpe.wcpelib.bukkit.extend.stringaction

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.tools.StringActionTool
import top.wcpe.wcpelib.bukkit.utils.StringActionUtil

/**
 * 由 WCPE 在 2022/6/2 15:41 创建
 *
 * Created by WCPE on 2022/6/2 15:41
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-2
 */
fun String.executionCommands(parserPlaceholderApi: Boolean, player: Player) {
    StringActionUtil.executionCommands(this, parserPlaceholderApi, player)
}

fun List<String>.executionCommands(parserPlaceholderApi: Boolean, player: Player) {
    StringActionUtil.executionCommands(this, parserPlaceholderApi, player)
}

fun List<String>.evalActions(parserPlaceholderApi: Boolean, player: Player) {
    StringActionTool.eval(this, parserPlaceholderApi, player)
}
