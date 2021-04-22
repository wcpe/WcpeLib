package top.wcpe.wcpelib.model.bukkit.otherpluginapi.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * {@link Vault}的一个工具类
 * 
 * @author WCPE
 * @date 2021年4月8日 下午5:15:02
 */
public class VaultUtil {
	private static Economy economy = null;
	private static Chat chat = null;
	private static Permission permission = null;

	public static Economy getEconomy() {
		if (economy == null)
			if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
				RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
						.getRegistration(net.milkbowl.vault.economy.Economy.class);
				economy = economyProvider.getProvider();
			}
		return economy;
	}

	public static Chat getChat() {
		if (chat == null)
			if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
				RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager()
						.getRegistration(net.milkbowl.vault.chat.Chat.class);
				chat = chatProvider.getProvider();
			}
		return chat;
	}

	public static Permission getPermission() {
		if (permission == null)
			if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
				RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager()
						.getRegistration(net.milkbowl.vault.permission.Permission.class);
				permission = permissionProvider.getProvider();

			}
		return permission;
	}
}
