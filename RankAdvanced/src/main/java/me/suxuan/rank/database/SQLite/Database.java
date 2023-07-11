package me.suxuan.rank.database.SQLite;

import me.suxuan.rank.Rank;
import me.suxuan.rank.settings.Settings;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author CBer_SuXuan
 * @className Database
 * @date 2023/5/13 20:44
 * @description
 */
public abstract class Database {

	Rank plugin;
	Connection connection;
	public String table = "player_data";
	public int tokens = 0;

	public Database(Rank instance) {
		plugin = instance;
	}

	public abstract Connection getSQLConnection();

	public abstract void load();

	public void initialize() {
		connection = getSQLConnection();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE Name = ?");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
		}
	}

	public int getDataInt(String need, String name) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE Name = '" + name + "';");
			rs = ps.executeQuery();
			return rs.getInt(need);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
		return 0;
	}

	public String getDataString(String need, String name) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE Name = '" + name + "';");
			rs = ps.executeQuery();
			return rs.getString(need);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
		return null;
	}

	public void setDataInt(String need, String name, Integer value) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET " + need + " = " + value + " WHERE Name = '" + name + "';");
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
	}

	public void setDataString(String need, String name, String value) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET " + need + " = '" + value + "' WHERE Name = '" + name + "';");
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
	}

	public void setData(Player player, Integer points, Integer tier, Integer vessel, String rank) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (UUID,Name,Points,Rank,Vessel,Vessel_Int,Tier) VALUES(?,?,?,?,?,?,?)");
			ps.setString(1, String.valueOf(player.getUniqueId()));
			ps.setString(2, player.getName());
			ps.setInt(3, points);
			ps.setString(4, rank);
			ps.setString(5, Settings.ALL_VESSEL_NAME.get(vessel));
			ps.setInt(6, vessel);
			ps.setInt(7, tier);
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
	}

	public void close(PreparedStatement ps, ResultSet rs) {
		try {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
		}
	}

}
