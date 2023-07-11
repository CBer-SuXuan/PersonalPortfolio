package me.suxuan.loginreward;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends SimpleCommand {

	public MainCommand() {
		super("loginreward|lr");
		this.setAutoHandleHelp(false);
	}

	@Override
	protected void onCommand() {

		if (args.length <= 1 || args.length >= 4) {
			Common.tellTimedNoPrefix(5, sender, "[&6&lLogin&9&lReward&f] &c&l正确用法：/lr <玩家名> <物品名> [物品数量(默认1)]");
			return;
		}

		// 获取数目，默认为1
		String number;
		if (args.length == 2) number = "1";
		else number = args[2];

		// 获取玩家
		Player target = Bukkit.getPlayer(args[0]);

		// 检查物品是否拼写正确
		CompMaterial mat = CompMaterial.fromString(args[1]);
		if (mat == null) {
			tellNoPrefix("[&6&lLogin&9&lReward&f] &c&l物品名称有误，请核实后输入！");
			return;
		}

		// 检查在线状态
		if (target != null) {
			if (target.isOnline()) {
				ItemCreator.of(mat).amount(Integer.parseInt(number)).give(target);
				Common.tellNoPrefix(sender, "[&6&lLogin&9&lReward&f] &a&l该玩家在线，已经将物品给予该玩家！");
				return;
			}
		}

		// 输出文件，存储到服务器中，等待下次进游戏获取物品
		PlayerData.from(args[0]).setItem(mat.getMaterial().name() + ";" + number);

		Common.tellNoPrefix(sender, "[&6&lLogin&9&lReward&f] &a&l玩家将在登录后获取"
				+ number + "个" + mat.getMaterial().toString().toLowerCase() + "！");
	}

	@Override
	protected List<String> tabComplete() {
		List<String> items = new ArrayList<>();
		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers())
				items.add(player.getName());
			return items;
		}
		if (args.length == 2) {
			for (CompMaterial mat : CompMaterial.values())
				items.add(mat.name().toLowerCase());
			return items;
		}
		return NO_COMPLETE;
	}
}
