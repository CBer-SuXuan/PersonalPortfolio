package me.suxuan.quests.cache;

import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlStaticConfig;

public class ConfigSettings extends YamlStaticConfig {

	public static Integer MAIN_MENU_SIZE;
	public static String MAIN_MENU_TITLE;
	public static SerializedMap MAIN_MENU_SURROUND;
	public static SerializedMap MAIN_DIRECTION_BUTTON;

	public static Integer SUB_MENU_SIZE;
	public static String SUB_MENU_TITLE;
	public static SerializedMap SUB_MENU_SURROUND;
	public static SerializedMap SUB_MENU_BACK;
	public static SerializedMap SUB_MENU_DIRECTION_BUTTON;

	public static Integer CANCEL_MENU_SIZE;
	public static String CANCEL_MENU_TITLE;
	public static String CANCEL_MENU_MESSAGE;
	public static SerializedMap CANCEL_MENU_YES;
	public static SerializedMap CANCEL_MENU_NO;

	@Override
	protected void onLoad() {
		loadConfiguration("config.yml");
	}

	private static void init() {
		setPathPrefix("Menu");
		MAIN_MENU_SIZE = getInteger("Size");
		MAIN_MENU_TITLE = getString("Title");
		MAIN_MENU_SURROUND = getMap("Surround");
		MAIN_DIRECTION_BUTTON = new SerializedMap();
		MAIN_DIRECTION_BUTTON.put("next", getMap("Next_Page"));
		MAIN_DIRECTION_BUTTON.put("last", getMap("Last_Page"));
		MAIN_DIRECTION_BUTTON.put("no_next", getMap("No_Next_Page"));
		MAIN_DIRECTION_BUTTON.put("no_last", getMap("No_Last_Page"));

		setPathPrefix("Sub_Menu");
		SUB_MENU_SIZE = getInteger("Size");
		SUB_MENU_TITLE = getString("Title");
		SUB_MENU_SURROUND = getMap("Surround");
		SUB_MENU_BACK = getMap("Back_Main");
		SUB_MENU_DIRECTION_BUTTON = new SerializedMap();
		SUB_MENU_DIRECTION_BUTTON.put("next", getMap("Next_Page"));
		SUB_MENU_DIRECTION_BUTTON.put("last", getMap("Last_Page"));
		SUB_MENU_DIRECTION_BUTTON.put("no_next", getMap("No_Next_Page"));
		SUB_MENU_DIRECTION_BUTTON.put("no_last", getMap("No_Last_Page"));

		setPathPrefix("Cancel_Menu");
		CANCEL_MENU_SIZE = getInteger("Size");
		CANCEL_MENU_TITLE = getString("Title");
		CANCEL_MENU_MESSAGE = getString("Message");
		CANCEL_MENU_YES = getMap("Yes_Button");
		CANCEL_MENU_NO = getMap("No_Button");
	}
}
