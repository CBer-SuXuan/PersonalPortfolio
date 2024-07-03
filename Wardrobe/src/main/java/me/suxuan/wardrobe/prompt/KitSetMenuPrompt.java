package me.suxuan.wardrobe.prompt;

import me.suxuan.wardrobe.settings.MenuSetting;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

public class KitSetMenuPrompt implements Prompt {
	@NotNull
	@Override
	public String getPromptText(@NotNull ConversationContext conversationContext) {
		return Common.colorize("&f-----------------------------------------------\n" +
				"&6Enter the title of the Kit Set.\n" +
				"&7(What you type in will not send to other players!)\n" +
				"&aCurrent: &f") + MenuSetting.getInstance().getKitSetTitle() + "\n" +
				Common.colorize("&7Type &dexit &7to cancel set kit set title.\n" +
						"&7Will automatically cancel in 2 minutes.\n" +
						"&f-----------------------------------------------");
	}

	@Override
	public boolean blocksForInput(@NotNull ConversationContext conversationContext) {
		return true;
	}

	@Nullable
	@Override
	public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
		context.getForWhom().sendRawMessage(Common.colorize("&aThe title of the Kit Set has been set to: &r") + input);
		context.setSessionData(Result.TITLE, input);
		return END_OF_CONVERSATION;
	}

	public enum Result {
		TITLE
	}
}
