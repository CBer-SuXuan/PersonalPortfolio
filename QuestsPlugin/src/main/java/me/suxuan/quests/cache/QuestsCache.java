package me.suxuan.quests.cache;

import lombok.Getter;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class QuestsCache extends YamlConfig {

	public static final Map<String, QuestsCache> cacheMap = new HashMap<>();

	private final String quest_file_name;

	private SerializedMap actions;
	private SerializedMap unacceptedQuest;
	private SerializedMap acceptedQuest;
	private SerializedMap finishedQuest;
	private SerializedMap placeholder;
	private SerializedMap start;
	private SerializedMap complete;
	private List<String> rewards;
	private String position;
	private Boolean repeatable;

	private QuestsCache(String quest_file_name) {
		this.quest_file_name = quest_file_name;
		this.loadConfiguration(NO_DEFAULT, "quests/" + quest_file_name + ".yml");
	}

	@Override
	protected void onLoad() {

		// Put each type info of this quest
		SerializedMap typeMap = new SerializedMap();
		// Put the placeholder map
		SerializedMap placeholderMap = new SerializedMap();

		// Unaccepted quest map
		this.unacceptedQuest = getMap("Unaccepted");
		// Accepted quest map
		this.acceptedQuest = getMap("Accepted");
		// Finished quest map
		this.finishedQuest = getMap("Finished");

		// Check if action contain break
		if (!getStringList("Actions.Break").isEmpty()) {
			// put {Break:[xxx,xxx,xxx]} in type map
			typeMap.put("Break", getStringList("Actions.Break"));
			// Check if need add placeholder
			for (String breakRequire : getStringList("Actions.Break")) {
				String[] breakArray = breakRequire.split(";");
				// If length is 3, put in placeholder map
				if (breakArray.length == 3)
					placeholderMap.put(breakArray[2], breakArray[0]);
			}
		}
		// The same as break, check kill action
		if (!getStringList("Actions.Kill").isEmpty()) {
			typeMap.put("Kill", getStringList("Actions.Kill"));
			for (String killRequire : getStringList("Actions.Kill")) {
				String[] killArray = killRequire.split(";");
				if (killArray.length == 3)
					placeholderMap.put(killArray[2], killArray[0]);
			}
		}
		// The same as break, check catch action
		if (!getStringList("Actions.Catch").isEmpty()) {
			typeMap.put("Catch", getStringList("Actions.Catch"));
			for (String catchRequire : getStringList("Actions.Catch")) {
				String[] catchArray = catchRequire.split(";");
				if (catchArray.length == 3)
					placeholderMap.put(catchArray[2], catchArray[0]);
			}
		}

		this.placeholder = placeholderMap;
		this.rewards = getStringList("Rewards");
		this.position = getString("Position");
		this.start = getMap("Start");
		this.complete = getMap("Complete");
		this.repeatable = getBoolean("Repeatable");

		this.actions = typeMap;
	}

	public static QuestsCache from(String quest_file_name) {
		QuestsCache cache = cacheMap.get(quest_file_name);
		if (cache == null) {
			cache = new QuestsCache(quest_file_name);
			cacheMap.put(quest_file_name, cache);
		}
		return cache;
	}

	public static void clearCaches() {
		cacheMap.clear();
	}

}
