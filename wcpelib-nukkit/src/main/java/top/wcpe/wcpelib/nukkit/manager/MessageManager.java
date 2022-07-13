package top.wcpe.wcpelib.nukkit.manager;


import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.LogLevel;
import top.wcpe.wcpelib.common.utils.string.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class MessageManager {
    private final HashMap<String, String> messageMap = new HashMap<>();
    private final String fileName;
    private final Plugin plugin;
    private Path messagePath;
    private Config messageYaml;

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
        messagePath = plugin.getDataFolder().toPath().resolve(fileName);
        if (Files.notExists(messagePath)) {
            try {
                Files.createFile(messagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.messageYaml = new Config(messagePath.toFile());

        InputStream resource = plugin.getResource(fileName);

        if (resource == null) {
            plugin.getLogger().log(LogLevel.WARNING,
                    "default " + fileName + " no exists!");

        } else {
            Config yaml = new Config();
            yaml.load(resource);
            for (String s : yaml.getKeys(true)) {
                if (!messageYaml.isString(s))
                    messageYaml.set(s, yaml.get(s));
                messageMap.put(s, yaml.getString(s));
            }
            messageYaml.save(messagePath.toFile());

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
