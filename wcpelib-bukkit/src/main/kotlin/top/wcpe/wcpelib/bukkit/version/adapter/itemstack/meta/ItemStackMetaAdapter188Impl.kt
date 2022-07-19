package top.wcpe.wcpelib.bukkit.version.adapter.itemstack.meta

import org.bukkit.inventory.meta.ItemMeta

/**
 * 由 WCPE 在 2022/4/3 23:24 创建
 *
 * Created by WCPE on 2022/4/3 23:24
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
class ItemStackMetaAdapter188Impl : ItemStackMetaAdapter {
    override fun setUnbreakable(itemMeta: ItemMeta, unbreakable: Boolean) {
        itemMeta.spigot().isUnbreakable = unbreakable
    }

    override fun isUnbreakable(itemMeta: ItemMeta): Boolean {
        return itemMeta.spigot().isUnbreakable
    }

}