package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.ScheduleResult;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
@RestController("user_packageServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class PackageServiceDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(PackageServiceDataRestController.class);
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	FileService fileService;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@RequestMapping(value = "/saveMultipleTableMappingInfo/{packageId}/{industryId}", method = RequestMethod.POST)
	public ResponseEntity<Object> saveMultipleTableMappingInfo(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {

			LOGGER.info("pakcage id : " + packageId);
			if (StringUtils.isNotEmpty(packageId)) {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();

				String schemaName = clientDbDetails.get("clientdb_schema").toString();
				// staging schema name
				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
				String databaseHost = clientDbDetails.get("region_hostname").toString();
				String databasePort = clientDbDetails.get("region_port").toString();
				String databaseUserName = clientDbDetails.get("clientdb_username").toString();
				String databasePassword = clientDbDetails.get("clientdb_password").toString();

				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
						clientAppDbJdbcTemplate);
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
				boolean isEncryptionRequired = fileSettings.getFileEncryption();
				boolean isCustomTempTablesDeleted = fileService.deleteCustomTempTables(packageId, clientId, clientAppDbJdbcTemplate,clientJdbcTemplate
						 ,s3BucketInfo);

				if (!isCustomTempTablesDeleted) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null,
							locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
				}

				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);

				List<ClientData> schedulePackages = packageService.getILConnectionMappingInfoByPackage(clientId,
						clientIdfromHeader, Integer.parseInt(packageId), clientAppDbJdbcTemplate);
				List<FileInfo> files = fileService.getFileInfoByPackage(clientId, Integer.parseInt(packageId),
						clientAppDbJdbcTemplate);

				for (ClientData mappingInfo : schedulePackages) {

					ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();

					String filePathInS3 = null;
					boolean isManuallyUploaded = false;
					if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsHavingParentTable()) {

						if (mappingInfo.getIlConnectionMapping().getFilePath() != null) {
							filePathInS3 = mappingInfo.getIlConnectionMapping().getFilePath();

						}
						if (StringUtils.isNotBlank(filePathInS3)) {
							ilConnectionMapping.setFilePath(filePathInS3);
							ilConnectionMapping.setDelimeter(",");
							ilConnectionMapping.setFileType(Constants.FileType.CSV);
							isManuallyUploaded = true;
						}

					} else if (!ilConnectionMapping.getIsFlatFile() && ilConnectionMapping.getIsHavingParentTable()) {

						if (ilConnectionMapping.getiLConnection() == null
								&& ilConnectionMapping.getFilePath() == null) {
							File tempFile = null;

							try {
								ILConnectionMapping ilConnectionMappings = new ILConnectionMapping();
								ILConnection ilConnections = new ILConnection();

								StringBuffer query = new StringBuffer();
								query.append("select * from ");
								query.append(schemaName + ".");
								query.append(ilConnectionMapping.getParent_table_name());

								ilConnectionMappings.setiLquery(query.toString());
								ilConnections.setUsername(databaseUserName);
								ilConnections.setPassword(databasePassword);
								ilConnections.setServer(databaseHost + ":" + databasePort + "/" + schemaName);

								Database db = new Database();
								db.setId(1);
								db.setName("MySQL");
								db.setConnector_id(1);
								db.setProtocal(
										com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
								db.setDriverName(
										com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
								ilConnections.setDatabase(db);
								ilConnectionMappings.setTypeOfCommand("Query");
								ilConnectionMappings.setiLConnection(ilConnections);

								String filePath = CommonUtils.createFileByConncetion(ilConnectionMappings);
								if (StringUtils.isNotBlank(filePath)) {
									tempFile = new File(filePath);

									// upload the file to S3 for back up
									if (tempFile != null) {
										String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
										SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(
												s3BucketInfo, tempFile, clientId, Integer.parseInt(packageId),
												clientId,
												mappingInfo.getIlConnectionMapping().getConnectionMappingId(),
												deploymentType, "", false,isEncryptionRequired);
										filePathInS3 = sourceFileInfo.getFilePath();

									}

									if (tempFile != null) {
										tempFile.delete();
									}
									// download the file from S3 as input to ETL
									// job
									if (StringUtils.isNotBlank(filePathInS3)) {
										ilConnectionMapping.setFilePath(filePathInS3);
										ilConnectionMapping.setDelimeter(",");
										ilConnectionMapping.setFileType(Constants.FileType.CSV);
										isManuallyUploaded = true;
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					// file processing start for both database and flat file.
					if (ilConnectionMapping.getFilePath() != null) {
						String filePath = ilConnectionMapping.getFilePath();

						ClientData clientData = new ClientData();

						List<String> columns = fileService.getHeadersFromFile(filePath,
								ilConnectionMapping.getFileType(), ilConnectionMapping.getDelimeter(), null);

						StringBuilder fileHeaders = new StringBuilder();

						int index = 1, colslen = columns.size();

						for (String column : columns) {
							fileHeaders.append(column.replaceAll("\\s+", "_"));
							if (index < colslen)
								if (ilConnectionMapping.getFileType().equals(Constants.FileType.CSV)) {
									fileHeaders.append(ilConnectionMapping.getDelimeter());
								} else if (ilConnectionMapping.getFileType().equals(Constants.FileType.XLS)) {
									fileHeaders.append(",");
								} else if (ilConnectionMapping.getFileType().equals(Constants.FileType.XLSX)) {
									fileHeaders.append(",");
								}

							index++;
						}

						FileInfo fileInfo = new FileInfo();
						fileInfo.setFileHeaders(fileHeaders.toString());
						fileInfo.setDelimeter(ilConnectionMapping.getDelimeter());
						fileInfo.setFileType(ilConnectionMapping.getFileType());
						fileInfo.setPackageId(Integer.parseInt(packageId));
						fileInfo.setClientid(clientId);
						fileInfo.setFilePath(ilConnectionMapping.getFilePath());
						fileInfo.setIsFirstRowHasColumnNames(false);
						fileInfo.setModification(modification);
						fileInfo.setIsTempTableCreated(isManuallyUploaded);
						fileInfo.setIlMappingId(ilConnectionMapping.getConnectionMappingId());
						// inserting into fileinfo details

						if (!ilConnectionMapping.getIsFlatFile() && isManuallyUploaded) {
							fileService.insertFileColumnDetails(fileInfo, clientAppDbJdbcTemplate);
						} else {

							for (FileInfo file : files) {
								if (StringUtils.equals(file.getFilePath(), ilConnectionMapping.getFilePath())) {
									fileInfo.setFileId(file.getFileId());
									break;
								}
							}

						}
						LOGGER.info(" file id :  " + fileInfo.getFileId());

						// creating temp tables in staging schema.
						Table table = fileService.processTempTableForFile(fileInfo);

						table.setSchemaName(clientStagingSchema);

						table.setOriginalColumnNames(columns);

						clientData.setUserPackage(new Package());
						clientData.getUserPackage().setTable(table);
						Package userPackage = packageService.getPackageById(Integer.parseInt(packageId), clientId,
								clientAppDbJdbcTemplate);
						clientData.getUserPackage().setPackageId(Integer.parseInt(packageId));
						clientData.getUserPackage().setPackageName(userPackage.getPackageName());

						packageService.createTargetTable(clientData, clientJdbcTemplate);

						fileService.insertTempTableMapping(fileInfo, table, modification, clientAppDbJdbcTemplate);

						try {
							FileUtils.deleteQuietly(new File(filePath));
						} catch (Exception e) {
						}

					}
				}

				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentDuplicateFileNameException(
					messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null));
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}

	/**
	 * @param clientData
	 * @param clientId
	 * @return ilConnectionMapping
	 */
	@RequestMapping(value = "/importSourcesFromPackageToPackage", method = RequestMethod.POST)
	public DataResponse importSourcesFromPackageToPackage(@RequestBody ClientData clientData,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			Locale locale) {

		List<ILConnectionMapping> ilConnectionMappingList = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		LOGGER.info("clientId  -- > " + clientId);
		int packageId = clientData.getUserPackage().getPackageId();
		int existedPackageId = clientData.getUserPackage().getExistingPackageId();
		String storageType = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String filePath = "";
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToImportDataFromPackage", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ilConnectionMappingList = packageService.getIlConnectionMappingInfoByPackageId(clientId, existedPackageId,clientAppDbJdbcTemplate);
			Package existedDackage = packageService.getPackageById(existedPackageId, clientId, clientAppDbJdbcTemplate);
			fileService.updateFilesHavingSameColumns(clientId, packageId, existedDackage.getFilesHavingSameColumns(),
					clientAppDbJdbcTemplate);

			String onpremStoaragePath = request.getHeader(Constants.Config.CSV_SAVE_PATH);
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			if (StringUtils.isBlank(deploymentType)) {
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}
			if (deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				storageType = Constants.Config.STORAGE_TYPE_LOCAL;
			} else {
				storageType = Constants.Config.STORAGE_TYPE_S3;
			}

			if (ilConnectionMappingList != null && ilConnectionMappingList.size() > 0) {

				for (ILConnectionMapping ilConnectionMapping : ilConnectionMappingList) {
					if (ilConnectionMapping.getIsFlatFile()) {
						String s3FilePath = ilConnectionMapping.getFilePath();
						S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
						FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
						boolean isEncryptionRequired = fileSettings.getFileEncryption();
						
						List<String> downloadedFilesFromS3 = MinidwServiceUtil.downloadFilesFromS3(s3FilePath, clientIdfromHeader, deploymentType, false, s3BucketInfo, isEncryptionRequired);
						if (downloadedFilesFromS3 != null && downloadedFilesFromS3.size() > 0) {
							filePath = downloadedFilesFromS3.get(0);
						} else {
							continue;
						}
						File tempFile = new File(filePath);

						String basePath = FilenameUtils.getFullPath(filePath);
						String fileName = FilenameUtils.getName(filePath);
						String fileExtension = FilenameUtils.getExtension(filePath);
						String newFilePath = basePath
								+ fileName.substring(0, StringUtils.lastOrdinalIndexOf(fileName, "_", 3)) + "."
								+ fileExtension;
						// TODO Delete temp file if we download that from s3
						File destination = new File(newFilePath);
						FileUtils.copyFile(tempFile, destination);
						tempFile = destination;

						List<String> headers = fileService.getHeadersFromFile(tempFile.getAbsolutePath(),
								ilConnectionMapping.getFileType(), ilConnectionMapping.getDelimeter(), null);

						// upload the file
						// User user = userService.getUserDetails(clientId);
						SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFile,
								clientId, packageId, clientId,
								ilConnectionMapping.getConnectionMappingId(), deploymentType, "", false,isEncryptionRequired, onpremStoaragePath);
						filePath = sourceFileInfo.getFilePath();

						if (StringUtils.isNotBlank(filePath)) {
							ilConnectionMapping.setFilePath(filePath);

							// replacing spaces with '_' as table is not
							// accepting column name with spaces
							StringBuilder cols = new StringBuilder();
							int i = 1, length = headers.size();
							for (String column : headers) {
								cols.append(column.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_"));

								if (i < length)
									cols.append(",");
								i++;
							}
							// save file headers
							FileInfo fileInfo = new FileInfo();
							fileInfo.setClientid(clientId);
							fileInfo.setPackageId(packageId);
							fileInfo.setFilePath(filePath);
							fileInfo.setFileType(ilConnectionMapping.getFileType());
							fileInfo.setDelimeter(ilConnectionMapping.getDelimeter());
							fileInfo.setIsFirstRowHasColumnNames(ilConnectionMapping.getIsFirstRowHasColoumnNames());
							fileInfo.setFileHeaders(cols.toString());
							Modification modification = new Modification(new Date());
							modification.setCreatedBy(clientId);
							fileInfo.setModification(modification);
							int sourceFileInfoId = packageService.updateSourceFileInfo(sourceFileInfo, clientAppDbJdbcTemplate);
							fileInfo.setSourceFileInfoId(sourceFileInfoId);
							ilConnectionMapping.setSourceFileInfoId(sourceFileInfoId);
							fileService.insertFileColumnDetails(fileInfo, clientAppDbJdbcTemplate);

							try {
								FileUtils.deleteQuietly(tempFile);
							} catch (Exception e) {
							}
						}

					}
					if (!ilConnectionMapping.getIsHavingParentTable()) {
						ilConnectionMapping.setStorageType(storageType);
						ilConnectionMapping.setClientId(clientId);
						ilConnectionMapping.setPackageId(packageId);
						packageService.saveILConnectionMapping(ilConnectionMapping, clientAppDbJdbcTemplate);
					}
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {

			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToImportDataFromPackage", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileImportDataFromPackage",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/activateUserPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse activateUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") int packageId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (clientId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				int userId = Integer.parseInt(clientId);
				packageService.activateUserPackage(packageId, userId, clientAppDbJdbcTemplate);
				List<Package> packageList = packageService.getAllUserPackages(clientId, clientAppDbJdbcTemplate);
				for (Package userPackage : packageList) {
					if (!userPackage.getIsStandard()) {
						ClientData clientData = packageService.getTargetTableInfoByPackage(clientId,
								userPackage.getPackageId(), clientAppDbJdbcTemplate);
						if (clientData != null) {
							if (clientData.getUserPackage().getTable().getTableName() != null) {
								Table table = new Table();
								table.setTableName(clientData.getUserPackage().getTable().getTableName());
								table.setIsProcessed(clientData.getUserPackage().getTable().getIsProcessed());
								table.setNoOfRecordsFailed(
										clientData.getUserPackage().getTable().getNoOfRecordsFailed());
								table.setDuplicateRecords(clientData.getUserPackage().getTable().getDuplicateRecords());
								table.setTotalRecords(clientData.getUserPackage().getTable().getTotalRecords());
								table.setIsDirect(clientData.getUserPackage().getTable().getIsDirect());
								userPackage.setTable(table);
							}
						}
					}
				}
				dataResponse.setObject(packageList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.packageActivatedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToActivatePackage", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Exception e) {
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileActivatingPackage",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/deleteUserPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse deleteUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") int packageId,Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (clientId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				int userId = Integer.parseInt(clientId);
				message = packageService.deleteUserPackage(userId, packageId, clientAppDbJdbcTemplate);
				if (message.getCode() == "SUCCESS")
					message.setText(messageSource.getMessage("anvizent.message.success.text.packageDeletedSuccesfully",
							null, null));
				else
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDeletePackage",
							null, null));

				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToDeletePackage", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Exception e) {
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDeletePackage", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		return dataResponse;
	}

	@RequestMapping(value = "/renameUserPackage", method = RequestMethod.POST)
	public DataResponse renameUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestBody Package userPackage1) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (userPackage1 != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				int userId = Integer.parseInt(clientId);
				int packageId = userPackage1.getPackageId();
				String packageName = userPackage1.getPackageName();
				message = packageService.renameUserPackage(userId, packageId, packageName, clientAppDbJdbcTemplate);
				if (message.getCode() == "SUCCESS")
					message.setText(messageSource.getMessage("anvizent.message.success.text.packageRenamedSuccesfully",
							null, null));
				else
					message.setText(messageSource.getMessage("anvizent.message.validation.text.packageNameAlreadyExist",
							null, null));

				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToRenamePackage", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Exception e) {
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileRenamingPackage", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateDatabaseConnection", method = RequestMethod.POST)
	public DataResponse updateDatabaseConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnection ilConnection, Locale locale, HttpServletRequest request) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ilConnection.setClientId(clientId);
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			ilConnection.setModification(modification);

			if (ilConnection.getDataSourceName().equals("-1")) {
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
						ilConnection.getDataSourceNameOther());
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
				ilConnection.setDataSourceName(ilConnection.getDataSourceNameOther());
			}
			packageService.updateDatabaseConnection(ilConnection, clientAppDbJdbcTemplate);

			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.connectionUpdatedSuccessfully",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(
					messageSource.getMessage("anvizent.message.error.duplicateConnectionName.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.validation.text.connectionNameAlreadyExist",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingDatabase", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getScheduleStartTime", method = RequestMethod.POST)
	public DataResponse getScheduleStartTime(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam(value = "packageId") Integer packageId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ScheduleResult> scheduleResults = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			scheduleResults = packageService.getScheduleStartTime(packageId, clientAppDbJdbcTemplate);
			dataResponse.setObject(scheduleResults);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getScheduleDetails", method = RequestMethod.POST)
	public DataResponse getScheduleDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam(value = "packageId") Integer packageId,
			@RequestParam(value = "scheduleStartTime") String scheduleStartTime, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ScheduleResult> scheduleDetailsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			scheduleDetailsList = packageService.getScheduleDetails(packageId, scheduleStartTime,
					clientAppDbJdbcTemplate);
			if (scheduleDetailsList != null) {
				dataResponse.setObject(scheduleDetailsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource
						.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getScheduleDetails() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * 
	 * @param clientId
	 * @param connectionId
	 * @return
	 */

	@RequestMapping(value = "/getPackageExecution", method = RequestMethod.POST)
	public DataResponse getPackageExecution(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam(value = "packageId") Integer packageId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecution = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageExecution = packageService.getPackageExecution(packageId, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/getPackageExecutionByPagination", method = RequestMethod.POST)
	public DataResponse getPackageExecutionByPagination(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam(value = "packageId") Integer packageId,
			@RequestParam(value="offset") int offset,
			@RequestParam(value="limit") int limit,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecution = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageExecution = packageService.getPackageExecutionByPagination(packageId,offset,limit ,clientAppDbJdbcTemplate);
			dataResponse.setObject(packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/getPackageExecutionCount", method = RequestMethod.POST)
	public DataResponse getPackageExecutionCount(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam(value = "packageId") Integer packageId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int count = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			count = packageService.getPackageExecutionCount(packageId, clientAppDbJdbcTemplate);
			dataResponse.setObject(count);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAuditLogsForPackages", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getInProgressPackageExecutionList", method = RequestMethod.POST)
	public DataResponse getInProgressPackageExecutionList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request,
			@RequestParam(value = "packageId") Integer packageId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecution = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			packageExecution = packageService.getInProgressPackageExecutionList(packageId, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable ae) {
			packageService.logError(ae, request,clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
			messages.add(message);
			dataResponse.setMessages(messages);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/getCustomPackageExecutionByPaginationId", method = RequestMethod.POST)
	public DataResponse getCustomPackageExecutionByPaginationId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, 
			HttpServletRequest request, @RequestParam("packageId") int packageId, 
			@RequestParam(value = "offset") int offset,
			@RequestParam(value = "limit") int limit, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<PackageExecution> packageExecutionList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			if( packageId != 0 )
			{
				if( offset != 0 )
				{
					offset = offset * limit;
				}
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				packageExecutionList = packageService.getPackageExecutionByPagination(packageId,offset,limit ,clientAppDbJdbcTemplate);

				String timeZone = CommonUtils.getTimeZoneFromHeader(request);

				for (PackageExecution packageExecution : packageExecutionList)
				{
					packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
					packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));

					packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
					packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
					packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
					packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
				}
				dataResponse.setObject(packageExecutionList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getCustomPackageExecutionByPaginationId() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText("unable to retrieve pakage execution info.");
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
}
