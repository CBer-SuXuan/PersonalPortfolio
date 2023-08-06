package me.suxuan.villager.prompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;

public final class NamePrompt implements Prompt {
    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &bType the display name you want to change. \n" +
                "&cVi&6lla&eger&aSh&bop &7>> &bExample: &6") + "&b&lCustom trade " + Common.colorize("&b(Type quit to cancel).");
    }

    @Override
    public boolean blocksForInput(@NotNull ConversationContext conversationContext) {
        return true;
    }

    @Nullable
    @Override
    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        conversationContext.getForWhom().sendRawMessage(Common.colorize("&cVi&6lla&eger&aSh&bop &7>> &6Trade display name has been set to: '") + s + "'.");
        conversationContext.setSessionData(Name.NAME, s);
        return null;
    }

    public enum Name {
        NAME
    }

}
