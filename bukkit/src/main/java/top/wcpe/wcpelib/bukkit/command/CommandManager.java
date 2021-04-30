package top.wcpe.wcpelib.bukkit.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import lombok.Getter;

/**
 * Command管理类
 * 
 * @author WCPE
 * @date 2021年4月23日 下午4:48:26
 */
@SuppressWarnings("all")
public class CommandManager {
	@Getter
	private static SimpleCommandMap bukkitSimpleCommandMap;
	@Getter
	private static HashMap<String, Command> bukkitCommandMap;
	static {
		final Class<?> c = Bukkit.getServer().getClass();
		try {
			bukkitSimpleCommandMap = (SimpleCommandMap) c.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer(),
					null);
			Field knownCommandsField = bukkitSimpleCommandMap.getClass().getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			bukkitCommandMap = (HashMap<String, Command>) knownCommandsField.get(bukkitSimpleCommandMap);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Getter
	private static HashMap<String, CommandPlus> commandPlusMap = new HashMap<>();

	public static void registerCommandPlus(CommandPlus commandPlus) {
		bukkitCommandMap.put(commandPlus.getName().toLowerCase(), commandPlus);
		bukkitCommandMap.put(commandPlus.getPlugin().getName().toLowerCase() + ":" + commandPlus.getName(), commandPlus);
		for (String s : commandPlus.getAliases()) {
			bukkitCommandMap.put(s, commandPlus);
			bukkitCommandMap.put(commandPlus.getPlugin().getName().toLowerCase() + ":" + s, commandPlus);
		}
		commandPlusMap.put(commandPlus.getName(), commandPlus);
		commandPlus.register(bukkitSimpleCommandMap);
	}

}
