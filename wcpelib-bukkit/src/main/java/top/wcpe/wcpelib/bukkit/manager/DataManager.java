package top.wcpe.wcpelib.bukkit.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;


public class DataManager {
    private final Plugin plugin;
    private final HashMap<String, DataEntity> dataMap = new HashMap<>();

    public DataManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public DataEntity getDataEntityByName(String fileName) {
        DataEntity dataEntity = dataMap.get(fileName);
        if (dataEntity == null) {
            dataEntity = new DataEntity(fileName);
            dataMap.put(fileName, dataEntity);
        }
        return dataEntity;
    }

    public void saveAll() {
        dataMap.values().forEach(DataEntity::save);
    }

    public void saveAllToFile(File file) {
        dataMap.values().forEach(d -> d.save(file));
    }

    public class DataEntity {
        private final File dataFile;
        private final YamlConfiguration dataYaml;

        public DataEntity(String fileName) {
            this.dataFile = new File(plugin.getDataFolder(), fileName);
            if (!this.dataFile.exists())
                try {
                    this.dataFile.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.WARNING,
                            "[" + plugin.getName() + "] create file " + fileName + " fail!");
                    e.printStackTrace();
                }
            this.dataYaml = YamlConfiguration.loadConfiguration(dataFile);
        }

        public File getDataFile() {
            return dataFile;
        }

        public YamlConfiguration getDataYaml() {
            return dataYaml;
        }

        public void save() {
            try {
                dataYaml.save(dataFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING,
                        "[" + plugin.getName() + "] save " + dataFile.getName() + " exception！");
                e.printStackTrace();
            }
        }

        public void save(File file) {
            try {
                dataYaml.save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING,
                        "[" + plugin.getName() + "] save " + file.getName() + " exception！");
                e.printStackTrace();
            }
        }
    }

}
