package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.ELTLoadParametersDao;
import com.datamodel.anvizent.service.model.EltLoadParameters;

@Service
public class ELTLoadParametersServiceImpl implements ELTLoadParametersService {

	@Autowired
	ELTLoadParametersDao eltLoadParametersDao;

	@Override
	public List<EltLoadParameters> getLoadParametersList(JdbcTemplate commonJdbcTemplate) {
		return eltLoadParametersDao.fetchLoadParametersList(commonJdbcTemplate);
	}

	@Override
	public EltLoadParameters getLoadParametersById(long id, JdbcTemplate commonJdbcTemplate) {
		
		EltLoadParameters loadParamsById = eltLoadParametersDao.fetchLoadParametersById(id, commonJdbcTemplate);
		if (loadParamsById == null) {
			throw new RuntimeException("Load parameters not found with id " + id  );
		}
		return loadParamsById;
	}

	@Override
	public long saveLoadParameters(EltLoadParameters eltLoadParameters, JdbcTemplate commonJdbcTemplate) {
		long loadParamsId = 0;
		if (eltLoadParameters.getId() == null || eltLoadParameters.getId() == 0) {
			loadParamsId = eltLoadParametersDao.addLoadParameters(eltLoadParameters, commonJdbcTemplate);
		} else {
			eltLoadParametersDao.updateLoadParameters(eltLoadParameters, commonJdbcTemplate);
			loadParamsId = eltLoadParameters.getId();
		}
		return loadParamsId;
	}

	@Override
	public EltLoadParameters deleteLoadParametersById(long id, JdbcTemplate commonJdbcTemplate) {
		EltLoadParameters loadParams = getLoadParametersById(id, commonJdbcTemplate);
		eltLoadParametersDao.deleteLoadParametersById(id, commonJdbcTemplate);
		return loadParams;
	}

}
