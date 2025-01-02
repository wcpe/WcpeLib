package top.wcpe.wcpelib.common.mysql

import com.alibaba.druid.pool.DruidDataSource
import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import javax.sql.DataSource

/**
 * 由 WCPE 在 2024/11/9 15:29 创建
 * <p>
 * Created by WCPE on 2024/11/9 15:29
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
open class MySQLDataSourceConfig(
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
        fun load(configAdapter: ConfigAdapter): MySQLDataSourceConfig {
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

            return MySQLDataSourceConfig(
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


    /**
     * 构建数据源
     */
    fun build(): DataSource {
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

        return druidDataSource
    }
}