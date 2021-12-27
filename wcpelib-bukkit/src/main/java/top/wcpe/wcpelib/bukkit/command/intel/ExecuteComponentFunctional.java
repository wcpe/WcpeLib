package top.wcpe.wcpelib.bukkit.command.intel;

import org.bukkit.command.CommandSender;
/**
 * 命令执行器
 * @author WCPE
 * @date 2021年4月24日 下午3:36:51
 */
@FunctionalInterface
public interface ExecuteComponentFunctional {
	public void execute(CommandSender sender, String[] args);
}
