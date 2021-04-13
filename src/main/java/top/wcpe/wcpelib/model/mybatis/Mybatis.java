package top.wcpe.wcpelib.model.mybatis;

import java.io.IOException;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.bukkit.configuration.file.FileConfiguration;

import top.wcpe.wcpelib.WcpeLib;

public class Mybatis {




    public SqlSessionFactory initMybatis(Class<?>... classes) throws IOException {
        return initMybatis(classes, null);
    }

    public SqlSessionFactory initMybatis(
            Class<?>[] classes, TypeHandlerRegistry[] typeHandlerRegistrys) throws IOException {
        FileConfiguration config = WcpeLib.getInstance().getConfig();
        PooledDataSource pooledDataSource = new PooledDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://" + config.getString("Mysql.Url") + ":" + config.getInt("Mysql.Port") + "/" + config.getString("Mysql.Database") + "?useSSL=false&characterEncoding=UTF-8", config.getString("Mysql.User"),
                config.getString("Mysql.Password"));
        pooledDataSource.setPoolMaximumActiveConnections(2000);
        pooledDataSource.setPoolMaximumIdleConnections(2000);
        pooledDataSource.setPoolTimeToWait(2000);
        pooledDataSource.setPoolPingQuery("SELECT NOW()");
        pooledDataSource.setPoolPingEnabled(true);
        Environment environment = new Environment("development", new JdbcTransactionFactory(), pooledDataSource);
        Configuration configuration = new Configuration(environment);
        for (Class<?> c : classes) {
            configuration.addMapper(c);
        }
        if (typeHandlerRegistrys != null)
            for (TypeHandlerRegistry typeHandler : typeHandlerRegistrys) {
                configuration.getTypeHandlerRegistry().register(typeHandler.getJavaTypeClass(),
                        typeHandler.getJdbcType(), typeHandler.getTypeHandlerClass());
            }
        return new SqlSessionFactoryBuilder().build(configuration);

    }
}
