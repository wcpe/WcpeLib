package top.wcpe.wcpelib.bukkit.inventory.listener;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.entity.*;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.*;

/**
 * {@link InventoryPlus}的监听事件
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:13:03
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
            return;
        InventoryPlus inventoryPlus = ((WcpeLibInventoryHolder) e.getInventory().getHolder()).getInventoryPlus();
        if (inventoryPlus == null) {
            return;
        }
        InventoryClickEventFunctional invOnClick = inventoryPlus.getOnClick();
        if (invOnClick != null) {
            invOnClick.run(new InventoryClickEventDTO(e, inventoryPlus));
        }
        if (inventoryPlus.isDisDoubleClick() && e.getClick() == ClickType.DOUBLE_CLICK) {
            e.setCancelled(true);
        }
        int rawSlot = e.getRawSlot();
        if (inventoryPlus.getIsLockSlot().contains(rawSlot)) {
            e.setCancelled(true);
            return;
        }
        if (inventoryPlus.isDisClickPlayerGui()) {
            int min = inventoryPlus.getRow() * 9;
            int max = inventoryPlus.getRow() * 9 + 35;
            if (rawSlot >= min && rawSlot <= max) {
                e.setCancelled(true);
            }
        }
        Slot<?> slot = inventoryPlus.getSlotMap().get(rawSlot);
        if (slot == null) {
            return;
        }

        SlotEventFunctional onClick = slot.getOnClick();
        if (onClick == null) {
            if (inventoryPlus.isDisClickNullSlot())
                e.setCancelled(true);
            return;
        }

        onClick.run(new SlotDTO(e, inventoryPlus, slot));
    }

    @EventHandler
    public void inventoryDragEvent(InventoryDragEvent e) {
        if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
            return;
        InventoryPlus inventoryPlus = ((WcpeLibInventoryHolder) e.getInventory().getHolder()).getInventoryPlus();
        if (inventoryPlus == null) {
            return;
        }
        InventoryDragEventFunctional onDrag = inventoryPlus.getOnDrag();
        if (onDrag != null) {
            onDrag.run(new InventoryDragEventDTO(e, inventoryPlus));
        }
        if (inventoryPlus.isDisDrag()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void inventoryOpenEvent(InventoryOpenEvent e) {
        if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
            return;
        InventoryPlus inventoryPlus = ((WcpeLibInventoryHolder) e.getInventory().getHolder()).getInventoryPlus();
        if (inventoryPlus == null) {
            return;
        }
        inventoryPlus.setClose(false);
        InventoryOpenEventFunctional onOpen = inventoryPlus.getOnOpen();
        if (onOpen == null) {
            return;
        }
        onOpen.run(new InventoryOpenEventDTO(e, inventoryPlus));

    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
            return;
        InventoryPlus inventoryPlus = ((WcpeLibInventoryHolder) e.getInventory().getHolder()).getInventoryPlus();
        if (inventoryPlus == null) {
            return;
        }
        if (inventoryPlus.isClose())
            return;
        inventoryPlus.setClose(true);
        InventoryCloseEventFunctional onClose = inventoryPlus.getOnClose();
        if (onClose == null) {
            return;
        }
        onClose.run(new InventoryCloseEventDTO(e, inventoryPlus));
    }
}
