package me.suxuan.backpack;

import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static List<ItemStack> ITEM_LIST = new ArrayList<>();
	public static List<Integer> SIZE_LIST = new ArrayList<>();
	public static List<String> COMMAND_LIST = new ArrayList<>();
	public static List<String> TITLE_LIST = new ArrayList<>();

	private static void init() {
		ITEM_LIST.clear();
		SIZE_LIST.clear();
		COMMAND_LIST.clear();
		TITLE_LIST.clear();
		List<String> prefixes = new ArrayList<>();
		for (Map.Entry<String, Object> item : getMap("Items")) {
			prefixes.add(item.getKey());
		}
		for (String prefix : prefixes) {
			setPathPrefix("Items." + prefix);
			if (getInteger("Model_Data") == 0)
				ITEM_LIST.add(ItemCreator.of(CompMaterial.fromString(getString("Type")))
						.name(getString("Name")).lore(getStringList("Lore")).make());
			else
				ITEM_LIST.add(ItemCreator.of(CompMaterial.fromString(getString("Type")))
						.name(getString("Name")).lore(getStringList("Lore")).modelData(getInteger("Model_Data")).make());
			SIZE_LIST.add(getInteger("Size"));
			COMMAND_LIST.add(getString("Command_Name"));
			TITLE_LIST.add(getString("Menu_Title"));
		}

	}
}
