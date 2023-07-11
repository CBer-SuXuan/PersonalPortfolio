package me.littleme.invitationcodeplugin.tab;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ICTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("ic")) {
                    return StringUtil.copyPartialMatches(args[0], Arrays.asList("reset","help","test","get","add","remove","reload","take","old","new"), new ArrayList<>());
                } else {
                    return StringUtil.copyPartialMatches(args[0], Arrays.asList("get","help"), new ArrayList<>());
                }
            } else if (args.length == 2) {
                if (!player.hasPermission("ic")) {
                    return new ArrayList<>();
                } else {
                    if (args[0].equalsIgnoreCase("help")) {
                        return new ArrayList<>();
                    } else {
                        List<String> names = new ArrayList<>();
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            names.add(target.getName());
                        }
                        return StringUtil.copyPartialMatches(args[1], names, new ArrayList<>());
                    }
                }
            } else if (args.length == 3) {
                if (!player.hasPermission("ic")) {
                    return new ArrayList<>();
                } else {
                    if (args[0].equalsIgnoreCase("reset")) {
                        return StringUtil.copyPartialMatches(args[2], Arrays.asList("used","times"), new ArrayList<>());
                    }
                }
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
