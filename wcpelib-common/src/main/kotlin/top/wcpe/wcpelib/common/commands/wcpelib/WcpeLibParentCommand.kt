package top.wcpe.wcpelib.common.commands.wcpelib

import top.wcpe.wcpelib.common.command.v2.ParentCommand

/**
 * 由 WCPE 在 2023/7/24 14:05 创建
 * <p>
 * Created by WCPE on 2023/7/24 14:05
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
class WcpeLibParentCommand : ParentCommand("wcpeLib", "WcpeLib Commands", aliases = listOf("wl")) {
    init {
        addChildCommand(ReloadChildCommand(this))
    }
}