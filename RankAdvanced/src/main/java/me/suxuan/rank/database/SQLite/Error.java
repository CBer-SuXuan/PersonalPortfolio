package me.suxuan.rank.database.SQLite;

import me.suxuan.rank.Rank;

import java.util.logging.Level;

/**
 * @author CBer_SuXuan
 * @className Error
 * @date 2023/5/13 20:46
 * @description
 */
public class Error {

	public static void execute(Rank plugin, Exception ex) {
		plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
	}

	public static void close(Rank plugin, Exception ex) {
		plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
	}

}
