package top.wcpe.wcpelib.nukkit.placeholder;

import cn.nukkit.Player;
import lombok.Getter;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 占位符管理器
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-20 22:02
 */
public class PlaceholderManager {
    @Getter
    private static final HashMap<String, PlaceholderExtend> placeholderMap = new HashMap<>();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");

    public static String replaceString(Player player, String text) {
        Matcher m = PLACEHOLDER_PATTERN.matcher(text);

        while (m.find()) {
            String format = m.group(1);
            int index = format.indexOf("_");
            if (index > 0) {
                String identifier = format.substring(0, index).toLowerCase();
                String params = format.substring(index + 1);
                if (placeholderMap.containsKey(identifier)) {
                    String value = placeholderMap.get(identifier).onPlaceholderRequest(player, params);
                    if (value != null) {
                        text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
                    }
                }
            }
        }
        return text;
    }

}
