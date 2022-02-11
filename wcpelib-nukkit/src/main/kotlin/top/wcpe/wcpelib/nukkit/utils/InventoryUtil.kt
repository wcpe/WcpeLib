package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.inventory.Inventory
import cn.nukkit.item.Item
import top.wcpe.wcpelib.nukkit.extend.getWcpeLibCustomName

/**
 * 由 WCPE 在 2022/1/6 18:45 创建
 *
 * Created by WCPE on 2022/1/6 18:45
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
object InventoryUtil {
    /**
     * <p>获取 inventory 中是否有 sureNullSlot 个空位
     *
     * @param inventory 玩家背包
     * @param sureNullSlot 空位数量
     * @return Boolean 是否有
     * @author WCPE
     * @since 1.0.8-alpha-dev-1
     */
    @JvmStatic
    fun hasNullSlot(inventory: Inventory, sureNullSlot: Int): Boolean {
        var temp = 0
        for (i in 0 until inventory.size) {
            inventory.getItem(i).let {
                if (it == null || it.id == 0) {
                    temp++
                }
            }
        }
        return temp >= sureNullSlot
    }

    /**
     * <p>获取背包中物品数量
     *
     * @param inventory 玩家背包
     * @param hasItem 判断的物品
     * @return 物品数量
     * @author WCPE
     * @since 1.0.9-alpha-dev-2
     */
    @JvmStatic
    fun inventoryHasItem(inventory: Inventory, hasItem: Item): Int {
        return inventory.contents.filter {
            it.value.id != 0 && hasItem.id == it.value.id && hasItem.damage == it.value.damage && hasItem.getWcpeLibCustomName() == it.value.getWcpeLibCustomName() && ItemUtil.equalsLore(
                hasItem.lore.toMutableList(),
                it.value.lore.toMutableList()
            )
        }.map { it.value.getCount() }.sum()
    }

    /**
     * <p>清理背包中的指定物品
     *
     * @param inventory 玩家背包
     * @param clearItem 清理物品
     * @param clearAmount 清理数量
     * @author WCPE
     * @since 1.0.9-alpha-dev-2
     */
    @JvmStatic
    fun clearItem(inventory: Inventory, clearItem: Item, clearAmount: Int) {
        var tempAmount = clearAmount
        for (i in 0 until inventory.size) {
            val item = inventory.getItem(i)
            if (tempAmount <= 0) {
                return
            }
            if (item.id == 0) {
                continue
            }
            if (clearItem.id != item.id) {
                continue
            }
            if (clearItem.damage != item.damage) {
                continue
            }
            if (clearItem.name != item.name) {
                continue
            }
            if (!ItemUtil.equalsLore(clearItem.lore.toMutableList(), item.lore.toMutableList())) continue
            val count = item.getCount()
            if (tempAmount >= count) {
                inventory.clear(i)
                tempAmount -= count
                continue
            }
            item.setCount(count - tempAmount)
            tempAmount = 0
            inventory.setItem(i, item)
        }
    }

}