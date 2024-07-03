package me.suxuan.wardrobe.prompt;

import me.suxuan.wardrobe.settings.MenuSetting;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

public class WardrobeMenuPrompt implements Prompt {
	@NotNull
	@Override
	public String getPromptText(@NotNull ConversationContext conversationContext) {
		return Common.colorize("&f-----------------------------------------------\n" +
				"&6Enter the title of the wardrobe.\n" +
				"&7(What you type in will not send to other players!)\n" +
				"&aCurrent: &f") + MenuSetting.getInstance().getWardrobeTitle() + "\n" +
				Common.colorize("&bWill convert '{current}' to current page number\n" +
						"&band convert '{total}' to the total page number.\n" +
						"&7Type &dexit &7to cancel set wardrobe title.\n" +
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
		context.getForWhom().sendRawMessage(Common.colorize("&aThe title of the wardrobe has been set to: &r") + input);
		context.setSessionData(Result.TITLE, input);
		return END_OF_CONVERSATION;
	}

	public enum Result {
		TITLE
	}
}
