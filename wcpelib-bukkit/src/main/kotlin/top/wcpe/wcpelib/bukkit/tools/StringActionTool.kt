package top.wcpe.wcpelib.bukkit.tools

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.bukkit.extend.plugin.runTask
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
    @JvmStatic
    fun eval(stringActions: List<String>, parserPlaceholderApi: Boolean, player: Player?) {
        WcpeLib.pluginScope.launch {
            for (action in stringActions) {
                eval(action, parserPlaceholderApi, player)
            }
        }
    }

    private fun setPlaceholders(player: Player?, text: String): String {
        return try {
            val forName = Class.forName("me.clip.placeholderapi.PlaceholderAPI")
            val method = forName.getMethod("setPlaceholders", Player::class.java, String::class.java)
            method.invoke(null, player, text) as String
        } catch (e: Exception) {
            logger.log(Level.WARNING, "无法获取 PlaceholderAPI !", e)
            text
        }
    }

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
                    WcpeLib.instance.runTask {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.substring(5))
                    }
                }

                action.startsWith("[BD]") -> {
                    Bukkit.broadcastMessage(
                        if (parserPlaceholderApi) setPlaceholders(
                            player,
                            action.substring(4)
                        ) else action.substring(4)
                    )
                }

                action.startsWith("[PLAYER]") -> {
                    WcpeLib.instance.runTask {
                        player?.let { Bukkit.dispatchCommand(it, action.substring(8)) }
                    }
                }

                action.startsWith("[CHAT]") -> {
                    WcpeLib.instance.runTask {
                        player?.chat(
                            if (parserPlaceholderApi) setPlaceholders(
                                player,
                                action.substring(6)
                            ) else action.substring(6)
                        )
                    }
                }

                action.startsWith("[TITLE]") -> {
                    val split = action.substring(7).split(";")
                    player?.sendTitle(
                        if (parserPlaceholderApi) setPlaceholders(player, split[3]) else split[3],
                        if (parserPlaceholderApi) setPlaceholders(player, split[4]) else split[4],
                        split[0].toInt(),
                        split[1].toInt(),
                        split[2].toInt()
                    )
                }

                else -> {
                    WcpeLib.instance.runTask {
                        player?.sendMessage(if (parserPlaceholderApi) setPlaceholders(player, action) else action)
                    }
                }
            }
        } catch (e: Exception) {
            logger.log(Level.WARNING, "执行操作 [$action] 出现错误!", e)
        }
    }
}