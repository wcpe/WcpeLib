package top.wcpe.wcpelib.nukkit;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import top.wcpe.wcpelib.common.WcpeLibCommon;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;
import top.wcpe.wcpelib.nukkit.adapter.ConfigAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.adapter.LoggerAdapterNukkitImpl;
import top.wcpe.wcpelib.nukkit.mybatis.entity.PlayerServer;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.nukkit.placeholder.data.PlayerPlaceholder;
import top.wcpe.wcpelib.nukkit.placeholder.data.ServerPlaceholder;
import top.wcpe.wcpelib.nukkit.server.ServerInfo;

import java.io.File;
import java.util.HashMap;


/**
 * 功能描述：WcpeLib 一个自己用的破烂Nukkit插件前置
 *
 * @Author: WCPE
 * @Date: 2021/4/13 12:46
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
        final WcpeLibCommon wcpeLibCommon = new WcpeLibCommon(new LoggerAdapterNukkitImpl(), new ConfigAdapterNukkitImpl(new File(getDataFolder(), "mysql.yml")), new ConfigAdapterNukkitImpl(new File(getDataFolder(), "redis.yml")));
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
        new PlayerPlaceholder().register();
        new ServerPlaceholder().register();
    }

    private void initDefaultMapper() {
        final Long start = System.currentTimeMillis();
        getLogger().info("开始初始化默认 Mapper");
        this.mybatis.addMapper(PlayerServerMapper.class);
        SqlSessionFactory sqlSessionFactory = this.mybatis.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();

        PlayerServerMapper playerServerMapper = session.getMapper(PlayerServerMapper.class);
        if (playerServerMapper.existTable(this.mybatis.getDatabaseName()) == 0) {
            playerServerMapper.createTable();
        }
        WcpeLib.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGH)
            public void join(PlayerJoinEvent e) {
                String playerName = e.getPlayer().getName();
                SqlSession openSession = sqlSessionFactory.openSession();
                PlayerServerMapper mapper = openSession.getMapper(PlayerServerMapper.class);
                PlayerServer playerServer = mapper.selectPlayerServer(playerName);
                if (playerServer == null) {
                    mapper.insertPlayerServer(new PlayerServer(playerName, getServerName(), true));
                } else {
                    mapper.updatePlayerServer(new PlayerServer(playerName, getServerName(), true));
                }
                openSession.commit();
                openSession.close();
            }

            @EventHandler(priority = EventPriority.HIGH)
            public void quit(PlayerQuitEvent e) {
                String playerName = e.getPlayer().getName();
                SqlSession openSession = sqlSessionFactory.openSession();
                PlayerServerMapper mapper = openSession.getMapper(PlayerServerMapper.class);
                PlayerServer playerServer = mapper.selectPlayerServer(playerName);
                if (playerServer == null) {
                    mapper.insertPlayerServer(new PlayerServer(playerName, getServerName(), false));
                } else {
                    mapper.updatePlayerServer(new PlayerServer(playerName, getServerName(), false));
                }
                openSession.commit();
                openSession.close();
            }
        }, WcpeLib.getInstance());
        session.commit();
        getLogger().info("始化默认 Mapper 完成 耗时:" + (System.currentTimeMillis() - start) + " Ms");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disable！！！");
    }

}