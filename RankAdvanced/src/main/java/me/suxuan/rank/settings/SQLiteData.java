package me.suxuan.rank.settings;

import lombok.Getter;
import me.suxuan.rank.Rank;
import me.suxuan.rank.database.SQLite.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author CBer_SuXuan
 * @className PlayerDatabase
 * @date 2023/5/13 22:33
 * @description
 */
public class SQLiteData {

	@Getter
	private static final Map<UUID, SQLiteData> playerData = new HashMap<>();

	private final String playerName;

	@Getter
	private String rank;
	@Getter
	private String vessel;
	@Getter
	private Integer vessel_int;
	@Getter
	private Integer tier;
	@Getter
	private Integer points;


	private SQLiteData(String playerName) {
		this.playerName = playerName;
		Player player = Bukkit.getPlayer(playerName);
		onLoad();
		if (player == null) return;
		if (rank == null) {
			Rank.getInstance().getDatabase().setData(
					player,
					0,
					0,
					0,
					"None"
			);
		}
	}

	protected void onLoad() {
		Database db = Rank.getInstance().getDatabase();
		this.rank = db.getDataString("Rank", playerName);
		this.vessel = db.getDataString("Vessel", playerName);
		this.vessel_int = db.getDataInt("Vessel_Int", playerName);
		this.tier = db.getDataInt("Tier", playerName);
		this.points = db.getDataInt("Points", playerName);
	}

	protected void onSave() {
		Database db = Rank.getInstance().getDatabase();
		db.setDataString("Rank", playerName, this.rank);
		db.setDataString("Vessel", playerName, this.vessel);
		db.setDataInt("Vessel_Int", playerName, this.vessel_int);
		db.setDataInt("Tier", playerName, this.tier);
		db.setDataInt("Points", playerName, this.points);
	}

	public void setRank(String rank) {
		this.rank = rank;
		this.onSave();
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
		this.onSave();
	}

	public void setVesselInt(Integer vessel_int) {
		this.vessel_int = vessel_int;
		this.onSave();
	}

	public void setTier(Integer tier) {
		this.tier = tier;
		this.onSave();
	}

	public void setPoints(Integer points) {
		this.points = points;
		this.onSave();
	}

	public void addPoints(Integer points) {
		Integer old = this.points;
		this.points = old + points;
		this.onSave();
	}

	public static SQLiteData from(Player player) {

		UUID uuid = player.getUniqueId();
		SQLiteData data = playerData.get(uuid);
		if (data == null) {
			data = new SQLiteData(player.getName());
		}
		return data;
	}

	public static void remove(Player player) {
		playerData.remove(player.getUniqueId());
	}

}
