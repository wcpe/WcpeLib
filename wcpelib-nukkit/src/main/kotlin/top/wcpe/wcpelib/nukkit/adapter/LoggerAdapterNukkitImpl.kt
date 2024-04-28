package top.wcpe.wcpelib.nukkit.adapter

import cn.nukkit.plugin.PluginLogger
import top.wcpe.wcpelib.common.adapter.LoggerAdapter
import top.wcpe.wcpelib.nukkit.WcpeLib

/**
 * 由 WCPE 在 2022/1/3 23:13 创建
 *
 * Created by WCPE on 2022/1/3 23:13
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
class LoggerAdapterNukkitImpl(private val logger: PluginLogger = WcpeLib.getInstance().logger) : LoggerAdapter {
    override fun info(msg: String) {
        logger.info(msg)
    }
}