package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.dao.MigrationDao;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.Industry;

public interface MigrationService {

	
	List<Industry> getMasterVerticals(List<Integer> verticalIds,JdbcTemplate clientJdbcTemplate);

	int saveNotMappedVerticals(Industry industry, JdbcTemplate clientAppDbJdbcTemplate);

	MigrationDao getMigrationDao();

	List<Database> getMasterDataBases(List<Integer> databaseIds, JdbcTemplate commonJdbcTemplate);

	int saveNotMappedDatabases(Database database, JdbcTemplate clientAppDbJdbcTemplate);

	List<Database> getMasterConnectors(List<Integer> connectorsIds, JdbcTemplate commonJdbcTemplate);

	int saveNotMappedConnectors(Database database, JdbcTemplate clientAppDbJdbcTemplate);
}
