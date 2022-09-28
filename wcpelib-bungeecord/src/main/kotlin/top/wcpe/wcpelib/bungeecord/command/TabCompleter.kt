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
 */
@FunctionalInterface
fun interface TabCompleter {
    fun onTabComplete(sender: net.md_5.bungee.api.CommandSender, args: Array<String>): Iterable<String>
}
