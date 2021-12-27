package top.wcpe.wcpelib.nukkit.placeholder.data;

import cn.nukkit.Player;
import top.wcpe.wcpelib.nukkit.placeholder.PlaceholderExtend;

/**
 * 玩家占位符
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-22 22:32
 */
public class PlayerPlaceholder extends PlaceholderExtend {

    @Override
    public String getAuthor() {
        return "WCPE";
    }

    @Override
    public String getIdentifier() {
        return "player";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if ("name".equals(identifier)) {
            return p.getName();
        }
        if ("worldName".equals(identifier)) {
            return p.getLevel().getName();
        }
        return null;
    }
}
