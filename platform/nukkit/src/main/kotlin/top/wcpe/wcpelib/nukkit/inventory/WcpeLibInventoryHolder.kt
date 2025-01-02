package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.InventoryHolder

class WcpeLibInventoryHolder(val inventoryPlus: InventoryPlus) : InventoryHolder {
    override fun getInventory(): Inventory? {
        return null
    }
}