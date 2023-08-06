package me.suxuan.quests.cache;

import lombok.Getter;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CategoriesCache extends YamlConfig {

	public static final Map<String, CategoriesCache> cacheMap = new HashMap<>();

	private final String categoryFile;

	private String name;
	private String position;
	private String icon;
	private List<String> details;
	private List<String> quests;
	private Boolean glowing;

	private CategoriesCache(String categoryFile) {
		this.categoryFile = categoryFile;
		this.loadConfiguration(NO_DEFAULT, "categories/" + categoryFile + ".yml");
	}

	@Override
	protected void onLoad() {
		this.name = getString("Name");
		this.position = getString("Position");
		this.icon = getString("Icon");
		this.details = getStringList("Details");
		this.quests = getStringList("Quests");
		this.glowing = getBoolean("Glowing");
	}

	public static CategoriesCache from(String categoryFile) {
		CategoriesCache categoriesCache = cacheMap.get(categoryFile);
		if (categoriesCache == null) {
			categoriesCache = new CategoriesCache(categoryFile);
			cacheMap.put(categoryFile, categoriesCache);
		}
		return categoriesCache;
	}

	public static void clearCaches() {
		cacheMap.clear();
	}
}
