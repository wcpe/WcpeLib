package top.wcpe.wcpelib.nukkit;


import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import top.wcpe.wcpelib.common.WcpeLibCommon;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;
import top.wcpe.wcpelib.nukkit.adapter.ConfigAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.adapter.LoggerAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.placeholder.data.PlayerPlaceholderExtend;
import top.wcpe.wcpelib.nukkit.placeholder.data.ServerPlaceholder;
import top.wcpe.wcpelib.nukkit.server.ServerInfo;

import java.io.File;
import java.util.HashMap;


/**
 * 由 WCPE 在 2021/4/13 12:46 创建
 * <p>
 * Created by WCPE on 2022/1/2 17:07
 * <p>
 * Github: https://github.com/wcpe
 * <p>
 * QQ: 1837019522
 *
 * @author WCPE
 */
public final class WcpeLib extends PluginBase {

    private static WcpeLib instance;

    public static WcpeLib getInstance() {
        return instance;
    }

    public static String getServerName() {
        return instance.getConfig().getString("server.server-name");
    }

    @Getter
    public static boolean enableMysql;
    @Getter
    private static Mybatis mybatis;

    @Getter
    public static boolean enableRedis;
    @Getter
    private static Redis redis;

    @Getter
    private static final HashMap<String, ServerInfo> serverInfoMap = new HashMap<>();

    public static ServerInfo getServerInfo(String serverName) {
        return serverInfoMap.get(serverName);
    }


    private static Config itemConfig;

    public static Config getItemConfig() {
        return itemConfig;
    }

    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
        File itemFile = new File(this.getDataFolder(), "item.yml");
        if (!itemFile.exists()) {
            this.saveResource("item.yml", false);
        }
        itemConfig = new Config(itemFile);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        itemConfig.reload();
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        initPlaceholderExtend();

        getLogger().info("开始读取各个服务器信息");
        ConfigSection serverInfoCfg = getConfig().getSection("server.server-info");
        for (String key : serverInfoCfg.getKeys(false)) {
            ConfigSection serverInfoCfgSection = serverInfoCfg.getSection(key);
            serverInfoMap.put(key, new ServerInfo(key, serverInfoCfgSection.getString("host"), serverInfoCfgSection.getInt("port")));
            getLogger().info(key + " -> " + serverInfoCfgSection.getString("host") + ":" + serverInfoCfgSection.getInt("port"));
        }
        final WcpeLibCommon wcpeLibCommon = new WcpeLibCommon(
                new LoggerAdapterNukkitImpl(),
                new ConfigAdapterNukkitImpl(new File(getDataFolder(), "mysql.yml")),
                new ConfigAdapterNukkitImpl(new File(getDataFolder(), "redis.yml")));
        mybatis = wcpeLibCommon.getMybatis();
        if (enableMysql = mybatis != null) {
            initDefaultMapper();
        }
        redis = wcpeLibCommon.getRedis();
        enableRedis = redis != null;

        new WcpeLibCommands();
        getLogger().info("load time: §e" + (System.currentTimeMillis() - start) + " ms");
        getServer().getConsoleSender().sendMessage("§a  _       __                          __     _     __  ");
        getServer().getConsoleSender().sendMessage("§a | |     / /  _____    ____   ___    / /    (_)   / /_ ");
        getServer().getConsoleSender().sendMessage("§a | | /| / /  / ___/   / __ \\ / _ \\  / /    / /   / __ \\");
        getServer().getConsoleSender().sendMessage("§a | |/ |/ /  / /__    / /_/ //  __/ / /___ / /   / /_/ /");
        getServer().getConsoleSender().sendMessage("§a |__/|__/   \\___/   / .___/ \\___/ /_____//_/   /_.___/ ");
        getServer().getConsoleSender().sendMessage("§a                   /_/                                 ");

    }

    private void initPlaceholderExtend() {
        new PlayerPlaceholderExtend().register();
        new ServerPlaceholder().register();
    }

    private void initDefaultMapper() {
        final Long start = System.currentTimeMillis();
        getLogger().info("开始初始化默认 Mapper");
        getLogger().info("始化默认 Mapper 完成 耗时:" + (System.currentTimeMillis() - start) + " Ms");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disable！！！");
    }

}