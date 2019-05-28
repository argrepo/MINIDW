package com.anvizent.scheduler.service.sql.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.anvizent.scheduler.model.User;

public class UserExtractor implements ResultSetExtractor<User> {


	@Override
	public User extractData(ResultSet paramResultSet) throws SQLException, DataAccessException {
		if (paramResultSet.next()) {
			User user = new User(paramResultSet.getLong("userId"),paramResultSet.getString("user_name") , paramResultSet.getString("user_name"), paramResultSet.getString("role_name"));
			user.setPassword("12345");
			return user;
		} else {
			return null;
		}
	}
	
}
