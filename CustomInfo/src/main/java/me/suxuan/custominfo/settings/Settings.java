package me.suxuan.custominfo.settings;

import org.mineacademy.bfo.settings.YamlStaticConfig;

public class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static Boolean USE_ADVANCED;
	public static Integer MAX_PLAYERS;
	public static String PLAYER_MESSAGE;
	public static String MOTD;
	public static String HOVER_MESSAGE;
	public static String FRONT_MESSAGE;

	private static void init() {
		setPathPrefix(null);
		USE_ADVANCED = getBoolean("Use_Advanced");
		MAX_PLAYERS = getInteger("Max_Players");
		PLAYER_MESSAGE = getString("Player_Message");
		MOTD = getString("MOTD");
		HOVER_MESSAGE = getString("Hover_Message");
		FRONT_MESSAGE = getString("Front_Message");
	}


}
