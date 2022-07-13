package top.wcpe.wcpelib.bukkit.inventory.entity;

import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * {@link InventoryPlus} 点击事件
 *
 * @author WCPE
 * @date 2021年7月17日 下午4:38:57
 */
@Data
public class InventoryClickEventDTO {
    private final InventoryClickEvent inventoryClickEvent;
    private final InventoryPlus inventoryPlus;
}
