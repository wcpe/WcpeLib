package top.wcpe.wcpelib.bukkit.version.adapter.itemstack

import top.wcpe.wcpelib.bukkit.version.VersionInfo
import top.wcpe.wcpelib.bukkit.version.VersionManager
import top.wcpe.wcpelib.bukkit.version.adapter.itemstack.meta.ItemStackMetaAdapter1121mpl
import top.wcpe.wcpelib.bukkit.version.adapter.itemstack.meta.ItemStackMetaAdapter188Impl

/**
 * 由 WCPE 在 2022/4/3 23:20 创建
 *
 * Created by WCPE on 2022/4/3 23:20
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-1
 */
object ItemStackManager {
    @JvmStatic
    val itemMetaAdapter =
        VersionManager.versionInfo.run {
            when (versionNumber) {
                in VersionInfo.V1_8_8.versionNumber until VersionInfo.V1_12_1.versionNumber -> {
                    ItemStackMetaAdapter188Impl()
                }

                in VersionInfo.V1_12_1.versionNumber..VersionInfo.V1_18_2.versionNumber -> {
                    ItemStackMetaAdapter1121mpl()
                }

                else -> {
                    TODO("Unknown Version")
                }
            }

        }
}