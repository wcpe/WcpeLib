package top.wcpe.wcpelib.bukkit.command;

import top.wcpe.wcpelib.common.command.v2.AbstractCommand;

/**
 * Command管理类
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:26
 * @deprecated This class is deprecated. Use the {@link top.wcpe.wcpelib.common.PlatformAdapter#registerCommand(AbstractCommand, Object)}.
 */
@Deprecated
public class CommandManager {
    public static void registerCommandPlus(CommandPlus commandPlus) {
        top.wcpe.wcpelib.bukkit.command.v2.CommandManager.registerBukkitCommand(commandPlus);
    }
}
