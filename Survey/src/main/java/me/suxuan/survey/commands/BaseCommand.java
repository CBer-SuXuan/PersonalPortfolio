package me.suxuan.survey.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class BaseCommand implements TabExecutor {

	public List<String> matchInput(String input, List<String> toMatch) {
		List<String> toReturn = new ArrayList<>();
		for (String string : toMatch)
			if (string.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT))) toReturn.add(string);
		return toReturn;
	}

	public List<String> getPlayerNames() {
		List<String> toReturn = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) toReturn.add(player.getName());
		return toReturn;
	}
}
