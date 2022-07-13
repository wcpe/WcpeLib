package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Deprecated
    public static void clearItem(Player p, int id, int data, String name, List<String> lore, int number) {
        final HashMap<Integer, Integer> idAmound = new HashMap<>();
        for (int i = 0; i < p.getInventory().getMaxStackSize(); i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null) {
                ItemMeta im = item.getItemMeta();
                if (item.getTypeId() == id && item.getData().getData() == data && name.equals(im.getDisplayName())
                        && equalsLore(lore, im.getLore())) {
                    idAmound.put(i, item.getAmount());
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : idAmound.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if (number == value) {
                p.getInventory().setItem(key, null);
                return;
            } else if (number > value) {
                number -= value;
                p.getInventory().setItem(key, null);
            } else if (number < value) {
                p.getInventory().getItem(key).setAmount(value - number);
                return;
            }
        }

    }

    /**
     * 获取 ItemLore 中 字符串的索引位置
     *
     * @param itemLore
     * @param str
     * @return
     */
    public static int getStringInItemLoreIndex(List<String> itemLore, String str) {
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

    /**
     * ItemStack 比对物品
     *
     * @param item1
     * @param item2
     * @return
     */
    public static boolean equalsItem(ItemStack item1, ItemStack item2) {
        if (item1 == null && item2 == null) return true;
        if (item1 == null && item2 != null || item2 == null && item1 != null) return false;
        if (item1.getType() != item2.getType()) return false;
        if (item1.getDurability() != item2.getDurability()) return false;
        if (item1.getAmount() != item2.getAmount()) return false;
        if (!item1.getData().equals(item2.getData())) return false;
        if (!item1.hasItemMeta() && !item2.hasItemMeta()) return true;
        if (item1.hasItemMeta() && !item2.hasItemMeta() || item2.hasItemMeta() && !item1.hasItemMeta()) return false;
        ItemMeta item1Meta = item1.getItemMeta();
        ItemMeta item2Meta = item2.getItemMeta();
        String item1DisplayName = item1Meta.getDisplayName();
        String item2DisplayName = item2Meta.getDisplayName();
        if (item1DisplayName != null && item2DisplayName == null || item2DisplayName != null && item1DisplayName == null)
            return false;
        List<String> item1Lore = item1Meta.getLore();
        List<String> item2Lore = item2Meta.getLore();
        if (equalsLore(item1Lore, item2Lore)) {
            if (item1DisplayName == null && item2DisplayName == null) {
                return true;
            }
            return item1DisplayName.equals(item2DisplayName);
        }
        return false;
    }
}
