package top.wcpe.wcpelib.bukkit.version

import org.bukkit.Bukkit

/**
 * 由 WCPE 在 2022/4/1 16:47 创建
 *
 * Created by WCPE on 2022/4/1 16:47
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-4
 */
object VersionManager {

    @JvmStatic
    val versionInfo: VersionInfo by lazy {
        Regex(".*\\(MC: ").replace(Bukkit.getServer().version, "").replace(")", "").replace(".", "").toInt().let {
            for (value in VersionInfo.values()) {
                if (it == value.versionNumber)
                    return@let value
            }
            VersionInfo.Unknown
        }
    }
}