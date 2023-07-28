package top.wcpe.wcpelib.common.adapter

import java.nio.file.Path


/**
 * 由 WCPE 在 2022/1/3 22:19 创建
 *
 * Created by WCPE on 2022/1/3 22:19
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.7-alpha-dev-1
 */
interface ConfigAdapter {

    fun saveDefaultConfig()

    fun reloadConfig()

    fun getStringList(key: String): List<String>

    fun getString(key: String): String

    fun getBoolean(key: String): Boolean

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getDouble(key: String): Double

    fun exists(key: String): Boolean

    fun getKeys(): Set<String>

    fun getSection(key: String): SectionAdapter?

    fun getPath(): Path
}