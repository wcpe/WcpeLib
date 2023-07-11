package top.wcpe.wcpelib.common

import top.wcpe.wcpelib.common.adapter.ConfigAdapter
import top.wcpe.wcpelib.common.adapter.LoggerAdapter

/**
 * 由 WCPE 在 2023/7/10 13:40 创建
 * <p>
 * Created by WCPE on 2023/7/10 13:40
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
interface PlatformAdapter {
    fun createLoggerAdapter(): LoggerAdapter
    fun createMySQLConfigAdapter(): ConfigAdapter
    fun createRedisConfigAdapter(): ConfigAdapter
    fun createKtorConfigAdapter(): ConfigAdapter
}