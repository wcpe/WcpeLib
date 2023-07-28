package top.wcpe.wcpelib.common.command.v2.annotation


/**
 * 由 WCPE 在 2023/7/26 15:01 创建
 * <p>
 * Created by WCPE on 2023/7/26 15:01
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class Argument(
    val name: String, val required: Boolean = true, val description: String = ""
)