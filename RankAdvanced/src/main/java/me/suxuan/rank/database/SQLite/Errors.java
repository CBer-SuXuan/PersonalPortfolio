package me.suxuan.rank.database.SQLite;

/**
 * @author CBer_SuXuan
 * @className Errors
 * @date 2023/5/13 20:46
 * @description
 */
public class Errors {

	public static String sqlConnectionExecute() {
		return "Couldn't execute MySQL statement: ";
	}

	public static String sqlConnectionClose() {
		return "Failed to close MySQL connection: ";
	}

	public static String noSQLConnection() {
		return "Unable to retreive MYSQL connection: ";
	}

	public static String noTableFound() {
		return "Database Error: No Table Found";
	}

}
