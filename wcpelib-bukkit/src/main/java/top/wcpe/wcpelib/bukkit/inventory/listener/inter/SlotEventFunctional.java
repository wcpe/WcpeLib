package top.wcpe.wcpelib.bukkit.inventory.listener.inter;

import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;
import top.wcpe.wcpelib.bukkit.inventory.entity.Slot;
import top.wcpe.wcpelib.bukkit.inventory.entity.SlotDTO;

/**
 * {@link InventoryPlus}中的{@link Slot}点击函数接口
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:14:32
 */
@FunctionalInterface
public interface SlotEventFunctional {
    void run(SlotDTO e);
}
