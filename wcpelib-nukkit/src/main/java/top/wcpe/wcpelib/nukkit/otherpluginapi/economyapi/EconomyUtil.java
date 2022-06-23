package top.wcpe.wcpelib.nukkit.otherpluginapi.economyapi;

import cn.nukkit.Server;
import me.onebone.economyapi.EconomyAPI;

/**
 * EconomyAPI 插件
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-06-24 20:17
 */
public class EconomyUtil {
    private static EconomyAPI economy = null;


    public static EconomyAPI getEconomy() {
        if (economy == null) {
            if (Server.getInstance().getPluginManager().getPlugin("EconomyAPI") != null) {
                economy = EconomyAPI.getInstance();
            }
        }
        return economy;
    }

}
