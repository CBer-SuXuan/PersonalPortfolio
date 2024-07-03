package me.suxuan.wardrobe;

import lombok.Getter;
import me.suxuan.wardrobe.database.SQLite.PlayerInfoDatabase;
import me.suxuan.wardrobe.database.SQLite.PlayerInfoSQLite;
import me.suxuan.wardrobe.database.SQLite.WardrobeDatabase;
import me.suxuan.wardrobe.database.SQLite.WardrobeSQLite;
import me.suxuan.wardrobe.manager.PlayerInfoManager;
import me.suxuan.wardrobe.manager.WardrobeManager;
import me.suxuan.wardrobe.utils.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.plugin.SimplePlugin;

public final class Main extends SimplePlugin {

	@Getter
	private WardrobeDatabase wardrobeDatabase;
	@Getter
	private PlayerInfoDatabase playerInfoDatabase;
	@Getter
	private boolean useHead;

	@Override
	protected void onPluginStart() {
		FileUtil.createIfNotExists("..\\Wardrobe");
		this.wardrobeDatabase = new WardrobeSQLite(this);
		this.wardrobeDatabase.load();
		this.playerInfoDatabase = new PlayerInfoSQLite(this);
		this.playerInfoDatabase.load();
		Cooldown.setupCooldown();
	}

	public static Main getInstance() {
		return (Main) SimplePlugin.getInstance();
	}

	@Override
	protected void onPluginReload() {

	}

	@Override
	protected void onPluginStop() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerInfoManager.remove(player);
			WardrobeManager.remove(player);
		}
		Bukkit.getLogger().info("[Wardrobe] Player info saved!");
	}
}
