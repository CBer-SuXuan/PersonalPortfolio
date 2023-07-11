package me.suxuan.rank.utils;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Messenger;

/**
 * @author CBer_SuXuan
 * @className MathUtils
 * @date 2023/5/13 18:12
 * @description
 */
public class MathUtils {

	public static int getRandomNumberInRange(Player player, int min, int max) {
		if (min >= max) {
			Messenger.error(player, "&cMax number must be greater than min!");
			return -1;
		}
		return (int) (Math.random() * ((max - min) + 1)) + min;
	}

}
