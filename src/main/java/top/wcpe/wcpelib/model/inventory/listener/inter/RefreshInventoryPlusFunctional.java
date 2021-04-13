package top.wcpe.wcpelib.model.inventory.listener.inter;

import java.util.stream.Stream;

import org.bukkit.entity.Player;

import top.wcpe.wcpelib.model.inventory.listener.InventoryPlus;
/**
 * {@link InventoryPlus}刷新函数接口
 * @author WCPE
 * @date 2021年4月8日 下午5:14:23
 */
@FunctionalInterface
public interface RefreshInventoryPlusFunctional {
	public void run(Stream<? extends Player> playersStream);
}
