package me.suxuan.rank.utils;

import me.suxuan.rank.settings.Settings;

import java.util.List;

/**
 * @author CBer_SuXuan
 * @className MessageUtils
 * @date 2023/5/13 16:53
 * @description
 */
public class MessageUtils {

	public static String vesselToString(int vessel_int) {

		List<String> all = Settings.ALL_VESSEL_NAME;
		return all.get(vessel_int);

	}

	public static String handleLevelUpMessage(String message, int new_vessel, int tier) {
		return message.replace("{vessel}", vesselToString(new_vessel))
				.replace("{tier}", String.valueOf(tier));
	}

}
