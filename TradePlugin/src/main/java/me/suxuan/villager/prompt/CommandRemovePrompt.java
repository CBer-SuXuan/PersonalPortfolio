package me.suxuan.villager.prompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

import java.util.List;

public final class CommandRemovePrompt extends NumericPrompt {

    private final int all;
    private final List<String> commands;

    public CommandRemovePrompt(int all, List<String> commands) {
        this.all = all;
        this.commands = commands;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        if (conversationContext.getSessionData("index") != null) {
            if ((int) conversationContext.getSessionData("index") == -1)
                return Common.colorize("                               &7All commands now:\n" + listToString(commands));
        }
        return Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &bType command number you want to &c&ldelete&b. \n" +
                "&cVi&6lla&eger&aSh&bop &7>> &bFrom 1 to " + all + " &b(Type quit to cancel).\n" +
                "&cVi&6lla&eger&aSh&bop &7>> &7All commands now:\n" + listToString(commands));
    }

    private String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String command : list) {
            builder.append("                               ").append(command).append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean blocksForInput(@NotNull ConversationContext conversationContext) {
        return true;
    }

    @Override
    protected @NotNull String getFailedValidationText(@NotNull ConversationContext context, @NotNull Number invalidInput) {
        context.setSessionData("index", -1);
        if (all == 1)
            return Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &cNumber is only 1");
        return Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &cNumber is from 1 to " + all);
    }

    @Override
    protected boolean isNumberValid(@NotNull ConversationContext context, @NotNull Number input) {
        return input.intValue() > 0 && input.intValue() <= all;
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull Number number) {
        conversationContext.getForWhom().sendRawMessage(Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &6Command successful delete!'"));
        conversationContext.setSessionData(Delete.COMMAND, number.intValue());
        return END_OF_CONVERSATION;
    }

    public enum Delete {
        COMMAND
    }

}
