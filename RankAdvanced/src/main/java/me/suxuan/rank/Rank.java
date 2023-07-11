package me.suxuan.rank;

import me.suxuan.rank.database.SQLite.Database;
import me.suxuan.rank.database.SQLite.SQLite;
import me.suxuan.rank.settings.Settings;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.sql.Connection;

/**
 * @author CBer_SuXuan
 * @className Rank
 * @date 2023/5/7 16:01
 * @description Main class
 */
public final class Rank extends SimplePlugin {

	private Connection connection;
	private me.suxuan.rank.database.MySQL.Database database;

	private Database db;

	@Override
	protected void onPluginStart() {

		Messenger.setErrorPrefix("&7[&e&lR&b&lS&7] &4&l✕ ");
		Messenger.setSuccessPrefix("&7[&e&lR&b&lS&7] &2&l✔ ");
		Messenger.setWarnPrefix("&7[&e&lR&b&lS&7] &6&l! ");
		Common.setLogPrefix("&7[&e&lRank&b&lSystem&7] ");
		SimpleLocalization.Commands.NO_CONSOLE = "&cOnly player can use this command";
		LagCatcher.setPrintingMessages(false);

		switch (Settings.USE_DATABASE) {
			case "SQLite" -> {
				this.db = new SQLite(this);
				this.db.load();
			}
			case "MySQL" -> {
				this.database = new me.suxuan.rank.database.MySQL.Database();
				database.initializeDatabase();
			}
			case "None" -> {
				Common.log("&cYou must use a way to store data. " +
						"Go to settings.yml, choose one way and restart the server!");
			}
		}

	}

	@Override
	protected void onReloadablesStart() {
	}

	@Override
	protected void onPluginPreReload() {
	}

	public static Rank getInstance() {
		return (Rank) SimplePlugin.getInstance();
	}

	public Database getDatabase() {
		return this.db;
	}

	public me.suxuan.rank.database.MySQL.Database getMySQLDatabase() {
		return this.database;
	}

}
