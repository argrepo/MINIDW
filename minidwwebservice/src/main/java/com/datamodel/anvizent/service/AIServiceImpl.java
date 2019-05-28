package com.datamodel.anvizent.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.AIServiceDao;
import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.AISourceDefinition;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIJobExecutionInfo;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.PackageExecution;


@Service
public class AIServiceImpl implements AIService{
	
	@Autowired
	AIServiceDao aiServiceDao;

	@Override
	public int saveBusinessModal(BusinessModal aiDataModel, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveBusinessModal(aiDataModel, clientAppDbJdbcTemplate);
	}

	@Override
	public List<BusinessModal> getBusinessModelInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getBusinessModelInfo(clientAppDbJdbcTemplate);
	}

	@Override
	public BusinessModal getBusinessModalInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getBusinessModalInfoById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteBusinessInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteBusinessInfoById(id, clientAppDbJdbcTemplate);
	}


	@Override
	public List<AISourceDefinition> getAiSourceDefinitionList(JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAiSourceDefinitionList(clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAiContextParameters(AIContextParameter aiContextParameter, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAiContextParameters(aiContextParameter, clientAppDbJdbcTemplate);
	}

	@Override
	public List<AIContextParameter> getAiContextParameters(JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return aiServiceDao.getAiContextParameters(clientAppDbJdbcTemplate);
	}

	@Override
	public AIContextParameter getAiContextParametersById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return aiServiceDao.getAiContextParametersById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteAiContextParametersById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return aiServiceDao.deleteAiContextParametersById(id, clientAppDbJdbcTemplate);
	}

	
	@Override
	public AISourceDefinition getAiSourceInfoById(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAiSourceInfoById(sourceDefId,clientAppDbJdbcTemplate);
	}

	@Override
	public void deleteAISourceDefinitionBYId(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate) {
		aiServiceDao.deleteAISourceDefinitionBYId(sourceDefId,clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAISourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAISourceDefinition(aiSourceDefinition,clientAppDbJdbcTemplate);
	}

	@Override
	public int updateSourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.updateSourceDefinition(aiSourceDefinition,clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAIJobUploadInfo(aiJobUploadInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public int updateAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.updateAIJobUploadInfo(aiJobUploadInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAIGenericContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAIJobContextParams(aiJobUploadInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAiModel(AIModel aiModel, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAiModel(aiModel, clientAppDbJdbcTemplate);
	}

	@Override
	public List<AIModel> getAiModelInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAiModelnfo(clientAppDbJdbcTemplate);
	}

	@Override
	public AIModel getAiModelInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAiModelInfoById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteAiModelInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteAiModelInfoById(id, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveAISpecificContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveAISpecificContextParams(aiJobUploadInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public List<AIJobUploadInfo> getAIJobsUploadList(JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAIJobsUploadList(clientAppDbJdbcTemplate);
	}

	@Override
	public AIJobUploadInfo getAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getAIUploadedJobById(id,clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteAIUploadedJobById(id,clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteAIGenericContextParams(int rJobId, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteAIGenericContextParams(rJobId,clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteAISpecificContextParam(int rJobId, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteAISpecificContextParam(rJobId,clientAppDbJdbcTemplate);
	}


	@Override
	public void executeQuery(String query, JdbcTemplate jdbcTemplate) {
		
		 aiServiceDao.executeQuery(query, jdbcTemplate);
		
	}

	@Override
	public List<Object> getTablePreview(String query, JdbcTemplate jdbcTemplate) throws SQLException, IOException {
		return aiServiceDao.getTablePreview(query, jdbcTemplate);
	}


	@Override
	public int saveBusinessAIMapping(BusinessModal businessModal, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.saveBusinessAIMapping(businessModal,clientAppDbJdbcTemplate);
	}

	@Override
	public int deleteBusinessAIMapping(Integer bmid, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.deleteBusinessAIMapping(bmid,clientAppDbJdbcTemplate);
	}

	@Override
	public List<PackageExecution> getExecutionInfoByBusinessId(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getExecutionInfoByBusinessId(id,clientAppDbJdbcTemplate);
	}

	@Override
	public void dropAndCreateTable(String[] queries, JdbcTemplate jdbcTemplate) {
		aiServiceDao.dropAndCreateTable(queries , jdbcTemplate );
		
	}

	@Override
	public String getSourceQueryById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getSourceQueryById(id,clientAppDbJdbcTemplate);
	}

	@Override
	public List<AIJobExecutionInfo> getrJobExecutionInfo(String businessName,String modelName, JdbcTemplate clientStagingDbJdbcTemplate) {
		return aiServiceDao.getrJobExecutionInfo(businessName,modelName,clientStagingDbJdbcTemplate);
	}

	@Override
	public String getExecutionCommentsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		return aiServiceDao.getExecutionCommentsById(id,clientAppDbJdbcTemplate);
	}

	@Override
	public String getAIStagingTableByBusinessCaseName(String businessCaseName, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return aiServiceDao.getAIStagingTableByBusinessCaseName(businessCaseName,clientAppDbJdbcTemplate);
	}

	@Override
	public AIModel getAiModelInfoByByName(String name, JdbcTemplate clientAppDbJdbcTemplate) 
	{
		return aiServiceDao.getAiModelInfoByByName(name,clientAppDbJdbcTemplate);
	}

}
