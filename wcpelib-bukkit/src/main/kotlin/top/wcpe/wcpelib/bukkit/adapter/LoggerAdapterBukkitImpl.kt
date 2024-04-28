package top.wcpe.wcpelib.bukkit.adapter

import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import java.util.logging.Logger

/**
 * 由 WCPE 在 2022/1/3 22:56 创建
 *
 * Created by WCPE on 2022/1/3 22:56
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class LoggerAdapterBukkitImpl(private val logger: Logger = WcpeLib.instance.logger) : LoggerAdapter {
    override fun info(msg: String) {
        logger.info(msg)
    }
}