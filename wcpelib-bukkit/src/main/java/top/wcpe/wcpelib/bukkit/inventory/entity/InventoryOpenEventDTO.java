package top.wcpe.wcpelib.bukkit.inventory.entity;

import lombok.Data;
import org.bukkit.event.inventory.InventoryOpenEvent;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * {@link InventoryPlus} 打开事件的数据传输类
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:19:10
 */
@Data
public class InventoryOpenEventDTO {
    private final InventoryOpenEvent inventoryOpenEvent;
    private final InventoryPlus inventoryPlus;
}
