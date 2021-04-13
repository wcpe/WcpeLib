package top.wcpe.wcpelib.model.mybatis.utils;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import top.wcpe.wcpelib.WcpeLib;
import top.wcpe.wcpelib.model.mybatis.mapper.MapListMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MapListUtil {
    public static String getString(String indexKey, String key) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession();
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            return mapper.get(key, indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static void addString(String indexKey, String key, String value) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            mapper.insert(key, value, indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static void putString(String indexKey, String key, String value) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            String s = mapper.get(key, indexKey);
            if (s == null) {
                mapper.insert(key, value, indexKey);
                return;
            }
            mapper.update(key, value, indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static List<String> getList(String indexKey, String key) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession();
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            String s = mapper.get(key, indexKey);
            if (s == null) return null;
            JSONArray jsonArray = new JSONArray(s);
            return jsonArray.toList().stream().map(o -> (String) o).collect(Collectors.toList());
        } finally {
            sqlSession.close();
        }
    }

    public static void getListAddValue(String indexKey, String key, String... value) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            String s = mapper.get(key, indexKey);
            if (s == null) {
                JSONArray jsonArray = new JSONArray();
                for (String v : value) {
                    jsonArray.put(v);
                }
                System.out.println(jsonArray.toString());
                mapper.insert(key, jsonArray.toString(), indexKey);
                return;
            }
            JSONArray jsonArray = new JSONArray(s);
            for (String v : value) {
                jsonArray.put(v);
            }
            System.out.println(jsonArray.toString());
            mapper.update(key, jsonArray.toString(), indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static void addList(String indexKey, String key, List<String> value) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            String s = mapper.get(key, indexKey);
            JSONArray jsonArray = new JSONArray(value);
            mapper.insert(key, jsonArray.toString(), indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static void putList(String indexKey, String key, List<String> value) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            String s = mapper.get(key, indexKey);
            JSONArray jsonArray = new JSONArray(value);
            if (s == null) {
                mapper.insert(key, jsonArray.toString(), indexKey);
                return;
            }
            mapper.update(key, jsonArray.toString(), indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static void remove(String indexKey, String key) {
        SqlSession sqlSession = WcpeLib.getSqlSessionFactory().openSession(true);
        try {
            MapListMapper mapper = sqlSession.getMapper(MapListMapper.class);
            mapper.delete(key, indexKey);
        } finally {
            sqlSession.close();
        }
    }

    public static boolean removeListElement(String indexKey, String key, String element) {
        List<String> list = getList(indexKey, key);
        if (list == null) return false;
        boolean remove = list.remove(element);
        if (remove)
            putList(indexKey, key, list);
        return remove;

    }
}
