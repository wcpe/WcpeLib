package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.Message
import kotlin.math.max

/**
 * 由 WCPE 在 2023/7/21 14:06 创建
 * <p>
 * Created by WCPE on 2023/7/21 14:06
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
abstract class AbstractCommand(
    val name: String,
    val description: String,
    val permission: String,
    val permissionMessage: String,
    val aliases: List<String>,
    val arguments: List<Argument>,
    val usageMessage: String,
    val playerOnly: Boolean,
    val playerOnlyMessage: String,
    val opOnly: Boolean,
    val opOnlyMessage: String,
) {

    var commandExecutor: CommandExecutor? = null
    var tabCompleter: TabCompleter? = null

    infix fun String.to(required: Boolean): Argument = Argument(this, required)

    fun register(instance: Any): Boolean {
        if (this is ChildCommand) {
            return false
        }
        return CommandManager.registerCommand(this, instance)
    }

    private fun requiredArgs(
        argsStrings: Array<String?>, arguments: List<Argument>,
    ): List<Argument> {
        val result = mutableListOf<Argument>()
        for ((i, value) in arguments.withIndex()) {
            if (value.required && argsStrings[i] == null) {
                result.add(value)
            }
        }
        return result
    }

    private fun notRequiredArgsReplace(argsStrings: Array<String?>, arguments: List<Argument>) {
        for ((i, argument) in arguments.withIndex()) {
            if (argument.required) {
                continue
            }
            val arg = argsStrings[i]
            if (arg == null || arg == " " || arg == ".") {
                argsStrings[i] = null
            }
        }
    }

    fun handleExecute(commandSender: CommandSender<*>, args: Array<String?>) {
        val argsStrings = args.copyOf(max(args.size, arguments.size))
        if (beforeExecute(commandSender, argsStrings)) {
            notRequiredArgsReplace(argsStrings, arguments)
            if (this is CommandExecutor) {
                execute(commandSender, argsStrings)
            } else {
                commandExecutor?.execute(commandSender, argsStrings)
            }
        }
    }

    fun handleTabComplete(commandSender: CommandSender<*>, args: Array<String>): List<String> {
        return if (this is TabCompleter) {
            tabComplete(commandSender, args)
        } else {
            tabCompleter?.tabComplete(commandSender, args) ?: emptyList()
        }
    }

    /**
     *
     * 执行 execute 之前执行
     *
     * @param commandSender 执行的对象
     * @param args 参数
     * @return true 检查通过
     * @return false 检查不通过
     */
    open fun beforeExecute(commandSender: CommandSender<*>, args: Array<String?>): Boolean {
        if (playerOnly && !commandSender.isPlayer()) {
            commandSender.sendMessage(playerOnlyMessage)
            return false
        }
        if (opOnly) {
            if (!commandSender.isOp()) {
                commandSender.sendMessage(opOnlyMessage)
                return false
            }
        } else {
            if (permission != "*" && !commandSender.hasPermission(permission)) {
                commandSender.sendMessage(permissionMessage)
                return false
            }
        }
        if (arguments.isEmpty()) {
            return true
        }
        val requiredArgResult = requiredArgs(args, arguments)
        if (requiredArgResult.isNotEmpty()) {
            commandSender.sendMessage(usageMessage)
            commandSender.sendMessage(
                Message.CommandArgsError.toLocalization("%arguments%" to requiredArgResult.joinToString(
                    " "
                ) {
                    Message.RequiredFormat.toLocalization("%command_name%" to it.name)
                })
            )
            return false
        }
        return true
    }


}
