package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.level.GlobalBlockPalette
import cn.nukkit.math.BlockVector3
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BlockEntityDataPacket
import cn.nukkit.network.protocol.UpdateBlockPacket
import java.io.IOException
import java.nio.ByteOrder
import java.util.*

open class ChestRawInventory(type: InventoryType, holder: InventoryHolder, title: String) :
    RawInventory(type, holder, title) {

    constructor(holder: InventoryHolder, title: String) : this(InventoryType.CHEST, holder, title)

    companion object {
        @JvmStatic
        fun getNbt(pos: BlockVector3, name: String): ByteArray {
            var tag = CompoundTag().putString("id", "Chest").putInt("x", pos.x).putInt("y", pos.y)
                .putInt("z", pos.z).putString("CustomName", name)
            try {
                return NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true)
            } catch (e: IOException) {
                throw RuntimeException("Unable to create NBT for chest")
            }
        }
    }

    override fun onOpenBlock(paramPlayer: Player): MutableList<BlockVector3> {
        var blockPosition = BlockVector3(paramPlayer.x.toInt(), (paramPlayer.y + 2).toInt(), paramPlayer.z.toInt())
        placeChest(paramPlayer, blockPosition)
        return Collections.singletonList(blockPosition)
    }

    open fun placeChest(who: Player, pos: BlockVector3) {
        var updateBlock = UpdateBlockPacket()
        updateBlock.blockRuntimeId = GlobalBlockPalette.getOrCreateRuntimeId(54, 0)
        updateBlock.flags = 11
        updateBlock.x = pos.x
        updateBlock.y = pos.y
        updateBlock.z = pos.z
        who.dataPacket(updateBlock)
        var blockEntityData = BlockEntityDataPacket()
        blockEntityData.x = pos.x
        blockEntityData.y = pos.y
        blockEntityData.z = pos.z
        blockEntityData.namedTag = getNbt(pos, getName())
        who.dataPacket(blockEntityData)
    }

}