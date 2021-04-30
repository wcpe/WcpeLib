package top.wcpe.wcpelib.bukkit.bc.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import top.wcpe.wcpelib.bukkit.WcpeLib;
import top.wcpe.wcpelib.bukkit.bc.PluginMessageTask;
import top.wcpe.wcpelib.bukkit.bc.utils.ServerUtil;
import top.wcpe.wcpelib.bukkit.utils.SerializeClassUtil;


public class SendServerTpLocationTask implements PluginMessageTask {
    @Override
    public String key() {
        return "sendServerTpLocation";
    }


    @Override
    public void ReceiveRunTask(String[] args) {
        Bukkit.getScheduler().runTaskLater(WcpeLib.getInstance(), () -> {
            Player p = Bukkit.getPlayerExact(args[0]);
            if (p == null)
                return;
            Location loc = SerializeClassUtil.stringToLocation(args[1]);
            if (loc == null) return;
            Bukkit.getScheduler().runTaskLater(WcpeLib.getInstance(), () -> {
                p.teleport(loc);
            }, 20);
        }, 20 * 2);
    }

    @Override
    public boolean sendBeforeTask(String serverName, String... args) {
        Player p = Bukkit.getPlayerExact(args[0]);
        if (p == null) return false;
        if (WcpeLib.getServerName().equals(serverName)) {
            Location location = SerializeClassUtil.stringToLocation(args[1]);
            if (location == null) return false;
            p.teleport(location);
            return false;
        }
        ServerUtil.tpServer(p, serverName);
        return true;
    }

    @Override
    public void sendAfterTask() {
    }
}
