package me.suxuan.survey;

import me.suxuan.survey.commands.MainCommand;
import me.suxuan.survey.listeners.JoinEvent;
import me.suxuan.survey.listeners.KillEvent;
import me.suxuan.survey.mysql.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class SurveyPlugin extends JavaPlugin {

	public static List<String> questions;
	public static int min;
	public static String invalid;
	public static String complete_one;
	public static String complete_all;
	public static String button;
	public static String restart;
	public static double interval;
	private static SurveyPlugin instance;
	private SQLUtils sql;

	@Override
	public void onEnable() {
		SurveyPlugin.instance = this;

		if (!checkFirstLoad()) {

			onReload();
			registerEvents();

		} else
			this.getLogger().log(Level.WARNING, "Modify config.yml in Surveys folder, after modifying use /sv reload");

		// Register things
		registerCommands();

		// Save default config
		saveDefaultConfigResource();
		reloadConfig();

		if (sql != null)
			this.getLogger().log(Level.INFO, "Survey plugin load successfully");
	}

	public void onReload() {

		reloadConfig();

		// Connect to the MySQL
		sql = new SQLUtils();
		parseSql();

		// Initial some variable
		questions = getConfig().getStringList("questions");
		min = getConfig().getInt("min");
		invalid = getConfig().getString("invalid_message");
		complete_one = getConfig().getString("complete_one_message");
		complete_all = getConfig().getString("complete_all_message");
		button = getConfig().getString("button_message");
		restart = getConfig().getString("restart_message");
		interval = getConfig().getDouble("interval");

	}

	void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new KillEvent(), this);
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
	}

	void registerCommands() {
		Objects.requireNonNull(getCommand("survey")).setExecutor(new MainCommand());
	}

	boolean checkFirstLoad() {
		String path = "config.yml";
		File file = new File(getDataFolder(), path);
		return !file.exists();
	}

	void saveDefaultConfigResource() {
		String path = "config.yml";
		File file = new File(getDataFolder(), path);
		if (!file.exists()) this.saveResource(path, false);
	}

	void parseSql() {
		sql.setHost(getConfig().getString("connection.host", "jdbc:mysql://localhost:3306/test"));
		sql.setUsername(getConfig().getString("connection.username", "root"));
		sql.setPassword(getConfig().getString("connection.password", ""));
		sql.connect();
		createTable();
	}

	void createTable() {
		sql.asyncUpdate("CREATE TABLE IF NOT EXISTS survey (" +
				"uuid VARCHAR(50) NOT NULL, " +
				"name VARCHAR(50) NOT NULL, " +
				"answer1 LONGTEXT, " +
				"answer2 LONGTEXT, " +
				"answer3 LONGTEXT, " +
				"last_survey VARCHAR(32));");
		sql.asyncUpdate("CREATE TABLE IF NOT EXISTS kill_number (" +
				"`uuid` VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY, " +
				"`name` VARCHAR(50) NOT NULL, " +
				"`kill` INT(8));");
	}

	public static SurveyPlugin getInstance() {
		return SurveyPlugin.instance;
	}

	public SQLUtils getSql() {
		return sql;
	}
}
