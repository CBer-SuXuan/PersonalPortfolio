package me.suxuan.custominfo;

import me.suxuan.custominfo.commands.ReloadCommand;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.plugin.SimplePlugin;

public class CustomInfo extends SimplePlugin {
	@Override
	protected void onPluginStart() {
		Common.warning("Custom Info plugin start successfully! Go to config.yml to change info!");
		registerCommand(new ReloadCommand());
	}

	public static CustomInfo getInstance() {
		return (CustomInfo) SimplePlugin.getInstance();
	}

}
