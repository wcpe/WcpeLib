package top.wcpe.wcpelib.common.adapter


/**
 * 由 WCPE 在 2022/1/3 22:19 创建
 *
 * Created by WCPE on 2022/1/3 22:19
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
interface ConfigAdapter {

    fun saveDefaultConfig()

    fun reloadConfig()


    fun getString(key: String): String

    fun getBoolean(key: String): Boolean

    fun getInt(key: String): Int

    fun getLong(key: String): Long

    fun getDouble(key: String): Double

}