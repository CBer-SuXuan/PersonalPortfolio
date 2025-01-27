package me.suxuan.wardrobe.settings;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

// Use to handle empty kit menu setting, including create file, add contents, remove contents...
@Getter
public class EmptySetting extends YamlConfig {

	@Getter
	private static final EmptySetting instance = new EmptySetting();
	private List<ItemStack> menu;

	private EmptySetting() {
		this.setHeader("This file is for empty kit menu config.\nDo not edit this file!");
		this.loadConfiguration(NO_DEFAULT, "menu/empty_kit.yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		this.menu = this.getList("Menu", ItemStack.class);
	}

	@Override
	protected void onSave() {
		this.set("Menu", this.menu);
	}

	public void setMenu(List<ItemStack> menu) {
		this.menu = menu;
		this.save();
	}

}
