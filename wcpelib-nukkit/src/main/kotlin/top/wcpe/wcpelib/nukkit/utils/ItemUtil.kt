package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.item.Item
import top.wcpe.wcpelib.nukkit.WcpeLib

/**
 * 由 WCPE 在 2022/4/3 22:36 创建
 *
 * Created by WCPE on 2022/4/3 22:36
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-4
 */
object ItemUtil {

    @JvmStatic
    fun getStringInItemLoreIndex(itemLore: MutableList<String>, str: String): Int {
        if (itemLore.isEmpty()) {
            return -1
        }
        for (i in itemLore.indices) {
            if (itemLore[i].contains(str)) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun itemLoreContainLore(configLore: MutableList<String>, itemLore: MutableList<String>): Boolean {
        if (configLore.size > itemLore.size) return false
        for (i in configLore.indices) {
            if (!itemLore.contains(configLore[i])) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun equalsLore(lores: MutableList<String>, lores2: MutableList<String>): Boolean {
        if (lores.isEmpty() && lores2.isEmpty()) {
            return true
        }
        if (lores.size != lores2.size) return false
        for (i in lores.indices) {
            if (lores[i] != lores2[i]) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun getItemCustomName(item: Item): String {
        return item.run {
            if (hasCustomName()) name
            else
                (WcpeLib.getItemConfig().getSection("item-custom-name") ?: return name).let {
                    return if (it.exists("${this.id}:${this.damage}"))
                        it.getString("${this.id}:${this.damage}")
                    else
                        name
                }
        }
    }

}