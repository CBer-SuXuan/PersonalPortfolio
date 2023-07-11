package me.littleme.invitationcodeplugin.playerSQL;

import me.littleme.invitationcodeplugin.InvitationCodePlugin;

import java.util.logging.Level;

public class Error {
    public static void execute(InvitationCodePlugin plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "\033[31m不能执行MySQL语句:\033[0m", ex);
    }
    public static void close(InvitationCodePlugin plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "\033[31m关闭MySQL连接失败:\033[0m", ex);
    }
}
