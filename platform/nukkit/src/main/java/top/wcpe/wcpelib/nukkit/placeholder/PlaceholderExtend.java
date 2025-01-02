package top.wcpe.wcpelib.nukkit.placeholder;

import cn.nukkit.Player;

/**
 * 占位符扩展
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-20 22:04
 */
public abstract class PlaceholderExtend {
    public final void register() {
        PlaceholderManager.getPlaceholderMap().put(getIdentifier().toLowerCase(), this);
    }

    public abstract String getAuthor();

    public abstract String getIdentifier();

    public abstract String onPlaceholderRequest(Player p, String identifier);
}
