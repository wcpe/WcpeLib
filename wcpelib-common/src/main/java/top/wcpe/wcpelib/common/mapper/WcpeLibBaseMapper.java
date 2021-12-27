package top.wcpe.wcpelib.common.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface WcpeLibBaseMapper {
    @Select("select count(*) from information_schema.TABLES t where t.TABLE_SCHEMA =#{databaseName} and t.TABLE_NAME = #{tableName}")
    int existTable(@Param("databaseName") String databaseName, @Param("tableName") String tableName);

}
