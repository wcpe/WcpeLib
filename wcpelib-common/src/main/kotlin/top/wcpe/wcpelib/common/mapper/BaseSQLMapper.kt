package top.wcpe.wcpelib.common.mapper

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

/**
 * 由 WCPE 在 2023/2/2 15:05 创建
 * <p>
 * Created by WCPE on 2023/2/2 15:05
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.4-alpha-dev-1
 */
interface BaseSQLMapper {

    @Select(
        """
        SELECT
        COUNT(*)
        FROM
        INFORMATION_SCHEMA.TABLES
        WHERE
        TABLE_SCHEMA =#{databaseName}
        AND
        TABLE_NAME = #{tableName};
        """
    )
    fun existTable(@Param("databaseName") databaseName: String, @Param("tableName") tableName: String): Boolean


    @Select(
        """
        SELECT
        COUNT(*)
        FROM
        INFORMATION_SCHEMA.COLUMNS
        WHERE
        TABLE_SCHEMA =#{databaseName}
        AND
        TABLE_NAME = #{tableName}
        AND
        COLUMN_NAME = #{columnName};
        """
    )
    fun existColumn(
        @Param("databaseName") databaseName: String,
        @Param("tableName") tableName: String,
        @Param("columnName") columnName: String
    ): Boolean

    @Update(
        """
        ALTER TABLE #{oldTableName} RENAME TO #{newTableName};
        """
    )
    fun alterTableName(
        @Param("oldTableName") oldTableName: String,
        @Param("newTableName") newTableName: String
    ): Boolean

}