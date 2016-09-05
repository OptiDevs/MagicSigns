package ml.voltiac.bukkit.MagicSigns;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ml.voltiac.bukkit.MagicSigns.Signs.SignText;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

public class EventListener implements Listener {
	// you NEED to have @EventHandler before any event}

	// @EventHandler(priority = EventPriority.HIGHEST)

	@EventHandler(priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBreak(BlockBreakEvent e) {
		boolean debug = false;
		Player p = e.getPlayer();
		FileConfiguration con = Main.getInstance().getConfig();
		SignText ClickedSign = SignText.toSignText(e.getBlock());

		if (e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN
				|| e.getBlock().getType() == Material.SIGN) {

			for (String s : con.getConfigurationSection("Signs").getKeys(false)) {
				String path = "Signs." + s + ".";
				SignText find = SignText.toSignText(con.getStringList(path + "find"));
				SignText replace = SignText.toSignText(con.getStringList(path + "replace"));
				if (debug)
					p.sendMessage("Checking if matches " + s + ":");
				if (debug)
					p.sendMessage(find.line0);
				if (debug)
					p.sendMessage(find.line1);
				if (debug)
					p.sendMessage(find.line2);
				if (debug)
					p.sendMessage(find.line3);

				if (ClickedSign.line0.equalsIgnoreCase(find.line0) || find.line0.equals("%a")) {
					if (debug)
						p.sendMessage("Found " + replace.line0);
					if (ClickedSign.line1.equalsIgnoreCase(find.line1) || find.line1.equals("%a")) {
						if (debug)
							p.sendMessage("Found " + replace.line1);
						if (ClickedSign.line2.equalsIgnoreCase(find.line2) || find.line2.equals("%a")) {
							if (debug)
								p.sendMessage("Found " + replace.line2);
							if (ClickedSign.line3.equalsIgnoreCase(find.line3) || find.line3.equals("%a")) {
								if (debug)
									p.sendMessage("Found " + replace.line3);
								if (p.hasPermission(path + "permission.break") || p.isOp()) {
									if (debug)
										p.sendMessage("Player Has permission");
									e.setCancelled(false);
								} else {
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hey!" + ChatColor.GRAY
											+ " You cant break that type of sign.");
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		FileConfiguration con = Main.getInstance().getConfig();
		SignText ClickedSign = SignText.toSignText(e.getClickedBlock());
		ClickedSign = translateAlternateColorCodes('&', ClickedSign);
		// Sign sign = (Sign) e.getClickedBlock();
		p.sendMessage("Found interaction");
		if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN
				|| e.getClickedBlock().getType() == Material.SIGN) {

			p.sendMessage("Interaction was sign");
			for (String s : con.getConfigurationSection("Signs").getKeys(false)) {
				p.sendMessage("Checking if sign is " + s);
				String path = "Signs." + s + ".";
				SignText replace = SignText.toSignText(con.getStringList(path + "replace"));
				replace = replaceSignPlaceHolders(replace, p, path);
				replace = translateAlternateColorCodes('&', replace);
				if (ClickedSign.line0.equals(replace.line0)) {
					if (ClickedSign.line1.equals(replace.line1)) {
						if (ClickedSign.line2.equals(replace.line2)) {
							if (ClickedSign.line3.equals(replace.line3)) {
								if (p.hasPermission(Strings.get.permissionKey(s).SIGN_USE) || p.isOp()) {
									for (String a : con.getStringList(path + "anyclick")) {
										runStarters(p, a);
									}
									if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
										for (String a : con.getStringList(path + "leftclick")) {
											runStarters(p, a);
											e.setCancelled(true);
											if (p.hasPermission(path + "permission.break")) {
												p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Info!"
														+ ChatColor.GRAY + " To break a sign, " + ChatColor.AQUA
														+ "Shift + Left Click" + ChatColor.GRAY + ".");
											}
										}
									}
									if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
										for (String a : con.getStringList(path + "rightclick")) {
											runStarters(p, a);
										}
									}
								} else {
									p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hey!" + ChatColor.GRAY
											+ " You cant use that type of sign.");
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignEdit(SignChangeEvent e) {
		Player p = e.getPlayer();
		FileConfiguration con = Main.getInstance().getConfig();
		String[] arr = { e.getLine(0), e.getLine(1), e.getLine(2), e.getLine(3) };
		SignText PlacedSign = SignText.toSignText(arr);
		for (String s : con.getConfigurationSection("Signs").getKeys(false)) {
			String path = "Signs." + s + ".";
			SignText find = SignText.toSignText(con.getStringList(path + "find"));
			SignText replace = SignText.toSignText(con.getStringList(path + "replace"));

			if (PlacedSign.line0.equalsIgnoreCase(find.line0) || find.line0.equals("%a")) {
				if (PlacedSign.line1.equalsIgnoreCase(find.line1) || find.line1.equals("%a")) {
					if (PlacedSign.line2.equalsIgnoreCase(find.line2) || find.line2.equals("%a")) {
						if (PlacedSign.line3.equalsIgnoreCase(find.line3) || find.line3.equals("%a")) {
							if (p.hasPermission(path + "permission.create") || p.isOp()) {
								replace = replaceSignPlaceHolders(replace, e, path);
								replace = translateAlternateColorCodes('&', replace);

								e.setLine(0, replace.line0);
								e.setLine(1, replace.line1);
								e.setLine(2, replace.line2);
								e.setLine(3, replace.line3);

								p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Yay!" + ChatColor.GRAY + " " + s
										+ " sign was created!");
								p.sendMessage(replace.line0);
								p.sendMessage(replace.line1);
								p.sendMessage(replace.line2);
								p.sendMessage(replace.line3);
							} else {
								p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Hey!" + ChatColor.GRAY
										+ " You cant create that type of sign.");
							}
						}
					}
				}
			}
		}
	}

	public boolean runStarters(Player p, String s) {
		if (s.startsWith("[command]")) {
			s = s.replaceFirst("[command] ", "");
			p.performCommand(s);
			return true;
		} else if (s.startsWith("[command*] ")) {
			s = s.replaceFirst("[command*] ", "");
			p.setOp(true);
			p.performCommand(s);
			p.setOp(false);
			return true;
		} else if (s.startsWith("[console] ")) {
			s = s.replaceFirst("[console] ", "");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), s);
			return true;
		} else if (s.startsWith("[message] ")) {
			s = s.replaceFirst("[message] ", "");
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			return true;
		} else if (s.startsWith("[sendRaw] ")) {
			s = s.replaceFirst("[sendRaw] ", "");
			p.sendRawMessage(s);
			return true;
		} else if (s.startsWith("[sendRaw] ")) {
			s = s.replaceFirst("[sendRaw] ", "");
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(s)));
			return true;
		} else if (s.startsWith("[broadcast] ")) {
			s = s.replaceFirst("[broadcast] ", "");
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', s));
			return true;
		}
		return false;
	}

	public String replaceSignPlaceHolders(String string, SignChangeEvent e, String ConfigPath) {
		Player p = e.getPlayer();
		Sign sign = (Sign) e.getBlock();

		return replaceSignPlaceHolders(string, SignText.toSignText(sign), p, ConfigPath);
	}

	public String replaceSignPlaceHolders(String string, SignText e, Player p, String ConfigPath) {

		string = string.replaceAll("%p", p.getName());
		string = string.replaceAll("%d", p.getDisplayName());
		string = string.replaceAll("%h", String.valueOf(p.getHealth()));
		string = string.replaceAll("%f", String.valueOf(p.getFoodLevel()));
		string = string.replaceAll("%b", "<none>");

		if (e != null) {
			string = string.replaceAll("%1", e.line0);
			string = string.replaceAll("%2", e.line1);
			string = string.replaceAll("%3", e.line2);
			string = string.replaceAll("%4", e.line3);
		}

		string = string.replaceAll("%o", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
		string = string.replaceAll("%o", String.valueOf(Bukkit.getServer().getMaxPlayers()));

		return string;
	}

	public SignText translateAlternateColorCodes(char args0, SignText args1) {
		String[] text = args1.toArray();

		for (int i = 0; i < text.length; i++) {
			text[i] = ChatColor.translateAlternateColorCodes('&', text[i]);
		}

		return SignText.toSignText(text);
	}

	public SignText replaceSignPlaceHolders(SignChangeEvent e, String ConfigPath) {
		SignText sign = SignText.toSignText(e.getLines());
		String[] text = sign.toArray();

		for (int i = 0; i < text.length; i++) {
			String string = text[i];
			text[i] = replaceSignPlaceHolders(string, sign, e.getPlayer(), ConfigPath);
		}

		return SignText.toSignText(text);
	}

	public SignText replaceSignPlaceHolders(SignText sign, SignChangeEvent e, String ConfigPath) {
		String[] text = sign.toArray();

		for (int i = 0; i < text.length; i++) {
			String string = text[i];
			text[i] = replaceSignPlaceHolders(string, sign, e.getPlayer(), ConfigPath);
		}

		return SignText.toSignText(text);
	}

	public SignText replaceSignPlaceHolders(SignText sign, Player p, String ConfigPath) {
		String[] text = sign.toArray();

		for (int i = 0; i < text.length; i++) {
			String string = text[i];
			text[i] = replaceSignPlaceHolders(string, sign, p, ConfigPath);
		}

		return SignText.toSignText(text);
	}
}
