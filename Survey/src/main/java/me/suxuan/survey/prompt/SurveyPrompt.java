package me.suxuan.survey.prompt;

import me.suxuan.survey.PlayerCache;
import me.suxuan.survey.SurveyPlugin;
import me.suxuan.survey.utils.Log;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

public class SurveyPrompt extends ValidatingPrompt implements Prompt {

	private final SurveyPlugin instance = SurveyPlugin.getInstance();

	// Message send to the player every question.
	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		if (PlayerCache.getQuestion().isEmpty())
			PlayerCache.getQuestion().put(player.getUniqueId(), 0);
		return Log.colorize(SurveyPlugin.questions.get(PlayerCache.getQuestion().get(player.getUniqueId())));
	}

	// Check if the answer is valid.
	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		return input.contains(" ") && input.length() > SurveyPlugin.min;
	}

	// Message send to the player after invalid answer.
	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput) {
		return Log.colorize(SurveyPlugin.invalid);
	}

	// Run after player type in the valid answer.
	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {

		// Send complete one question message to player
		context.getForWhom().sendRawMessage(Log.colorize(SurveyPlugin.complete_one));
		Player player = (Player) context.getForWhom();

		// Check the first question.
		if (PlayerCache.getQuestion().get(player.getUniqueId()) == null) {

			PlayerCache.getQuestion().put(player.getUniqueId(), 1);
			PlayerCache.getAnswer().put(player.getName() + ":1", input);
			return new SurveyPrompt();

		}

		// Check the last question.
		if (PlayerCache.getQuestion().get(player.getUniqueId()) + 1 == SurveyPlugin.questions.size()) {
			PlayerCache.getAnswer().put(player.getName() + ":" + (PlayerCache.getQuestion().get(player.getUniqueId()) + 1), input);

			// Make time nice to look.
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			// Insert answer.
			instance.getSql().update("INSERT INTO survey(uuid, name, answer1, answer2, answer3, last_survey) VALUES ('" +
					player.getUniqueId().toString() + "', '" + player.getName() + "', '" +
					PlayerCache.getAnswer().get(player.getName() + ":1") + "', '" +
					PlayerCache.getAnswer().get(player.getName() + ":2") + "', '" +
					PlayerCache.getAnswer().get(player.getName() + ":3") + "', '" +
					format.format(System.currentTimeMillis()) + "');"
			);

			// Clear player's answer map
			for (int i = 1; i <= SurveyPlugin.questions.size(); i++) {
				PlayerCache.getAnswer().remove(player.getName() + ":" + i);
			}

			// Clear player's question map
			PlayerCache.getQuestion().remove(player.getUniqueId());
			return null;
		}

		// Put player's info into answer and question maps.
		PlayerCache.getAnswer().put(player.getName() + ":" + (PlayerCache.getQuestion().get(player.getUniqueId()) + 1), input);
		PlayerCache.getQuestion().put(player.getUniqueId(), PlayerCache.getQuestion().get(player.getUniqueId()) + 1);
		return new SurveyPrompt();

	}

}
