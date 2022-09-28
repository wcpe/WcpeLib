package top.wcpe.wcpelib.bungeecord.adapter

import com.google.common.base.Charsets
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import top.wcpe.wcpelib.bungeecord.WcpeLib
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.SectionAdapter
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Path

/**
 * 由 WCPE 在 2022/9/28 21:38 创建
 *
 * Created by WCPE on 2022/9/28 21:38
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class ConfigAdapterBungeeCordImpl(private val file: File, private val defaultPath: String) : ConfigAdapter {

    private fun load(file: File): Configuration {
        val resource = WcpeLib.instance.getResource(defaultPath)
        return if (resource == null) {
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(file)
        } else {
            ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(
                file, ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(
                    InputStreamReader(
                        resource, Charsets.UTF_8
                    )
                )
            )
        }
    }

    private var config: Configuration = file.let {
        saveDefaultConfig()
        load(it)
    }

    override fun saveDefaultConfig() {
        if (!file.exists()) {
            WcpeLib.instance.saveResource(file.name, false)
        }
    }

    override fun reloadConfig() {
        this.config = load(file)
    }

    override fun getString(key: String): String {
        return config.getString(key) ?: ""
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
        return config.contains(key)
    }

    override fun getKeys(): Set<String> {
        return config.keys.toSet()
    }

    override fun getSection(key: String): SectionAdapter? {
        return SectionAdapterBungeeCordImpl(config.getSection(key) ?: return null)
    }

    override fun getPath(): Path {
        return file.toPath()
    }
}