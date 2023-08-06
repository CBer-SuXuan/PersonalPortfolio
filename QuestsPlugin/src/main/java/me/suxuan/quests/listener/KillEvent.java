package me.suxuan.quests.listener;

import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class KillEvent implements Listener {

	@EventHandler
	public void onKillEvent(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();

		if (player == null) return;
		if (!player.getScoreboardTags().contains("kill_quest")) return;

		PlayerCache cache = PlayerCache.from(player);
		List<String> kill_list = cache.getKill_quests();

		String entity_name = event.getEntity().getType().name();
		boolean finish_quest = true;

		for (String kill_one : kill_list) {
			// Check if not the need block
			if (!kill_one.split(";")[0].toUpperCase().equals(entity_name)) continue;
			for (String done : cache.getKill_done()) {
				if (!done.split(";")[0].toUpperCase().equals(entity_name)) continue;
				int old_num = Integer.parseInt(done.split(";")[1]);
				String new_string = done.split(";")[0] + ";" + (old_num + 1);
				cache.modifyKillDone(done, new_string);
			}
		}

		for (String kill_quest : kill_list) {
			for (String kill_done : cache.getKill_done()) {
				if (!kill_done.split(";")[0].equals(kill_quest.split(";")[0])) continue;
				if (Integer.parseInt(kill_done.split(";")[1]) < Integer.parseInt(kill_quest.split(";")[1])) {
					finish_quest = false;
					cache.setKillFinish(false);
					break;
				}

			}
		}

		// Check if all tasks about this event are done
		if (finish_quest) cache.setKillFinish(true);

		// Check if the quest is done
		QuestsUtil.finishQuest(player, cache);
	}

}
