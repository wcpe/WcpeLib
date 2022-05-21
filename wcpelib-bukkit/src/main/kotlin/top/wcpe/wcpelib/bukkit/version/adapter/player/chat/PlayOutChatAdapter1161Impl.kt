package top.wcpe.wcpelib.bukkit.version.adapter.player.chat

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionInfo
import top.wcpe.wcpelib.bukkit.version.adapter.player.PlayerNmsManager
import java.util.UUID

/**
 * 由 WCPE 在 2022/4/3 20:19 创建
 *
 * Created by WCPE on 2022/4/3 20:19
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
class PlayOutChatAdapter1161Impl(versionInfo: VersionInfo) : PlayOutChatAdapter(versionInfo) {
    override fun sendAction(player: Player, message: String) {
        val chatComponentText = versionInfo.getNmsClass("ChatComponentText").getConstructor(String::class.java)
            .newInstance(ChatColor.translateAlternateColorCodes('&', message))

        val chatMessageType = versionInfo.getNmsClass("ChatMessageType").getField("GAME_INFO").get(null)
        val packetPlayOutChat = versionInfo.getNmsClass("PacketPlayOutChat").getConstructor(
            versionInfo.getNmsClass("IChatBaseComponent"),
            versionInfo.getNmsClass("ChatMessageType"), UUID::class.java
        ).newInstance(chatComponentText, chatMessageType, player.uniqueId)
        PlayerNmsManager.sendPacket(player, packetPlayOutChat)
    }
}