/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package tntrun.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;
import tntrun.utils.FormattingCodesParser;
import tntrun.utils.Menu;
import tntrun.utils.Utils;

public class GameCommands implements CommandExecutor {

	private TNTRun plugin;
	private Menu menu;

	public GameCommands(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(FormattingCodesParser.parseFormattingCodes(Messages.trprefix + "&c You must be a player"));
			return true;
		}
		Player player = (Player) sender;
		if (args.length < 1) {
			Messages.sendMessage(player, "&7============" + Messages.trprefix + "============");
			Messages.sendMessage(player, Messages.trprefix + "&c Please use &6/tr help");
			return true;
		}
		// help command
		if (args[0].equalsIgnoreCase("help")) {
			Messages.sendMessage(player, "&7============" + Messages.trprefix + "============");
			player.spigot().sendMessage(Utils.getTextComponent("/tr lobby", true), Utils.getTextComponent(Messages.helplobby));
			player.spigot().sendMessage(Utils.getTextComponent("/tr list [arena]", true), Utils.getTextComponent(Messages.helplist));
			player.spigot().sendMessage(Utils.getTextComponent("/tr join [arena]", true), Utils.getTextComponent(Messages.helpjoin));
			player.spigot().sendMessage(Utils.getTextComponent("/tr autojoin", true), Utils.getTextComponent(Messages.helpautojoin));
			player.spigot().sendMessage(Utils.getTextComponent("/tr leave", true), Utils.getTextComponent(Messages.helpleave));
			player.spigot().sendMessage(Utils.getTextComponent("/tr vote", true), Utils.getTextComponent(Messages.helpvote));
			player.spigot().sendMessage(Utils.getTextComponent("/tr info", true), Utils.getTextComponent(Messages.helpinfo));
			player.spigot().sendMessage(Utils.getTextComponent("/tr stats", true), Utils.getTextComponent(Messages.helpstats));
			player.spigot().sendMessage(Utils.getTextComponent("/tr leaderboard [size]", true), Utils.getTextComponent(Messages.helplb));
			player.spigot().sendMessage(Utils.getTextComponent("/tr listkit [kit]", true), Utils.getTextComponent(Messages.helplistkit));
			player.spigot().sendMessage(Utils.getTextComponent("/tr start {arena}", true), Utils.getTextComponent(Messages.helpstart));
			player.spigot().sendMessage(Utils.getTextComponent("/tr cmds", true), Utils.getTextComponent(Messages.helpcmds));
			return true;

		} else if (args[0].equalsIgnoreCase("lobby")) {
			if (plugin.globallobby.isLobbyLocationSet()) {
				if (plugin.globallobby.isLobbyLocationWorldAvailable()) {
					player.teleport(plugin.globallobby.getLobbyLocation());
					Messages.sendMessage(player, Messages.trprefix + Messages.teleporttolobby);
				} else {
					Messages.sendMessage(player, Messages.trprefix + "&c Lobby world is unloaded or doesn't exist");
				}
			} else {
				Messages.sendMessage(player, Messages.trprefix + "&c Lobby isn't set");
			}
			return true;
		}

		// list arenas
		else if (args[0].equalsIgnoreCase("list")) {
			if (args.length >= 2) {
				Arena arena = plugin.amanager.getArenaByName(args[1]);
				if (arena == null) {
					Messages.sendMessage(player, Messages.trprefix + Messages.arenanotexist.replace("{ARENA}", args[1]));
					return true;
				}
				//list arena details
				Messages.sendMessage(player, "&7============" + Messages.trprefix + "============");
				Messages.sendMessage(player, "&7Arena Details: &a" + arena.getArenaName());

				String arenaStatus = "Enabled";
				if (!arena.getStatusManager().isArenaEnabled()) {
					arenaStatus = "Disabled";
				}
				player.sendMessage(ChatColor.GOLD + "Status " + ChatColor.WHITE + "- " + ChatColor.RED + arenaStatus);
				player.sendMessage(ChatColor.GOLD + "Min Players " + ChatColor.WHITE + "- " + ChatColor.RED + + arena.getStructureManager().getMinPlayers());
				player.sendMessage(ChatColor.GOLD + "Max Players " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getMaxPlayers());
				player.sendMessage(ChatColor.GOLD + "Time Limit " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getTimeLimit() + " seconds");
				player.sendMessage(ChatColor.GOLD + "Countdown " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getCountdown() + " seconds");
				player.sendMessage(ChatColor.GOLD + "Teleport to " + ChatColor.WHITE + "- " + ChatColor.RED + Utils.getTitleCase(arena.getStructureManager().getTeleportDestination().toString()));
				player.sendMessage(ChatColor.GOLD + "Player Count " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getPlayersManager().getPlayersCount());
				player.sendMessage(ChatColor.GOLD + "Vote Percent " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getVotePercent());
				player.sendMessage(ChatColor.GOLD + "PVP Damage Enabled " + ChatColor.WHITE + "- " + ChatColor.RED + Utils.getTitleCase(arena.getStructureManager().getDamageEnabled().toString()));
				if (arena.getStructureManager().isKitsEnabled()) {
					player.sendMessage(ChatColor.GOLD + "Kits Enabled " + ChatColor.WHITE +"- " + ChatColor.RED + "Yes");
				} else {
					player.sendMessage(ChatColor.GOLD + "Kits Enabled " + ChatColor.WHITE + "- " + ChatColor.RED + "No");
				}
				if (arena.getStructureManager().getRewards().getXPReward() != 0) {
					player.sendMessage(ChatColor.GOLD + "XP Reward " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getRewards().getXPReward());
				}
				if (arena.getStructureManager().getRewards().getMoneyReward() != 0) {
					player.sendMessage(ChatColor.GOLD + "Money Reward " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getRewards().getMoneyReward());
				}
				List<String> materialrewards = arena.getStructureManager().getRewards().getMaterialReward();
				List<String> materialamounts = arena.getStructureManager().getRewards().getMaterialAmount();

				String rewardmessage = "";
				for (int i=0; i < materialrewards.size(); i++) {
					if (arena.getStructureManager().getRewards().isValidReward(materialrewards.get(i), materialamounts.get(i))) {
						rewardmessage += materialamounts.get(i) + ChatColor.GOLD + " x " + ChatColor.RED + materialrewards.get(i) + ", ";
					}
				}
				if (rewardmessage.length() > 0) {
					player.sendMessage(ChatColor.GOLD + "Material Reward " + ChatColor.WHITE + "-  " + ChatColor.RED + rewardmessage.substring(0, rewardmessage.length() - 2));
				}

				if (arena.getStructureManager().getRewards().getCommandReward() != null) {
					player.sendMessage(ChatColor.GOLD + "Command Reward " + ChatColor.WHITE + "- " + ChatColor.GOLD + "\"" + ChatColor.RED + arena.getStructureManager().getRewards().getCommandReward() + ChatColor.GOLD + "\"");
				}

				if (arena.getStructureManager().getFee() > 0) {
					player.sendMessage(ChatColor.GOLD + "Join Fee " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getFee());
					if (arena.getStructureManager().isCurrencyEnabled()) {
						player.sendMessage(ChatColor.GOLD + "Item Currency " + ChatColor.WHITE + "- " + ChatColor.RED + arena.getStructureManager().getCurrency().toString());
					}
				}

				return true;
			}
			StringBuilder message = new StringBuilder(200);
			message.append(Messages.trprefix + Messages.availablearenas);
			if (plugin.amanager.getArenas().size() != 0) {
				for (Arena arena : plugin.amanager.getArenas()) {
					if (arena.getStatusManager().isArenaEnabled()) {
						message.append("&a" + arena.getArenaName() + " ; ");
					} else {
						message.append("&c" + arena.getArenaName() + " ; ");
					}
				}
				message.setLength(message.length() - 2);
			}
			Messages.sendMessage(player, message.toString());
			return true;
		}

		// join arena
		else if (args[0].equalsIgnoreCase("join")) {
			if (args.length == 1 && player.hasPermission("tntrun.joinmenu")) {
				menu = new Menu(plugin);
				menu.buildMenu(player);
				return false;
			}
			if (args.length != 2) {
				Messages.sendMessage(player, Messages.trprefix + "&c Invalid number of arguments supplied");
				return false;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena != null) {
				boolean canJoin = arena.getPlayerHandler().checkJoin(player);
				if (canJoin) {
					arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoplayer, Messages.playerjoinedtoothers);
				}
				return true;
			} else {
				Messages.sendMessage(player, Messages.trprefix + Messages.arenanotexist.replace("{ARENA}", args[1]));
				return true;
			}
		}

		// autojoin
		else if (args[0].equalsIgnoreCase("autojoin")) {
			if (plugin.amanager.getPlayerArena(player.getName()) != null) {
				Messages.sendMessage(player, Messages.trprefix + Messages.arenajoined);
				return true;
			}
			if (plugin.amanager.getArenas().size() != 0) {
				for (Arena arena : plugin.amanager.getArenas()) {
					if (arena.getStatusManager().isArenaEnabled()) {
						boolean canJoin = arena.getPlayerHandler().checkJoin(player);
						if (canJoin) {
							arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoplayer, Messages.playerjoinedtoothers);
							return false;
						}
					}
				}
			}
			Messages.sendMessage(player, Messages.trprefix + Messages.noarenas);
			return true;
		}

		// tntrun_reloaded info
		else if (args[0].equalsIgnoreCase("info")) {
			Utils.displayInfo(player);
		}

		// player stats
		else if (args[0].equalsIgnoreCase("stats")) {
			if (!plugin.useStats()) {
				Messages.sendMessage(player, Messages.trprefix + Messages.statsdisabled);
				return true;
			}
			Messages.sendMessage(player, Messages.statshead);
			Messages.sendMessage(player, Messages.gamesplayed + plugin.stats.getPlayedGames(player));
			Messages.sendMessage(player, Messages.gameswon + plugin.stats.getWins(player));
			Messages.sendMessage(player, Messages.gameslost + plugin.stats.getLosses(player));
		}

		// leaderboard
		else if (args[0].equalsIgnoreCase("leaderboard")) {
			if (!plugin.useStats()) {
				Messages.sendMessage(player, Messages.trprefix + Messages.statsdisabled);
				return true;
			}
			int entries = plugin.getConfig().getInt("leaderboard.maxentries", 10);
			if (args.length > 1) {
				if (Utils.isNumber(args[1]) && Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= entries) {
					entries = Integer.parseInt(args[1]);
				}
			}
			Messages.sendMessage(player, Messages.leaderhead);
			plugin.stats.getLeaderboard(player, entries);
		}

		// leave arena
		else if (args[0].equalsIgnoreCase("leave")) {
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena != null) {
				arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
				return true;
			} else {
				Messages.sendMessage(player, Messages.trprefix + Messages.playernotinarena);
				return true;
			}
		}

		// all commands
		else if (args[0].equalsIgnoreCase("cmds")) {
			Messages.sendMessage(player, "&7============" + Messages.trprefix + "============");
			Utils.displayHelp(player);
			Messages.sendMessage(player, "&7============[&6Other commands&7]============");
			Messages.sendMessage(player, "&6/trsetup delspectate {arena} &f- &c\n    " + Messages.setupdelspectate);
			Messages.sendMessage(player, "&6/trsetup setgameleveldestroydelay {arena} {ticks} &f- &c\n    " + Messages.setupdelay);
			Messages.sendMessage(player, "&6/trsetup setregenerationdelay {arena} {ticks} &f- &c\n    " + Messages.setupregendelay);
			Messages.sendMessage(player, "&6/trsetup setmaxplayers {arena} {players} &f- &c\n    " + Messages.setupmax);
			Messages.sendMessage(player, "&6/trsetup setminplayers {arena} {players} &f- &c\n    " + Messages.setupmin);
			Messages.sendMessage(player, "&6/trsetup setvotepercent {arena} {0<votepercent<1} &f- &c\n    " + Messages.setupvote);
			Messages.sendMessage(player, "&6/trsetup settimelimit {arena} {seconds} &f- &c\n    " + Messages.setuptimelimit);
			Messages.sendMessage(player, "&6/trsetup setcountdown {arena} {seconds} &f- &c\n    " + Messages.setupcountdown);
			Messages.sendMessage(player, "&6/trsetup setmoneyreward {arena} {amount} &f- &c\n    " + Messages.setupmoney);
			Messages.sendMessage(player, "&6/trsetup setteleport {arena} {previous/lobby} &f- &c\n    " + Messages.setupteleport);
			Messages.sendMessage(player, "&6/trsetup setdamage {arena} {yes/no/zero} &f- &c\n    " + Messages.setupdamage);
			Messages.sendMessage(player, "&6/trsetup enablekits {arena} &f- &c" + Messages.setupenablekits);
			Messages.sendMessage(player, "&6/trsetup disablekits {arena} &f- &c" + Messages.setupdisablekits);
			Messages.sendMessage(player, "&6/trsetup setbarcolor &f- &c" + Messages.setupbarcolor);
			Messages.sendMessage(player, "&6/trsetup reloadbars &f- &c" + Messages.setupreloadbars);
			Messages.sendMessage(player, "&6/trsetup reloadtitles &f- &c" + Messages.setupreloadtitles);
			Messages.sendMessage(player, "&6/trsetup reloadmsg &f- &c" + Messages.setupreloadmsg);
			Messages.sendMessage(player, "&6/trsetup reloadconfig &f- &c" + Messages.setupreloadconfig);
			Messages.sendMessage(player, "&6/trsetup enable {arena} &f- &c" + Messages.setupenable);
			Messages.sendMessage(player, "&6/trsetup disable {arena} &f- &c" + Messages.setupdisable);
			Messages.sendMessage(player, "&6/trsetup delete {arena} &f- &c" + Messages.setupdelete);
			Messages.sendMessage(player, "&6/trsetup setreward {arena} &f- &c" + Messages.setupreward);
			Messages.sendMessage(player, "&6/trsetup setfee {arena} &f- &c" + Messages.setupfee);
			Messages.sendMessage(player, "&6/trsetup setcurrency {arena} &f- &c" + Messages.setupcurrency);
			Messages.sendMessage(player, "&6/trsetup help &f- &c" + Messages.setuphelp);
		}

		// vote
		else if (args[0].equalsIgnoreCase("vote")) {
			Arena arena = plugin.amanager.getPlayerArena(player.getName());
			if (arena != null) {
				if (arena.getPlayerHandler().vote(player)) {
					Messages.sendMessage(player, Messages.trprefix + Messages.playervotedforstart);
				} else {
					Messages.sendMessage(player, Messages.trprefix + Messages.playeralreadyvotedforstart);
				}
				return true;
			} else {
				Messages.sendMessage(player, Messages.trprefix + Messages.playernotinarena);
				return true;
			}
		}

		// listkits
		else if (args[0].equalsIgnoreCase("listkit") || args[0].equalsIgnoreCase("listkits")) {
			if (args.length >= 2) {
				//list kit details
				plugin.kitmanager.listKit(args[1], player);
				return true;
			}
			StringBuilder message = new StringBuilder(200);
			message.append(Messages.trprefix + Messages.availablekits);
			if (plugin.kitmanager.getKits().size() != 0) {
				for (String kit : plugin.kitmanager.getKits()) {
					message.append("&a" + kit + " ; ");
				}
				message.setLength(message.length() - 2);
			}
			Messages.sendMessage(player, message.toString());
			return true;
		}

		// start
		else if (args[0].equalsIgnoreCase("start")) {
			if (!player.hasPermission("tntrun.start")) {
				Messages.sendMessage(player, Messages.nopermission);
				return true;
			}
			if (args.length != 2) {
				Messages.sendMessage(player, Messages.trprefix + "&c Invalid number of arguments supplied");
				return true;
			}
			Arena arena = plugin.amanager.getArenaByName(args[1]);
			if (arena != null) {
				if (arena.getPlayersManager().getPlayersCount() <= 1) {
					Messages.sendMessage(player, Messages.trprefix + Messages.playersrequiredtostart);
					return true;
				}
				if (!arena.getStatusManager().isArenaStarting()) {
					plugin.getServer().getConsoleSender().sendMessage("[TNTRun] Arena " + ChatColor.GOLD + arena.getArenaName() + ChatColor.WHITE + " force-started by " + ChatColor.AQUA + player.getName());
					arena.getGameHandler().forceStartByCommand();
					return false;
				}
			} else {
				Messages.sendMessage(player, Messages.trprefix + Messages.arenanotexist.replace("{ARENA}", args[1]));
				return true;
			}
		}

		else {
			Messages.sendMessage(player, Messages.trprefix + "&c Invalid argument supplied, please use &6/tr help");
			return true;
		}	
		return false;
	}

}
