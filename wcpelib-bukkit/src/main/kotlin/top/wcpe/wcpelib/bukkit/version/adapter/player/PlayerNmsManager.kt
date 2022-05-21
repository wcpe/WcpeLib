package top.wcpe.wcpelib.bukkit.version.adapter.player

import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.version.VersionInfo
import top.wcpe.wcpelib.bukkit.version.VersionManager
import top.wcpe.wcpelib.bukkit.version.adapter.player.chat.*

/**
 * 由 WCPE 在 2022/4/1 23:18 创建
 *
 * Created by WCPE on 2022/4/1 23:18
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
object PlayerNmsManager {

    @JvmStatic
    fun sendPacket(player: Player, packet: Any) {
        val craftPlayer = player::class.java.getMethod("getHandle").invoke(player)
        val craftPlayerClass = craftPlayer::class.java

        VersionManager.versionInfo.run {
            if (versionNumber >= VersionInfo.V1_17_1.versionNumber) {
                val playerConnection = craftPlayerClass.getField("b")[craftPlayer]
                val sendPacketMethod = playerConnection::class.java.getMethod(
                    "sendPacket",
                    getNmsClass("network.protocol.Packet")
                )
                Triple(sendPacketMethod, playerConnection, packet)
            } else {
                val playerConnection = craftPlayerClass.getField("playerConnection")[craftPlayer]
                val sendPacketMethod = playerConnection::class.java.getMethod(
                    "sendPacket",
                    getNmsClass("Packet")
                )
                Triple(sendPacketMethod, playerConnection, packet)
            }
        }.run {
            first.invoke(second, third)
        }
    }

    @JvmStatic
    val playOutChatAdapter =
        VersionManager.versionInfo.run {
            when (versionNumber) {
                in VersionInfo.V1_8_8.versionNumber until VersionInfo.V1_12_1.versionNumber -> {
                    PlayOutChatAdapter188Impl(this)
                }
                in VersionInfo.V1_12_1.versionNumber until VersionInfo.V1_16_1.versionNumber -> {
                    PlayOutChatAdapter1121Impl(this)
                }
                in VersionInfo.V1_16_1.versionNumber until VersionInfo.V1_17_1.versionNumber -> {
                    PlayOutChatAdapter1161Impl(this)
                }
                in VersionInfo.V1_17_1.versionNumber..VersionInfo.V1_17_1.versionNumber -> {
                    PlayOutChatAdapter1171Impl(this)
                }
                else -> {
                    TODO("Unknown Version")
                }
            }

        }

}