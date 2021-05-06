package top.wcpe.wcpelib.nukkit.manager;


import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.LogLevel;
import top.wcpe.wcpelib.common.utils.string.StringUtil;

import java.io.*;
import java.util.HashMap;

public class MessageManager {
    private final HashMap<String, String> messageMap = new HashMap<>();
    private File messageFile;
    private Config messageYaml;

    public MessageManager(Plugin plugin, String lang) {
        String fileName = "Message_" + lang + ".yml";
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.messageFile = new File(plugin.getDataFolder(), fileName);
        if (!this.messageFile.exists())
            try {
                this.messageFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(LogLevel.WARNING,
                        " create file " + fileName + " fail!");

                e.printStackTrace();
            }
        this.messageYaml = new Config(messageFile);

        InputStream resource = plugin.getResource(fileName);

        if (resource == null) {
            plugin.getLogger().log(LogLevel.WARNING,
                    "[" + plugin.getName() + "] default " + fileName + " no exists!");

        } else {
            Config yaml = new Config();
            yaml.load(resource);
            for (String s : yaml.getKeys(false)) {
                if (!messageYaml.isString(s))
                    messageYaml.set(s, yaml.getString(s));
                messageMap.put(s, yaml.getString(s));
            }
            messageYaml.save(messageFile);

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
