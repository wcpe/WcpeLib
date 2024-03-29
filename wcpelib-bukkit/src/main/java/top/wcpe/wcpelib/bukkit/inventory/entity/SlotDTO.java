package top.wcpe.wcpelib.bukkit.inventory.entity;

import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * {@link InventoryPlus}中格子数据传输对象
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:17:21
 */
@Data
public class SlotDTO {
    private final InventoryClickEvent inventoryClickEvent;
    private final InventoryPlus inventoryPlus;
    private final Slot<?> slot;
}
