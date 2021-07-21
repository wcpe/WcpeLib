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
     * 判断 entityLocation 是否在 targetLocation 周围 radius 格内
     * @param targetLocation
     * @param entityLocation
     * @param radius
     * @return
     */
    public static boolean isInRadius(Location targetLocation, Location entityLocation, double radius) {
        if (targetLocation == null || entityLocation == null) {
            return false;
        }
        return targetLocation.distance(entityLocation) < radius;
    }

    /**
     * @param needGo 点坐标
     * @param AA     一个对角点
     * @param BB     另一个对角点
     * @return 返回boolean
     * @apiNote 判断needGo是否在AA BB之间
     */
    public static boolean isInAABB(Location needGo, Location AA, Location BB) {
        int xMax = (int) Math.max(AA.getX(), BB.getX()) + 1;
        int xMin = (int) (Math.min(AA.getX(), BB.getX()));
        int yMax = (int) (Math.max(AA.getY(), BB.getY()));
        int yMin = (int) (Math.min(AA.getY(), BB.getY()));
        int zMax = (int) (Math.max(AA.getZ(), BB.getZ())) + 1;
        int zMin = (int) (Math.min(AA.getZ(), BB.getZ()));
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
