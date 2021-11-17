package top.wcpe.wcpelib.nukkit.mybatis.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import org.apache.ibatis.session.SqlSession;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.mybatis.entity.PlayerServer;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;

import java.util.List;

public class PlayerServerUtil {
    public static PlayerServer getPlayerServer(String playerName, boolean defaultCreate) {
        if (!WcpeLib.isEnableMysql()) {
            Player playerExact = Server.getInstance().getPlayerExact(playerName);
            if (playerExact == null || !playerExact.isOnline()) {
                return new PlayerServer(playerName, WcpeLib.getServerName(), false);
            }
            return new PlayerServer(playerName, WcpeLib.getServerName(), true);
        }
        SqlSession sqlSession = WcpeLib.getMybatis().getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            if (!defaultCreate) {
                return mapper.selectPlayerServer(playerName);
            }
            PlayerServer playerServer = mapper.selectPlayerServer(playerName);
            if (playerServer != null) {
                return playerServer;
            }
            playerServer = new PlayerServer(playerName, WcpeLib.getServerName(), false);
            mapper.insertPlayerServer(playerServer);
            return playerServer;
        } finally {
            sqlSession.close();
        }
    }

    public static List<PlayerServer> getPlayerServerToServerName(String serverName) {
        SqlSession sqlSession = WcpeLib.getMybatis().getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            return mapper.selectOnlinePlayerToServer(serverName);
        } finally {
            sqlSession.close();
        }
    }


}
