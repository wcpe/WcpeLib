package top.wcpe.wcpelib.model.bc.utils;

import org.bukkit.entity.Player;
import top.wcpe.wcpelib.WcpeLib;
import top.wcpe.wcpelib.model.bc.impl.SendMessageToPlayerTask;
import top.wcpe.wcpelib.model.bc.impl.SendServerTpLocationTask;
import top.wcpe.wcpelib.model.bc.impl.SendTempMessageToPlayerTask;
import top.wcpe.wcpelib.model.mybatis.utils.PlayerServerUtil;
import top.wcpe.wcpelib.model.bc.PluginMessageTask;
import top.wcpe.wcpelib.model.bukkit.utils.SerializeClassUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerUtil {
    /**
     * 将玩家传送至指定服务器
     *
     * @param p          玩家对象
     * @param serverName 目标服务器
     */
    public static void tpServer(Player p, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException iOException) {
        }
        p.sendPluginMessage(WcpeLib.getInstance(), "BungeeCord", b.toByteArray());
    }

    private static PluginMessageTask sendTempMessageToPlayerTask = new SendTempMessageToPlayerTask().register();


    public static boolean sendTempMessageToPlayer(String name, String msg) {
        if (PlayerServerUtil.getPlayerServer(name) != null) {
            sendTempMessageToPlayer(name, PlayerServerUtil.getPlayerServer(name), msg);
            return true;
        }
        return false;

    }

    public static void sendTempMessageToPlayer(String serverName, String name, String msg) {
        sendTempMessageToPlayerTask.sendTaskToServer(serverName, name, msg);
    }

    private static PluginMessageTask sendMessageToPlayerTask = new SendMessageToPlayerTask().register();

    public static void sendMessageToPlayerOrElseAddMessage(String name, String msg) {
        sendMessageToPlayerOrElseAddMessage(PlayerServerUtil.getPlayerServer(name), name, msg);
    }

    public static void sendMessageToPlayerOrElseAddMessage(String serverName, String name, String msg) {
        sendMessageToPlayerTask.sendTaskToServer(serverName, name, msg);
    }


    private static PluginMessageTask sendServerTpLocation = new SendServerTpLocationTask().register();

    public static void tpServerLocation(String name, String servrName, String worldName, Double x, Double y, Double z) {
        sendServerTpLocation.sendTaskToServer(servrName,name, SerializeClassUtil.joining(";", worldName, String.valueOf(x), String.valueOf(y), String.valueOf(z)));
    }


}
