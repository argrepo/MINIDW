package com.datamodel.anvizent.service.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.MigrationDao;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.Industry;

public class MigrationDaoImpl implements MigrationDao{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ETLAdminDaoImpl.class);

	private SqlHelper sqlHelper;

	public MigrationDaoImpl() {
		try {
			this.sqlHelper = new SqlHelper(MigrationDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating MigrationDaoImpl SqlHelper.", ex);
		}
	}
	
	
	@Override
	public List<Industry> getMasterVerticals(List<Integer> verticalIds, JdbcTemplate clientJdbcTemplate) {
		List<Industry> industries = null;
		
		StringBuilder builder = new StringBuilder();
		verticalIds.forEach(value -> {
			builder.append("?,");
		});

		try {
			String sql = StringUtils.replace(sqlHelper.getSql("getMasterVerticals"), "{verticalIds}", builder.deleteCharAt(builder.length() - 1).toString());
			industries = clientJdbcTemplate.query(sql, verticalIds.toArray(),new RowMapper<Industry>() {

				@Override
				public Industry mapRow(ResultSet rs, int arg1) throws SQLException {
					Industry industry = new Industry();
					industry.setId(rs.getInt("id"));
					industry.setName(rs.getString("name"));
					industry.setDescription(rs.getString("description"));
					industry.setIsActive(rs.getBoolean("isActive"));
					industry.setIsDefault(rs.getBoolean("is_default"));
					return industry;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistingVerticals()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistingVerticals()", e);
			
			throw new AnvizentRuntimeException(e);
		}
		return industries;
	}

	@Override
	public int saveNotMappedVerticals(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("saveNotMappedVerticals");
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						industry.getId(),
						industry.getName(),
						industry.getDescription(),
						industry.getIsActive(),
				});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveNotMappedVerticals()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveNotMappedVerticals()", e);
			
		}
		return update;
	}


	@Override
	public List<Database> getMasterDataBases(List<Integer> databaseIds, JdbcTemplate commonJdbcTemplate) {
		List<Database> dataBaseList = null;
		StringBuilder builder = new StringBuilder();
		databaseIds.forEach(value -> {
			builder.append("?,");
		});

		try {
			String sql = StringUtils.replace(sqlHelper.getSql("getMasterDatabases"), "{databaseIds}", builder.deleteCharAt(builder.length() - 1).toString());
			dataBaseList = commonJdbcTemplate.query(sql, databaseIds.toArray(),new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
						database.setId(rs.getInt("id"));
						database.setName(rs.getString("name"));
						database.setDriverName(rs.getString("driver_name"));
						database.setProtocal(rs.getString("protocal"));
						database.setUrlFormat(rs.getString("url_format"));
						database.setIsActive(rs.getBoolean("isActive"));
					return database;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistingVerticals()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistingVerticals()", e);
			
			throw new AnvizentRuntimeException(e);
		}
		return dataBaseList;
	}


	@Override
	public int saveNotMappedDatabases(Database database, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("saveNotMappedDatabases");
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						database.getId(),
						database.getName(),
						database.getDriverName(),
						database.getProtocal(),
						database.getUrlFormat(),
						database.getIsActive()
				});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveNotMappedDatabases()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveNotMappedDatabases()", e);
			
		}
		return update;
	}


	@Override
	public List<Database> getMasterConnectors(List<Integer> connectorsIds, JdbcTemplate commonJdbcTemplate) {
		List<Database> connectorList = null;
		StringBuilder builder = new StringBuilder();
		connectorsIds.forEach(value -> {
			builder.append("?,");
		});

		try {
			String sql = StringUtils.replace(sqlHelper.getSql("getMasterConnectors"), "{connectorsIds}", builder.deleteCharAt(builder.length() - 1).toString());
			connectorList = commonJdbcTemplate.query(sql, connectorsIds.toArray(),new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("connectorid"));
					database.setConnector_id(rs.getInt("database_id"));
					database.setConnectorName(rs.getString("connector_name"));
					database.setName(rs.getString("database_name"));
					database.setIsActive(rs.getBoolean("isActive"));
					database.setIsDefault(rs.getBoolean("is_default"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					return database;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getMasterConnectors()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMasterConnectors()", e);
			
			throw new AnvizentRuntimeException(e);
		}
		return connectorList;
	}


	@Override
	public int saveNotMappedConnectors(Database database, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		int databaseId = 0;
		try {
			databaseId = getDbIds(database,clientAppDbJdbcTemplate);
			if(databaseId == 0){
				String sql = sqlHelper.getSql("createDataBase");
				update = clientAppDbJdbcTemplate.update(sql, new Object[] { 
						database.getConnector_id(),
						database.getName(), 
						database.getDriverName(),
						database.getProtocal(),
						database.getUrlFormat(),
						database.getConnectorJars(),
						database.getIsActive()
				});
				databaseId = saveNotMappedConnectors(database,clientAppDbJdbcTemplate);
			}else{
				String sql = sqlHelper.getSql("saveNotMappedConnectors");
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						database.getId(),
						database.getConnector_id(),
						database.getConnectorName(),
						database.getIsActive()
				});
			}
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveNotMappedConnectors()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveNotMappedConnectors()", e);
			
		}
		return update;
	}
	
	public int getDbIds(Database database, JdbcTemplate clientAppDbJdbcTemplate) {
		int databaseId = 0;
		
		try {
			String sqlPreCheck = sqlHelper.getSql("checkDatabaseIdExistOrNot");
			databaseId = clientAppDbJdbcTemplate.query(sqlPreCheck, new Object[] { database.getConnector_id() }, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getInt("id");
					} else {
						return 0;
					}
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDbIds()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDbIds()", e);
			
		}
		return databaseId;
	}

}
