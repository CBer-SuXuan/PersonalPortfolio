package me.suxuan.changeserver.listener;

import me.suxuan.changeserver.ChangeServer;
import me.suxuan.changeserver.settings.Settings;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.mineacademy.bfo.Common;

public final class PlayerListener implements Listener {

	@EventHandler
	public void onServerKick(ServerKickEvent event) {
		ServerInfo kickedFrom = null;

		System.out.println(event.getPlayer().getServer().getInfo().getName());
		System.out.println(ChangeServer.getInstance().getProxy().getReconnectHandler());

		if (event.getPlayer().getServer() != null) {
			kickedFrom = event.getPlayer().getServer().getInfo();
		} else if (ChangeServer.getInstance().getProxy().getReconnectHandler() != null) {
			kickedFrom = AbstractReconnectHandler.getForcedHost(event.getPlayer().getPendingConnection());
			if (kickedFrom == null)
				kickedFrom = ProxyServer.getInstance().getServerInfo(event.getPlayer().getPendingConnection().getListener().getDefaultServer());
		}
		ServerInfo kickTo = ChangeServer.getInstance().getProxy().getServerInfo(Settings.TO_SERVER);
		if (kickedFrom != null && kickedFrom.equals(kickTo)) return;
		event.setCancelled(true);
		event.setCancelServer(kickTo);
		Common.tellNoPrefix(event.getPlayer(), Settings.MESSAGE);

	}

}
