package top.wcpe.wcpelib.nukkit;


import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import top.wcpe.wcpelib.common.mybatis.Mybatis;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;


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
    private static Mybatis mybatis;

    @Getter
    private static WcpeLib instance;

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
        }
        if (enableMysql) {
            long start = System.currentTimeMillis();
            try {
                this.mybatis = new Mybatis(getConfig().getString("Setting.mysql.url"), getConfig().getInt("Setting.mysql.port"), getConfig().getString("Setting.mysql.database"), getConfig().getString("Setting.mysql.user"), getConfig().getString("Setting.mysql.password"));
                long end = System.currentTimeMillis();
                log(" Mybatis 链接成功! 共耗时:" + (end - start) + "Ms");
                log(" 开始初始化默认 Mapper");
                initDefaultMapper();
                log(" 初始化默认 Mapper 成功! 共耗时:" + (System.currentTimeMillis() - end) + "Ms");
            } catch (Exception e) {
                e.printStackTrace();
                log(" §c无法链接数据库! 请确认 WcpeLib 配置文件中的数据配置填写正确!");
            }
        }


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
            playerServerMapper.createTable();
        }
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