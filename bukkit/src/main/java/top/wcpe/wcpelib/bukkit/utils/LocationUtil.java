package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WCPE
 */
public final class LocationUtil {

    /**
     * @param needGo 点坐标
     * @param AA     一个对角点
     * @param BB     另一个对角点
     * @return 返回boolean
     * @apiNote 判断needGo是否在AA BB之间
     */
    public static boolean isInAABB(Location needGo, Location AA, Location BB) {
        double xMax = (Math.max(AA.getX(), BB.getX()));
        double xMin = (Math.min(AA.getX(), BB.getX()));
        double yMax = (Math.max(AA.getY(), BB.getY()));
        double yMin = (Math.min(AA.getY(), BB.getY()));
        double zMax = (Math.max(AA.getZ(), BB.getZ()));
        double zMin = (Math.min(AA.getZ(), BB.getZ()));
        return (needGo.getX() >= xMin) && (needGo.getX() <= xMax) && (needGo.getY() >= yMin) && (needGo.getY() <= yMax)
                && (needGo.getZ() >= zMin) && (needGo.getZ() <= zMax);
    }

    /**
     * @param a 一个对角点
     * @param b 另一个对角点
     * @return 返回List<Location>
     * @apiNote 获得之间所有坐标
     */
    public static List<Location> getAllLocation(Location a, Location b) {
        int maxX = Math.max(a.getBlockX(), b.getBlockX());
        int maxY = Math.max(a.getBlockY(), b.getBlockY());
        int maxZ = Math.max(a.getBlockZ(), b.getBlockZ());
        int minX = Math.min(a.getBlockX(), b.getBlockX());
        int minY = Math.min(a.getBlockY(), b.getBlockY());
        int minZ = Math.min(a.getBlockZ(), b.getBlockZ());
        List<Location> list = new ArrayList<>();
        for (double y = minY; y <= maxY; y++) {
            for (double z = minZ; z <= maxZ; z++) {
                for (double x = minX; x <= maxX; x++) {
                    list.add(new Location(a.getWorld(), x, y, z));
                }
            }
        }
        return list;
    }

    /**
     * @param a 一个对角点
     * @param b 另一个对角点
     * @return List<Block>
     * @apiNote 获得之间所有方块
     */
    public static List<Block> getAllBlock(Location a, Location b) {
        return getAllLocation(a, b).stream().map(Location::getBlock).collect(Collectors.toList());
    }
}
