package top.wcpe.wcpelib.bukkit.extend.player

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.adapter.player.PlayerNmsManager

/**
 * 由 WCPE 在 2022/4/3 22:59 创建
 *
 * Created by WCPE on 2022/4/3 22:59
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
fun Player.sendAction(player: Player, message: String) {
    PlayerNmsManager.playOutChatAdapter.sendAction(player, message)
}