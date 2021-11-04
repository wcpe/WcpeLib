package top.wcpe.wcpelib.bukkit;

import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import top.wcpe.wcpelib.bukkit.bc.PluginMessageBase;
import top.wcpe.wcpelib.bukkit.bc.utils.ServerUtil;
import top.wcpe.wcpelib.bukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.common.readis.Redis;

/**
 * 功能描述：WcpeLib 一个自己用的破烂Bukkit插件前置
 *
 * @Author: WCPE
 * @Date: 2021/2/17 3:45
 */
public final class WcpeLib extends JavaPlugin {
    public void log(String log) {
        getServer().getConsoleSender().sendMessage("§a[§e" + this.getName() + "§a]§r" + log);
    }


    @Getter
    private static WcpeLib instance;

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
        if (enableMysql = getConfig().getBoolean("mysql.enable")) {
            log(" Mybatis 开启! 开始连接数据库");
            try {
                this.mybatis = new Mybatis(getConfig().getString("mysql.url"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.user"), getConfig().getString("mysql.password"));
                initDefaultMapper();
                log(" Mybatis 链接成功! 初始化默认 Mapper 成功! 共耗时:" + (System.currentTimeMillis() - start) + "Ms");
            } catch (Exception e) {
                log(" §c无法链接数据库! 请确认数据库开启，并且 WcpeLib 配置文件中的数据配置填写正确!");
                this.enableMysql = false;
            }
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageBase());


        serverUtil = new ServerUtil();

        if (enableRedis = getConfig().getBoolean("redis.enable")) {
            log(" Redis 开启! 开始连接!");
            long s = System.currentTimeMillis();
            try {
                ConfigurationSection redisSection = getConfig().getConfigurationSection("setting").getConfigurationSection("redis");
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
    }

    @Override
    public void onDisable() {
        log(" Disable！！！");
    }
}