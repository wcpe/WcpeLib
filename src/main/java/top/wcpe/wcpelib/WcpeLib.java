package top.wcpe.wcpelib;

import com.onarandombox.MultiverseCore.MultiverseCore;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import top.wcpe.wcpelib.model.mybatis.Mybatis;
import top.wcpe.wcpelib.model.mybatis.TypeHandlerRegistry;
import top.wcpe.wcpelib.model.mybatis.mapper.MapListMapper;
import top.wcpe.wcpelib.model.mybatis.mapper.PlayerServerMapper;
import top.wcpe.wcpelib.model.mybatis.mapper.WorldAliasMapper;
import top.wcpe.wcpelib.model.mybatis.utils.MapListUtil;
import top.wcpe.wcpelib.model.bc.PluginMessageBase;
import top.wcpe.wcpelib.model.bc.utils.ServerUtil;
import top.wcpe.wcpelib.model.bukkit.utils.NmsUtil;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述：WcpeLib 一个自己用的破烂Bukkit插件前置
 *
 * @Author: WCPE
 * @Date: 2021/2/17 3:45
 */
public final class WcpeLib extends JavaPlugin {
    public void log(String log) {
        getServer().getConsoleSender().sendMessage("§a[§e" + this.getName() + "§a]§r" + log);
    }

    @Getter
    private static Mybatis mybatis;

    @Getter
    private static WcpeLib instance;

    @Getter
    private static ServerUtil messageUtils;

    public static String getServerName() {
        return instance.getConfig().getString("ServerName");
    }

    @Getter
    public static boolean enableMysql;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        enableMysql = getConfig().getBoolean("Mysql.enable");
        this.mybatis = Mybatis.createMybatis();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageBase());


        messageUtils = new ServerUtil();
        log(" Enable！！！");
    }


    @Override
    public void onDisable() {
        log(" Disable！！！");
    }

    public static void addPlayerMessage(String name, String msg) {
        MapListUtil.getListAddValue("PlayerMessage", name, msg);
    }

    public static boolean delPlayerMessage(String name, String msg) {
        return MapListUtil.removeListElement("PlayerMessage", name, msg);
    }

    public static void sendMessage(Player p, String... value) {
        p.sendMessage(repString(value));
    }

    public static void sendMessage(String prefix, Player p, String... value) {
        p.sendMessage(prefix + repString(value));
    }

    public static void sendActionMessage(Player p, String... value) {
        NmsUtil.sendAction_1_15(p, repString(value));
    }

    public static void sendActionMessage(String prefix, Player p, String... value) {
        NmsUtil.sendAction_1_15(p, prefix + repString(value));
    }
    private static final String[] regxString = new String[] { "$", "(", ")", "*", "+", ".", "[", "]", "?", "\\", "/",
            "^" ,"{","}"};
    /**
     * 替换value[0]里的变量 %1 替换为 value[1] %x替换为 value[x]
     *
     * @param value value[0]为主要String 后面都是变量
     */
    public static String repString(String... value) {
        for (int i = 0; i < value.length; i++) {
            for (String s : regxString) {
                if (value[i].contains(s)) {
                    value[i] = value[i].replace(s, "\\" + s);
                }
            }
        }
        Pattern r = Pattern.compile("%\\d+");
        Matcher m = r.matcher(value[0]);
        while (m.find()) {
            String group = m.group();
            value[0] = value[0].replaceFirst(group, value[Integer.parseInt(group.substring(1, group.length()))]);
        }
        return value[0];
    }
}