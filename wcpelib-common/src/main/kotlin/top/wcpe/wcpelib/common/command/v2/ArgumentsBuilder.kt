package top.wcpe.wcpelib.common.command.v2

/**
 * 由 WCPE 在 2023/7/23 14:16 创建
 * <p>
 * Created by WCPE on 2023/7/23 14:16
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@JvmInline
value class ArgumentsBuilder(
    val arguments: MutableList<Argument> = mutableListOf()
) {
    fun argument(name: String) {
        argument(name, true)
    }

    fun argument(name: String, required: Boolean) {
        arguments.add(Argument(name, required))
    }
}