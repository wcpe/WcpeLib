package top.wcpe.wcpelib.nukkit.placeholder.data

import cn.nukkit.Player
import top.wcpe.wcpelib.nukkit.placeholder.PlaceholderExtend

/**
 * 由 WCPE 在 2022/2/11 18:57 创建
 *
 * Created by WCPE on 2022/2/11 18:57
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.9-alpha-dev-3
 */
class PlayerPlaceholderExtend : PlaceholderExtend() {

    override fun getAuthor(): String {
        return "WCPE"
    }

    override fun getIdentifier(): String {
        return "player"
    }

    override fun onPlaceholderRequest(player: Player, identifier: String): String {
        return when (identifier) {
            "name" -> player.name
            "worldName" -> player.level.name
            "health" -> "${player.health}"
            "maxHealth" -> "${player.maxHealth}"
            else -> ""
        }
    }
}