package top.wcpe.wcpelib.nukkit.utils;




import java.util.List;


public class ItemUtil {


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
        for (int i = 0; i < itemLore.size(); i++) {
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
}
