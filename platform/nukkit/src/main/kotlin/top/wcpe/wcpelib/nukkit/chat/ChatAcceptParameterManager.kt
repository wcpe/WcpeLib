package top.wcpe.wcpelib.nukkit.chat

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import top.wcpe.wcpelib.nukkit.WcpeLib
import top.wcpe.wcpelib.nukkit.chat.entity.ChatAcceptParameterTask

/**
 * 由 WCPE 在 2021/12/18 19:32 创建
 *
 * Created by WCPE on 2021/12/18 19:32
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
        Server.getInstance().pluginManager.registerEvents(object : Listener {
            @EventHandler(priority = EventPriority.HIGH)
            fun playerChatEvent(e: PlayerChatEvent) {
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
        }, WcpeLib.getInstance())

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