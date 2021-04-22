package top.wcpe.wcpelib.model.bukkit.inventory.listener;

import java.util.Map.Entry;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import top.wcpe.wcpelib.model.bukkit.inventory.entity.InventoryCloseEventDTO;
import top.wcpe.wcpelib.model.bukkit.inventory.entity.InventoryOpenEventDTO;
import top.wcpe.wcpelib.model.bukkit.inventory.entity.Slot;
import top.wcpe.wcpelib.model.bukkit.inventory.entity.SlotDTO;
import top.wcpe.wcpelib.model.bukkit.inventory.listener.InventoryManager.InventoryPlusMap;
import top.wcpe.wcpelib.model.bukkit.inventory.listener.inter.InventoryCloseEventFunctional;
import top.wcpe.wcpelib.model.bukkit.inventory.listener.inter.InventoryOpenEventFunctional;
import top.wcpe.wcpelib.model.bukkit.inventory.listener.inter.SlotEventFunctional;

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
		for (Entry<String, InventoryPlusMap> entry : InventoryManager.getInventoryPlusMap().entrySet()) {
			InventoryPlus inventoryPlus = entry.getValue().get(e.getInventory());
			if (inventoryPlus == null) {
				continue;
			}

			if (inventoryPlus.isDisDoubleClick() && e.getClick() == ClickType.DOUBLE_CLICK) {
				e.setCancelled(true);
			}
			int rawSlot = e.getRawSlot();
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
	}

	@EventHandler
	public void inventoryDragEvent(InventoryDragEvent e) {
		if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
			return;
		for (Entry<String, InventoryPlusMap> entry : InventoryManager.getInventoryPlusMap().entrySet()) {
			InventoryPlus inventoryPlus = entry.getValue().get(e.getInventory());
			if (inventoryPlus == null) {
				continue;
			}
			if (inventoryPlus.isDisDrag()) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void inventoryOpenEvent(InventoryOpenEvent e) {
		if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
			return;
		for (Entry<String, InventoryPlusMap> entry : InventoryManager.getInventoryPlusMap().entrySet()) {
			InventoryPlus inventoryPlus = entry.getValue().get(e.getInventory());
			if (inventoryPlus == null) {
				continue;
			}
			InventoryOpenEventFunctional onOpen = inventoryPlus.getOnOpen();
			if (onOpen == null) {
				return;
			}
			onOpen.run(new InventoryOpenEventDTO(e, inventoryPlus));
			return;
		}
	}

	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent e) {
		if (!(e.getInventory().getHolder() instanceof WcpeLibInventoryHolder))
			return;
		for (Entry<String, InventoryPlusMap> entry : InventoryManager.getInventoryPlusMap().entrySet()) {
			InventoryPlus inventoryPlus = entry.getValue().get(e.getInventory());
			if (inventoryPlus == null) {
				continue;
			}
			InventoryCloseEventFunctional onClose = inventoryPlus.getOnClose();
			if (onClose == null) {
				return;
			}
			onClose.run(new InventoryCloseEventDTO(e, inventoryPlus));
			return;
		}
	}
}
