package me.suxuan.rollboss;

import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SimpleCommand {

	public ReloadCommand() {
		super("rbb");
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {

		if (args.length == 1 && args[0].equals("reload"))
			RollBossBar.getInstance().reload();

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return Collections.singletonList("reload");
		}
		return NO_COMPLETE;
	}
}
