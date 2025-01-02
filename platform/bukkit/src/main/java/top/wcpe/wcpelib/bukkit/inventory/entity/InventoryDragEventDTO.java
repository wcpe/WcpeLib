package top.wcpe.wcpelib.bukkit.inventory.entity;

import lombok.Data;
import org.bukkit.event.inventory.InventoryDragEvent;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * {@link InventoryPlus} 拖拽事件
 *
 * @author WCPE
 * @date 2021年7月17日 下午2:05:57
 */
@Data
public class InventoryDragEventDTO {
    private final InventoryDragEvent inventoryDragEvent;
    private final InventoryPlus inventoryPlus;
}
