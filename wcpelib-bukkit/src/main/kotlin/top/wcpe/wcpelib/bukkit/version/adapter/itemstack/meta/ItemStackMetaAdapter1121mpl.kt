package top.wcpe.wcpelib.bukkit.version.adapter.itemstack.meta

import org.bukkit.inventory.meta.ItemMeta
import top.wcpe.wcpelib.bukkit.version.VersionInfo

/**
 * 由 WCPE 在 2022/4/3 23:26 创建
 *
 * Created by WCPE on 2022/4/3 23:26
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v
 */
class ItemStackMetaAdapter1121mpl(versionInfo: VersionInfo) : ItemStackMetaAdapter(versionInfo) {
    override fun setUnbreakable(itemMeta: ItemMeta, unbreakable: Boolean) {
        itemMeta.isUnbreakable = unbreakable
    }

    override fun isUnbreakable(itemMeta: ItemMeta): Boolean {
        return itemMeta.isUnbreakable
    }
}