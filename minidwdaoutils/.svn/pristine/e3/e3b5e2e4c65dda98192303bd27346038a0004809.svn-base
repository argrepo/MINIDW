package com.datamodel.anvizent.service.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.AISourceDefinition;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIJobExecutionInfo;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.PackageExecution;

public interface AIServiceDao {
	
	
	int saveBusinessModal(BusinessModal aiDataModel, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<BusinessModal> getBusinessModelInfo( JdbcTemplate clientAppDbJdbcTemplate );
	
	BusinessModal getBusinessModalInfoById(Integer id , JdbcTemplate clientAppDbJdbcTemplate);
	
	int deleteBusinessInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	List<AISourceDefinition> getAiSourceDefinitionList(JdbcTemplate clientAppDbJdbcTemplate);

	AISourceDefinition getAiSourceInfoById(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate);

	void deleteAISourceDefinitionBYId(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate);

	int saveAISourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate);

	int updateSourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate);

	int saveAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int updateAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int saveAIJobContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate);

	int saveAiContextParameters(AIContextParameter aiContextParameter, JdbcTemplate clientAppDbJdbcTemplate);
	
	List<AIContextParameter> getAiContextParameters( JdbcTemplate clientAppDbJdbcTemplate );
	
	AIContextParameter getAiContextParametersById(Integer id , JdbcTemplate clientAppDbJdbcTemplate);
	
	int deleteAiContextParametersById(Integer id , JdbcTemplate clientAppDbJdbcTemplate);
	
	int saveAiModel(AIModel aiModel, JdbcTemplate clientAppDbJdbcTemplate);
		
    List<AIModel> getAiModelnfo( JdbcTemplate clientAppDbJdbcTemplate );
		
    AIModel getAiModelInfoById(Integer id , JdbcTemplate clientAppDbJdbcTemplate);
		
	int deleteAiModelInfoById(Integer id , JdbcTemplate clientAppDbJdbcTemplate);

	int saveAISpecificContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate);

	List<AIJobUploadInfo> getAIJobsUploadList(JdbcTemplate clientAppDbJdbcTemplate);

	AIJobUploadInfo getAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteAIGenericContextParams(int rJobId, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteAISpecificContextParam(int rJobId, JdbcTemplate clientAppDbJdbcTemplate);
	
	void executeQuery(String query, JdbcTemplate jdbcTemplate);
	
	List<Object> getTablePreview(String query , JdbcTemplate jdbcTemplate) throws SQLException, IOException;

	int saveBusinessAIMapping(BusinessModal businessModal, JdbcTemplate clientAppDbJdbcTemplate);

	int deleteBusinessAIMapping(Integer bmid, JdbcTemplate clientAppDbJdbcTemplate);
	
	AIModel getAIModelFromBid(Integer bmid, JdbcTemplate clientAppDbJdbcTemplate);
	
	AIJobUploadInfo getAIUploadedJobFromBid(String id, JdbcTemplate clientAppDbJdbcTemplate);

	List<PackageExecution> getExecutionInfoByBusinessId(Integer id, JdbcTemplate clientAppDbJdbcTemplate);
	
	void dropAndCreateTable(String[] queries ,JdbcTemplate jdbcTemplate );

	String getSourceQueryById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);

	List<AIJobExecutionInfo> getrJobExecutionInfo(String businessName,String modelName, JdbcTemplate clientStagingDbJdbcTemplate);

	String getExecutionCommentsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate);
	
	String getAIStagingTableByBusinessCaseName(String businessCaseName, JdbcTemplate clientAppDbJdbcTemplate);
	
	AIModel getAiModelInfoByByName(String name , JdbcTemplate clientAppDbJdbcTemplate);
}
