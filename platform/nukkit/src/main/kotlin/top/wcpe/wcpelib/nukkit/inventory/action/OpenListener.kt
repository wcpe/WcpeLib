package top.wcpe.wcpelib.nukkit.inventory.action

import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryOpenEventDTO
import java.util.function.Consumer

fun interface OpenListener : Consumer<InventoryOpenEventDTO> {
    override fun accept(e: InventoryOpenEventDTO)
}