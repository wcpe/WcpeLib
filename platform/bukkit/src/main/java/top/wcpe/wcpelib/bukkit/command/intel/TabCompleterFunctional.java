package top.wcpe.wcpelib.bukkit.command.intel;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * tab补全
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:07
 * @deprecated This class is deprecated. Use the {@link top.wcpe.wcpelib.common.command.v2.TabCompleter}.
 */
@Deprecated
@FunctionalInterface
public interface TabCompleterFunctional {
    List<String> onTabComplete(CommandSender sender, String[] args);
}
