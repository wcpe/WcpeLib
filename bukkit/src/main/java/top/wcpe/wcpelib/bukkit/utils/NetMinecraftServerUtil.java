package top.wcpe.wcpelib.bukkit.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NetMinecraftServerUtil {
    public static Class<?> getNmsClass(String Name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server."
                + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + Name);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static int getServerVersionNum() {
        String[] split = Bukkit.getServer().getVersion().replaceAll(".{0,}\\(MC: ", "").replace(")", "").split("\\.");
        return Integer.parseInt(split[0] + split[1] + split[2]);
    }

    public static void sendAction(Player p, String msg) {
        if (getServerVersionNum() >= 1160)
            sendAction_1_16(p, msg);
        else
            sendAction_1_15(p, msg);
    }

    private static void sendAction_1_16(Player p, String msg) {
        try {
            Object icbc = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class})
                    .newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', msg)});

            Object cmt = getNmsClass("ChatMessageType").getField("GAME_INFO").get(null);
            Object ppoc = getNmsClass("PacketPlayOutChat")
                    .getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), getNmsClass("ChatMessageType"), UUID.class})
                    .newInstance(new Object[]{icbc, cmt, p.getUniqueId()});
            Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);

            Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
            pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon,
                    new Object[]{ppoc});
        } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void sendAction_1_15(Player p, String msg) {
        try {
            Object icbc = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class})
                    .newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', msg)});

            Object cmt = getNmsClass("ChatMessageType").getField("GAME_INFO").get(null);

            Object ppoc = getNmsClass("PacketPlayOutChat")
                    .getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), getNmsClass("ChatMessageType")})
                    .newInstance(new Object[]{icbc, cmt});
            Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);

            Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
            pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon,
                    new Object[]{ppoc});
        } catch (IllegalAccessException | InstantiationException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}