package me.littleme.invitationcodeplugin.commands;

import me.littleme.invitationcodeplugin.InvitationCodePlugin;
import me.littleme.invitationcodeplugin.config.ConfigMessage;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ResetCommand {

    private final InvitationCodePlugin main;

    public ResetCommand(InvitationCodePlugin main) {
        this.main = main;
    }

    public void resetUseTimes(Player player, UUID uuid) {

        if (main.getDatabase().isPlayerCodeInDatabase(uuid)) {
            if (main.getDatabase().getTimes(uuid) != 0) {
                main.getDatabase().resetTimes(uuid);
                player.sendMessage(main.manager.getMessage(ConfigMessage.RESET_TIMES_MESSAGE));
            } else {
                player.sendMessage(main.manager.getMessage(ConfigMessage.CODE_NOUSED_MESSAGE));
            }
        } else {
            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
        }

    }

    public void resetUsed(Player player, UUID uuid) {

        if (main.getDatabase().isPlayerUUIDInDatabase(uuid)) {
            if (main.getDatabase().getUsed(uuid) != 0) {
                main.getDatabase().resetUsed(uuid);
                player.sendMessage(main.manager.getMessage(ConfigMessage.RESET_USED_MESSAGE));
            } else {
                player.sendMessage(main.manager.getMessage(ConfigMessage.CODE_NOINPUT_MESSAGE));
            }
        } else {
            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
        }

    }

}
