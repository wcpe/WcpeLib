package top.wcpe.wcpelib.bukkit.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.PlayerInventory

/**
 * 由 WCPE 在 2022/7/20 1:12 创建
 *
 * Created by WCPE on 2022/7/20 1:12
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.17-alpha-dev-2
 */
object InventoryUtil {

    @JvmStatic
    fun getEmptySlot(inventory: Inventory): Int {
        var emptySlotNumber = 0
        for (i in 0 until inventory.size) {
            inventory.getItem(i) ?: emptySlotNumber++
        }
        return emptySlotNumber
    }

    @JvmStatic
    fun getContentsEmptySlot(inventory: PlayerInventory): Int {
        var emptySlotNumber = 0
        for (i in 0 until inventory.size) {
            inventory.getItem(i) ?: emptySlotNumber++
        }

        val off = inventory.itemInOffHand
        if (off == null || off.type == Material.AIR) {
            emptySlotNumber--
        }
        inventory.helmet ?: emptySlotNumber--
        inventory.chestplate ?: emptySlotNumber--
        inventory.leggings ?: emptySlotNumber--
        inventory.boots ?: emptySlotNumber--
        return emptySlotNumber
    }
}