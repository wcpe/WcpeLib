package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.wcpe.wcpelib.bukkit.tools.StringActionTool;

import java.util.List;

public class StringActionUtil {

    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [ACTION]快捷栏消息
     * [BD]服务器公告
     * [DELAY]5s
     *
     * @param stringActionList     执行的操作列表
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player               玩家
     */
    public static void executionCommands(@NotNull List<String> stringActionList, boolean parserPlaceholderApi, @Nullable Player player) {
        StringActionTool.INSTANCE.eval(stringActionList, parserPlaceholderApi, player);
    }

    /**
     * 解析执行操作 目前有以下操作
     * [CMD]say 我是后台执行的指令say
     * [PLAYER]say hello
     * [CHAT]哈喽 我说了一句话
     * [TITLE]10;70;20;主标题;副标题
     * [BD]服务器公告
     *
     * @param stringAction         执行的操作列表
     * @param parserPlaceholderApi 是否解析 PlaceholderApi 注册的变量
     * @param player               玩家
     */
    public static void executionCommands(@NotNull String stringAction, boolean parserPlaceholderApi, @Nullable Player player) {
        StringActionTool.INSTANCE.compatibleEval(stringAction, parserPlaceholderApi, player);
    }
}
