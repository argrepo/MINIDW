/**
 * 
 */
package com.datamodel.anvizent.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rajesh.anthari Created Date: 14-Feb-2017 Last Modified Date:
 *         14-Feb-2017
 */
public interface CrossReferenceService {

	public List<ILInfo> getAllClientILsForXref(String clientId, JdbcTemplate clientAppDbJdbcTemplate);

	public List<Object> getDistinctValues(String columnName, String tableName, String xRefTableName, String columnValue,
			boolean isXrefValueNull, String xRefColumnName, JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> getMatchedRecordWithMap(String columnName, List<String> values, String tableName,
			String xRefTableName, boolean isXrefValueNull, String xRefColumnName, JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> mergeXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName,
			String xRefKeyColumnName, List<Column> columns, List<String> mergeColumnValues,
			String xReferenceColumnValue, String columnName, String ilXreferenceValue, String conditionName, JdbcTemplate clientJdbcTemplate);

	public boolean splitXreferenceRecords(String ilTableName, String xRefTableName, String autoincrementColumnName,
			String xRefKeyColumnName, List<Column> columns, List<String> splitColumnValues,
			JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> getExistingXrefRecord(String tableName, String columnName, String existingXrefValue,
			JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> mergeCrossReferenceRecordsWithExistingXref(String ilTableName, String existingXrefKey,
			List<String> mergeColumnValues, String xRefKeyColumnName, String autoincrementColumnName,
			JdbcTemplate clientJdbcTemplate);

	public int crossReferenceAuditLogs(String ilId, String columnName, String crossReferenceOption,
			String ilColumnValue, String newOrExistingXref, String xReferenceColumnValue,
			List<String> mergeColumnValues, String autoXrefQueries, String clientId, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate);

	public List<CrossReferenceForm> getCrossReferenceInfoById(int ilid, String clientId,
			JdbcTemplate clientAppDbJdbcTemplate);

	public String getColumnValues(int id, String clientId, JdbcTemplate clientAppDbJdbcTemplate);
	
	public String getAutoMergeQueriesById(int id,String clientId,JdbcTemplate jdbcTemplate) ;

	public int unMergeSelectedCrossReferenceRecord(String ilTableName, String autoincrementColumnName,
			String xRefKeyColumnName, Integer ilColumnValue, JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> getXrefTableRecordsWithMap(String xrefIlTableName, JdbcTemplate clientJdbcTemplate);

	public Map<String, Object> getUnMergeRowWithColumns(CrossReferenceForm crossReferenceForm,
			JdbcTemplate clientJdbcTemplate);
	
	public Map<String, Object> processAutoMergeCrossReference(CrossReferenceForm crossReferenceForm,ILInfo ilInfo,User user,JdbcTemplate clientJdbcTemplate,
			JdbcTemplate clientAppJdbcTemplate) throws Exception;
	
	public void processBulkMergeCrossReference(Integer xrefConditionId, ILInfo ilInfo,User user,Map<String, Object> clientDbCredentials,String filePath, String referenceField, String xReferenceField,
			int fileInfoId,String conditionName, String xrefExecutionType, String applicableDate, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppJdbcTemplate) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException ;
	
	public int manualmergeLog(CrossReferenceLogs crossReferenceLogs,JdbcTemplate clientAppJdbcTemplate);
	
	public int splitLog(CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientAppJdbcTemplate);

	public List<CrossReferenceLogs> getCrossReferenceLogsInfo(int ilId, JdbcTemplate clientAppDbJdbcTemplate);

	public CrossReferenceLogs getCrossReferenceLogsInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate);

	public List<CrossReferenceLogs> getAllCrossReferences(String clientIdfromHeader, JdbcTemplate clientAppDbJdbcTemplate);

	public int saveorEditAutoMergeCrossReference(CrossReferenceForm crossReferenceForm, ILInfo ilInfo, User user, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate);

	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate);

	public void processAutoMergeCrossReference(ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials, CrossReferenceLogs crossReferenceLogs, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate);

	public void archiveCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate);

	public void activateCrossReferenceById(String clientId, Integer conditionId, JdbcTemplate clientAppDbJdbcTemplate);

	public void processDeleteCrossReference(CrossReferenceLogs crossReferenceLogs,  ILInfo ilInfo, User user, Map<String, Object> clientDbCredentials,  JdbcTemplate clientAppDbJdbcTemplate) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException;

}
