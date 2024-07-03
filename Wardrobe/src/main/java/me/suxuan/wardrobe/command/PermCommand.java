package me.suxuan.wardrobe.command;

import me.suxuan.wardrobe.Main;
import me.suxuan.wardrobe.database.SQLite.WardrobeDatabase;
import me.suxuan.wardrobe.manager.WardrobeManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PermCommand {

	public static final WardrobeDatabase WARDROBE_DATABASE = Main.getInstance().getWardrobeDatabase();
	LuckPerms luckPerms = LuckPermsProvider.get();

	public void performPermCommand(Player player, String[] args) {

		WardrobeManager wardrobeManager = WardrobeManager.from(player);
		if (args.length <= 2 || (args.length == 3 && !("list".equals(args[2]) || "reload".equals(args[2])))) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cWrong use!");
			return;
		}

		// Target valid check
		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cNo such player called '" + args[1] + "'!");
			return;
		}

		switch (args[2]) {
			case "add": {

				// Get the perm to add
				String permToAdd = args[3];

				// Valid check
				if (invalidNumber(permToAdd, player)) return;

				AtomicBoolean success = new AtomicBoolean(false);
				// Add perm to the player
				luckPerms.getUserManager().modifyUser(target.getUniqueId(), user -> {

					if (user.resolveInheritedNodes(QueryOptions.nonContextual()).contains(Node.builder("wardrobe.slot." + permToAdd).build())) {
						Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cThe player already has the perm wardrobe.slot." + permToAdd + "!");
						updatePermissionSlot(user);
						success.set(true);
					} else {
						user.data().add(Node.builder("wardrobe.slot." + permToAdd).build());
						success.set(false);
					}

				});

				Common.runLater(2, () -> {

					if (success.get()) return;

					wardrobeManager.addPermissionSlot(Integer.parseInt(permToAdd));
					Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &7Successfully add perm &awardrobe.slot." + permToAdd + "&7 to the player!");

				});

				return;

			}
			case "remove": {

				// Get the perm to remove
				String permToRemove = args[3];

				// Valid check
				if (invalidNumber(permToRemove, player)) return;

				AtomicBoolean success = new AtomicBoolean(false);
				// Remove perm from the player
				luckPerms.getUserManager().modifyUser(target.getUniqueId(), user -> {

					if (!user.resolveInheritedNodes(QueryOptions.nonContextual()).contains(Node.builder("wardrobe.slot." + permToRemove).build())) {
						Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cThe player does not have perm wardrobe.slot." + permToRemove + "!");
						success.set(false);
					} else {
						if (!user.getNodes().contains(Node.builder("wardrobe.slot." + permToRemove).build())) {
							Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cPlayer has perm wardrobe.slot." + permToRemove + " in one group, can not remove it!");
							success.set(false);
						} else {
							user.data().remove(Node.builder("wardrobe.slot." + permToRemove).build());
							success.set(true);
						}
					}

				});

				Common.runLater(2, () -> {

					if (!success.get()) return;

					wardrobeManager.removePermissionSlot(Integer.parseInt(permToRemove));
					Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &7Successfully remove perm &cwardrobe.slot." + permToRemove + "&7 from the player!");

				});

				return;

			}
			case "list": {

				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &aPlayer &9" + target.getName() + "&a has following slot perms:");
				for (Node node : luckPerms.getUserManager().getUser(target.getUniqueId()).resolveInheritedNodes(QueryOptions.nonContextual())) {
					String key = node.getKey();
					if (key.startsWith("wardrobe.slot."))
						if (luckPerms.getUserManager().getUser(target.getUniqueId()).getNodes().contains(Node.builder(key).build()))
							Common.tellNoPrefix(player, " ● &6" + key + " &7(Personal)");
						else
							Common.tellNoPrefix(player, " ● &6" + key + " &7(Group)");
				}
				return;

			}
			case "reload": {
				updatePermissionSlot(luckPerms.getUserManager().getUser(target.getUniqueId()));
				Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &7Successfully reload player perm info!");
			}
		}
	}

	private boolean invalidNumber(String str, Player player) {
		int slot;
		try {
			slot = Integer.parseInt(str);
		} catch (Exception ex) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou must type in a number great than 0!");
			return true;
		}
		if (slot <= 0) {
			Common.tellNoPrefix(player, "&f[&#df9820W&#bf9a40a&#9f9d60r&#80a080d&#60a29fr&#40a5bfo&#20a7dfb&#00aaffe&f] &cYou must type in a number great than 0!");
			return true;
		}
		return false;
	}

	private void updatePermissionSlot(User user) {
		List<Integer> permissions = new ArrayList<>();
		for (Node node : user.resolveInheritedNodes(QueryOptions.nonContextual())) {
			String key = node.getKey();
			if (key.startsWith("wardrobe.slot."))
				permissions.add(Integer.valueOf(key.replace("wardrobe.slot.", "")));
		}
		WARDROBE_DATABASE.updatePermissionSlot(Bukkit.getPlayer(user.getUniqueId()), permissions);
	}

}
