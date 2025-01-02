package top.wcpe.wcpelib.bukkit.data.impl

import top.wcpe.wcpelib.bukkit.data.IDataManager
import top.wcpe.wcpelib.bukkit.entity.PlayerData

/**
 * 由 WCPE 在 2023/7/29 9:47 创建
 * <p>
 * Created by WCPE on 2023/7/29 9:47
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.1-alpha-dev-2
 */
class NullDataManager : IDataManager {
    override fun getPlayerDataByName(playerName: String): PlayerData? {
        return null
    }

    override fun savePlayerData(playerData: PlayerData): Boolean {
        return false
    }
}