package top.wcpe.wcpelib.model.mybatis;

import java.util.List;

import lombok.Getter;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.wcpe.wcpelib.WcpeLib;
import top.wcpe.wcpelib.model.mybatis.mapper.PlayerServerMapper;

public class Mybatis {

    private Mybatis() {
        initMybatis();
    }

    @Getter
    private SqlSessionFactory sqlSessionFactory;

    public static Mybatis createMybatis() {
        return createMybatis(true);
    }

    public static Mybatis createMybatis(boolean initDefault) {
        Mybatis mybatis = new Mybatis();
        if (initDefault && WcpeLib.isEnableMysql()) {
            mybatis.initDefault();
        }
        return mybatis;
    }


    public void addMapper(Class... classes) {
        if (sqlSessionFactory != null)
            for (Class clazz : classes) {
                sqlSessionFactory.getConfiguration().addMapper(clazz);
            }
    }

    public void addTypeHandler(TypeHandlerRegistry... typeHandlerRegistriess) {
        if (sqlSessionFactory != null)
            for (TypeHandlerRegistry typeHandlerRegistries : typeHandlerRegistriess) {
                sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(typeHandlerRegistries.getJavaTypeClass(), typeHandlerRegistries.getJdbcType(), typeHandlerRegistries.getTypeHandlerClass());
            }
    }


    private void initDefault() {
        addMapper( PlayerServerMapper.class);
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


    public void initMybatis() {
        FileConfiguration config = WcpeLib.getInstance().getConfig();
        PooledDataSource pooledDataSource = new PooledDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://" + config.getString("Mysql.url") + ":" + config.getInt("Mysql.port") + "/" + config.getString("Mysql.database") + "?useSSL=false&characterEncoding=UTF-8", config.getString("Mysql.user"),
                config.getString("Mysql.password"));
        pooledDataSource.setPoolMaximumActiveConnections(2000);
        pooledDataSource.setPoolMaximumIdleConnections(2000);
        pooledDataSource.setPoolTimeToWait(2000);
        pooledDataSource.setPoolPingQuery("SELECT NOW()");
        pooledDataSource.setPoolPingEnabled(true);
        Environment environment = new Environment("development", new JdbcTransactionFactory(), pooledDataSource);
        Configuration configuration = new Configuration(environment);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
