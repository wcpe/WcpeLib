package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.inventory.Inventory

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

}