package com.anvizent.scheduler.service.sql.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.anvizent.scheduler.model.User;

public class UsersListRowMapper implements RowMapper<User> {

	public User mapRow(ResultSet paramResultSet, int paramInt) throws SQLException {
		return new User(paramResultSet.getLong("userId"),paramResultSet.getString("user_name") , paramResultSet.getString("user_name"), paramResultSet.getString("role_name"));
	}
	
}
