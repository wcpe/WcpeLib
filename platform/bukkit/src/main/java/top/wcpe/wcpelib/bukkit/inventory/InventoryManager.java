package top.wcpe.wcpelib.bukkit.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Inventory管理类
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:13:18
 */
public class InventoryManager {

    private final Map<Inventory, InventoryPlus> map = new HashMap<>();
    @Getter
    @Setter
    private InventoryPlus firstInventoryPlus;
    @Getter
    @Setter
    private InventoryPlus lastPutInventoryPlus;

    public InventoryManager() {

    }

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

        if (this.lastPutInventoryPlus != null) {
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
