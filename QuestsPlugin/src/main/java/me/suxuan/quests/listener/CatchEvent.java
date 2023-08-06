package me.suxuan.quests.listener;

import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;

public class CatchEvent implements Listener {

	@EventHandler
	public void onCatchEvent(PlayerFishEvent event) {
		Entity entity = event.getCaught();
		Player player = event.getPlayer();
		if (!(entity instanceof Item)) return;

		if (!player.getScoreboardTags().contains("catch_quest")) return;

		PlayerCache cache = PlayerCache.from(player);
		List<String> catch_list = cache.getCatch_quests();

		String catch_name = ((Item) entity).getItemStack().getType().toString();
		boolean finish_quest = true;

		for (String catch_one : catch_list) {
			// Check if not the need block
			if (!catch_one.split(";")[0].toUpperCase().equals(catch_name)) continue;
			for (String done : cache.getCatch_done()) {
				if (!done.split(";")[0].toUpperCase().equals(catch_name)) continue;
				int old_num = Integer.parseInt(done.split(";")[1]);
				String new_string = done.split(";")[0] + ";" + (old_num + 1);
				cache.modifyCatchDone(done, new_string);
			}
		}

		for (String catch_quest : catch_list) {
			for (String catch_done : cache.getCatch_done()) {
				if (!catch_done.split(";")[0].equals(catch_quest.split(";")[0])) continue;
				if (Integer.parseInt(catch_done.split(";")[1]) < Integer.parseInt(catch_quest.split(";")[1])) {
					finish_quest = false;
					cache.setCatchFinish(false);
					break;
				}
			}
		}
		// Check if all tasks about this event are done
		if (finish_quest) cache.setCatchFinish(true);

		// Check if the quest is done
		QuestsUtil.finishQuest(player, cache);
	}

}
