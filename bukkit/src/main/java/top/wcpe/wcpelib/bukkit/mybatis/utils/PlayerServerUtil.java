package top.wcpe.wcpelib.bukkit.mybatis.utils;

import org.apache.ibatis.session.SqlSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.wcpe.wcpelib.bukkit.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.bukkit.WcpeLib;

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
            Player playerExact = Bukkit.getPlayerExact(name);
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
