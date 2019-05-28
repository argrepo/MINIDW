/**
 * 
 */
package com.datamodel.anvizent.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.datamodel.anvizent.common.exception.FlatFileReadingException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.WebService;

/**
 * @author rakesh.gajula
 *
 */
public interface FileService {
	
	FileDao getFileDao();
	
	String uploadFileToS3(File file,String userId,Integer packegeId,String userName,Integer ilConnectionMappingId,HttpServletRequest request,S3BucketInfo s3BucketInfo,JdbcTemplate clientJdbcTemplate) throws OnpremFileCopyException,AmazonS3Exception;
	
	String uploadFileToS3(File file,String userId,Integer packegeId,String userName,Integer ilConnectionMappingId,String deploymentType, String csvSavePath,S3BucketInfo s3BucketInfo,JdbcTemplate clientJdbcTemplate) throws OnpremFileCopyException,AmazonS3Exception ;
	
	
	String uploadFileIntoServer(MultipartFile file,String userId,Integer packegeId,HttpServletRequest request);
	
	List<String> getHeadersFromFile(String filePath,String fileType,String separatorChar, String stringQuoteChar) throws Exception;
	
	boolean insertFileColumnDetails(FileInfo fileInfo, JdbcTemplate clientJdbcTemplate);
	
	List<String> getFileHeaderColumns(String clientId, String packageId, String filename, String mappingid , JdbcTemplate clientJdbcTemplate);
	
	boolean updateFileHeaderByFileId(String headers, String userId, int packageId, int fileId, JdbcTemplate clientJdbcTemplate);
	
	Map<String,Object> processDataFromFile(String filePath, String fileType, String seperatorChar, String stringQuoteChar, ClientData clientData,JdbcTemplate clientJdbcTemplate) throws Exception;
	
	Map<String,Object> processDataFromFileBatch(String filePath, String fileType, String seperatorChar, String stringQuoteChar, ClientData clientDatam,String clientSchemaStaging
			,JdbcTemplate clientJdbcTemplate) throws FlatFileReadingException;
	
	/*
	 * To check the data record already exist or not.
	 */
	Map<String,Object> processDataFromFile(String filePath, String fileType, String seperatorChar, String stringQuoteChar, ClientData clientData, boolean validateInsert
			,JdbcTemplate clientJdbcTemplate) 
							throws Exception;

	Map<String, Object> getFileInfoForCustomPackage(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);
	
	List<FileInfo> getFileInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate);
	
	int updateFilesHavingSameColumns(String clientId, int packageId, boolean filesHavingSameColumns, JdbcTemplate clientJdbcTemplate);
	
	FileInfo getFileInfoByFileId(String clientId, String packageId, String fileId, JdbcTemplate clientJdbcTemplate);
	
	Table processTempTableForFile(FileInfo fileInfo);
	
	boolean insertTempTableMapping(FileInfo fileInfo, Table table, Modification modification, JdbcTemplate clientJdbcTemplate);

	List<Table> getCustomTempTables(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);

	Table getCustomTempTableByMappingId(String clientId, String packageId, int mappingId, JdbcTemplate clientJdbcTemplate);
	
	boolean validateCustomTempTablesQuery(String clientId, String packageId, String industryId, String query,JdbcTemplate clientJdbcTemplate);

	boolean saveCustomTempTablesQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	Map<String,Object> processCustomTargetTableQuery(ClientData clientData, String clientStagingSchema,JdbcTemplate clientJdbcTemplate);
	
	String processFileMappingWithIL(String filePath,String fileType,String separatorChar, String stringQuoteChar,
			List<String> iLColumnNames,	List<String> selectedFileHeaders,List<String> dafaultValues) throws IOException;

	boolean saveCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query,
			String tablename, String tableid, String ccols, String schemaName,Modification modification, JdbcTemplate clientJdbcTemplate);

	Table createCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query,
			String tablename, String targetTable, String ccols, String schemaName,JdbcTemplate clientJdbcTemplate);

	List<TableDerivative> getCustomTargetDerivativeTables(String clientId, int packageId, Integer tableId, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> processCustomTargetDerivativeTable(TableDerivative tableDerivative, String targetTableName,JdbcTemplate clientJdbcTemplate);

	void updateCustomTargetDerivativeTableResults(TableDerivative tableDerivative,
			Map<String, Object> processedResults, Modification modification, JdbcTemplate clientJdbcTemplate);

	void truncateTable(String schemaName, String tableName,JdbcTemplate clientJdbcTemplate);

	List<Table> getCustomFileTempTableMappings(String clientId, String packageId, JdbcTemplate clientJdbcTemplate);
	
	void deleteFileFromS3(String filePath,HttpServletRequest request, JdbcTemplate clientJdbcTemplate, S3BucketInfo s3BucketInfo) throws Exception;

	List<String> getAllTables(String clientId, String clientSchemaName,JdbcTemplate clientJdbcTemplate);
	
	boolean isTableExists(String clientId, String clientSchemaName, String tableName,JdbcTemplate clientJdbcTemplate);
	
	void deleteDirInS3(String s3DirPath) throws AmazonServiceException;
	
	String uploadFileIntoServer(File file,String userId,String dir) throws IOException;
	
	List<List<String>> getFileDataPreview(String filePath, String fileType, String delimeter) throws Exception;
	
	public TableDerivative getCustomTargetDerivativeTablesById(String clientId, int packageId, Integer targetTableId, Integer tableId, JdbcTemplate clientJdbcTemplate);
	
	public boolean deleteCustomTempTables(String packageId, String userId,  JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate clientJdbcTemplate, S3BucketInfo s3BucketInfo);
	
	List<String> fileNameByIlId(int ilId, JdbcTemplate clientJdbcTemplate);
	
	List<String> fileNameByXRefIlId(int ilId, JdbcTemplate clientJdbcTemplate);

	List<String> getColumnsDataTypeFromFile(String filePath, String fileType, String separatorChar,
			String stringQuoteChar) throws Exception;

	String getTempTableNameByMappingId(int connectionMappingId, JdbcTemplate clientJdbcTemplate);

	boolean insertIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification,WebService webService, JdbcTemplate clientJdbcTemplate);
	
	
	List<Table> getCustomTempTablesForWebservice(String clientId, String packageId,int ilId, JdbcTemplate clientJdbcTemplate);
	
	void deleteIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification,WebService webService, JdbcTemplate clientJdbcTemplate);
	
	void deletefileHeader(FileInfo fileInfo, Table table, Modification modification,WebService webService, JdbcTemplate clientJdbcTemplate);
	
	void saveUploadedFileInfo(SourceFileInfo sourceFileInfo, JdbcTemplate clientJdbcTemplate);
	
	String getCustomTempQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<List<String>> getCSVFileDataPreview(String decryptedFilepath, String string, int i) throws Exception;

	void deleteTableDataByCalenderKey(String schemaName, String insertDateIntoTableName, Integer calendarKey, JdbcTemplate clientJdbcTemplate);

	int insertIlConnectionWebServiceMappingForJsonOrXml( Modification modification,WebService webService, JdbcTemplate clientJdbcTemplate);
	
	int updateDbConSSlAuthCertFilesInfo(int dbTypeId,int conId,String fileName,String filePath,File file,Modification modification,JdbcTemplate clientJdbcTemplate) ;
	
	int deleteAndUpdateDbConSSlAuthCertFilesInfo(int dbTypeId,int conId,String fileName,String filePath,File file,String deleteFileName ,Modification modification,JdbcTemplate clientJdbcTemplate) ;
	
	List<String> getDbConSslAuthCertFilesInfo(int dbTypeId,int conId,JdbcTemplate clientJdbcTemplate);
	
	int updateSSLTrustKeyStorePathsAtDbConnection(int dbTypeId,int conId,String filePaths,Modification modification,JdbcTemplate clientJdbcTemplate);
	
}
