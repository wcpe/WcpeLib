package top.wcpe.wcpelib.nukkit.extend

import cn.nukkit.item.Item
import top.wcpe.wcpelib.nukkit.WcpeLib

/**
 * 由 WCPE 在 2021/12/31 23:01 创建
 *
 * Created by WCPE on 2021/12/31 23:01
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
fun Item.getWcpeLibCustomName(): String {
    return if (hasCustomName()) name
    else
        WcpeLib.getItemConfig().getSection("item-custom-name").let {
            return if (it.exists("${this.id}:${this.damage}"))
                it.getString("${this.id}:${this.damage}")
            else
                name
        }
}