package top.wcpe.wcpelib.common.mybatis

import com.alibaba.druid.pool.DruidDataSource
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import top.wcpe.wcpelib.common.mapper.BaseSQLMapper
import java.util.function.Consumer


/**
 * 由 WCPE 在 2022/1/3 21:47 创建
 *
 * Created by WCPE on 2022/1/3 21:47
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.7-alpha-dev-1
 */
data class Mybatis(
    val url: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String,
    val parameter: String,
    val filters: String,
    val maxActive: Int,
    val initialSize: Int,
    val maxWait: Long,
    val minIdle: Int,
    val timeBetweenEvictionRunsMillis: Long,
    val minEvictableIdleTimeMillis: Long,
    val validationQuery: String,
    val testWhileIdle: Boolean,
    val testOnBorrow: Boolean,
    val testOnReturn: Boolean,
    val poolPreparedStatements: Boolean,
    val maxOpenPreparedStatements: Int,
    val removeAbandoned: Boolean,
    val removeAbandonedTimeout: Int,
    val logAbandoned: Boolean,
    val asyncInit: Boolean
) {

    companion object {
        private val mybatisConfiguration = Configuration()

        init {
            mybatisConfiguration.addMapper(BaseSQLMapper::class.java)
        }
    }

    fun getDatabaseName(): String {
        return database
    }

    lateinit var sqlSessionFactory: SqlSessionFactory
        private set

    init {

        val druidDataSource = DruidDataSource()
        druidDataSource.url = "jdbc:mysql://$url:$port/$database?$parameter"

        druidDataSource.username = user
        druidDataSource.password = password

        druidDataSource.setFilters(filters)
        druidDataSource.maxActive = maxActive
        druidDataSource.initialSize = initialSize
        druidDataSource.maxWait = maxWait
        druidDataSource.minIdle = minIdle
        druidDataSource.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis
        druidDataSource.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis
        druidDataSource.validationQuery = validationQuery
        druidDataSource.isTestWhileIdle = testWhileIdle
        druidDataSource.isTestOnBorrow = testOnBorrow
        druidDataSource.isTestOnReturn = testOnReturn
        druidDataSource.isPoolPreparedStatements = poolPreparedStatements
        druidDataSource.maxOpenPreparedStatements = maxOpenPreparedStatements
        druidDataSource.isRemoveAbandoned = removeAbandoned
        druidDataSource.removeAbandonedTimeout = removeAbandonedTimeout
        druidDataSource.isLogAbandoned = logAbandoned
        druidDataSource.isAsyncInit = asyncInit
        val environment = Environment(
            "development", JdbcTransactionFactory(), druidDataSource
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
}