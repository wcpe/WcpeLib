package top.wcpe.wcpelib.nukkit.adapter

import cn.nukkit.utils.ConfigSection
import top.wcpe.wcpelib.common.adapter.SectionAdapter

/**
 * 由 WCPE 在 2022/3/9 20:31 创建
 *
 * Created by WCPE on 2022/3/9 20:31
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-3
 */
class SectionAdapterNukkitImpl(private val configSection: ConfigSection) : SectionAdapter {
    override fun getString(key: String): String {
        return configSection.getString(key)
    }

    override fun getBoolean(key: String): Boolean {
        return configSection.getBoolean(key)
    }

    override fun getInt(key: String): Int {
        return configSection.getInt(key)
    }

    override fun getLong(key: String): Long {
        return configSection.getLong(key)
    }

    override fun getDouble(key: String): Double {
        return configSection.getDouble(key)
    }

    override fun getKeys(): Set<String> {
        return configSection.getKeys()
    }

    override fun exists(key: String): Boolean {
        return configSection.exists(key)
    }

    override fun getSection(key: String): SectionAdapter? {
        return SectionAdapterNukkitImpl((configSection.getSection(key) ?: return null))
    }
}