package top.wcpe.wcpelib.nukkit.npc.function;

import cn.nukkit.event.player.PlayerInteractEntityEvent;

/**
 * 点击Npc触发函数
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-09-01 09:37
 */
@FunctionalInterface
public interface ClickNpcFunctional {
    void run(PlayerInteractEntityEvent e);
}
