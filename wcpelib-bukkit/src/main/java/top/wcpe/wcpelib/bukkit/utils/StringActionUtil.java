package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
     * @param stringActionList
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player
     */
    public static void executionCommands(List<String> stringActionList, boolean parserPlaceholderApi, Player player) {
        for (String command : stringActionList) {
            executionCommands(command, parserPlaceholderApi, player);
        }
    }

    private static String setPlaceholders(Player p, String text) {
        try {
            Class<?> forName = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            text = (String) forName.getMethod("setPlaceholders", Player.class, String.class).invoke(null, p,
                    text);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
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
     * @param stringAction
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player
     */
    public static void executionCommands(String stringAction, boolean parserPlaceholderApi, Player player) {
        if (player != null)
            stringAction = stringAction.replace("%player%", player.getName());
        if (stringAction.startsWith("[CMD]")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), stringAction.substring(5));
            return;
        }
        if (stringAction.startsWith("[BD]")) {
            Bukkit.broadcastMessage(parserPlaceholderApi ? setPlaceholders(player, stringAction.substring(4)) : stringAction.substring(4));
            return;
        }
        if (player != null) {
            if (stringAction.startsWith("[OP]")) {
                boolean isOp = player.isOp();
                try {
                    player.setOp(true);
                    player.chat(parserPlaceholderApi ? setPlaceholders(player, stringAction.substring(4)) : stringAction.substring(4));
                } catch (Exception e) {
                } finally {
                    player.setOp(isOp);
                }
                return;
            }

            if (stringAction.startsWith("[PLAYER]")) {
                Bukkit.dispatchCommand(player, stringAction.substring(8));
                return;
            }

            if (stringAction.startsWith("[CHAT]")) {
                player.chat(parserPlaceholderApi ? setPlaceholders(player, stringAction.substring(6)) : stringAction.substring(6));
                return;
            }

            if (stringAction.startsWith("[TITLE]")) {
                String[] split = stringAction.substring(7).split(";");
                player.sendTitle(parserPlaceholderApi ? setPlaceholders(player, split[3]) : split[3], parserPlaceholderApi ? setPlaceholders(player, split[4]) : split[4], Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                return;
            }

            if (stringAction.startsWith("[ACTION]")) {
                NetMinecraftServerUtil.sendAction(player, parserPlaceholderApi ? setPlaceholders(player, stringAction.substring(8)) : stringAction.substring(8));
                return;
            }

            player.sendMessage(parserPlaceholderApi ? setPlaceholders(player, stringAction) : stringAction);
        }
    }
}
