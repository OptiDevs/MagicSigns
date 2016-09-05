package ml.voltiac.bukkit.MagicSigns;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public EventListener listener = new EventListener();

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(), this);
		
		Logger log= Bukkit.getServer().getLogger();
		log.info("Plugin Enabled");
		loadConfiguration();
	}

	@Override
	public void onDisable() {
		Logger log= Bukkit.getServer().getLogger();
		log.info("Plugin Disabled");

	}
	
	public FileConfiguration Config = getConfig();

	public static Main getInstance() {
		return (Main) Bukkit.getPluginManager().getPlugin("MagicSigns");
	}  

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m+--------------------------+"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6    Plugin:&b Magic Signs"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6       Version:&b v0.1"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6    Author: DiamondDeveloper"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m+--------------------------+"));
		return true;
	}

	public void loadConfiguration() {
		getConfig().getDefaults();
		saveDefaultConfig();
		reloadConfig();
	}

}
