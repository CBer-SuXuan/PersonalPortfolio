package me.suxuan.survey.commands;

import me.suxuan.survey.PlayerCache;
import me.suxuan.survey.SurveyPlugin;
import me.suxuan.survey.prompt.SurveyPrompt;
import me.suxuan.survey.utils.Log;
import me.suxuan.survey.utils.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends BaseCommand {

	private final SurveyPlugin instance = SurveyPlugin.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length > 1) return false;
		String type = args[0];
		switch (type) {
			case "reload":
				instance.onReload();
				break;
			case "start": {
				Player player = (Player) sender;
				if (!player.hasPermission("survey.command.start")) return false;
				PlayerCache.getQuestion().remove(player.getUniqueId());
				player.beginConversation(makeConversation(player));
				break;
			}
			case "restart": {
				Player player = (Player) sender;
				player.beginConversation(makeConversation(player));
				break;
			}
		}
		return false;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return new ArrayList<>();
	}

	private Conversation makeConversation(Player player) {
		return new ConversationFactory(SurveyPlugin.getInstance())
				.addConversationAbandonedListener(event -> {
					if (event.gracefulExit()) {

						// Remove permission
						Permission.removePerm(player);

						// Answer all question!
						player.sendMessage(Log.colorize(SurveyPlugin.complete_all));
					}
				})
				.withLocalEcho(false)
				.withFirstPrompt(new SurveyPrompt())
				.buildConversation(player);
	}

}
