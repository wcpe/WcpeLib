package top.wcpe.wcpelib.nukkit.mybatis.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import org.apache.ibatis.session.SqlSession;
import top.wcpe.wcpelib.nukkit.WcpeLib;
import top.wcpe.wcpelib.nukkit.mybatis.mapper.PlayerServerMapper;

public class PlayerServerUtil {
    public static String getPlayerServer(String name) {
        if (!WcpeLib.isEnableMysql()) return WcpeLib.getServerName();
        SqlSession sqlSession = WcpeLib.getMybatis().getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            return mapper.selectPlayerServer(name);
        } finally {
            sqlSession.close();
        }
    }

    public static boolean getPlayerIsOnline(String name) {
        if (!WcpeLib.isEnableMysql()) {
            Player playerExact = Server.getInstance().getPlayerExact(name);
            if (playerExact == null) return false;
            return playerExact.isOnline();
        }
        SqlSession sqlSession = WcpeLib.getMybatis().getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            return mapper.selectPlayerServer(name) == null ? false : true;
        } finally {
            sqlSession.close();
        }
    }
}
