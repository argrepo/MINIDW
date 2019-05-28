package com.datamodel.anvizent.service.dao.impl;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.AIServiceDao;
import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.AISourceDefinition;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIJobExecutionInfo;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.opencsv.ResultSetHelper;
import com.opencsv.ResultSetHelperService;

@Repository
public class AIServiceDaoImpl extends JdbcDaoSupport implements AIServiceDao{
	private static final Logger LOGGER = LoggerFactory.getLogger(AIServiceDaoImpl.class);
	static ResultSetHelper resultService = new ResultSetHelperService();
	private SqlHelper sqlHelper; 
	@Autowired
	public AIServiceDaoImpl(@Qualifier("app_dataSource") DataSource datSource) {
		try {
			setDataSource(datSource);
			this.sqlHelper = new SqlHelper(AIServiceDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating AIServiceDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<AISourceDefinition> getAiSourceDefinitionList(JdbcTemplate clientAppDbJdbcTemplate) {
		List<AISourceDefinition> aiSourceList = null;
		try {
			String sql = sqlHelper.getSql("getAiSourceDefinitionList");
			aiSourceList = clientAppDbJdbcTemplate.query(sql, new RowMapper<AISourceDefinition>() {

				@Override
				public AISourceDefinition mapRow(ResultSet rs, int rowNum) throws SQLException {
					AISourceDefinition aiSourceDefinition = new AISourceDefinition();
					aiSourceDefinition.setSourceDefId(rs.getInt("SdID"));
					aiSourceDefinition.setBussinessName(rs.getString("Bmid"));
				//	aiSourceDefinition.setBusinessId(rs.getInt("Bmid"));
					aiSourceDefinition.setStagingTbl(rs.getString("Staging_Table"));
					aiSourceDefinition.setSourceQuery(rs.getString("SOURCE_QUERY"));
					aiSourceDefinition.setIsActive(rs.getBoolean("IsActive"));
					return aiSourceDefinition;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAiSourceDefinitionList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiSourceDefinitionList", e);
			
		}
		return aiSourceList;
	}
	@Override
	public AISourceDefinition getAiSourceInfoById(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate) {
		AISourceDefinition aiSource = null;
		try {
			String sql = sqlHelper.getSql("getAiSourceInfoById");
			aiSource = clientAppDbJdbcTemplate.query(sql, new Object[] {sourceDefId}, new ResultSetExtractor<AISourceDefinition>() {

				@Override
				public AISourceDefinition extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					if (rs != null && rs.next()) {
						AISourceDefinition aiSourceDefinition = new AISourceDefinition();
						aiSourceDefinition.setSourceDefId(rs.getInt("SdID"));
						//aiSourceDefinition.setBusinessId(rs.getInt("Bmid"));
						aiSourceDefinition.setBussinessName(rs.getString("Bmid"));
						aiSourceDefinition.setAiModalId(rs.getString("aid"));
						aiSourceDefinition.setStagingTbl(rs.getString("Staging_Table"));
						aiSourceDefinition.setSourceQuery(rs.getString("SOURCE_QUERY"));
						aiSourceDefinition.setIsActive(rs.getBoolean("IsActive"));
						return aiSourceDefinition;
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getAiSourceInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiSourceInfoById", e);
			
		}
		return aiSource;
	}
	@Override
	public void deleteAISourceDefinitionBYId(int sourceDefId, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteAISourceDefinitionBYId");
			clientAppDbJdbcTemplate.update(sql, new Object[] {sourceDefId});
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAISourceDefinitionBYId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAISourceDefinitionBYId", e);
			
		}
	}
	@Override
	public int saveAISourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate) {
		int sourceDefId = 0;
		try {
			final String sql = sqlHelper.getSql("saveAISourceDefinition");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "SdID" });
					ps.setString(1, aiSourceDefinition.getBussinessName());
					//ps.setString(2, aiSourceDefinition.getAiModalId());
					ps.setString(2, aiSourceDefinition.getStagingTbl());
					ps.setString(3, aiSourceDefinition.getSourceQuery());
					ps.setBoolean(4, aiSourceDefinition.getIsActive());
					ps.setString(5, aiSourceDefinition.getModification().getCreatedBy());
					ps.setString(6, aiSourceDefinition.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				sourceDefId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while saveAISourceDefinition()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveAISourceDefinition", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveAISourceDefinition", e);
			
		}
		return sourceDefId;
	}
	@Override
	public int updateSourceDefinition(AISourceDefinition aiSourceDefinition, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateSourceDefinition");
			update  = clientAppDbJdbcTemplate.update(sql,new Object[]{
					aiSourceDefinition.getBussinessName(),
					//aiSourceDefinition.getAiModalId(),
					aiSourceDefinition.getStagingTbl(),
					aiSourceDefinition.getSourceQuery(),
					aiSourceDefinition.getIsActive(),
					aiSourceDefinition.getSourceDefId()
			});
			
		}catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		}   catch (DataAccessException ae) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", e);
			
		}
		return update ;
	}
	
	@Override
	public int saveBusinessModal(BusinessModal businessModal, JdbcTemplate clientAppDbJdbcTemplate) {
		int createBMId = 0;
		if(businessModal.getBmid()== null) {
		try {
			String sql = sqlHelper.getSql("saveBusinessModalInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps =con.prepareStatement(sql, new String[] {"Bmid"});
                    ps.setString(1, businessModal.getBusinessProblem());
                    ps.setString(2, businessModal.getaIModel().get(0).getaIModelName());
                    ps.setString(3, businessModal.getaIStagingTable());
                    ps.setString(4, businessModal.getaIStagingTableScript());
                    ps.setString(5, businessModal.getaIILTable());
                    ps.setString(6, businessModal.getaIILTableScript());
                    ps.setString(7, businessModal.getaIOLTable());
                    ps.setString(8, businessModal.getaIOLTableScript());
                    ps.setString(9, businessModal.getIsActive());
                    ps.setString(10, businessModal.getModification().getCreatedBy());
                    ps.setString(11, businessModal.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			
			if(keyHolder !=null){
				Number autoInc = keyHolder.getKey();
				createBMId = autoInc.intValue();
			}
			
		}catch (DuplicateKeyException ae) {
					throw new AnvizentDuplicateFileNameException(ae);
				} catch (Exception e) {
					MinidwServiceUtil.getErrorMessage("ERROR", e);
					throw new AnvizentRuntimeException(e);
				}
	}else {
		

		try {
			createBMId = businessModal.getBmid();
			String sql = sqlHelper.getSql("updateBusinessModalInfo");
			clientAppDbJdbcTemplate.update(sql,new Object[]{
					businessModal.getBusinessProblem(),
					businessModal.getaIModel().get(0).getaIModelName(),
					businessModal.getaIStagingTable(),
					businessModal.getaIStagingTableScript(),
					businessModal.getaIILTable(),
					businessModal.getaIILTableScript(),
					businessModal.getaIOLTable(),
					businessModal.getaIOLTableScript(),
					businessModal.getIsActive(),
					businessModal.getModification().getModifiedBy(),
					businessModal.getModification().getModifiedTime(),
					businessModal.getBmid()
			});
		} catch (DuplicateKeyException ae) {
			throw new AnvizentDuplicateFileNameException(ae);
		}catch (Exception e) {
			MinidwServiceUtil.getErrorMessage("ERROR", e);
			throw new AnvizentRuntimeException(e);
		}	
	
		
	}
		return createBMId;
}
	@Override
	public List<BusinessModal> getBusinessModelInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		List<BusinessModal> businessModalInfo = null;
		try {
			String sql = sqlHelper.getSql("getBusinessModalInfo");

			businessModalInfo = clientAppDbJdbcTemplate.query(sql, new RowMapper<BusinessModal>() {

				@Override
				public BusinessModal mapRow(ResultSet rs, int rowNum) throws SQLException {
					BusinessModal businessModal = new BusinessModal();
					businessModal.setBmid(rs.getInt("Bmid"));
					businessModal.setBusinessProblem(rs.getString("Business_Problem"));
					businessModal.setIsActive(rs.getString("IsActive"));
					businessModal.setaIOLTable(rs.getString("AI_OL_Table"));
					businessModal.setaIILTable(rs.getString("AI_IL_Table"));
					businessModal.setaIStagingTable(rs.getString("AI_Staging_Table"));
					businessModal.setModelName(rs.getString("AI_Model"));
					return businessModal;
				}

			});
			
			
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getBusinessModelInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getBusinessModelInfo", e);
			
		}
		return businessModalInfo;
	}

	@Override
	public BusinessModal getBusinessModalInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		BusinessModal businessModalinfo = null;
		try {
			String sql = sqlHelper.getSql("getBusinessModalById");
			businessModalinfo = clientAppDbJdbcTemplate.query(sql, new Object[] {id},new ResultSetExtractor<BusinessModal>(){
				List<AIModel> aimodal = new ArrayList<>();
				@Override
				public BusinessModal extractData(ResultSet rs) throws SQLException, DataAccessException {
					BusinessModal businessModal = new BusinessModal();
					if(rs != null && rs.next()){
						
						businessModal.setBmid(rs.getInt("Bmid"));
						businessModal.setBusinessProblem(rs.getString("Business_Problem"));
						businessModal.setaIStagingTable(rs.getString("AI_Staging_Table"));
						businessModal.setModelName(rs.getString("AI_Model"));
						//aimodal =  getBusinessAImappingInfo(id,clientAppDbJdbcTemplate);
						//businessModal.setaIModel(aimodal);
						businessModal.setaIStagingTableScript(rs.getString("AI_Staging_Table_Script"));
						businessModal.setaIILTable(rs.getString("AI_IL_Table"));
						businessModal.setaIILTableScript(rs.getString("AI_IL_table_Script"));
						businessModal.setaIOLTable(rs.getString("AI_OL_Table"));
						businessModal.setaIOLTableScript(rs.getString("AI_OL_Table_Script"));
						businessModal.setIsActive(rs.getString("IsActive"));
						return businessModal;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getBusinessModalInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getBusinessModalInfoById()", e);
			
		}
		return businessModalinfo;
	}
	@Override
	public int deleteBusinessInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteFile = 0;
		try {
			String sql = sqlHelper.getSql("deleteBusinessModelById");
			deleteFile = clientAppDbJdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteBusinessModal", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteBusinessModal", e);
			
		}
		return deleteFile;
	}
	@Override
	public int saveAiContextParameters(AIContextParameter aiContextParameter, JdbcTemplate clientAppDbJdbcTemplate) {

		String sql="";
		int save = 0;
		if(aiContextParameter.getPid()== null) {
		try {
	    sql = sqlHelper.getSql("saveAiContextParameters");
				
					save = clientAppDbJdbcTemplate.update(sql,new Object[]{
							
							aiContextParameter.getParamName(),
							aiContextParameter.getParamValue(),
							aiContextParameter.isActive(),
							aiContextParameter.getModification().getCreatedBy(),
							aiContextParameter.getModification().getCreatedTime()
							
							
					});
				
		}catch (DuplicateKeyException ae) {
					throw new AnvizentDuplicateFileNameException(ae);
				} catch (Exception e) {
					MinidwServiceUtil.getErrorMessage("ERROR", e);
					throw new AnvizentRuntimeException(e);
				}
	}else {
		

		try {
			sql = sqlHelper.getSql("UpdateAiContextParameters");
			save = clientAppDbJdbcTemplate.update(sql,new Object[]{
					aiContextParameter.getParamName(),
					aiContextParameter.getParamValue(),
					aiContextParameter.isActive(),
					aiContextParameter.getModification().getModifiedBy(),
					aiContextParameter.getModification().getModifiedTime(),
			      	aiContextParameter.getPid()
			});
					
		} catch (DuplicateKeyException ae) {
			throw new AnvizentDuplicateFileNameException(ae);
		}catch (Exception e) {
			MinidwServiceUtil.getErrorMessage("ERROR", e);
			throw new AnvizentRuntimeException(e);
		}	
	
		
	}
		return save;

	}
	@Override
	public List<AIContextParameter> getAiContextParameters(JdbcTemplate clientAppDbJdbcTemplate) {

		List<AIContextParameter> aIContextParameterinfo = null;
		try {
			String sql = sqlHelper.getSql("getAiContextParametersInfo");

			aIContextParameterinfo = clientAppDbJdbcTemplate.query(sql, new RowMapper<AIContextParameter>() {

				@Override
				public AIContextParameter mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIContextParameter aIContextParameter = new AIContextParameter();
					aIContextParameter.setPid(rs.getInt("PID"));
					aIContextParameter.setParamName(rs.getString("ParamName"));
					aIContextParameter.setParamValue(rs.getString("ParamVal"));
					aIContextParameter.setActive(rs.getBoolean("Isactive"));
					return aIContextParameter;
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAiContextParametersInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiContextParametersInfo", e);
			
		}
		return aIContextParameterinfo;
	
	}
	@Override
	public AIContextParameter getAiContextParametersById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {

		AIContextParameter aIContextParameterinfo = null;
		try {
			String sql = sqlHelper.getSql("getAiContextParametersById");
			aIContextParameterinfo = clientAppDbJdbcTemplate.query(sql, new Object[] {id},new ResultSetExtractor<AIContextParameter>(){

				@Override
				public AIContextParameter extractData(ResultSet rs) throws SQLException, DataAccessException {
					AIContextParameter aIContextParameter = new AIContextParameter();
					if(rs != null && rs.next()){
						aIContextParameter.setPid(rs.getInt("PID"));
						aIContextParameter.setParamName(rs.getString("ParamName"));
						aIContextParameter.setParamValue(rs.getString("ParamVal"));
						aIContextParameter.setActive(rs.getBoolean("Isactive"));
						return aIContextParameter;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAiContextParametersById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiContextParametersById()", e);
			
		}
		return aIContextParameterinfo;
	
	}
	@Override
	public int deleteAiContextParametersById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {

		int deleteFile = 0;
		try {
			String sql = sqlHelper.getSql("deleteAiContextParametersById");
			deleteFile = clientAppDbJdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAiContextParametersById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAiContextParametersById", e);
			
		}
		return deleteFile;
	
	}

	@Override
	public int saveAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int createAIJobId = 0;
		try{
			String sql = sqlHelper.getSql("saveAIJobUploadInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps =con.prepareStatement(sql, new String[] {"RID"});
                    ps.setString(1, aiJobUploadInfo.getBusinessName());
                    ps.setString(2, aiJobUploadInfo.getJobName());
                    ps.setString(3, aiJobUploadInfo.getJobFileName());
                    ps.setBoolean(4, aiJobUploadInfo.getIsActive());
                    ps.setString(5, aiJobUploadInfo.getModification().getCreatedBy());
                    ps.setString(6, aiJobUploadInfo.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			
			if(keyHolder !=null){
				Number autoInc = keyHolder.getKey();
				createAIJobId = autoInc.intValue();
			}
			
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return createAIJobId;
	}

	@Override
	public int updateAIJobUploadInfo(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		Integer rJobid = aiJobUploadInfo.getRid();

		try {
			String sql = sqlHelper.getSql("updateAIJobUploadInfo");

			updateStatus = clientAppDbJdbcTemplate.update(sql, new Object[] { 
					aiJobUploadInfo.getBusinessName(),
					aiJobUploadInfo.getJobName(),
					aiJobUploadInfo.getJobFileName(),
					aiJobUploadInfo.getIsActive(),
					aiJobUploadInfo.getModification().getModifiedBy(),
					aiJobUploadInfo.getModification().getModifiedTime(),
					rJobid });

		} catch (DataAccessException ae) {
			logger.error("error while updateAIJobUploadInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while updateAIJobUploadInfo()", e);
		}
		return updateStatus;
	}

	@Override
	public int saveAIJobContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try{
			String sql = sqlHelper.getSql("saveAIJobContextParams");
		
			save = clientAppDbJdbcTemplate.update(sql,new Object[]{
				aiJobUploadInfo.getRid(),
                aiJobUploadInfo.getAiGenericContextParam().getPid(),
                aiJobUploadInfo.getAiGenericContextParam().getParamValue(),
                aiJobUploadInfo.getModification().getCreatedBy(),
                aiJobUploadInfo.getModification().getCreatedTime()
				
		});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return save;
	}

	@Override
	public int saveAiModel(AIModel aiModel, JdbcTemplate clientAppDbJdbcTemplate) {

		String sql="";
		int save = 0;
		if(aiModel.getId()== null) {
		try {
	    sql = sqlHelper.getSql("saveAiModelInfo");
				
					save = clientAppDbJdbcTemplate.update(sql,new Object[]{
							
							aiModel.getaIModelName(),
							aiModel.getAiModelType(),
							aiModel.getAiContextParameters(),
							aiModel.getModification().getCreatedBy(),
							aiModel.getModification().getCreatedTime()
							
							
					});
				
		}catch (DuplicateKeyException ae) {
					throw new AnvizentDuplicateFileNameException(ae);
				} catch (Exception e) {
					MinidwServiceUtil.getErrorMessage("ERROR", e);
					throw new AnvizentRuntimeException(e);
				}
	}else {
		

		try {
			sql = sqlHelper.getSql("updateAiModelInfo");
			save = clientAppDbJdbcTemplate.update(sql,new Object[]{
					aiModel.getaIModelName(),
					aiModel.getAiModelType(),
					aiModel.getAiContextParameters(),
					aiModel.getModification().getModifiedBy(),
					aiModel.getModification().getModifiedTime(),
					aiModel.getId()
			});
		} catch (DuplicateKeyException ae) {
			throw new AnvizentDuplicateFileNameException(ae);
		}catch (Exception e) {
			MinidwServiceUtil.getErrorMessage("ERROR", e);
			throw new AnvizentRuntimeException(e);
		}	
	
		
	}
		return save;

	}

	@Override
	public List<AIModel> getAiModelnfo(JdbcTemplate clientAppDbJdbcTemplate) {

		List<AIModel> aiModalInfo = null;
		try {
			String sql = sqlHelper.getSql("getaiModelInfo");

			aiModalInfo = clientAppDbJdbcTemplate.query(sql, new RowMapper<AIModel>() {

				@Override
				public AIModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIModel aiModel = new AIModel();
					aiModel.setId(rs.getInt("ID"));
					aiModel.setaIModelName(rs.getString("AI_Model_Name"));
					aiModel.setAiModelType(rs.getString("AI_Model_Type"));
					aiModel.setAiContextParameters(rs.getString("AI_Model_Context_params"));
					
					return aiModel;
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getaiModelInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getaiModelInfo", e);
			
		}
		return aiModalInfo;
	
	}

	@Override
	public AIModel getAiModelInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {

		AIModel aiModelinfo = null;
		try {
			String sql = sqlHelper.getSql("getAiModelById");
			aiModelinfo = clientAppDbJdbcTemplate.query(sql, new Object[] {id},new ResultSetExtractor<AIModel>(){

				@Override
				public AIModel extractData(ResultSet rs) throws SQLException, DataAccessException {
					AIModel aiModel = new AIModel();
					if(rs != null && rs.next()){
						aiModel.setId(rs.getInt("ID"));
						aiModel.setaIModelName(rs.getString("AI_Model_Name"));
						aiModel.setAiModelType(rs.getString("AI_Model_Type"));
						aiModel.setAiContextParameters(rs.getString("AI_Model_Context_params"));
						return aiModel;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAiModelById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiModelById()", e);
			
		}
		return aiModelinfo;
	
	}

	@Override
	public int deleteAiModelInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {

		int deleteFile = 0;
		try {
			String sql = sqlHelper.getSql("deleteAiModelById");
			deleteFile = clientAppDbJdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAiModelById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAiModelById", e);
			
		}
		return deleteFile;
	
	}

	@Override
	public int saveAISpecificContextParams(AIJobUploadInfo aiJobUploadInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<AIModel> aiModalList = aiJobUploadInfo.getAiModal();
		try{
			String sql = sqlHelper.getSql("saveAISpecificContextParams");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AIModel aiModel = aiModalList.get(i);
					ps.setInt(1, aiJobUploadInfo.getRid());
					BusinessModal businessModal = getBusinessCaseByModelName(clientAppDbJdbcTemplate,aiJobUploadInfo.getBusinessName());
					ps.setInt(2, businessModal.getBmid());
					ps.setInt(3, aiModel.getId());
					ps.setString(4, aiModel.getAiContextParameters());
					ps.setString(5, aiJobUploadInfo.getModification().getCreatedBy());
					ps.setString(6, aiJobUploadInfo.getModification().getCreatedTime());
				}
				@Override
				public int getBatchSize() {
					return aiModalList.size();
				}
			});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count != null?count.length:0;
		
	}

	@Override
	public List<AIJobUploadInfo> getAIJobsUploadList(JdbcTemplate clientAppDbJdbcTemplate) {
		List<AIJobUploadInfo> aiUpload = null;
		
		try {
			String sql = sqlHelper.getSql("getAIJobsUploadList");
			aiUpload = clientAppDbJdbcTemplate.query(sql,new RowMapper<AIJobUploadInfo>() {

				@Override
				public AIJobUploadInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIJobUploadInfo aiJobUploadInfo = new AIJobUploadInfo();
					BusinessModal businessModal = getBusinessCaseByModelName(clientAppDbJdbcTemplate,rs.getString("Bmid"));
					aiJobUploadInfo.setBusinessName(businessModal.getBusinessProblem());
					aiJobUploadInfo.setModelName(businessModal.getModelName());
					aiJobUploadInfo.setRid(rs.getInt("RID"));
					aiJobUploadInfo.setBusinessId(businessModal.getBmid());
					aiJobUploadInfo.setJobName(rs.getString("RJobName"));
					aiJobUploadInfo.setIsActive(rs.getBoolean("isActive"));
					return aiJobUploadInfo;
				}
				
			});
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return aiUpload;
	}

	private BusinessModal getBusinessCaseByModelName(JdbcTemplate clientAppDbJdbcTemplate,String modelName)
	{
		BusinessModal businessModal = null;
		try {
			String sql = sqlHelper.getSql("getBusinessCaseByModelName");
			businessModal = clientAppDbJdbcTemplate.query(sql, new Object[] {modelName},new ResultSetExtractor<BusinessModal>(){
				@Override
				public BusinessModal extractData(ResultSet rs) throws SQLException, DataAccessException {
					BusinessModal businessModal = new BusinessModal();
					if(rs != null && rs.next()){
						businessModal.setBmid(rs.getInt("Bmid"));
						businessModal.setModelName(rs.getString("AI_Model"));
						businessModal.setBusinessProblem(rs.getString("Business_Problem"));
						return businessModal;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getBusinessCaseByModelName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getBusinessCaseByModelName()", e);
			
		}
		return businessModal;
	}
	private AIModel getModelInfoByModelName(JdbcTemplate clientAppDbJdbcTemplate,String modelName)
	{
		AIModel aIModel = null;
		try {
			String sql = sqlHelper.getSql("getModelInfoByModelName");
			aIModel = clientAppDbJdbcTemplate.query(sql, new Object[] {modelName},new ResultSetExtractor<AIModel>(){
				@Override
				public AIModel extractData(ResultSet rs) throws SQLException, DataAccessException {
					AIModel aIModel = new AIModel();
					if(rs != null && rs.next()){
						aIModel.setId(rs.getInt("ID"));
						aIModel.setaIModelName(rs.getString("AI_Model_Name"));
						aIModel.setAiModelType(rs.getString("AI_Model_Type"));
						aIModel.setAiContextParameters(rs.getString("AI_Model_Context_params"));
						return aIModel;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getModelIdByModelName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getModelIdByModelName()", e);
		}
		return aIModel;
	}
	@Override
	public AIJobUploadInfo getAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		AIJobUploadInfo aiUploadedJobInfo = null;
		
		try {
			String sql = sqlHelper.getSql("getAIUploadedJobById");
			aiUploadedJobInfo = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new ResultSetExtractor<AIJobUploadInfo>() {
				List<AIModel> aimodal = new ArrayList<>();
				@Override
				public AIJobUploadInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					AIJobUploadInfo aiJobInfo = new AIJobUploadInfo();
					if(rs != null && rs.next()) {
						
						AIContextParameter aiGenericContextParams = new AIContextParameter();
						aiJobInfo.setRid(rs.getInt("RID"));
						//aiJobInfo.setBusinessId(rs.getInt("Bmid"));
						aiJobInfo.setBusinessName(rs.getString("Bmid"));
						
						aimodal =  getSpecificContextParams(id,clientAppDbJdbcTemplate);
						aiJobInfo.setAiModal(aimodal);
						
						aiGenericContextParams.setPid(rs.getInt("PID"));
						//aiGenericContextParams.setParamValue(rs.getString("ParamVal"));
						aiJobInfo.setAiGenericContextParam(aiGenericContextParams);
						aiJobInfo.setJobName(rs.getString("RJobName"));
						aiJobInfo.setJobFileName(rs.getString("RJobJars"));
						aiJobInfo.setIsActive(rs.getBoolean("isActive"));
						
						
						return aiJobInfo;
					}else {
						return null;
					}
				}

				
				
			});
		}  catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return aiUploadedJobInfo;
	}
	
	public List<AIModel> getSpecificContextParams(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		List<AIModel> aimodal = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getSpecificContextParams");
			aimodal = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new RowMapper<AIModel>() {
				@Override
				public AIModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIModel aiModalInfo = new AIModel();
					aiModalInfo.setId(rs.getInt("AID"));
					aiModalInfo.setAiContextParameters(rs.getString("contextparam_object"));
					return aiModalInfo;
				}
			});
		} 
		catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return aimodal;
	}

	@Override
	public int deleteAIUploadedJobById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteRecord = 0;
		try {
			String sql = sqlHelper.getSql("deleteAIUploadedJobById");
			deleteRecord = clientAppDbJdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAIUploadedJob", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAIUploadedJob", e);
			
		}
		return deleteRecord;
	}

	@Override
	public int deleteAIGenericContextParams(int rJobId, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteRecord = 0;
		try {
			String sql = sqlHelper.getSql("deleteAIGenericContextParams");
			deleteRecord = clientAppDbJdbcTemplate.update(sql, new Object[] { rJobId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAIGenericContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAIGenericContextParams", e);
			
		}
		return deleteRecord;
	}

	@Override
	public int deleteAISpecificContextParam(int rJobId, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteRecord = 0;
		try {
			String sql = sqlHelper.getSql("deleteAISpecificContextParam");
			deleteRecord = clientAppDbJdbcTemplate.update(sql, new Object[] { rJobId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteAISpecificContextParam", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteAISpecificContextParam", e);
			
		}
		return deleteRecord;
	}


	@Override
	public void executeQuery(String query, JdbcTemplate jdbcTemplate) {
	
		try {
			if(StringUtils.isNotEmpty(query))
				jdbcTemplate.execute(query);
		} catch (Exception e) {
			LOGGER.error("error while executing Query()", e);
				throw new AnvizentRuntimeException(e);
		}

	}
	
	/*public void executeQuery(String query, JdbcTemplate jdbcTemplate) {
		
		try {
			jdbcTemplate.execute(new ConnectionCallback<Integer>() {

				@Override
				public Integer doInConnection(Connection con) throws SQLException, DataAccessException{
				Statement st = null;
				st = con.createStatement();
				st.addBatch(query);
				st.executeBatch();
				return 1;
				}
				});
		} catch (Exception e) {
			LOGGER.error("error while executing Query()", e);
				throw new AnvizentRuntimeException(e);
		}

	}
	*/
	
	public  List<Object> getTablePreview(String query, JdbcTemplate jdbcTemplate) throws SQLException, IOException {

		SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		CallableStatement cstmt = null;
		List<String> columns = new ArrayList<>();
		List<Object> previewtableList = new ArrayList<>();
		ResultSetMetaData metaData = null;
		
		try {
			
					String queryTime = startDateFormat.format(new Date());
					LOGGER.info("Query execution started " + queryTime);
					stmt = conn.prepareStatement(query);
					try {
						stmt.setMaxRows(10);
					} catch (Exception e) {
						LOGGER.warn("setRows not supported",e);
					}
					rs = stmt.executeQuery();
					queryTime = startDateFormat.format(new Date());
					LOGGER.info("Query execution Completed " + queryTime);
					metaData = rs.getMetaData();
					if (rs != null) {
						queryTime = startDateFormat.format(new Date());
						LOGGER.info("Preview fetching Started " + queryTime);
						int countcolumns = metaData.getColumnCount();
						for (int i = 1; i <= countcolumns; i++) {
							columns.add(metaData.getColumnLabel(i));
						}
						previewtableList.add(columns);
						int rowCount = 0;
						while (rs.next()) {
							if (rowCount > 10) {
								break;
							}
							String[] datRow = resultService.getColumnValues(rs, false,
									Constants.Config.DEFAULT_DATE_FORMAT, Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
							previewtableList.add(Arrays.asList(datRow));
							rowCount++;
						}
						queryTime = startDateFormat.format(new Date());
						LOGGER.info("Preview fetching Complated " + queryTime);

			
			}
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cstmt != null)
				cstmt.close();
			if (conn != null)
				conn.close();
		}
		return previewtableList;
	}
	


	@Override
	public int saveBusinessAIMapping(BusinessModal businessModal, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<AIModel> aiModalList = businessModal.getaIModel();
		try{
			String sql = sqlHelper.getSql("saveBusinessAIMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AIModel aiModel = aiModalList.get(i);
					ps.setInt(1, businessModal.getBmid());
					ps.setInt(2, aiModel.getId());
					ps.setString(3, businessModal.getModification().getCreatedBy());
					ps.setString(4, businessModal.getModification().getCreatedTime());
				}
				@Override
				public int getBatchSize() {
					return aiModalList.size();
				}
			});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count != null?count.length:0;

	}
	
	public List<AIModel> getAIModalInfoBYBMID(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		List<AIModel> aimodal = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getAIModalInfoBYBMID");
			aimodal = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new RowMapper<AIModel>() {
				@Override
				public AIModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIModel aiModalInfo = new AIModel();
					aiModalInfo.setId(rs.getInt("AID"));
					return aiModalInfo;
				}
			});
		} 
		catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return aimodal;

	}

	@Override
	public int deleteBusinessAIMapping(Integer bmid, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteRecord = 0;
		try {
			String sql = sqlHelper.getSql("deleteBusinessAIMapping");
			deleteRecord = clientAppDbJdbcTemplate.update(sql, new Object[] { bmid });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteBusinessAIMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteBusinessAIMapping", e);
			
		}
		return deleteRecord;
	}
	@Override
	public AIModel getAIModelFromBid(Integer bmid, JdbcTemplate clientAppDbJdbcTemplate)
	{
		AIModel aIModel = null;
		try {
			String sql = sqlHelper.getSql("getAIModelFromBid");
			aIModel = clientAppDbJdbcTemplate.query(sql, new Object[] {bmid}, new ResultSetExtractor<AIModel>() {
				@Override
				public AIModel extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						AIModel aIModel = new AIModel();
						aIModel.setaIModelName(rs.getString("AI_Model_Name"));
						aIModel.setId(rs.getInt("ID"));
						aIModel.setAiContextParameters(rs.getString("AI_Model_Context_params"));
						return aIModel;
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getAIModelFromBid", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAIModelFromBid", e);
			
		}
		return aIModel;
	}

	@Override
	public AIJobUploadInfo getAIUploadedJobFromBid(String bmid, JdbcTemplate clientAppDbJdbcTemplate)
	{
		AIJobUploadInfo aIJobUploadInfo = null;
		try {
			String sql = sqlHelper.getSql("getAIUploadedJobFromBid");
			aIJobUploadInfo = clientAppDbJdbcTemplate.query(sql, new Object[] {bmid}, new ResultSetExtractor<AIJobUploadInfo>() {
				@Override
				public AIJobUploadInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						AIJobUploadInfo aIJobUploadInfo = new AIJobUploadInfo();
						aIJobUploadInfo.setJobName(rs.getString("RJobName"));
						aIJobUploadInfo.setJobFileName(rs.getString("RJobJars"));
						aIJobUploadInfo.setBusinessName(bmid);
						//aIJobUploadInfo.setBusinessId(bmid);
						return aIJobUploadInfo;
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getAIUploadedJobFromBid", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAIUploadedJobFromBid", e);
			
		}
		return aIJobUploadInfo;
	}
	
	public List<AIModel> getBusinessAImappingInfo(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		List<AIModel> aimodal = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getBusinessAImappingInfo");
			aimodal = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new RowMapper<AIModel>() {
				@Override
				public AIModel mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIModel aiModalInfo = new AIModel();
					aiModalInfo.setId(rs.getInt("AID"));
					aiModalInfo.setaIModelName(rs.getString("aimodalName"));
					return aiModalInfo;
				}
			});
		} 
		catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return aimodal;
	}

	@Override
	public List<PackageExecution> getExecutionInfoByBusinessId(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		List<PackageExecution> packExec = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getExecutionInfoByBusinessId");
			packExec = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new RowMapper<PackageExecution>() {
				@Override
				public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
					PackageExecution execInfo = new PackageExecution();
					execInfo.setExecutionId(rs.getLong("execution_id"));
					execInfo.setDlId(rs.getInt("dl_id"));
					execInfo.setRunType(rs.getString("run_type"));
					execInfo.setExecutionStatus(rs.getString("execution_status"));
					execInfo.setExecutionComments(rs.getString("execution_comments"));
					execInfo.setExecutionStartDate(rs.getString("exection_start_date"));
					execInfo.setLastExecutedDate(rs.getString("last_executed_date"));
					return execInfo;
				}
			});
		} 
		catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return packExec;
	}
	
	@Override
	public void dropAndCreateTable(String[] queries, JdbcTemplate jdbcTemplate)
	{
	jdbcTemplate.batchUpdate(queries);
	}

	@Override
	public String getSourceQueryById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		String query = "";
		try {
			String sql = sqlHelper.getSql("getSourceQueryById");
			query = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					if (rs != null && rs.next()) {
						return rs.getString("SOURCE_QUERY");
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getSourceQueryById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getSourceQueryById", e);
			
		}
		return query;
	}

	@Override
	public List<AIJobExecutionInfo> getrJobExecutionInfo(String businessName,String modelName,
			JdbcTemplate clientStagingDbJdbcTemplate) {
		List<AIJobExecutionInfo> rJobExec = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getrJobExecutionInfo");
			rJobExec = clientStagingDbJdbcTemplate.query(sql, new Object[] {businessName,modelName}, new RowMapper<AIJobExecutionInfo>() {
				@Override
				public AIJobExecutionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					AIJobExecutionInfo jobExecInfo = new AIJobExecutionInfo();
					jobExecInfo.setRunId(rs.getInt("RUN_ID"));
					jobExecInfo.setBusinessProblem(rs.getString("BUSINESS_PROBLEM"));
					jobExecInfo.setAiModel(rs.getString("AI_MODEL"));
					jobExecInfo.setSourceCount(rs.getInt("SOURCE_RECORD_COUNT"));
					jobExecInfo.setStagingCount(rs.getInt("STAGING_RECORD_COUNT"));
					jobExecInfo.setaILCount(rs.getInt("AIL_RECORD_COUNT"));
					jobExecInfo.setaOLCount(rs.getInt("AOL_RECORD_COUNT"));
					jobExecInfo.setErrorMsg(rs.getString("ERROR_MESSAGE"));
					jobExecInfo.setJobStartTime(rs.getString("JOB_START_DATETIME"));
					jobExecInfo.setJobEndTime(rs.getString("JOB_END_DATETIME"));
					jobExecInfo.setJobDuration(rs.getLong("JOB_DURATION"));
					jobExecInfo.setJobRunStatus(rs.getString("JOB_RUN_STATUS"));
					jobExecInfo.setJobLogFileName(rs.getString("JOB_LOG_FILENAME"));
					return jobExecInfo;
				}
			});
		} 
		catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return rJobExec;
	}

	@Override
	public String getExecutionCommentsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		String query = "";
		try {
			String sql = sqlHelper.getSql("getExecutionCommentsById");
			query = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					if (rs != null && rs.next()) {
						return rs.getString("execution_comments");
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getExecutionCommentsById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExecutionCommentsById", e);
			
		}
		return query;
	}

	@Override
	public String getAIStagingTableByBusinessCaseName(String businessCaseName, JdbcTemplate clientAppDbJdbcTemplate)
	{
		String stagingTableName = null ;
		try {
			String sql = sqlHelper.getSql("getAIStagingTableByBusinessCaseName");
			stagingTableName = clientAppDbJdbcTemplate.query(sql, new Object[] {businessCaseName}, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					if (rs != null && rs.next()) {
						return rs.getString("Staging_Table");
					}else {
						return null;
					}
				}
				
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getAIStagingTableByBusinessCaseName", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAIStagingTableByBusinessCaseName", e);
			
		}
		return stagingTableName;
	}

	@Override
	public AIModel getAiModelInfoByByName(String name, JdbcTemplate clientAppDbJdbcTemplate) {

		AIModel aiModelinfo = null;
		try {
			String sql = sqlHelper.getSql("getAiModelInfoByByName");
			aiModelinfo = clientAppDbJdbcTemplate.query(sql, new Object[] {name},new ResultSetExtractor<AIModel>(){

				@Override
				public AIModel extractData(ResultSet rs) throws SQLException, DataAccessException {
					AIModel aiModel = new AIModel();
					if(rs != null && rs.next()){
						aiModel.setId(rs.getInt("ID"));
						aiModel.setaIModelName(rs.getString("AI_Model_Name"));
						aiModel.setAiModelType(rs.getString("AI_Model_Type"));
						aiModel.setAiContextParameters(rs.getString("AI_Model_Context_params"));
						return aiModel;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAiModelInfoByByName()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAiModelInfoByByName()", e);
			
		}
		return aiModelinfo;
	
	}
}



