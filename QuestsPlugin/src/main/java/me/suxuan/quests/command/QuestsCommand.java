package me.suxuan.quests.command;

import me.suxuan.quests.QuestsPlugin;
import me.suxuan.quests.cache.PlayerCache;
import me.suxuan.quests.menu.CategoriesMenu;
import me.suxuan.quests.utils.QuestsUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public final class QuestsCommand extends SimpleCommand {

	public QuestsCommand() {
		super("quests|qt");
		this.setAutoHandleHelp(false);
		this.setPermission(null);
	}

	@Override
	protected void onCommand() {
		if (args.length == 0)
			CategoriesMenu.showTo(getPlayer(), 1);
		if (args.length == 1 && args[0].equals("reload")) {
			// Check if sender is command block or console
			if (!(sender instanceof Player)) {
				QuestsPlugin.getInstance().reload();
				return;
			}
			// Check if player have permission
			if (getPlayer().hasPermission("qt.command.reload")) {
				QuestsPlugin.getInstance().reload();
				Common.tellNoPrefix(getPlayer(), "&a&lQuests plugin successfully reload!");
			}
		}
		// This command is to reset player all info.
		if (args.length == 1 && args[0].equals("reset"))
			if (getPlayer().hasPermission("qt.command.reset"))
				QuestsUtil.resetAll(getPlayer());
		// This command is to reload player info after edit file.
		if (args.length == 1 && args[0].equals("cache"))
			if (getPlayer().hasPermission("qt.command.cache"))
				PlayerCache.reloadCache(getPlayer());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			if (getPlayer().hasPermission("qt.command"))
				return completeLastWord("reload");
		return NO_COMPLETE;
	}
}
