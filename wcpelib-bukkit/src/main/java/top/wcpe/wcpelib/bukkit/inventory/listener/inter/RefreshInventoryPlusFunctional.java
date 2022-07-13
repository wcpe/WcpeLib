package top.wcpe.wcpelib.bukkit.inventory.listener.inter;

import org.bukkit.entity.Player;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

import java.util.stream.Stream;

/**
 * {@link InventoryPlus}刷新函数接口
 *
 * @author WCPE
 * @date 2021年4月8日 下午5:14:23
 */
@FunctionalInterface
public interface RefreshInventoryPlusFunctional {
    void run(Stream<? extends Player> playersStream);
}
