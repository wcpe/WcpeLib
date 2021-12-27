package top.wcpe.wcpelib.bukkit.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StringActionUtil {
    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [OP]/say 我用OP权限执行了say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     *
     * @param commands
     * @param hasPapi  是否解析Papi变量
     * @param p
     */
    public static void executionCommands(List<String> commands, boolean hasPapi, Player p) {
        for (String command : commands) {
            executionCommands(command, hasPapi, p);
        }
    }

    private static String setPlaceholders(Player p, String text) {
        try {
            Class<?> forName = Class.forName("PlaceholderAPI");
            Object newInstance = forName.newInstance();
            text = (String) forName.getMethod("setPlaceholders", Player.class, String.class).invoke(newInstance, p,
                    text);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [OP]/say 我用OP权限执行了say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     *
     * @param command
     * @param p
     */
    public static void executionCommands(String command, boolean hasPapi, Player p) {
        if (p != null)
            command = command.replace("%player%", p.getName());
        if (command.startsWith("[CMD]")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(5));
            return;
        }
        if (command.startsWith("[BD]")) {
            Bukkit.broadcastMessage(hasPapi ? setPlaceholders(p, command.substring(4)) : command.substring(4));
            return;
        }
        if (p != null) {
            if (command.startsWith("[OP]")) {
                boolean isOp = p.isOp();
                try {
                    p.setOp(true);
                    p.chat(hasPapi ? setPlaceholders(p, command.substring(4)) : command.substring(4));
                } catch (Exception e) {
                } finally {
                    p.setOp(isOp);
                }
                return;
            }

            if (command.startsWith("[PLAYER]")) {
                Bukkit.dispatchCommand(p, command.substring(8));
                return;
            }

            if (command.startsWith("[CHAT]")) {
                p.chat(hasPapi ? setPlaceholders(p, command.substring(6)) : command.substring(6));
                return;
            }

            if (command.startsWith("[TITLE]")) {
                String[] split = command.substring(7).split(";");
                p.sendTitle(hasPapi ? setPlaceholders(p, split[3]) : split[3], hasPapi ? setPlaceholders(p, split[4]) : split[4], Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                return;
            }

            if (command.startsWith("[ACTION]")) {
                NetMinecraftServerUtil.sendAction(p, hasPapi ? setPlaceholders(p, command.substring(8)) : command.substring(8));
                return;
            }

            p.sendMessage(hasPapi ? setPlaceholders(p, command) : command);
        }
    }
}
