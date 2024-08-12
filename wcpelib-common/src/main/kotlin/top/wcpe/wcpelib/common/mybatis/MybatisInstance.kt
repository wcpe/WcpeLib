package top.wcpe.wcpelib.common.mybatis

import com.alibaba.druid.pool.DruidDataSource
import org.apache.ibatis.binding.MapperRegistry
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.mapper.BaseSQLMapper
import java.util.function.Consumer

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
 * @since  : v
 */
data class MybatisInstance(
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
    val asyncInit: Boolean,
) {

    companion object {

        @JvmStatic
        fun load(configAdapter: ConfigAdapter): MybatisInstance {
            val url = configAdapter.getString("mysql.url")
            val port = configAdapter.getInt("mysql.port")
            val database = configAdapter.getString("mysql.database")
            val user = configAdapter.getString("mysql.user")
            val password = configAdapter.getString("mysql.password")
            val parameter = configAdapter.getString("mysql.parameter")
            val filters = configAdapter.getString("mysql.filters")
            val maxActive = configAdapter.getInt("mysql.maxActive")
            val initialSize = configAdapter.getInt("mysql.initialSize")
            val maxWait = configAdapter.getLong("mysql.maxWait")
            val minIdle = configAdapter.getInt("mysql.minIdle")
            val timeBetweenEvictionRunsMillis = configAdapter.getLong("mysql.timeBetweenEvictionRunsMillis")
            val minEvictableIdleTimeMillis = configAdapter.getLong("mysql.minEvictableIdleTimeMillis")
            val validationQuery = configAdapter.getString("mysql.validationQuery")
            val testWhileIdle = configAdapter.getBoolean("mysql.testWhileIdle")
            val testOnBorrow = configAdapter.getBoolean("mysql.testOnBorrow")
            val testOnReturn = configAdapter.getBoolean("mysql.testOnReturn")
            val poolPreparedStatements = configAdapter.getBoolean("mysql.poolPreparedStatements")
            val maxOpenPreparedStatements = configAdapter.getInt("mysql.maxOpenPreparedStatements")
            val removeAbandoned = configAdapter.getBoolean("mysql.removeAbandoned")
            val removeAbandonedTimeout = configAdapter.getInt("mysql.removeAbandonedTimeout")
            val logAbandoned = configAdapter.getBoolean("mysql.logAbandoned")
            val asyncInit = configAdapter.getBoolean("mysql.asyncInit")

            return MybatisInstance(
                url = url,
                port = port,
                database = database,
                user = user,
                password = password,
                parameter = parameter,
                filters = filters,
                maxActive = maxActive,
                initialSize = initialSize,
                maxWait = maxWait,
                minIdle = minIdle,
                timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis,
                minEvictableIdleTimeMillis = minEvictableIdleTimeMillis,
                validationQuery = validationQuery,
                testWhileIdle = testWhileIdle,
                testOnBorrow = testOnBorrow,
                testOnReturn = testOnReturn,
                poolPreparedStatements = poolPreparedStatements,
                maxOpenPreparedStatements = maxOpenPreparedStatements,
                removeAbandoned = removeAbandoned,
                removeAbandonedTimeout = removeAbandonedTimeout,
                logAbandoned = logAbandoned,
                asyncInit = asyncInit
            )
        }
    }

    private val mybatisConfiguration = Configuration()

    init {
        mybatisConfiguration.addMapper(BaseSQLMapper::class.java)
    }

    private val mapperRegistryClass = MapperRegistry::class.java
    private val knownMappersField = mapperRegistryClass.getDeclaredField("knownMappers")

    fun getDatabaseName(): String {
        return database
    }

    val sqlSessionFactory: SqlSessionFactory

    init {
        knownMappersField.isAccessible = true
    }

    init {

        val druidDataSource = DruidDataSource()
        druidDataSource.url = "jdbc:mysql://${url}:${port}/${database}?${parameter}"

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

    fun removeMapper(vararg classes: Class<*>?) {
        val current = sqlSessionFactory
        val getObj = knownMappersField.get(current.configuration.mapperRegistry) as? HashMap<*, *> ?: return
        for (clazz in classes) {
            if (clazz == null) continue
            getObj.remove(clazz)
        }
    }


}