package top.wcpe.common.command.v2.annotation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import top.wcpe.wcpelib.common.command.v2.annotation.Argument
import top.wcpe.wcpelib.common.command.v2.annotation.SingleCommand

/**
 * 由 WCPE 在 2023/7/28 9:21 创建
 * <p>
 * Created by WCPE on 2023/7/28 9:21
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.2.1
 */
class SingleCommandTest {
    @Test
    fun `test name`() {
        val singleCommand = SingleCommand(name = "command")
        assertEquals("command", singleCommand.name)
    }
    @Test
    fun `test description`() {
        val singleCommand = SingleCommand(name = "command", description = "Command description")
        assertEquals("Command description", singleCommand.description)
    }
    @Test
    fun `test aliases`() {
        val singleCommand = SingleCommand(name = "command", aliases = arrayOf("alias1", "alias2"))
        assertArrayEquals(arrayOf("alias1", "alias2"), singleCommand.aliases)
    }
    @Test
    fun `test arguments`() {
        val argument1 = Argument(name = "arg1", description = "Argument 1")
        val argument2 = Argument(name = "arg2", description = "Argument 2")
        val singleCommand = SingleCommand(name = "command", arguments = arrayOf(argument1, argument2))
        assertArrayEquals(arrayOf(argument1, argument2), singleCommand.arguments)
    }
    @Test
    fun `test playerOnly`() {
        val singleCommand = SingleCommand(name = "command", playerOnly = true)
        assertTrue(singleCommand.playerOnly)
    }
    @Test
    fun `test playerOnlyMessage`() {
        val singleCommand = SingleCommand(name = "command", playerOnlyMessage = "This command can only be executed by players")
        assertEquals("This command can only be executed by players", singleCommand.playerOnlyMessage)
    }
    @Test
    fun `test usageMessage`() {
        val singleCommand = SingleCommand(name = "command", usageMessage = "Usage: /command <arg>")
        assertEquals("Usage: /command <arg>", singleCommand.usageMessage)
    }
    @Test
    fun `test permission`() {
        val singleCommand = SingleCommand(name = "command", permission = "myplugin.command")
        assertEquals("myplugin.command", singleCommand.permission)
    }
    @Test
    fun `test permissionMessage`() {
        val singleCommand = SingleCommand(name = "command", permissionMessage = "You don't have permission to execute this command")
        assertEquals("You don't have permission to execute this command", singleCommand.permissionMessage)
    }
}