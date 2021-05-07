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

import top.wcpe.wcpelib.bukkit.bc.PluginMessageBase;
import top.wcpe.wcpelib.bukkit.bc.utils.ServerUtil;
import top.wcpe.wcpelib.bukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.common.mybatis.Mybatis;

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
    private static Mybatis mybatis;

    @Getter
    private static WcpeLib instance;

    @Getter
    private static ServerUtil serverUtil;

    public static String getServerName() {
        return instance.getConfig().getString("ServerName");
    }

    @Getter
    public static boolean enableMysql;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if (enableMysql = getConfig().getBoolean("Setting.mysql.enable")) {
            log(" Mybatis 开启! 开始连接数据库");
            long start = System.currentTimeMillis();
            try {
                this.mybatis = new Mybatis(getConfig().getString("Setting.mysql.url"), getConfig().getInt("Setting.mysql.port"), getConfig().getString("Setting.mysql.database"), getConfig().getString("Setting.mysql.user"), getConfig().getString("Setting.mysql.password"));
                long end = System.currentTimeMillis();
                log(" Mybatis 链接成功! 共耗时:" + (end - start) + "Ms");
                log(" 开始初始化默认 Mapper");
                initDefaultMapper();
                log(" 初始化默认 Mapper 成功! 共耗时:" + (System.currentTimeMillis() - end) + "Ms");
            } catch (Exception e) {
                log(" §c无法链接数据库! 请确认 WcpeLib 配置文件中的数据配置填写正确!");
            }
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageBase());


        serverUtil = new ServerUtil();
        log(" Enable！！！");
    }

    private void initDefaultMapper() {
        this.mybatis.addMapper(PlayerServerMapper.class);
        SqlSessionFactory sqlSessionFactory = this.mybatis.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession();

        PlayerServerMapper playerServerMapper = session.getMapper(PlayerServerMapper.class);
        if (playerServerMapper.existTable() == 0) {
            playerServerMapper.createTable();
        } else {
            playerServerMapper.dropTable();
        }
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