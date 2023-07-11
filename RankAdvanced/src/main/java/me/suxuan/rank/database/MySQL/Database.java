package me.suxuan.rank.database.MySQL;

import lombok.Getter;
import me.suxuan.rank.settings.MySQLData;
import me.suxuan.rank.settings.Settings;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;

import java.sql.*;
import java.util.UUID;

/**
 * @author CBer_SuXuan
 * @className Database
 * @date 2023/5/14 10:34
 * @description
 */
public class Database {

	private Connection connection = null;

	@Getter
	private final static Database instance = new Database();

	public Connection getConnection() {
		String url = "jdbc:mysql://" + Settings.MySQLStore.HOST + ":" + Settings.MySQLStore.PORT + "/" + Settings.MySQLStore.DATABASE;
		try {
			this.connection = DriverManager.getConnection(url, Settings.MySQLStore.USER, Settings.MySQLStore.PASSWORD);
		} catch (SQLException e) {
			Common.log("There are some errors of MySQL(connect)! " + e.getMessage());
			e.printStackTrace();
		}
		return this.connection;
	}

	public void initializeDatabase() {
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + Settings.MySQLStore.TABLE + "(" +
					"UUID VARCHAR(64) PRIMARY KEY , Name VARCHAR(64), Points INT(11), `Rank` VARCHAR(32), Vessel VARCHAR(32), Vessel_Int INT(8), Tier INT(8)" +
					");";
			statement.execute(sql);
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Common.log("There are some errors of MySQL(initial)! " + e.getMessage());
		}

	}

	public MySQLData findPlayerDataByUUID(UUID uuid) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + Settings.MySQLStore.TABLE + " WHERE UUID = '" + uuid + "';");
		ResultSet result = statement.executeQuery();
		if (result.next()) {
			int points = result.getInt("Points");
			int vessel_int = result.getInt("Vessel_Int");
			int tier = result.getInt("Tier");
			String rank = result.getString("Rank");
			String vessel = result.getString("Vessel");
			MySQLData data = new MySQLData(Bukkit.getPlayer(uuid).getName(), uuid, rank, vessel, vessel_int, tier, points);
			statement.close();
			connection.close();
			return data;
		}
		statement.close();
		connection.close();
		return null;
	}

	public void createPlayerData(MySQLData data) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("INSERT INTO " + Settings.MySQLStore.TABLE
				+ " (`UUID`, `Name`, `Points`, `Rank`, `Vessel`, `Vessel_Int`, `Tier`) VALUES (?,?,?,?,?,?,?);");
		statement.setString(1, data.getUuid().toString());
		statement.setString(2, data.getPlayerName());
		statement.setInt(3, data.getPoints());
		statement.setString(4, data.getRank());
		statement.setString(5, data.getVessel());
		statement.setInt(6, data.getVessel_int());
		statement.setInt(7, data.getTier());
		statement.executeUpdate();
		statement.close();
		connection.close();
	}

	public void updatePlayerData(MySQLData data) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("UPDATE " + Settings.MySQLStore.TABLE
				+ " SET `UUID` = ?, `Name` = ?, `Points` = ?, `Rank` = ?, `Vessel` = ?, `Vessel_Int` = ?, `Tier` = ? " + "WHERE `UUID` = ?;");
		statement.setString(1, data.getUuid().toString());
		statement.setString(2, data.getPlayerName());
		statement.setInt(3, data.getPoints());
		statement.setString(4, data.getRank());
		statement.setString(5, data.getVessel());
		statement.setInt(6, data.getVessel_int());
		statement.setInt(7, data.getTier());
		statement.setString(8, data.getUuid().toString());
		statement.executeUpdate();
		statement.close();
		connection.close();
	}

	public void addPlayerData(MySQLData data, int number) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement("UPDATE " + Settings.MySQLStore.TABLE
				+ " SET `UUID` = ?, `Name` = ?, `Points` = ?, `Rank` = ?, `Vessel` = ?, `Vessel_Int` = ?, `Tier` = ? " + "WHERE `UUID` = ?;");
		statement.setString(1, data.getUuid().toString());
		statement.setString(2, data.getPlayerName());
		statement.setInt(3, data.getPoints());
		statement.setString(4, data.getRank());
		statement.setString(5, data.getVessel());
		statement.setInt(6, data.getVessel_int());
		statement.setInt(7, data.getTier());
		statement.setString(8, data.getUuid().toString());
		statement.executeUpdate();
		statement.close();
		connection.close();
	}
}
