package top.wcpe.wcpelib.common.command

/**
 * 由 WCPE 在 2022/9/6 19:23 创建
 *
 * Created by WCPE on 2022/9/6 19:23
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-3
 */
interface CommandArgument {
    val name: String
    val describe: String
    val ignoreArg: String?
}