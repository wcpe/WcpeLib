package top.wcpe.wcpelib.bukkit.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
	public ConfigManager(Plugin plugin) {
		this.plugin = plugin;
	}

	private Plugin plugin;
	private HashMap<String, ConfigEntity> configMap = new HashMap<>();

	public ConfigEntity getConfigEntityByName(String fileName) {
		ConfigEntity configEntity = configMap.get(fileName);
		if (configEntity == null) {
			configEntity = new ConfigEntity(fileName);
			configMap.put(fileName, configEntity);
		}
		return configEntity;
	}

	public class ConfigEntity {
		public ConfigEntity(String fileName) {
			this.configFile = new File(plugin.getDataFolder(), fileName);
			if (!this.configFile.exists()) {
				InputStream resource = plugin.getResource(fileName);
				if (resource == null) {
					plugin.getLogger().log(Level.WARNING,
							"[" + plugin.getName() + "] default " + fileName + " no exists!");
				} else {
					try {
						configYaml = YamlConfiguration.loadConfiguration(new InputStreamReader(resource, "utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					try {
						configYaml.save(configFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
			this.configYaml = YamlConfiguration.loadConfiguration(configFile);
		}

		private File configFile;
		private YamlConfiguration configYaml;

		public YamlConfiguration getConfig() {
			return configYaml;
		}

		public void reloadConfig() {
			this.configYaml = YamlConfiguration.loadConfiguration(configFile);
		}
	}

}
