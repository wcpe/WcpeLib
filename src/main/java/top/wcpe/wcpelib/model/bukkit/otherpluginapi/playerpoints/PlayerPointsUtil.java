package top.wcpe.wcpelib.model.bukkit.otherpluginapi.playerpoints;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;


/**
 * {@link PlayerPoints}的一个工具类
 * 
 * @author WCPE
 * @date 2021年4月8日 下午5:14:48
 */
public class PlayerPointsUtil {
	private static PlayerPointsAPI playerPointsAPI = null;

	public static PlayerPointsAPI getPlayerPointsAPI() {
		if (playerPointsAPI == null)
			if (Bukkit.getPluginManager().getPlugin("PlayerPoints") != null) {
				playerPointsAPI = PlayerPoints.class.cast(Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
			}
		return playerPointsAPI;
	}
}
