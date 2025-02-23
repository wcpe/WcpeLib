package top.wcpe.wcpelib.bukkit.tools.v2

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.slf4j.LoggerFactory
import top.wcpe.wcpelib.bukkit.WcpeLib
import top.wcpe.wcpelib.bukkit.extend.plugin.sync
import top.wcpe.wcpelib.bukkit.extend.setPlaceholders

/**
 * 由 WCPE 在 2025/2/23 23:59 创建
 * <p>
 * Created by WCPE on 2025/2/23 23:59
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
data class StringActionTool(
    private val stringActions: List<String>,
    private val plugin: Plugin = WcpeLib.instance,
) {
    companion object {

        @JvmStatic
        fun setPlaceholders(player: Player?, text: String): String {
            if (player == null) {
                return text.setPlaceholders(player)
            }
            val rep = text.replace("%player%", player.name).replace("%player_name%", player.name)
            return rep.setPlaceholders(player)
        }
    }

    constructor(vararg stringActions: String, plugin: Plugin = WcpeLib.instance) : this(stringActions.toList(), plugin)

    private val logger = LoggerFactory.getLogger(javaClass)

    private val asts: List<Ast>

    init {
        val asts = mutableListOf<Ast>()
        for (action in stringActions) {
            val ast = Ast.generateAst(plugin, action)
            if (ast == null) {
                throw IllegalArgumentException("无法解析字符串 [$action]!")
            }
            asts.add(ast)
        }
        this.asts = asts
    }

    abstract class Ast() {
        abstract val plugin: Plugin
        abstract val text: String
        abstract suspend fun execute(player: Player?)

        companion object {

            private val astMap = mutableMapOf<String, Class<out Ast>>()

            init {
                register(MessageAction::class.java, "")
                register(DelayAction::class.java, "delay")
                register(ConsoleCommandAction::class.java, "console", "cmd")
                register(PlayerCommandAction::class.java, "player")
                register(BroadcastAction::class.java, "broadcast", "bd")
                register(ChatAction::class.java, "chat")
                register(TitleAction::class.java, "title")
            }

            /**
             * 注册一个 Ast
             */
            @JvmStatic
            fun register(astClass: Class<out Ast>, vararg aliases: String) {
                for (s in aliases) {
                    astMap[s.lowercase()] = astClass
                }
            }

            /**
             * 从给定的字符串中提取以 `[` 开头、以 `]` 结尾的子字符串。
             *
             * @param text 要解析的字符串。
             * @return 提取的子字符串，如果没有找到则返回 `null`。
             */
            @JvmStatic
            fun readToken(text: String): String? {
                // 检查文本是否以 [ 开头
                if (!text.startsWith("[")) {
                    return null
                }
                // 找到第一个 ] 的位置
                val endIndex = text.indexOf(']')

                // 如果找到了 ]
                if (endIndex == -1) {
                    return null

                }
                // 提取 [ 和 ] 之间的内容
                return text.substring(1, endIndex)
            }

            /**
             * 从给定的字符串中生成一个抽象语法树（AST）节点。
             *
             * @param plugin 插件实例。
             * @param text 要解析的字符串。
             * @return 生成的 AST 节点，如果无法解析则返回 `null`。
             */
            @JvmStatic
            fun generateAst(plugin: Plugin, text: String): Ast? {
                val token = readToken(text)
                if (token == null) {
                    val astClass = astMap[""] ?: return null
                    return astClass.getConstructor(Plugin::class.java, String::class.java).newInstance(plugin, text)
                }
                val astClass = astMap[token.lowercase()]
                if (astClass == null) {
                    return null
                }
                val subText = text.substring(token.length + 2)
                return astClass.getConstructor(Plugin::class.java, String::class.java).newInstance(plugin, subText)
            }
        }
    }

    data class ConsoleCommandAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            val text = setPlaceholders(player, text)
            plugin.sync {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text)
            }
        }
    }

    class PlayerCommandAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            if (player == null) {
                return
            }
            val text = setPlaceholders(player, text)
            plugin.sync {
                Bukkit.dispatchCommand(player, text)
            }
        }
    }

    data class ChatAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            if (player == null) {
                return
            }
            val text = setPlaceholders(player, text)
            plugin.sync {
                player.chat(text)
            }
        }
    }

    data class DelayAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            val text = setPlaceholders(player, text)
            val pattern = """\[DELAY](\d+)([smh]?)""".toRegex()

            val match = pattern.find(text)
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
    }

    data class BroadcastAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            val text = setPlaceholders(player, text)
            Bukkit.broadcastMessage(text)
        }
    }


    data class TitleAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            if (player == null) {
                return
            }
            val text = setPlaceholders(player, text)

            val origin = text.split(";")
            val title = origin[3]
            val subTitle = origin[4]
            val fadeIn = origin[0].toIntOrNull() ?: 10
            val stay = origin[1].toIntOrNull() ?: 70
            val fadeOut = origin[2].toIntOrNull() ?: 20

            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }


    data class MessageAction(
        override val plugin: Plugin,
        override val text: String,
    ) : Ast() {
        override suspend fun execute(player: Player?) {
            if (player == null) {
                return
            }
            val text = setPlaceholders(player, text)
            player.sendMessage(text)
        }
    }

    /**
     * 运行
     * @param player 玩家
     */
    suspend fun eval(player: Player?) {
        for (ast in asts) {
            ast.execute(player)
        }
    }

    /**
     * 兼容性运行
     * @param player 玩家
     */
    fun compatibleEval(player: Player?) {
        runBlocking {
            for (ast in asts) {
                ast.execute(player)
            }
        }
    }
}