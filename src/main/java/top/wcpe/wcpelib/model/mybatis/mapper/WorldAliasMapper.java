package top.wcpe.wcpelib.model.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorldAliasMapper {
	int existTable();

	void createTable();
void dropTable();
	List<String> listWorld();

	String selectWorldAlias(@Param("name") String name);

	void updateWorldAlias(@Param("name") String name, @Param("alias") String serverName);

	void insertWorldAlias(@Param("name") String name, @Param("alias") String serverName);

	int delWorldAlias(@Param("name") String name);
}
