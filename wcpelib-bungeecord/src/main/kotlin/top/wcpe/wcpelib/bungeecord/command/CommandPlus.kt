package top.wcpe.wcpelib.bungeecord.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.TabExecutor
import top.wcpe.wcpelib.common.command.Command
import top.wcpe.wcpelib.common.command.CommandPlus

/**
 * 由 WCPE 在 2022/9/12 13:31 创建
 *
 * Created by WCPE on 2022/9/12 13:31
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
class CommandPlus private constructor(
    name: String,
    private val plugin: Plugin,
    override val aliasList: List<String>,
    override val subCommandMap: LinkedHashMap<String, Command>,
    override var mainCommand: Command?,
    override var hideNoPermissionHelp: Boolean
) : net.md_5.bungee.api.plugin.Command(
    name,
    mainCommand?.permission ?: "",
    *aliasList.toTypedArray()
), CommandPlus, TabExecutor {


    override fun execute(sender: CommandSender, args: Array<String?>) {

        super.execute(object : top.wcpe.wcpelib.common.command.CommandSender<CommandSender> {
            override fun getAdapter(): CommandSender {
                return sender
            }

            override fun isPlayer(): Boolean {
                return sender is ProxiedPlayer
            }

            override fun sendMessage(message: String) {
                sender.sendMessage(message)
            }

            override fun hasPermission(permission: String): Boolean {
                return sender.hasPermission(permission)
            }

        }, args)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): Iterable<String> {
        if (args.isEmpty()) {
            return emptyList()
        }
        val m = mainCommand
        if (m != null && sender.hasPermission(getSubPermission(m))) {
            val tabCompleter = (m as BungeeCordCommand).tabCompleter
            if (tabCompleter != null) {
                return tabCompleter.onTabComplete(sender, args)
            }
        }
        if (args.size == 1) {
            return subCommandMap.values
                .filter { sub ->
                    !sub.hideNoPermissionHelp || sender.hasPermission(
                        getSubPermission(sub)
                    )
                }
                .map(Command::name)
                .filter { name -> name.startsWith(args[0]) }.toList()
        }
        val command = subCommandMap[args[0]]
        if (command != null && (!command.onlyPlayerUse || sender is ProxiedPlayer)) {
            val tabCompleter = (command as BungeeCordCommand).tabCompleter
            if (tabCompleter != null) {
                return tabCompleter.onTabComplete(sender, args.copyOfRange(1, args.size))
            }
        }
        return emptyList()
    }


    override fun registerThis(): CommandPlus {
        plugin.proxy.pluginManager.registerCommand(plugin, this)
        return this
    }

    class Builder {
        private val name: String
        private val plugin: Plugin
        private val aliasList = mutableListOf<String>()
        private val subCommandMap = linkedMapOf<String, Command>()
        private var mainCommand: Command? = null
        private var hideNoPermissionHelp: Boolean = false

        constructor(name: String, plugin: Plugin) {
            this.name = name
            this.plugin = plugin
        }

        constructor(mainCommand: top.wcpe.wcpelib.bungeecord.command.Command, plugin: Plugin) {
            this.mainCommand = mainCommand
            this.name = mainCommand.name
            this.plugin = plugin
        }

        fun mainCommand(mainCommand: top.wcpe.wcpelib.bungeecord.command.Command): Builder {
            this.mainCommand = mainCommand
            return this
        }

        fun aliases(vararg aliases: String): Builder {
            this.aliasList.addAll(aliases)
            return this
        }

        fun hideNoPermissionHelp(hideNoPermissionHelp: Boolean): Builder {
            this.hideNoPermissionHelp = hideNoPermissionHelp
            return this
        }

        fun build(): top.wcpe.wcpelib.bungeecord.command.CommandPlus {
            return CommandPlus(name, plugin, aliasList, subCommandMap, mainCommand, hideNoPermissionHelp)
        }
    }

}