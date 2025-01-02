package top.wcpe.wcpelib.bungeecord

import net.md_5.bungee.config.Configuration

import java.io.InputStream

/**
 * 由 WCPE 在 2022/9/12 12:49 创建
 *
 * Created by WCPE on 2022/9/12 12:49
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
interface PluginConfigurationMethod {

    /**
     * 获取插件默认配置文件
     */
    fun getConfig(): Configuration

    /**
     * 重载插件默认配置文件
     */
    fun reloadConfig()

    /**
     * 释放插件默认配置文件
     */
    fun saveDefaultConfig()

    /**
     * 释放插件里的资源
     */
    fun saveResource(path: String, replace: Boolean)

    /**
     * 获取插件里的资源
     */
    fun getResource(filename: String): InputStream?

}