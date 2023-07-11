package top.wcpe.wcpelib.bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import top.wcpe.wcpelib.bukkit.entity.PlayerData

/**
 * 由 WCPE 在 2023/7/10 17:19 创建
 * <p>
 * Created by WCPE on 2023/7/10 17:19
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.5-alpha-dev-3
 */
class WcpeLibListener : Listener {
    private val logger = WcpeLib.getInstance().logger

    @EventHandler
    fun listenerPlayerJoinEvent(e: PlayerJoinEvent) {
        val player = e.player
        val firstPlayed = player.firstPlayed

        val playerData = WcpeLib.getDataManager().getPlayerDataByName(player.name)
        if (playerData == null) {
            logger.info("由于玩家: [${player.name}] 未上线过 自动读取本地 firstPlayed: [$firstPlayed] 填入")
            WcpeLib.getDataManager().savePlayerData(
                PlayerData(
                    playerName = player.name,
                    uuid = player.uniqueId.toString(),
                    firstLoginTime = if (firstPlayed <= 0) {
                        logger.info("firstPlayed 为空以当前时间为注册时间!")
                        System.currentTimeMillis()
                    } else {
                        logger.info("firstPlayed 为 [${firstPlayed}] 以这个时间为注册时间!")
                        firstPlayed
                    }
                )
            )
            return
        }
        logger.info("玩家: [${player.name}] 上一次进入的服务器: [${playerData.lastServerName}]")
        playerData.lastServerName = WcpeLib.getServerName()
        playerData.lastLoginTime = System.currentTimeMillis()
        WcpeLib.getDataManager().savePlayerData(playerData)
    }
}