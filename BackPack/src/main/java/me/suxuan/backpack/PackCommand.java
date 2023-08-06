package me.suxuan.backpack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;
import java.util.List;

public class PackCommand extends SimpleCommand {
	protected PackCommand() {
		super("backpack");
		setPermission("backpack.command");
	}

	@Override
	protected void onCommand() {
		if (args.length == 0) return;
		if (args.length == 1 && "reload".equals(args[0]))
			BackPack.getInstance().reload();
		if (args.length == 3 && "give".equals(args[0])) {
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				Common.logNoPrefix("No player called " + args[0]);
				return;
			}
			String name = args[2];
			if (!Settings.COMMAND_LIST.contains(name)) {
				Common.logNoPrefix("No backpack called " + name);
				return;
			}
			getPlayer().getInventory().addItem(Settings.ITEM_LIST.get(Settings.COMMAND_LIST.indexOf(name)));
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("give", "reload");
		if (args.length == 2 && args[0].equals("give"))
			return completeLastWordPlayerNames();
		if (args.length == 3 && args[0].equals("give"))
			return completeLastWord(Collections.singletonList(Settings.COMMAND_LIST));
		return NO_COMPLETE;
	}
}
