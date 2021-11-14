package top.wcpe.wcpelib.nukkit.mybatis.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import org.apache.ibatis.session.SqlSession;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.mybatis.entity.PlayerServer;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;

public class PlayerServerUtil {
    public static String getPlayerServer(String playerName) {
        return getPlayerServerEntity(playerName).getPlayerName();
    }

    public static PlayerServer getPlayerServerEntity(String playerName) {
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
            return mapper.selectPlayerServer(playerName);
        } finally {
            sqlSession.close();
        }
    }


}
