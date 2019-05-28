package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.dao.DataValidationDao;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationIntegration;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.JobResult;

public class DataValidationServiceImpl implements DataValidationService {

	protected final Logger logger = LoggerFactory.getLogger(DataValidationServiceImpl.class);

	@Autowired
	private DataValidationDao dataValidationDao;

	public DataValidationServiceImpl(DataValidationDao dataValidationDao) {
		this.dataValidationDao = dataValidationDao;
	}

	@Override
	public List<DataValidation> getValidationInfo(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		logger.info("in DataValidationServiceImpl.... getValidationInfo");
		return dataValidationDao.getValidationInfo(validationTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public Integer saveDataValidation(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {
		logger.info("in DataValidationServiceImpl.... saveDataValidation");
		return dataValidationDao.saveDataValidation(dataValidation, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveScriptDlMapping(int createValdationId, DataValidation dataValidation,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.saveScriptDlMapping(createValdationId, dataValidation, clientAppDbJdbcTemplate);
	}

	@Override
	public DataValidation getValidationScriptDataById(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getValidationScriptDataById(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DLInfo> getDLInfoByValidationScriptId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getDLInfoByValidationScriptId(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDataValidationScript(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.updateDataValidationScript(dataValidation, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteScriptDlMapping(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.deleteScriptDlMapping(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public DataValidationIntegration getDataValidationIntegration(JdbcTemplate commonJdbcTemplate) {
		return dataValidationDao.getDataValidationIntegration(commonJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobExecutionResults(String clientId, Integer scriptId, JdbcTemplate clientJdbcTemplate) {
		return dataValidationDao.getJobExecutionResults(clientId, scriptId, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getValidationScriptErrorLogs(String batchId, JdbcTemplate clientJdbcTemplate) {
		return dataValidationDao.getValidationScriptErrorLogs(batchId, clientJdbcTemplate);
	}

	@Override
	public int saveScriptIlMapping(int createValdationId, DataValidation dataValidation,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.saveScriptIlMapping(createValdationId, dataValidation, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteScriptilMapping(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.deleteScriptilMapping(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DataValidation> getPreloadValidationInfoByConnectionId(Integer validationTypeId, Integer connectionId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getPreloadValidationInfoByConnectionId(validationTypeId, connectionId,
				clientAppDbJdbcTemplate);
	}

	@Override
	public DataValidation getValidationScriptByScriptId(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getValidationScriptByScriptId(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<DataValidation> getSelectedValidationByIds(String scriptIds,Integer connectionId,JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getValidationScriptByScriptId(scriptIds,connectionId,clientAppDbJdbcTemplate);
	}

	@Override
	public int saveDataValidationContextParams(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.saveDataValidationContextParams(dataValidationType, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteValidationContextParams(int dataValidationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.deleteValidationContextParams(dataValidationTypeId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<String> getContextParamsByValidationId(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getContextParamsByValidationId(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public Map<String, String> getDataValidationContextParams(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getDataValidationContextParams(scriptId, clientAppDbJdbcTemplate);
	}

	@Override
	public DataValidationType getDataValidationTypeNameById(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getValidationTypeNameById(validationTypeId, clientAppDbJdbcTemplate);
	}


	@Override
	public DataValidationType getDataValidationTypeJobsandDependencyJars(Integer validationTypeId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getDataValidationTypeJobsandDependencyJars(validationTypeId, clientAppDbJdbcTemplate);
	}


	@Override
	public List<DataValidationType> getAllDataValidationTypes(String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return  dataValidationDao.getAllDataValidationTypes(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveDataValidationTypes(DataValidationType dataValidationType,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.saveDataValidationTypes(dataValidationType, clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDataValidationTypes(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.updateDataValidationTypes(dataValidationType, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Map<String, String>> getDataValidation(JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getDataValidation(clientAppDbJdbcTemplate);
	}

	@Override
	public List<DataValidationType> getDataValidationTypesByvalidationId(String clientId, Integer validationId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getDataValidationTypesByvalidationId(clientId, validationId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Map<String, String>> getContextParamNameAndValueById(int validationTypeId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return dataValidationDao.getContextParamNameAndValueById(validationTypeId, clientAppDbJdbcTemplate);
	}
}
