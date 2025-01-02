package top.wcpe.wcpelib.bungeecord.adapter

import top.wcpe.wcpelib.bungeecord.WcpeLib
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import java.util.logging.Logger

/**
 * 由 WCPE 在 2022/9/28 21:38 创建
 *
 * Created by WCPE on 2022/9/28 21:38
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class LoggerAdapterBungeeCordImpl(private val logger: Logger = WcpeLib.instance.logger) : LoggerAdapter {
    override fun info(msg: String) {
        logger.info(msg)
    }
}