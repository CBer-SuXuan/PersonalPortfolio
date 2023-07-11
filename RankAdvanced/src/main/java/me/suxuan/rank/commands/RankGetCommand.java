package me.suxuan.rank.commands;

import me.suxuan.rank.database.MySQL.Database;
import me.suxuan.rank.settings.LocalData;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.SQLiteData;
import me.suxuan.rank.settings.Settings;
import me.suxuan.rank.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.sql.SQLException;

/**
 * @author CBer_SuXuan
 * @className TestCommand
 * @date 2023/5/12 20:01
 * @description
 */
public class RankGetCommand extends SimpleSubCommand {

	protected RankGetCommand(SimpleCommandGroup parent) {
		super(parent, "get");
		setAutoHandleHelp(false);
		setPermission("rank.command.check");
		setPermissionMessage("&cYou don't have permission to this command");
		setUsage("<player>");
		setDescription("Check the rank info of the player.");

	}

	@Override
	protected void onCommand() {
		Common.setTellPrefix("&7[&e&lR&b&lS&7] ");
		if (Settings.USE_DATABASE.equals("None")) {
			Messenger.error(getPlayer(), "&cYou must use a way to store data. " +
					"Go to settings.yml, choose one way and restart the server!");
			return;
		}
		if (args.length == 0) {
			Messenger.error(getPlayer(), "&cYou need a &dplayer &cto get info!");
		} else if (args.length == 1) {
			Player player = Bukkit.getPlayer(args[0]);
			Database database = Database.getInstance();
			// Check player
			if (player == null || !player.isOnline()) {
				Messenger.error(getPlayer(), "&cPlayer &d" + args[0] + "&c is not online on this server!");
				return;
			}
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
			switch (Settings.USE_DATABASE) {
				case "Local" -> {
					LocalData data = LocalData.from(player);
					String info = "    &9Rank: &f" + data.getRank() + "\n" +
							"    &9Vessel: &f" + data.getVessel() + "\n" +
							"    &9Tier: &f" + data.getTier() + "\n" +
							"    &9Points: &f" + data.getPoints() + "\n" +
							"    &9Need &f" + PlayerUtils.getPointNeed(player) + "&9 Points to level up";
					Common.tell(getPlayer(), "&dPlayer &6" + player.getName() + "&d have following info\n" + info);
				}
				case "SQLite" -> {
					SQLiteData data = SQLiteData.from(player);
					String info = "    &9Rank: &f" + data.getRank() + "\n" +
							"    &9Vessel: &f" + data.getVessel() + "\n" +
							"    &9Tier: &f" + data.getTier() + "\n" +
							"    &9Points: &f" + data.getPoints() + "\n" +
							"    &9Need &f" + PlayerUtils.getPointNeed(player) + "&9 Points to level up";
					Common.tell(getPlayer(), "&dPlayer &6" + player.getName() + "&d have following info\n" + info);
				}
				case "MySQL" -> {
					try {
						MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
						String info = "    &9Rank: &f" + data.getRank() + "\n" +
								"    &9Vessel: &f" + data.getVessel() + "\n" +
								"    &9Tier: &f" + data.getTier() + "\n" +
								"    &9Points: &f" + data.getPoints() + "\n" +
								"    &9Need &f" + PlayerUtils.getPointNeed(player) + "&9 Points to level up";
						Common.tell(getPlayer(), "&dPlayer &6" + player.getName() + "&d have following info\n" + info);
					} catch (SQLException e) {
						Common.log("There are some errors of MySQL(get)! " + e.getMessage());
					}

				}
			}
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
		}
	}

}
