package top.wcpe.wcpelib.bukkit.inventory;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import lombok.Getter;
import lombok.Setter;
import top.wcpe.wcpelib.bukkit.inventory.listener.WcpeLibInventoryHolder;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.InventoryCloseEventFunctional;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.InventoryOpenEventFunctional;
import top.wcpe.wcpelib.bukkit.inventory.listener.inter.RefreshInventoryPlusFunctional;
import top.wcpe.wcpelib.bukkit.inventory.entity.Slot;

/**
 * {@link Inventory}增强版 233
 * 
 * @author WCPE
 * @date 2021年4月8日 下午5:12:48
 */
public class InventoryPlus {

	@Getter
	private final String nameId;
	private final Inventory rawInventory;
	@Getter
	private final HashMap<Integer, Slot<?>> slotMap;
	@Getter
	@Setter
	private InventoryPlus lastInventory;
	@Getter
	@Setter
	private InventoryPlus nextInventory;
	@Getter
	private String title;
	@Getter
	private int row;
	@Getter
	private boolean disDoubleClick;
	@Getter
	private boolean disDrag;
	@Getter
	private boolean disClickPlayerGui;
	@Getter
	private boolean disClickNullSlot;
	@Getter
	private InventoryOpenEventFunctional onOpen;
	@Getter
	private InventoryCloseEventFunctional onClose;

	public InventoryPlus setSlot(int index, Slot<?> slot) {
		slotMap.put(index, slot);
		return this;
	}

	public void clearInventory(int[] slots) {
		for (int i : slots) {
			this.rawInventory.clear(i);
			this.slotMap.remove(i);
		}

	}

	public Stream<? extends Player> getOpenThisInventoryPlayers() {
		return Bukkit.getServer().getOnlinePlayers().stream().filter(p -> {
			InventoryView openInventory = p.getOpenInventory();
			if (openInventory != null) {
				Inventory topInventory = openInventory.getTopInventory();
				if (topInventory != null) {
					if (topInventory.getHolder() instanceof WcpeLibInventoryHolder) {
						InventoryPlus inventoryPlus = InventoryManager.getInventoryPlusMap(nameId).get(topInventory);
						if (inventoryPlus != null) {
							if (inventoryPlus.getNameId().equals(nameId)) {
								return true;
							}
						}
					}
				}
			}
			return false;
		});
	}

	public void openThisInventory(Player p) {
		p.openInventory(getRawInventory());
	}

	public void customRefreshRun(RefreshInventoryPlusFunctional run) {
		run.run(getOpenThisInventoryPlayers());
	}

	public void refreshInventoryAndOpen() {
		getOpenThisInventoryPlayers().forEach(this::openThisInventory);
	}

	public void refreshInventory() {
		for (Entry<Integer, Slot<?>> entry : this.slotMap.entrySet()) {
			if (entry.getValue() == null)
				this.rawInventory.setItem(entry.getKey(), null);
			else
				this.rawInventory.setItem(entry.getKey(), entry.getValue().getItemStack());
		}
	}

	public Inventory getRawInventory() {
		refreshInventory();
		return this.rawInventory;
	}

	private InventoryPlus(Builder builder) {
		this.nameId = builder.nameId;
		this.nextInventory = builder.nextInventory;
		this.lastInventory = builder.lastInventory;
		this.title = builder.title;
		this.row = builder.row;
		Inventory inv = Bukkit.createInventory(new WcpeLibInventoryHolder(), this.row * 9, this.title);
		this.slotMap = builder.slotMap;
		this.rawInventory = inv;
		this.disDoubleClick = builder.disDoubleClick;
		this.disClickNullSlot = builder.disClickNullSlot;
		this.disDrag = builder.disDrag;
		this.disClickPlayerGui = builder.disClickPlayerGui;
		this.onOpen = builder.onOpen;
		this.onClose = builder.onClose;
	}

	public static class Builder {
		private final String nameId;
		private int row = 6;
		private String title = " ";
		private HashMap<Integer, Slot<?>> slotMap = new HashMap<>();
		private InventoryPlus lastInventory;
		private InventoryPlus nextInventory;

		private boolean disDoubleClick;
		private boolean disClickNullSlot;
		private boolean disDrag;
		private boolean disClickPlayerGui;

		private InventoryOpenEventFunctional onOpen;
		private InventoryCloseEventFunctional onClose;

		public Builder(String nameId) {
			this.nameId = nameId;
		}

		public Builder row(int row) {
			this.row = row;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder setSlot(int index, Slot<?> slot) {
			slotMap.put(index, slot);
			return this;
		}

		public Builder disDoubleClick(boolean disDoubleClick) {
			this.disDoubleClick = disDoubleClick;
			return this;
		}

		public Builder disClickNullSlot(boolean disClickNullSlot) {
			this.disClickNullSlot = disClickNullSlot;
			return this;
		}

		public Builder disClickPlayerGui(boolean disClickPlayerGui) {
			this.disClickPlayerGui = disClickPlayerGui;
			return this;
		}

		public Builder disDrag(boolean disDrag) {
			this.disDrag = disDrag;
			return this;
		}

		public Builder onOpen(InventoryOpenEventFunctional onOpen) {
			this.onOpen = onOpen;
			return this;
		}

		public Builder onClose(InventoryCloseEventFunctional onClose) {
			this.onClose = onClose;
			return this;
		}

		public InventoryPlus build() {
			return new InventoryPlus(this);
		}
	}

}