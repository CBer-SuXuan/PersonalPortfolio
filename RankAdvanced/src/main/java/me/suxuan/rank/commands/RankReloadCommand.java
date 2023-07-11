package me.suxuan.rank.commands;

import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CBer_SuXuan
 * @className RankReloadCommand
 * @date 2023/5/8 22:29
 * @description
 */
public class RankReloadCommand extends SimpleSubCommand {

	public RankReloadCommand(SimpleCommandGroup parent) {
		super(parent, "reload");
		setAutoHandleHelp(false);
		setPermission("rank.command.reload");
		setPermissionMessage("&cYou don't have permission to this command");
		setDescription("Reload settings.yml");
	}

	@Override
	protected void onCommand() {
		setTellPrefix("&7[&e&lR&b&lS&7] ");
		try {
			Messenger.warn(sender, "&eReloading plugin's data, please wait..");

			// Syntax check YML files before loading
			boolean syntaxParsed = true;

			final List<File> yamlFiles = new ArrayList<>();

			this.collectYamlFiles(SimplePlugin.getData(), yamlFiles);

			for (final File file : yamlFiles)
				try {
					YamlConfig.fromFile(file);

				} catch (final Throwable t) {
					t.printStackTrace();

					syntaxParsed = false;
				}

			if (!syntaxParsed) {
				Messenger.error(sender, "&cThere was a problem loading files from your disk! See the console for more information.");
				return;
			}

			SimplePlugin.getInstance().reload();
			Messenger.success(sender, "&6Plugin has been reloaded.");

		} catch (final Throwable t) {
			Messenger.error(sender, "&cReloading failed! See the console for more information. Error: {error}".replace("{error}", t.getMessage() != null ? t.getMessage() : "unknown"));
			t.printStackTrace();
		}
	}

	private List<File> collectYamlFiles(File directory, List<File> list) {

		if (directory.exists())
			for (final File file : directory.listFiles()) {
				if (file.getName().endsWith("yml"))
					list.add(file);

				if (file.isDirectory())
					this.collectYamlFiles(file, list);
			}

		return list;
	}

	@Override
	protected List<String> tabComplete() {
		return NO_COMPLETE;
	}

}
