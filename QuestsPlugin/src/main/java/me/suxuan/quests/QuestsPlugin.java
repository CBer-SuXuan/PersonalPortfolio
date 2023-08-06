package me.suxuan.quests;

import lombok.Getter;
import me.suxuan.quests.cache.CategoriesCache;
import me.suxuan.quests.cache.QuestsCache;
import me.suxuan.quests.command.QuestsCommand;
import me.suxuan.quests.listener.BreakEvent;
import me.suxuan.quests.listener.CatchEvent;
import me.suxuan.quests.listener.KillEvent;
import me.suxuan.quests.listener.PlayerListener;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;
import java.util.Objects;

public final class QuestsPlugin extends SimplePlugin {

	@Getter
	private boolean useHead;

	@Override
	protected void onPluginStart() {
		// Check if use HeadDatabase plugin.
		this.useHead = getServer().getPluginManager().getPlugin("HeadDatabase") != null;
		File file = new File(getDataFolder(), "quests");
		if (file.exists()) {
			QuestsCache.clearCaches();
			// Save cache for quests
			File[] quests = file.listFiles();
			for (File quest : Objects.requireNonNull(quests)) {
				// Not add template in the cache
				if (quest.getName().equals("template.yml")) continue;
				QuestsCache.from(quest.getName().replace(".yml", ""));
			}

			// Save cache for categories
			File[] categories = new File(getDataFolder(), "categories").listFiles();
			CategoriesCache.clearCaches();
			for (File category : Objects.requireNonNull(categories)) {
				// Not add template in the cache
				if (category.getName().equals("template.yml")) continue;
				CategoriesCache.from(category.getName().replace(".yml", ""));
			}
		} else {
			Common.warning("Add your own quests in QuestsPlugin/quests folder. After modifying use /qt reload");
			Common.warning("Add your own categories in QuestsPlugin/categories folder. After modifying use /qt reload");
			// Save template and example file
			saveResource("quests/break.yml", false);
			saveResource("quests/diamond.yml", false);
			saveResource("quests/break_kill.yml", false);
			saveResource("quests/template.yml", false);
			saveResource("quests/catch.yml", false);
			saveResource("categories/mining.yml", false);
			saveResource("categories/kill.yml", false);
			saveResource("categories/template.yml", false);
			// Save config.yml
			saveDefaultConfig();
		}
		registerCommand(new QuestsCommand());
		registerEvents(new PlayerListener());
		registerEvents(new BreakEvent());
		registerEvents(new KillEvent());
		registerEvents(new CatchEvent());
		// Check again if server use HeadDatabase
		Common.runLaterAsync(100, () -> {
			this.useHead = getServer().getPluginManager().getPlugin("HeadDatabase") != null;
		});
	}

	public static QuestsPlugin getInstance() {
		return (QuestsPlugin) SimplePlugin.getInstance();
	}

	@Override
	protected void onPluginReload() {

		File file = new File(getDataFolder(), "quests");
		QuestsCache.clearCaches();
		File[] quests = file.listFiles();
		for (File quest : Objects.requireNonNull(quests)) {
			if (quest.getName().equals("template.yml")) continue;
			QuestsCache.from(quest.getName().replace(".yml", ""));
		}

		File category_file = new File(getDataFolder(), "categories");
		CategoriesCache.clearCaches();
		File[] categories = category_file.listFiles();
		for (File category : Objects.requireNonNull(categories)) {
			if (category.getName().equals("template.yml")) continue;
			CategoriesCache.from(category.getName().replace(".yml", ""));
		}

	}
}
