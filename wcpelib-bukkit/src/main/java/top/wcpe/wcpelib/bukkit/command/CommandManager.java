package top.wcpe.wcpelib.bukkit.command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import top.wcpe.wcpelib.bukkit.WcpeLib;
import top.wcpe.wcpelib.common.command.v2.AbstractCommand;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Command管理类
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:26
 * @deprecated This class is deprecated. Use the {@link top.wcpe.wcpelib.common.PlatformAdapter#registerCommand(AbstractCommand, Object)}.
 */
@Deprecated
public class CommandManager {
    @Getter
    private static final HashMap<String, CommandPlus> commandPlusMap = new HashMap<>();
    @Getter
    private static SimpleCommandMap bukkitSimpleCommandMap;
    @Getter
    private static HashMap<String, Command> bukkitCommandMap;

    static {
        final Class<?> c = Bukkit.getServer().getClass();
        try {
            bukkitSimpleCommandMap = (SimpleCommandMap) c.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer(), new Object[]{});
            Field knownCommandsField = getField(bukkitSimpleCommandMap.getClass(), "knownCommands");
            knownCommandsField.setAccessible(true);
            bukkitCommandMap = (HashMap<String, Command>) knownCommandsField.get(bukkitSimpleCommandMap);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException |
                 SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }

    public static void registerCommandPlus(CommandPlus commandPlus) {
        top.wcpe.wcpelib.bukkit.command.v2.CommandManager.registerBukkitCommand(commandPlus, WcpeLib.getInstance());
    }

}
