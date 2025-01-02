package top.wcpe.common.command.v2

import top.wcpe.wcpelib.common.command.v2.Argument
import kotlin.math.max

/**
 * 由 WCPE 在 2023/7/23 10:50 创建
 * <p>
 * Created by WCPE on 2023/7/23 10:50
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.0-alpha-dev-1
 */

private fun requiredArgs(
    argsStrings: Array<out String?>,
    arguments: List<Argument>
): List<Argument> {
    val result = mutableListOf<Argument>()
    for ((i, value) in arguments.withIndex()) {
        if (value.required && argsStrings[i] == null) {
            result.add(value)
        }
    }
    return result
}


fun main() {
    val args = arrayOf("param1", "2a3", "\\.", ".")
    val arguments = listOf(
        Argument("param1", true),
        Argument("param2", true),
        Argument("param3", false),
        Argument("param4", true)
    )

    val argsStrings = args.copyOf(max(args.size, arguments.size))
    val requiredArgResult = requiredArgs(argsStrings, arguments)
    if (requiredArgResult.isNotEmpty()) {
        println("未填写的参数:")
        requiredArgResult.forEach {
            println(it)
        }

        return
    }


    for ((i, argument) in arguments.withIndex()) {
        if (argument.required) {
            continue
        }
        val arg = argsStrings[i]
        if (arg == null || arg == " " || arg == ".") {
            argsStrings[i] = null
        }
    }


    println("校验后的参数列表:")
    argsStrings.forEach { arg ->
        println(arg)
    }
}
