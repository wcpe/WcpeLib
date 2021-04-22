package top.wcpe.wcpelib.model.bukkit.inventory.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;

/**
 * Inventory管理类
 * @author WCPE
 * @date 2021年4月8日 下午5:13:18
 */
public class InventoryManager {

	@Getter
	private static HashMap<String, InventoryPlusMap> inventoryPlusMap = new HashMap<>();

	public static InventoryPlusMap getInventoryPlusMap(String key) {
		InventoryPlusMap ipMap = inventoryPlusMap.get(key);
		if (ipMap == null) {
			ipMap = new InventoryPlusMap();
			inventoryPlusMap.put(key, ipMap);
		}
		return ipMap;
	}

	public static class InventoryPlusMap {
		@Getter
		@Setter
		private InventoryPlus firstInventoryPlus;
		@Getter
		@Setter
		private InventoryPlus lastPutInventoryPlus;

		public InventoryPlusMap() {

		}

		private final Map<Inventory, InventoryPlus> map = new HashMap<>();

		public InventoryPlus get(Inventory i) {
			return map.get(i);
		}

		public void remove(Inventory i) {
			InventoryPlus inventoryPlus = map.get(i);
			if (inventoryPlus != null) {
				InventoryPlus lastInventory = inventoryPlus.getLastInventory();
				InventoryPlus nextInventory = inventoryPlus.getNextInventory();
				lastInventory.setNextInventory(nextInventory);
				nextInventory.setLastInventory(lastInventory);
			}
			map.remove(i);
		}

		public int size() {
			return map.size();
		}

		public Collection<InventoryPlus> values() {
			return map.values();
		}

		public boolean containsKey(Inventory i) {
			return map.containsKey(i);
		}

		public InventoryPlus put(Inventory i, InventoryPlus inventoryPlus) {
			if (firstInventoryPlus == null)
				this.firstInventoryPlus = inventoryPlus;
			InventoryPlus ip = this.map.get(i);
			if (ip != null) {
				InventoryPlus lastInventory = ip.getLastInventory();
				InventoryPlus nextInventory = ip.getNextInventory();
				lastInventory.setNextInventory(inventoryPlus);
				nextInventory.setLastInventory(inventoryPlus);
			}

			if(this.lastPutInventoryPlus!=null) {
				InventoryPlus inv = this.map.get(this.lastPutInventoryPlus.getRawInventory());
				if (inv != null) {
					inv.setNextInventory(inventoryPlus);
				}
			}
			
			inventoryPlus.setLastInventory(this.lastPutInventoryPlus);
			this.lastPutInventoryPlus = inventoryPlus;
			return map.put(i, inventoryPlus);
		}

		public Set<Map.Entry<Inventory, InventoryPlus>> entrySet() {
			return map.entrySet();
		}
	}
}
