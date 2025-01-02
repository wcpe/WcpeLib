package top.wcpe.wcpelib.bukkit.inventory.listener.inter;

import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.entity.InventoryDragEventDTO;

/**
 * {@link InventoryPlus}拖拽函数接口
 *
 * @author WCPE
 * @date 2021年7月17日 下午2:05:57
 */
@FunctionalInterface
public interface InventoryDragEventFunctional {
    void run(InventoryDragEventDTO e);
}
