package me.littleme.invitationcodeplugin.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.littleme.invitationcodeplugin.ICCommand;
import me.littleme.invitationcodeplugin.InvitationCodePlugin;
import me.littleme.invitationcodeplugin.config.ConfigMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class GetCommand {

    private InvitationCodePlugin main;

    public GetCommand(InvitationCodePlugin main) {
        this.main = main;
    }

    public void useCommand(Player player, String[] args) {

        ICCommand icCommand = new ICCommand(main);

        int code = player.getUniqueId().hashCode();
        if (code <= 0) {
            code = -code;
        }
        if (args.length == 1) {
            if (icCommand.isOldPlayer(player)) {
                TextComponent click = new TextComponent(main.manager.getMessage(ConfigMessage.COPY_MESSAGE));
                click.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(code)));
                if (!main.getDatabase().isPlayerCodeInDatabase(player.getUniqueId())) {
                    if (!main.getDatabase().isPlayerUUIDInDatabase(player.getUniqueId())) {
                        main.getDatabase().setData(player);
                    }
                    main.getDatabase().setData(player, code);
                }
                player.sendMessage(PlaceholderAPI.setPlaceholders(player, main.manager.getMessage(ConfigMessage.SHOW_MESSAGE)));
                player.spigot().sendMessage(click);
            } else {
                player.sendMessage(main.manager.getMessage(ConfigMessage.NOT_OLD_MESSAGE));
                main.getDatabase().setCodeZero(player);
            }

        } else if (args.length == 2) {
            if (player.hasPermission("ic")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (!icCommand.isOldPlayer((Player) target)) {
                    main.getDatabase().setCodeZero(player);
                }
                if (!main.getDatabase().isPlayerCodeInDatabase(target.getUniqueId())) {
                    player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                } else {
                    player.sendMessage(PlaceholderAPI.setPlaceholders(target, main.manager.getMessage(ConfigMessage.GET_MESSAGE)));
                }
            } else {
                player.sendMessage(main.manager.getMessage(ConfigMessage.NO_ASSESS_MESSAGE));
            }
        } else {
            icCommand.onError(player);
        }

    }

    public String getTimeLeft(Player player) {
        double first = Bukkit.getOfflinePlayer(player.getUniqueId()).getFirstPlayed();
        double now = new Date().getTime();
        double time = (now - first) / 1000 / 3600;
        return String.format("%.1f",main.getConfig().getDouble("time_as_old_player") - time);
    }

}
