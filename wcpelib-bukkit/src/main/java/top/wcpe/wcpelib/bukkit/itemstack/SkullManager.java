package top.wcpe.wcpelib.bukkit.itemstack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import top.wcpe.wcpelib.bukkit.WcpeLib;
import top.wcpe.wcpelib.bukkit.version.VersionInfo;
import top.wcpe.wcpelib.bukkit.version.VersionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 玩家头颅
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-12 10:57
 */
public class SkullManager {
    public static AtomicInteger limitTime = new AtomicInteger(0);
    private static final HashMap<String, String[]> playerSkin = new HashMap<>();

    static {
        Bukkit.getScheduler().runTaskTimerAsynchronously(WcpeLib.getInstance(), () -> limitTime.set(0), 12000L, 12000L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(WcpeLib.getInstance(), () -> {
            try {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    savePlayerSkullSkin(p);
                }
            } catch (Exception exception) {
            }
        }, (20L * WcpeLib.getInstance().getConfig().getInt("skull-save-time")), (20L * WcpeLib.getInstance().getConfig().getInt("skull-save-time")));
    }

    static void savePlayerSkullSkin(Player p) {
        String[] playerTexture;
        try {
            Object entityPlayer = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
            Method getProfileMethod = entityPlayer.getClass().getMethod("getProfile");
            GameProfile gameProfile = (GameProfile) getProfileMethod.invoke(entityPlayer, new Object[0]);
            Property property = gameProfile.getProperties().get("textures").iterator().next();
            playerTexture = new String[]{property.getSignature(), property.getValue()};
        } catch (Exception e) {
            playerTexture = new String[]{"K9P4tCIENYbNpDuEuuY0shs1x7iIvwXi4jUUVsATJfwsAIZGS+9OZ5T2HB0tWBoxRvZNi73Vr+syRdvTLUWPusVXIg+2fhXmQoaNEtnQvQVGQpjdQP0TkZtYG8PbvRxE6Z75ddq+DVx/65OSNHLWIB/D+Rg4vINh4ukXNYttn9QvauDHh1aW7/IkIb1Bc0tLcQyqxZQ3mdglxJfgIerqnlA++Lt7TxaLdag4y1NhdZyd3OhklF5B0+B9zw/qP8QCzsZU7VzJIcds1+wDWKiMUO7+60OSrIwgE9FPamxOQDFoDvz5BOULQEeNx7iFMB+eBYsapCXpZx0zf1bduppBUbbVC9wVhto/J4tc0iNyUq06/esHUUB5MHzdJ0Y6IZJAD/xIw15OLCUH2ntvs8V9/cy5/n8u3JqPUM2zhUGeQ2p9FubUGk4Q928L56l3omRpKV+5QYTrvF+AxFkuj2hcfGQG3VE2iYZO6omXe7nRPpbJlHkMKhE8Xvd1HP4PKpgivSkHBoZ92QEUAmRzZydJkp8CNomQrZJf+MtPiNsl/Q5RQM+8CQThg3+4uWptUfP5dDFWOgTnMdA0nIODyrjpp+bvIJnsohraIKJ7ZDnj4tIp4ObTNKDFC/8j8JHz4VCrtr45mbnzvB2DcK8EIB3JYT7ElJTHnc5BKMyLy5SKzuw=", "eyJ0aW1lc3RhbXAiOjE1MjkyNTg0MTE4NDksInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzRlZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMxYzc3Y2U4ZTU0OTI1YWI1ODEyNTQ0NmVjNTNiMGNkZDNkMGNhM2RiMjczZWI5MDhkNTQ4Mjc4N2VmNDAxNiJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc2N2Q0ODMyNWVhNTMyNDU2MTQwNmI4YzgyYWJiZDRlMjc1NWYxMTE1M2NkODVhYjA1NDVjYzIifX19"};
            if (limitTime.get() >= 400) {
                return;

            }
            String value = sendGetMojangApi(p.getName());
            limitTime.addAndGet(1);
            if (value == null) {
                return;
            }
            playerTexture[1] = value;
        }
        playerSkin.put(p.getName(), playerTexture);
    }

    public static String sendGetMojangApi(String playerName) {
        try {
            String result = sendGet("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(result).getAsJsonObject();
            String uid = jsonObject.getAsJsonPrimitive("id").getAsString();
            String signature = sendGet("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            jsonObject = parser.parse(signature).getAsJsonObject();
            jsonObject = jsonObject.getAsJsonArray("properties").get(0).getAsJsonObject();
            String value = jsonObject.getAsJsonPrimitive("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            jsonObject = parser.parse(decoded).getAsJsonObject();
            String skinURL = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").getAsJsonPrimitive("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception exception) {
            return null;
        }
    }

    public static String sendGet(String urlStr) {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception exception) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException iOException) {
            }
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException iOException) {
            }
        }
        return sb.toString();
    }


    public static ItemStack getSkullItemStack() {
        ItemStack skull;
        if (VersionManager.getVersionInfo().getVersionNumber() > VersionInfo.V1_12_2.getVersionNumber()) {
            skull = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            skull = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
        }
        return skull;
    }

    public static ItemStack getSkullItemStack(String playerName) {
        String[] pSk = playerSkin.get(playerName);
        if (pSk == null) {
            return getSkullItemStack();
        }
        String signature = pSk[0];
        String value = pSk[1];
        ItemStack is = getSkullItemStack();
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        try {
            Field profileField = sm.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(sm, gameProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        is.setItemMeta(sm);
        return is;

    }

}
