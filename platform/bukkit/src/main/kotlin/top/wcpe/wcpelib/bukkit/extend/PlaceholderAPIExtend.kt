package top.wcpe.wcpelib.bukkit.extend

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.bukkit.extend.plugin.awaitSync
import top.wcpe.wcpelib.bukkit.hook.PlaceholderAPIHook

/**
 * 由 WCPE 在 2024/8/26 14:58 创建
 * <p>
 * Created by WCPE on 2024/8/26 14:58
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.8.0-SNAPSHOT
 */

/**
 * 替换字符串中的占位符
 *
 * @return 替换后的字符串
 */
fun String.setPlaceholders(): String {
    return PlaceholderAPIHook.setPlaceholders(null, this)
}

/**
 * 替换字符串中的占位符
 *
 * @param playerName 玩家名
 * @return 替换后的字符串
 */
fun String.setPlaceholders(playerName: String): String {
    val offlinePlayer = if (Bukkit.isPrimaryThread()) {
        Bukkit.getOfflinePlayer(playerName)
    } else {
        WcpeLib.instance.awaitSync {
            Bukkit.getOfflinePlayer(playerName)
        } ?: return this
    }
    return PlaceholderAPIHook.setPlaceholders(offlinePlayer, this)
}

/**
 * 替换字符串中的占位符
 *
 * @param player 玩家
 * @return 替换后的字符串
 */
fun String.setPlaceholders(player: Player?): String {
    if (player == null) {
        return this
    }
    return PlaceholderAPIHook.setPlaceholders(player, this)
}
