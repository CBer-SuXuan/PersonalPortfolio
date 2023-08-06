package me.suxuan.jump;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class PlayerEvent implements Listener {

	private Set<UUID> prevPlayersOnGround = Sets.newHashSet();

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getVelocity().getY() > 0) {
			double jumpVelocity = 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP)) {
				jumpVelocity += (float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
			}
			if (event.getPlayer().getLocation().getBlock().getType() != Material.LADDER && prevPlayersOnGround.contains(player.getUniqueId())) {
				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
					randomEvent(player);
				}
			}
		}
		if (player.isOnGround()) {
			prevPlayersOnGround.add(player.getUniqueId());
		} else {
			prevPlayersOnGround.remove(player.getUniqueId());
		}
	}

	private void randomEvent(Player player) {
		Random random = new Random();
		String need = Settings.ALL_NEED.get(random.nextInt(Settings.ALL_NEED.size()));
		if (need.equals("TNT"))
			player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
		else if (need.split(";").length == 2) {
			ItemStack stack = new ItemStack(Material.matchMaterial(need.split(";")[0].toUpperCase()));
			stack.setAmount(Integer.parseInt(need.split(";")[1]));
			player.getInventory().addItem(stack);
		} else if (need.split(";").length == 3)
			player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(need.split(";")[0].toUpperCase()),
					Integer.parseInt(need.split(";")[2]) * 20, Integer.parseInt(need.split(";")[1])));
		else
			player.getWorld().spawnEntity(player.getLocation(), EntityType.fromName(need.toUpperCase()));
	}

}
