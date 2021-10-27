package top.wcpe.wcpelib.nukkit.inventory

import top.wcpe.wcpelib.nukkit.inventory.action.ClickListener
import top.wcpe.wcpelib.nukkit.inventory.action.CloseListener
import top.wcpe.wcpelib.nukkit.inventory.action.OpenListener

class InventoryPlusBuilder {

    var doubleInventory = false
    var title = " "

    var slotMap: MutableMap<Int, SlotPlus> = mutableMapOf()

    var lastInventory: InventoryPlus? = null
    var nextInventory: InventoryPlus? = null

    var disDoubleClick = false
    var disClickNullSlot = false
    var disClickPlayerGui = false

    var onOpen: OpenListener? = null
    var onClick: ClickListener? = null
    var onClose: CloseListener? = null


    fun doubleInventory(doubleInventory: Boolean): InventoryPlusBuilder {
        this.doubleInventory = doubleInventory
        return this
    }

    fun title(title: String): InventoryPlusBuilder {
        this.title = title
        return this
    }

    fun setSlot(index: Int, slot: SlotPlus): InventoryPlusBuilder {
        slotMap[index] = slot
        return this
    }

    fun disDoubleClick(disDoubleClick: Boolean): InventoryPlusBuilder {
        this.disDoubleClick = disDoubleClick
        return this
    }

    fun disClickNullSlot(disClickNullSlot: Boolean): InventoryPlusBuilder {
        this.disClickNullSlot = disClickNullSlot
        return this
    }

    fun disClickPlayerGui(disClickPlayerGui: Boolean): InventoryPlusBuilder {
        this.disClickPlayerGui = disClickPlayerGui
        return this
    }


    fun onOpen(onOpen: OpenListener): InventoryPlusBuilder {
        this.onOpen = onOpen
        return this
    }


    fun onClick(onClick: ClickListener): InventoryPlusBuilder {
        this.onClick = onClick
        return this
    }

    fun onClose(onClose: CloseListener): InventoryPlusBuilder {
        this.onClose = onClose
        return this
    }

    fun build(): InventoryPlus {
        return InventoryPlus(this)
    }
}