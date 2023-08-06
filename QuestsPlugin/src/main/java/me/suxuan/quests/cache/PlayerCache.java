package me.suxuan.quests.cache;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;
import java.util.*;

@Getter
public final class PlayerCache extends YamlConfig {

	private static final Map<UUID, PlayerCache> cacheMap = new HashMap<>();

	private final UUID uniqueId;
	private final String playerName;

	private String doing_quest;
	private List<String> doing_types;

	private List<String> done_quests;

	private List<String> break_quests;
	private List<String> catch_quests;
	private List<String> kill_quests;

	private List<String> break_done;
	private List<String> kill_done;
	private List<String> catch_done;

	private Boolean break_finish;
	private Boolean kill_finish;
	private Boolean catch_finish;

	private Integer page;

	private PlayerCache(String name, UUID uniqueId) {
		this.playerName = name;
		this.uniqueId = uniqueId;

		this.loadConfiguration(NO_DEFAULT, "players/" + uniqueId + ".yml");
		this.setHeader("This player is \"" + playerName + "\".");
		this.save();
	}

	@Override
	protected void onLoad() {
		this.doing_quest = getString("Doing.quest", "");
		this.doing_types = getStringList("Doing.types");

		this.done_quests = getStringList("Done_quests");

		this.break_quests = getStringList("Break.quests");
		this.catch_quests = getStringList("Catch.quests");
		this.kill_quests = getStringList("Kill.quests");

		this.break_done = getStringList("Break.done");
		this.catch_done = getStringList("Catch.done");
		this.kill_done = getStringList("Kill.done");

		this.break_finish = getBoolean("Break.finish", false);
		this.catch_finish = getBoolean("Catch.finish", false);
		this.kill_finish = getBoolean("Kill.finish", false);

		this.page = getInteger("Page", 1);
	}

	@Override
	public void onSave() {
		this.set("Doing.quest", this.doing_quest);
		this.set("Doing.types", this.doing_types);

		this.set("Done_quests", this.done_quests);

		this.set("Break.quests", this.break_quests);
		this.set("Catch.quests", this.catch_quests);
		this.set("Kill.quests", this.kill_quests);

		this.set("Break.done", this.break_done);
		this.set("Catch.done", this.catch_done);
		this.set("Kill.done", this.kill_done);

		this.set("Break.finish", this.break_finish);
		this.set("Catch.finish", this.catch_finish);
		this.set("Kill.finish", this.kill_finish);

		this.set("Page", this.page);
	}

	/* ------------------------------------------------------------------------------- */
	/* Data-related methods */
	/* ------------------------------------------------------------------------------- */

	/* ----------------------------------Set methods-------------------------------------*/
	public void setDoingQuest(String doing) {
		this.doing_quest = doing;
		this.save();
	}

	public void setBreakQuests(List<String> break_quests) {
		this.break_quests = break_quests;
		this.save();
	}

	public void setCatchQuests(List<String> catch_quests) {
		this.catch_quests = catch_quests;
		this.save();
	}

	public void setKillQuests(List<String> kill_quests) {
		this.kill_quests = kill_quests;
		this.save();
	}

	public void setBreakFinish(boolean finish) {
		this.break_finish = finish;
		this.save();
	}

	public void setKillFinish(boolean finish) {
		this.kill_finish = finish;
		this.save();
	}

	public void setCatchFinish(boolean finish) {
		this.catch_finish = finish;
		this.save();
	}

	public void setDoingTypes(List<String> types) {
		this.doing_types = types;
		this.save();
	}

	public void setPage(int page) {
		this.page = page;
		this.save();
	}
	/* ----------------------------------------------------------------------------------*/

	/* ----------------------------------Add methods-------------------------------------*/
	public void addDoneQuest(String done) {
		List<String> done_list = getStringList("Done_quests");
		if (!done_list.contains(done))
			done_list.add(done);
		this.done_quests = done_list;
		this.save();
	}

	public void addBreakDone(String done) {
		List<String> done_list = getStringList("Break.done");
		if (!done_list.contains(done))
			done_list.add(done);
		this.break_done = done_list;
		this.save();
	}

	public void addKillDone(String done) {
		List<String> done_list = getStringList("Kill.done");
		if (!done_list.contains(done))
			done_list.add(done);
		this.kill_done = done_list;
		this.save();
	}

	public void addCatchDone(String done) {
		List<String> done_list = getStringList("Catch.done");
		if (!done_list.contains(done))
			done_list.add(done);
		this.catch_done = done_list;
		this.save();
	}

	public void plusPage() {
		int old = getInteger("Page");
		this.page = old + 1;
		this.save();
	}

	public void minusPage() {
		int old = getInteger("Page");
		this.page = old - 1;
		this.save();
	}
	/* ----------------------------------------------------------------------------------*/

	/* --------------------------------Modify methods------------------------------------*/
	public void modifyBreakDone(String old_string, String new_string) {
		List<String> done_list = getStringList("Break.done");
		done_list.remove(old_string);
		done_list.add(new_string);
		this.break_done = done_list;
		this.save();
	}

	public void modifyKillDone(String old_string, String new_string) {
		List<String> done_list = getStringList("Kill.done");
		done_list.remove(old_string);
		done_list.add(new_string);
		this.kill_done = done_list;
		this.save();
	}

	public void modifyCatchDone(String old_string, String new_string) {
		List<String> done_list = getStringList("Catch.done");
		done_list.remove(old_string);
		done_list.add(new_string);
		this.catch_done = done_list;
		this.save();
	}
	/* ----------------------------------------------------------------------------------*/

	/* ---------------------------------Reset Method-------------------------------------*/

	public void doneQuest(String quest) {
		resetBasic();
		this.addDoneQuest(quest);
		this.save();
	}

	public void resetAll() {
		resetBasic();
		done_quests = new ArrayList<>();
		this.save();
	}

	// Reset except done quest
	public void resetBasic() {
		this.break_done = new ArrayList<>();
		this.kill_done = new ArrayList<>();
		this.catch_done = new ArrayList<>();
		this.break_quests = new ArrayList<>();
		this.kill_quests = new ArrayList<>();
		this.catch_quests = new ArrayList<>();
		this.doing_types = new ArrayList<>();
		this.break_finish = false;
		this.catch_finish = false;
		this.kill_finish = false;
		this.doing_quest = "";
		this.save();
	}
	/* ----------------------------------------------------------------------------------*/

	/* ------------------------------------------------------------------------------- */
	/* Misc methods */
	/* ------------------------------------------------------------------------------- */

	@Nullable
	public Player toPlayer() {
		final Player player = Remain.getPlayerByUUID(this.uniqueId);

		return player != null && player.isOnline() ? player : null;
	}

	public void removeFromMemory() {
		synchronized (cacheMap) {
			cacheMap.remove(this.uniqueId);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PlayerCache && ((PlayerCache) obj).getUniqueId().equals(this.uniqueId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.uniqueId);
	}

	@Override
	public String toString() {
		return "PlayerCache{" + this.playerName + ", " + this.uniqueId + "}";
	}

	/* ------------------------------------------------------------------------------- */
	/* Initial methods */
	/* ------------------------------------------------------------------------------- */

	public static PlayerCache from(Player player) {
		synchronized (cacheMap) {
			final UUID uniqueId = player.getUniqueId();
			final String playerName = player.getName();

			PlayerCache cache = cacheMap.get(uniqueId);

			if (cache == null) {
				cache = new PlayerCache(playerName, uniqueId);

				cacheMap.put(uniqueId, cache);
			}

			return cache;
		}
	}

	public static void clearCaches() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}

	public static void reloadCache(Player player) {
		cacheMap.remove(player.getUniqueId());
		PlayerCache.from(player);
	}
}
