package top.wcpe.wcpelib.bukkit.mapper

import org.apache.ibatis.annotations.*
import top.wcpe.wcpelib.bukkit.entity.PlayerData


/**
 * 由 WCPE 在 2023/7/10 14:46 创建
 * <p>
 * Created by WCPE on 2023/7/10 14:46
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
interface PlayerDataMapper {
    @Update(
        """
        CREATE TABLE IF NOT EXISTS `wpcelib_player_data` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `player_name` VARCHAR(255) NOT NULL,
            `uuid` VARCHAR(36) NOT NULL,
            `last_server_name` VARCHAR(255),
            `first_login_time` bigint NOT NULL,
            `last_login_time` bigint NOT NULL
        );
        """
    )
    fun createTable()

    @Insert("INSERT INTO `wpcelib_player_data` (player_name, uuid, last_server_name, first_login_time, last_login_time) VALUES (#{playerName}, #{uuid}, #{lastServerName}, #{firstLoginTime}, #{lastLoginTime})")
    fun insertPlayerData(player: PlayerData): Int

    @Update("UPDATE `wpcelib_player_data` SET uuid = #{uuid}," +
            " last_server_name = #{lastServerName}," +
            " last_login_time = #{lastLoginTime}" +
            " WHERE player_name = #{playerName}")
    fun updatePlayerByName(playerData: PlayerData): Int

    @Select("SELECT * FROM `wpcelib_player_data` WHERE player_name = #{playerName}")
    @Results(
        Result(property = "playerName", column = "player_name"),
        Result(property = "uuid", column = "uuid"),
        Result(property = "lastServerName", column = "last_server_name"),
        Result(property = "firstLoginTime", column = "first_login_time"),
        Result(property = "lastLoginTime", column = "last_login_time")
    )
    fun getPlayerDataByName(playerName: String): PlayerData?
}