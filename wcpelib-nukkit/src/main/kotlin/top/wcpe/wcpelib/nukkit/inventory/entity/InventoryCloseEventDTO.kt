package top.wcpe.wcpelib.nukkit.inventory.entity

import cn.nukkit.event.inventory.InventoryCloseEvent
import top.wcpe.wcpelib.nukkit.inventory.InventoryPlus

class InventoryCloseEventDTO(
    val inventoryCloseEvent: InventoryCloseEvent,
    val inventoryPlus: InventoryPlus
)