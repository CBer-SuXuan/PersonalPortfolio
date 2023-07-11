package me.suxuan.changeserver.settings;

import org.mineacademy.bfo.settings.YamlStaticConfig;

public class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static String TO_SERVER;
	public static String MESSAGE;

	private static void init() {
		setPathPrefix(null);
		TO_SERVER = getString("To_Server");
		MESSAGE = getString("Tell_Message");
	}


}
