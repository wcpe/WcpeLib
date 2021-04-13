package top.wcpe.wcpelib.model.bukkit.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {
    /**
     * @param type 物品的种类
     * @param name 物品的名称
     * @param lore 物品的Lore
     * @return ItemStack
     */
    public static ItemStack getItem(String type, String name, List<String> lore) {
        return getItem(type, name, lore, 1);
    }

    /**
     * @param type   物品的种类
     * @param name   物品的名称
     * @param lore   物品的Lore
     * @param amount 物品的数量
     * @return ItemStack
     */
    public static ItemStack getItem(String type, String name, List<String> lore, int amount) {
        ItemStack item = new ItemStack(Material.valueOf(type.toUpperCase()));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        item.setItemMeta(im);
        item.setAmount(amount);
        return item;
    }

    /**
     * @param type   物品的数字id
     * @param damage 特殊值
     * @param name   物品的名称
     * @param lore   物品的Lore
     * @param amount 物品的数量
     * @return ItemStack
     */
    public static ItemStack getItem(String type, String damage, String name, List<String> lore, int amount) {
        @SuppressWarnings("deprecation")
        ItemStack item = new ItemStack(Integer.valueOf(type), amount, Short.valueOf(damage));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        item.setItemMeta(im);
        item.setAmount(amount);
        return item;
    }


    public static int loreContainsNumCount(List<String> lore, String con) {
        int i = 0;
        for (String s : lore) {
            if (s.equals(con)) {
                i++;
            }
        }
        return i;
    }

    public static boolean itemLoreContainLore(List<String> configLore, List<String> itemLore) {
        if (configLore.size() <= itemLore.size()) {
            for (String s : configLore) {
                int configLoreNum = loreContainsNumCount(configLore, s);
                if (configLoreNum > 1) {
                    if (loreContainsNumCount(itemLore, s) == configLoreNum) {
                        continue;
                    }
                    return false;
                }
                if (!itemLore.contains(s)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
