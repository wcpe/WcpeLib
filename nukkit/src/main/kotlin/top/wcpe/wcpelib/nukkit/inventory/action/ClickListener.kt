package top.wcpe.wcpelib.nukkit.inventory.action

import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryClickEventDTO
import java.util.function.Consumer

fun interface ClickListener : Consumer<InventoryClickEventDTO> {
    override fun accept(e: InventoryClickEventDTO)
}