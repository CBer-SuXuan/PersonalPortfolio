package me.suxuan.jump;

import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SimpleCommand {

	public ReloadCommand() {
		super("jump");
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {

		if (args.length == 1 && args[0].equals("reload"))
			JumpEvents.getInstance().reload();

	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return Collections.singletonList("reload");
		}
		return NO_COMPLETE;
	}
}
