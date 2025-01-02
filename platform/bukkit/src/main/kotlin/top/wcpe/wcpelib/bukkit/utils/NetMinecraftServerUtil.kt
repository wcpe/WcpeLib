package top.wcpe.wcpelib.bukkit.utils

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionManager.versionInfo
import top.wcpe.wcpelib.bukkit.version.adapter.player.PlayerNmsManager

/**
 * 由 WCPE 在 2022/4/1 20:39 创建
 *
 * Created by WCPE on 2022/4/1 20:39
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-4
 */
object NetMinecraftServerUtil {

    @Deprecated(
        "", ReplaceWith(
            "versionInfo.versionNumber",
            "top.wcpe.wcpelib.bukkit.version.VersionManager.versionInfo"
        )
    )
    @JvmStatic
    fun getServerVersionNum(): Int {
        return versionInfo.versionNumber
    }


    @JvmStatic
    fun sendAction(player: Player, message: String) {
        PlayerNmsManager.playOutChatAdapter.sendAction(player, message)
    }

}