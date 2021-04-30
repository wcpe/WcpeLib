package top.wcpe.wcpelib.bukkit.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
