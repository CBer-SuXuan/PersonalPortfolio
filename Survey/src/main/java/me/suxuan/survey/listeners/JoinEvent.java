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
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinEvent implements Listener {

	private final SQLUtils sql = SurveyPlugin.getInstance().getSql();

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) throws SQLException {

		Player player = event.getPlayer();

		Bukkit.getScheduler().runTaskAsynchronously(SurveyPlugin.getInstance(), () -> {
			ResultSet set = sql.query("SELECT * FROM kill_number WHERE uuid = '" + player.getUniqueId().toString() + "';");
			try {
				if (!set.next())
					sql.asyncUpdate("INSERT INTO kill_number (`uuid`, `name`, `kill`) VALUES ('"
							+ player.getUniqueId().toString() + "', '" + player.getName() + "', 0);");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			if (PlayerCache.getQuestion().containsKey(player.getUniqueId())) {

				Permission.addPerm(player, "survey.command.start");

				TextComponent click = new TextComponent(Log.colorize(SurveyPlugin.restart));
				click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sv restart"));
				player.spigot().sendMessage(new BaseComponent[]{click});

			}
		});

	}

}
