package me.suxuan.wardrobe.settings;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

// Use to handle empty kit menu setting, including create file, add contents, remove contents...
@Getter
public class BorderSetting extends YamlConfig {

	@Getter
	private static final BorderSetting instance = new BorderSetting();
	private List<ItemStack> one;
	private List<ItemStack> first;
	private List<ItemStack> middle;
	private List<ItemStack> last;

	private BorderSetting() {
		this.setHeader("This file is for border config.\nDo not edit this file!");
		this.loadConfiguration(NO_DEFAULT, "menu/border.yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		this.one = this.getList("One", ItemStack.class);
		this.first = this.getList("First", ItemStack.class);
		this.middle = this.getList("Middle", ItemStack.class);
		this.last = this.getList("Last", ItemStack.class);
	}

	@Override
	protected void onSave() {
		this.set("One", this.one);
		this.set("First", this.first);
		this.set("Middle", this.middle);
		this.set("Last", this.last);
	}

	public void setOne(List<ItemStack> one) {
		this.one = one;
		this.save();
	}

	public void setFirst(List<ItemStack> first) {
		this.first = first;
		this.save();
	}

	public void setMiddle(List<ItemStack> middle) {
		this.middle = middle;
		this.save();
	}

	public void setLast(List<ItemStack> last) {
		this.last = last;
		this.save();
	}

}