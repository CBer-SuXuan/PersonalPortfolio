package me.suxuan.wardrobe.database.SQLite;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public abstract class WardrobeDatabase {
	Main plugin;
	Connection connection;
	// The name of the table we created back in SQLite class.
	public String table = "wardrobe";

	public WardrobeDatabase(Main instance) {
		plugin = instance;
	}

	public abstract Connection getSQLConnection();

	public abstract void load();

	public void initialize() {
		connection = getSQLConnection();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid = ?");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
		}
	}

	// ---------------------------------------------------------------------------------
	// Slot method
	// ---------------------------------------------------------------------------------

	public void updateSlot(Player player, int totalSlot) {
		Connection conn = null;
		PreparedStatement ps = null;
		for (int i = 1; i <= totalSlot; i++) {
			try {
				conn = getSQLConnection();
				ps = conn.prepareStatement("INSERT INTO " + table + " (uuid,id) SELECT ?,? WHERE NOT EXISTS(SELECT * FROM " + table + " where UUID = '" + player.getUniqueId().toString() + "' AND id = " + i + ");");
				ps.setString(1, player.getUniqueId().toString());
				ps.setInt(2, i);
				ps.executeUpdate();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			} finally {
				close(ps, conn);
			}
		}
	}

	public void addSlotWithId(Player player, int slotId) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + table + " (uuid,id) SELECT ?,? WHERE NOT EXISTS(SELECT * FROM " + table + " where UUID = '" + player.getUniqueId().toString() + "' AND id = " + slotId + ");");
			ps.setString(1, player.getUniqueId().toString());
			ps.setInt(2, slotId);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public List<Integer> getSlotList(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> slots = new ArrayList<>();
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + player.getName() + "';");
			rs = ps.executeQuery();
			while (rs.next()) slots.add(rs.getInt("id"));
			return slots;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);
		}
		return slots;
	}

	public Integer getFirstSlot(Player player) {
		List<Integer> selected = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM wardrobe WHERE uuid = '" + player.getUniqueId().toString() + "' ORDER BY id ASC;");
			rs = ps.executeQuery();
			if (rs.next()) return rs.getInt("id");
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);
		}
		return -1;
	}

	// ---------------------------------------------------------------------------------
	// Permission slot method
	// ---------------------------------------------------------------------------------

	public void addPermissionSlot(Player player, int permissionSlot) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + table + " (uuid,id) SELECT ?,? WHERE NOT EXISTS(SELECT * FROM " + table + " where UUID = '" + player.getUniqueId().toString() + "' AND id = " + permissionSlot + ");");
			ps.setString(1, player.getUniqueId().toString());
			ps.setInt(2, permissionSlot);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void removePermissionSlot(Player player, int permissionSlot) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("DELETE FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + permissionSlot + ";");
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void updatePermissionSlot(Player player, List<Integer> permissionSlots) {
		Connection conn = null;
		PreparedStatement ps = null;
		List<Integer> deleteList = getPermissionSlotList(player);

		deleteList.removeIf(permissionSlots::contains);

		for (int delete : deleteList) {
			try {
				conn = getSQLConnection();
				ps = conn.prepareStatement("DELETE FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + delete + ";");
				ps.executeUpdate();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			} finally {
				close(ps, conn);
			}
		}

		for (int slot : permissionSlots) {
			try {
				conn = getSQLConnection();
				ps = conn.prepareStatement("INSERT INTO " + table + " (uuid,id) SELECT ?,? WHERE NOT EXISTS(SELECT * FROM " + table + " where UUID = '" + player.getUniqueId().toString() + "' AND id = " + slot + ");");
				ps.setString(1, player.getUniqueId().toString());
				ps.setInt(2, slot);
				ps.executeUpdate();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			} finally {
				close(ps, conn);
			}
		}
	}

	public List<Integer> getPermissionSlotList(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Integer> slots = new ArrayList<>();
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "';");
			rs = ps.executeQuery();
			while (rs.next()) slots.add(rs.getInt("id"));
			return slots;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);

		}
		return slots;
	}

	// ---------------------------------------------------------------------------------
	// Get equip method
	// ---------------------------------------------------------------------------------

	public ItemStack getEquip(Player player, Equip equip, int slot_id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + slot_id + ";");
			rs = ps.executeQuery();
			if (rs.next()) {
				String get = "";
				switch (equip) {
					case HELMET -> get = "helmet";
					case CHESTPLATE -> get = "chestplate";
					case LEGGINGS -> get = "leggings";
					case BOOTS -> get = "boots";
				}
				return StringUtils.stringToItem(rs.getString(get), true);
			} else return null;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, rs, conn);

		}
		return null;
	}

	public List<ItemStack> getEquipAll(Player player, int slot_id) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ItemStack> stacks = new ArrayList<>();
		try {
			conn = getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + slot_id + ";");
			rs = ps.executeQuery();
			if (rs.next()) {
				stacks.add(StringUtils.stringToItem(rs.getString("helmet"), false));
				stacks.add(StringUtils.stringToItem(rs.getString("chestplate"), false));
				stacks.add(StringUtils.stringToItem(rs.getString("leggings"), false));
				stacks.add(StringUtils.stringToItem(rs.getString("boots"), false));
			}
			return stacks;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
		return stacks;
	}

	// ---------------------------------------------------------------------------------
	// Set equip method
	// ---------------------------------------------------------------------------------

	public void setEquip(Player player, Equip equip, String data, int slot_id) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			String prepare = "UPDATE " + table + " SET {Equip} = ? WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + slot_id + ";";
			switch (equip) {
				case HELMET -> prepare = prepare.replace("{Equip}", "helmet");
				case CHESTPLATE -> prepare = prepare.replace("{Equip}", "chestplate");
				case LEGGINGS -> prepare = prepare.replace("{Equip}", "leggings");
				case BOOTS -> prepare = prepare.replace("{Equip}", "boots");
			}
			ps = conn.prepareStatement(prepare);
			ps.setString(1, data);
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void setEquipAll(Player player, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, int slot_id) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			String prepare = "UPDATE " + table + " SET helmet = ?, chestplate = ?, leggings = ?, boots = ? WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = " + slot_id + ";";
			ps = conn.prepareStatement(prepare);
			ps.setString(1, StringUtils.itemToString(helmet));
			ps.setString(2, StringUtils.itemToString(chestplate));
			ps.setString(3, StringUtils.itemToString(leggings));
			ps.setString(4, StringUtils.itemToString(boots));
			ps.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	public void setEquipForAllSlot(HashMap<Integer, List<ItemStack>> manager, Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getSQLConnection();
			String prepare = "UPDATE " + table + " SET helmet = ?, chestplate = ?, leggings = ?, boots = ? WHERE uuid = '" + player.getUniqueId().toString() + "' AND id = ?;";
			for (int i : manager.keySet()) {
				ps = conn.prepareStatement(prepare);
				ps.setString(1, StringUtils.itemToString(manager.get(i).get(0)));
				ps.setString(2, StringUtils.itemToString(manager.get(i).get(1)));
				ps.setString(3, StringUtils.itemToString(manager.get(i).get(2)));
				ps.setString(4, StringUtils.itemToString(manager.get(i).get(3)));
				ps.setInt(5, i);
				ps.executeUpdate();
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			close(ps, conn);
		}
	}

	// ---------------------------------------------------------------------------------
	// Other method
	// ---------------------------------------------------------------------------------

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

	public enum Equip {
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS
	}
}