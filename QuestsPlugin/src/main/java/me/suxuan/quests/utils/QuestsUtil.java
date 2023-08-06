package me.suxuan.quests.utils;

import me.suxuan.quests.cache.ConfigSettings;
import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.cache.QuestsCache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompSound;

import java.util.List;

public class QuestsUtil {

	// Start one quest
	public static boolean startRequest(String quest, Player player) {

		PlayerCache player_cache = PlayerCache.from(player);
		QuestsCache cache = QuestsCache.from(quest);

		player_cache.setDoingQuest(quest);

		// Send sound
		CompSound sound = CompSound.fromName(QuestsCache.from(player_cache.getDoing_quest()).getStart().getString("Sound"));
		if (sound == null) {
			Common.tellNoPrefix(player, "&c&LThe sound is wrong! In " + player_cache.getDoing_quest() + ".yml file" +
					" of \"Start.Sound: " + QuestsCache.from(player_cache.getDoing_quest()).getStart().getString("Sound") + "\"!");
			player_cache.setDoingQuest("");
			player.closeInventory();
			return false;
		}
		sound.play(player);
		// Send message
		Common.tellNoPrefix(player, QuestsCache.from(player_cache.getDoing_quest()).getStart().getString("Message"));

		List<String> require_type = cache.getActions().keySet().stream().toList();
		player_cache.setDoingTypes(require_type);

		if (require_type.contains("Break")) {
			player.addScoreboardTag("break_quest");
			player_cache.setBreakQuests(cache.getActions().getStringList("Break"));
			for (String one : cache.getActions().getStringList("Break")) {
				player_cache.addBreakDone(one.split(";")[0] + ";0");
			}
		}
		if (require_type.contains("Catch")) {
			player.addScoreboardTag("catch_quest");
			player_cache.setCatchQuests(cache.getActions().getStringList("Catch"));
			for (String one : cache.getActions().getStringList("Catch")) {
				player_cache.addCatchDone(one.split(";")[0] + ";0");
			}
		}
		if (require_type.contains("Kill")) {
			player.addScoreboardTag("kill_quest");
			player_cache.setKillQuests(cache.getActions().getStringList("Kill"));
			for (String one : cache.getActions().getStringList("Kill")) {
				player_cache.addKillDone(one.split(";")[0] + ";0");
			}
		}
		return true;
	}

	// Add done quest and remove tag
	public static void doneRequest(String quest, Player player) {
		PlayerCache.from(player).doneQuest(quest);

		player.removeScoreboardTag("break_quest");
		player.removeScoreboardTag("catch_quest");
		player.removeScoreboardTag("kill_quest");
	}

	// Reset quest and remove tag
	public static void resetRequest(String quest, Player player) {
		PlayerCache.from(player).resetBasic();

		player.removeScoreboardTag("break_quest");
		player.removeScoreboardTag("catch_quest");
		player.removeScoreboardTag("kill_quest");
	}

	// Use only for "/qt reset" command
	public static void resetAll(Player player) {
		PlayerCache.from(player).resetAll();

		player.removeScoreboardTag("break_quest");
		player.removeScoreboardTag("catch_quest");
		player.removeScoreboardTag("kill_quest");
	}

	// Check if all requirement satisfied
	public static boolean checkIfFinish(Player player) {
		PlayerCache cache = PlayerCache.from(player);
		boolean break_done = true;
		boolean catch_done = true;
		boolean kill_done = true;
		for (String type : cache.getDoing_types()) {
			if (type.contains("Break"))
				break_done = cache.getBreak_finish();
			if (type.contains("Catch"))
				catch_done = cache.getCatch_finish();
			if (type.contains("Kill"))
				kill_done = cache.getKill_finish();
		}
		return break_done && catch_done && kill_done;
	}

	// run reward
	public static void rewardQuest(String quest, Player player) {
		for (String reward : QuestsCache.from(quest).getRewards())
			Common.dispatchCommand(player, reward);
	}

	// Finish the quest
	public static void finishQuest(Player player, PlayerCache cache) {

		if (cache.getDoing_quest().equals("")) return;
		if (QuestsUtil.checkIfFinish(player)) {
			// Send sound
			CompSound sound = CompSound.fromName(QuestsCache.from(cache.getDoing_quest()).getComplete().getString("Sound"));
			if (sound == null) {
				Common.tellNoPrefix(player, "&c&LThe sound is wrong! In " + cache.getDoing_quest() + ".yml file" +
						" of \"Complete.Sound: " + QuestsCache.from(cache.getDoing_quest()).getComplete().getString("Sound") + "\"!");
				return;
			}
			sound.play(player);
			// Send message
			Common.tellNoPrefix(player, QuestsCache.from(cache.getDoing_quest()).getComplete().getString("Message"));
			// Give reward
			QuestsUtil.rewardQuest(QuestsCache.from(cache.getDoing_quest()).getQuest_file_name(), player);

			if (QuestsCache.from(cache.getDoing_quest()).getRepeatable())
				QuestsUtil.resetRequest(cache.getDoing_quest(), player);
			else
				// Done quest
				QuestsUtil.doneRequest(cache.getDoing_quest(), player);
		}

	}

	// Cancel the quest
	public static void cancelQuest(String quest, Player player) {

		Common.tellNoPrefix(player, ConfigSettings.CANCEL_MENU_MESSAGE.replace("{quest_name}", quest));
		PlayerCache.from(player).resetBasic();
		player.removeScoreboardTag("break_quest");
		player.removeScoreboardTag("catch_quest");
		player.removeScoreboardTag("kill_quest");

	}

}
