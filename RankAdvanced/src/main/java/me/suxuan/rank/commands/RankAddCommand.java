package me.suxuan.rank.commands;

import me.suxuan.rank.database.MySQL.Database;
import me.suxuan.rank.settings.LocalData;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.SQLiteData;
import me.suxuan.rank.settings.Settings;
import me.suxuan.rank.utils.MathUtils;
import me.suxuan.rank.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.sql.SQLException;
import java.util.List;

/**
 * @author CBer_SuXuan
 * @className RankSetCommand
 * @date 2023/5/12 22:29
 * @description /rank set xxx xxx
 */
public class RankAddCommand extends SimpleSubCommand {

	protected RankAddCommand(SimpleCommandGroup parent) {
		super(parent, "add");
		setAutoHandleHelp(false);
		setPermission("rank.command.add");
		setPermissionMessage("&cYou don't have permission to this command");
		setUsage("<player> <number|min> <max>");
		setDescription("Add point(s) for player, type two number represent random points include min and max");
	}

	// /rank add <player> <number|from> [to]
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
			Messenger.error(getPlayer(), "&cYou need to set a positive integer!");
		} else if (args.length == 2) {
			Player player = Bukkit.getPlayer(args[0]);
			Database database = Database.getInstance();
			// Check player
			if (player == null || !player.isOnline()) {
				Messenger.error(getPlayer(), "&cPlayer &d" + args[0] + "&c is not online on this server!");
				return;
			}
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
			int num = 0;
			// Check number
			try {
				num = Integer.parseInt(args[1]);
				if (num < 0) {
					Messenger.error(getPlayer(), "&cNeed positive integer!");
					return;
				}
			} catch (NumberFormatException e) {
				Messenger.error(getPlayer(), "&cNeed positive integer!");
				return;
			}
			// Run add number
			add(player, num);

			Messenger.success(getPlayer(), "&aSuccessfully add player &d" + args[0]
					+ " &b" + num + " &apoint(s)");
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
		} else if (args.length == 3) {
			Player player = Bukkit.getPlayer(args[0]);
			// Check player
			if (player == null || !player.isOnline()) {
				Messenger.error(getPlayer(), "&cPlayer &d" + args[0] + "&c is not online on this server!");
				return;
			}
			if (PlayerUtils.checkNeedUp(player)) PlayerUtils.levelUp(player);
			PlayerUtils.checkUpdateRank(player);
			int min = 0;
			int max = 0;
			// Check number
			try {
				min = Integer.parseInt(args[1]);
				max = Integer.parseInt(args[2]);
				if (min < 0 || max < 0) {
					Messenger.error(getPlayer(), "&cNeed positive integer!");
					return;
				}
			} catch (NumberFormatException e) {
				Messenger.error(getPlayer(), "&cNeed positive integer!");
				return;
			}
			// Run add from to
			int random = MathUtils.getRandomNumberInRange(getPlayer(), min, max);
			if (random == -1) return;

			add(player, random);

			Messenger.success(getPlayer(), "&aSuccessfully add player &d" + args[0]
					+ " &b" + random + " &apoint(s)");
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
				return completeLastWord("<number>", "<min>");
			}
			case 3 -> {
				return completeLastWord("<max>");
			}
		}
		return NO_COMPLETE;
	}

	private void add(Player player, int num) {
		Database database = Database.getInstance();
		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(player);
				data.addPoints(num);
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(player);
				data.addPoints(num);
			}
			case "MySQL" -> {
				try {
					MySQLData data = database.findPlayerDataByUUID(player.getUniqueId());
					data.addPoints(num);
					database.updatePlayerData(data);
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(add points)! " + e.getMessage());
				}
			}
		}
	}
}
