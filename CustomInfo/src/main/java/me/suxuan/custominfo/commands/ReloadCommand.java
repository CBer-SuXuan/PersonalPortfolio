package me.suxuan.custominfo.commands;

import me.suxuan.custominfo.CustomInfo;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.command.SimpleCommand;

public class ReloadCommand extends SimpleCommand {
	public ReloadCommand() {
		super("info");
		setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (args.length != 1 || !"reload".equals(args[0])) {
			Common.warning("&c需要输入 info reload 去重载插件！");
			return;
		}
		CustomInfo.getInstance().reload();
	}
}
