package me.littleme.invitationcodeplugin.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.littleme.invitationcodeplugin.ICCommand;
import me.littleme.invitationcodeplugin.InvitationCodePlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimesPlaceholder extends PlaceholderExpansion {
    private final InvitationCodePlugin main;

    public TimesPlaceholder(InvitationCodePlugin main) { this.main = main; }

    @Override
    public @NotNull String getIdentifier() {
        return "ic";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "Little_Me";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        Date current_time = new Date();
        ICCommand icCommand = new ICCommand(main);
        return switch (params) {
            case "new", "old" -> player.getName();
            case "times" -> String.valueOf(main.getDatabase().getTimes(player.getUniqueId()));
            case "isold" -> String.valueOf(icCommand.isOldPlayer((Player) player));
            case "isnew" -> String.valueOf(!icCommand.isOldPlayer((Player) player));
            case "time" -> getDistanceTime(current_time.getTime(), player.getFirstPlayed());
            case "code" -> String.valueOf(main.getDatabase().getCode(player.getUniqueId()));
            case "invited" -> main.getDatabase().getUsed(player.getUniqueId()) != 0 ? "true" : "false";
            default -> "!!papi went wrong, check the grammar!!";
        };
    }

    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + " Day "+hour + " Hour "+min + " Min " + sec + " Sec ";
        if (hour != 0) return hour + " Day "+ min + " Min " + sec + " Sec ";
        if (min != 0) return min + " Min " + sec + " Sec ";
        if (sec != 0) return sec + " Sec " ;
        return "0 Sec";
    }
}
