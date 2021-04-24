package top.wcpe.wcpelib.model.bukkit.command.intel;

import java.util.List;

import org.bukkit.command.CommandSender;

/**
 * tab补全
 * 
 * @author WCPE
 * @date 2021年4月23日 下午4:48:07
 */
@FunctionalInterface
public interface TabCompleterFunctional {
	List<String> onTabComplete(CommandSender sender, String[] args);
}
