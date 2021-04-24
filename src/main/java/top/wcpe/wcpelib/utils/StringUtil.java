package top.wcpe.wcpelib.utils;

/**
 * 字符串工具类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-04-24 16:37
 */
public class StringUtil {
    /**
     * 获取重复的字符串
     * @param string 重复的字符串
     * @param length 重复多少次
     * @return {@link String}
     */
    public static String getRepeatString(String string ,int length) {
        StringBuilder sb = new StringBuilder();
        for (; length > 0; length--) {
            sb.append(string);
        }
        return sb.toString();
    }

}
