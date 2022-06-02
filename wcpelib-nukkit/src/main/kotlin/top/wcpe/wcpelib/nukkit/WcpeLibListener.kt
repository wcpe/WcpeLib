package top.wcpe.wcpelib.nukkit

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.server.DataPacketSendEvent
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.AvailableEntityIdentifiersPacket
import java.nio.ByteOrder

/**
 * 由 WCPE 在 2022/6/2 16:10 创建
 *
 * Created by WCPE on 2022/6/2 16:10
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-3
 */
class WcpeLibListener : Listener {
    @EventHandler
    fun listenerDataPacketSendEvent(e: DataPacketSendEvent) {
        val packet = e.packet
        if (packet is AvailableEntityIdentifiersPacket) {
            val tags = NBTIO.read(packet.tag, ByteOrder.LITTLE_ENDIAN, true)
            val idList = tags.getList("idlist", CompoundTag::class.java)
            WcpeLib.getRegisterEntity().getSection("register-entity")?.run {
                for (key in getKeys()) {
                    val keySection = getSection(key) ?: continue
                    idList.add(
                        CompoundTag()
                            .putBoolean("hasspawnegg", keySection.getBoolean("hasSpawnEgg"))
                            .putBoolean("summonable", keySection.getBoolean("summonAble"))
                            .putString("id", keySection.getString("id"))
                            .putString("bid", keySection.getString("bid"))
                            .putInt("rid", keySection.getInt("rid"))
                    )
                }
            }
            tags.put("idlist", idList)
            packet.tag = NBTIO.write(tags, ByteOrder.LITTLE_ENDIAN, true)
        }
    }
}