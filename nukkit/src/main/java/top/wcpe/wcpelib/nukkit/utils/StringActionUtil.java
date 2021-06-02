package top.wcpe.wcpelib.nukkit.utils;


import cn.nukkit.Player;
import cn.nukkit.Server;

import java.util.List;

public class StringActionUtil {
    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [OP]/say 我用OP权限执行了say
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     *
     * @param commands
     * @param p
     */
    public static void executionCommands(List<String> commands, Player p) {
        for (String command : commands) {
            executionCommands(command, p);
        }
    }

    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [OP]/say 我用OP权限执行了say
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     *
     * @param command
     * @param p
     */
    public static void executionCommands(String command, Player p) {
        if (p != null)
            command = command.replace("%player%", p.getName());
        if (command.startsWith("[CMD]")) {
            Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.substring(5));
            return;
        }
        if (p != null)
            if (command.startsWith("[OP]")) {
                boolean isOp = p.isOp();
                try {
                    p.setOp(true);
                    p.chat(command.substring(4));
                } catch (Exception e) {
                } finally {
                    p.setOp(isOp);
                }
                return;
            }
        if (p != null)
            if (command.startsWith("[CHAT]")) {
                p.chat(command.substring(6));
                return;
            }
        if (p != null)
            if (command.startsWith("[TITLE]")) {
                String[] split = command.substring(7).split(";");
                p.sendTitle(split[3], split[4], Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                return;
            }
        if (p != null)
            if (command.startsWith("[ACTION]")) {
                p.sendActionBar(command.substring(8));
                return;
            }
        if (command.startsWith("[BD]")) {
            Server.getInstance().broadcastMessage(command.substring(4));
            return;
        }
        if (p != null)
            p.sendMessage(command);
    }
}
