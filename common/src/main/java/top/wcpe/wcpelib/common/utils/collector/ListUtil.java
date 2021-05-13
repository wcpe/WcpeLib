package top.wcpe.wcpelib.common.utils.collector;


import java.util.ArrayList;
import java.util.List;

/**
 * 集合工具类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-04-24 11:37
 */
public class ListUtil {
    /**
     * 按长度分割集合类
     *
     * @param resList       源集合
     * @param subListLength 一个集合多少个参数
     * @param <T>           集合存储元素类型
     * @return {@link List}
     */
    public static <T> List<List<T>> splitList(List<T> resList, int subListLength) {
        if (resList == null || subListLength <= 0) {
            return new ArrayList<>();
        }
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= subListLength) {
            ret.add(resList);
        } else {
            int pre = size / subListLength;
            int last = size % subListLength;
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < subListLength; j++) {
                    itemList.add(resList.get(i * subListLength + j));
                }
                ret.add(itemList);
            }
            if (last > 0) {
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * subListLength + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    /**
     * 替换字符串中的变量
     *
     * @param sours
     * @param replaces
     * @return List<String>
     */
    public static List<String> replaceString(List<String> sours, String... replaces) {
        for (int i = 0; i < sours.size(); i++) {
            for (String s : replaces) {
                int index = s.indexOf(":");
                if (index != -1) {
                    sours.set(i, sours.get(i).replaceAll("\\%" + s.substring(0, index) + "\\%", s.substring(index + 1)));
                }
            }
        }
        return sours;
    }
}
