package me.suxuan.recordblock.utils;

import java.util.Calendar;

public class TimeUtil {

	public static int getHours() {
		Calendar instance = Calendar.getInstance();
		return instance.get(Calendar.HOUR_OF_DAY);
	}

	public static String getDay() {
		Calendar instance = Calendar.getInstance();
		return instance.get(Calendar.YEAR) + "-"
				+ (instance.get(Calendar.MONTH) + 1) + "-"
				+ instance.get(Calendar.DATE);
	}

	public static String handleTime() {
		int hours = getHours();
		if (hours >= 0 && hours <= 5)
			return getDay() + "_0-6";
		else if (hours >= 6 && hours <= 11)
			return getDay() + "_6-12";
		else if (hours >= 12 && hours <= 17)
			return getDay() + "_12-18";
		else if (hours >= 18 && hours <= 23)
			return getDay() + "_18-24";
		return "";
	}

}
