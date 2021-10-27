package top.wcpe.wcpelib.nukkit;


import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.readis.Redis;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.nukkit.server.ServerInfo;

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

    @Getter
    private static WcpeLib instance;

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

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        if (enableMysql = getConfig().getBoolean("setting.mysql.enable")) {
            log(" Mybatis 开启! 开始连接数据库");
            long s = System.currentTimeMillis();
            try {
                this.mybatis = new Mybatis(getConfig().getString("setting.mysql.url"), getConfig().getInt("setting.mysql.port"), getConfig().getString("setting.mysql.database"), getConfig().getString("setting.mysql.user"), getConfig().getString("setting.mysql.password"));
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

        if (enableRedis = getConfig().getBoolean("setting.redis.enable")) {
            log(" Redis 开启! 开始连接!");
            long s = System.currentTimeMillis();
            try {
                ConfigSection redisSection = getConfig().getSection("setting").getSection("redis");
                this.redis = new Redis(redisSection.getString("url"), redisSection.getInt("port"), redisSection.getInt("max-total"), redisSection.getInt("max-idle"), redisSection.getInt("min-idle"), redisSection.getInt("max-wait-millis"), redisSection.getBoolean("test-on-borrow"), redisSection.getBoolean("test-on-return"));
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

    private void initDefaultMapper() {
        this.mybatis.addMapper(PlayerServerMapper.class);
        SqlSessionFactory sqlSessionFactory = this.mybatis.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();

        PlayerServerMapper playerServerMapper = session.getMapper(PlayerServerMapper.class);
        if (playerServerMapper.existTable(getConfig().getString("setting.mysql.database")) != 0) {
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
                Server.getInstance().getScheduler().scheduleDelayedTask(WcpeLib.getInstance(), () -> {
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
    }

    @Override
    public void onDisable() {
        log(" Disable！！！");
    }

}