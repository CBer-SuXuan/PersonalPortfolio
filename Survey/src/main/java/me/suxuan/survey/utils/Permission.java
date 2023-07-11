package me.suxuan.survey.utils;

import me.suxuan.survey.PlayerCache;
import me.suxuan.survey.SurveyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class Permission {

	public static void addPerm(Player player, String perm) {

		// Give permission
		PermissionAttachment attachment = player.addAttachment(SurveyPlugin.getInstance());
		PlayerCache.getPerms().put(player.getUniqueId(), attachment);
		PermissionAttachment pperms = PlayerCache.getPerms().get(player.getUniqueId());
		pperms.setPermission(perm, true);

	}

	public static void removePerm(Player player) {
		PlayerCache.getPerms().remove(player.getUniqueId());
	}

}
