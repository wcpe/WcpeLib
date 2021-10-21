package top.wcpe.wcpelib.nukkit.npc;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.chat.ChatAcceptParameterManager;

/**
 * Npc管理器
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-02 14:53
 */
public class NpcManager {
    static {
        Server.getInstance().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void playerChat(PlayerChatEvent e) {

            }
        }, WcpeLib.getInstance());
    }

}
