package me.suxuan.rank.settings;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author CBer_SuXuan
 * @className PlayerData
 * @date 2023/5/12 20:23
 * @description
 */
@Getter
public final class LocalData extends YamlConfig {

	@Getter
	private static final Map<UUID, LocalData> playerData = new HashMap<>();

	private final String playerName;
	private final UUID uuid;

	private String rank;
	private String vessel;
	private Integer vessel_int;
	private Integer tier;
	private Integer points;

	private LocalData(String playerName, UUID uuid) {
		this.playerName = playerName;
		this.uuid = uuid;
		this.setHeader("Player name : " + playerName);
		this.loadConfiguration(NO_DEFAULT, Settings.LocalStore.FOLDER + "/" + uuid + ".yml");
		if (rank == null) {
			setRank("None");
			setVessel("Mortal");
			setVesselInt(0);
			setTier(0);
			setPoints(0);
		}
		this.save();
	}

	@Override
	protected void onLoad() {
		this.rank = getString("Rank");
		this.vessel = getString("Vessel");
		this.vessel_int = getInteger("Vessel_Int");
		this.tier = getInteger("Tier");
		this.points = getInteger("Points");

	}

	@Override
	protected void onSave() {
		this.set("Rank", this.rank);
		this.set("Vessel", this.vessel);
		this.set("Vessel_Int", this.vessel_int);
		this.set("Tier", this.tier);
		this.set("Points", this.points);
	}

	public void setRank(String rank) {
		this.rank = rank;
		this.save();
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
		this.save();
	}

	public void setVesselInt(Integer vessel_int) {
		this.vessel_int = vessel_int;
		this.save();
	}

	public void setTier(Integer tier) {
		this.tier = tier;
		this.save();
	}

	public void setPoints(Integer points) {
		this.points = points;
		this.save();
	}

	public void addPoints(Integer points) {
		Integer old = this.points;
		this.points = old + points;
		this.save();
	}

	public static LocalData from(Player player) {

		UUID uuid = player.getUniqueId();
		LocalData data = playerData.get(uuid);
		if (data == null) {
			data = new LocalData(player.getName(), uuid);
		}
		return data;
	}

	public static void remove(Player player) {
		playerData.remove(player.getUniqueId());
	}
}
