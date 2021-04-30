package top.wcpe.wcpelib.bukkit.bc;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import top.wcpe.wcpelib.bukkit.WcpeLib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface PluginMessageTask {

    default PluginMessageTask register() {
        PluginMessageBase.addSubCommand(this);
        return this;
    }

    String key();

    void ReceiveRunTask(String[] args);

    default void sendTaskToServer(String serverName, String... args) {
        if (!sendBeforeTask(serverName, args)) return;
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("WcpeLib");
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeLong(System.currentTimeMillis());
            msgout.writeUTF(key());
            msgout.writeUTF(serverName);
            msgout.writeInt(args.length);
            for (String s : args) {
                msgout.writeUTF(s);
            }
            out.writeShort((msgbytes.toByteArray()).length);
            out.write(msgbytes.toByteArray());
            Bukkit.getServer().sendPluginMessage(WcpeLib.getInstance(), "BungeeCord", out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAfterTask();
    }

    boolean sendBeforeTask(String serverName, String... args);


    void sendAfterTask();
}