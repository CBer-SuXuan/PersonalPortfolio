package me.suxuan.wardrobe.manager;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.database.SQLite.WardrobeDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WardrobeManager {
	private static final Map<UUID, WardrobeManager> wardrobeManager = new HashMap<>();

	private final UUID uuid;
	private HashMap<Integer, List<ItemStack>> playerWardrobe = new HashMap<>();

	private WardrobeManager(UUID uuid) {
		this.uuid = uuid;
		WardrobeDatabase wardrobeDatabase = Main.getInstance().getWardrobeDatabase();
		for (Integer id : wardrobeDatabase.getPermissionSlotList(Bukkit.getPlayer(uuid))) {
			playerWardrobe.put(id, wardrobeDatabase.getEquipAll(Bukkit.getPlayer(uuid), id));
		}
	}

	public void updateSlot(int totalSlot) {
		for (int i = 0; i < totalSlot; i++) {
			if (playerWardrobe.containsKey(i)) continue;
			playerWardrobe.put(i, Arrays.asList(null, null, null, null));
		}
	}

	public void addPermissionSlot(int permissionSlot) {
		if (playerWardrobe.containsKey(permissionSlot)) return;
		playerWardrobe.put(permissionSlot, Arrays.asList(null, null, null, null));
	}

	public void removePermissionSlot(int permissionSlot) {
		if (!playerWardrobe.containsKey(permissionSlot)) return;
		playerWardrobe.remove(permissionSlot);
	}

	public void updatePermissionSlot(List<Integer> permissionSlots) {
		List<Integer> needDelete = new ArrayList<>();
		for (int i : playerWardrobe.keySet())
			if (!permissionSlots.contains(i)) needDelete.add(i);
		for (int i : permissionSlots) {
			playerWardrobe.putIfAbsent(i, Arrays.asList(null, null, null, null));
		}
		for (int i : needDelete)
			playerWardrobe.remove(i);
	}

	public List<Integer> getPermissionSlotList() {
		return new ArrayList<>(playerWardrobe.keySet());
	}

	public Integer getFirstSlot() {
		if (playerWardrobe.isEmpty()) return -1;
		else return Collections.min(playerWardrobe.keySet());
	}

	public ItemStack getEquip(Equip equip, int slot_id) {
		int i = 0;
		switch (equip) {
			case HELMET -> i = 0;
			case CHESTPLATE -> i = 1;
			case LEGGINGS -> i = 2;
			case BOOTS -> i = 3;
		}
		return playerWardrobe.get(slot_id).get(i);
	}

	public List<ItemStack> getEquipAll(int slot_id) {
		return playerWardrobe.get(slot_id);
	}

	public void setEquipAll(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, int slot_id) {
		List<ItemStack> equips = new ArrayList<>();
		equips.add(helmet);
		equips.add(chestplate);
		equips.add(leggings);
		equips.add(boots);
		playerWardrobe.put(slot_id, equips);
	}

	private void saveToDatabase() {
		Main.getInstance().getWardrobeDatabase().updatePermissionSlot(Bukkit.getPlayer(uuid), new ArrayList<>(playerWardrobe.keySet()));
		Main.getInstance().getWardrobeDatabase().setEquipForAllSlot(playerWardrobe, Bukkit.getPlayer(uuid));
	}

	public static WardrobeManager from(Player player) {
		UUID uuid = player.getUniqueId();
		WardrobeManager data = wardrobeManager.get(uuid);

		if (data == null) {
			data = new WardrobeManager(uuid);
			wardrobeManager.put(uuid, data);
		}
		return data;
	}

	public static void remove(Player player) {
		wardrobeManager.get(player.getUniqueId()).saveToDatabase();
		wardrobeManager.remove(player.getUniqueId());
	}

	public static void removeWithoutSave(Player player) {
		wardrobeManager.remove(player.getUniqueId());
	}

	public enum Equip {
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS
	}

	@Override
	public String toString() {
		return "WardrobeManager{" +
				"uuid=" + uuid +
				", playerWardrobe=" + playerWardrobe.keySet() +
				'}';
	}
}
