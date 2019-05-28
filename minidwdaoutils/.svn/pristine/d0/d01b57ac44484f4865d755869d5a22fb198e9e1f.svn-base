package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.Industry;

public interface MigrationDao {

	List<Industry> getMasterVerticals(List<Integer> verticalIds, JdbcTemplate clientJdbcTemplate);

	int saveNotMappedVerticals(Industry industry, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getMasterDataBases(List<Integer> databaseIds, JdbcTemplate commonJdbcTemplate);

	int saveNotMappedDatabases(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getMasterConnectors(List<Integer> connectorsIds, JdbcTemplate commonJdbcTemplate);

	int saveNotMappedConnectors(Database database, JdbcTemplate clientAppDbJdbcTemplate);

}
