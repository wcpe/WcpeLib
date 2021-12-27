package top.wcpe.wcpelib.nukkit.server;

import lombok.Data;

/**
 * 服务器信息
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-10-22 19:52
 */
@Data
public class ServerInfo {
    private String serverName;
    private String host;
    private int port;

    public ServerInfo(String serverName, String host, int port) {
        this.serverName = serverName;
        this.host = host;
        this.port = port;
    }
}
