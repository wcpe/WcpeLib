package top.wcpe.wcpelib.nukkit.utils;


import cn.nukkit.item.Item;
import cn.nukkit.utils.ConfigSection;
import top.wcpe.wcpelib.nukkit.WcpeLib;

import java.util.List;


public class ItemUtil {


    /**
     * 获取 ItemLore 中 字符串的索引位置
     *
     * @param itemLore
     * @param str
     * @return
     */
    public int getStringInItemLoreIndex(List<String> itemLore, String str) {
        if (itemLore == null) {
            return -1;
        }
        for (int i = 0; i < itemLore.size(); i++) {
            if (itemLore.get(i).contains(str)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * itemLore 中是否包含 configLore
     *
     * @param configLore
     * @param itemLore
     * @return boolean
     */
    public static boolean itemLoreContainLore(List<String> configLore, List<String> itemLore) {
        if (configLore == null || itemLore == null) return false;
        if (configLore.size() > itemLore.size()) return false;
        for (int i = 0; i < configLore.size(); i++) {
            if (!itemLore.contains(configLore.get(i))) {
                return false;
            }
        }
        return true;

    }

    /**
     * 对比两个lore是否相等
     *
     * @param lores
     * @param lores2
     * @return {@link boolean}
     * @author WCPE
     * @date 2021年4月20日 下午6:03:43
     */
    public static boolean equalsLore(List<String> lores, List<String> lores2) {
        if (lores == null && lores2 == null) {
            return true;
        }
        if ((lores == null && lores2 != null) || (lores2 == null && lores != null)) {
            return false;
        }
        if (lores.size() != lores2.size())
            return false;
        for (int i = 0; i < lores.size(); i++) {
            if (!lores.get(i).equals(lores2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String getItemCustomName(Item item) {
        ConfigSection itemCustomNameCs = WcpeLib.getItemConfig().getSection("item-custom-name");
        if (!itemCustomNameCs.exists(String.valueOf(item.getId()))) {
            return item.getName();
        }
        return itemCustomNameCs.getString(String.valueOf(item.getId()));
    }
}
