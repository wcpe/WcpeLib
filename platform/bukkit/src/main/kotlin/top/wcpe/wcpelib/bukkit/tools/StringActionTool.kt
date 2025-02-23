package top.wcpe.wcpelib.bukkit.tools

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.bukkit.extend.plugin.runTask
import top.wcpe.wcpelib.bukkit.extend.setPlaceholders
import java.util.logging.Level

/**
 * 由 WCPE 在 2024/7/2 16:53 创建
 * <p>
 * Created by WCPE on 2024/7/2 16:53
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.8.0-SNAPSHOT
 */
@Deprecated("请使用 [top.wcpe.wcpelib.bukkit.tools.v2.StringActionTool]")
object StringActionTool {


    private val logger = WcpeLib.instance.logger


    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     * [DELAY]5s
     *
     * @param stringActions 执行的操作列表
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player 玩家
     */
    @Deprecated("请使用 [top.wcpe.wcpelib.bukkit.tools.v2.StringActionTool]")
    @JvmStatic
    fun eval(stringActions: List<String>, parserPlaceholderApi: Boolean, player: Player?) {
        WcpeLib.pluginScope.launch {
            for (action in stringActions) {
                eval(action, parserPlaceholderApi, player)
            }
        }
    }

    /**
     * 解析执行操作列表
     *
     * @param stringActions 单个操作字符串
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player 玩家
     * @param consumerSingleAction 消费单个操作字符串
     */
    @Deprecated("请使用 [top.wcpe.wcpelib.bukkit.tools.v2.StringActionTool]")
    @JvmStatic
    fun eval(
        stringActions: List<String>,
        parserPlaceholderApi: Boolean,
        player: Player?,
        consumerSingleAction: (String) -> String,
    ) {
        WcpeLib.pluginScope.launch {
            for (action in stringActions) {
                eval(consumerSingleAction(action), parserPlaceholderApi, player)
            }
        }
    }

    private fun setPlaceholders(player: Player?, text: String, parserPlaceholderApi: Boolean): String {
        if (!parserPlaceholderApi || player == null) {
            return text
        }
        return text.setPlaceholders(player)
    }

    @Deprecated("请使用 [top.wcpe.wcpelib.bukkit.tools.v2.StringActionTool]")
    @JvmStatic
    fun compatibleEval(stringAction: String, parserPlaceholderApi: Boolean, player: Player?) {
        WcpeLib.pluginScope.launch {
            eval(stringAction, parserPlaceholderApi, player)
        }
    }


    /**
     * 解析并执行单个操作
     *
     * @param stringAction 单个操作字符串
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player 玩家
     */
    @Deprecated("请使用 [top.wcpe.wcpelib.bukkit.tools.v2.StringActionTool]")
    @JvmStatic
    suspend fun eval(stringAction: String, parserPlaceholderApi: Boolean, player: Player?) {
        val action = if (player != null) {
            stringAction.replace("%player%", player.name)
        } else {
            stringAction
        }
        try {
            when {
                action.startsWith("[DELAY]") -> {
                    val pattern = """\[DELAY](\d+)([smh]?)""".toRegex()

                    val match = pattern.find(action)
                    if (match != null) {
                        val (value, unit) = match.destructured
                        val timeInMillis = when (unit) {
                            "s" -> value.toLong() * 1000
                            "m" -> value.toLong() * 60 * 1000
                            "h" -> value.toLong() * 60 * 60 * 1000
                            "" -> value.toLong()
                            else -> 0L
                        }
                        delay(timeInMillis)
                    }
                }


                action.startsWith("[CMD]") -> {
                    val actionStr = action.substring(5)
                    val result = setPlaceholders(player, actionStr, parserPlaceholderApi)
                    WcpeLib.instance.runTask {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), result)
                    }
                }

                action.startsWith("[BD]") -> {
                    val actionStr = action.substring(4)
                    val result = setPlaceholders(player, actionStr, parserPlaceholderApi)
                    Bukkit.broadcastMessage(result)
                }

                action.startsWith("[PLAYER]") -> {
                    player?.let { confirmPlayer ->
                        val actionStr = action.substring(8)
                        val result = setPlaceholders(player, actionStr, parserPlaceholderApi)
                        WcpeLib.instance.runTask {
                            Bukkit.dispatchCommand(confirmPlayer, result)
                        }
                    }
                }

                action.startsWith("[CHAT]") -> {
                    player?.run {
                        WcpeLib.instance.runTask {
                            val actionStr = action.substring(6)
                            val result = setPlaceholders(player, actionStr, parserPlaceholderApi)
                            chat(result)
                        }
                    }

                }

                action.startsWith("[TITLE]") -> {
                    player?.run {
                        val actionStr = action.substring(7)
                        val result = setPlaceholders(player, actionStr, parserPlaceholderApi)
                        val origin = result.split(";")
                        val title = origin[3]
                        val subTitle = origin[4]
                        val fadeIn = origin[0].toIntOrNull() ?: 10
                        val stay = origin[1].toIntOrNull() ?: 70
                        val fadeOut = origin[2].toIntOrNull() ?: 20

                        sendTitle(
                            title,
                            subTitle,
                            fadeIn,
                            stay,
                            fadeOut
                        )
                    }
                }

                else -> {
                    player?.run {
                        val result = setPlaceholders(player, action, parserPlaceholderApi)
                        sendMessage(result)
                    }
                }
            }
        } catch (e: Exception) {
            logger.log(Level.WARNING, "执行操作 [$action] 出现错误!", e)
        }
    }
}