package top.wcpe.wcpelib.common.command

/**
 * 由 WCPE 在 2022/9/6 19:24 创建
 *
 * Created by WCPE on 2022/9/6 19:24
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-3
 */
@FunctionalInterface
fun interface CommandExecute {
    fun execute(sender: CommandSender<*>, args: Array<String?>)
}