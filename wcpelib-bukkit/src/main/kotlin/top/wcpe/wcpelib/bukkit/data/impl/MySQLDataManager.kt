package top.wcpe.wcpelib.bukkit.data.impl

import top.wcpe.wcpelib.bukkit.data.IDataManager
import top.wcpe.wcpelib.bukkit.entity.PlayerData
import top.wcpe.wcpelib.bukkit.mapper.PlayerDataMapper
import top.wcpe.wcpelib.common.mybatis.Mybatis

/**
 * 由 WCPE 在 2023/7/10 15:24 创建
 * <p>
 * Created by WCPE on 2023/7/10 15:24
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
class MySQLDataManager(private val mybatis: Mybatis) : IDataManager {

    init {
        mybatis.addMapper(PlayerDataMapper::class.java)
        mybatis.sqlSessionFactory.openSession(true).use { sqlSession ->
            val mapper = sqlSession.getMapper(PlayerDataMapper::class.java)
            mapper.createTable()
        }
    }

    override fun getPlayerDataByName(playerName: String): PlayerData? {
        mybatis.sqlSessionFactory.openSession(true).use { sqlSession ->
            val mapper = sqlSession.getMapper(PlayerDataMapper::class.java)
            return mapper.getPlayerDataByName(playerName)
        }
    }

    override fun savePlayerData(playerData: PlayerData): Boolean {
        mybatis.sqlSessionFactory.openSession(true).use { sqlSession ->
            val mapper = sqlSession.getMapper(PlayerDataMapper::class.java)
            val playerDataByName = mapper.getPlayerDataByName(playerData.playerName)
            return if (playerDataByName == null) {
                mapper.insertPlayerData(playerData)
            } else {
                mapper.updatePlayerByName(playerData)
            } > 0
        }
    }
}