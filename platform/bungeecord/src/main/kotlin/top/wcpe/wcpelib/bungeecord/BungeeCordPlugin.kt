package top.wcpe.wcpelib.bungeecord

import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.logging.Level

/**
 * 由 WCPE 在 2022/9/28 21:07 创建
 *
 * Created by WCPE on 2022/9/28 21:07
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
open class BungeeCordPlugin : Plugin(), PluginConfigurationMethod {

    private val configFile = File(dataFolder, "config.yml")

    private var config: Configuration? = null

    override fun getConfig(): Configuration {
        if (config == null) {
            reloadConfig()
        }
        return config!!
    }


    override fun reloadConfig() {
        config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(configFile)
    }

    override fun saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource("config.yml", false)
        }
    }

    override fun saveResource(path: String, replace: Boolean) {
        require(path.isNotEmpty()) { "ResourcePath cannot be empty" }

        val resourcePath = path.replace('\\', '/')

        val resource = getResource(resourcePath)
            ?: throw IllegalArgumentException("The embedded resource '$resourcePath' cannot be found")

        val outFile = File(dataFolder, resourcePath)
        val lastIndex = resourcePath.lastIndexOf('/')
        val outDir = File(dataFolder, resourcePath.substring(0, if (lastIndex >= 0) lastIndex else 0))
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        try {
            if (!outFile.exists() || replace) {
                outFile.writeBytes(resource.readBytes())
            } else {
                logger.log(
                    Level.WARNING,
                    "Could not save " + outFile.name + " to " + outFile + " because " + outFile.name + " already exists."
                )
            }
        } catch (ex: IOException) {
            logger.log(Level.SEVERE, "Could not save " + outFile.name + " to " + outFile, ex)
        }
    }

    override fun getResource(filename: String): InputStream? {
        return try {
            val url = javaClass.classLoader.getResource(filename) ?: return null
            val connection = url.openConnection()
            connection.useCaches = false
            connection.getInputStream()
        } catch (ex: IOException) {
            null
        }
    }

}