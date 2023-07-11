package me.suxuan.rank.utils;

import me.suxuan.rank.Rank;
import me.suxuan.rank.database.MySQL.Database;
import me.suxuan.rank.settings.LocalData;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.SQLiteData;
import me.suxuan.rank.settings.Settings;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.sql.SQLException;

import static me.suxuan.rank.utils.MessageUtils.handleLevelUpMessage;
import static me.suxuan.rank.utils.MessageUtils.vesselToString;

/**
 * @author CBer_SuXuan
 * @className PlayerUtils
 * @date 2023/5/12 21:07
 * @description
 */
public class PlayerUtils {

	public static int getPointNeed(Player player) {
		int base = 100;

		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(player);
				int now_vessel = data.getVessel_int();
				int now_tier = data.getTier();
				if (now_vessel != 0) {
					int loop = (now_vessel - 1) * 10 + now_tier;
					for (int i = 1; i <= loop; i++) {
						int add = 100 + 10 * i;
						base += add;
					}
				}
				break;
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(player);
				int now_vessel = data.getVessel_int();
				int now_tier = data.getTier();
				if (now_vessel != 0) {
					int loop = (now_vessel - 1) * 10 + now_tier;
					for (int i = 1; i <= loop; i++) {
						int add = 100 + 10 * i;
						base += add;
					}
				}
				break;
			}
			case "MySQL" -> {
				try {
					MySQLData data = Rank.getInstance().getMySQLDatabase().findPlayerDataByUUID(player.getUniqueId());
					int now_vessel = data.getVessel_int();
					int now_tier = data.getTier();
					if (now_vessel != 0) {
						int loop = (now_vessel - 1) * 10 + now_tier;
						for (int i = 1; i <= loop; i++) {
							int add = 100 + 10 * i;
							base += add;
						}
					}
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(points need)! " + e.getMessage());
				}
				break;
			}
		}
		return base;
	}

	public static void levelUp(Player player) {

		Common.setTellPrefix(null);

		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(player);
				int now_tier = data.getTier();
				int now_vessel = data.getVessel_int();
				if (checkTopLevel(now_tier, now_vessel, player)) return;
				if (now_tier == 10 || now_tier == 0) {
					data.setTier(1);
					data.setVesselInt(now_vessel + 1);
					data.setVessel(vesselToString(now_vessel + 1));
					data.setRank(getRank(now_vessel + 1));
					if (checkGroupExit(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1))) {
						removePlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel));
						addPlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1));
					}
					Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel + 1, 1));
				} else {
					data.setTier(now_tier + 1);
					data.setVessel(vesselToString(now_vessel));
					data.setRank(getRank(now_vessel));
					Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel, now_tier + 1));
				}
				data.setPoints(0);
				break;
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(player);
				int now_tier = data.getTier();
				int now_vessel = data.getVessel_int();
				if (checkTopLevel(now_tier, now_vessel, player)) return;
				if (now_tier == 10 || now_tier == 0) {
					data.setTier(1);
					data.setVesselInt(now_vessel + 1);
					data.setVessel(vesselToString(now_vessel + 1));
					data.setRank(getRank(now_vessel + 1));
					if (checkGroupExit(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1))) {
						removePlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel));
						addPlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1));
					}
					Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel + 1, 1));
				} else {
					data.setTier(now_tier + 1);
					data.setVessel(vesselToString(now_vessel));
					data.setRank(getRank(now_vessel));
					Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel, now_tier + 1));
				}
				data.setPoints(0);
				break;
			}
			case "MySQL" -> {
				try {
					MySQLData data = Rank.getInstance().getMySQLDatabase().findPlayerDataByUUID(player.getUniqueId());
					int now_tier = data.getTier();
					int now_vessel = data.getVessel_int();
					if (checkTopLevel(now_tier, now_vessel, player)) return;
					if (now_tier == 10 || now_tier == 0) {
						data.setTier(1);
						data.setVessel_int(now_vessel + 1);
						data.setVessel(vesselToString(now_vessel + 1));
						data.setRank(getRank(now_vessel + 1));
						if (checkGroupExit(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1))) {
							removePlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel));
							addPlayerGroup(player, Settings.ALL_VESSEL_GROUP.get(now_vessel + 1));
						}
						Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel + 1, 1));
					} else {
						data.setTier(now_tier + 1);
						data.setVessel(vesselToString(now_vessel));
						data.setRank(getRank(now_vessel));
						Common.tell(player, handleLevelUpMessage(Settings.LEVEL_UP_MESSAGE, now_vessel, now_tier + 1));
					}
					data.setPoints(0);
					Rank.getInstance().getMySQLDatabase().updatePlayerData(data);
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(level up)! " + e.getMessage());
				}
				break;
			}
		}
	}

	public static boolean checkTopLevel(int now_tier, int now_vessel, Player player) {
		if (now_vessel == 20 && now_tier == 10) {
			Common.tell(player, Settings.TOP_MESSAGE);
			return true;
		}
		return false;
	}

	public static boolean checkNeedUp(Player player) {

		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(player);
				return data.getPoints() >= getPointNeed(player);
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(player);
				return data.getPoints() >= getPointNeed(player);
			}
			case "MySQL" -> {
				try {
					MySQLData data = Rank.getInstance().getMySQLDatabase().findPlayerDataByUUID(player.getUniqueId());
					return data.getPoints() >= getPointNeed(player);
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(need up?)! " + e.getMessage() + e.getSQLState());
				}
			}
		}
		return false;

	}

	public static void checkUpdateRank(Player player) {
		switch (Settings.USE_DATABASE) {
			case "Local" -> {
				LocalData data = LocalData.from(player);
				int now_vessel = data.getVessel_int();
				String now_rank = data.getRank();
				if (!getRank(now_vessel).equals(now_rank)) data.setRank(getRank(now_vessel));
				break;
			}
			case "SQLite" -> {
				SQLiteData data = SQLiteData.from(player);
				int now_vessel = data.getVessel_int();
				String now_rank = data.getRank();
				if (getRank(now_vessel).equals(now_rank)) data.setRank(getRank(now_vessel));
				break;
			}
			case "MySQL" -> {
				try {
					MySQLData data = Rank.getInstance().getMySQLDatabase().findPlayerDataByUUID(player.getUniqueId());
					Database database = Database.getInstance();
					int now_vessel = data.getVessel_int();
					String now_rank = data.getRank();
					if (!getRank(now_vessel).equals(now_rank)) {
						data.setRank(getRank(now_vessel));
						database.updatePlayerData(data);
					}
					break;
				} catch (SQLException e) {
					Common.log("There are some errors of MySQL(update rank)! " + e.getMessage());
				}
				break;
			}
		}
	}

	public static boolean checkGroupExit(Player player, String group) {
		if (group.equals("none")) return false;
		LuckPerms luckPerms = LuckPermsProvider.get();
		return !(luckPerms.getGroupManager().getGroup(group) == null);
	}

	public static void addPlayerGroup(Player player, String add_group) {
		LuckPerms luckPerms = LuckPermsProvider.get();
		User user = luckPerms.getUserManager().getUser(player.getUniqueId());
		if (user == null) return;
		if (add_group.equals("none")) return;
		InheritanceNode node = InheritanceNode.builder(add_group).value(true).build();
		user.data().add(node);
		luckPerms.getUserManager().saveUser(user);
	}

	public static void removePlayerGroup(Player player, String add_group) {
		LuckPerms luckPerms = LuckPermsProvider.get();
		User user = luckPerms.getUserManager().getUser(player.getUniqueId());
		if (user == null) return;
		if (add_group.equals("none")) return;
		InheritanceNode node = InheritanceNode.builder(add_group).value(true).build();
		user.data().remove(node);
		luckPerms.getUserManager().saveUser(user);
	}

	public static String getRank(int vessel) {

		switch (vessel) {
			case 1, 2 -> {
				return "Third rate warrior";
			}
			case 3, 4 -> {
				return "Second rate warrior";
			}
			case 5, 6 -> {
				return "First rate warrior";
			}
			case 7, 8 -> {
				return "Master";
			}
			case 9, 10 -> {
				return "Peerless Master";
			}
			case 11, 12, 13 -> {
				return "Grandmaster";
			}
			case 14, 15, 16 -> {
				return "Peak master";
			}
			case 17, 18, 19 -> {
				return "Transcendent master";
			}
			case 20 -> {
				return "Divine Being";
			}
			default -> {
				return "None";
			}
		}
	}

}
