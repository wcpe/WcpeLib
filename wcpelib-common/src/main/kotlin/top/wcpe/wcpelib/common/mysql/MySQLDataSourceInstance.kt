package top.wcpe.wcpelib.common.mysql

import javax.sql.DataSource

/**
 * 由 WCPE 在 2024/11/13 16:07 创建
 * <p>
 * Created by WCPE on 2024/11/13 16:07
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
class MySQLDataSourceInstance(
    val mysqlDataSourceConfig: MySQLDataSourceConfig,
    val dataSource: DataSource,
)