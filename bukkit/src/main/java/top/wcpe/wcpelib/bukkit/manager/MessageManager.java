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
import top.wcpe.wcpelib.common.utils.string.StringUtil;

public class MessageManager {
    private final HashMap<String, String> messageMap = new HashMap<>();
    private File messageFile;
    private YamlConfiguration messageYaml;

    public MessageManager(Plugin plugin, String lang) {
        String fileName = "Message_" + lang + ".yml";
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.messageFile = new File(plugin.getDataFolder(), fileName);
        if (!this.messageFile.exists())
            try {
                this.messageFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING,
                        " create file " + fileName + " fail!");

                e.printStackTrace();
            }
        this.messageYaml = YamlConfiguration.loadConfiguration(messageFile);

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
            for (String s : yaml.getKeys(false)) {
                if (!messageYaml.isString(s))
                    messageYaml.set(s, yaml.getString(s));
                messageMap.put(s, yaml.getString(s));
            }
            try {
                messageYaml.save(messageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
