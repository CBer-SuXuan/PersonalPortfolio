package me.suxuan.wardrobe.database.SQLite;

import me.suxuan.wardrobe.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class PlayerInfoDatabase {
	Main plugin;
	Connection connection;
	// The name of the table we created back in SQLite class.
	public String table = "player_info";

	public PlayerInfoDatabase(Main instance) {
		plugin = instance;
	}

	public abstract Connection getSQLConnection();

	public abstract void load();

	public void initialize() {
		connection = getSQLConnection();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
		}
	}

	// ---------------------------------------------------------------------------------
	// Unlock method
	// ---------------------------------------------------------------------------------

	public int getUnlock(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
			rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("unlock");
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);

		}
		return -1;
	}

	public void setUnlock(Player player, int unlockAmount) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET unlock = ? WHERE uuid = '" + player.getUniqueId().toString() + "';");
			ps.setInt(1, unlockAmount);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void setUnlockByPermission(Player player, int permissionSlots) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET unlock = ? WHERE uuid = '" + player.getUniqueId().toString() + "';");
			ps.setInt(1, permissionSlots);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void addUnlock(Player player, int amountToAdd) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET unlock = unlock + ? WHERE uuid = '" + player.getUniqueId().toString() + "';");
			ps.setInt(1, amountToAdd);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	// ---------------------------------------------------------------------------------
	// Selected method
	// ---------------------------------------------------------------------------------

	public int getSelected(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
			rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("selected");
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);
		}
		return -1;
	}

	public void setSelected(Player player, int selected) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET selected = ? WHERE uuid = '" + player.getUniqueId().toString() + "';");
			ps.setInt(1, selected);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	// ---------------------------------------------------------------------------------
	// Other method
	// ---------------------------------------------------------------------------------

	// Store basic player info when player join the game. Only run once for every player.
	public void initialPlayerData(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + table + " (player,uuid,unlock,selected) SELECT ?,?,0,-1 WHERE NOT EXISTS(SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "');");
			ps.setString(1, player.getName());
			ps.setString(2, player.getUniqueId().toString());
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void close(PreparedStatement ps, ResultSet rs, Connection conn) {
		try {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
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

	public void close(PreparedStatement ps, Connection conn) {
		try {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
		}
	}
}