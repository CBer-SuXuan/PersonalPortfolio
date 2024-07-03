package me.suxuan.wardrobe.utils;

import me.suxuan.wardrobe.settings.ButtonsSetting;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

public class StringUtils {

	// Convert item to string
	public static String itemToString(ItemStack itemStack) {
		YamlConfiguration config = new YamlConfiguration();
		config.set("i", itemStack);
		return config.saveToString();
	}

	// Convert string to item
	public static ItemStack stringToItem(String string, boolean wardrobe) {
		if (string == null && wardrobe) return ButtonsSetting.getInstance().getButtons().get(5);
		if (string == null) return CompMaterial.AIR.toItem();
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(string);
		} catch (Exception e) {
			System.out.println("Something wrong when change " + string + " to ItemStack");
			return null;
		}
		return config.getItemStack("i", null);
	}

}
