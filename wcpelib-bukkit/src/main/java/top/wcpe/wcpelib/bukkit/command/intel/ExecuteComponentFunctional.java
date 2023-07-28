package top.wcpe.wcpelib.bukkit.command.intel;

import org.bukkit.command.CommandSender;

/**
 * 命令执行器
 *
 * @author WCPE
 * @date 2021年4月24日 下午3:36:51
 * @deprecated This class is deprecated. Use the {@link top.wcpe.wcpelib.common.command.v2.CommandExecutor}.
 */
@Deprecated
@FunctionalInterface
public interface ExecuteComponentFunctional {
    void execute(CommandSender sender, String[] args);
}
