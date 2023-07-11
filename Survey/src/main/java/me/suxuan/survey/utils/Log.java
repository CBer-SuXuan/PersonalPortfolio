package me.suxuan.survey.utils;

import org.bukkit.ChatColor;

public final class Log {

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}

