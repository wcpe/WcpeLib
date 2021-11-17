package top.wcpe.wcpelib.nukkit.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import top.wcpe.wcpelib.nukkit.mybatis.entity.PlayerServer;

import java.util.List;

public interface PlayerServerMapper {
    @Select("select count(*) from information_schema.TABLES t where t.TABLE_SCHEMA =#{databaseName} and t.TABLE_NAME = 'wcpelib_player_server'")
    int existTable(@Param("databaseName") String databaseName);

    @Update("create table `wcpelib_player_server` ( `player_name` varchar(16) NOT NULL, `server_name` varchar(16) NOT NULL, `online` boolean NOT NULL, PRIMARY KEY(`player_name`));")
    void createTable();

    @Update("drop table `wcpelib_player_server`")
    void dropTable();

    @Select("select player_name,server_name,online from `wcpelib_player_server`")
    @ResultMap("PlayerServerMap")
    List<PlayerServer> listPlayerServer();

    @Select("select player_name,server_name,online from `wcpelib_player_server` where `player_name`=#{playerName}")
    @Results(id = "PlayerServerMap", value = {
            @Result(column = "player_name", property = "playerName", id = true),
            @Result(column = "server_name", property = "serverName"),
            @Result(column = "online", property = "online")
    })
    PlayerServer selectPlayerServer(@Param("playerName") String playerName);

    @Select("select player_name,server_name,online from `wcpelib_player_server` where `server_name`=#{serverName} AND `online`= 1")
    @ResultMap("PlayerServerMap")
    List<PlayerServer> selectOnlinePlayerToServer(@Param("serverName") String serverName);


    @Update("update `wcpelib_player_server` set server_name=#{serverName},online=#{online} where `player_name`=#{playerName}")
    void updatePlayerServer(PlayerServer playerServer);

    @Insert("insert into `wcpelib_player_server`(player_name, server_name, online) values(#{playerName},#{serverName},#{online})")
    void insertPlayerServer(PlayerServer playerServer);

    @Update("delete from `wcpelib_player_server` where `player_name`=#{playerName}")
    void delPlayerServer(@Param("player_name") String playerName);
}
