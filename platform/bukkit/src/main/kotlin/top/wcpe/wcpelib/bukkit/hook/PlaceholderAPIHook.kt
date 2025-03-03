package top.wcpe.wcpelib.bukkit.hook

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import top.wcpe.wcpelib.bukkit.WcpeLibPlaceholder

/**
 * 由 WCPE 在 2024/8/24 12:12 创建
 * <p>
 * Created by WCPE on 2024/8/24 12:12
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.8.0-SNAPSHOT
 */
object PlaceholderAPIHook {
    private var plugin: Plugin? = null

    @JvmStatic
    fun getPlugin(): Plugin? {
        plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")
        if (plugin != null) {
            WcpeLibPlaceholder().register()
        }
        return plugin
    }

    @JvmStatic
    fun setPlaceholders(player: Player, text: String): String {
        if (plugin == null) {
            return text
        }
        return PlaceholderAPI.setPlaceholders(player, text)
    }

    @JvmStatic
    fun setPlaceholders(player: OfflinePlayer?, text: String): String {
        if (plugin == null) {
            return text
        }
        return PlaceholderAPI.setPlaceholders(player, text)
    }
}