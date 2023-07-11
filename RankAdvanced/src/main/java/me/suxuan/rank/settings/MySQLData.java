package me.suxuan.rank.settings;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author CBer_SuXuan
 * @className MySQLData
 * @date 2023/5/13 22:34
 * @description
 */
@Getter
@Setter
public final class MySQLData {

	private String playerName;
	private UUID uuid;
	private String rank;
	private String vessel;
	private Integer vessel_int;
	private Integer tier;
	private Integer points;

	public MySQLData(String playerName, UUID uuid, String rank, String vessel, int vessel_int, int tier, int points) {
		this.playerName = playerName;
		this.uuid = uuid;
		this.vessel = vessel;
		this.rank = rank;
		this.vessel_int = vessel_int;
		this.points = points;
		this.tier = tier;
	}

	public void addPoints(int number) {
		int old_ = this.getPoints();
		int new_ = old_ + number;
		this.setPoints(new_);
	}
}
