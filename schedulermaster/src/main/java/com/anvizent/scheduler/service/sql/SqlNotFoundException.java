package com.anvizent.scheduler.service.sql;

import java.sql.SQLException;

/**
 * SQL not found exception.
 * 
 * @author
 *
 */
public class SqlNotFoundException extends SQLException {
	private static final long serialVersionUID = 1L;

	public SqlNotFoundException(String sqlId) {
		super(String.format("SQL [%s] not found.", sqlId));
	}

}
