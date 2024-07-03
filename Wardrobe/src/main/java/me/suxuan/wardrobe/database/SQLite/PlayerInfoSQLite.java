package me.suxuan.wardrobe.database.SQLite;

import me.suxuan.wardrobe.Main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class PlayerInfoSQLite extends PlayerInfoDatabase {
	String dbname;

	public PlayerInfoSQLite(Main instance) {
		super(instance);
		dbname = "player_info";
	}

	public String CreatePlayerInfoTable = "CREATE TABLE IF NOT EXISTS player_info (" +
			"`player` varchar(32) NOT NULL UNIQUE," +
			"`uuid` varchar(255) NOT NULL UNIQUE," +
			"`unlock` int(16) NOT NULL," +
			"`selected` int(16)" +
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
			s.executeUpdate(CreatePlayerInfoTable);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initialize();
	}

}
