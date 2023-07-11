package me.suxuan.survey;

import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

	// Use to detect next question for each players
	private static final Map<UUID, Integer> question = new HashMap<>();
	// Use to save player answers
	private static final Map<String, String> answer = new HashMap<>();
	// Use to manage player permissions
	private static final Map<UUID, PermissionAttachment> perms = new HashMap<>();

	public static Map<UUID, Integer> getQuestion() {
		return question;
	}

	public static Map<String, String> getAnswer() {
		return answer;
	}

	public static Map<UUID, PermissionAttachment> getPerms() {
		return perms;
	}
}
