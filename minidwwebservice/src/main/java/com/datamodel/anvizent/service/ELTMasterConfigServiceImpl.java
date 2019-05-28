package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.ELTMasterConfigDao;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;

@Service
public class ELTMasterConfigServiceImpl implements ELTMasterConfigService {

	@Autowired
	ELTMasterConfigDao eltmasterConfigDao;

	@Override
	public List<EltMasterConfiguration> getAllMasterConfigList(JdbcTemplate commonJdbcTemplate) {
		return eltmasterConfigDao.fetchAllMasterConfigList(commonJdbcTemplate);
	}

	@Override
	public EltMasterConfiguration getMasterConfigById(long id, JdbcTemplate commonJdbcTemplate) {
		
		EltMasterConfiguration masterConfigById = eltmasterConfigDao.fetchMasterConfigById(id, commonJdbcTemplate);
		if (masterConfigById == null) {
			throw new RuntimeException("Master Configuration not found with id " + id  );
		} else {
			masterConfigById.setEnvironmentVariables(eltmasterConfigDao.fetchMasterConfigEnvironmentVariablesByMappingId(id, commonJdbcTemplate));
		}
		return masterConfigById;
	}

	@Override
	public long saveMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate) {
		long masterConfigId = 0;
		if(eltMasterConfiguration.getMasterDefault()){
			eltmasterConfigDao.updateMasterConfigDefault(null);
		}
		if (eltMasterConfiguration.getId() == 0) {
			masterConfigId = eltmasterConfigDao.addMasterConfig(eltMasterConfiguration, commonJdbcTemplate);
		} else {
			eltmasterConfigDao.updateMasterConfig(eltMasterConfiguration, commonJdbcTemplate);
			masterConfigId = eltMasterConfiguration.getId();
			eltmasterConfigDao.deleteMasterConfigEnvironmentVariablesByMappingId(masterConfigId, commonJdbcTemplate);
		}
		if (eltMasterConfiguration.getEnvironmentVariables() != null && eltMasterConfiguration.getEnvironmentVariables().size() > 0) { 
			eltmasterConfigDao.addMasterConfigEnvironmentVariablesByMappingId(masterConfigId, eltMasterConfiguration.getEnvironmentVariables(), commonJdbcTemplate);
		}
		
		return masterConfigId;
	}

	@Override
	public EltMasterConfiguration deleteMasterConfigById(long id, JdbcTemplate commonJdbcTemplate) {
		EltMasterConfiguration masterConfigById = getMasterConfigById(id, commonJdbcTemplate);
		eltmasterConfigDao.deleteMasterConfigById(id, commonJdbcTemplate);
		return masterConfigById;
	}

	@Override
	public int updateMasterConfigDefault(JdbcTemplate commonJdbcTemplate) {
		return eltmasterConfigDao.updateMasterConfigDefault(commonJdbcTemplate);
	}

}
