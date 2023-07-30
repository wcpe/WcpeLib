package top.wcpe.wcpelib.bukkit.chat

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.bukkit.chat.entity.ChatAcceptParameterTask

/**
 * 由 WCPE 在 2021/12/28 0:40 创建
 *
 * Created by WCPE on 2021/12/28 0:40
 *
 * Github: https://github.com/wcpe
 *
 * QQ: 1837019522
 *
 * @author WCPE
 */
object ChatAcceptParameterManager {

    @JvmStatic
    val playerTask: MutableMap<String, ChatAcceptParameterTask> = mutableMapOf()


    init {
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler(priority = EventPriority.HIGH)
            fun playerChatEvent(e: AsyncPlayerChatEvent) {
                playerTask[e.player.name]?.let {
                    if (it.timeStamp < System.currentTimeMillis()) {
                        playerTask.remove(e.player.name)
                        return
                    }

                    e.isCancelled = true

                    if (it.cancelJudgeTask(e.player, e.message)) {
                        it.cancelSuccessTask(e.player, e.message)
                        playerTask.remove(e.player.name)
                        return
                    }
                    if (it.judge(e.player, e.message)) {
                        it.judgeTrueTask(e.player, e.message)
                        playerTask.remove(e.player.name)
                        return
                    }
                    it.judgeFalseTask(e.player, e.message)
                }
            }
        }, WcpeLib.instance)
    }

    fun putChatAcceptParameterTask(
        player: Player,
        inputTime: Long,
        cancelJudgeTask: (player: Player, message: String) -> Boolean,
        cancelSuccessTask: (player: Player, message: String) -> Unit,
        judge: (player: Player, message: String) -> Boolean,
        judgeTrueTask: (player: Player, message: String) -> Unit,
        judgeFalseTask: (player: Player, message: String) -> Unit
    ): Long {
        playerTask[player.name]?.let {
            val currentTimeMillis = System.currentTimeMillis()
            if (it.timeStamp > currentTimeMillis) {
                return it.timeStamp - currentTimeMillis
            }
        }
        playerTask[player.name] = ChatAcceptParameterTask(
            System.currentTimeMillis() + inputTime * 1000,
            cancelJudgeTask,
            cancelSuccessTask,
            judge,
            judgeTrueTask,
            judgeFalseTask
        )
        return -1
    }

}