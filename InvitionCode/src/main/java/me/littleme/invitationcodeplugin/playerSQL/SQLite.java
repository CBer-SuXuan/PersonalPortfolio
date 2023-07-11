package me.littleme.invitationcodeplugin.playerSQL;

import me.littleme.invitationcodeplugin.InvitationCodePlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {
    String dbname;
    public SQLite(InvitationCodePlugin instance) {
        super(instance);
        dbname = plugin.getConfig().getString("SQLite.Filename", "data");
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS player_data (" +
            "`PlayerName` varchar(128) NOT NULL," +
            "`UUID` varchar(40) PRIMARY KEY," +
            "`InviteTimes` int(10) DEFAULT 0," +
            "`UseCode` int(1) DEFAULT 0," +
            "`InvitationCode` int(20) DEFAULT 0," +
            "`ForceOldPlayer` int(1) DEFAULT 0," +
            "`ForceNewPlayer` int(1) DEFAULT 0" +
            ");";

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "\033[31m文件写入失败: \033[0m"+dbname+".db！");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"\033[31mSQLite初始化异常！\033[0m", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "\033[31m程序出错！\033[0m");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
