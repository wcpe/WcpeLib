package top.wcpe.wcpelib.bukkit.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import top.wcpe.wcpelib.common.utils.string.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;

public class MessageManager {

    private final HashMap<String, String> messageMap = new HashMap<>();
    private final String fileName;
    private final Plugin plugin;

    public MessageManager(Plugin plugin, String lang) {
        this.fileName = "Message_" + lang + ".yml";
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        Path dataFolderPath = plugin.getDataFolder().toPath();
        if (Files.notExists(dataFolderPath)) {
            try {
                Files.createDirectories(dataFolderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path messagePath = plugin.getDataFolder().toPath().resolve(fileName);
        if (Files.notExists(messagePath)) {
            try {
                Files.createFile(messagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration messageYaml = YamlConfiguration.loadConfiguration(messagePath.toFile());

        InputStream resource = plugin.getResource(fileName);

        if (resource == null) {
            plugin.getLogger().log(Level.WARNING,
                    "[" + plugin.getName() + "] default " + fileName + " no exists!");

        } else {
            YamlConfiguration yaml = null;
            yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8));
            for (String s : yaml.getKeys(true)) {
                if (!messageYaml.isString(s))
                    messageYaml.set(s, yaml.get(s));
                messageMap.put(s, yaml.getString(s));
            }
            try {
                messageYaml.save(messagePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        messageMap.clear();
        for (String s : messageYaml.getKeys(false)) {
            if (messageYaml.isString(s)) {
                messageMap.put(s, messageYaml.getString(s));
            }
        }

    }

    public String getMessage(String loc, String... rep) {
        return StringUtil.replaceString(messageMap.get(loc), rep);
    }
}
