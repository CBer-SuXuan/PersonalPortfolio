package me.suxuan.quests.listener;

import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BreakEvent implements Listener {

	@EventHandler
	public void onBreakEvent(BlockBreakEvent event) {

		Player player = event.getPlayer();
		if (!player.getScoreboardTags().contains("break_quest")) return;

		PlayerCache cache = PlayerCache.from(player);
		List<String> break_list = cache.getBreak_quests();

		String block_name = event.getBlock().getType().toString();
		boolean finish_quest = true;

		for (String break_one : break_list) {
			// Check if not the need block
			if (!break_one.split(";")[0].toUpperCase().equals(block_name)) continue;
			for (String done : cache.getBreak_done()) {
				if (!done.split(";")[0].toUpperCase().equals(block_name)) continue;
				int old_num = Integer.parseInt(done.split(";")[1]);
				String new_string = done.split(";")[0] + ";" + (old_num + 1);
				cache.modifyBreakDone(done, new_string);
			}
		}

		for (String break_quest : break_list) {
			for (String break_done : cache.getBreak_done()) {
				if (!break_done.split(";")[0].equals(break_quest.split(";")[0])) continue;
				if (Integer.parseInt(break_done.split(";")[1]) < Integer.parseInt(break_quest.split(";")[1])) {
					finish_quest = false;
					cache.setBreakFinish(false);
					break;
				}
			}
		}
		// Check if all tasks about this event are done
		if (finish_quest) cache.setBreakFinish(true);

		// Check if the quest is done
		QuestsUtil.finishQuest(player, cache);
	}

}
