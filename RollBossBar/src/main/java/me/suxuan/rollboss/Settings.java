package me.suxuan.rollboss;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.ArrayList;
import java.util.List;

public final class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static List<String> TEXTS = new ArrayList<>();
	public static String WORLD;
	public static Integer INTERVAL;

	private static void init() {
		TEXTS = getStringList("Texts");
		WORLD = getString("world");
		INTERVAL = getInteger("Interval");
	}

}
