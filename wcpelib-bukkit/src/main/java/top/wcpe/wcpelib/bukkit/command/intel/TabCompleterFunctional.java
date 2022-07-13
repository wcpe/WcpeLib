package top.wcpe.wcpelib.bukkit.command.intel;

import org.bukkit.command.CommandSender;

import java.util.List;

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
