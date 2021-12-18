package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.inventory.ContainerInventory
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.level.GlobalBlockPalette
import cn.nukkit.math.BlockVector3
import cn.nukkit.network.protocol.ContainerOpenPacket
import cn.nukkit.network.protocol.UpdateBlockPacket
import top.wcpe.wcpelib.nukkit.WcpeLib


abstract class RawInventory(inventoryType: InventoryType, inventoryHolder: InventoryHolder) :
    ContainerInventory(inventoryHolder, inventoryType) {
    companion object {
        @JvmField
        val ZERO: BlockVector3 = BlockVector3(0, 0, 0)

        @JvmField
        val open: MutableMap<Player, RawInventory> = mutableMapOf()


    }

    val blockPositions: MutableMap<Player, MutableList<BlockVector3>> = mutableMapOf()

    private var closed = false

    private var titleName: String = " "
    override fun getName(): String {
        return titleName
    }

    constructor(type: InventoryType, holder: InventoryHolder, title: String?) : this(type, holder) {
        titleName = title ?: type.defaultTitle
    }

    override fun onOpen(who: Player) {
        this.viewers.add(who)
        if (open.putIfAbsent(who, this) != null)
            return
        var blocks: MutableList<BlockVector3> = onOpenBlock(who)
        blockPositions[who] = blocks
        onFakeOpen(who, blocks)
    }

    override fun onClose(who: Player) {
        super.onClose(who)
        open.remove(who, this)
        var blocks = blockPositions[who]
        if (blocks != null) {
            for ((index, block) in blocks.withIndex()) {
                Server.getInstance().scheduler.scheduleDelayedTask(WcpeLib.getInstance(), {
                    var blockPosition = block.asVector3()
                    var updateBlock = UpdateBlockPacket()
                    updateBlock.blockRuntimeId =
                        GlobalBlockPalette.getOrCreateRuntimeId(who.getLevel().getBlock(blockPosition).fullId)
                    updateBlock.flags = 11
                    updateBlock.x = blockPosition.floorX
                    updateBlock.y = blockPosition.floorY
                    updateBlock.z = blockPosition.floorZ
                    who.dataPacket(updateBlock)
                }, 2 + index, false)
            }

        }
    }

    fun onFakeOpen(who: Player, blocks: MutableList<BlockVector3>) {
        var blockPosition: BlockVector3 = if (blocks.isEmpty()) ZERO else blocks[0]
        var containerOpen = ContainerOpenPacket()
        containerOpen.windowId = who.getWindowId(this)
        containerOpen.type = getType().networkType
        containerOpen.x = blockPosition.x
        containerOpen.y = blockPosition.y
        containerOpen.z = blockPosition.z
        who.dataPacket(containerOpen)
        sendContents(who)
    }


    fun getPosition(player: Player): MutableList<BlockVector3>? {
        return blockPositions[player]
    }


    fun close() {
        for (viewer in getViewers()) {
            viewer.removeWindow(this)
        }
        this.closed = true
    }


    protected abstract fun onOpenBlock(paramPlayer: Player): MutableList<BlockVector3>

}