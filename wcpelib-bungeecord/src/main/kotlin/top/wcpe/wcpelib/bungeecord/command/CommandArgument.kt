package top.wcpe.wcpelib.bungeecord.command

import top.wcpe.wcpelib.common.command.CommandArgument

/**
 * 由 WCPE 在 2022/9/27 22:31 创建
 *
 * Created by WCPE on 2022/9/27 22:31
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.1.0-alpha-dev-4
 */
@Deprecated("replace common command v2")
class CommandArgument private constructor(
    override val name: String,
    override val describe: String,
    override val ignoreArg: String?
) : CommandArgument {

    class Builder(private val name: String) {
        private var describe: String = ""
        private var ignoreArg: String? = null

        fun describe(describe: String): Builder {
            this.describe = describe
            return this
        }

        fun ignoreArg(ignoreArg: String): Builder {
            this.ignoreArg = ignoreArg
            return this
        }

        fun build(): CommandArgument {
            return CommandArgument(name, describe, ignoreArg)
        }
    }
}