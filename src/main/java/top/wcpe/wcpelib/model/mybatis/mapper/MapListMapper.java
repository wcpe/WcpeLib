package top.wcpe.wcpelib.model.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MapListMapper {
    int existTable();

    void createTable();

    String get(@Param("key") String key, @Param("indexKey")String indexKey);

    List<String> list(@Param("key")String key, @Param("indexKey")String indexKey);

    void insert(@Param("key")String key, @Param("value")String value, @Param("indexKey")String indexKey);

    void update(@Param("key")String key, @Param("value")String value, @Param("indexKey")String indexKey);

    void delete(@Param("key")String key, @Param("indexKey")String indexKey);
}
