package me.suxuan.joinreward;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.List;

public class Settings extends YamlStaticConfig {

	public static List<String> GIVE_ITEMS;

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	private static void init() {
		GIVE_ITEMS = getStringList("Give_Items");
	}

}
