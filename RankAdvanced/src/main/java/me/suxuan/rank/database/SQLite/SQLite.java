package me.suxuan.rank.database.SQLite;

import me.suxuan.rank.Rank;
import me.suxuan.rank.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * @author CBer_SuXuan
 * @className SQLite
 * @date 2023/5/13 20:47
 * @description
 */
public class SQLite extends Database {
	String dbname;

	public SQLite(Rank instance) {
		super(instance);
		dbname = Settings.SQLiteStore.FILE; // Set the table name here e.g player_kills
	}

	public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS player_data (" +
			"`UUID` varchar(64) NOT NULL," +
			"`Name` varchar(32) NOT NULL," +
			"`Points` int(11) NOT NULL," +
			"`Rank` varchar(32) NOT NULL," +
			"`Vessel` varchar(32) NOT NULL," +
			"`Vessel_Int` int(8) NOT NULL," +
			"`Tier` int(8) NOT NULL," +
			"PRIMARY KEY (`Name`)" +
			");";

	@Override
	public Connection getSQLConnection() {
		File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
		if (!dataFolder.exists()) {
			try {
				dataFolder.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
			}
		}
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
			return connection;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
		} catch (ClassNotFoundException ex) {
			plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
		}
		return null;
	}

	@Override
	public void load() {
		connection = getSQLConnection();
		try {
			Statement s = connection.createStatement();
			s.executeUpdate(SQLiteCreateTokensTable);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initialize();
	}
}
