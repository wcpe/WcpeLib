package top.wcpe.wcpelib.nukkit.extend.stringaction

import cn.nukkit.Player
import top.wcpe.wcpelib.nukkit.utils.StringActionUtil

/**
 * 由 WCPE 在 2022/6/2 15:46 创建
 *
 * Created by WCPE on 2022/6/2 15:46
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-2
 */
fun String.executionCommands(parserPlaceholderApi: Boolean = false, player: Player) {
    StringActionUtil.executionCommands(this, parserPlaceholderApi, player)
}

fun List<String>.executionCommands(parserPlaceholderApi: Boolean = false, player: Player) {
    StringActionUtil.executionCommands(this, parserPlaceholderApi, player)
}