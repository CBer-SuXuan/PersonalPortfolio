package me.suxuan.survey.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Callback<T> {

	void onSuccess(ResultSet result) throws SQLException;

	void onException(Throwable cause);

	void onDataNotFound();

}
