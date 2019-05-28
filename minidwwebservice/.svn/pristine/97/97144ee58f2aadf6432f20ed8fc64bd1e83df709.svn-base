package com.datamodel.anvizent.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.CrossReferenceConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.CrossReferenceDao;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.User;

/**
 * 
 * @author rajesh.anthari
 */
@Service
public class CrossReferenceServiceImpl implements CrossReferenceService {

	protected static final Log LOG = LogFactory.getLog(CrossReferenceServiceImpl.class);

	private @Value("${crosssreference.classname:local_project.il_xref_bulk_main_v2_0_1.IL_XRef_Bulk_Main_v2}") String BULK_JOB_CLASS; // = "local_project.il_xref_main_v1_0_1.IL_XRef_Main_v1";
	private @Value("${crosssreference.dependancyjars.list:il_xref_bulk_main_v2_0_1.jar,il_xref_bulk_v2_0_1.jar}") String BULK_DEPENDENCY_JARS;// = "il_xref_main_v1_0_1.jar,il_xref_v1_0_1.jar";
	
	private @Value("${crosssreference.unmerge.classname:local_project.il_xref_auto_merge_main_v2_0_1.IL_XRef_Auto_Merge_Main_v2}") String UNMERGE_JOB_CLASS; // = "local_project.local_project.il_xref_auto_merge_main_v2_0_1.IL_XRef_Auto_Merge_Main_v2";
	private @Value("${crosssreference.unmerge.dependancyjars.list:il_xref_auto_merge_main_v2_0_1.jar,il_xref_auto_merge_v2_0_1.jar}") String UNMERGE_DEPENDENCY_JARS;// = "il_xref_auto_merge_main_v2_0_1.jar,il_xref_auto_merge_v2_0_1.jar";
	
	@Autowired
	private CrossReferenceDao crossReferenceDao;

	@Override
	public List<ILInfo> getAllClientILsForXref(String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		LOG.info("in getAllClientILsForXref");
		return crossReferenceDao.getAllClientILsForXref(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<Object> getDistinctValues(String columnName, String tableName, String xRefTableName, String columnValue, boolean isXrefValueNull,
			String xRefColumnName, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in getDistinctValues");
		return crossReferenceDao.getDistinctValues(columnName, tableName, xRefTableName, columnValue, isXrefValueNull, xRefColumnName, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getMatchedRecordWithMap(String columnName, List<String> values, String tableName, String xRefTableName, boolean isXrefValueNull,
			String xRefColumnName, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in getMatchedRecordWithMap");
		return crossReferenceDao.getMatchedRecordWithMap(columnName, values, tableName, xRefTableName, isXrefValueNull, xRefColumnName, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> mergeXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName, String xRefKeyColumnName,
			List<Column> columns, List<String> mergeColumnValues, String xReferenceColumnValue, String columnName, String ilXreferenceValue,
			String conditionName, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in mergeXreferenceRecords");

		return crossReferenceDao.mergeXreferenceRecords(ilTableName, xRefTableName, autoincrementColumnName, xRefKeyColumnName, columns, mergeColumnValues,
				xReferenceColumnValue, columnName, ilXreferenceValue, conditionName, clientJdbcTemplate);
	}

	public boolean splitXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName, String xRefKeyColumnName,
			List<Column> columns, List<String> splitColumnValues, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in splitXreferenceRecords");

		return crossReferenceDao.splitXreferenceRecords(ilTableName, xRefTableName, autoincrementColumnName, xRefKeyColumnName, columns, splitColumnValues,
				clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getExistingXrefRecord(String tableName, String columnName, String existingXrefValue, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in getExistingXrefRecord");
		return crossReferenceDao.getExistingXrefRecord(tableName, columnName, existingXrefValue, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> mergeCrossReferenceRecordsWithExistingXref(String ilTableName, String existingXrefKey, List<String> mergeColumnValues,
			String xRefKeyColumnName, String autoincrementColumnName, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in mergeCrossReferenceRecordsWithExistingXref");
		return crossReferenceDao.mergeCrossReferenceRecordsWithExistingXref(ilTableName, existingXrefKey, mergeColumnValues, xRefKeyColumnName,
				autoincrementColumnName, clientJdbcTemplate);
	}

	@Override
	public int crossReferenceAuditLogs(String ilId, String columnName, String crossReferenceOption,
			String ilColumnValue, String newOrExistingXref, String xReferenceColumnValue,
			List<String> mergeColumnValues,String autoXrefQueries,String clientId,Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		LOG.info("in crossReferenceAuditLogs");
		return crossReferenceDao.crossReferenceAuditLogs(ilId,columnName,crossReferenceOption,ilColumnValue,newOrExistingXref,xReferenceColumnValue,mergeColumnValues,autoXrefQueries,clientId,modification, clientAppDbJdbcTemplate);
	}

	@Override
	public List<CrossReferenceForm> getCrossReferenceInfoById(int ilid,String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		LOG.info("in getCrossReferenceInfoById");
		return crossReferenceDao.getCrossReferenceInfoById(ilid,clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public String getColumnValues(int id,String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		LOG.info("in getColumnValues");
		return crossReferenceDao.getColumnValues(id,clientId, clientAppDbJdbcTemplate);
	}
	@Override
	public String getAutoMergeQueriesById(int id, String clientId,JdbcTemplate jdbcTemplate) {
		// TODO Auto-generated method stub
		return crossReferenceDao.getAutoMergeQueriesById(id, clientId, jdbcTemplate);
	}
	
	@Override
	public int unMergeSelectedCrossReferenceRecord(String ilTableName, String autoincrementColumnName, String xRefKeyColumnName, Integer ilColumnValue,
			JdbcTemplate clientJdbcTemplate) {
		LOG.info("in unMergeSelectedCrossReferenceRecord");
		return crossReferenceDao.unMergeSelectedCrossReferenceRecord(ilTableName, autoincrementColumnName, xRefKeyColumnName, ilColumnValue, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getXrefTableRecordsWithMap(String xrefIlTableName, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in getXrefTableRecordsWithMap");
		return crossReferenceDao.getXrefTableRecordsWithMap(xrefIlTableName,clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getUnMergeRowWithColumns(CrossReferenceForm crossReferenceForm, JdbcTemplate clientJdbcTemplate) {
		LOG.info("in xrefTableRecordsWithMap");
		return crossReferenceDao.getUnMergeRowWithColumns(crossReferenceForm,clientJdbcTemplate);
	}
	@Override 
	public Map<String, Object> processAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo,User user,
			JdbcTemplate clientJdbcTemplate,
			JdbcTemplate clientAppJdbcTemplate)  throws Exception {
		// TODO Auto-generated method stub
		return crossReferenceDao.processAutoMergeCrossReference(crossReferenceForm, ilInfo, user, clientJdbcTemplate , clientAppJdbcTemplate);
	}

	@Override
	public void processBulkMergeCrossReference(Integer xrefConditionId, ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials,String filePath, String referenceField, String xReferenceField,
			 int fileInfoId, String conditionName, String xrefExecutionType, String applicableDate, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppJdbcTemplate) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException {
		String startDate = TimeZoneDateHelper.getFormattedDateString();
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(user.getUserId());
		String dbHost = clientDbCredentials.get("region_hostname").toString();
		String dbPort = clientDbCredentials.get("region_port").toString();
		String dbUserName = clientDbCredentials.get("clientdb_username").toString();
		String dbPassword = clientDbCredentials.get("clientdb_password").toString();
		String mainSchemaName = clientDbCredentials.get("clientdb_schema").toString();
		String stagingSchemaName = clientDbCredentials.get("clientdb_staging_schema").toString();
		LOG.info("Cross reference job class name " + BULK_JOB_CLASS );
		LOG.info("Cross reference dependency jars " + BULK_DEPENDENCY_JARS );
		
		if(xrefConditionId == 0) {
			xrefConditionId = (int) crossReferenceDao.bulkmergeLogInsert(new CrossReferenceLogs(ilInfo.getiL_id(),CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.BULKMERGE.toString(), 
					conditionName, referenceField, xReferenceField, fileInfoId, applicableDate, null, startDate, startDate, modification), clientAppJdbcTemplate);
		}
		
		Map<String, String> xRefContextParams = MinidwServiceUtil.getXRefContextParams(dbHost, dbPort, stagingSchemaName, dbUserName, dbPassword, dbHost, 
												dbPort, mainSchemaName, dbUserName, dbPassword, ilInfo, user.getClientId(), Constants.Temp.getTempFileDir(), filePath,referenceField,
												xReferenceField,applicableDate, xrefConditionId, xrefExecutionType, conditionName);
		JobExecutionInfo xrefJobStatus =  MinidwServiceUtil.runXrefJob(xRefContextParams, ilInfo,BULK_JOB_CLASS, BULK_DEPENDENCY_JARS);
		
		
		JSONObject statObject = new JSONObject();
		statObject.put("job_output", xrefJobStatus.getExecutionMessages());
		if ( xrefJobStatus.getStatusCode() == 1) {
			throw new RuntimeException(xrefJobStatus.getExecutionMessages());
		}
		String endTime = TimeZoneDateHelper.getFormattedDateString();
		crossReferenceDao.bulkmergeLogUpdate(xrefConditionId, new CrossReferenceLogs(ilInfo.getiL_id(),CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.BULKMERGE.toString(), 
				conditionName, referenceField, xReferenceField, fileInfoId,applicableDate,statObject.toString(), startDate, endTime, modification), clientAppJdbcTemplate);
	}
	

	@Override
	public int splitLog(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate) {
		if(crossReferenceLogs.getId() == 0) {
			return crossReferenceDao.splitLogInsert(crossReferenceLogs, clientAppJdbcTemplate);
		}else {
			return crossReferenceDao.splitLogUpdate(crossReferenceLogs, clientAppJdbcTemplate);
		}
	}
	
	@Override
	public int manualmergeLog(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate) {
		if(crossReferenceLogs.getId() == 0) {
			return crossReferenceDao.manualmergeLogInsert(crossReferenceLogs,clientAppJdbcTemplate);
		}else {
			return crossReferenceDao.manualmergeLogUpdate(crossReferenceLogs,clientAppJdbcTemplate);
		}
	}

	@Override
	public List<CrossReferenceLogs> getCrossReferenceLogsInfo(int ilId, JdbcTemplate clientAppDbJdbcTemplate) {
		return crossReferenceDao.getCrossReferenceLogsInfo(ilId, clientAppDbJdbcTemplate);
	}

	@Override
	public CrossReferenceLogs getCrossReferenceLogsInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		return crossReferenceDao.getCrossReferenceLogsInfoById(id,clientAppDbJdbcTemplate);
	}

	@Override
	public List<CrossReferenceLogs> getAllCrossReferences(String clientIdfromHeader, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return crossReferenceDao.getAllCrossReferences(clientIdfromHeader, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveorEditAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo, User user, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		if(crossReferenceForm.getId() == 0) {
			return crossReferenceDao.saveAutoMergeCrossReference(crossReferenceForm, ilInfo, user, clientJdbcTemplate, clientAppDbJdbcTemplate);
		}else {
			return crossReferenceDao.updateAutoMergeCrossReference(crossReferenceForm, ilInfo, user, clientJdbcTemplate, clientAppDbJdbcTemplate);
		}
	}

	@Override
	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return crossReferenceDao.getCrossReferencesByIlId(ilId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public void processAutoMergeCrossReference(ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials, CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate)
	{
		crossReferenceDao.processAutoMergeCrossReference(ilInfo, user, clientDbCredentials, crossReferenceLogs, clientJdbcTemplate, clientAppDbJdbcTemplate);
	}

	@Override
	public void archiveCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		crossReferenceDao.archiveCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
	}

	@Override
	public void activateCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		crossReferenceDao.activateCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
	}

	@Override
	public void processDeleteCrossReference(CrossReferenceLogs crossReferenceLogs, ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials,
			     JdbcTemplate clientAppDbJdbcTemplate) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException
	{
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserId());
			String dbHost = clientDbCredentials.get("region_hostname").toString();
			String dbPort = clientDbCredentials.get("region_port").toString();
			String dbUserName = clientDbCredentials.get("clientdb_username").toString();
			String dbPassword = clientDbCredentials.get("clientdb_password").toString();
			String mainSchemaName = clientDbCredentials.get("clientdb_schema").toString();
			String stagingSchemaName = clientDbCredentials.get("clientdb_staging_schema").toString();
			LOG.info("Cross reference unmerge job class name " + UNMERGE_JOB_CLASS );
			LOG.info("Cross reference unmerge  dependency jars " + UNMERGE_DEPENDENCY_JARS );
			
			Map<String, String> xRefContextParams = MinidwServiceUtil.getXRefContextParams(dbHost, dbPort, stagingSchemaName, dbUserName, dbPassword, dbHost, 
													dbPort, mainSchemaName, dbUserName, dbPassword, ilInfo, user.getClientId(), Constants.Temp.getTempFileDir(), "","",
													"",crossReferenceLogs.getApplicableDate(), crossReferenceLogs.getId(), crossReferenceLogs.getXrefExecutionType(), crossReferenceLogs.getConditionName());
			
			JobExecutionInfo xrefJobStatus = null;
					
			if(crossReferenceLogs.getTypeOfMerge().equals(CrossReferenceConstants.BULKMERGE.toString())) {
				xrefJobStatus = MinidwServiceUtil.runXrefJob(xRefContextParams, ilInfo, BULK_JOB_CLASS, BULK_DEPENDENCY_JARS);
			}else {
				xrefJobStatus = MinidwServiceUtil.runXrefJob(xRefContextParams, ilInfo, UNMERGE_JOB_CLASS, UNMERGE_DEPENDENCY_JARS);
			}
			
			JSONObject statObject = new JSONObject();
			statObject.put("job_output", xrefJobStatus.getExecutionMessages());
			if ( xrefJobStatus.getStatusCode() == 1) {
				throw new RuntimeException(xrefJobStatus.getExecutionMessages());
			}
	
	}
	
	
	
	
	
	
}