package me.suxuan.heartcraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.mineacademy.fo.remain.CompAttribute;

public final class PlayerListener implements Listener {

	@EventHandler
	public void onEat(PlayerItemConsumeEvent event) {
		Material type = event.getItem().getType();
		Player player = event.getPlayer();
		CompAttribute max_health = CompAttribute.GENERIC_MAX_HEALTH;

		// 当前玩家血量
		Double start_heart = max_health.get(player);
		// 普通金苹果
		if (type.equals(Material.GOLDEN_APPLE)) {

			if (start_heart <= 40.0D) {
				max_health.set(player, max_health.get(player) + 4.0D);
				// 如果大于40就设置为40
				if (max_health.get(player) > 40.0D)
					max_health.set(player, 40.0D);
			}
			
		} else if (type.equals(Material.ENCHANTED_GOLDEN_APPLE)) {  // 附魔金苹果

			Double old_heart = max_health.get(player);
			if (old_heart > 20.0D && old_heart < 40.0D) {
				max_health.set(player, max_health.get(player) + 20.0D);
				double sub_heart = -20.0D + old_heart;
				max_health.set(player, 40.0D + sub_heart / 2.0D);
			} else if (old_heart >= 40.0D) {
				max_health.set(player, max_health.get(player) + 4.0D);
			} else {
				max_health.set(player, max_health.get(player) + 20.0D);
			}
			if (max_health.get(player) > 80.0D)
				max_health.set(player, 80.0D);

		}
	}

	@EventHandler
	public void onDead(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Double heart = CompAttribute.GENERIC_MAX_HEALTH.get(player);
		CompAttribute.GENERIC_MAX_HEALTH.set(player, heart - 2.0D);
		if (CompAttribute.GENERIC_MAX_HEALTH.get(player) < 12.0D)
			CompAttribute.GENERIC_MAX_HEALTH.set(player, 12.0D);
	}
}