package me.suxuan.rank.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CBer_SuXuan
 * @className Settings
 * @date 2023/5/7 15:59
 * @description Initial settings.yml file
 */

public final class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("settings.yml");
	}

	public static final class LocalStore {
		public static Boolean USE;
		public static String FOLDER;

		private static void init() {
			setPathPrefix("Yaml");
			FOLDER = getString("Folder-Name");
			USE = getBoolean("Use");
		}
	}

	public static final class SQLiteStore {
		public static Boolean USE;
		public static String FILE;

		private static void init() {
			setPathPrefix("SQLite");
			USE = getBoolean("Use");
			FILE = getString("File-Name");
		}
	}

	public static final class MySQLStore {
		public static Boolean USE;
		public static String HOST;
		public static Integer PORT;
		public static String USER;
		public static String PASSWORD;
		public static String DATABASE;
		public static String TABLE;

		private static void init() {
			setPathPrefix("MySQL");
			USE = getBoolean("Use");
			HOST = getString("Host-Port").split(";")[0];
			PORT = Integer.valueOf(getString("Host-Port").split(";")[1]);
			USER = getString("User-Password").split(";")[0];
			PASSWORD = getString("User-Password").split(";")[1];
			DATABASE = getString("Database");
			TABLE = getString("Table");
		}
	}

	public static String USE_DATABASE;
	public static List<String> ALL_VESSEL_NAME;
	public static List<String> ALL_VESSEL_GROUP;
	public static String LEVEL_UP_MESSAGE;
	public static String TOP_MESSAGE;

	private static void init() {
		LocalStore.init();
		SQLiteStore.init();
		MySQLStore.init();

		if (LocalStore.USE) USE_DATABASE = "Local";
		else if (SQLiteStore.USE) USE_DATABASE = "SQLite";
		else if (MySQLStore.USE) USE_DATABASE = "MySQL";
		else USE_DATABASE = "None";

		List<String> vessel_name_list = new ArrayList<>();
		List<String> vessel_group_list = new ArrayList<>();
		setPathPrefix(null);
		for (String vessel : getStringList("Vessel-Name")) {
			vessel_name_list.add(vessel.split(";")[0]);
			vessel_group_list.add(vessel.split(";")[1]);
		}
		ALL_VESSEL_NAME = vessel_name_list;
		ALL_VESSEL_GROUP = vessel_group_list;
		LEVEL_UP_MESSAGE = getString("Up-Message");
		TOP_MESSAGE = getString("Top-Message");
	}

}
