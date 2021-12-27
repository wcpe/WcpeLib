package top.wcpe.wcpelib.nukkit.inventory.entity

import cn.nukkit.item.Item
import java.util.*

data class SaveItem(var id: Int = 0, var meta: Int = 0, var count: Int = 0, var compoundTag: String?) {

    constructor(item: Item) : this(
        item.id,
        item.damage,
        item.getCount(),
        null
    ) {
        this.compoundTag = if (item.hasCompoundTag()) bytesToBase64(item.compoundTag) else null
    }

    private fun bytesToBase64(src: ByteArray?): String? {
        return if (src == null || src.isEmpty()) null else Base64.getEncoder().encodeToString(src)
    }

    private fun base64ToBytes(hexString: String?): ByteArray? {
        return if (hexString == null || hexString == "") null else Base64.getDecoder().decode(hexString)
    }

    fun toItem(): Item {
        val item = Item.get(id, meta, count)
        if (compoundTag != null) {
            item.compoundTag = base64ToBytes(compoundTag)
        }
        return item
    }
}