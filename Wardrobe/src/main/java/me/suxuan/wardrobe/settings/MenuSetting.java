package me.suxuan.wardrobe.settings;

import lombok.Getter;
import org.mineacademy.fo.settings.YamlConfig;

// Use to handle main setting, including create file, add contents, remove contents...
@Getter
public class MenuSetting extends YamlConfig {

	@Getter
	private static final MenuSetting instance = new MenuSetting();

	private String wardrobeTitle;
	private String kitSetTitle;
	private Boolean slotPermission;

	private MenuSetting() {
		this.setHeader("This file is for menu config.\nYou can edit this file both here and in game!");
		this.loadConfiguration(NO_DEFAULT, "menu_config.yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		this.wardrobeTitle = this.getString("Wardrobe_Title", "&9My Wardrobe ({current}/{total})");
		this.kitSetTitle = this.getString("Kit_Set_Title", "&9Set kit");
		this.slotPermission = this.getBoolean("Slot_Permission", false);
	}

	@Override
	protected void onSave() {
		this.set("Wardrobe_Title", this.wardrobeTitle);
		this.set("Kit_Set_Title", this.kitSetTitle);
		this.set("Slot_Permission", this.slotPermission);
	}

	public void setWardrobeTitle(String title) {
		this.wardrobeTitle = title;
		this.save();
	}

	public void setKitSetTitle(String title) {
		this.kitSetTitle = title;
		this.save();
	}

	public void setSlotPermission(Boolean slotPermission) {
		this.slotPermission = slotPermission;
		this.save();
	}

}
