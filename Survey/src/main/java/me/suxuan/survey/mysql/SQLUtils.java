package me.suxuan.survey.mysql;

import me.suxuan.survey.SurveyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class SQLUtils {

	private String host;
	private String username;
	private String password;

	private Connection con;

	public Connection getConnection() {
		return this.con;
	}

	public boolean connect() {
		try {
			long startTime = System.currentTimeMillis();

			con = DriverManager.getConnection(host, username, password);

			long endTime = System.currentTimeMillis();
			SurveyPlugin.getInstance().getLogger().info("Connected to database in " + (endTime - startTime) + " ms");
			return true;
		} catch (SQLException e) {
			SurveyPlugin.getInstance().getLogger().warning("Error while connecting to database, maybe you don't modify yml file!");
			return false;
		}
	}

	public ResultSet query(String query, Object... args) {
		boolean canContinue = checkConnection();
		if (!canContinue)
			return null;
		try {
			PreparedStatement ps = con.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			return ps.executeQuery();
		} catch (SQLException e) {
			if (connect())
				return query(query, args);
			return null;
		}
	}

	public void asyncQuery(final Callback<ResultSet> callback, String query, Object... args) {
		boolean canContinue = checkConnection();
		if (!canContinue)
			return;

		// This creates an async querry with the callback you can run a function after
		// the results.
		Bukkit.getScheduler().runTaskAsynchronously(SurveyPlugin.getInstance(), new Runnable() {
			@Override
			public void run() {

				try {
					PreparedStatement ps = con.prepareStatement(query);
					for (int i = 0; i < args.length; i++) {
						ps.setObject(i + 1, args[i]);
					}
					ResultSet result = ps.executeQuery();

					if (result.next()) {
						callback.onSuccess(result);
						return;
					}
					callback.onDataNotFound();
				} catch (SQLException ex) {
					callback.onException(ex.getCause());
					ex.printStackTrace();
				}
			}
		});
	}

	public int update(String query, Object... args) {
		try {
			boolean canContinue = checkConnection();
			if (!canContinue)
				return 0;
			PreparedStatement ps = con.prepareStatement(query);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			return ps.executeUpdate();
		} catch (SQLException e) {
			if (connect())
				return update(query, args);
			return 0;
		}
	}

	public void asyncUpdate(String query, Object... args) {
		Bukkit.getScheduler().runTaskAsynchronously(SurveyPlugin.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					boolean canContinue = checkConnection();
					if (!canContinue)
						return;
					PreparedStatement ps = con.prepareStatement(query);
					for (int i = 0; i < args.length; i++) {
						ps.setObject(i + 1, args[i]);
					}
					ps.executeUpdate();
				} catch (SQLException e) {
					if (connect())
						asyncUpdate(query, args);
					e.printStackTrace();
				}
			}
		});
	}

	public boolean playerInDatabase(Player p, String databaseName) {
		try {
			PreparedStatement ps = SurveyPlugin.getInstance().getSql().getConnection()
					.prepareStatement("SELECT * FROM " + databaseName + " WHERE UUID=?");
			ps.setString(1, p.getUniqueId().toString());
			ResultSet results = ps.executeQuery();
			return results.next();
		} catch (Exception ex) {
			if (connect())
				return playerInDatabase(p, databaseName);
			return false;
		}
	}

	/**
	 * reconnect if the sql server died and disconnected
	 */

	public boolean checkConnection() {
		if (con == null) {
			SurveyPlugin.getInstance().getLogger()
					.warning("Cannot use 'con.isClosed()' in 'checkConnection()' because 'con' is null.");
			return false;
		}

		try {
			if (con.isClosed()) {
				connect();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void disconnect() {
		if (this.con == null)
			return;
		try {
			con.close();
		} catch (SQLException e) {
			SurveyPlugin.getInstance().getLogger().warning("Error while disconnecting from database");
			e.printStackTrace();
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean pluginIsConnected() {
		return (this.con != null);
	}

}
