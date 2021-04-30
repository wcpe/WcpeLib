package top.wcpe.wcpelib.common.utils.string;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-04-24 16:37
 */
public class StringUtil {
    /**
     * 获取重复的字符串
     *
     * @param string 重复的字符串
     * @param length 重复多少次
     * @return {@link String}
     */
    public static String getRepeatString(String string, int length) {
        StringBuilder sb = new StringBuilder();
        for (; length > 0; length--) {
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * 合并字符串 添加分隔符
     *
     * @return
     */
    public static String joining(String splitString, String... strings) {
        return Arrays.asList(strings).stream().collect(Collectors.joining(splitString));
    }

    /**
     * 替换字符串中的变量
     *
     * @param sour
     * @param replaces
     * @return
     */
    public static String replaceString(String sour, String... replaces) {
        for (String s : replaces) {
            String[] sSplit = s.split(":");
            sour = sour.replaceFirst("\\%" + sSplit[0] + "\\%", sSplit[1]);
        }
        return sour;
    }
}
