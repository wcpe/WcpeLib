package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.Player
import cn.nukkit.Server
import top.wcpe.wcpelib.nukkit.WcpeLib
import top.wcpe.wcpelib.nukkit.extend.plugin.runTask
import top.wcpe.wcpelib.nukkit.placeholder.PlaceholderManager

/**
 * 由 WCPE 在 2022/6/2 15:48 创建
 *
 * Created by WCPE on 2022/6/2 15:48
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-2
 */
object StringActionUtil {

    @JvmStatic
    fun executionCommands(stringActionList: List<String>, parserPlaceHolder: Boolean, player: Player?) {
        for (command in stringActionList) {
            executionCommands(command, parserPlaceHolder, player)
        }
    }

    @JvmStatic
    fun executionCommands(stringActionList: List<String>, player: Player?) {
        executionCommands(stringActionList, false, player)
    }

    @JvmStatic
    fun executionCommands(stringAction: String, player: Player?) {
        executionCommands(stringAction, false, player)
    }

    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [OP]/say 我用OP权限执行了say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     *
     * @param stringAction
     * @param parserPlaceHolder 是否解析 PlaceHolder 注册的变量
     * @param player
     */
    @JvmStatic
    fun executionCommands(stringAction: String, parserPlaceHolder: Boolean, player: Player?) {

        if (stringAction.startsWith("[CMD]")) {
            WcpeLib.getInstance().runTask {
                Server.getInstance()
                    .dispatchCommand(Server.getInstance().consoleSender, stringAction.substring(5))
            }
            return
        }
        if (stringAction.startsWith("[BD]")) {
            Server.getInstance().broadcastMessage(stringAction.substring(4))
            return
        }

        player ?: return

        val finalStringAction = (if (parserPlaceHolder) PlaceholderManager.replaceString(
            player,
            stringAction
        ) else stringAction).replace("%player%", player.name)

        if (finalStringAction.startsWith("[OP]")) {
            val isOp = player.isOp
            try {
                player.isOp = true
                Server.getInstance().dispatchCommand(player, finalStringAction.substring(4))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                player.isOp = isOp
            }
            return
        }
        if (finalStringAction.startsWith("[PLAYER]")) {
            Server.getInstance().dispatchCommand(player, finalStringAction.substring(8))
            return
        }
        if (finalStringAction.startsWith("[CHAT]")) {
            player.chat(finalStringAction.substring(6))
            return
        }
        if (finalStringAction.startsWith("[TITLE]")) {
            val split = finalStringAction.substring(7).split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            player.sendTitle(split[3], split[4], split[0].toInt(), split[1].toInt(), split[2].toInt())
            return
        }
        if (finalStringAction.startsWith("[ACTION]")) {
            player.sendActionBar(finalStringAction.substring(8))
            return
        }
        player.sendMessage(finalStringAction)
    }
}