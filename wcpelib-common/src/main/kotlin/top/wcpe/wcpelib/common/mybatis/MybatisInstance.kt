package top.wcpe.wcpelib.common.mybatis

import org.apache.ibatis.binding.MapperRegistry
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import top.wcpe.wcpelib.common.mapper.BaseSQLMapper
import top.wcpe.wcpelib.common.mysql.MySQLDataSourceInstance
import java.util.function.Consumer
import javax.sql.DataSource

/**
 * 由 WCPE 在 2024/8/8 16:45 创建
 * <p>
 * Created by WCPE on 2024/8/8 16:45
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.8.0
 */
data class MybatisInstance(
    private val mysqlDataSourceInstance: MySQLDataSourceInstance,
) {


    private val mybatisConfiguration = Configuration()

    init {
        mybatisConfiguration.addMapper(BaseSQLMapper::class.java)
    }

    private val mapperRegistryClass = MapperRegistry::class.java
    private val knownMappersField = mapperRegistryClass.getDeclaredField("knownMappers")

    fun getDatabaseName(): String {
        return mysqlDataSourceInstance.mysqlDataSourceConfig.database
    }

    val sqlSessionFactory: SqlSessionFactory

    init {
        knownMappersField.isAccessible = true
    }

    val dataSource: DataSource = mysqlDataSourceInstance.dataSource

    init {
        val environment = Environment(
            "development", JdbcTransactionFactory(), dataSource
        )
        mybatisConfiguration.environment = environment
        this.sqlSessionFactory = SqlSessionFactoryBuilder().build(mybatisConfiguration)
    }

    fun useSession(callBack: Consumer<SqlSession>) {
        sqlSessionFactory.openSession().use {
            try {
                callBack.accept(it)
                it.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                it.rollback()
            }
        }
    }


    fun addMapper(vararg classes: Class<*>?) {
        for (clazz in classes) {
            if (clazz == null) continue
            sqlSessionFactory.configuration.addMapper(clazz)
        }
    }

    fun removeMapper(vararg classes: Class<*>?) {
        val current = sqlSessionFactory
        val getObj = knownMappersField.get(current.configuration.mapperRegistry) as? HashMap<*, *> ?: return
        for (clazz in classes) {
            if (clazz == null) continue
            getObj.remove(clazz)
        }
    }


}