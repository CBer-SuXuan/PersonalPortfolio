package me.littleme.invitationcodeplugin;

import me.littleme.invitationcodeplugin.config.ConfigManager;
import me.littleme.invitationcodeplugin.config.ConfigMessage;
import me.littleme.invitationcodeplugin.placeholder.TimesPlaceholder;
import me.littleme.invitationcodeplugin.tab.ICTab;
import me.littleme.invitationcodeplugin.playerSQL.Database;
import me.littleme.invitationcodeplugin.playerSQL.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class InvitationCodePlugin extends JavaPlugin implements Listener {
    private Database db;
    FileConfiguration config;
    public ConfigManager manager;
    File file;
    @Override
    public void onEnable() {

        // create yml file
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        file = new File(getDataFolder(), "config.yml");

        // support multiple language
        saveResource("lang_en.yml", false);
        saveResource("lang_zh.yml", false);
        manager = new ConfigManager(this);

        // listener register
        Bukkit.getPluginManager().registerEvents(this, this);

        // command register
        Objects.requireNonNull(getCommand("ic")).setExecutor(new ICCommand(this));
        Objects.requireNonNull(getCommand("ic")).setTabCompleter(new ICTab());

        // database initialize
        this.db = new SQLite(this);
        this.db.load();

        // Add all player joined before to database
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!getDatabase().isPlayerUUIDInDatabase(player.getUniqueId())) {
                getDatabase().setData(player);
            }
        }

        // support Placeholder
        new TimesPlaceholder(this).register();

        System.out.println("\033[35m" + manager.getMessage(ConfigMessage.LOAD_MESSAGE) + "\033[0m");
    }

    // get database
    public Database getDatabase() {
        return this.db;
    }

    // Player join detection and add data to the database
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!getDatabase().isPlayerUUIDInDatabase(e.getPlayer().getUniqueId())) {
            getDatabase().setData(e.getPlayer());
        }
    }

}

