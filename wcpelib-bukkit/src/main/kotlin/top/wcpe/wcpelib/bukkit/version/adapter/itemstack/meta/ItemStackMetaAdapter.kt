package top.wcpe.wcpelib.bukkit.version.adapter.itemstack.meta

import org.bukkit.inventory.meta.ItemMeta
import top.wcpe.wcpelib.bukkit.version.VersionInfo

/**
 * 由 WCPE 在 2022/4/3 23:22 创建
 *
 * Created by WCPE on 2022/4/3 23:22
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
abstract class ItemStackMetaAdapter(val versionInfo: VersionInfo)  {
    abstract fun setUnbreakable(itemMeta: ItemMeta, unbreakable: Boolean)
    abstract fun isUnbreakable(itemMeta: ItemMeta): Boolean
}