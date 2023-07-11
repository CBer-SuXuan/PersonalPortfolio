package me.suxuan.loginteleport;

import lombok.Getter;
import org.bukkit.Location;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

@Getter
public final class Settings extends YamlConfig {

	private Boolean isEmpty;
	private String world;
	private Double x;
	private Double y;
	private Double z;
	private Double yaw;
	private Double pitch;
	private List<String> allowedPlayers;


	public Settings() {
		this.loadConfiguration(NO_DEFAULT, "settings.yml");
		this.setHeader("Version不要修改\nIs_Empty不用管\n把允许使用指令的玩家姓名输入到Allowed_Players列表中");
		this.save();
	}

	@Override
	protected void onLoad() {
		setPathPrefix("Location");
		this.world = getString("World");
		this.x = getDouble("X");
		this.y = getDouble("Y");
		this.z = getDouble("Z");
		this.yaw = getDouble("Yaw");
		this.pitch = getDouble("Pitch");
		setPathPrefix(null);
		this.allowedPlayers = getStringList("Allowed_Players");
		this.isEmpty = getBoolean("Is_Empty");
	}

	@Override
	protected void onSave() {
		setPathPrefix("Location");
		this.set("World", this.world);
		this.set("X", this.x);
		this.set("Y", this.y);
		this.set("Z", this.z);
		this.set("Yaw", this.yaw);
		this.set("Pitch", this.pitch);
		setPathPrefix(null);
		this.set("Allowed_Players", this.allowedPlayers);
		this.set("Is_Empty", this.isEmpty);
	}

	public void setPosition(Location loc) {
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = (double) loc.getYaw();
		this.pitch = (double) loc.getPitch();
		this.world = loc.getWorld().getName();
		this.isEmpty = false;
		this.save();
	}

	public void setIsEmpty(Boolean isEmpty) {
		this.isEmpty = isEmpty;
		this.save();
	}

	public void addAllowedPlayers(String allowedPlayers) {
		List<String> list = getStringList("Allowed_Players");
		list.add(allowedPlayers);
		this.allowedPlayers = list;
		this.save();
	}
}
