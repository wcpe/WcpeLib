package top.wcpe.wcpelib.nukkit;


import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.wcpe.wcpelib.common.PlatformAdapter;
import top.wcpe.wcpelib.common.WcpeLibCommon;
import top.wcpe.wcpelib.common.adapter.ConfigAdapter;
import top.wcpe.wcpelib.common.adapter.LoggerAdapter;
import top.wcpe.wcpelib.common.adapter.SectionAdapter;
import top.wcpe.wcpelib.common.command.v2.AbstractCommand;
import top.wcpe.wcpelib.common.ktor.Ktor;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;
import top.wcpe.wcpelib.nukkit.adapter.ConfigAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.adapter.LoggerAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.command.v2.CommandManager;
import top.wcpe.wcpelib.nukkit.placeholder.data.PlayerPlaceholderExtend;
import top.wcpe.wcpelib.nukkit.placeholder.data.ServerPlaceholder;
import top.wcpe.wcpelib.nukkit.server.RegisterEntityInfo;
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
public final class WcpeLib extends PluginBase implements PlatformAdapter {

    @Getter
    private static final HashMap<String, ServerInfo> serverInfoMap = new HashMap<>();
    private static final HashMap<String, RegisterEntityInfo> registerEntityInfoMap = new HashMap<>();

    private static WcpeLib instance;
    private static ConfigAdapter itemConfig;
    private static ConfigAdapter registerEntityConfig;

    @Deprecated
    public static boolean isEnableMysql() {
        return WcpeLibCommon.INSTANCE.getMybatis() != null;
    }

    @Deprecated
    public static boolean isEnableRedis() {
        return WcpeLibCommon.INSTANCE.getRedis() != null;
    }

    @Deprecated
    public static boolean isEnableKtor() {
        return WcpeLibCommon.INSTANCE.getKtor() != null;
    }

    @Deprecated
    public static Mybatis getMybatis() {
        return WcpeLibCommon.INSTANCE.getMybatis();
    }

    @Deprecated
    public static Redis getRedis() {
        return WcpeLibCommon.INSTANCE.getRedis();
    }

    @Deprecated
    public static Ktor getKtor() {
        return WcpeLibCommon.INSTANCE.getKtor();
    }

    public static WcpeLib getInstance() {
        return instance;
    }

    public static String getServerName() {
        return instance.getConfig().getString("server.server-name");
    }

    public static ServerInfo getServerInfo(String serverName) {
        return serverInfoMap.get(serverName);
    }

    public static HashMap<String, RegisterEntityInfo> getRegisterEntityInfoMap() {
        return registerEntityInfoMap;
    }

    public static RegisterEntityInfo getRegisterEntityInfo(String registerEntityKey) {
        return registerEntityInfoMap.get(registerEntityKey);
    }

    public static ConfigAdapter getItemConfig() {
        return itemConfig;
    }

    public static ConfigAdapter getRegisterEntityConfig() {
        return registerEntityConfig;
    }


    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
        itemConfig.saveDefaultConfig();
        registerEntityConfig.saveDefaultConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        itemConfig.reloadConfig();
        registerEntityConfig.reloadConfig();
    }

    public void reloadOtherConfig() {
        getLogger().info("开始读取各个服务器信息");
        serverInfoMap.clear();
        ConfigSection serverInfoCfg = getConfig().getSection("server.server-info");
        for (String key : serverInfoCfg.getKeys(false)) {
            ConfigSection serverInfoCfgSection = serverInfoCfg.getSection(key);
            serverInfoMap.put(key, new ServerInfo(key, serverInfoCfgSection.getString("host"), serverInfoCfgSection.getInt("port"), serverInfoCfgSection.getString("view-name", key)));
            getLogger().info(key + ":" + serverInfoCfgSection.getString("view-name", key) + " -> " + serverInfoCfgSection.getString("host") + ":" + serverInfoCfgSection.getInt("port"));
        }
        getLogger().info("开始读取注册实体信息");
        registerEntityInfoMap.clear();
        SectionAdapter registerEntitySection = getRegisterEntityConfig().getSection("register-entity");
        if (registerEntitySection != null) {
            for (String key : registerEntitySection.getKeys(false)) {
                SectionAdapter keySection = registerEntitySection.getSection(key);
                if (keySection == null) {
                    continue;
                }
                registerEntityInfoMap.put(key, new RegisterEntityInfo(key, keySection.getBoolean("hasSpawnEgg"), keySection.getBoolean("summonAble"), keySection.getString("id"), keySection.getString("bid"), keySection.getInt("rid")));
                getLogger().info("读取 " + key + "成功 id -> " + keySection.getString("id"));
            }
        }
    }

    @Override
    public void onLoad() {
        instance = this;
        WcpeLibCommon.INSTANCE.init(this);
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        itemConfig = new ConfigAdapterNukkitImpl(new File(this.getDataFolder(), "item.yml"), this);
        registerEntityConfig = new ConfigAdapterNukkitImpl(new File(this.getDataFolder(), "register-entity.yml"), this);
        saveDefaultConfig();
        initPlaceholderExtend();
        reloadOtherConfig();

        initDefaultMapper();
        getServer().getPluginManager().registerEvents(new WcpeLibListener(), this);
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
        final long start = System.currentTimeMillis();
        getLogger().info("开始初始化默认 Mapper");
        getLogger().info("始化默认 Mapper 完成 耗时:" + (System.currentTimeMillis() - start) + " Ms");
    }

    @Override
    public boolean reloadAllConfig() {
        try {
            reloadConfig();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @NotNull
    @Override
    public LoggerAdapter createLoggerAdapter() {
        return new LoggerAdapterNukkitImpl();
    }

    @NotNull
    @Override
    public File getDataFolderFile() {
        return getDataFolder();
    }

    @NotNull
    @Override
    public ConfigAdapter createConfigAdapter(@NotNull String fileName) {
        return new ConfigAdapterNukkitImpl(new File(getDataFolder(), fileName), this);
    }

    @Override
    public boolean registerCommand(@NotNull AbstractCommand abstractCommand, @NotNull Object pluginInstance) {
        if (!(pluginInstance instanceof PluginBase)) {
            return false;
        }
        return CommandManager.registerCommand(abstractCommand, (PluginBase) pluginInstance);
    }

}