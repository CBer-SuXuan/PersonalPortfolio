package me.suxuan.survey.listeners;

import me.suxuan.survey.PlayerCache;
import me.suxuan.survey.SurveyPlugin;
import me.suxuan.survey.mysql.SQLUtils;
import me.suxuan.survey.utils.Log;
import me.suxuan.survey.utils.Permission;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KillEvent implements Listener {

	private final SQLUtils sql = SurveyPlugin.getInstance().getSql();

	@EventHandler
	public void playerKillEvent(PlayerDeathEvent event) throws SQLException, ParseException {

		Player player = event.getEntity().getKiller();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (player == null) return;
		if (PlayerCache.getQuestion().containsKey(player.getUniqueId())) return;

		Bukkit.getScheduler().runTaskAsynchronously(SurveyPlugin.getInstance(), () -> {
			try {
				ResultSet set = sql.query("SELECT * FROM survey WHERE uuid = '" + player.getUniqueId().toString() + "' ORDER BY last_survey DESC;");
				if (set.next()) {

					long last_survey = format.parse(set.getString("last_survey")).getTime();
					if (System.currentTimeMillis() <= last_survey + 1000L * 3600 * SurveyPlugin.interval) return;

				}

				// Plus one.
				sql.asyncUpdate("UPDATE kill_number SET `kill` = `kill` + 1 WHERE uuid = '" + player.getUniqueId().toString() + "';");

				// Check if need send survey
				ResultSet kill_set = sql.query("SELECT * FROM kill_number WHERE uuid = '" + player.getUniqueId().toString() + "';");

				// If kill times over than 10.
				if (kill_set.next()) {
					if (kill_set.getInt("kill") >= 10) {

						// Give permission
						Permission.addPerm(player, "survey.command.start");

						// Create text
						TextComponent click = new TextComponent(Log.colorize(SurveyPlugin.button));
						click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sv start"));
						player.spigot().sendMessage(new BaseComponent[]{click});
						sql.asyncUpdate("UPDATE kill_number SET `kill` = 0 WHERE uuid = '" + player.getUniqueId().toString() + "';");

					}
				}
			} catch (SQLException | ParseException e) {
				throw new RuntimeException(e);
			}


		});

	}

}
