package top.wcpe.wcpelib.common.command.v2

import top.wcpe.wcpelib.common.WcpeLibCommon
import top.wcpe.wcpelib.common.command.v2.annotation.ChildCommand
import top.wcpe.wcpelib.common.command.v2.annotation.ParentCommand
import top.wcpe.wcpelib.common.command.v2.annotation.SingleCommand
import top.wcpe.wcpelib.common.command.v2.extend.parentCommand
import top.wcpe.wcpelib.common.command.v2.extend.singleCommand
import kotlin.reflect.KClass

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
        val platformAdapter = WcpeLibCommon.platformAdapter ?: return false
        return platformAdapter.registerCommand(abstractCommand, pluginInstance)
    }

    private fun parseAnnotation(commandClass: Class<*>): AbstractCommand? {
        return parseSingleCommand(commandClass) ?: parseParentCommand(commandClass)
    }

    private inline fun <reified T> findAnnotation(commandClass: Class<*>): T? {
        for (annotation in commandClass.annotations) {
            if (annotation is T) {
                return annotation
            }
        }
        return null
    }

    private fun newInstance(clazz: Class<*>): Any? {
        val constructor = clazz.getDeclaredConstructor()
        constructor.isAccessible = true
        return constructor.newInstance()
    }

    private fun parseParentCommand(commandClass: Class<*>): AbstractCommand? {
        val parentCommandAnnotation = findAnnotation<ParentCommand>(commandClass) ?: return null

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
            parentCommandAnnotation.permissionMessage,
            parentCommandAnnotation.opVisibleHelp
        )

        for (nestedClass in commandClass.getDeclaredClasses()) {
            parentCommand.addChildCommand(parseChildCommand(parentCommand, nestedClass) ?: continue)
        }

        return parentCommand
    }

    private fun parseChildCommand(
        parentInstance: top.wcpe.wcpelib.common.command.v2.ParentCommand, commandClass: Class<*>,
    ): top.wcpe.wcpelib.common.command.v2.ChildCommand? {
        val childCommandAnnotation = findAnnotation<ChildCommand>(commandClass) ?: return null

        val newInstance = newInstance(commandClass)

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

    private fun parseSingleCommand(commandClass: Class<*>, classInstance: Any? = null): AbstractCommand? {
        val singleCommandAnnotation = findAnnotation<SingleCommand>(commandClass) ?: return null

        val instance = classInstance ?: newInstance(commandClass)
        // 生成抽象命令对象并返回
        return singleCommand(
            singleCommandAnnotation.name,  // 命令名称
            singleCommandAnnotation.description,  // 命令描述
            singleCommandAnnotation.aliases.toList(),  // 命令别名列表
            // 转换命令参数列表为 Argument 对象列表
            singleCommandAnnotation.arguments.map { Argument(it.name, it.required, it.description) }.toList(),
            singleCommandAnnotation.playerOnly,  // 是否只允许玩家执行
            singleCommandAnnotation.playerOnlyMessage,  // 玩家执行时的提示消息
            singleCommandAnnotation.opOnly,  // 是否只允许管理员执行
            singleCommandAnnotation.opOnlyMessage,  // 管理员执行时的提示消息
            singleCommandAnnotation.usageMessage,  // 使用方法的提示消息
            singleCommandAnnotation.permission,  // 执行命令所需权限
            singleCommandAnnotation.permissionMessage,  // 权限不足时的提示消息
            instance as? CommandExecutor,  // 命令执行器
            instance as? TabCompleter  // Tab 补全器
        )
    }


    /**
     * 解析单个命令实例，生成对应的抽象命令对象。
     *
     * @param singleInstance 单个命令实例，应为实现了 [CommandExecutor] 和 [TabCompleter] 接口的对象。
     * @return 抽象命令对象，如果无法解析则返回 null。
     */
    @JvmStatic
    fun parseSingleCommand(singleInstance: Any): AbstractCommand? {
        return parseSingleCommand(singleInstance::class.java, singleInstance)
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
        val parseAnnotation = parseAnnotation(commandClass.java) ?: return false
        return registerCommand(parseAnnotation, pluginInstance)
    }
}