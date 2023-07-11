package me.suxuan.custominfo.listener;

import me.suxuan.custominfo.settings.Settings;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.annotation.AutoRegister;
import org.mineacademy.bfo.remain.Remain;

import java.util.List;
import java.util.UUID;

@AutoRegister
public final class PlayerListener implements Listener {

	@EventHandler
	public void onPing(ProxyPingEvent e) {
		ServerPing ping = e.getResponse();
		ServerPing.Players player = ping.getPlayers();

		if (Settings.USE_ADVANCED)
			// Front message and Player message
			e.getResponse().setVersion(new ServerPing.Protocol(Common.colorize(Settings.FRONT_MESSAGE +
					"                                                  "
					+ Settings.PLAYER_MESSAGE.replace("{current_players}",
					String.valueOf(Remain.getOnlinePlayers().size()))), -999));
		else {
			// Max players
			player.setMax(Settings.MAX_PLAYERS);

			// Current players
			player.setOnline(Remain.getOnlinePlayers().size());
		}

		// Hover message
		String[] string_list = Settings.HOVER_MESSAGE.split("\n");
		List<ServerPing.PlayerInfo> list = new java.util.ArrayList<>(List.of(new ServerPing.PlayerInfo[]{}));
		for (String hover : string_list) {
			list.add(new ServerPing.PlayerInfo(Common.colorize(hover), UUID.randomUUID()));
		}
		ServerPing.PlayerInfo[] infos = list.toArray(new ServerPing.PlayerInfo[0]);
		player.setSample(infos);

		// MOTD
		ping.setDescriptionComponent(new TextComponent(Common.colorize(Settings.MOTD)));

		// All done
		e.setResponse(ping);
	}

}
