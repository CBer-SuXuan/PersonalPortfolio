package me.suxuan.joinreward;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;
import java.util.List;

public class MainCommand extends SimpleCommand {

	public MainCommand() {
		super("joinreward|jr");
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {

		if (args.length >= 2 || !args[0].equals("reload")) {
			Common.tellTimedNoPrefix(5, sender, "[&6&lJoin&9&lReward&f] &c&l正确用法：/jr reload");
			return;
		}
		JoinReward.getInstance().reload();

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return Collections.singletonList("reload");
		} else
			return NO_COMPLETE;
	}
}
