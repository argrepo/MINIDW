package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.dao.MigrationDao;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.Industry;

public class MigrationServiceImpl implements MigrationService{
	
	@Autowired
	private MigrationDao migrationDao;
	
	public MigrationServiceImpl(MigrationDao migrationDao) {
		
		this.migrationDao = migrationDao;
	}

	@Override
	public MigrationDao getMigrationDao() {
		return migrationDao;
	}

	@Override
	public List<Industry> getMasterVerticals(List<Integer> verticalIds, JdbcTemplate clientJdbcTemplate) {
		return migrationDao.getMasterVerticals(verticalIds, clientJdbcTemplate);
	}

	@Override
	public int saveNotMappedVerticals(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		return migrationDao.saveNotMappedVerticals(industry,clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getMasterDataBases(List<Integer> databaseIds, JdbcTemplate commonJdbcTemplate) {
		return migrationDao.getMasterDataBases(databaseIds,commonJdbcTemplate);
	}

	@Override
	public int saveNotMappedDatabases(Database database, JdbcTemplate clientAppDbJdbcTemplate) {
		return migrationDao.saveNotMappedDatabases(database,clientAppDbJdbcTemplate);
	}

	@Override
	public List<Database> getMasterConnectors(List<Integer> connectorsIds, JdbcTemplate commonJdbcTemplate) {
		return migrationDao.getMasterConnectors(connectorsIds,commonJdbcTemplate);
	}

	@Override
	public int saveNotMappedConnectors(Database database, JdbcTemplate clientAppDbJdbcTemplate) {
		return migrationDao.saveNotMappedConnectors(database,clientAppDbJdbcTemplate);
	}

}
