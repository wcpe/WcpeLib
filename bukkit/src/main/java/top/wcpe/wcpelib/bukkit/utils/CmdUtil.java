package top.wcpe.wcpelib.bukkit.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdUtil {
    /**
     * 玩家执行指令
     *
     * @param a
     * @param hasPapi
     * @param p
     * @author WCPE
     * @date 2021年4月22日 下午5:20:00
     */
    public static void executionCommands(List<String> a, boolean hasPapi, Player p) {
        for (String commands : a) {
            executionCommands(commands, hasPapi, p);
        }
    }

    private static String setPlaceholders(Player p, String text) {
        try {
            Class<?> forName = Class.forName("PlaceholderAPI");
            Object newInstance = forName.newInstance();
            text = (String) forName.getMethod("setPlaceholders", Player.class, String.class).invoke(newInstance, p,
                    text);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 玩家执行指令
     *
     * @param commands
     * @param hasPapi
     * @param p
     * @author WCPE
     * @date 2021年4月22日 下午5:20:21
     */
    public static void executionCommands(String commands, boolean hasPapi, Player p) {
        String pd[] = commands.split("]");
        if (pd[0].equals("[CMD")) {
            if (pd[0].equals("[CMD")) {
                pd[1] = pd[1].replaceAll("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), pd[1]);
            }
        } else if (pd[0].equals("[CHAT")) {
            if (pd[0].equals("[CHAT")) {
                if (hasPapi) {
                    pd[1] = setPlaceholders(p, pd[1]);
                }
                pd[1] = pd[1].replaceAll("%player%", p.getName());
                p.chat(pd[1]);
            }
        } else if (pd[0].equals("[TITLE")) {
            if (pd[0].equals("[TITLE")) {
                if (hasPapi) {
                    pd[1] = setPlaceholders(p, pd[1]);
                    pd[2] = setPlaceholders(p, pd[2]);
                }
                pd[1] = pd[1].replaceAll("%player%", p.getName());
                pd[2] = pd[2].replaceAll("%player%", p.getName());
                p.sendTitle(pd[1], pd[2], Integer.valueOf(pd[3]), Integer.valueOf(pd[4]), Integer.valueOf(pd[5]));

            }
        } else if (pd[0].equals("[ACTION")) {
            if (pd[0].equals("[ACTION")) {
                if (hasPapi) {
                    pd[1] = setPlaceholders(p, pd[1]);
                }
                pd[1] = pd[1].replaceAll("%player%", p.getName());
                NmsUtil.sendAction(p, pd[1]);
            }
        } else if (pd[0].equals("[OP")) {
            if (pd[0].equals("[OP")) {
                boolean isop = p.isOp();
                try {
                    p.setOp(true);
                    if (hasPapi) {
                        pd[1] = setPlaceholders(p.getPlayer(), pd[1]);
                    }
                    pd[1] = pd[1].replaceAll("%player%", p.getName());
                    p.chat(pd[1]);
                } catch (Exception eee) {
                } finally {
                    p.setOp(isop);
                }

            }
        } else if (pd[0].equals("[Bd")) {
            if (hasPapi) {
                pd[1] = setPlaceholders(p, pd[1]);
            }
            pd[1] = pd[1].replaceAll("%player%", p.getName());
            Bukkit.broadcastMessage(pd[1]);
        } else {
            if (hasPapi) {
                pd[0] = setPlaceholders(p, pd[0]);
            }
            pd[0] = pd[0].replaceAll("%player%", p.getName());
            p.sendMessage(pd[0]);
        }
    }

}
