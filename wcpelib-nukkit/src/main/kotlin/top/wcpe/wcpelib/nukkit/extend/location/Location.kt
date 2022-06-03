package top.wcpe.wcpelib.nukkit.extend.location

import cn.nukkit.level.Location
import top.wcpe.wcpelib.nukkit.utils.SerializeClassUtil

/**
 * 由 WCPE 在 2022/6/2 15:38 创建
 *
 * Created by WCPE on 2022/6/2 15:38
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-2
 */

fun Location.locationToString(): String {
    return SerializeClassUtil.locationToString(this)
}

fun String.locationToString(): Location {
    return SerializeClassUtil.stringToLocation(this)
}