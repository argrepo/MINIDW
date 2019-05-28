package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.EltMasterConfiguration;

public interface ELTMasterConfigService {

	List<EltMasterConfiguration> getAllMasterConfigList(JdbcTemplate commonJdbcTemplate);

	EltMasterConfiguration getMasterConfigById(long id, JdbcTemplate commonJdbcTemplate);

	long saveMasterConfig(EltMasterConfiguration eltMasterConfiguration, JdbcTemplate commonJdbcTemplate);

	EltMasterConfiguration deleteMasterConfigById(long id, JdbcTemplate commonJdbcTemplate);

	int updateMasterConfigDefault(JdbcTemplate commonJdbcTemplate);

}
