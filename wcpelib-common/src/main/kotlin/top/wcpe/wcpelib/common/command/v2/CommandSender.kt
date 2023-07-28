package top.wcpe.wcpelib.common.command.v2

/**
 * 由 WCPE 在 2023/7/21 13:57 创建
 * <p>
 * Created by WCPE on 2023/7/21 13:57
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
interface CommandSender<T> {
    fun getCommandSender(): T

    /**
     * 获取名称
     */
    fun getName(): String

    /**
     * 是否为玩家
     */
    fun isPlayer(): Boolean

    /**
     * 发送消息
     */
    fun sendMessage(message: String)

    /**
     * 判断权限
     */
    fun hasPermission(permission: String): Boolean
}