package top.wcpe.wcpelib.bukkit.extend

import org.bukkit.Bukkit
import org.bukkit.entity.Player
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

fun String.setPlaceholders(playerName: String): String {
    val offlinePlayer = Bukkit.getOfflinePlayer(playerName)
    return PlaceholderAPIHook.setPlaceholders(offlinePlayer, this)
}

fun String.setPlaceholders(player: Player?): String {
    if (player == null) {
        return this
    }
    return PlaceholderAPIHook.setPlaceholders(player, this)
}
