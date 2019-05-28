/**
 * 
 */
package com.datamodel.anvizent.service.dao;

import java.io.File;
import java.util.List;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;

/**
 * @author srinu.chinka
 *
 */
public interface FileDao {

	boolean insertFileColumnDetails(FileInfo fileInfo , JdbcTemplate clientJdbcTemplate);

	List<String> getFileHeaderColumns(String clientId, String packageId, String filename, String mappingid, JdbcTemplate clientJdbcTemplate);

	boolean updateFileHeaderByFileId(String headers, String userId, int packageId, int fileId, JdbcTemplate clientJdbcTemplate);
	
	Map<String, Object> getFileInfoForCustomPackage(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);

	List<FileInfo> getFileInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate);

	int updateFilesHavingSameColumns(String clientId, int packageId, boolean filesHavingSameColumns, JdbcTemplate clientJdbcTemplate);

	FileInfo getFileInfoByFileId(String clientId, String packageId, String fileId, JdbcTemplate clientJdbcTemplate);

	boolean insertTempTableMapping(FileInfo fileInfo, Table table, Modification modification, JdbcTemplate clientJdbcTemplate);

	List<Table> getCustomTempTables(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);
	
	Table getCustomTempTableByMappingId(String clientId, String packageId, int mappingId, JdbcTemplate clientJdbcTemplate);

	boolean validateCustomTempTablesQuery(String clientId, String packageId, String industryId, String query, JdbcTemplate clientJdbcTemplate);

	boolean saveCustomTempTablesQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> processCustomTargetTableQuery(ClientData clientData, String clientStagingSchema, JdbcTemplate clientJdbcTemplate);

	boolean saveCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String tableid, String ccols,
			String schemaName, Modification modification, JdbcTemplate clientJdbcTemplate);

	Table createCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String targetTable,
			String ccols, String schemaName, JdbcTemplate clientJdbcTemplate);

	List<TableDerivative> getCustomTargetDerivativeTables(String clientId, int packageId, Integer tableId, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> processCustomTargetDerivativeTable(TableDerivative tableDerivative, String targetTableName, JdbcTemplate clientJdbcTemplate);

	void updateCustomTargetDerivativeTableResults(TableDerivative tableDerivative, Map<String, Object> processedResults, Modification modification, JdbcTemplate clientJdbcTemplate);

	void truncateTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate);

	List<Table> getCustomFileTempTableMappings(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);

	List<String> getAllTables(String clientId, String clientSchemaName, JdbcTemplate clientJdbcTemplate);

	boolean isTableExists(String clientId, String clientSchemaName, String tableName, JdbcTemplate clientJdbcTemplate);

	public TableDerivative getCustomTargetDerivativeTablesById(String clientId, int packageId, Integer targetTableId, Integer tableId, JdbcTemplate clientJdbcTemplate);

	public boolean deleteCustomTempTables(String packageId, String userId, JdbcTemplate clientAppDbJdbcTemplate  , String accessKey, String secretKey, String bucketName,
			JdbcTemplate clientJdbcTemplate);

	List<String> fileNameByIlId(int ilId, JdbcTemplate clientJdbcTemplate);

	List<String> fileNameByXRefIlId(int ilId, JdbcTemplate clientJdbcTemplate);

	String getTempTableNameByMappingId(int connectionMappingId, JdbcTemplate clientJdbcTemplate);

	void saveUploadedFileInfo(SourceFileInfo s3fileInfo, JdbcTemplate clientJdbcTemplate);

	void deleteUploadedFileInfo(String filePath, JdbcTemplate clientJdbcTemplate);

	boolean insertIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate);

	List<Table> getCustomTempTablesForWebservice(String clientId, String packageId, int ilId, JdbcTemplate clientJdbcTemplate);

	void deleteIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate);

	void deletefileHeader(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate);
	
	String getCustomTempQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	void deleteTableDataByCalenderKey(String schemaName, String insertDateIntoTableName, Integer calendarKey,
			JdbcTemplate clientJdbcTemplate);
	
	int insertIlConnectionWebServiceMappingForJsonOrXml(Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate);
	
	int updateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file, Modification modification,JdbcTemplate clientJdbcTemplate);
	
	int deleteAndUpdateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file,String deleteFileName, Modification modification, JdbcTemplate clientJdbcTemplate);
	
	List<String> getDbConSslAuthCertFilesInfo(int dbTypeId, int conId, JdbcTemplate clientJdbcTemplate);
	
	int updateSSLTrustKeyStorePathsAtDbConnection(int dbTypeId, int conId, String filePaths, Modification modification, JdbcTemplate clientJdbcTemplate);
}
