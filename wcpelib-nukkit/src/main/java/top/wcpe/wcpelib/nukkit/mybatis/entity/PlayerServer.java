package top.wcpe.wcpelib.nukkit.mybatis.entity;

import lombok.Data;

/**
 * 玩家服务器对象
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-11-11 21:17
 */
@Data
public class PlayerServer {
    private final String playerName;
    private final String serverName;
    private final boolean online;

}
