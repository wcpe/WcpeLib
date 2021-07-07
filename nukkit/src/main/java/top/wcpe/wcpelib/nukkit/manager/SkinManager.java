package top.wcpe.wcpelib.nukkit.manager;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 皮肤管理类
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-04-30 17:48
 */
public class SkinManager {

    @Getter
    private final Plugin plugin;

    public SkinManager(Plugin plugin) throws IOException {
        this.plugin = plugin;
        skinFolderPath = plugin.getDataFolder().toPath().resolve("skins");
        if (Files.notExists(skinFolderPath)) {
            Files.createDirectories(skinFolderPath);
        }
    }

    @Getter
    private final HashMap<String, Skin> skinMap = new HashMap<>();
    private Path skinFolderPath;

    public Skin getSkin(String skinName) {
        Skin s = skinMap.get(skinName);
        if (s != null) return s;
        Skin skin = new Skin();
        Path skinDir = skinFolderPath.resolve(skinName);
        Path skinGeometryPath = skinDir.resolve(skinName + ".json");
        Path skinPath = skinDir.resolve(skinName + ".png");
        if (Files.notExists(skinDir) || !Files.isDirectory(skinDir) || Files.notExists(skinGeometryPath)
                || !Files.isRegularFile(skinGeometryPath) || Files.notExists(skinPath)
                || !Files.isRegularFile(skinPath)) {
            return null;
        }

        String geometry;
        BufferedImage skinData;
        try {
            geometry = new String(Files.readAllBytes(skinGeometryPath), StandardCharsets.UTF_8);
            skinData = ImageIO.read(skinPath.toFile());
        } catch (IOException e) {
            return null;
        }

        skin.setGeometryData(geometry);
        skin.setGeometryName("geometry." + skinName);
        skin.setSkinData(skinData);
        skin.setSkinId(skinName);
        skin.setTrusted(true);
        skinMap.put(skinName, skin);
        return skin;
    }

    public CompoundTag getSkinTag(String skinName) {
        Skin skin = getSkin(skinName);
        if (skin == null) {
            return new CompoundTag();
        }
        return new CompoundTag().putByteArray("Data", skin.getSkinData().data)
                .putInt("SkinImageWidth", skin.getSkinData().width).putInt("SkinImageHeight", skin.getSkinData().height)
                .putString("ModelId", skin.getSkinId()).putString("CapeId", skin.getCapeId())
                .putByteArray("CapeData", skin.getCapeData().data).putInt("CapeImageWidth", skin.getCapeData().width)
                .putInt("CapeImageHeight", skin.getCapeData().height)
                .putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("AnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", skin.isPremium()).putBoolean("PersonaSkin", skin.isPersona())
                .putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic());
    }
}
