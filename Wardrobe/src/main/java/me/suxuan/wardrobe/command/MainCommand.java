package me.suxuan.wardrobe.command;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.manager.PlayerInfoManager;
import me.suxuan.wardrobe.manager.WardrobeManager;
import me.suxuan.wardrobe.menus.MainSettingsMenu;
import me.suxuan.wardrobe.menus.player.PlayerWardrobeMenu;
import me.suxuan.wardrobe.settings.MenuSetting;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public final class MainCommand extends SimpleCommand {

	LuckPerms luckPerms = LuckPermsProvider.get();

	public MainCommand() {
		super("wardrobe|wd");
		setAutoHandleHelp(false);
		setPermission(null);
	}

	@Override
	protected void onCommand() {

		if (!getPlayer().getOpenInventory().getTopInventory().getType().equals(InventoryType.CRAFTING)) return;

		System.out.println(233);

		PlayerInfoManager playerInfoManager = PlayerInfoManager.from(getPlayer());
		WardrobeManager wardrobeManager = WardrobeManager.from(getPlayer());
		PlayerInventory inventory = getPlayer().getInventory();

		List<Integer> permissions = new ArrayList<>();
		for (Node node : LuckPermsProvider.get().getUserManager().getUser(getPlayer().getUniqueId()).resolveInheritedNodes(QueryOptions.nonContextual())) {
			String key = node.getKey();
			if (key.startsWith("wardrobe.slot."))
				permissions.add(Integer.valueOf(key.replace("wardrobe.slot.", "")));
		}

		if (args.length == 0) {

			// Player first use wardrobe command
			if (playerInfoManager.getSelected() == -1) {
				Main.getInstance().getWardrobeDatabase().updatePermissionSlot(getPlayer(), permissions);

				for (int i : permissions) wardrobeManager.setEquipAll(null, null, null, null, i);
				playerInfoManager.setSelected(wardrobeManager.getFirstSlot());
				wardrobeManager.setEquipAll(inventory.getHelmet(), inventory.getChestplate(),
						inventory.getLeggings(), inventory.getBoots(), playerInfoManager.getSelected());

				PlayerWardrobeMenu.showTo(getPlayer(), getPlayer(), 1);
				return;
			}

			permissions.removeIf(wardrobeManager.getPermissionSlotList()::contains);
			for (int i : permissions) {
				wardrobeManager.setEquipAll(null, null, null, null, i);
			}

			wardrobeManager.setEquipAll(inventory.getHelmet(), inventory.getChestplate(),
					inventory.getLeggings(), inventory.getBoots(), playerInfoManager.getSelected());
			PlayerWardrobeMenu.showTo(getPlayer(), getPlayer(), 1);
			return;
		}

		if (Valid.isInteger(args[0])) {
			if (!getPlayer().hasPermission("wardrobe.quickcommand")) return;
			if (!getPermissionSlot(getPlayer()).contains(args[0])) return;
			// Update old armors
			wardrobeManager.setEquipAll(inventory.getHelmet(), inventory.getChestplate(),
					inventory.getLeggings(), inventory.getBoots(), playerInfoManager.getSelected());

			// Set new armors
			List<ItemStack> stacks = wardrobeManager.getEquipAll(Integer.parseInt(args[0]));
			inventory.setHelmet(stacks.get(0));
			inventory.setChestplate(stacks.get(1));
			inventory.setLeggings(stacks.get(2));
			inventory.setBoots(stacks.get(3));
			playerInfoManager.setSelected(Integer.parseInt(args[0]));
		}

		switch (args[0]) {
			case "slot" -> {
				if (!getPlayer().hasPermission("wardrobe.command.slot")) return;
				SlotCommand.performSlotCommand(getPlayer(), args);
			}
			case "settings" -> {
				if (!getPlayer().hasPermission("wardrobe.command.settings")) return;
				MainSettingsMenu.showTo(getPlayer());
			}
			case "reload" -> {
				if (!getPlayer().hasPermission("wardrobe.command.reload")) return;
				MenuSetting.getInstance().reload();
			}
			case "perm" -> {
				if (!getPlayer().hasPermission("wardrobe.command.perm")) return;
				(new PermCommand()).performPermCommand(getPlayer(), args);
			}
			case "player" -> {
				if (!getPlayer().hasPermission("wardrobe.show.other")) return;
				if (args.length < 2) return;
				Player target = Bukkit.getPlayer(args[1]);
				if (target == null) {
					tellNoPrefix("&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cNo online player called '" + args[1] + "'!");
					return;
				}
				PlayerWardrobeMenu.showTo(getPlayer(), target, 1);
			}
			default -> {
			}
		}

	}

	@Override
	protected List<String> tabComplete() {
		if (!isPlayer()) return NO_COMPLETE;
		if (!getPlayer().isOp() && getPlayer().hasPermission("wardrobe.quickcommand")) {
			if (args.length == 1) return getPermissionSlot(getPlayer());
			else return NO_COMPLETE;
		}
		switch (args.length) {
			case 1:
				return completeLastWord("settings", "slot", "reload", "perm", "player", getPermissionSlot(getPlayer()));
			case 2:
				switch (args[0]) {
					case "slot", "perm" -> {
						return completeLastWordPlayerNames();
					}
					case "player" -> {
						return completeLastWord(Remain.getOnlinePlayers());
					}
				}
			case 3:
				if (args[0].equals("slot"))
					return completeLastWord("add");
				else if (args[0].equals("perm"))
					return completeLastWord("add", "remove", "list", "reload");
			case 4:
				if (args[0].equals("perm") && args[2].equals("remove")) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) return NO_COMPLETE;
					return getPermissionSlot(target);
				} else if (args[0].equals("perm") && args[2].equals("add")) {
					return List.of("<slot_number_to_add>");
				}
			default:
				return NO_COMPLETE;
		}
	}

	private List<String> getPermissionSlot(Player player) {
		List<String> complete = new ArrayList<>();
		for (Node node : luckPerms.getUserManager().getUser(player.getUniqueId()).resolveInheritedNodes(QueryOptions.nonContextual())) {
			String key = node.getKey();
			if (key.startsWith("wardrobe.slot."))
				if (luckPerms.getUserManager().getUser(player.getUniqueId()).getNodes().contains(Node.builder(key).build()))
					complete.add(key.replace("wardrobe.slot.", ""));
				else
					complete.add(key.replace("wardrobe.slot.", ""));
		}
		return complete;
	}
}
