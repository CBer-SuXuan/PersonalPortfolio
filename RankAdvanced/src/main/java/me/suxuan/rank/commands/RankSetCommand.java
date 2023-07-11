package me.suxuan.rank.commands;

import me.suxuan.rank.database.MySQL.Database;
import me.suxuan.rank.settings.LocalData;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.SQLiteData;
import me.suxuan.rank.settings.Settings;
import me.suxuan.rank.utils.MessageUtils;
import me.suxuan.rank.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author CBer_SuXuan
 * @className RankSetCommand
 * @date 2023/5/12 22:29
 * @description /rank set xxx xxx
 */
public class RankSetCommand extends SimpleSubCommand {

	protected RankSetCommand(SimpleCommandGroup parent) {
		super(parent, "set");
		setAutoHandleHelp(false);
		setPermission("rank.command.set");
		setPermissionMessage("&cYou don't have permission to this command");
		setUsage("<player> <vessel|tier|points> <number>");
		setDescription("Set vessel|tier|points for player, vessel need to be int(0 represent join game level)");
	}

	// /rank set <player> <vessel|tier|points> <number>
	@Override
	protected void onCommand() {
		Common.setTellPrefix("&7[&e&lR&b&lS&7] ");
		if (Settings.USE_DATABASE.equals("None")) {
			Messenger.error(getPlayer(), "&cYou must use a way to store data. " +
					"Go to settings.yml, choose one way and restart the server!");
			return;
		}
		if (args.length == 0) {
			Messenger.error(getPlayer(), "&cYou need a &dplayer &cto set!");
		} else if (args.length == 1) {
			Messenger.error(getPlayer(), "&cYou need a target to set, &6vessel&c, &6tier &cor &6points!");
		} else if (args.length == 2) {
			Messenger.error(getPlayer(), "&cYou need to set a positive integer!");
		} else if (args.length == 3) {
			Player player = Bukkit.getPlayer(args[0]);
			Database database = Database.getInstance();
			// Check player
			if (player == null || !player.isOnline()) {
				Messenger.error(getPlayer(), "&cPlayer &d" + args[0] + "&c is not online on this server!");
				return;
			}
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
			// Check target(vessel ,tier or points)
			List<String> target = Arrays.asList("vessel", "tier", "points");
			if (!target.contains(args[1])) {
				Messenger.error(getPlayer(), "&cNeed &6vessel&c, &6tier &cor &6points!");
				return;
			}
			int num = 0;
			// Check number
			try {
				num = Integer.parseInt(args[2]);
				if (num < 0) {
					Messenger.error(getPlayer(), "&cNeed positive integer!");
					return;
				}
			} catch (NumberFormatException e) {
				Messenger.error(getPlayer(), "&cNeed positive integer!");
				return;
			}
			// Run set
			switch (args[1]) {
				case "vessel" -> {
					if (num > 20) {
						Messenger.error(getPlayer(), "&cVessel must less than 20");
						return;
					}
					switch (Settings.USE_DATABASE) {
						case "Local" -> {
							LocalData data = LocalData.from(player);
							data.setVesselInt(num);
						}
						case "SQLite" -> {
							SQLiteData data = SQLiteData.from(player);
							data.setVesselInt(num);
						}
						case "MySQL" -> {
							try {
								MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
								data.setVessel_int(num);
								database.updatePlayerData(data);
							} catch (SQLException e) {
								Common.log("There are some errors of MySQL(set vessel_int)! " + e.getMessage());
							}
						}
					}

					switch (Settings.USE_DATABASE) {
						case "Local" -> {
							LocalData data = LocalData.from(player);
							data.setVessel(MessageUtils.vesselToString(num));
						}
						case "SQLite" -> {
							SQLiteData data = SQLiteData.from(player);
							data.setVessel(MessageUtils.vesselToString(num));
						}
						case "MySQL" -> {
							try {
								MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
								data.setVessel(MessageUtils.vesselToString(num));
								database.updatePlayerData(data);
							} catch (SQLException e) {
								Common.log("There are some errors of MySQL(set vessel)!" + e.getMessage());
							}
						}
					}
					Messenger.success(getPlayer(), "&aSuccessfully set player &d" + args[0]
							+ " &6" + args[1] + "&a to &b" + MessageUtils.vesselToString(num));
					if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
					PlayerUtils.checkUpdateRank(player);
					return;
				}
				case "tier" -> {
					if (num > 10) {
						Messenger.error(getPlayer(), "&cTier must less than 10");
						return;
					}
					switch (Settings.USE_DATABASE) {
						case "Local" -> {
							LocalData data = LocalData.from(player);
							if (num == 0) {
								data.setVessel("Mortal");
								data.setVesselInt(0);
								data.setPoints(0);
							}
							data.setTier(num);
						}
						case "SQLite" -> {
							SQLiteData data = SQLiteData.from(player);
							if (num == 0) {
								data.setVessel("Mortal");
								data.setVesselInt(0);
								data.setPoints(0);
							}
							data.setTier(num);
						}
						case "MySQL" -> {
							try {
								MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
								if (num == 0) {
									data.setVessel("Mortal");
									data.setVessel_int(0);
									data.setPoints(0);
								}
								data.setTier(num);
								database.updatePlayerData(data);
							} catch (SQLException e) {
								Common.log("There are some errors of MySQL(set tier)! " + e.getMessage());
							}
						}
					}
					PlayerUtils.checkUpdateRank(player);
				}
				case "points" -> {
					switch (Settings.USE_DATABASE) {
						case "Local" -> {
							LocalData data = LocalData.from(player);
							data.setPoints(num);
						}
						case "SQLite" -> {
							SQLiteData data = SQLiteData.from(player);
							data.setPoints(num);
						}
						case "MySQL" -> {
							try {
								MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
								data.setPoints(num);
								database.updatePlayerData(data);
							} catch (SQLException e) {
								Common.log("There are some errors of MySQL(set points)! " + e.getMessage());
								e.printStackTrace();
							}
						}
					}
					PlayerUtils.checkUpdateRank(player);
				}
			}
			Messenger.success(getPlayer(), "&aSuccessfully set player &d" + args[0]
					+ " &6" + args[1] + "&a to &b" + num);
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
		}
	}

	@Override
	protected List<String> tabComplete() {
		switch (args.length) {
			case 1 -> {
				return completeLastWord(Common.getPlayerNames());
			}
			case 2 -> {
				return completeLastWord("vessel", "tier", "points");
			}
			case 3 -> {
				return completeLastWord("<number>");
			}
		}
		return NO_COMPLETE;
	}
}
