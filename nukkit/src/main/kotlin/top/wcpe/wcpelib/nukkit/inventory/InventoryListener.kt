package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryClickEvent
import cn.nukkit.event.inventory.InventoryCloseEvent
import cn.nukkit.event.inventory.InventoryOpenEvent
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.transaction.action.SlotChangeAction
import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryClickEventDTO
import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryCloseEventDTO
import top.wcpe.wcpelib.nukkit.inventory.entity.InventoryOpenEventDTO

class InventoryListener : Listener {
    @EventHandler
    fun inventoryTransactionEvent(e: InventoryTransactionEvent) {
        var transaction = e.transaction
        var from: SlotChangeAction? = null
        var to: SlotChangeAction? = null
        var who: Player? = null

        for (action in transaction.actions) {
            if (action !is SlotChangeAction) {
                continue
            }
            if (action.inventory.holder is Player) {
                who = action.inventory.holder as Player
            }
            if (from == null) {
                from = action
            } else {
                to = action
            }
        }
        var target: SlotChangeAction? =
            (if (from?.inventory is RawInventory) from else if (to?.inventory is RawInventory) to else null) ?: return
        var inventory = target!!.inventory
        var slot = target!!.slot
        if (inventory.holder !is WcpeLibInventoryHolder) return
        val inventoryPlus: InventoryPlus = (inventory.holder as WcpeLibInventoryHolder).inventoryPlus
        var inventoryClickEventDTO = InventoryClickEventDTO(
            InventoryClickEvent(
                who,
                inventory,
                target.slot,
                target.sourceItem,
                target.targetItem
            ), inventoryPlus
        )
        inventoryPlus.onClick?.let {
            it.accept(inventoryClickEventDTO)
            e.isCancelled = inventoryClickEventDTO.inventoryClickEvent.isCancelled
        }
        if (inventoryPlus.isLockSlot.contains(slot)) {
            e.isCancelled = true
            return
        }

        var slotPlus = inventoryPlus.slotMap[slot]

        slotPlus?.listener?.accept(inventoryClickEventDTO)
        e.isCancelled =inventoryClickEventDTO.inventoryClickEvent.isCancelled
    }


    @EventHandler
    fun inventoryOpenEvent(e: InventoryOpenEvent) {
        if (e.inventory.holder !is WcpeLibInventoryHolder) return
        val inventoryPlus: InventoryPlus = (e.inventory.holder as WcpeLibInventoryHolder).inventoryPlus

        inventoryPlus.close = false
        inventoryPlus.onOpen?.let { it.accept(InventoryOpenEventDTO(e, inventoryPlus)) }
    }

    @EventHandler
    fun inventoryCloseEvent(e: InventoryCloseEvent) {
        if (e.inventory.holder !is WcpeLibInventoryHolder) return
        val inventoryPlus: InventoryPlus = (e.inventory.holder as WcpeLibInventoryHolder).inventoryPlus

        if (inventoryPlus.close) return
        inventoryPlus.close = true
        inventoryPlus.onClose?.let { it.accept(InventoryCloseEventDTO(e, inventoryPlus)) }
    }
}