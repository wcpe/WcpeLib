package top.wcpe.wcpelib.nukkit.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.SimpleCommandMap;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Command管理类
 * 
 * @author WCPE
 * @date 2021年4月23日 下午4:48:26
 */
@SuppressWarnings("all")
public class CommandManager {
	@Getter
	private static SimpleCommandMap nukkitSimpleCommandMap;
	@Getter
	private static Map<String, Command> nukkitCommandMap = new HashMap<>();
	static {
		nukkitSimpleCommandMap = Server.getInstance().getCommandMap();
		try {
			Field knownCommandsField = nukkitSimpleCommandMap.getClass().getDeclaredField("knownCommands");
			knownCommandsField.setAccessible(true);
			nukkitCommandMap = (Map<String, Command>) knownCommandsField.get(nukkitSimpleCommandMap);
		} catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Getter
	private static HashMap<String, CommandPlus> commandPlusMap = new HashMap<>();

	public static void registerCommandPlus(CommandPlus commandPlus) {
		nukkitCommandMap.put(commandPlus.getName().toLowerCase(), commandPlus);
		nukkitCommandMap.put(commandPlus.getPlugin().getName().toLowerCase() + ":" + commandPlus.getName(), commandPlus);
		for (String s : commandPlus.getAliases()) {
			nukkitCommandMap.put(s, commandPlus);
			nukkitCommandMap.put(commandPlus.getPlugin().getName().toLowerCase() + ":" + s, commandPlus);
		}
		commandPlusMap.put(commandPlus.getName(), commandPlus);
		commandPlus.register(nukkitSimpleCommandMap);
	}

}
