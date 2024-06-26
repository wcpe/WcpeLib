package top.wcpe.wcpelib.nukkit.adapter

import cn.nukkit.plugin.Plugin
import cn.nukkit.utils.Config
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.SectionAdapter
import top.wcpe.wcpelib.nukkit.WcpeLib
import java.io.File
import java.nio.file.Path

/**
 * 由 WCPE 在 2022/1/3 23:14 创建
 *
 * Created by WCPE on 2022/1/3 23:14
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class ConfigAdapterNukkitImpl(
    private val file: File, private val pluginInstance: Plugin = WcpeLib.getInstance(),
) : ConfigAdapter {

    private var config: Config = file.let {
        saveDefaultConfig()
        Config(it)
    }

    override fun saveDefaultConfig() {
        if (!file.exists()) {
            pluginInstance.saveResource(file.name, false)
        }
    }

    override fun reloadConfig() {
        config.reload()
    }

    override fun getStringList(key: String): List<String> {
        return if (config.isList(key)) {
            config.getStringList(key)
        } else {
            listOf(config.getString(key))
        }
    }

    override fun getString(key: String): String {
        return config.getString(key)
    }

    override fun getBoolean(key: String): Boolean {
        return config.getBoolean(key)
    }

    override fun getInt(key: String): Int {
        return config.getInt(key)
    }

    override fun getLong(key: String): Long {
        return config.getLong(key)
    }

    override fun getDouble(key: String): Double {
        return config.getDouble(key)
    }

    override fun exists(key: String): Boolean {
        return config.exists(key)
    }

    override fun getKeys(): Set<String> {
        return config.getKeys(false)
    }

    override fun getSection(key: String): SectionAdapter {
        return SectionAdapterNukkitImpl(config.getSection(key))
    }

    override fun getPath(): Path {
        return file.toPath()
    }
}