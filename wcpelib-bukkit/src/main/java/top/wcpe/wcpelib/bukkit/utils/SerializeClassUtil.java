package top.wcpe.wcpelib.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SerializeClassUtil {
    public static String joining(String appendString, String... args) {
        return Arrays.asList(args).stream().collect(Collectors.joining(appendString));
    }

    public static String locationToString(Location loc) {
        if (loc == null) return null;
        World world = loc.getWorld();
        if (world == null) return null;
        String sb = world.getName() + ";" +
                loc.getX() +
                ";" +
                loc.getY() +
                ";" +
                loc.getZ() +
                ";" +
                loc.getYaw() +
                ";" +
                loc.getPitch();
        return sb;
    }

    public static Location stringToLocation(String stringLocation) {
        if (stringLocation == null) return null;
        String[] locStringSplit = stringLocation.split(";");
        if (locStringSplit.length < 3) {
            return null;
        }
        World world = Bukkit.getWorld(locStringSplit[0]);
        if (world == null) return null;
        try {
            if (locStringSplit.length >= 5) {
                return new Location(world, Double.parseDouble(locStringSplit[1]), Double.parseDouble(locStringSplit[2]), Double.parseDouble(locStringSplit[3]), Float.parseFloat(locStringSplit[4]), Float.parseFloat(locStringSplit[5]));
            } else {
                return new Location(world, Double.parseDouble(locStringSplit[1]), Double.parseDouble(locStringSplit[2]), Double.parseDouble(locStringSplit[3]));
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
