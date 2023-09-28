package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.command.v2.annotation.ChildCommand
import top.wcpe.wcpelib.common.command.v2.annotation.ParentCommand
import top.wcpe.wcpelib.common.command.v2.annotation.SingleCommand
import top.wcpe.wcpelib.common.command.v2.extend.parentCommand
import top.wcpe.wcpelib.common.command.v2.extend.singleCommand
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

/**
 * 由 WCPE 在 2023/7/26 13:41 创建
 * <p>
 * Created by WCPE on 2023/7/26 13:41
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
object CommandManager {
    /**
     * 注册命令
     * 通过 实现 AbstractCommand 的实例 和 插件的主类实例来注册
     * @param abstractCommand 命令类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerCommand(abstractCommand: AbstractCommand, pluginInstance: Any): Boolean {
        return WcpeLibCommon.platformAdapter?.registerCommand(abstractCommand, pluginInstance) ?: false
    }


    private fun parseAnnotation(commandClass: KClass<*>): AbstractCommand? {
        return parseSingleCommand(commandClass) ?: parseParentCommand(commandClass)
    }

    private fun parseSingleCommand(commandClass: KClass<*>): AbstractCommand? {
        val singleCommandAnnotation = commandClass.findAnnotation<SingleCommand>() ?: return null

        val newInstance = commandClass.createInstance()

        return singleCommand(
            singleCommandAnnotation.name,
            singleCommandAnnotation.description,
            singleCommandAnnotation.aliases.toList(),
            singleCommandAnnotation.arguments.map { Argument(it.name, it.required, it.description) }.toList(),
            singleCommandAnnotation.playerOnly,
            singleCommandAnnotation.playerOnlyMessage,
            singleCommandAnnotation.opOnly,
            singleCommandAnnotation.opOnlyMessage,
            singleCommandAnnotation.usageMessage,
            singleCommandAnnotation.permission,
            singleCommandAnnotation.permissionMessage,
            newInstance as? CommandExecutor,
            newInstance as? TabCompleter
        )
    }

    private fun parseParentCommand(commandClass: KClass<*>): AbstractCommand? {
        val parentCommandAnnotation = commandClass.findAnnotation<ParentCommand>() ?: return null

        val parentCommand = parentCommand(
            parentCommandAnnotation.name,
            parentCommandAnnotation.description,
            parentCommandAnnotation.aliases.toList(),
            parentCommandAnnotation.playerOnly,
            parentCommandAnnotation.playerOnlyMessage,
            parentCommandAnnotation.opOnly,
            parentCommandAnnotation.opOnlyMessage,
            parentCommandAnnotation.usageMessage,
            parentCommandAnnotation.permission,
            parentCommandAnnotation.permissionMessage
        )

        for (nestedClass in commandClass.nestedClasses) {
            parentCommand.addChildCommand(parseChildCommand(parentCommand, nestedClass) ?: continue)
        }

        return parentCommand
    }

    private fun parseChildCommand(
        parentInstance: top.wcpe.wcpelib.common.command.v2.ParentCommand, commandClass: KClass<*>
    ): top.wcpe.wcpelib.common.command.v2.ChildCommand? {
        val childCommandAnnotation = commandClass.findAnnotation<ChildCommand>() ?: return null

        val newInstance = commandClass.createInstance()

        return parentInstance.childCommand(
            childCommandAnnotation.name,
            childCommandAnnotation.description,
            childCommandAnnotation.aliases.toList(),
            childCommandAnnotation.arguments.map { Argument(it.name, it.required, it.description) }.toList(),
            childCommandAnnotation.playerOnly,
            childCommandAnnotation.playerOnlyMessage,
            childCommandAnnotation.opOnly,
            childCommandAnnotation.opOnlyMessage,
            childCommandAnnotation.usageMessage,
            childCommandAnnotation.permission,
            childCommandAnnotation.permissionMessage,
            childCommandAnnotation.shouldDisplay,
            newInstance as? CommandExecutor,
            newInstance as? TabCompleter
        )
    }


    /**
     * 注册命令
     * 通过含有命令注解的类和插件主类实例来注册
     * @param commandClass 含 ParentCommand 注解的 javaClass 类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerCommand(commandClass: Class<*>, pluginInstance: Any): Boolean {
        return registerCommand(commandClass.kotlin, pluginInstance)
    }

    /**
     * 注册命令
     * 通过含有命令注解的类和插件主类实例来注册
     * @param commandClass 含 ParentCommand 注解的 KClass 类
     * @param pluginInstance 插件主类实例
     * @return 返回注册是否成功
     */
    @JvmStatic
    fun registerCommand(commandClass: KClass<*>, pluginInstance: Any): Boolean {
        val parseAnnotation = parseAnnotation(commandClass) ?: return false
        return registerCommand(parseAnnotation, pluginInstance)
    }
}