package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.EltLoadParameters;

public interface ELTLoadParametersService {

	List<EltLoadParameters> getLoadParametersList(JdbcTemplate commonJdbcTemplate);

	EltLoadParameters getLoadParametersById(long id, JdbcTemplate commonJdbcTemplate);

	long saveLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate);

	EltLoadParameters deleteLoadParametersById(long id, JdbcTemplate commonJdbcTemplate);
	
}
