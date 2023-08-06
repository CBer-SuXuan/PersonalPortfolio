package me.suxuan.villager.prompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

public final class CommandAddPrompt implements Prompt {

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &bType a command you want to &c&ladd &b. \n" +
                "&cVi&6lla&eger&aSh&bop &7>> &bExample: &6") + "give {player} diamond 10 " + Common.colorize("&b(Type quit to cancel).");
    }

    @Override
    public boolean blocksForInput(@NotNull ConversationContext conversationContext) {
        return true;
    }

    @Nullable
    @Override
    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        conversationContext.getForWhom().sendRawMessage(Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &6Commands add one: '") + s + "'.");
        conversationContext.setSessionData(Commands.COMMAND, s);
        return null;
    }

    public enum Commands {
        COMMAND
    }

}
