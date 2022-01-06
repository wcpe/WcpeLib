package top.wcpe.wcpelib.bukkit;

import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import top.wcpe.wcpelib.bukkit.adapter.ConfigAdapterBukkitImpl;
import top.wcpe.wcpelib.bukkit.adapter.LoggerAdapterBukkitImpl;
import top.wcpe.wcpelib.bukkit.bc.PluginMessageBase;
import top.wcpe.wcpelib.bukkit.bc.utils.ServerUtil;
import top.wcpe.wcpelib.bukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.common.WcpeLibCommon;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.redis.Redis;

import java.io.File;

/**
 * 功能描述：WcpeLib 一个自己用的破烂Bukkit插件前置
 *
 * @Author: WCPE
 * @Date: 2021/2/17 3:45
 */
public final class WcpeLib extends JavaPlugin {

    private static WcpeLib instance;

    public static WcpeLib getInstance() {
        return instance;
    }

    @Getter
    private static ServerUtil serverUtil;

    public static String getServerName() {
        return instance.getConfig().getString("server-name");
    }

    @Getter
    public static boolean enableMysql;
    @Getter
    private static Mybatis mybatis;

    @Getter
    public static boolean enableRedis;
    @Getter
    private static Redis redis;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        final WcpeLibCommon wcpeLibCommon = new WcpeLibCommon(new LoggerAdapterBukkitImpl(), new ConfigAdapterBukkitImpl(new File(getDataFolder(), "mysql.yml")), new ConfigAdapterBukkitImpl(new File(getDataFolder(), "redis.yml")));
        mybatis = wcpeLibCommon.getMybatis();
        if (enableMysql = mybatis != null) {
            initDefaultMapper();
        }
        redis = wcpeLibCommon.getRedis();
        enableRedis = redis != null;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageBase());


        serverUtil = new ServerUtil();

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
        this.mybatis.addMapper(PlayerServerMapper.class);
        SqlSessionFactory sqlSessionFactory = this.mybatis.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();

        PlayerServerMapper playerServerMapper = session.getMapper(PlayerServerMapper.class);
        if (playerServerMapper.existTable(getConfig().getString("mysql.database")) != 0) {
            playerServerMapper.dropTable();
        }
        playerServerMapper.createTable();
        WcpeLib.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGH)
            public void join(PlayerJoinEvent e) {
                SqlSession openSession = sqlSessionFactory.openSession();
                PlayerServerMapper mapper = openSession.getMapper(PlayerServerMapper.class);
                mapper.delPlayerServer(e.getPlayer().getName());
                openSession.commit();
                openSession.close();
                Bukkit.getScheduler().runTaskLater(WcpeLib.getInstance(), () -> {
                    SqlSession o = sqlSessionFactory.openSession();
                    PlayerServerMapper m = o.getMapper(PlayerServerMapper.class);
                    try {
                        if (m.selectPlayerServer(e.getPlayer().getName()) == null)
                            m.insertPlayerServer(e.getPlayer().getName(), WcpeLib.getServerName());
                        m.updatePlayerServer(e.getPlayer().getName(), WcpeLib.getServerName());
                        o.commit();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        o.rollback();
                    } finally {
                        o.close();
                    }
                }, 40);

            }

            @EventHandler(priority = EventPriority.HIGH)
            public void quit(PlayerQuitEvent e) {
                SqlSession openSession = sqlSessionFactory.openSession();
                PlayerServerMapper mapper = openSession.getMapper(PlayerServerMapper.class);
                try {
                    mapper.delPlayerServer(e.getPlayer().getName());
                    openSession.commit();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    openSession.rollback();
                } finally {
                    openSession.close();
                }
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