package com.datamodel.anvizent.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.amazon.AmazonS3Utilities;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.ClassPathException;
import com.datamodel.anvizent.common.exception.FlatFileReadingException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ParseCSV;
import com.datamodel.anvizent.helper.ParseExcel;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
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
 * 
 * @author rakesh.gajula
 *
 */

public class FileServiceImpl implements FileService {

	protected static final Log LOGGER = LogFactory.getLog(FileServiceImpl.class);
	@Autowired
	private FileDao fileDao;
	
	public FileServiceImpl(FileDao fileDao) {
		
		this.fileDao = fileDao;
	}

	@Override
	public FileDao getFileDao() {
		return fileDao;
	}

	//@PostConstruct
	public void addFilesToClassPath() {
		try {

			CommonUtils.addFilesToClassPath();

		} catch (ClassPathException e) {
			LOGGER.error("error while on ApplicationEvent");
		} catch (Exception e) {
			LOGGER.error("", e);;
		}

		return;
	}

	@Override
	public String uploadFileToS3(File originalFile, String userId, Integer packegeId, String userName, Integer ilConnectionMappingId,
			HttpServletRequest request,S3BucketInfo s3BucketInfo,JdbcTemplate clientJdbcTemplate) throws OnpremFileCopyException,AmazonS3Exception {

		// upload the file into the bucket of S3 with dir :
		// userId/packageId/encryptedFileName

		String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
		return uploadFileToS3(originalFile, userId, packegeId, userName, ilConnectionMappingId, deploymentType, request.getHeader(Constants.Config.CSV_SAVE_PATH), s3BucketInfo ,clientJdbcTemplate);
		
	}
	
	

	@Override
	public String uploadFileToS3(File originalFile, String userId, Integer packegeId, String userName, Integer ilConnectionMappingId,
			String deploymentType, String csvSavePath,S3BucketInfo s3BucketInfo , JdbcTemplate clientJdbcTemplate) throws OnpremFileCopyException,AmazonS3Exception  {
		String filePath = null;
		SourceFileInfo sourceFileInfo = new SourceFileInfo();
		String fileName = originalFile.getName();
		if ( StringUtils.isBlank(csvSavePath)) {
			csvSavePath = Constants.Temp.getTempFileDir();
		}
		if (StringUtils.isNotBlank(deploymentType) && deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			
			String fileExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + CommonUtils.generateUniqueIdWithTimestamp();
			String logicalFileName = formattedFileName + fileExtention;
			String directoryPath = csvSavePath+"/uploadedFiles/"+userId+"/"+packegeId+"/";
			try {
				FileUtils.forceMkdir(new File(directoryPath));
			} catch (IOException e) {
				throw new OnpremFileCopyException("File copying failed while writing (Onprem) :"+e.getMessage(), e);
			}
			originalFile.renameTo(new File(directoryPath + logicalFileName));
			originalFile = new File(directoryPath + logicalFileName);
			sourceFileInfo.setUploadStartTime(CommonDateHelper.formatDateAsString(new Date()));
			sourceFileInfo.setUploadEndTime(CommonDateHelper.formatDateAsString(new Date()));
			
			filePath = originalFile.getAbsolutePath();
		} else {
			if (StringUtils.isNotBlank(s3BucketInfo.getAccessKey()) && StringUtils.isNotBlank(s3BucketInfo.getSecretKey()) && StringUtils.isNotBlank(s3BucketInfo.getBucketName())) {

				if (originalFile != null && userId != null && packegeId != null ) {
					
					/* TODO we can make the below variable as global */
					AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(), s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
					String folderName = ""; //userId + SUFFIX + packegeId + SUFFIX;
					sourceFileInfo.setUploadStartTime(CommonDateHelper.formatDateAsString(new Date()));
					LOGGER.info("file upload started..." + new Date());
					String s3filePath = amazonS3Utilities.uploadFileToS3(originalFile, folderName, true);
					LOGGER.info("file upload completed..." + new Date());
					sourceFileInfo.setUploadEndTime(CommonDateHelper.formatDateAsString(new Date()));
					filePath = s3filePath;
					LOGGER.info("File uploaded to S3 :" + filePath);
				} else {
					throw new AmazonS3Exception("Invalid details for s3 upload");
				}
			} else {
				throw new AmazonS3Exception("Invalid s3 bucket details ");
			}
		}
		

		String storageType = Constants.Config.STORAGE_TYPE_S3;
		if (StringUtils.isNotBlank(deploymentType) && deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			storageType = Constants.Config.STORAGE_TYPE_LOCAL;
		}
		
		
		// save file info uploaded to s3
		String originalFileName = fileName.substring(0, fileName.lastIndexOf("."));
		String fileType = StringUtils.substringAfterLast(fileName, ".");
		long fileSize = originalFile.length();
		String fileSizeInBytes = String.valueOf(Math.round(fileSize));
		sourceFileInfo.setFileName(originalFileName);
		sourceFileInfo.setUserId(Integer.parseInt(userId));
		sourceFileInfo.setFileType(fileType);
		sourceFileInfo.setFileSize(fileSizeInBytes);
		sourceFileInfo.setBucketName(s3BucketInfo.getBucketName());
		sourceFileInfo.setPackageId(packegeId);
		sourceFileInfo.setStorageType(storageType);
		sourceFileInfo.setIlConnectionMappingId(ilConnectionMappingId);
		sourceFileInfo.setFilePath(filePath);
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(userName);
		sourceFileInfo.setModification(modification);
		fileDao.saveUploadedFileInfo(sourceFileInfo , clientJdbcTemplate);

		return filePath;
	}
	
	@Override
	public void saveUploadedFileInfo(SourceFileInfo sourceFileInfo , JdbcTemplate clientJdbcTemplate) {
		fileDao.saveUploadedFileInfo(sourceFileInfo, clientJdbcTemplate);
	}
	

	public void writeIntoFile(File source, File dest) throws IOException {
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		try {
			instream = new FileInputStream(source);
			outstream = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					LOGGER.error("", e);;
				}
			}
			if (outstream != null) {
				try {
					outstream.close();
				} catch (IOException e) {
					LOGGER.error("", e);;
				}
			}

		}
	}

	@Override
	public String uploadFileIntoServer(MultipartFile file, String userId, Integer packegeId, HttpServletRequest request) {

		File tempSourceFile = null;
		File tempDestFile = null;
		String tempDestFilePath = null;
		try {
			String dir = CommonUtils.createDir(Constants.Temp.getTempFileDir());
			tempSourceFile = CommonUtils.multipartToFile(file);
			tempDestFilePath = dir + file.getOriginalFilename();
			tempDestFile = new File(tempDestFilePath);
			writeIntoFile(tempSourceFile, tempDestFile);
			tempDestFilePath = EncryptionServiceImpl.getInstance().encrypt(tempDestFilePath);
		} catch (IOException e) {
			LOGGER.error("error while uploadFileIntoServer()", e);
			;
		} catch (Exception e) {
			LOGGER.error("error while uploadFileIntoServer()", e);
			;
			throw new AnvizentRuntimeException(e);
		} finally {
			if (tempSourceFile != null) {
				tempSourceFile.delete();
			}
		}

		return tempDestFilePath;
	}

	@Override
	public List<String> getHeadersFromFile(String filePath, String fileType, String separatorChar, String stringQuoteChar) throws Exception {

		List<String> headers = null;
		switch (fileType) {
			case Constants.FileType.CSV:
				// get the headers from the csv file
				LOGGER.info("in getHeadersFromCSV()");
				ParseCSV parseCSV = new ParseCSV(filePath);
				headers = parseCSV.readColumns(separatorChar, stringQuoteChar);
				break;
			case Constants.FileType.XLS:
				// get the headers from the xls file
				LOGGER.info("in getHeadersFromXLS()");
				ParseExcel parseExcel = new ParseExcel(filePath);
				headers = parseExcel.getHeadersFromFile(filePath);
				break;
			case Constants.FileType.XLSX:
				// get the headers from the xlsx file
				LOGGER.info("in getHeadersFromXLSX()");
				ParseExcel parseExcel1 = new ParseExcel(filePath);
				headers = parseExcel1.getHeadersFromXLSXFile(filePath);
				break;
		}
		return headers;
	}

	@Override
	public List<String> getColumnsDataTypeFromFile(String filePath, String fileType, String separatorChar, String stringQuoteChar) throws Exception {

		List<String> datatypes = null;
		switch (fileType) {
			case Constants.FileType.CSV:
				// get column data types from the csv file
				LOGGER.info("in getDataTypesFromCSV()");
				ParseCSV parseCSV = new ParseCSV(filePath);
				datatypes = parseCSV.getColumnsDataType(separatorChar, stringQuoteChar);
				break;
			case Constants.FileType.XLS:
				// get column data types from the xls file
				LOGGER.info("in getDataTypesFromXLS()");
				ParseExcel parseExcel = new ParseExcel(filePath);
				datatypes = parseExcel.getColumnsDataType(filePath);
				break;
			case Constants.FileType.XLSX:
				// get column data types from the xlsx file
				LOGGER.info("in getDataTypesFromXLSX()");
				ParseExcel parseExcel1 = new ParseExcel(filePath);
				datatypes = parseExcel1.getColumnDataTypesFromXLSXFile(filePath);
				break;
		}
		return datatypes;
	}


	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean insertFileColumnDetails(FileInfo fileInfo , JdbcTemplate clientJdbcTemplate) {

		boolean insertedStatus = false;
		insertedStatus = fileDao.insertFileColumnDetails(fileInfo, clientJdbcTemplate);
		return insertedStatus;
	}

	@Override
	public List<String> getFileHeaderColumns(String clientId, String packageId, String filename, String mappingid, JdbcTemplate clientJdbcTemplate) {

		List<String> columns = null;

		try {

			columns = fileDao.getFileHeaderColumns(clientId, packageId, filename, mappingid, clientJdbcTemplate);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return columns;
	}
	
	@Override
	public boolean updateFileHeaderByFileId(String headers, String userId, int packageId, int fileId,
			JdbcTemplate clientJdbcTemplate) {
		return fileDao.updateFileHeaderByFileId(headers, userId, packageId, fileId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public Map<String, Object> processDataFromFile(String filePath, String fileType, String separatorChar, String stringQuoteChar, ClientData clientData,
			boolean validateInsert, JdbcTemplate clientJdbcTemplate) throws Exception {

		// deprecated
		return null;
	}

	@Override
	public Map<String, Object> processDataFromFileBatch(String filePath, String fileType, String separatorChar, String stringQuoteChar, ClientData clientData,
			String clientSchemaStaging, JdbcTemplate clientJdbcTemplate) throws FlatFileReadingException {

		Map<String, Object> processedData = null;

		DataSource dataSource = clientJdbcTemplate.getDataSource();

		switch (fileType) {
			case Constants.FileType.CSV:
				ParseCSV parseCSV = new ParseCSV(filePath);
				processedData = parseCSV.processCSVDataBatch(clientData, dataSource, separatorChar, stringQuoteChar, clientSchemaStaging);
				break;

			case Constants.FileType.XLS:
				ParseExcel parseExcel = new ParseExcel(filePath);
				processedData = parseExcel.processExcelDataBatch(filePath, clientData, dataSource, clientSchemaStaging);
				break;

			case Constants.FileType.XLSX:
				ParseExcel parseExcel1 = new ParseExcel(filePath);
				processedData = parseExcel1.processXLSXDataBatch(filePath, clientData, dataSource, clientSchemaStaging);
				break;
		}

		return processedData;
	}

	@Override
	public void truncateTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		fileDao.truncateTable(schemaName, tableName, clientJdbcTemplate);
	}

	public Map<String, Object> processDataFromFile(String filePath, String fileType, String separatorChar, String stringQuoteChar, ClientData clientData,
			JdbcTemplate clientJdbcTemplate) throws Exception {
		return processDataFromFile(filePath, fileType, separatorChar, stringQuoteChar, clientData, true, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getFileInfoForCustomPackage(String clientId, String packageId, JdbcTemplate clientJdbcTemplate) {
		Map<String, Object> data = null;
		data = fileDao.getFileInfoForCustomPackage(clientId, packageId, clientJdbcTemplate);

		return data;
	}

	@Override
	public List<FileInfo> getFileInfoByPackage(String clientId, int packageId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getFileInfoByPackage(clientId, packageId, clientJdbcTemplate);
	}

	@Override
	public List<Table> getCustomFileTempTableMappings(String clientId, String packageId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomFileTempTableMappings(clientId, packageId, clientJdbcTemplate);
	}

	@Override
	public FileInfo getFileInfoByFileId(String clientId, String packageId, String fileId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getFileInfoByFileId(clientId, packageId, fileId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public Table processTempTableForFile(FileInfo fileInfo) {
		return CommonUtils.processTempTableForFile(fileInfo);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean insertTempTableMapping(FileInfo fileInfo, Table table, Modification modification, JdbcTemplate clientJdbcTemplate) {
		return fileDao.insertTempTableMapping(fileInfo, table, modification, clientJdbcTemplate);
	}

	@Override
	public List<Table> getCustomTempTables(String clientId, String packageId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomTempTables(clientId, packageId, clientJdbcTemplate);
	}
	
	@Override
	public Table getCustomTempTableByMappingId(String clientId, String packageId, int mappingId,
			JdbcTemplate clientJdbcTemplate) {
		// TODO Auto-generated method stub
		return fileDao.getCustomTempTableByMappingId(clientId, packageId, mappingId, clientJdbcTemplate);
	}

	@Override
	public boolean validateCustomTempTablesQuery(String clientId, String packageId, String industryId, String query, JdbcTemplate clientJdbcTemplate) {
		return fileDao.validateCustomTempTablesQuery(clientId, packageId, industryId, query, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean saveCustomTempTablesQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		return fileDao.saveCustomTempTablesQuery(clientData, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean saveCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String tableid,
			String ccols, String schemaName, Modification modification, JdbcTemplate clientJdbcTemplate) {
		return fileDao.saveCustomTargetDerivativeTable(clientId, packageId, industryId, query, tablename, tableid, ccols, schemaName, modification, clientJdbcTemplate);
	}

	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public Table createCustomTargetDerivativeTable(String clientId, String packageId, String industryId, String query, String tablename, String targetTable,
			String ccols, String schemaName, JdbcTemplate clientJdbcTemplate) {
		return fileDao.createCustomTargetDerivativeTable(clientId, packageId, industryId, query, tablename, targetTable, ccols, schemaName, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public Map<String, Object> processCustomTargetTableQuery(ClientData clientData, String clientStagingSchema, JdbcTemplate clientJdbcTemplate) {
		return fileDao.processCustomTargetTableQuery(clientData, clientStagingSchema, clientJdbcTemplate);
	}

	@Override
	public List<TableDerivative> getCustomTargetDerivativeTables(String clientId, int packageId, Integer tableId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomTargetDerivativeTables(clientId, packageId, tableId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public Map<String, Object> processCustomTargetDerivativeTable(TableDerivative tableDerivative, String targetTableName, JdbcTemplate clientJdbcTemplate) {
		return fileDao.processCustomTargetDerivativeTable(tableDerivative, targetTableName, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateCustomTargetDerivativeTableResults(TableDerivative tableDerivative, Map<String, Object> processedResults, Modification modification, JdbcTemplate clientJdbcTemplate) {
		fileDao.updateCustomTargetDerivativeTableResults(tableDerivative, processedResults, modification, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateFilesHavingSameColumns(String clientId, int packageId, boolean filesHavingSameColumns, JdbcTemplate clientJdbcTemplate) {
		return fileDao.updateFilesHavingSameColumns(clientId, packageId, filesHavingSameColumns, clientJdbcTemplate);

	}

	@Override
	public String processFileMappingWithIL(String filePath, String fileType, String separatorChar, String stringQuoteChar, List<String> iLColumnNames,
			List<String> selectedFileHeaders, List<String> dafaultValues) throws IOException {
		String destFilePath = null;
		String outputFileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "fileMappingWithIL/new/");
		String outputFilePath = outputFileDir + StringUtils.substringAfterLast(filePath, "/");
		boolean isProcessed = Boolean.FALSE;
		switch (fileType) {
			case Constants.FileType.CSV:
				ParseCSV parseCSV = new ParseCSV(filePath);
				isProcessed = parseCSV.processCSVDataToFile(outputFilePath, iLColumnNames, selectedFileHeaders, dafaultValues, separatorChar, stringQuoteChar);
				if (isProcessed)
					destFilePath = outputFilePath;
				break;

			case Constants.FileType.XLS:
				ParseExcel parseExcel = new ParseExcel(filePath);
				isProcessed = parseExcel.processXLSDataToFile(outputFilePath, iLColumnNames, selectedFileHeaders, dafaultValues);
				if (isProcessed)
					destFilePath = outputFilePath;
				break;

			case Constants.FileType.XLSX:
				ParseExcel parseExcel1 = new ParseExcel(filePath);
				isProcessed = parseExcel1.processXLSXDataToFile(filePath, outputFilePath, iLColumnNames, selectedFileHeaders, dafaultValues);

				if (isProcessed)
					destFilePath = outputFilePath;
				break;
		}
		return destFilePath;
	}

	@Override
	public void deleteFileFromS3(String filePath,HttpServletRequest request, JdbcTemplate clientJdbcTemplate, S3BucketInfo s3BucketInfo) throws Exception {
		
		String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
		if (StringUtils.isNotBlank(deploymentType) && deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			FileUtils.deleteQuietly(new File(filePath));
		} else {
			AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(), s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
			amazonS3Utilities.deleteFileFromS3Bucket(filePath);
		}
		fileDao.deleteUploadedFileInfo(filePath, clientJdbcTemplate);
	}

	@Override
	public List<String> getAllTables(String clientId, String clientSchemaName, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getAllTables(clientId, clientSchemaName, clientJdbcTemplate);
	}

	public boolean isTableExists(String clientId, String clientSchemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		return fileDao.isTableExists(clientId, clientSchemaName, tableName, clientJdbcTemplate);
	}

	@Override
	public void deleteDirInS3(String s3DirPath) {
		/* Not used */
	}

	@Override
	public String uploadFileIntoServer(File file, String userId, String dir) throws IOException {

		File tempDestFile = null;
		CommonUtils.createDir(dir);
		String tempDestFilePath = null;
		if (file != null) {
			tempDestFilePath = dir + File.separator + file.getName();
			tempDestFile = new File(tempDestFilePath);
			writeIntoFile(file, tempDestFile);
		}
		return tempDestFilePath;

	}

	@Override
	public List<List<String>> getFileDataPreview(String filePath, String fileType, String delimeter) throws Exception {
		// Auto-generated method stub

		List<List<String>> previewData = new ArrayList<>();
		switch (fileType) {
			case Constants.FileType.CSV:
				LOGGER.info("in getPreviewDataFromCSV()");
				ParseCSV csv = new ParseCSV(filePath);
				previewData = csv.processCSVDataForPreview(delimeter, "");
				break;

			case Constants.FileType.XLS:
				LOGGER.info("in getPreviewDataFromXLS()");
				ParseExcel xls = new ParseExcel(filePath);
				previewData = xls.processExcelDataForPreview(filePath);
				break;

			case Constants.FileType.XLSX:
				LOGGER.info("in getPreviewDataFromXLSX()");
				ParseExcel xlsx = new ParseExcel(filePath);
				previewData = xlsx.processXLSXDataForPreview(filePath);
				break;
		}
		return previewData;
	}
	
	@Override
	public List<List<String>> getCSVFileDataPreview(String filePath, String delimeter,int limit) throws Exception {
		List<List<String>> previewData = new ArrayList<>();
		if(filePath != null){
			LOGGER.info("in getPreviewDataFromCSV()");
			ParseCSV csv = new ParseCSV(filePath);
			previewData = csv.processCSVDataForPreview(delimeter, "",limit);
		}
		return previewData;
	}

	@Override
	public TableDerivative getCustomTargetDerivativeTablesById(String clientId, int packageId, Integer targetTableId, Integer tableId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomTargetDerivativeTablesById(clientId, packageId, targetTableId, tableId, clientJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean deleteCustomTempTables(String packageId, String userId, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate clientJdbcTemplate, S3BucketInfo s3BucketInfo) {
		return fileDao.deleteCustomTempTables(packageId, userId, clientAppDbJdbcTemplate, s3BucketInfo.getAccessKey(), s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName(), clientJdbcTemplate);
	}

	@Override
	public List<String> fileNameByIlId(int ilId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.fileNameByIlId(ilId, clientJdbcTemplate);

	}

	@Override
	public List<String> fileNameByXRefIlId(int ilId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.fileNameByXRefIlId(ilId, clientJdbcTemplate);

	}

	@Override
	public String getTempTableNameByMappingId(int connectionMappingId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getTempTableNameByMappingId(connectionMappingId, clientJdbcTemplate);

	}


	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public boolean insertIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {
		return fileDao.insertIlConnectionWebServiceMapping(fileInfo, table, modification, webService, clientJdbcTemplate);
	}

	@Override
	public List<Table> getCustomTempTablesForWebservice(String clientId, String packageId, int ilId, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomTempTablesForWebservice(clientId, packageId, ilId, clientJdbcTemplate);
	}

	@Override
	public void deleteIlConnectionWebServiceMapping(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {
		fileDao.deleteIlConnectionWebServiceMapping(fileInfo, table, modification, webService, clientJdbcTemplate);
	}

	@Override
	public void deletefileHeader(FileInfo fileInfo, Table table, Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate) {
		fileDao.deletefileHeader(fileInfo, table, modification, webService, clientJdbcTemplate);
	}


	@Override
	public String getCustomTempQuery(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		return fileDao.getCustomTempQuery(clientData, clientJdbcTemplate);
	}

	@Override
	public void deleteTableDataByCalenderKey(String schemaName, String insertDateIntoTableName, Integer calendarKey,
			JdbcTemplate clientJdbcTemplate) {
         fileDao.deleteTableDataByCalenderKey(schemaName, insertDateIntoTableName, calendarKey, clientJdbcTemplate);		
	}
	
	@Override
	public int insertIlConnectionWebServiceMappingForJsonOrXml(Modification modification, WebService webService, JdbcTemplate clientJdbcTemplate)
	{
		return fileDao.insertIlConnectionWebServiceMappingForJsonOrXml(modification, webService,clientJdbcTemplate);	
	}

	@Override
	public int updateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file, Modification modification,JdbcTemplate clientJdbcTemplate)
	{
		return fileDao.updateDbConSSlAuthCertFilesInfo(dbTypeId,conId,fileName,filePath,file,modification,clientJdbcTemplate);
	}

	@Override
	public int deleteAndUpdateDbConSSlAuthCertFilesInfo(int dbTypeId, int conId, String fileName, String filePath, File file,String deleteFileName, Modification modification, JdbcTemplate clientJdbcTemplate)
	{
		return fileDao.deleteAndUpdateDbConSSlAuthCertFilesInfo(dbTypeId,conId,fileName,filePath,file,deleteFileName,modification,clientJdbcTemplate);
	}

	@Override
	public List<String> getDbConSslAuthCertFilesInfo(int dbTypeId, int conId, JdbcTemplate clientJdbcTemplate)
	{
		return fileDao.getDbConSslAuthCertFilesInfo(dbTypeId,conId, clientJdbcTemplate);
	}

	@Override
	public int updateSSLTrustKeyStorePathsAtDbConnection(int dbTypeId, int conId, String filePaths, Modification modification, JdbcTemplate clientJdbcTemplate)
	{
		return fileDao.updateSSLTrustKeyStorePathsAtDbConnection(  dbTypeId,   conId,   filePaths,   modification,   clientJdbcTemplate);
	}
}
