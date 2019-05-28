package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationIntegration;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.JobResult;

public interface DataValidationService {

	List<DataValidation> getValidationInfo(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	Integer saveDataValidation(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate);

	int saveScriptDlMapping(int createValdationId, DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate);

	DataValidation getValidationScriptDataById(int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	List<DLInfo> getDLInfoByValidationScriptId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	int updateDataValidationScript(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteScriptDlMapping(int scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	DataValidationIntegration getDataValidationIntegration(JdbcTemplate commonJdbcTemplate);

	List<JobResult> getJobExecutionResults(String clientId, Integer scriptId, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getValidationScriptErrorLogs(String batchId, JdbcTemplate clientJdbcTemplate);

	int saveScriptIlMapping(int createValdationId, DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteScriptilMapping(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	List<DataValidation> getPreloadValidationInfoByConnectionId(Integer validationTypeId, Integer connectionId, JdbcTemplate clientAppDbJdbcTemplate);

	DataValidation getValidationScriptByScriptId(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	List<DataValidation> getSelectedValidationByIds(String scriptIds,Integer connectionId,JdbcTemplate clientAppDbJdbcTemplate);

	int saveDataValidationContextParams(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteValidationContextParams(int dataValidationTypeId, JdbcTemplate clientAppDbJdbcTemplate);

	List<String> getContextParamsByValidationId(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	Map<String, String> getDataValidationContextParams(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate);

	DataValidationType getDataValidationTypeNameById(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate);
	
	DataValidationType getDataValidationTypeJobsandDependencyJars(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<DataValidationType> getAllDataValidationTypes(String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<DataValidationType> getDataValidationTypesByvalidationId(String clientId,Integer validationId, JdbcTemplate clientAppDbJdbcTemplate);
	
	int saveDataValidationTypes(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate);
	
	int updateDataValidationTypes(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<Map<String, String>> getDataValidation(JdbcTemplate clientAppDbJdbcTemplate);
	
	List<Map<String, String>> getContextParamNameAndValueById(int validationTypeId, JdbcTemplate clientAppDbJdbcTemplate);
	
}
