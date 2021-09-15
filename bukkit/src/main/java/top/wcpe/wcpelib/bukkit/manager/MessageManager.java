package top.wcpe.wcpelib.bukkit.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import top.wcpe.wcpelib.common.utils.string.StringUtil;

public class MessageManager {

    private String fileName;
    private Plugin plugin;

    public MessageManager(Plugin plugin, String lang) {
        this.fileName = "Message_" + lang + ".yml";
        this.plugin = plugin;
        reload();
    }


    private Path messagePath;
    private YamlConfiguration messageYaml;

    public void reload() {
        Path dataFolderPath = plugin.getDataFolder().toPath();
        if (Files.notExists(dataFolderPath)) {
            try {
                Files.createDirectories(dataFolderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        messagePath = plugin.getDataFolder().toPath().resolve(fileName);
        if (Files.notExists(messagePath)) {
            try {
                Files.createFile(messagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.messageYaml = YamlConfiguration.loadConfiguration(messagePath.toFile());

        InputStream resource = plugin.getResource(fileName);

        if (resource == null) {
            plugin.getLogger().log(Level.WARNING,
                    "[" + plugin.getName() + "] default " + fileName + " no exists!");

        } else {
            YamlConfiguration yaml = null;
            try {
                yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(resource, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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

    private final HashMap<String, String> messageMap = new HashMap<>();

    public String getMessage(String loc, String... rep) {
        return StringUtil.replaceString(messageMap.get(loc), rep);
    }
}
