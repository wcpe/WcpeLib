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
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;
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
    public void log(String log) {
        getServer().getConsoleSender().sendMessage("§a[§e" + this.getName() + "§a]§r" + log);
    }


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

    public void saveDefaultFile(File itemFile) {
        if (!itemFile.exists()) {
            this.saveResource(itemFile.getName(), false);
        }
    }

    @Getter
    private static Config itemConfig;


    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();

        File itemFile = new File(this.getDataFolder(), "item.yml");
        saveDefaultFile(itemFile);
        itemConfig = new Config(itemFile);

        initPlaceholderExtend();

        log("开始读取各个服务器信息");
        ConfigSection serverInfoCfg = getConfig().getSection("server.server-info");
        for (String key : serverInfoCfg.getKeys(false)) {
            ConfigSection serverInfoCfgSection = serverInfoCfg.getSection(key);
            serverInfoMap.put(key, new ServerInfo(key, serverInfoCfgSection.getString("host"), serverInfoCfgSection.getInt("port")));
            log(key + " -> " + serverInfoCfgSection.getString("host") + ":" + serverInfoCfgSection.getInt("port"));
        }
        if (enableMysql = getConfig().getBoolean("mysql.enable")) {
            log(" Mybatis 开启! 开始连接数据库");
            long s = System.currentTimeMillis();
            try {
                this.mybatis = new Mybatis(getConfig().getString("mysql.url"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.user"), getConfig().getString("mysql.password"));
                long end = System.currentTimeMillis();
                log(" Mybatis 链接成功! 共耗时:" + (end - s) + "Ms");
                log(" 开始初始化默认 Mapper");
                initDefaultMapper();
                log(" 初始化默认 Mapper 成功! 共耗时:" + (System.currentTimeMillis() - end) + "Ms");
            } catch (Exception e) {
                e.printStackTrace();
                log(" §c无法链接数据库! 请确认数据库开启，并且 WcpeLib 配置文件中的数据配置填写正确!");
                this.enableMysql = false;
            }
        }

        if (enableRedis = getConfig().getBoolean("redis.enable")) {
            log(" Redis 开启! 开始连接!");
            long s = System.currentTimeMillis();
            try {
                ConfigSection redisSection = getConfig().getSection("redis");
                String password = redisSection.getString("password");
                this.redis = new Redis(redisSection.getString("url"), redisSection.getInt("port"), redisSection.getInt("time-out"), password == null || password.isEmpty() ? null : password, redisSection.getInt("max-total"), redisSection.getInt("max-idle"), redisSection.getInt("min-idle"), redisSection.getBoolean("jmx-enabled"), redisSection.getBoolean("test-on-create"), redisSection.getBoolean("block-when-exhausted"), redisSection.getInt("max-wait-millis"), redisSection.getBoolean("test-on-borrow"), redisSection.getBoolean("test-on-return"));
                log(" Redis 链接成功! 共耗时:" + (System.currentTimeMillis() - s) + "Ms");
                log(" host->" + redisSection.getString("url") + ",port->" + redisSection.getInt("port"));
            } catch (Exception e) {
                e.printStackTrace();
                log(" §c无法链接 Redis ! 请确认 Redis 开启，并且 WcpeLib 配置文件中的 Redis 配置填写正确!");
                this.enableRedis = false;
            }
        }
        log("§aload time: §e" + (System.currentTimeMillis() - start) + " §ams");
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
        this.mybatis.addMapper(PlayerServerMapper.class);
        SqlSessionFactory sqlSessionFactory = this.mybatis.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();

        PlayerServerMapper playerServerMapper = session.getMapper(PlayerServerMapper.class);
        if (playerServerMapper.existTable(getConfig().getString("mysql.database")) == 0) {
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
    }

    @Override
    public void onDisable() {
        log(" Disable！！！");
    }

}