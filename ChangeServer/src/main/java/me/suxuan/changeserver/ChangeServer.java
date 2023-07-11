package me.suxuan.changeserver;

import me.suxuan.changeserver.commands.ReloadCommand;
import me.suxuan.changeserver.listener.PlayerListener;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.plugin.SimplePlugin;

public class ChangeServer extends SimplePlugin {
	@Override
	protected void onPluginStart() {
		Common.warning("Change server plugin start successfully! Go to config.yml to change server name!");
		registerCommand(new ReloadCommand());
		registerEvents(new PlayerListener());
	}

	public static ChangeServer getInstance() {
		return (ChangeServer) SimplePlugin.getInstance();
	}

}
