package top.wcpe.wcpelib.nukkit.inventory.entity

import cn.nukkit.event.inventory.InventoryClickEvent
import top.wcpe.wcpelib.nukkit.inventory.InventoryPlus

class InventoryClickEventDTO(
    val inventoryClickEvent: InventoryClickEvent,
    val inventoryPlus: InventoryPlus
)