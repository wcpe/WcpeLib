package top.wcpe.wcpelib.common.utils.string;


/**
 * 字符串工具类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-04-24 16:37
 */
public class StringUtil {
    /**
     * 保留中文 数字 字母 下划线
     *
     * @param string
     * @return {@link String}
     */
    public static String replace(String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll("[^\\u4e00-\\u9fbba-zA-Z0-9_]", "");
    }

    /**
     * 获取重复的字符串
     *
     * @param string 重复的字符串
     * @param length 重复多少次
     * @return {@link String}
     */
    public static String getRepeatString(String string, int length) {
        if (string == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (; length > 0; length--) {
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * 合并字符串 添加分隔符
     *
     * @param splitString
     * @param strings
     * @return String
     */
    public static String joining(String splitString, String... strings) {
        return String.join(splitString, strings);
    }

    /**
     * 替换字符串中的变量
     *
     * @param sour
     * @param replaces
     * @return String
     */
    public static String replaceString(String sour, String... replaces) {
        if (sour == null) return null;
        for (String s : replaces) {
            int i = s.indexOf(":");
            if (i != -1) {
                sour = sour.replace("%" + s.substring(0, i) + "%", s.substring(i + 1));
            }
        }
        return sour;
    }

    /**
     * 替换 Windows 文件名不能使用的特殊字符
     * . \ / : * ? " < > |
     *
     * @param src
     * @return
     */
    public static String replaceWindowsSpecialCharacters(String src) {
        if (src == null) return null;
        return src.replaceAll("[\\.\\\\\\/:\\*\\?\"\\<\\>\\|]", "#");
    }
}
