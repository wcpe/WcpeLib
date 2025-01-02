package top.wcpe.wcpelib.common.adapter

/**
 * 由 WCPE 在 2022/3/9 20:18 创建
 *
 * Created by WCPE on 2022/3/9 20:18
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.10-alpha-dev-3
 */
interface SectionAdapter {
    fun getString(key: String): String

    fun getBoolean(key: String): Boolean

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getDouble(key: String): Double

    fun getKeys(): Set<String> {
        return getKeys(false)
    }

    fun getKeys(child: Boolean = false): Set<String>

    fun exists(key: String): Boolean

    fun getSection(key: String): SectionAdapter?
}