package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.EltLoadParameters;

public interface ELTLoadParametersDao {

	List<EltLoadParameters> fetchLoadParametersList(JdbcTemplate commonJdbcTemplate);

	EltLoadParameters fetchLoadParametersById(long id, JdbcTemplate commonJdbcTemplate);

	long addLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate);

	long updateLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate);

	void deleteLoadParametersById(long id, JdbcTemplate commonJdbcTemplate);

}
