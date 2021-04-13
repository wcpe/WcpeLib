package top.wcpe.wcpelib.model.mybatis.utils;

import org.apache.ibatis.session.SqlSession;
import top.wcpe.wcpelib.WcpeLib;
import top.wcpe.wcpelib.model.mybatis.mapper.PlayerServerMapper;

public class PlayerServerUtil {
    public static String getPlayerServer(String name) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            return mapper.selectPlayerServer(name);
        } finally {
            sqlSession.close();
        }
    }

    public static boolean getPlayerIsOnline(String name) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession();
        try {
            PlayerServerMapper mapper = sqlSession.getMapper(PlayerServerMapper.class);
            return mapper.selectPlayerServer(name) == null ? false : true;
        } finally {
            sqlSession.close();
        }
    }
}
