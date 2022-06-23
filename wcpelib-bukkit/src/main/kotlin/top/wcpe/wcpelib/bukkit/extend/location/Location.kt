package top.wcpe.wcpelib.bukkit.extend.location

import org.bukkit.Location
import top.wcpe.wcpelib.bukkit.utils.SerializeClassUtil

/**
 * 由 WCPE 在 2022/6/2 15:35 创建
 *
 * Created by WCPE on 2022/6/2 15:35
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-2
 */

fun Location.locationToString(): String {
    return SerializeClassUtil.locationToString(this)
}

fun String.stringToLocation(): Location {
    return SerializeClassUtil.stringToLocation(this)
}