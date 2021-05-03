package top.wcpe.wcpelib.bukkit.inventory.entity;

import org.bukkit.event.inventory.InventoryCloseEvent;

import lombok.Data;
import top.wcpe.wcpelib.bukkit.inventory.InventoryPlus;

/**
 * {@link InventoryPlus}关闭事件的数据传输类
 * @author WCPE
 * @date 2021年4月8日 下午5:19:27
 */
@Data
public class InventoryCloseEventDTO {
	private final InventoryCloseEvent inventoryCloseEvent;
	private final InventoryPlus inventoryPlus;
}
