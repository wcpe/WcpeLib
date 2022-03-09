package top.wcpe.wcpelib.bukkit.adapter

import org.bukkit.configuration.ConfigurationSection
import top.wcpe.wcpelib.common.adapter.SectionAdapter

/**
 * 由 WCPE 在 2022/3/9 20:19 创建
 *
 * Created by WCPE on 2022/3/9 20:19
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10.-alpha-dev-3
 */
class SectionAdapterBukkitImpl(private val configurationSection: ConfigurationSection) : SectionAdapter {
    override fun getString(key: String): String {
        return configurationSection.getString(key)
    }

    override fun getBoolean(key: String): Boolean {
        return configurationSection.getBoolean(key)
    }

    override fun getInt(key: String): Int {
        return configurationSection.getInt(key)
    }

    override fun getLong(key: String): Long {
        return configurationSection.getLong(key)
    }

    override fun getDouble(key: String): Double {
        return configurationSection.getDouble(key)
    }

    override fun getKeys(): Set<String> {
        return configurationSection.getKeys(false)
    }

    override fun exists(key: String): Boolean {
        return configurationSection.contains(key)
    }

    override fun getSection(key: String): SectionAdapter? {
        return SectionAdapterBukkitImpl(configurationSection.getConfigurationSection(key) ?: return null)
    }
}