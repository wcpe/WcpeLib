package top.wcpe.wcpelib.bukkit.version.adapter.player.chat

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionInfo

/**
 * 由 WCPE 在 2022/4/1 20:53 创建
 *
 * Created by WCPE on 2022/4/1 20:53
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
abstract class PlayOutChatAdapter(val versionInfo: VersionInfo) {
    abstract fun sendAction(player: Player, message: String)
}