package top.wcpe.wcpelib.model.bukkit.inventory.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

import top.wcpe.wcpelib.model.bukkit.inventory.entity.Slot;
import top.wcpe.wcpelib.model.bukkit.inventory.listener.InventoryPlus;
/**
 * {@link InventoryPlus}中 {@link Slot}的点击事件
 * @author WCPE
 * @date 2021年4月8日 下午5:16:43
 */
public class SlotClickEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private boolean cancel;

	public SlotClickEvent(InventoryClickEvent inventoryClickEvent, Slot<?> slot) {
		super();
		this.inventoryClickEvent = inventoryClickEvent;
		this.slot = slot;
	}

	private InventoryClickEvent inventoryClickEvent;
	private Slot<?> slot;

	public Slot<?> getSlot() {
		return slot;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
		if (cancel) {
			inventoryClickEvent.setCancelled(true);
			return;
		}
		inventoryClickEvent.setCancelled(false);
	}

	public InventoryClickEvent getInventoryClickEvent() {
		return inventoryClickEvent;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
