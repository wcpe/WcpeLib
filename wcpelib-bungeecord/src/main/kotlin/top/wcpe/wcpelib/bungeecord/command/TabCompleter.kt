package top.wcpe.wcpelib.bungeecord.command

/**
 * 由 WCPE 在 2022/9/27 22:46 创建
 *
 * Created by WCPE on 2022/9/27 22:46
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 * @deprecated This class is deprecated. Use {@link top.wcpe.wcpelib.common.command.v2.TabCompleter} instead.
 * For more information, see {@link top.wcpe.wcpelib.common.command.v2.TabCompleter}.
 */
@Deprecated("This class is deprecated. Use top.wcpe.wcpelib.common.command.v2.TabCompleter instead.")
@FunctionalInterface
fun interface TabCompleter {
    fun onTabComplete(sender: net.md_5.bungee.api.CommandSender, args: Array<String>): Iterable<String>
}
