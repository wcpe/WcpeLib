package top.wcpe.wcpelib.model.bukkit.utils;

import javafx.beans.binding.StringBinding;
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
        StringBuilder sb = new StringBuilder(world.getName());
        sb.append(";");
        sb.append(loc.getX());
        sb.append(";");
        sb.append(loc.getY());
        sb.append(";");
        sb.append(loc.getZ());
        return sb.toString();
    }

    public static Location stringToLocation(String stringLocation) {
        if (stringLocation == null) return null;
        String[] locStringSplit = stringLocation.split(";");
        World world = Bukkit.getWorld(locStringSplit[0]);
        if (world == null) return null;
        try {
            return new Location(world, Double.parseDouble(locStringSplit[1]), Double.parseDouble(locStringSplit[2]), Double.parseDouble(locStringSplit[3]));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
