package top.wcpe.wcpelib.common.mysql

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import javax.sql.DataSource

/**
 * 由 WCPE 在 2024/11/13 16:51 创建
 * <p>
 * Created by WCPE on 2024/11/13 16:51
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
object MySQL {

    fun init(mysqlDataSourceInstance: MySQLDataSourceInstance): MySQL {
        this.mysqlDataSourceInstance = mysqlDataSourceInstance
        TransactionManager.defaultDatabase = Database.connect(mysqlDataSourceInstance.dataSource)
        return this
    }

    @JvmStatic
    lateinit var mysqlDataSourceInstance: MySQLDataSourceInstance


    /**
     * 获取数据源
     */
    fun getDataSource(): DataSource {
        return mysqlDataSourceInstance.dataSource
    }
}