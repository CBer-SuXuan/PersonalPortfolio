package me.suxuan.villager.menu;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.collection.StrictMap;
import org.mineacademy.fo.menu.MenuContainer;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuClickLocation;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemSetMenu extends MenuContainer {

	private final String trade_name;
	private final Integer trade_number;
	private final Button makeButton;
	private final Button cancelButton;

	protected ItemSetMenu(String trade_name, Integer trade_number, boolean cancel) {
		super(null);
		VillagerShop.getEditing().add(trade_name);
		this.trade_number = trade_number;
		this.trade_name = trade_name;
		this.makeButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD)
						.skullUrl("http://textures.minecraft.net/texture/a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756")
						.name("&a&lMake this deal!"),
				HumanEntity::closeInventory);
		if (cancel)
			this.cancelButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD)
							.skullUrl("http://textures.minecraft.net/texture/27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065")
							.name("&c&lCancel making deal!"),
					player -> {
						TradeCache.from(trade_name).deleteDeals(trade_number);
						player.closeInventory();
					});
		else
			this.cancelButton = Button.makeSimple(ItemCreator.of(CompMaterial.PLAYER_HEAD)
							.skullUrl("http://textures.minecraft.net/texture/27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065")
							.name("&c&lDelete this deal!"),
					player -> {
						TradeCache.from(trade_name).deleteDeals(trade_number);
						player.closeInventory();
					});
		this.setSize(3 * 9);
		this.setTitle("&6&lMake a deal");
	}

	@Override
	protected boolean canEditItem(MenuClickLocation location, int slot, ItemStack clicked, ItemStack cursor, InventoryAction action) {
		if (location == MenuClickLocation.PLAYER_INVENTORY) return true;
		return slot == 11 || slot == 12 || slot == 15;
	}

	@Override
	protected ItemStack getDropAt(int slot) {

		TradeCache cache = TradeCache.from(trade_name);
		if (slot == 11) {
			if (cache.getDeals().getMap(String.valueOf(trade_number)).isEmpty()) return NO_ITEM;
			return cache.getDeals().getMap(String.valueOf(trade_number)).getItemStack("0");
		}
		if (slot == 12) {
			if (cache.getDeals().getMap(String.valueOf(trade_number)).isEmpty()) return NO_ITEM;
			if (cache.getDeals().getMap(String.valueOf(trade_number)).getString("1") == null)
				return NO_ITEM;
			return cache.getDeals().getMap(String.valueOf(trade_number)).getItemStack("1");
		}
		if (slot == 15) {
			if (cache.getDeals().getMap(String.valueOf(trade_number)).isEmpty()) return NO_ITEM;
			if (cache.getDeals().getMap(String.valueOf(trade_number)).getString("2").equals("null"))
				return NO_ITEM;
			return cache.getDeals().getMap(String.valueOf(trade_number)).getItemStack("2");
		}
		if (slot == 2 || slot == 3)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/e7742034f59db890c8004156b727c77ca695c4399d8e0da5ce9227cf836bb8e2")
					.name("&b&lPut demand here").make();
		if (slot == 6)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/7189c997db7cbfd632c2298f6db0c0a3dd4fc4cbbb278be75484fc82c6b806d4")
					.name("&9&lPut supply here").make();
		if (slot == 10)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/291ac432aa40d7e7a687aa85041de636712d4f022632dd5356c880521af2723a")
					.name("&b&lPut demand here").make();
		if (slot == 13)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/7a2c12cb22918384e0a81c82a1ed99aebdce94b2ec2754800972319b57900afb")
					.name("&b&lPut demand here").make();
		if (slot == 14)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/f716ca39511a96720c3379e771963befe224b60ecced9e693495975eda81de72")
					.name("&9&lPut supply here").make();
		if (slot == 16)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/98b5e9d5afac183f1f570c1b6ef5156c121c1efbd85527d8d79d0adeeb672485")
					.name("&9&lPut supply here").make();
		if (slot == 18)
			return this.cancelButton.getItem();
		if (slot == 20 || slot == 21)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/77334cddfab45d75ad28e1a47bf8cf5017d2f0982f6737da22d4972952510661")
					.name("&b&lPut demand here").make();
		if (slot == 24)
			return ItemCreator.of(CompMaterial.PLAYER_HEAD).skullUrl("http://textures.minecraft.net/texture/f3514f23d6b09e1840cdec7c0d6912dcd30f82110858c133a7f7778c728566dd")
					.name("&9&lPut supply here").make();

		List<Integer> surround = Arrays.asList(0, 1, 4, 5, 7, 8, 9, 17, 18, 19, 22, 23, 25);
		if (surround.contains(slot))
			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE).make();
		if (slot == 26)
			return makeButton.getItem();
		return NO_ITEM;
	}

	@Override
	protected void onMenuClose(StrictMap<Integer, ItemStack> items) {
		VillagerShop.getEditing().remove(trade_name);

		TradeCache cache = TradeCache.from(trade_name);
		if (cache.getDelete()) {
			TradeCache.from(trade_name).setDelete(false);
			DealSelectMenu.showTo(getViewer(), trade_name);
			return;
		}
		if ((items.get(11) == null && items.get(12) == null) || items.get(15) == null) {
			DealSelectMenu.showTo(getViewer(), trade_name);
			Common.tell(getViewer(), "&c&lSupply and demand must not empty, try again!");
			return;
		}
		SerializedMap map = new SerializedMap();

		if (items.get(11) == null) {
			map.put("0", items.get(12));
		} else if (items.get(12) == null) {
			map.put("0", items.get(11));
		} else {
			map.put("0", items.get(11));
			map.put("1", items.get(12));
		}
		map.put("2", items.get(15));
		if (cache.getDeals().getMap(String.valueOf(trade_number)).isEmpty()) {
			map.put("command", new ArrayList<>());
			cache.addDeals(map);
			cache.addDealsAmount();
		} else
			cache.changeDeals(map, trade_number);
		if (cache.getDeals().getMap(String.valueOf(trade_number)).getStringList("command").isEmpty()) {
			if (!map.containsKey("command"))
				map.put("command", new ArrayList<>());
			else
				map.override("command", new ArrayList<>());
			cache.changeDeals(map, trade_number);
		}
		DealSelectMenu.showTo(getViewer(), trade_name);
	}

	@Override
	protected boolean addInfoButton() {
		return false;
	}

	public static void showTo(Player player, String trade, Integer number, boolean cancel) {
		new ItemSetMenu(trade, number, cancel).displayTo(player);
	}
}
