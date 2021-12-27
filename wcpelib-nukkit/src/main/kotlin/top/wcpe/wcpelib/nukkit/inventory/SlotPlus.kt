package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.item.Item
import top.wcpe.wcpelib.nukkit.inventory.action.ClickListener
import top.wcpe.wcpelib.nukkit.inventory.entity.SaveItem

class SlotPlus(builder: SlotPlusBuilder) {

    var item: SaveItem = SaveItem(builder.createItem())
    fun getItem(): Item {
        return item.toItem()
    }

    fun setItem(item: Item) {
        this.item = SaveItem(item)
    }

    var listener: ClickListener? = null


}