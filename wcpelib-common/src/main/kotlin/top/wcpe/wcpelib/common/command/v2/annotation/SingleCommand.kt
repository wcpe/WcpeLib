package top.wcpe.wcpelib.common.command.v2.annotation

/**
 * 由 WCPE 在 2023/7/27 22:04 创建
 * <p>
 * Created by WCPE on 2023/7/27 22:04
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class SingleCommand(
    val name: String,
    val description: String = "",
    val aliases: Array<String> = [],
    val arguments: Array<Argument> = [],
    val playerOnly: Boolean = false,
    val playerOnlyMessage: String = "",
    val usageMessage: String = "",
    val permission: String = "",
    val permissionMessage: String = ""
)