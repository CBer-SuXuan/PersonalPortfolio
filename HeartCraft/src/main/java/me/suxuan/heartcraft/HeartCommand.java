package me.suxuan.heartcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompAttribute;

import java.util.ArrayList;
import java.util.List;

public final class HeartCommand extends SimpleCommand {

	public HeartCommand() {
		super("heart");
		setAutoHandleHelp(false);
		setPermission("heartcraft.command.heart");
	}


	@Override
	protected void onCommand() {
		setTellPrefix("&7[&e&lH&b&lC&7] ");
		if (!getPlayer().hasPermission("heartcraft.command.heart")) {
			Common.tell(this.sender, "&c你没有权限设置血量");
		}
		if (this.args.length == 0) {
			Common.tell(this.sender, "&c你需要一个玩家来设置血量");
		} else if (this.args.length == 1) {
			Common.tell(this.sender, "&c你需要为该玩家设置一个血量的数值");
		} else if (this.args.length == 2) {
			Player target = Bukkit.getPlayer(this.args[0]);
			if (target == null || !target.isOnline()) {
				Common.tell(this.sender, "&c该玩家不在线或不存在");
				return;
			}
			try {
				int num = Integer.parseInt(this.args[1]);
				if (num >= 1) {
					Common.tell(this.sender, "&a成功将该玩家血量设置成 " + num);
					CompAttribute.GENERIC_MAX_HEALTH.set((LivingEntity) target, num);
				} else {
					Common.tell(this.sender, "&c血量必须为正整数");
				}
			} catch (NumberFormatException e) {
				Common.tell(this.sender, "&c血量必须为正整数");
			}
		}
	}

	@Override
	protected List<String> tabComplete() {
		List<String> completions = new ArrayList<>();
		switch (this.args.length) {
			case 1:
				return completeLastWord(Common.getPlayerNames());
			case 2:
				completions.add("<number>");
				return completions;
		}

		return NO_COMPLETE;
	}
}