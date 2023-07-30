package top.wcpe.wcpelib.bukkit;

import lombok.Getter;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import top.wcpe.wcpelib.bukkit.adapter.ConfigAdapterBukkitImpl;
import top.wcpe.wcpelib.bukkit.adapter.LoggerAdapterBukkitImpl;
import top.wcpe.wcpelib.bukkit.command.v2.CommandManager;
import top.wcpe.wcpelib.bukkit.data.IDataManager;
import top.wcpe.wcpelib.bukkit.data.impl.MySQLDataManager;
import top.wcpe.wcpelib.bukkit.data.impl.NullDataManager;
import top.wcpe.wcpelib.bukkit.version.VersionManager;
import top.wcpe.wcpelib.common.PlatformAdapter;
import top.wcpe.wcpelib.common.WcpeLibCommon;
import top.wcpe.wcpelib.common.adapter.ConfigAdapter;
import top.wcpe.wcpelib.common.adapter.LoggerAdapter;
import top.wcpe.wcpelib.common.command.v2.AbstractCommand;
import top.wcpe.wcpelib.common.ktor.Ktor;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;

import java.io.File;

/**
 * 由 WCPE 在 2021/2/17 3:45 创建
 * <p>
 * Created by WCPE on 2022/1/2 17:07
 * <p>
 * Github: https://github.com/wcpe
 * <p>
 * QQ: 1837019522
 *
 * @author WCPE
 */
public final class WcpeLib extends JavaPlugin implements PlatformAdapter {
    @Getter
    private static WcpeLib instance;
    @Getter
    private static IDataManager dataManager = new NullDataManager();

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


    public static String getServerName() {
        return instance.getConfig().getString("server-name");
    }


    @Override
    public void onLoad() {
        instance = this;
        WcpeLibCommon.INSTANCE.init(this);
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        initDefaultMapper();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new WcpeLibListener(), this);
        getLogger().info("load time: " + (System.currentTimeMillis() - start) + " ms");
        getServer().getConsoleSender().sendMessage("§a  _       __                          __     _     __  ");
        getServer().getConsoleSender().sendMessage("§a | |     / /  _____    ____   ___    / /    (_)   / /_ ");
        getServer().getConsoleSender().sendMessage("§a | | /| / /  / ___/   / __ \\ / _ \\  / /    / /   / __ \\");
        getServer().getConsoleSender().sendMessage("§a | |/ |/ /  / /__    / /_/ //  __/ / /___ / /   / /_/ /");
        getServer().getConsoleSender().sendMessage("§a |__/|__/   \\___/   / .___/ \\___/ /_____//_/   /_.___/ ");
        getServer().getConsoleSender().sendMessage("§a                   /_/                                 ");
        val versionInfo = VersionManager.getVersionInfo();
        getLogger().info("load version: " + getServer().getVersion());
        getLogger().info("nms version: " + versionInfo.getNmsClassPath());
        getLogger().info("obc version: " + versionInfo.getObcClassPath());
    }

    private void initDefaultMapper() {
        final long start = System.currentTimeMillis();
        getLogger().info("开始初始化默认 Mapper");
        val mybatis = WcpeLibCommon.INSTANCE.getMybatis();
        if (mybatis == null) {
            getLogger().info("Mybatis 未连接 初始化失败!");
            return;
        }
        dataManager = new MySQLDataManager(mybatis);
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
        return new LoggerAdapterBukkitImpl();
    }

    @NotNull
    @Override
    public File getDataFolderFile() {
        return getDataFolder();
    }

    @NotNull
    @Override
    public ConfigAdapter createConfigAdapter(@NotNull String fileName) {
        return new ConfigAdapterBukkitImpl(new File(getDataFolder(), fileName), fileName);
    }

    @Override
    public boolean registerCommand(@NotNull AbstractCommand abstractCommand, @NotNull Object pluginInstance) {
        if (!(pluginInstance instanceof JavaPlugin)) {
            return false;
        }
        return CommandManager.registerCommand(abstractCommand, (JavaPlugin) pluginInstance);
    }

}