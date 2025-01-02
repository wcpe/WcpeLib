package top.wcpe.wcpelib.nukkit.placeholder.data;

import cn.nukkit.Player;
import lombok.val;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.placeholder.PlaceholderExtend;

/**
 * 服务器占位符
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-22 22:26
 */
public class ServerPlaceholder extends PlaceholderExtend {

    @Override
    public String getAuthor() {
        return "WCPE";
    }

    @Override
    public String getIdentifier() {
        return "server";
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        switch (identifier) {
            case "name":
                return WcpeLib.getServerName();
            case "view_name":
                val serverInfo = WcpeLib.getServerInfo(WcpeLib.getServerName());
                if (serverInfo == null) {
                    return null;
                }
                return serverInfo.getViewName();
        }
        return null;
    }
}
