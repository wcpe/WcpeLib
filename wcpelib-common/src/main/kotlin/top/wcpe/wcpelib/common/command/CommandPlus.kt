package top.wcpe.wcpelib.common.command

import top.wcpe.wcpelib.common.utils.collector.ListUtil
import top.wcpe.wcpelib.common.utils.string.StringUtil

/**
 * 由 WCPE 在 2022/9/5 21:05 创建
 *
 * Created by WCPE on 2022/9/5 21:05
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-3
 */
interface CommandPlus {
    fun getName(): String
    val aliasList: List<String>
    val subCommandMap: LinkedHashMap<String, Command>
    var mainCommand: Command?
    var hideNoPermissionHelp: Boolean


    fun registerThis(): CommandPlus

    fun getSubPermission(command: Command): String {
        return command.permission ?: " ${
            if (mainCommand == null) {
                "${getName()}."
            } else {
                ""
            }
        }${command.name}.use"
    }

    fun registerSubCommand(subCommand: Command): CommandPlus {
        subCommandMap[subCommand.name] = subCommand
        return this
    }

    private fun executeJudge(
        command: Command,
        sender: CommandSender<*>
    ): Boolean {
        if (command.onlyPlayerUse && !sender.isPlayer()) {
            sender.sendMessage(command.noPlayerMessage)
            return true
        }
        val permission = getSubPermission(command)
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(
                StringUtil.replaceString(
                    command.noPermissionMessage,
                    "permission:$permission"
                )
            )
            return true
        }
        return false
    }

    private fun requiredArgs(
        args: Array<String?>,
        listCommandArgument: List<CommandArgument>
    ): Int {

        val tempArgs = if (args.size < listCommandArgument.size) {
            args.copyOf(listCommandArgument.size)
        } else {
            args
        }
        for (i in listCommandArgument.indices) {
            if (listCommandArgument[i].ignoreArg == null && tempArgs[i] == null) {
                return i
            }
        }
        return -1
    }

    private fun ignoreArgReplace(
        args: Array<String?>,
        listCommandArgument: List<CommandArgument>
    ): Array<String?> {
        val tempArgs = if (args.size < listCommandArgument.size) {
            args.copyOf(listCommandArgument.size)
        } else {
            args
        }
        for (i in listCommandArgument.indices) {
            val ignoreArg = listCommandArgument[i].ignoreArg
            val arg = tempArgs[i]
            if (ignoreArg != null && (arg.isNullOrEmpty() || arg == " ")) {
                tempArgs[i] = ignoreArg
            }
        }
        return tempArgs
    }

    fun execute(sender: CommandSender<*>, args: Array<String?>) {
        val command = mainCommand
        if (command != null) {
            if (executeJudge(command, sender)) {
                return
            }
            val listCommandArgument = command.args
            val i = requiredArgs(args, listCommandArgument)
            if (i != -1) {
                sender.sendMessage("§c指令执行错误 请填写必填参数 §6<${listCommandArgument[i].name}> §c!")
                return
            }

            command.executeComponent?.execute(sender, ignoreArgReplace(args, listCommandArgument))
            return
        }
        if (args.isNotEmpty()) {
            val subCommand = subCommandMap[args[0]]
            if (subCommand != null) {
                var subArgs = if (args.size > 1) {
                    args.copyOfRange(1, args.size)
                } else {
                    arrayOf()
                }
                val listCommandArgument = subCommand.args
                val i = requiredArgs(subArgs, listCommandArgument)
                if (i != -1) {
                    sender.sendMessage("§c指令执行错误 请填写必填参数 §6<${listCommandArgument[i].name}> §c!")
                    return
                }
                subArgs = ignoreArgReplace(subArgs, listCommandArgument)
                if (executeJudge(subCommand, sender)) {
                    return
                }

                subCommand.executeComponent?.execute(sender, subArgs)
                return
            }
        }
        if (hideNoPermissionHelp && !sender.hasPermission("${getName()}.help.use")) {
            return
        }
        if (args.isEmpty() || "help" == args[0]) {
            var page = 1
            if (args.size > 1) {
                try {
                    page = args[1]!!.toInt()
                } catch (_: NumberFormatException) {

                }
            }

            val splitSubCommandList = ListUtil.splitList(
                subCommandMap.values.filter {
                    if (it.hideNoPermissionHelp) {
                        sender.hasPermission(getSubPermission(it))
                    } else {
                        true
                    }
                }.toList(), 5
            )

            page = page.coerceAtMost(splitSubCommandList.size)
            sender.sendMessage("§6===== §e${getName()} §a指令帮助 §e$page§a/§e${splitSubCommandList.size} §a页 §6=====")
            sender.sendMessage("§b/${getName()}")
            for (alias: String in aliasList) {
                sender.sendMessage("§b/$alias")
            }
            val repeatString = StringUtil.getRepeatString(" ", getName().length)
            for (subCommand in splitSubCommandList[page - 1]) {
                sender.sendMessage(
                    ("§6>$repeatString §e${subCommand.name} ${
                        subCommand.args.joinToString(" ") { arg ->
                            if (arg.ignoreArg == null) {
                                "<${arg.name}>"
                            } else {
                                "[${arg.name}]"
                            }
                        }
                    }")
                )
                sender.sendMessage("§6>$repeatString §a描述:§6 " + subCommand.describe)
                sender.sendMessage("§6>$repeatString §a权限:§c " + getSubPermission(subCommand))
            }
            sender.sendMessage("§6> []为选填参数 <>为必填参数")
            return
        }
        sender.sendMessage("§c指令不存在或参数错误! §a请输入§e/${getName()} help [页码] §a进行查询")

    }


}