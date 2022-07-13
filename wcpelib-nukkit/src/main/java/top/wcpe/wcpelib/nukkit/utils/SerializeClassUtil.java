package top.wcpe.wcpelib.nukkit.utils;


import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SerializeClassUtil {
    public static String joining(String appendString, String... args) {
        return Arrays.asList(args).stream().collect(Collectors.joining(appendString));
    }

    public static String locationToString(Location loc) {
        if (loc == null) return null;
        Level world = loc.getLevel();
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
        Level world = Server.getInstance().getLevelByName(locStringSplit[0]);
        if (world == null) return null;
        try {
            if (locStringSplit.length >= 5) {
                return new Location(Double.parseDouble(locStringSplit[1]), Double.parseDouble(locStringSplit[2]), Double.parseDouble(locStringSplit[3]), Double.parseDouble(locStringSplit[4]), Double.parseDouble(locStringSplit[5]), world);
            } else {
                return new Location(Double.parseDouble(locStringSplit[1]), Double.parseDouble(locStringSplit[2]), Double.parseDouble(locStringSplit[3]), world);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
