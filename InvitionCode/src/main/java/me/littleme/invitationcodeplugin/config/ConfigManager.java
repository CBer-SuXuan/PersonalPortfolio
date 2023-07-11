package me.littleme.invitationcodeplugin.config;

import me.littleme.invitationcodeplugin.InvitationCodePlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private YamlConfiguration yamlConfiguration;

    public ConfigManager(InvitationCodePlugin main) {
        File file = new File(main.getDataFolder(), main.getConfig().getString("language") + ".yml");
        if (file.exists()) {
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            System.out.println("\033[31m" + "Invitation Code Plugin couldn't start as an invalid " +
                    "language file was selected in config.yml!" + "\033[0m");
        }
    }

    public String getMessage(ConfigMessage message) {
        return yamlConfiguration.getString(message.name().toLowerCase());
    }

}
