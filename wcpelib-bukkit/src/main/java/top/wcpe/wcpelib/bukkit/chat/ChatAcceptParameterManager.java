package top.wcpe.wcpelib.bukkit.chat;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import top.wcpe.wcpelib.bukkit.WcpeLib;

import java.util.HashMap;

/**
 * 聊天接受参数
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-02 17:06
 */
public class ChatAcceptParameterManager {

    private static HashMap<String, ChatAcceptParameterTask> playerTask = new HashMap<>();

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void playerChat(AsyncPlayerChatEvent e) {
                ChatAcceptParameterTask chatAcceptParameterTask = playerTask.get(e.getPlayer().getName());
                if (chatAcceptParameterTask == null) return;
                if (chatAcceptParameterTask.getTimeStamp() < System.currentTimeMillis()) {
                    playerTask.remove(e.getPlayer().getName());
                    return;
                }
                e.setCancelled(true);
                if (chatAcceptParameterTask.getCancelString().equals(e.getMessage())) {
                    e.getPlayer().sendMessage(chatAcceptParameterTask.getCancelTipString());
                    playerTask.remove(e.getPlayer().getName());
                    return;
                }
                if (chatAcceptParameterTask.getJudge().judge(e.getMessage())) {
                    chatAcceptParameterTask.getJudgeSuccessTask().run(e.getPlayer(), e.getMessage());
                    playerTask.remove(e.getPlayer().getName());
                    return;
                }
                chatAcceptParameterTask.getJudgeFailTask().run(e.getPlayer(), e.getMessage());
                e.getPlayer().sendMessage(chatAcceptParameterTask.getTipString());

            }
        }, WcpeLib.getInstance());

    }

    public static void putChatAcceptParameterTask(Player p, long time, String tipString, String cancelString, String cancelTipString, ChatAcceptParameterReturnJudge judge, ChatAcceptParameterJudgeSuccessTask judgeSuccessTask, ChatAcceptParameterJudgeFailTask judgeFailTask) {
        p.sendMessage(tipString);
        playerTask.put(p.getName(), new ChatAcceptParameterTask(System.currentTimeMillis() + time * 1000, tipString, cancelString, cancelTipString, judge, judgeSuccessTask, judgeFailTask));
    }

    @Data
    private static class ChatAcceptParameterTask {
        public ChatAcceptParameterTask(long timeStamp, String tipString, String cancelString, String cancelTipString, ChatAcceptParameterReturnJudge judge, ChatAcceptParameterJudgeSuccessTask judgeSuccessTask, ChatAcceptParameterJudgeFailTask judgeFailTask) {
            this.timeStamp = timeStamp;
            this.tipString = tipString;
            this.cancelString = cancelString;
            this.cancelTipString = cancelTipString;
            this.judge = judge;
            this.judgeSuccessTask = judgeSuccessTask;
            this.judgeFailTask = judgeFailTask;
        }

        private long timeStamp;
        private String tipString;
        private String cancelString;
        private String cancelTipString;
        private ChatAcceptParameterReturnJudge judge;
        private ChatAcceptParameterJudgeSuccessTask judgeSuccessTask;
        private ChatAcceptParameterJudgeFailTask judgeFailTask;
    }


    @FunctionalInterface
    public interface ChatAcceptParameterReturnJudge {
        boolean judge(String message);
    }

    @FunctionalInterface
    public interface ChatAcceptParameterJudgeSuccessTask {
        void run(Player p, String message);
    }

    @FunctionalInterface
    public interface ChatAcceptParameterJudgeFailTask {
        void run(Player p, String message);
    }
}
