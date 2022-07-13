package top.wcpe.wcpelib.bukkit.inventory.listener.inter;

import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.entity.InventoryClickEventDTO;

/**
 * {@link InventoryPlus}点击函数接口
 *
 * @author WCPE
 * @date 2021年7月17日 下午4:38:57
 */
@FunctionalInterface
public interface InventoryClickEventFunctional {
    void run(InventoryClickEventDTO e);
}
