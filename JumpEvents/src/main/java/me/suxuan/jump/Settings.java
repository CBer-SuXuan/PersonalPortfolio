package me.suxuan.jump;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Settings extends YamlStaticConfig {

	@Override
	protected void onLoad() {
		this.loadConfiguration("config.yml");
	}

	public static List<String> MOB_LIST;
	public static List<String> POTION_LIST;
	public static List<String> ITEM_LIST;
	public static Boolean SUMMON_TNT;

	public static List<String> ALL_NEED;

	private static void init() {
		List<String> all = new ArrayList<>();
		MOB_LIST = getBoolean("Summon_mob.Use") ? getStringList("Summon_mob.List") : null;
		POTION_LIST = getBoolean("Give_potion.Use") ? getStringList("Give_potion.List") : null;
		ITEM_LIST = getBoolean("Give_item.Use") ? getStringList("Give_item.List") : null;
		SUMMON_TNT = getBoolean("Summon_TNT");
		for (String mob : MOB_LIST)
			if (MOB_LIST != null)
				all.add(mob);
		for (String potion : POTION_LIST)
			if (POTION_LIST != null)
				all.add(potion);
		for (String item : ITEM_LIST)
			if (ITEM_LIST != null)
				all.add(item);
		if (SUMMON_TNT)
			all.add("TNT");
		Collections.shuffle(all);
		ALL_NEED = all;
		System.out.println(ALL_NEED);
	}

}
