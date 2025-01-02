package top.wcpe.wcpelib.bukkit.version.adapter.player.chat

import org.bukkit.entity.Player

/**
 * 由 WCPE 在 2022/4/1 20:53 创建
 *
 * Created by WCPE on 2022/4/1 20:53
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
interface PlayOutChatAdapter {
    fun sendAction(player: Player, message: String)
}