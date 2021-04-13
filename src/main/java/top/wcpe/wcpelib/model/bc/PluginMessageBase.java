package top.wcpe.wcpelib.model.bc;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import top.wcpe.wcpelib.WcpeLib;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

public class PluginMessageBase implements PluginMessageListener {
    public final static HashMap<String, PluginMessageTask> PluginMessageTaskList = new HashMap<>();

    public static void addSubCommand(PluginMessageTask pluginMessageTask) {
        PluginMessageTaskList.put(pluginMessageTask.key(), pluginMessageTask);
    }

    @Override
    public void onPluginMessageReceived(String tag, Player player, byte[] data) {
        if (!tag.equals("BungeeCord"))
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(data);
        String channel = in.readUTF();
        if ("WcpeLib".equals(channel)) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);
            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                Long time = Long.valueOf(msgin.readLong());
                if (time.longValue() < System.currentTimeMillis() - 5000L)
                    return;
                String key = msgin.readUTF();
                PluginMessageTask pluginMessageTask = PluginMessageTaskList.get(key);
                if (pluginMessageTask == null) return;
                String servername = msgin.readUTF();
                if (WcpeLib.getServerName().equals(servername)) {
                    int argsNum = msgin.readInt();
                    String[] args = new String[argsNum];
                    for (int i = 0; i < argsNum; i++) {
                        args[i] = msgin.readUTF();
                    }
                    pluginMessageTask.ReceiveRunTask(args);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
