package me.suxuan.loginteleport;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;
import java.util.List;

/**
 * A sample standalone command.
 */
public class MainCommand extends SimpleCommand {

	public MainCommand() {
		super("loginteleport|lt");
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {
		if (!(sender instanceof Player)) {
			Common.tellNoPrefix(sender, "[&6&lLogin&9&lTeleport&f] 只有玩家能用该指令");
			return;
		}
		Settings settings = new Settings();
		if (!settings.getAllowedPlayers().contains(getPlayer().getName())) {
			Common.tellTimedNoPrefix(20, sender, "[&6&lLogin&9&lTeleport&f] &c&l你没有权限使用这个命令");
			return;
		}
		if (args.length != 1) {
			Common.tellNoPrefix(sender, "[&6&lLogin&9&lTeleport&f] &c&l指令使用不正确，正确用法：/lt set|clear");
			return;
		}
		String type = args[0];
		if ("set".equals(type)) {
			settings.setPosition(((Player) sender).getLocation());
			Common.tellNoPrefix(sender, "[&6&lLogin&9&lTeleport&f] &a&l位置设置成功");
		} else if ("clear".equals(type)) {
			if (settings.getIsEmpty()) {
				Common.tellNoPrefix(sender, "[&6&lLogin&9&lTeleport&f] " +
						"&8&l位置已经删除或者未初始化");
				return;
			}
			settings.setIsEmpty(true);
			Common.tellNoPrefix(sender, "[&6&lLogin&9&lTeleport&f] " +
					"&a&l位置删除成功，如果想恢复之前的位置，只需要把Is_Empty字段设置为false");
		}
	}

	@Override
	protected List<String> tabComplete() {
		Settings settings = new Settings();
		if (args.length == 1 && settings.getAllowedPlayers().contains(getPlayer().getName())) {
			return Arrays.asList("set", "clear");
		}
		return NO_COMPLETE;
	}
}
