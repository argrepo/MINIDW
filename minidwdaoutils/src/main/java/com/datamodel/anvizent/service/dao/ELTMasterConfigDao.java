package com.datamodel.anvizent.service.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.Modification;

public interface ELTMasterConfigDao {

	List<EltMasterConfiguration> fetchAllMasterConfigList(JdbcTemplate commonJdbcTemplate);

	EltMasterConfiguration fetchMasterConfigById(long id, JdbcTemplate commonJdbcTemplate);

	long addMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate);

	long updateMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate);

	void deleteMasterConfigById(long id, JdbcTemplate commonJdbcTemplate);

	List<ELTConfigTags> fetchMasterConfigEnvironmentVariablesByMappingId(long id, JdbcTemplate commonJdbcTemplate);

	long addMasterConfigEnvironmentVariablesByMappingId(long id, List<ELTConfigTags> environmentVariables,
			JdbcTemplate commonJdbcTemplate);

	void deleteMasterConfigEnvironmentVariablesByMappingId(long id, JdbcTemplate commonJdbcTemplate);

	int updateMasterConfigDefault(JdbcTemplate commonJdbcTemplate);

	EltMasterConfiguration fetchDefaultMasterConfig(JdbcTemplate commonJdbcTemplate);

	void saveEltInitiatedInfo(long executionId, long packageId, long scheduleId, Integer dlId, int ilId, Object jobId,
			String executionResponsemap, Modification modification,JdbcTemplate commonJdbcTemplate);

	
}
