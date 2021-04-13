package top.wcpe.wcpelib.model.bc.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import top.wcpe.wcpelib.WcpeLib;
import top.wcpe.wcpelib.model.bc.PluginMessageTask;


public class SendTempMessageToPlayerTask implements PluginMessageTask {
    @Override
    public String key() {
        return "sendTempMessageToPlayer";
    }

    @Override
    public void ReceiveRunTask(String[] args) {
        sendMessage(args[0],args[1]);
    }

    void sendMessage(String name, String msg) {
        Player playerExact = Bukkit.getPlayerExact(name);
        if (playerExact != null)
            WcpeLib.sendMessage(playerExact, msg);
    }

    @Override
    public boolean sendBeforeTask(String serverName, String... args) {
        if (serverName == null) {
            return false;
        }
        if (WcpeLib.getServerName().equals(serverName)) {
            sendMessage(args[0], args[1]);
            return false;
        }
        return true;
    }


    @Override
    public void sendAfterTask() {
    }
}
