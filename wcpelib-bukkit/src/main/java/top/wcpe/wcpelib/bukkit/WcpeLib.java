package top.wcpe.wcpelib.bukkit;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import top.wcpe.wcpelib.bukkit.adapter.ConfigAdapterBukkitImpl;
import top.wcpe.wcpelib.bukkit.adapter.LoggerAdapterBukkitImpl;
import top.wcpe.wcpelib.common.WcpeLibCommon;
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
public final class WcpeLib extends JavaPlugin {

    private static WcpeLib instance;

    public static WcpeLib getInstance() {
        return instance;
    }

    public static String getServerName() {
        return instance.getConfig().getString("server-name");
    }

    @Getter
    private static boolean enableMysql;
    @Getter
    private static Mybatis mybatis;

    @Getter
    private static boolean enableRedis;
    @Getter
    private static Redis redis;

//    @Getter
//    private static ConfigAdapterBukkitImpl itemConfig;

    @Override
    public void saveDefaultConfig() {
        super.saveDefaultConfig();
//        itemConfig.saveDefaultConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
//        itemConfig.reloadConfig();
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
//        itemConfig = new ConfigAdapterBukkitImpl(new File(getDataFolder(), "item.yml"));
        saveDefaultConfig();
        final WcpeLibCommon wcpeLibCommon = new WcpeLibCommon(
                new LoggerAdapterBukkitImpl(),
                new ConfigAdapterBukkitImpl(new File(getDataFolder(), "mysql.yml")),
                new ConfigAdapterBukkitImpl(new File(getDataFolder(), "redis.yml")));
        mybatis = wcpeLibCommon.getMybatis();
        if (enableMysql = mybatis != null) {
            initDefaultMapper();
        }
        redis = wcpeLibCommon.getRedis();
        enableRedis = redis != null;
//        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageBase());


        getLogger().info("load time: " + (System.currentTimeMillis() - start) + " ms");
        getServer().getConsoleSender().sendMessage("§a  _       __                          __     _     __  ");
        getServer().getConsoleSender().sendMessage("§a | |     / /  _____    ____   ___    / /    (_)   / /_ ");
        getServer().getConsoleSender().sendMessage("§a | | /| / /  / ___/   / __ \\ / _ \\  / /    / /   / __ \\");
        getServer().getConsoleSender().sendMessage("§a | |/ |/ /  / /__    / /_/ //  __/ / /___ / /   / /_/ /");
        getServer().getConsoleSender().sendMessage("§a |__/|__/   \\___/   / .___/ \\___/ /_____//_/   /_.___/ ");
        getServer().getConsoleSender().sendMessage("§a                   /_/                                 ");

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