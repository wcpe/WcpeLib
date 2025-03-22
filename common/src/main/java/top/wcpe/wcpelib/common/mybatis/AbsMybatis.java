package top.wcpe.wcpelib.common.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.util.function.Consumer;

public abstract class AbsMybatis {
    protected MybatisInstance mybatisInstance;
    protected String dataBaseName;

    public AbsMybatis(MybatisInstance mybatisInstance) {
        this.mybatisInstance = mybatisInstance;
        this.dataBaseName = mybatisInstance.getDatabaseName();
    }

    public void addMapper(Class<?>... classes) {
        mybatisInstance.addMapper(classes);
    }
    public void removeMapper(Class<?>... classes) {
        mybatisInstance.removeMapper(classes);
    }

    public DataSource getDataSource() {
        return mybatisInstance.getDataSource();
    }

    public void useSession(Consumer<SqlSession> callback) {
        mybatisInstance.useSession(callback);
    }
    public SqlSessionFactory getSqlSessionFactory() {
        return mybatisInstance.getSqlSessionFactory();
    }

    public MybatisInstance getMybatisInstance() {
        return mybatisInstance;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }
}
