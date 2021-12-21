package top.wcpe.wcpelib.nukkit.chat

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import lombok.Data
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
    private val playerTask: MutableMap<String, ChatAcceptParameterTask> = mutableMapOf()


    init {
        Server.getInstance().pluginManager.registerEvents(object : Listener {

            @EventHandler(priority = EventPriority.LOWEST)
            fun playerChatEvent(e: PlayerChatEvent) {
                var chatAcceptParameterTask: ChatAcceptParameterTask = playerTask[e.player.name] ?: return

                if (chatAcceptParameterTask.timeStamp < System.currentTimeMillis()) {
                    playerTask.remove(e.player.name)
                    return
                }

                e.isCancelled = true

                if (chatAcceptParameterTask.cancelJudgeTask(e.player, e.message)) {
                    chatAcceptParameterTask.cancelSuccessTask(e.player, e.message)
                    playerTask.remove(e.player.name)
                    return
                }
                if (chatAcceptParameterTask.judge(e.player, e.message)) {
                    chatAcceptParameterTask.judgeTrueTask(e.player, e.message)
                    playerTask.remove(e.player.name)
                    return
                }
                chatAcceptParameterTask.judgeFalseTask(e.player, e.message)
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
    ): Boolean {
        var task = playerTask[player.name]
        if (task != null) {
            if (task.timeStamp < System.currentTimeMillis()) {
                playerTask.remove(player.name)
                return false
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
        return true
    }


}