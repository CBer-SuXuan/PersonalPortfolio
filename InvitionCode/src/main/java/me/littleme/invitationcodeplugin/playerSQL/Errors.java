package me.littleme.invitationcodeplugin.playerSQL;

public class Errors {
    public static String sqlConnectionExecute(){
        return "\033[31m不能执行SQL语句:\033[0m";
    }
    public static String sqlConnectionClose(){
        return "\033[31m关闭MySQL连接失败:\033[0m";
    }
    public static String noSQLConnection(){
        return "\033[31m无法检索MySQL连接: \033[0m";
    }
    public static String noTableFound(){
        return "\033[31m数据库错误: 没有找到表\033[0m";
    }
}
