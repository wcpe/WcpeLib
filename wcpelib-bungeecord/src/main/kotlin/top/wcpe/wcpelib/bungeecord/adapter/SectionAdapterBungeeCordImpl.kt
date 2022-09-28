package top.wcpe.wcpelib.bungeecord.adapter

import net.md_5.bungee.config.Configuration
import top.wcpe.wcpelib.common.adapter.SectionAdapter

/**
 * 由 WCPE 在 2022/9/28 21:39 创建
 *
 * Created by WCPE on 2022/9/28 21:39
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class SectionAdapterBungeeCordImpl(private val configurationSection: Configuration) : SectionAdapter {
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

    override fun getKeys(child: Boolean): Set<String> {
        return configurationSection.keys.toSet()
    }

    override fun exists(key: String): Boolean {
        return configurationSection.contains(key)
    }

    override fun getSection(key: String): SectionAdapter? {
        return SectionAdapterBungeeCordImpl((configurationSection.getSection(key) ?: return null))
    }
}