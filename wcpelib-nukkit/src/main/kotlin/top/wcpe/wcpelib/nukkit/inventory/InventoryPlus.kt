package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.Server
import top.wcpe.wcpelib.nukkit.inventory.action.ClickListener
import top.wcpe.wcpelib.nukkit.inventory.action.CloseListener
import top.wcpe.wcpelib.nukkit.inventory.action.OpenListener
import java.util.stream.Stream

class InventoryPlus {
    companion object {
        init {
            Server.getInstance().pluginManager.registerEvents(
                InventoryListener(),
                Server.getInstance().pluginManager.getPlugin("WcpeLib")
            )
        }
    }

    constructor(builder: InventoryPlusBuilder) {
        nextInventory = builder.nextInventory
        lastInventory = builder.lastInventory
        title = builder.title
        doubleInventory = builder.doubleInventory

        rawInventory =
            if (doubleInventory) DoubleChestRawInventory(
                WcpeLibInventoryHolder(this),
                title
            ) else ChestRawInventory(
                WcpeLibInventoryHolder(this), title
            )

        slotMap = builder.slotMap
        disDoubleClick = builder.disDoubleClick
        disClickNullSlot = builder.disClickNullSlot
        disClickPlayerGui = builder.disClickPlayerGui
        onOpen = builder.onOpen
        onClick = builder.onClick
        this.onClose = builder.onClose
    }

    var rawInventory: RawInventory

    fun getBaseRawInventory(): RawInventory {
        refreshInventory()
        return rawInventory
    }

    var slotMap: MutableMap<Int, SlotPlus> = mutableMapOf()

    var lastInventory: InventoryPlus? = null
        get

    var nextInventory: InventoryPlus? = null
        get
        set

    var title: String = " "
        get

    var doubleInventory = false
    var disDoubleClick = false
    var disClickPlayerGui = false
    var disClickNullSlot = false
    var onOpen: OpenListener? = null
    var onClick: ClickListener? = null
    var onClose: CloseListener? = null
    var close = true
    val isLockSlot: List<Int> = ArrayList()

    fun setSlot(index: Int, slot: SlotPlus): InventoryPlus? {
        slotMap[index] = slot
        this.refreshInventory(index)
        return this
    }

    fun clearInventory(slots: IntArray) {
        for (i in slots) {
            rawInventory.clear(i)
            slotMap.remove(i)
        }
    }

    fun getOpenThisInventoryPlayers(): Stream<Player> {
        return Server.getInstance().onlinePlayers.values.stream().filter { p ->
            var topWindow = p.topWindow.orElse(null)
            topWindow?.let {
                if (topWindow.holder is WcpeLibInventoryHolder) {
                    return@filter false
                }
            }
            false
        }
    }

    fun openThisInventory(player: Player) {
//        var id = Entity.entityCount++
//        rawInventory.id = id
//        val fakeEntity = AddEntityPacket()
//        fakeEntity.entityUniqueId = id
//        fakeEntity.entityRuntimeId = id
//        fakeEntity.type = EntityMinecartChest.NETWORK_ID
//        fakeEntity.x = player.x.toFloat()
//        fakeEntity.y = player.y.toFloat()
//        fakeEntity.z = player.z.toFloat()
//        fakeEntity.metadata
//            .putString(Entity.DATA_NAMETAG, title)
//            .putByte(Entity.DATA_CONTAINER_TYPE, 10)
//            .putInt(
//                Entity.DATA_CONTAINER_BASE_SIZE,
//                if (doubleInventory) InventoryType.DOUBLE_CHEST.defaultSize else InventoryType.CHEST.defaultSize
//            )
//        player.dataPacket(fakeEntity)

        if (player.addWindow(rawInventory) == -1) {
            rawInventory.close()
        }
    }

    private fun refreshInventory() {
        for (mutableEntry in slotMap) {
            rawInventory.setItem(mutableEntry.key, mutableEntry.value.getItem())
        }
    }

    private fun refreshInventory(i: Int) {
        slotMap[i]?.let { rawInventory.setItem(i, it.getItem()) }
    }


}