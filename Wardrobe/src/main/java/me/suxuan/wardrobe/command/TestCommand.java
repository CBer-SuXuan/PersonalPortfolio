package me.suxuan.wardrobe.command;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

@AutoRegister
public final class TestCommand extends SimpleCommand {
	
	public TestCommand() {
		super("test");
		setAutoHandleHelp(false);
		setPermission("wardrobe.command.test");
	}

	@Override
	protected void onCommand() {
		Common.tellNoPrefix(getSender(), "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cThis is test command for Wardrobe plugin, ignore it! (wardrobe.command.test)");
	}

	@Override
	protected List<String> tabComplete() {
		return NO_COMPLETE;
	}
}
