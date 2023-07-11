package me.suxuan.changeserver.commands;

import me.suxuan.changeserver.ChangeServer;
import me.suxuan.changeserver.settings.Settings;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

import java.util.List;

public class ReloadCommand extends SimpleCommand {
	public ReloadCommand() {
		super("cs");
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (args.length != 1 || !"reload".equals(args[0])) {
			Common.warning("&c需要输入 cs reload 去重载插件！");
			return;
		}
		ChangeServer.getInstance().reload();
		System.out.println("传送服务器名称已经设置为\"" + Settings.TO_SERVER + "\"");
	}

	@Override
	protected List<String> tabComplete() {
		return List.of("reload");
	}
}
