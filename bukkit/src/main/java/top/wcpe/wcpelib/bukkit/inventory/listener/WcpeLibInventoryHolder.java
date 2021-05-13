package top.wcpe.wcpelib.bukkit.inventory.listener;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * 用来判断的接口
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:13:41
 */
public class WcpeLibInventoryHolder implements InventoryHolder {
    @Getter
    private InventoryPlus inventoryPlus;

    public WcpeLibInventoryHolder(InventoryPlus inventoryPlus) {
        this.inventoryPlus = inventoryPlus;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
