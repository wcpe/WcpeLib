package top.wcpe.wcpelib.bukkit.adapter

import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.common.adapter.LoggerAdapter

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
class LoggerAdapterBukkitImpl : LoggerAdapter {
    override fun info(msg: String) {
        WcpeLib.getInstance().logger.info(msg)
    }
}