package top.wcpe.wcpelib.common.mybatis;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import lombok.Getter;

public class Mybatis {
    @Getter
    private final String databaseName;

    public Mybatis(String url, int port, String database, String user, String password) {
        this(url, port, database, "useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC", user, password);
    }

    public Mybatis(String url, int port, String database, String parameter, String user, String password) {
        this.databaseName = database;
        PooledDataSource pooledDataSource = new PooledDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://" + url + ":" + port + "/" + database + "?" + parameter, user,
                password);
        pooledDataSource.setPoolMaximumActiveConnections(65535);
        pooledDataSource.setPoolMaximumIdleConnections(0);
        pooledDataSource.setPoolTimeToWait(10000);
        pooledDataSource.setPoolPingEnabled(true);
        pooledDataSource.setPoolPingQuery("SELECT 1");
        Environment environment = new Environment("development", new JdbcTransactionFactory(), pooledDataSource);
        Configuration configuration = new Configuration(environment);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Getter
    private SqlSessionFactory sqlSessionFactory;


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


}
