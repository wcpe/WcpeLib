package top.wcpe.wcpelib.nukkit.inventory.entity

import cn.nukkit.event.inventory.InventoryOpenEvent
import top.wcpe.wcpelib.nukkit.inventory.InventoryPlus

class InventoryOpenEventDTO(
    val inventoryOpenEvent: InventoryOpenEvent,
    val inventoryPlus: InventoryPlus
)