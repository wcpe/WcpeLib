package top.wcpe.wcpelib.nukkit.inventory

import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import top.wcpe.wcpelib.nukkit.inventory.action.ClickListener

class SlotPlus {

    fun getItem(): Item {
        var item = Item(this.id, this.data, this.amount)
        item.customName = this.name
        item.setLore(* this.lores.toTypedArray())
        if (unbreakable) {
            item.namedTag.putByte("Unbreakable", 1)
        }
        for (enchantment in this.enchantments) {
            item.addEnchantment(enchantment)
        }
        return item
    }

    fun setItem(item: Item) {
        item?.let {
            id = item.id
            data = item.damage
            name = item.customName
            amount = item.count
            lores = item.lore.toMutableList()
            enchantments = item.enchantments.toMutableList()
            unbreakable = item.isUnbreakable
        }
    }


    private var id: Int = 0
        get
        set

    private var data = 0
        get
        set


    private var name: String = " "
        get
        set

    private var lores: MutableList<String> = mutableListOf()
        get


    private var amount = 1
        get
        set

    private var enchantments: MutableList<Enchantment> = mutableListOf()


    private var unbreakable = false


    var listener: ClickListener? = null


    constructor(builder: SlotPlusBuilder) {
        id = builder.id
        data = builder.data
        name = builder.name
        lores = builder.lores
        amount = builder.amount
        enchantments = builder.enchantments
        unbreakable = builder.unbreakable
        listener = builder.listener
    }

}