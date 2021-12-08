package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.math.BlockVector3
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BlockEntityDataPacket
import cn.nukkit.network.protocol.DataPacket
import top.wcpe.wcpelib.nukkit.WcpeLib
import java.io.IOException
import java.nio.ByteOrder


class DoubleChestRawInventory(holder: InventoryHolder, title: String) :
    ChestRawInventory(InventoryType.DOUBLE_CHEST, holder, title) {
    companion object {
        @JvmStatic
        fun getDoubleNbt(pos: BlockVector3, pairPos: BlockVector3, name: String?): ByteArray? {
            val tag = CompoundTag().putString("id", "Chest").putInt("x", pos.x).putInt("y", pos.y).putInt("z", pos.z)
                .putInt("pairx", pairPos.x).putInt("pairz", pairPos.z).putString(
                    "CustomName",
                    name ?: "Chest"
                )
            return try {
                NBTIO.write(tag, ByteOrder.LITTLE_ENDIAN, true)
            } catch (e: IOException) {
                throw RuntimeException("Unable to create NBT for chest")
            }
        }
    }

    override fun onOpen(who: Player) {
        this.viewers.add(who)
        val blocks = onOpenBlock(who)
        blockPositions[who] = blocks
        Server.getInstance().scheduler.scheduleDelayedTask(WcpeLib.getInstance(), { onFakeOpen(who, blocks) }, 3)
    }

    override fun onOpenBlock(paramPlayer: Player): MutableList<BlockVector3> {
        val blockPositionA = BlockVector3(paramPlayer.x.toInt(), paramPlayer.y.toInt() + 2, paramPlayer.z.toInt())
        val blockPositionB = blockPositionA.add(1, 0, 0)
        placeChest(paramPlayer, blockPositionA)
        placeChest(paramPlayer, blockPositionB)
        pair(paramPlayer, blockPositionA, blockPositionB)
        pair(paramPlayer, blockPositionB, blockPositionA)
        return mutableListOf(blockPositionA, blockPositionB)
    }

    private fun pair(who: Player, pos1: BlockVector3, pos2: BlockVector3) {
        val blockEntityData = BlockEntityDataPacket()
        blockEntityData.x = pos1.x
        blockEntityData.y = pos1.y
        blockEntityData.z = pos1.z
        blockEntityData.namedTag = getDoubleNbt(pos1, pos2, getName())
        who.dataPacket(blockEntityData as DataPacket)
    }


}