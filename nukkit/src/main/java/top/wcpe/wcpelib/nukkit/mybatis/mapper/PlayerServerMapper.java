package top.wcpe.wcpelib.nukkit.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayerServerMapper {
	int existTable();

	void createTable();
	void dropTable();
	List<String> listPlayer();

	String selectPlayerServer(@Param("name") String name);

	void updatePlayerServer(@Param("name") String name, @Param("servername") String serverName);

	void insertPlayerServer(@Param("name") String name, @Param("servername") String serverName);

	void delPlayerServer(@Param("name") String name);
}
