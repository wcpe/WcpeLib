package top.wcpe.wcpelib.bukkit.version.adapter.player.chat

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionInfo
import top.wcpe.wcpelib.bukkit.version.adapter.player.PlayerNmsManager


/**
 * 由 WCPE 在 2022/4/1 20:56 创建
 *
 * Created by WCPE on 2022/4/1 20:56
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
class PlayOutChatAdapter188Impl(versionInfo: VersionInfo) : PlayOutChatAdapter(versionInfo) {
    override fun sendAction(player: Player, message: String) {
        val iChatBaseComponent = versionInfo.getNmsClass("IChatBaseComponent")
        for (declaredClass in iChatBaseComponent.declaredClasses) {
            if ("ChatSerializer" == declaredClass.simpleName) {
                val aMethod = declaredClass.getDeclaredMethod("a", String::class.java)
                aMethod.isAccessible = true
                val aMethodValue = aMethod.invoke(null, "{\"text\": \"$message\"}")

                val packetPlayOutChat = versionInfo.getNmsClass("PacketPlayOutChat").getConstructor(
                    iChatBaseComponent, Byte::class.javaPrimitiveType
                ).newInstance(aMethodValue, 2.toByte())
                PlayerNmsManager.sendPacket(player, packetPlayOutChat)
            }
        }
    }
}