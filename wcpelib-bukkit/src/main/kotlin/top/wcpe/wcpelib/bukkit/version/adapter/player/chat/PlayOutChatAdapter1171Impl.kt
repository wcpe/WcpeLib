package top.wcpe.wcpelib.bukkit.version.adapter.player.chat

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionInfo
import top.wcpe.wcpelib.bukkit.version.adapter.player.PlayerNmsManager
import java.util.*

/**
 * 由 WCPE 在 2022/4/3 20:24 创建
 *
 * Created by WCPE on 2022/4/3 20:24
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
class PlayOutChatAdapter1171Impl(versionInfo: VersionInfo) : PlayOutChatAdapter(versionInfo) {
    override fun sendAction(player: Player, message: String) {
        val chatComponentTextInstance =
            versionInfo.getNmsClass("network.chat.ChatComponentText").getConstructor(String::class.java)
                .newInstance(ChatColor.translateAlternateColorCodes('&', message))

        val chatMessageTypeClass = versionInfo.getNmsClass("network.chat.ChatMessageType")
        val chatMessageType = chatMessageTypeClass.getField("c").get(null)

        val packetPlayOutChat = versionInfo.getNmsClass("network.protocol.game.PacketPlayOutChat").getConstructor(
            versionInfo.getNmsClass("network.chat.IChatBaseComponent"),
            chatMessageTypeClass, UUID::class.java
        ).newInstance(chatComponentTextInstance, chatMessageType, player.uniqueId)
        PlayerNmsManager.sendPacket(player, packetPlayOutChat)
    }
}