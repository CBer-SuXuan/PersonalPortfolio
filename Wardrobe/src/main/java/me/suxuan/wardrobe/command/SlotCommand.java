package me.suxuan.wardrobe.command;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.settings.MenuSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

public class SlotCommand {

	public static void performSlotCommand(Player player, String[] args) {

		if (args.length == 1 || args.length == 2 || args.length == 3) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &fUsage: &6/wardrobe slot <player> add <number>");
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cNo Player called '" + args[1] + "'!");
			return;
		}
		String type = args[2];
		if (!"add".equals(type)) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cOnly support 'add'!");
			return;
		}
		int number = -1;
		try {
			number = Integer.parseInt(args[3]);
		} catch (Throwable t) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cCannot convert '" + args[3] + "' to number!");
			return;
		}
		if (MenuSetting.getInstance().getSlotPermission()) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou can't use this command when slot permission is on!");
		} else {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &eSlot permission is off, you may lost data when use it!");
			Main.getInstance().getPlayerInfoDatabase().addUnlock(target, number);
			Main.getInstance().getWardrobeDatabase().updateSlot(target, Main.getInstance().getPlayerInfoDatabase().getUnlock(target));
		}
	}

}
