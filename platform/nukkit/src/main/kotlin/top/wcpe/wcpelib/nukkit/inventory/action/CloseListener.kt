package top.wcpe.wcpelib.nukkit.inventory.action

import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryCloseEventDTO
import java.util.function.Consumer

fun interface CloseListener : Consumer<InventoryCloseEventDTO> {
    override fun accept(e: InventoryCloseEventDTO)
}