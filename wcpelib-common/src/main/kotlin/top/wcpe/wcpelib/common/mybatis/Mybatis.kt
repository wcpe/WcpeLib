package top.wcpe.wcpelib.common.mybatis

import com.alibaba.druid.pool.DruidDataSource
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import top.wcpe.wcpelib.common.mapper.WcpeLibBaseMapper
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
class Mybatis(
    url: String,
    port: Int,
    database: String,
    user: String,
    password: String,
    parameter: String,
    filters: String,
    maxActive: Int,
    initialSize: Int,
    maxWait: Long,
    minIdle: Int,
    timeBetweenEvictionRunsMillis: Long,
    minEvictableIdleTimeMillis: Long,
    validationQuery: String,
    testWhileIdle: Boolean,
    testOnBorrow: Boolean,
    testOnReturn: Boolean,
    poolPreparedStatements: Boolean,
    maxOpenPreparedStatements: Int,
    removeAbandoned: Boolean,
    removeAbandonedTimeout: Int,
    logAbandoned: Boolean,
    asyncInit: Boolean
) {
    var databaseName: String
        get
    var sqlSessionFactory: SqlSessionFactory
        get

    init {
        databaseName = database

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

        sqlSessionFactory = SqlSessionFactoryBuilder().build(
            Configuration(
                Environment(
                    "development",
                    JdbcTransactionFactory(),
                    druidDataSource
                )
            )
        )
        addMapper(WcpeLibBaseMapper::class.java)
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
            sqlSessionFactory.configuration.addMapper(clazz)
        }
    }
}