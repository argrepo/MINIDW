/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.PreparedObjectProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.helper.MiniDwJobUtil;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.AIService;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.ScheduleService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.WSService;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.HistoricalLoadStatus;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
/**
 * @author rakesh.gajula
 * Modification date:- 26/11/2018
 * Modified By:- mahender.alaveni
 * Modification For :- Implemented Retry schedule
 */
@RestController("user_packageServiceDataRestController_2")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class PackageServiceDataRestController_2 {

	protected static final Log logger = LogFactory.getLog(PackageServiceDataRestController_2.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	FileService fileService;
	@Autowired
	ETLAdminService etlAdminService;
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private PackageDao packageDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;
	@Autowired
	PreparedObjectProcessor preparedObjectProcessor;
	@Autowired
	ExecutionProcessor executionprocessor;
	@Autowired
	private WSService wSService;
	@Autowired
	AIService aiService;
	private @Value("${rscript.installation.path:}") String rscriptPath;
	
	@RequestMapping(value = "/connectionTest", method = RequestMethod.POST)
	public DataResponse connectionTest(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnection iLconnection, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Connection con = null;
		if (iLconnection != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				dataResponse = new DataResponse();
				 
				Database database = packageService.getDbIdDriverNameAndProtocal(iLconnection.getDatabase().getId(),
						clientAppDbJdbcTemplate);
				if (database != null) {
					iLconnection.getDatabase().setConnector_id(database.getConnector_id());
					iLconnection.getDatabase().setDriverName(database.getDriverName());
					iLconnection.getDatabase().setProtocal(database.getProtocal());
					con = CommonUtils.connectDatabase(iLconnection);
				}
				if (con != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMadeTheConnection",
							new Object[] { iLconnection.getDatabase().getName() }, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.failedToConnect",
							new Object[] { iLconnection.getDatabase().getName() }, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				logger.error("error while connectionTest()");
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileTestingConnection",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						logger.error("", e);
					}
				}
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.setObject(message);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getCustomTempTables/{packageId}", method = RequestMethod.GET)
	public DataResponse getCustomTempTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			dataResponse = new DataResponse();
			List<Table> tables = fileService.getCustomTempTables(clientId, packageId, clientAppDbJdbcTemplate);
			dataResponse.setObject(tables);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Error while getting custom temp tables ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingTempTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/validateCustomTempTablesQuery/{industryId}/{packageId}", method = RequestMethod.POST)
	public DataResponse validateCustomTempTablesQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, @PathVariable("packageId") String packageId,
			HttpServletRequest request, @RequestParam("query") String query, @RequestParam("tables") String tables,
			@RequestParam("isstaging") Boolean isStaging, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = null;
		List<Message> messages = new ArrayList<Message>();
		message = new Message();
		try {
			isStaging = (isStaging == null || isStaging == true);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(isStaging);
			boolean valid = fileService.validateCustomTempTablesQuery(clientId, packageId, "0", query,
					clientJdbcTemplate);
			boolean isDDl = Integer.parseInt(packageId) == 0 ? true : false;
			List<String> columnNames = packageService.getColumnHeadersByQuery(query, clientJdbcTemplate, isDDl);

			Map<String, Object> map = new HashMap<>();
			map.put("isValid", valid);
			map.put("columnNames", columnNames);

			if (valid) {
				dataResponse.setObject(map);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (ResourceAccessException | HttpServerErrorException | DataAccessException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while validating the query ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/insertSSLCert", method = RequestMethod.POST)
	public DataResponse insertSSLCert(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request,
			@RequestParam("sslClientKeyFile") MultipartFile  sslClientKeyFile, 
			@RequestParam("sslClientCertFile") MultipartFile  sslClientCertFile, 
			@RequestParam("sslServerCaFile") MultipartFile  sslServerCaFile, 
			@RequestParam("sslEnable") Boolean  sslEnable, 
			@RequestParam("databaseId") int  databaseId, 
			@RequestParam("conId") int  conId,
			Locale locale) {
 
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<Message>();
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			 
			String dir = CommonUtils.getSslPath()+"/"+clientIdfromHeader+"/"+databaseId+"/"+conId+"/"; 
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			
			dir= (dir.indexOf('\\') != -1) ? dir.replaceAll("\\\\", "/") : dir;
			/*delete the files if existing */
			File file = new File(dir);      
			String[] myFiles;    
			if (file.isDirectory()) {
			    myFiles = file.list();
			    for (int i = 0; i < myFiles.length; i++) {
			        File myFile = new File(file, myFiles[i]); 
			        myFile.delete();
			    }
			}
			//key file
			String sslClientKeyFilePath = dir + sslClientKeyFile.getOriginalFilename();
			CommonUtils.createFile(dir, sslClientKeyFilePath, sslClientKeyFile);

			String sslClientCertFilePath = dir + sslClientCertFile.getOriginalFilename();
			CommonUtils.createFile(dir, sslClientCertFilePath, sslClientCertFile);

			String sslServerCaFilePath = dir + sslServerCaFile.getOriginalFilename();
			CommonUtils.createFile(dir, sslServerCaFilePath, sslServerCaFile);
 
			sslClientKeyFilePath= (sslClientKeyFilePath.indexOf('\\') != -1) ? sslClientKeyFilePath.replaceAll("\\\\", "/") : sslClientKeyFilePath;
			
			sslClientCertFilePath= (sslClientCertFilePath.indexOf('\\') != -1) ? sslClientCertFilePath.replaceAll("\\\\", "/") : sslClientCertFilePath;
			
			sslServerCaFilePath = (sslServerCaFilePath.indexOf('\\') != -1) ? sslServerCaFilePath.replaceAll("\\\\", "/") : sslServerCaFilePath;
			
			StringJoiner sslConnectionParams = new StringJoiner(",");
			sslConnectionParams.add(dir);
			sslConnectionParams.add(sslClientKeyFilePath);
			sslConnectionParams.add(sslClientCertFilePath);
			sslConnectionParams.add(sslServerCaFilePath);
			
			int count = fileService.updateSSLTrustKeyStorePathsAtDbConnection(databaseId, conId, sslConnectionParams.toString(),modification, clientAppDbJdbcTemplate);
			if( count !=  0){
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("connection creation or updation successfully with ssl certificates.");
			messages.add(message);
			dataResponse.setMessages(messages);
			}
		} catch (ResourceAccessException | HttpServerErrorException | DataAccessException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while validating the query ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/createCustomTargetTable", method = RequestMethod.POST)
	public ResponseEntity<Object> createCustomTargetTable(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if (clientData != null) {
				if (clientData.getUserPackage() != null) {
					String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
					clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

					clientData.setUserId(clientId);

					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId);
					clientData.setModification(modification);

					String tablename = clientData.getUserPackage().getTable().getTableName();

					boolean duplicateTable = false;

					duplicateTable = packageService.isTargetTableExist(clientId, tablename, clientAppDbJdbcTemplate);

					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

					if (!duplicateTable)
						duplicateTable = fileService.isTableExists(clientId, null, tablename, clientJdbcTemplate);

					if (!duplicateTable) {

						packageService.createTargetTable(clientData, clientJdbcTemplate);

						int targetTableId = packageService.saveTargetTableInfo(clientData, clientAppDbJdbcTemplate);
						dataResponse.setObject(new Integer(targetTableId));

						fileService.saveCustomTempTablesQuery(clientData, clientAppDbJdbcTemplate);

						// update package mapping status to true when target
						// table is created so that it will show up in
						// schedule table
						clientData.getUserPackage().setIsMapped(Boolean.TRUE);
						modification.setModifiedBy(clientId);
						modification.setModifiedTime(CommonDateHelper.formatDateAsString(new Date()));
						packageService.updatePackageMappingStatus(clientData, clientAppDbJdbcTemplate);

						dataResponse.setObject(clientData.getUserPackage().getTable().getTableName());
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.success.text.targetTableCreated",
								null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					} else {
						dataResponse.setObject(duplicateTable);
						message.setCode(messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code",
								null, null));
						message.setText(messageSource
								.getMessage("anvizent.message.validation.text.targetTableAlreadyExist", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}

				} else {
					message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
					message.setText(messageSource
							.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale)
							.replace("?", "package"));
					messages.add(message);
					dataResponse.setMessages(messages);
					return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
				}
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
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccurredWhileCreatingTable",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/createsCustomTargetTable", method = RequestMethod.POST)
	public DataResponse createsCustomTargetTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData clientData, Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if (clientData != null) {
				if (clientData.getUserPackage() != null) {
					String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
					clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

					clientData.setUserId(clientId);

					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId);
					clientData.setModification(modification);

					String tablename = clientData.getUserPackage().getTable().getTableName();
					boolean duplicateTable = false;
					duplicateTable = packageService.isTargetTableExist(clientId, tablename, clientAppDbJdbcTemplate);
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					
					if (!duplicateTable)
						duplicateTable = fileService.isTableExists(clientId, null, tablename, clientJdbcTemplate);

					if (!duplicateTable) {
						packageService.createTargetTable(clientData, clientJdbcTemplate);
						if (clientData.getUserPackage().getPackageId() != 0) {
							int targetTableId = packageService.saveTargetTableInfo(clientData, clientAppDbJdbcTemplate);
							dataResponse.setObject(new Integer(targetTableId));
							fileService.saveCustomTempTablesQuery(clientData, clientAppDbJdbcTemplate);
							// update package mapping status to true when target
							// table is created so that it will show up in
							// schedule table
							clientData.getUserPackage().setIsMapped(Boolean.TRUE);
							modification.setModifiedBy(clientId);
							modification.setModifiedTime(CommonDateHelper.formatDateAsString(new Date()));
							packageService.updatePackageMappingStatus(clientData, clientAppDbJdbcTemplate);
						} else {
							String tableStructure = CommonUtils.buildMySQLCreateTable(clientData);
							List<Column> tableColumns = packageService.getTableStructure(null, tablename, 0, clientId,clientJdbcTemplate);
							clientData.getUserPackage().getTable().setColumns(tableColumns);
							clientData.getUserPackage().getTable().setTableStructure(tableStructure);
							clientData.setUserId(clientId);
							packageService.saveDDlTableInfo(clientData, clientIdfromHeader, clientAppDbJdbcTemplate);
						}
						dataResponse.setObject(clientData.getUserPackage().getTable().getTableName());
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.success.text.targetTableCreated", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					} else {
						dataResponse.setObject(duplicateTable);
						message.setCode(messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null));
						message.setText(messageSource.getMessage("anvizent.message.validation.text.targetTableAlreadyExist", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}

				} else {
					message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
					message.setText(messageSource.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale).replace("?", "package"));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(
					messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null));
			message.setText(
					messageSource.getMessage("anvizent.message.validation.text.targetTableAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (ResourceAccessException | HttpServerErrorException | DataAccessException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
			messages.add(message);
			dataResponse.setMessages(messages);
			logger.error("", ae);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccurredWhileCreatingTable",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/mapFileWithIL", method = RequestMethod.POST)
	public DataResponse mapFileWithIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("industryId") Integer industryId,
			@RequestParam("packageId") Integer packageId, @RequestParam("dL_Id") Integer dL_Id,
			@RequestParam("iL_Id") Integer iL_Id, @RequestParam("flatFileType") String flatFileType,
			@RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColumnNames") String isFirstRowHasColumnNames,
			@RequestParam("file") MultipartFile file, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		List<String> originalFileheaders = null;
		List<Column> iLcolumnNames = null;
		Map<String, Object> mappingFilesHeaders = new HashMap<>();
		File tempFile = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if (file != null) {
				tempFile = CommonUtils.multipartToFile(file, Constants.Temp.getTempFileDir() + "fileMappingWithIL/");
				originalFileheaders = fileService.getHeadersFromFile(tempFile.getAbsolutePath(), flatFileType,
						delimeter, null);
				if (originalFileheaders.size() > 0) {

					if (originalFileheaders.size() == 1 && originalFileheaders.get(0).length() == 0) {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(
								messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
						if (tempFile != null) {
							tempFile.delete();
						}
						return dataResponse;
					}
					String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
					clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
					ILInfo iLInfo = packageService.getILById(iL_Id, clientId, clientAppDbJdbcTemplate);
					if (StringUtils.isNotBlank(iLInfo.getiL_table_name())) {

						clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
						iLcolumnNames = packageService.getTableStructure(null, iLInfo.getiL_table_name(), 0, clientId,
								clientJdbcTemplate);
					}
					mappingFilesHeaders.put("originalFileheaders", originalFileheaders);
					mappingFilesHeaders.put("iLcolumnNames", iLcolumnNames);
					mappingFilesHeaders.put("originalFileName", file.getOriginalFilename());
					dataResponse = new DataResponse();
					dataResponse.setObject(mappingFilesHeaders);

				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(
							messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					if (tempFile != null) {
						tempFile.delete();
					}
					return dataResponse;
				}

			}

		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorOccurredWhileMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getIlTableStructure/{iLId}", method = RequestMethod.GET)
	public DataResponse getIlTableStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @PathVariable("iLId") Integer iLId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		List<Column> iLcolumnNames = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILInfo iLInfo = packageService.getILById(iLId, clientId, clientAppDbJdbcTemplate);
			if (StringUtils.isNotBlank(iLInfo.getiL_table_name())) {

				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				iLcolumnNames = packageService.getTableStructure(null, iLInfo.getiL_table_name(), 0, clientId,
						clientJdbcTemplate);
				if (iLcolumnNames != null) {
					dataResponse.setObject(iLcolumnNames);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		} catch (AnvizentCorewsException e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/processMappingFileWithIL", method = RequestMethod.POST)
	public DataResponse processMappingFileWithIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("industryId") Integer industryId,
			@RequestParam("packageId") Integer packageId, @RequestParam("iLId") Integer iLId,
			@RequestParam("dLId") Integer dLId, @RequestParam("fileType") String fileType,
			@RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColoumnNames") Boolean isFirstRowHasColoumnNames,
			@RequestParam("originalFileName") String originalFileName,
			@RequestParam("iLColumnNames") String iLColumnNames,
			@RequestParam("selectedFileHeaders") String selectedFileHeaders,
			@RequestParam("dafaultValues") String dafaultValues, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		File tempFile = null;
		String sourceFilePath = null;
		String s3filePath = null;
		try {

			sourceFilePath = Constants.Temp.getTempFileDir() + "fileMappingWithIL/" + originalFileName;
			List<String> iLColumnNamesList = stringToList(iLColumnNames, ",");
			List<String> selectedFileHeadersList = stringToList(selectedFileHeaders, ",");
			List<String> dafaultValuesList = stringToList(dafaultValues, ",");
			String destFilePath = fileService.processFileMappingWithIL(sourceFilePath, fileType, delimeter, null,
					iLColumnNamesList, selectedFileHeadersList, dafaultValuesList);

			if (StringUtils.isNotBlank(destFilePath)) {
				tempFile = new File(destFilePath);
				// upload to S3
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
						clientAppDbJdbcTemplate);
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
				boolean isEncryptionRequired = fileSettings.getFileEncryption();
				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFile,
						clientId , packageId, clientId, 0, deploymentType, "", false,isEncryptionRequired);
				s3filePath = sourceFileInfo.getFilePath();

				dataResponse.setObject(s3filePath);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.sourceDetailsAreSaved", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.fileUploadingFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AmazonS3Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.fileSavingFailedErrorDetails", null, locale)+ae.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			if (tempFile != null) {
				tempFile.delete();
			}
			if (sourceFilePath != null) {
				File file = new File(sourceFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	public List<String> stringToList(String strArr, String splitChar) {
		List<String> list = new ArrayList<>();
		String[] arr = strArr.split(splitChar);
		for (String str : arr) {
			list.add(str);
		}
		return list;

	}

	/**
	 * 
	 * @param clientId
	 * @param industryId
	 * @return
	 */
	@RequestMapping(value = "/getDLById/{dLId}", method = RequestMethod.GET)
	public ResponseEntity<DLInfo> getDLById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dLId") Integer dLId, HttpServletRequest request) {

		DLInfo dlInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (dLId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				dlInfo = new DLInfo();
				dlInfo = packageService.getDLById(dLId, clientId, clientAppDbJdbcTemplate);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				throw new AnvizentRuntimeException(ae);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage("ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}

		return new ResponseEntity<DLInfo>(dlInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDLsById/{dLId}", method = RequestMethod.GET)
	public DataResponse getDLsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dLId") Integer dLId, HttpServletRequest request, Locale locale) {

		DLInfo dlInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (dLId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				dlInfo = new DLInfo();
				dlInfo = packageService.getDLById(dLId, clientId, clientAppDbJdbcTemplate);
				dataResponse.setObject(dlInfo);
				if (dlInfo != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(
							messageSource.getMessage("anvizent.message.error.text.unableToFindDlInfo", null, locale));
				}

			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDLId", null, locale));
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * 
	 * @param clientId
	 * @param iLId
	 * @return
	 */
	@RequestMapping(value = "/getILById/{iLId}", method = RequestMethod.GET)
	public ResponseEntity<ILInfo> getILById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("iLId") Integer iLId, HttpServletRequest request) {

		ILInfo iLInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (iLId != null) {
			try {
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				iLInfo = new ILInfo();
				iLInfo = packageService.getILById(iLId, clientId, clientAppDbJdbcTemplate);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				throw new AnvizentRuntimeException(ae);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage("ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}

		return new ResponseEntity<ILInfo>(iLInfo, HttpStatus.OK);
	}

	@RequestMapping(value = "/getTargetTableStructure/{packageId}/{industryId}", method = RequestMethod.GET)
	public ResponseEntity<Table> getTargetTablleStructure(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			HttpServletRequest request) {

		Table table = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, Integer.parseInt(packageId),
					clientAppDbJdbcTemplate);

			table = clientData.getUserPackage().getTable();
			if (table != null) {
				if (table.getTableName() != null) {

					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					Boolean isTableExist = fileService.isTableExists(clientId, null, table.getTableName(),
							clientJdbcTemplate);
					if (isTableExist) {
						List<Column> columns = packageService.getTableStructure(null, table.getTableName(),
								Integer.parseInt(industryId), clientId, clientJdbcTemplate);
						table.setColumns(columns);
					}
				}
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while reading table structure : ", e);
			throw new AnvizentRuntimeException(e);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return new ResponseEntity<Table>(table, HttpStatus.OK);

	}

	@RequestMapping(value = "/getTargetTablesStructure/{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse getTargetTablesStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			HttpServletRequest request, Locale locale) {

		Table table = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, Integer.parseInt(packageId),
					clientAppDbJdbcTemplate);

			table = clientData.getUserPackage().getTable();
			if (table != null) {
				if (table.getTableName() != null) {

					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					Boolean isTableExist = fileService.isTableExists(clientId, null, table.getTableName(),
							clientJdbcTemplate);
					if (isTableExist) {
						List<Column> columns = packageService.getTableStructure(null, table.getTableName(), 0, clientId,
								clientJdbcTemplate);
						table.setColumns(columns);
					}
					dataResponse.setObject(table);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while reading table structure : ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingTableStructure",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/saveCustomTargetDerivativeTable/{industryId}/{packageId}", method = RequestMethod.POST)
	public DataResponse saveCustomTargetDerivativeTable(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			HttpServletRequest request, @RequestParam("query") String query, @RequestParam("table") String tablename,
			@RequestParam("targetTable") String targetTable, @RequestParam("tableid") String tableid,
			@RequestParam("ccols") String ccols, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			boolean duplicateTable = false;

			duplicateTable = packageService.isTargetTableExist(clientId, targetTable, clientAppDbJdbcTemplate);

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			if (!duplicateTable)
				duplicateTable = fileService.isTableExists(clientId, null, targetTable, clientJdbcTemplate);

			if (!duplicateTable) {

				Table table = fileService.createCustomTargetDerivativeTable(clientId, packageId, "0", query, tablename,
						targetTable, ccols, null, clientJdbcTemplate);

				Package userPackage = new Package();

				ClientData clientData = new ClientData();
				clientData.setUserPackage(userPackage);

				userPackage.setTable(table);

				packageService.createTargetTable(clientData, clientJdbcTemplate);

				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);

				boolean status = fileService.saveCustomTargetDerivativeTable(clientId, packageId, "0", query,
						targetTable, tableid, ccols, null, modification, clientAppDbJdbcTemplate);

				dataResponse.setObject(status);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				dataResponse.setObject(false);
				message.setCode(
						messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null));
				message.setText(messageSource.getMessage("anvizent.message.validation.text.derivedTableAlreadyExist",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while reading table structure : ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/deleteConnectionMapping/{packageId}/{mappingId}", method = RequestMethod.POST)
	public DataResponse deleteConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("mappingId") String mappingId,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			Package userPackage = packageService.getPackageById(Integer.parseInt(packageId), clientId,
					clientAppDbJdbcTemplate);
			/*
			 * To delete targetTable mapping when adding a source after process
			 * of mapping
			 */
			if (!userPackage.getIsStandard()) {
				if (userPackage.getIsMapped()) {
					boolean deleteTargetAndDerivedTables = false;

					if (userPackage.getFilesHavingSameColumns()) {
						List<ClientData> mappingInfoList = packageService.getILConnectionMappingInfoByPackage(clientId,
								clientIdfromHeader, Integer.parseInt(packageId), clientAppDbJdbcTemplate);
						if (mappingInfoList.size() == 1) {
							deleteTargetAndDerivedTables = true;
						}
					} else {
						deleteTargetAndDerivedTables = false;
					}

					if (deleteTargetAndDerivedTables) {
						ClientData clientData = packageService.getTargetTableInfoByPackage(clientId,
								Integer.parseInt(packageId), clientAppDbJdbcTemplate);
						userPackage.setTable(clientData.getUserPackage().getTable());

						Table targetTable = clientData.getUserPackage().getTable();
						// get derived target table info
						List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
								userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
						userPackage.setDerivedTables(derivedTables);
						clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
						packageService.deleteCustomTablesBYPackageId(userPackage, clientId, clientJdbcTemplate,clientAppDbJdbcTemplate);
					}

				}
				  packageService.deleteCustomTempTableByMappingId(Integer.parseInt(packageId), mappingId, clientId, clientJdbcTemplate, clientAppDbJdbcTemplate);
				if (userPackage.getFilesHavingSameColumns()) 
				   packageService.updatePackageAdvancedField(Integer.parseInt(packageId), false, clientAppDbJdbcTemplate);
				else 
				   packageService.updatePackageAdvancedField(Integer.parseInt(packageId), true, clientAppDbJdbcTemplate);
			}
			
			//delete flat file from S3
			List<ClientData> mappingInfo = packageService.getILConnectionMappingInfoByPackage(clientId,
					clientIdfromHeader, Integer.parseInt(packageId), clientAppDbJdbcTemplate);
			// delete from database
			boolean status = packageService.deleteConnectionMapping(clientId, packageId, mappingId,
					clientAppDbJdbcTemplate);
			for (ClientData clientData : mappingInfo) {
				ILConnectionMapping ilConnectionMapping = clientData.getIlConnectionMapping();
				if (ilConnectionMapping.getSourceFileInfoId() != 0  && ilConnectionMapping.getIsFlatFile()) {
					int ilConnectionMappingId = ilConnectionMapping.getConnectionMappingId();
					int mappingId_delete = Integer.parseInt(mappingId);
					S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
							clientAppDbJdbcTemplate);

					if (ilConnectionMappingId == mappingId_delete) {
						String filePath = ilConnectionMapping.getFilePath();
						fileService.deleteFileFromS3(filePath, request, clientAppDbJdbcTemplate, s3BucketInfo);
					}
				}
			}
			
			updatePackageMappingStatus(clientIdfromHeader, clientId, Integer.parseInt(packageId), clientAppDbJdbcTemplate);
			dataResponse.setObject(status);
			if (status) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/showCustomPackageTablesStatus//{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse showCustomPackageTablesStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Map<String, Object> tablesData = packageService.showCustomPackageTablesStatus(clientId, packageId, "0",
					clientAppDbJdbcTemplate);
			dataResponse.setObject(tablesData);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("Error while reading table structure : ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileReadingTableStructure",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;

	}

	/**
	 * in case of files having same columns option 'Yes'
	 * 
	 * @param clientId
	 * @param packageId
	 * @return
	 */
	@RequestMapping(value = "/getColumnHeaders/{packageId}", method = RequestMethod.GET)
	public DataResponse getFileColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		FileInfo fileInfo = null;
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ClientData> clientDataList = packageService.getILConnectionMappingInfoByPackage(clientId,
					clientIdfromHeader, packageId, clientAppDbJdbcTemplate);
			for (ClientData clientData : clientDataList) {
				ILConnectionMapping ilConnectionMapping = clientData.getIlConnectionMapping();
				if (ilConnectionMapping.getIsFlatFile()) {
					List<FileInfo> fileList = fileService.getFileInfoByPackage(clientId, packageId,
							clientAppDbJdbcTemplate);
					fileList = fileList.stream().filter(f -> f.getSourceFileInfoId() != 0 &&  f.getSourceFileInfoId() == ilConnectionMapping.getSourceFileInfoId()).collect(Collectors.toList());
					if (fileList.size() > 0) {
						// as files having same columns
						fileInfo = fileList.get(0);
						String encryptedFileName = fileInfo.getFilePath();
						String filename = FilenameUtils.getName(encryptedFileName);
						try {
							filename = EncryptionServiceImpl.getInstance().decrypt(filename);
						} catch (Exception e) {
						}

						fileInfo.setFileName(filename);
						break;
					}
					
				}
				
			}
			dataResponse.setObject(fileInfo);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingheaders", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/deleteIlSource/{connectionMappingId}/{packageId}/{iLId}/{dLId}", method = RequestMethod.GET)
	public DataResponse deleteIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") String connectionMappingId,
			@PathVariable("packageId") String packageId, @PathVariable("iLId") String iLId,
			@PathVariable("dLId") String dLId, HttpServletRequest request, Locale locale) {
		int id = 0;
		int packID = Integer.parseInt(packageId);
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (connectionMappingId != null && connectionMappingId.matches("[0-9]+")) {
			id = Integer.parseInt(connectionMappingId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetID", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ClientData> mappingInfoList = packageService.getILConnectionMappingInfoByPackage(clientId,
					clientIdfromHeader, packID, clientAppDbJdbcTemplate,false);
			for (ClientData mappingInfo : mappingInfoList) {
				ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
				if (ilConnectionMapping.getConnectionMappingId() == id) {
					if (ilConnectionMapping.getIsFlatFile()) {
						S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
								clientAppDbJdbcTemplate);
						fileService.deleteFileFromS3(ilConnectionMapping.getFilePath(), request,
								clientAppDbJdbcTemplate, s3BucketInfo);
					}
				}
			}
			packageService.deleteIlSource(id, clientId, clientAppDbJdbcTemplate);
			updatePackageMappingStatus(clientIdfromHeader, clientId, packID, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource
					.getMessage("anvizent.message.success.text.connectionMappingIdDeletedSuccesfully", null, locale)
					.replace("?", connectionMappingId));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	
	
	
	@RequestMapping(value = "/updateIsActiveStatusIlSource/{connectionMappingId}/{isActiveRequired}", method = RequestMethod.GET)
	public DataResponse updateIsActiveStatusIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") String connectionMappingId,@PathVariable("isActiveRequired") Boolean isActiveRequired,
			 HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int updateActiveStatus = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			updateActiveStatus = packageService.updateIsActiveStatusIlSource(connectionMappingId, isActiveRequired, clientAppDbJdbcTemplate);
			if(updateActiveStatus != 0){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Updated successfully");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	

	public void updatePackageMappingStatus(String clientIdfromHeader, String clientId, int packageId, JdbcTemplate clientAppDbJdbcTemplate) {

		boolean isPackageMapped = Boolean.FALSE;
		Package userPackage = packageService.getPackageById(packageId, clientId, clientAppDbJdbcTemplate);
		List<ClientData> ilMappingList = packageService.getILConnectionMappingInfoByPackage(clientId,
				clientIdfromHeader, packageId, clientAppDbJdbcTemplate);
		if (ilMappingList.size() > 0) {
			isPackageMapped = Boolean.TRUE;
		} else {
			isPackageMapped = Boolean.FALSE;
		}
		ClientData clientData = new ClientData();
		userPackage.setIsMapped(isPackageMapped);
		clientData.setUserPackage(userPackage);
		clientData.setUserId(clientId);
		Modification modification = new Modification();
		modification.setModifiedBy(clientId);
		modification.setModifiedTime(CommonDateHelper.formatDateAsString(new Date()));
		clientData.setModification(modification);
		if (userPackage.getIsStandard()) {
			packageService.updatePackageMappingStatus(clientData, clientAppDbJdbcTemplate);
		}

		// if isMapped is false and if package is already scheduled, delete
		// schedule and user has to reschedule
		if (!isPackageMapped) {
			if (userPackage.getIsScheduled()) {
				userPackage.setIsScheduled(Boolean.FALSE);
				userPackage.setScheduleStatus(Constants.Status.STATUS_PENDING);
				packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);
				scheduleService.deleteSchedule(userPackage.getPackageId(), clientAppDbJdbcTemplate);
			}
		}

	}

	@RequestMapping(value = "/getCustomFileTempTableMappings/{packageId}", method = RequestMethod.GET)
	public List<Table> getCustomFileTempTableMappings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request) {

		List<Table> tables = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			tables = fileService.getCustomFileTempTableMappings(clientId, packageId, clientAppDbJdbcTemplate);

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getting custom temp table mappings ", e);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return tables;
	}

	@RequestMapping(value = "/getIlEpicorQuery/{iLId}/{connectionId}", method = RequestMethod.GET)
	public DataResponse getIlEpicorQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("iLId") String IlId, @PathVariable("connectionId") Integer connectionId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		ILConnection iLConnection = null;
		String ilQuery = null;
		if (connectionId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				int ILId = Integer.parseInt(IlId);
				if (connectionId != null) {
					iLConnection = packageService.getILConnectionById(connectionId, clientId, clientAppDbJdbcTemplate);
					ilQuery = packageService.getIlEpicorQuery(iLConnection.getDatabase().getId(), ILId,
							clientAppDbJdbcTemplate);
					dataResponse.setObject(ilQuery);
				}
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingEpicorQuery",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Exception e) {
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				logger.error("error while getIlEpicorQuery() ", e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(e.getMessage());
				messages.add(message);
				dataResponse.setMessages(messages);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		return dataResponse;
	}

	/**
	 * file uploading by windows app in case of run now
	 */
	@RequestMapping(value = "/fileUploadByClientsApp", method = RequestMethod.POST)
	public DataResponse fileUploadByClientsApp(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody SourceFileInfo sourceFileInfo, HttpServletRequest request,
			@RequestParam(value = "incrementalDateValue", required = false) String incrementalDateValue,
			Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		// File tempFile = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			if (StringUtils.isBlank(deploymentType)) {
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}
			String storageType = Constants.Config.STORAGE_TYPE_S3;
			if (StringUtils.isNotBlank(deploymentType)
					&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				storageType = Constants.Config.STORAGE_TYPE_LOCAL;
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			if (StringUtils.isBlank(sourceFileInfo.getFilePath())) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.fileSourcePathNotFound", null, locale));
				return dataResponse;
			}

			// fileService.saveUploadedFileInfo(sourceFileInfo);
			packageService.updateFilePathForDatabaseConnection(sourceFileInfo.getSourceFileId(), sourceFileInfo.getIlConnectionMappingId(),
					sourceFileInfo.getFilePath(), storageType, sourceFileInfo.getS3BucketInfo().getId(),
					sourceFileInfo.isMultiPartFile(), clientAppDbJdbcTemplate);

			if (StringUtils.isNotBlank(sourceFileInfo.getIncrementalDateValue())) {
				JdbcTemplate clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				scheduleService.saveOrUpdateIncrementalUpdate(sourceFileInfo.getIncrementalDateValue(),
						sourceFileInfo.getIlConnectionMappingId(), clientJdbcTemplate);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUploadingFile", null, locale));

		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			
			logger.error("error while fileUploadByClientApp() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUploadingFile", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * 
	 * soft deleting the package( i.e. updating isActive = 0 for package.)
	 * 
	 * 
	 */

	@RequestMapping(value = "/disablePackage", method = RequestMethod.POST)
	public DataResponse disablePackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("packageIds") List<Integer> packageIds,HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			/* soft deleting the package( i.e. updating isActive = 0 for  package. */
			for(Integer packageId : packageIds){
				  packageService.getPackageById(packageId, clientId, clientAppDbJdbcTemplate);
			}
			packageService.disablePackage(packageIds, clientAppDbJdbcTemplate);

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
							table.setNoOfRecordsFailed(clientData.getUserPackage().getTable().getNoOfRecordsFailed());
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
			message.setText(
					messageSource.getMessage("anvizent.message.success.text.packageDeletedSuccesfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileDisablePackage", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getJobResults/{packageId}", method = RequestMethod.GET)
	public DataResponse getJobResults(@PathVariable("packageId") String packageId,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResults(packageId, userId, "", clientJdbcTemplate);
			dataResponse.setObject(resultlist);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsByDate/{packageId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("packageId") String packageId, @PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsByDate(packageId, userId, null, fromDate, toDate,
					clientJdbcTemplate);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/deleteILConnection/{connectionId}", method = RequestMethod.POST)
	public DataResponse deleteILConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") String connectionId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		boolean status = false;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			int id = Integer.parseInt(connectionId);
			if (clientId != null) {
				int ilconnectionId = id;
				int userId = Integer.parseInt(clientId);
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				List<Package> packagesList = packageService.getPackagesByILConnectionId(ilconnectionId, userId,
						clientAppDbJdbcTemplate);
				status = packageService.deleteILConnection(id, clientId, clientAppDbJdbcTemplate);
				if (status) {
					packageService.deleteILConnectionMapping(id, clientAppDbJdbcTemplate);
					// check if any package having no source and update package
					// mapping status

					ClientData clientData = new ClientData();
					Modification modification = new Modification(new Date());
					modification.setModifiedBy(clientId);
					clientData.setModification(modification);
					for (Package userPackage : packagesList) {
						int packageId = userPackage.getPackageId();
						Integer sourceCount = packageService.getSourceCountByPackageId(packageId, userId,
								clientAppDbJdbcTemplate);
						updatePackageMappingStatus(clientIdfromHeader, clientId, packageId, clientAppDbJdbcTemplate);
						if (sourceCount == 0) {
							userPackage.setIsMapped(Boolean.FALSE);
							userPackage.setIsScheduled(Boolean.FALSE);
							userPackage.setScheduleStatus(Constants.Status.STATUS_PENDING);
							clientData.setUserPackage(userPackage);
							packageService.updatePackageMappingStatus(clientData, clientAppDbJdbcTemplate);
							packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);
							if (!userPackage.getIsStandard()) {
								// delete every god damn table in custom package
								ClientData clientData1 = packageService.getTargetTableInfoByPackage(clientId,
										userPackage.getPackageId(), clientAppDbJdbcTemplate);
								if (clientData1 != null) {
									userPackage.setTable(clientData1.getUserPackage().getTable());

									Table targetTable = clientData1.getUserPackage().getTable();
									// get derived target table info
									List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(
											clientId, userPackage.getPackageId(), targetTable.getTableId(),
											clientAppDbJdbcTemplate);
									userPackage.setDerivedTables(derivedTables);
									userPackage.setModification(modification);

									clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
									packageService.deleteCustomTablesBYPackageId(userPackage, clientId,
											clientJdbcTemplate,clientAppDbJdbcTemplate);
								}

							}
						} else {
							if (!userPackage.getIsStandard() && !userPackage.getFilesHavingSameColumns()) {
								// delete every god damn table in custom package
								ClientData clientData1 = packageService.getTargetTableInfoByPackage(clientId,
										userPackage.getPackageId(), clientAppDbJdbcTemplate);
								if (clientData1 != null) {
									userPackage.setTable(clientData1.getUserPackage().getTable());

									Table targetTable = clientData1.getUserPackage().getTable();
									// get derived target table info
									List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(
											clientId, userPackage.getPackageId(), targetTable.getTableId(),
											clientAppDbJdbcTemplate);
									userPackage.setDerivedTables(derivedTables);
									userPackage.setModification(modification);

									clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
									packageService.deleteCustomTablesBYPackageId(userPackage, clientId,
											clientJdbcTemplate,clientAppDbJdbcTemplate);
								}

							}
						}

					}

					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.ILConnectionDeleted", null,
							locale));
					messages.add(message);
					dataResponse.setMessages(messages);

				}
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorDeletingConnection", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorDeletingConnection", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/viewErrorLogs/{batchId}", method = RequestMethod.GET)
	public DataResponse viewErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("batchId") String batchId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if (StringUtils.isNotBlank(batchId) && batchId.startsWith("IL_Currency_Rate")) {
				resultlist = packageService.getJobErrorLogs(batchId, null, null);
			} else {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				resultlist = packageService.getJobErrorLogs(batchId, null, clientJdbcTemplate);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/deleteSameDatasetCustomTablesBYPackageId/{packageId}", method = RequestMethod.POST)
	public DataResponse deleteSameDatasetCustomTablesBYPackageId(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();

		int id = 0;
		Package userPackage = null;
		if (packageId != null && packageId.matches("[0-9]+")) {
			id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			userPackage = packageService.getPackageById(id, clientId, clientAppDbJdbcTemplate);
			if (userPackage != null) {
				if (!userPackage.getIsStandard() && userPackage.getIsMapped()) {

					// get main target table info
					ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, id,
							clientAppDbJdbcTemplate);
					if (clientData != null) {
						userPackage.setTable(clientData.getUserPackage().getTable());

						Table targetTable = clientData.getUserPackage().getTable();
						// get derived target table info
						List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
						userPackage.setDerivedTables(derivedTables);

						clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
						clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
						S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
						boolean status = packageService.deleteCustomTablesBYPackageId(userPackage, clientId,clientJdbcTemplate,clientAppDbJdbcTemplate);
						fileService.deleteCustomTempTables(packageId, clientId, clientAppDbJdbcTemplate,clientStagingJdbcTemplate,s3BucketInfo);
						if (status) {
							packageService.updatePackageAdvancedField(Integer.parseInt(packageId), false, clientAppDbJdbcTemplate);
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							message.setText(messageSource.getMessage(
									"anvizent.message.success.text.tableDeletedSuccessfully", null, locale));

						} else {
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage(
									"anvizent.message.error.text.unableToProcessDeleteRequest", null, locale));

						}

					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.error.text.targetTableDoesNotExist",
								null, locale));
					}

				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessDeleteRequest",
							null, locale));

				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.packageDetailsDoesNotExist", null,
						locale));
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileDeletingTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileDeletingTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientStagingJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/deleteSameDatasetCustomTablesBYTableId/{packageId}/{targetTableId}/{tableId}", method = RequestMethod.POST)
	public DataResponse deleteSameDatasetCustomTablesBYTableId(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("targetTableId") String targetTableId,
			@PathVariable("tableId") String tableId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();

		int id = 0;
		TableDerivative tableDerivative = null;
		Package userPackage = null;
		if (packageId != null && packageId.matches("[0-9]+") && targetTableId != null && targetTableId.matches("[0-9]+")
				&& tableId != null && tableId.matches("[0-9]+")) {
			id = Integer.parseInt(packageId);
		} else {
			return dataResponse;
		}
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			userPackage = packageService.getPackageById(id, clientId, clientAppDbJdbcTemplate);

			if (!userPackage.getIsStandard() && userPackage.getIsMapped()) {
				tableDerivative = fileService.getCustomTargetDerivativeTablesById(clientId, id,
						Integer.parseInt(targetTableId), Integer.parseInt(tableId), clientAppDbJdbcTemplate);
				if (tableDerivative != null) {
					// get main target table info

					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					boolean status = packageService.deleteCustomTablesBYTableId(tableDerivative, id,
							clientJdbcTemplate,clientAppDbJdbcTemplate);
					if (status) {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource
								.getMessage("anvizent.message.success.text.tableDeletedSuccessfully", null, locale));

					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource
								.getMessage("anvizent.message.error.text.unableToProcessDeleteRequest", null, locale));

					}

				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.targetTableDoesNotExist",
							null, locale));
				}

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessDeleteRequest",
						null, locale));
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileDeletingTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileDeletingTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/updateIlSource", method = RequestMethod.POST)
	public DataResponse updateIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping ilConnectionMapping, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = 0;
		try {
			if (ilConnectionMapping != null) {
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				ilConnectionMapping.setModification(modification);
				ilConnectionMapping
						.setiLquery(EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getiLquery()));
				if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()))
					ilConnectionMapping.setMaxDateQuery(
							EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getMaxDateQuery()));
				count = packageService.updateIlSource(ilConnectionMapping, clientAppDbJdbcTemplate);
				if (count > 0) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(
							messageSource.getMessage("anvizent.message.success.text.QueryIsUpdated", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					dataResponse.setObject(count);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(
							messageSource.getMessage("anvizent.message.error.text.FailedtoUpdateQuery", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingIlSource", null, locale) + "-" + ae.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingIlSource", null, locale) + "-" + e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getILConnectionMappingInfoByMappingId/{Package_id}/{mapping_Id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfoByMappingId(@PathVariable("mapping_Id") String mapping_Id,
			@PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			Locale locale) {
		ILConnectionMapping ilConnectionMapping = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int packageId = 0;
		if (Package_id.matches("[0-9]+")) {
			packageId = Integer.parseInt(Package_id);
		}

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilConnectionMapping = packageService.getILConnectionMappingInfoForPreview(Integer.parseInt(mapping_Id),
					packageId, clientId, clientAppDbJdbcTemplate);

			if (ilConnectionMapping != null) {
				ClientData clientData = new ClientData();
				clientData.setIlConnectionMapping(ilConnectionMapping);
				MinidwServiceUtil.getILConnectionMapping(clientData);
				dataResponse.setObject(ilConnectionMapping);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetIlConnectionDetails", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getILConnectionMappingInfoByMappingId()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingGetIlConnectionDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/getClientsDLs", method = RequestMethod.GET)
	public DataResponse getClientsDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {

		List<DLInfo> dlList = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dlList = packageService.getClientDLs(clientIdfromHeader, clientAppDbJdbcTemplate);
			if (dlList != null && dlList.size() > 0) {
				dataResponse.setObject(dlList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetDLClientDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getTargetTables/{industryId}", method = RequestMethod.GET)
	public DataResponse getTargetTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<String> targettableList = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			targettableList = packageService.getTargetTables(null, clientJdbcTemplate);

			if (targettableList.size() > 0) {
				dataResponse.setObject(targettableList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetTargetTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getTargetTables()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/derivedTableMappingInfo/{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse derivedtableMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			HttpServletRequest request, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<String> targettableList = null;
		int id = Integer.parseInt(packageId);
		int industryID = 0;
		List<Column> colmnsList = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			List<Table> tables = new ArrayList<Table>();
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			targettableList = packageService.derivedTableMappingInfo(id, clientId, clientAppDbJdbcTemplate);
			if (targettableList != null) {
				for (String tableName : targettableList) {
					Table table = new Table();
					table.setTableName(tableName.trim());

					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					colmnsList = packageService.getTableStructure(null, tableName.trim(), industryID, clientId,
							clientJdbcTemplate);
					if (colmnsList != null) {
						table.setColumns(colmnsList);
					}
					tables.add(table);
				}
				dataResponse.setObject(tables);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDerivedTableDetails",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToDerivedTableDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while derivedTableMappingInfo()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDerivedTableDetails",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateIsFromDerivedTables/{packageId}/{isFromDerivedTables}", method = RequestMethod.GET)
	public DataResponse updateIsFromDerivedTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @PathVariable("packageId") String packageId,
			@PathVariable("isFromDerivedTables") Boolean IsFromDerivedTables) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Integer id = null;
		if (StringUtils.isNumeric(packageId)) {
			id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetID", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int i = packageService.updatePackageIsClientDbTablesStatus(clientId, id, IsFromDerivedTables,
					clientAppDbJdbcTemplate);
			if (i > 0) {
				dataResponse = new DataResponse();
				dataResponse.setObject(new Integer(i));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while derivedTableMappingInfo()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/downloadIlTemplate/{il_id}/{templateType}", method = RequestMethod.GET)
	public DataResponse downloadIlTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response, @PathVariable("il_id") String il_id,
			@PathVariable("templateType") String templateType, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int ilid = Integer.parseInt(il_id);
		List<String> fileNames = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String dir = null;
		String filePath = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			fileNames = fileService.fileNameByIlId(ilid, clientAppDbJdbcTemplate);
			if (fileNames != null && fileNames.size() > 0) {
				for (String fileName : fileNames) {
					if (StringUtils.substringAfterLast(fileName, ".").equals(templateType)) {
						dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(fileName, "."));
						filePath = dir + "/" + fileName;
						CommonUtils.sendFIleToStream(filePath, request, response);

					}
				}

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noTemplateFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while downloadIlTemplate()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/checkFordownloadIlTemplate/{il_id}/{templateType}", method = RequestMethod.GET)
	public DataResponse checkFordownloadIlTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response, @PathVariable("il_id") String il_id,
			@PathVariable("templateType") String templateType, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int ilid = Integer.parseInt(il_id);
		List<String> fileNames = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			fileNames = fileService.fileNameByIlId(ilid, clientAppDbJdbcTemplate);
			if (fileNames != null && fileNames.size() > 0) {
				String fileName = fileNames.get(0);
				String dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(fileName, "."));

				String filePath = dir + "/" + fileName;
				File csvFile = new File(filePath);
				if (csvFile != null && csvFile.exists() && !csvFile.isDirectory()) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(
							messageSource.getMessage("anvizent.message.error.text.noTemplateFound", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noTemplateFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while downloadIlTemplate()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/downloadIlTemplatenew/{il_id}/{templateType}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadIlTemplateNew(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("il_id") String il_id,
			@PathVariable("templateType") String templateType, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int ilid = Integer.parseInt(il_id);
		List<String> fileNames = null;
		String dir = null;
		String filePath = null;
		byte[] file = null;
		HttpHeaders headers = new HttpHeaders();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			fileNames = fileService.fileNameByIlId(ilid, clientAppDbJdbcTemplate);
			if (fileNames != null && fileNames.size() > 0) {
				for (String fileName : fileNames) {
					if (StringUtils.substringAfterLast(fileName, ".").equals(templateType)) {
						dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(fileName, "."));
						filePath = dir + "/" + fileName;
						InputStream io = new FileInputStream(new File(filePath));
						headers.add("filename", fileName);
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						messages.add(message);

						file = IOUtils.toByteArray(io);
					}
				}

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noTemplateFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while downloadIlTemplate()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getSourceDetails/{client_Id}", method = RequestMethod.GET)
	public ResponseEntity<ClientData> getSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("client_Id") String clientId, HttpServletRequest request) {

		ClientData clientData = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (clientId != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientData = new ClientData();
				clientData = packageService.getClientSourceDetails(clientId, clientAppDbJdbcTemplate);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				throw new AnvizentRuntimeException(ae);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage("ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}

		return new ResponseEntity<ClientData>(clientData, HttpStatus.OK);
	}

	@RequestMapping(value = "/getSourcesDetails/{client_Id}", method = RequestMethod.GET)
	public DataResponse getSourcesDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("client_Id") String clientId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		ClientData clientData = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (clientId != null) {
			try {
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientData = packageService.getClientSourceDetails(clientId, clientAppDbJdbcTemplate);
				dataResponse.setObject(clientData);
				if (clientData != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource
							.getMessage("anvizent.message.error.text.errorWhileGettingSourceDetails", null, locale));
				}
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingSourceDetails",
						null, locale));
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getMappedModulesForStandardPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse getMappedModulesForStandardPackage(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ClientData> clientDataList = packageService.getILConnectionMappingInfoByPackage(userId,
					clientIdfromHeader, packageId, clientAppDbJdbcTemplate,false);
			
			if (clientDataList != null) {
				
				clientDataList.sort((s1, s2) -> s1.getIlConnectionMapping().getdLId() - s2.getIlConnectionMapping().getdLId());
				
				List<String> dlIds = new ArrayList<>();
				Map<Integer, DLInfo> dlInfoList = new HashMap<>();
				Map<Integer, ILInfo> ilInfoList = new HashMap<>();
				ListIterator<ClientData> clietDataIterator = (ListIterator<ClientData>) clientDataList.listIterator();
				while (clietDataIterator.hasNext()) {
					ClientData clientData = clietDataIterator.next();
					ILConnectionMapping ilconnectionmapping = clientData.getIlConnectionMapping();
					int dlId = ilconnectionmapping.getdLId();
					int ilid = ilconnectionmapping.getiLId();
					if (dlIds.contains(dlId+"-"+ilid)) {
						clietDataIterator.remove();
					} else {
						DLInfo dlInfo = dlInfoList.get(dlId);
						if (dlInfo == null) {
							dlInfo = packageService.getDLById(dlId, userId, clientAppDbJdbcTemplate);
							dlInfoList.put(dlId, dlInfo);
						}
						ILInfo ilInfo = ilInfoList.get(ilid);
						if (ilInfo == null) {
							ilInfo = packageService.getILById(ilid, userId, clientAppDbJdbcTemplate);
							ilInfoList.put(ilid, ilInfo);
						}
						clientData.setDlInfo(dlInfo);
						clientData.setIlInfo(ilInfo);
						dlIds.add(dlId + "-" + ilid);
					}

				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(clientDataList);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.NoModuleIsMapped", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(clientDataList);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetModules", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getMappedModulesForStandardPackage()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableTogetModules", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTargetTableQuery/{packageId}", method = RequestMethod.GET)
	public DataResponse getTargetTableQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String targetTableQuery = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			targetTableQuery = packageService.getTargetTableQuery(clientId, packageId, clientAppDbJdbcTemplate);
			if (targetTableQuery != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(targetTableQuery);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToGetTargetTable", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetTargetTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getTargetTableQuery()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingTargetTableDetails",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/defaultILIncrementalQuery/{il_id}/{connection_id}", method = RequestMethod.GET)
	public DataResponse getSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("il_id") Integer il_id, @PathVariable("connection_id") Integer connection_id,
			HttpServletRequest request, Locale locale) {
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (il_id != null && connection_id != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				ILConnection ilConnection = packageService.getILConnectionById(connection_id, userId,
						clientAppDbJdbcTemplate);
				int dataBaseTypeId = ilConnection.getDatabase().getId();
				String query = packageService.getILDefaultIncrementalQuery(il_id, dataBaseTypeId,
						clientAppDbJdbcTemplate);
				String maxDateQuery = etlAdminService.geMaxDateQueryById(il_id, dataBaseTypeId, clientAppDbJdbcTemplate);
				Map<String, Object> map = new HashMap<>();
				map.put("query", query);
				map.put("maxDateQuery", maxDateQuery);
				dataResponse.setObject(map);

				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToGetDefaultQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}

		return dataResponse;
	}

	/**
	 * to desktop app
	 */
	@RequestMapping(value = "/dateForIncrementalUpdateQuery", method = RequestMethod.GET)
	public DataResponse getDateForIncremenatalUpdateQuery(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("ilId") int ilId,
			@RequestParam("dataSourceId") String dataSourceId, @RequestParam("typeOfSource") String typeOfSource,@RequestParam("packageSourceMappingId") int packageSourceMappingId,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			String incrementalDate = packageService.getILIncrementalUpdateDate(null, ilId, dataSourceId, typeOfSource,packageSourceMappingId,
					clientJdbcTemplate);
			dataResponse.setObject(incrementalDate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetIncrementalUpdateDate",
					null, locale));

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while dateForIncrementalUpdateQuery()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetIncrementalUpdateDate",
					null, locale));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDatesForHistoricalLoadForPreview/{connectionMappingId}", method = RequestMethod.GET)
	public DataResponse getDatesForHistoricalLoadForPreview(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") int connectionMappingId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILConnectionMapping iLConnectionMapping = packageService.getDatesForHistoricalLoad(connectionMappingId,
					clientAppDbJdbcTemplate);
			if (iLConnectionMapping != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);

				Date startDate = sdf.parse(iLConnectionMapping.getHistoricalFromDate());
				Date endDate = sdf.parse(iLConnectionMapping.getHistoricalToDate());

				LinkedHashMap<String, String> map = new LinkedHashMap<>();
				map.put("startDate", sdf.format(startDate));
				map.put("endDate", sdf.format(endDate));
				dataResponse.setObject(map);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource
						.getMessage("anvizent.message.error.text.unableToGetDatesForHistoricalLoad", null, locale));
			}
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getDatesForHistoricalLoad()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingDatesForHistoricalLoad", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDatesForHistoricalLoad/{connectionMappingId}", method = RequestMethod.GET)
	public DataResponse getDatesForHistoricalLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") int connectionMappingId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILConnectionMapping iLConnectionMapping = packageService.getDatesForHistoricalLoad(connectionMappingId,
					clientAppDbJdbcTemplate);
			if (iLConnectionMapping != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);

				Date startDate = sdf.parse(iLConnectionMapping.getHistoricalFromDate());
				Date endDate = sdf.parse(iLConnectionMapping.getHistoricalToDate());

				Date fromDate = null;
				Date calculatedToDate = null;

				if (iLConnectionMapping.getHistoricalLastUpdatedTime() != null) {
					fromDate = sdf.parse(iLConnectionMapping.getHistoricalLastUpdatedTime());
					/* if the last updated date and end date matches */
					Calendar fromCalDate = Calendar.getInstance();
					fromCalDate.setTime(fromDate);
					fromCalDate.add(Calendar.DATE, 1);
					fromDate = fromCalDate.getTime();
				} else {
					fromDate = new Date(startDate.getTime());
				}

				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, (iLConnectionMapping.getLoadInterval() - 1));
				Date toDate = c.getTime();

				if (toDate.compareTo(endDate) < 0) {
					calculatedToDate = new Date(toDate.getTime());
				} else {
					calculatedToDate = new Date(endDate.getTime());
				}

				LinkedHashMap<String, String> map = new LinkedHashMap<>();
				map.put("startDate", sdf.format(fromDate));
				map.put("endDate", sdf.format(calculatedToDate));
				dataResponse.setObject(map);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource
						.getMessage("anvizent.message.error.text.unableToGetDatesForHistoricalLoad", null, locale));
			}
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getDatesForHistoricalLoad()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.errorWhileGettingDatesForHistoricalLoad", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/deleteCustomTargetTable/{packageId}", method = RequestMethod.GET)
	public DataResponse deleteCustomTargetTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Package userPackage = packageService.getPackageById(packageId, clientId, clientAppDbJdbcTemplate);
			if (userPackage.getIsMapped() && !userPackage.getFilesHavingSameColumns()) {
				ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, packageId,
						clientAppDbJdbcTemplate);
				userPackage.setTable(clientData.getUserPackage().getTable());

				Table targetTable = clientData.getUserPackage().getTable();
				// get derived target table info
				List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
						userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
				userPackage.setDerivedTables(derivedTables);

				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				packageService.deleteCustomTablesBYPackageId(userPackage, clientId, clientJdbcTemplate,clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDeletingCustomtargetTable",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/filterPackagesOnIsActive", method = RequestMethod.GET)
	public DataResponse filterPackagesOnIsActive(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Package> packageList = packageService.getAllUserPackages(clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/purgeUserTables", method = RequestMethod.POST)
	public DataResponse purgeUserTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody List<Table> table, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			boolean result = false;
			List<String> truncateSqlScript = new ArrayList<>();
			List<Integer> tableIds = new ArrayList<>();
			Set<String> accessibleTableName = getClientILsandDLsNames(clientIdfromHeader, clientAppDbJdbcTemplate, userId);
			if (table != null && table.size() > 0) {
				for (Table tbl : table) {
					String tableName = tbl.getTableName();
					try {
						tableName = EncryptionServiceImpl.getInstance().decrypt(tableName);
						if (!accessibleTableName.contains(tableName)) {
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
							message.setText(messageSource.getMessage("anvizent.message.validation.text.youDontHaveAccesstoPurgeTable",
									new Object[] { tableName }, locale));
							return dataResponse;
						}
					} catch (Exception e) {
						packageService.logError(e, request, clientAppDbJdbcTemplate);
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.invalidTableNameFound",
								null, locale));
						return dataResponse;
					}
					truncateSqlScript.add("TRUNCATE TABLE " + tableName);
					if (tableName.startsWith("IL_")) {
						tableIds.add(tbl.getTableId());
					}
				}

			}

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();

			if (tableIds != null && tableIds.size() > 0) {
				List<String> defaultInsertsForIL = packageService.getDefaultInsertsForIL(tableIds,
						clientDbDetails.get("clientdb_schema").toString(), clientAppDbJdbcTemplate);
				if (defaultInsertsForIL != null && defaultInsertsForIL.size() > 0) {
					truncateSqlScript.addAll(defaultInsertsForIL);
				}
			}
			logger.info("Client Id: "+ clientIdfromHeader +";User id:- " + userId + " purge tables list : " + truncateSqlScript);
			result = packageService.purgeUserTables(null, truncateSqlScript, clientJdbcTemplate);
			if (result) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource
						.getMessage("anvizent.message.success.text.purgeRequestProcessedSuccessfully", null, locale));

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.UnableToProcessPurgeRequest",
						null, locale));

			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileProcessingPurgeRequest",
					null, locale));
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileProcessingPurgeRequest",
					null, locale));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getConnectorId/{dataBaseTypeId}", method = RequestMethod.GET)
	public DataResponse getConnectotId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("dataBaseTypeId") Integer dataBaseTypeId, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Integer connectorId = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (dataBaseTypeId != null) {
				connectorId = packageService.getConnectorId(dataBaseTypeId, clientAppDbJdbcTemplate);
			}

			dataResponse.setObject(connectorId);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetConnectorId", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDbIdDriverNameAndProtocal/{dataBaseTypeId}", method = RequestMethod.GET)
	public DataResponse getDbIdDriverNameAndProtocal(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("dataBaseTypeId") Integer dataBaseTypeId, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Database database = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (dataBaseTypeId != null) {
				database = packageService.getDbIdDriverNameAndProtocal(dataBaseTypeId, clientAppDbJdbcTemplate);
			}
			dataResponse.setObject(database);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetConnectorId", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientILsandDLs", method = RequestMethod.GET)
	public DataResponse getClientILsandDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Table> tableList = new ArrayList<>();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			getClientILsandDLs(clientIdfromHeader, userId, tableList, clientAppDbJdbcTemplate);
			dataResponse.setObject(tableList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetClientIlsAndDls", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetClientIlsAndDls", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	public void getClientILsandDLs(String clientIdfromHeader, String userId, List<Table> tableList, JdbcTemplate clientAppDbJdbcTemplate) {
		List<String> iLTableNames = new ArrayList<>();
		// adding first DL table names
		List<DLInfo> clientDlList = packageService.getClientDLs(clientIdfromHeader, clientAppDbJdbcTemplate);
		for (DLInfo dLInfo : clientDlList) {
			Table Dltable = new Table();
			Dltable.setTableName(dLInfo.getdL_table_name());
			Dltable.setTableId(dLInfo.getdL_id());
			tableList.add(Dltable);
		}
		// IL tables follow DL tables
		for (DLInfo dLInfo : clientDlList) {

			List<ILInfo> ILList = userService.getAllILs(dLInfo.getdL_id(), clientAppDbJdbcTemplate);
			for (ILInfo iLInfo : ILList) {
				Table ilTable = new Table();
				ilTable.setTableName(iLInfo.getiL_table_name());
				ilTable.setTableId(iLInfo.getiL_id());
				if (!iLTableNames.contains(iLInfo.getiL_table_name())) {
					tableList.add(ilTable);
				}
				iLTableNames.add(iLInfo.getiL_table_name());
			}

		}
		tableList.forEach(table -> {
			try {
				table.setTableStructure(EncryptionServiceImpl.getInstance().encrypt(table.getTableName()));
			} catch (Exception e) {
				logger.error("", e);
			}
		});
	}

	public Set<String> getClientILsandDLsNames(String clientIdfromHeader, JdbcTemplate clientAppDbJdbcTemplate, String userId) {
		Set<String> iLTableNames = new HashSet<>();

		// adding first DL table names
		List<DLInfo> clientDlList = packageService.getClientDLs(clientIdfromHeader, clientAppDbJdbcTemplate);
		for (DLInfo dLInfo : clientDlList) {
			iLTableNames.add(dLInfo.getdL_table_name());
		}
		// IL tables follow DL tables
		for (DLInfo dLInfo : clientDlList) {
			List<ILInfo> ILList = userService.getAllILs(dLInfo.getdL_id(), clientAppDbJdbcTemplate);
			for (ILInfo iLInfo : ILList) {
				iLTableNames.add(iLInfo.getiL_table_name());
			}

		}
		return iLTableNames;
	}
	@RequestMapping(value = "/getWebServicesByClientId", method = RequestMethod.GET)
	public DataResponse getWebServicesByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<Integer, String> allWebservices = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (clientId != null)

				allWebservices = packageService.getWebServicesByClientId(clientIdfromHeader,
						clientAppDbJdbcTemplate);

			if (allWebservices != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allWebservices);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.webserviceNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingWebserviceDetails",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getDefaultILWebServiceMapping/{web_service_id}/{il_id}", method = RequestMethod.GET)
	public DataResponse getDefaultILWebServiceMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("web_service_id") String web_service_id, @PathVariable("il_id") String il_id,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		WebService webservice = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			if (web_service_id != null && il_id != null) {
				Integer webserviceId = Integer.parseInt(web_service_id);
				Integer ilId = Integer.parseInt(il_id);
				webservice = packageService.getDefaultILWebServiceMapping(webserviceId, ilId, clientAppDbJdbcTemplate);
			}

			if (webservice != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(webservice);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.noDefaultWebserviceAdded", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingWebserviceDetails",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getHeadersFromS3File/{packageId}/{ilConnectionMappingId}", method = RequestMethod.GET)
	public DataResponse getHeadersFromS3File(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") Integer packageId,
			@PathVariable("ilConnectionMappingId") Integer ilConnectionMappingId, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String filePath = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILConnectionMapping ilConnectionMapping = packageService.getILConnectionMappingInfoForPreview(
					ilConnectionMappingId, packageId, clientId, clientAppDbJdbcTemplate);
			if (ilConnectionMapping.getIsFlatFile() || ilConnectionMapping.getFilePath() != null) {
				String s3FilePath = ilConnectionMapping.getFilePath();
				S3BucketInfo s3BucketInfo = null;
				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				if(deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)){
					   s3BucketInfo = new S3BucketInfo();
					   s3BucketInfo.setId(0);
					   s3BucketInfo.setClientId(Integer.valueOf(clientIdfromHeader));
				   }else{
					   s3BucketInfo = userDao.getS3BucketInfoById(clientIdfromHeader,ilConnectionMapping.getS3BucketId(), null);
				   }
				//filePath = MinidwServiceUtil.downloadFileFromS3(s3FilePath, s3BucketInfo);
				
				List<String> files = MinidwServiceUtil.downloadFilesFromS3(s3FilePath, clientIdfromHeader, deploymentType, false, s3BucketInfo,ilConnectionMapping.isEncryptionRequired());
				filePath = files.get(0);
				String fileType = ilConnectionMapping.getFileType();
				String separatorChar = ilConnectionMapping.getDelimeter();
				List<String> fileHeaders = fileService.getHeadersFromFile(filePath, fileType, separatorChar, null);
				dataResponse.setObject(fileHeaders);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noHeadersFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToGetHeaderFromFile", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			if (filePath != null) {
				new File(filePath).delete();
			}
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getDbSchemaVaribles/{connectorId}", method = RequestMethod.GET)
	public DataResponse getDbSchemaVaribles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectorId") Integer connectorId, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<String, String> dbSchemaVaribles = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			if (clientId != null)

				dbSchemaVaribles = packageService.getDbSchemaVaribles(
						clientIdfromHeader, connectorId,
						commonJdbcTemplate);

			if (dbSchemaVaribles != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(dbSchemaVaribles);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dbAndSchemaVariablesAreNotFound",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.dbAndSchemaVariablesAreNotFound",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveMappingWithILForWebService", method = RequestMethod.POST)
	public DataResponse saveMappingWithILForWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("packageId") Integer packageId,
			@RequestParam("industryId") Integer industryId, @RequestParam("iLId") Integer iLId,
			@RequestParam("dLId") Integer dLId, @RequestParam("fileType") String fileType,
			@RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColoumnNames") Boolean isFirstRowHasColoumnNames,
			@RequestParam("originalFileName") String originalFileName,
			@RequestParam("iLColumnNames") String iLColumnNames,
			@RequestParam("selectedFileHeaders") String selectedFileHeaders,
			@RequestParam("dafaultValues") String dafaultValues, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<String> iLColumnNamesList = stringToList(iLColumnNames, ",");
			List<String> selectedFileHeadersList = stringToList(selectedFileHeaders, ",");
			List<String> dafaultValuesList = stringToList(dafaultValues, ",");
			StringBuilder iLMappingHeaders = new StringBuilder();
			for (int i = 0; i < iLColumnNamesList.size(); i++) {
				iLMappingHeaders.append(iLColumnNamesList.get(i));
				iLMappingHeaders.append("=");
				String selectedHeader = selectedFileHeadersList.get(i);
				if (StringUtils.isNotBlank(selectedHeader)) {
					iLMappingHeaders.append(selectedHeader);
				} else {
					String defaultValue = dafaultValuesList.get(i);
					if (StringUtils.isNotBlank(defaultValue)) {
						iLMappingHeaders.append("{");
						iLMappingHeaders.append(defaultValue);
						iLMappingHeaders.append("}");
					} else {
						iLMappingHeaders.append("{}");
					}

				}
				iLMappingHeaders.append("||");
			}
			String iLMappingHeaders1 = iLMappingHeaders.toString();
			if (iLMappingHeaders1 != null) {
				dataResponse.setObject(iLMappingHeaders1);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noHeadersFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateMappedHeadersForWebservice", method = RequestMethod.POST)
	public DataResponse updateMappedHeadersForWebservice(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping iLConnectionMapping, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (iLConnectionMapping != null) {
				Modification modification = new Modification(new Date());
				modification.setModifiedBy(clientIdfromHeader);
				iLConnectionMapping.setModification(modification);
				count = packageService.updateMappedHeadersForWebservice(clientId, iLConnectionMapping,
						clientAppDbJdbcTemplate);
			}

			if (count != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource
						.getMessage("anvizent.message.success.text.mappedHeadersSuccessfullyUpdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.mappedHeadersAreNotUpdated", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingWebservice", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getDerivedTableQuery", method = RequestMethod.POST)
	public DataResponse getDerivedTableQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("packageId") Integer packageId,
			@RequestParam("derived_table_name") String derived_table_name,
			@RequestParam("targetTableId") Integer targetTableId, @RequestParam("tableId") Integer tableId,
			Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String targetTableQuery = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			targetTableQuery = packageService.getDerivedTableQuery(clientId, packageId, derived_table_name,
					targetTableId, tableId, clientAppDbJdbcTemplate);
			if (targetTableQuery != null && !targetTableQuery.isEmpty()) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(targetTableQuery);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noQueryFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);

			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getDerivedTableQuery()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/defaultILHistoricalLoadQuery/{il_id}/{connection_id}", method = RequestMethod.GET)
	public DataResponse defaultILHistoricalLoadQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("il_id") Integer il_id, @PathVariable("connection_id") Integer connection_id,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (il_id != null && connection_id != null) {
			try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				ILConnection ilConnection = packageService.getILConnectionById(connection_id, userId,
						clientAppDbJdbcTemplate);
				int dataBaseTypeId = ilConnection.getDatabase().getId();
				String query = packageService.getILHistoricalLoadQuery(il_id, dataBaseTypeId, clientAppDbJdbcTemplate);
				Map<String, String> mapObj = new HashMap<>();
				mapObj.put("query", query);
				mapObj.put("connectorId", ilConnection.getDatabase().getConnector_id() + "");
				mapObj.put("protocal", ilConnection.getDatabase().getProtocal());
				dataResponse.setObject(mapObj);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (AnvizentRuntimeException ae) {
				packageService.logError(ae, request, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getAuthenticationTypes", method = RequestMethod.GET)
	public DataResponse getAuthenticationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<Integer, String> getAuthenticationTypes = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			getAuthenticationTypes = packageService.getAuthenticationTypes(clientAppDbJdbcTemplate);
			if (getAuthenticationTypes != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(getAuthenticationTypes);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.authenticationTypesAreNotAvailable",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingWebserviceDetails",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveHistoricalLoad", method = RequestMethod.POST)
	public DataResponse saveHistoricalLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody HistoricalLoadForm historicalLoadForm, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try {
			if (historicalLoadForm != null) {

				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				historicalLoadForm.setClientId(Integer.parseInt(clientIdfromHeader));

				if (historicalLoadForm.getDataSourceName().equals("-1")) {
					String dataFrom = "historyLoad";
					ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
							historicalLoadForm.getDataSourceNameOther());
					clientDataSource.setDataSourceFrom(Constants.SourceType.WEBSERVICE);
					clientDataSource.setDataSourceFrom(dataFrom);
					clientDataSource.setModification(modification);
					packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
					historicalLoadForm.setDataSourceName(historicalLoadForm.getDataSourceNameOther());
				}

				count = packageService.saveHistoricalLoad(clientId, historicalLoadForm, modification,
						clientAppDbJdbcTemplate);
			}

			if (count != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.historicalLoadSavedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.historicalLoadNotSavedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getHistoricalLoad", method = RequestMethod.GET)
	public DataResponse getHistoricalLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<HistoricalLoadForm> historicalLoadList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			historicalLoadList = packageService.getHistoricalLoad(clientIdfromHeader, clientAppDbJdbcTemplate);

			if (historicalLoadList != null) {
				message.setText(messageSource.getMessage("anvizent.message.no", null, locale));
				int length = historicalLoadList.size();
				for (int i = 0; i < length; i++) {
					if (historicalLoadList.get(i).isRunning()) {
						message.setText(messageSource.getMessage("anvizent.message.yes",null, locale));
						break;
					}
				}
				historicalLoadList.forEach(val -> {

				});
				dataResponse.setObject(historicalLoadList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveHistoricalLoadList",null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getHistoricalLoad() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getHistoricalLoadDetailsById", method = RequestMethod.POST)
	public DataResponse getHistoricalLoadDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("loadId") Integer loadId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		HistoricalLoadForm historicalLoadList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			historicalLoadList = packageService.getHistoricalLoadDetailsById(clientIdfromHeader, loadId,
					clientAppDbJdbcTemplate);
			if (historicalLoadList != null) {
				dataResponse.setObject(historicalLoadList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveHistoricalLoadDetails",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getHistoricalLoad() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
	
	

	@RequestMapping(value = "/cloneHistoricalLoadDetailsById", method = RequestMethod.POST)
	public DataResponse cloneHistoricalLoadDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("loadId") Integer loadId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			boolean cloneStatus = packageService.cloneHistoricalLoadDetailsById( loadId,
					clientAppDbJdbcTemplate);
			if (cloneStatus) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.successFullyCloned",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.cloningFailed",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getHistoricalLoad() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getHistoricalLoadDetailsByIdWithConnectionDetails", method = RequestMethod.POST)
	public DataResponse getHistoricalLoadDetailsByIdWithConnectionDetails(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("loadId") Integer loadId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		HistoricalLoadForm historicalLoadList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			historicalLoadList = packageService.getHistoricalLoadDetailsByIdWithConnectionDetails(clientIdfromHeader,
					loadId, clientAppDbJdbcTemplate);

			if (historicalLoadList != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);

				Date startDate = sdf.parse(historicalLoadList.getHistoricalFromDate());
				Date endDate = sdf.parse(historicalLoadList.getHistoricalToDate());

				Date fromDate = null;
				Date calculatedToDate = null;

				if (historicalLoadList.getHistoricalLastUpdatedTime() != null) {
					fromDate = sdf.parse(historicalLoadList.getHistoricalLastUpdatedTime());
					Calendar fromCalDate = Calendar.getInstance();
					fromCalDate.setTime(fromDate);
					fromCalDate.add(Calendar.DATE, 1);
					fromDate = fromCalDate.getTime();
				} else {
					fromDate = new Date(startDate.getTime());
				}

				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, (historicalLoadList.getLoadInterval() - 1));
				Date toDate = c.getTime();

				if (toDate.compareTo(endDate) < 0) {
					calculatedToDate = new Date(toDate.getTime());
				} else {
					calculatedToDate = new Date(endDate.getTime());
				}

				LinkedHashMap<String, String> map = new LinkedHashMap<>();
				map.put("startDate", sdf.format(fromDate));
				map.put("endDate", sdf.format(calculatedToDate));
				historicalLoadList.setStartDate(sdf.format(fromDate));
				historicalLoadList.setEndDate(sdf.format(calculatedToDate));
				dataResponse.setObject(historicalLoadList);

				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveHistoricalLoadDetails",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getHistoricalLoad() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateHistoricalLoad", method = RequestMethod.POST)
	public DataResponse updateHistoricalLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody HistoricalLoadForm historicalLoadForm, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try {
			if (historicalLoadForm != null) {

				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				historicalLoadForm.setClientId(Integer.parseInt(clientIdfromHeader));

				if (historicalLoadForm.getDataSourceName().equals("-1")) {
					String dataFrom = "historyLoad";
					ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
							historicalLoadForm.getDataSourceNameOther());
					clientDataSource.setDataSourceFrom(Constants.SourceType.WEBSERVICE);
					clientDataSource.setDataSourceFrom(dataFrom);
					clientDataSource.setModification(modification);
					packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
					historicalLoadForm.setDataSourceName(historicalLoadForm.getDataSourceNameOther());
				}

				count = packageService.updateHistoricalLoad(clientId, historicalLoadForm, modification,
						clientAppDbJdbcTemplate);
			}

			if (count != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.historicalLoadUpdatedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.historicalLoadNotUpdatedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			logger.error("", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/viewHistoricalLoadUploadStatus/{historicalLoadId}", method = RequestMethod.GET)
	public DataResponse viewHistoricalLoadUploadStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("historicalLoadId") Integer historicalLoadId, Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<HistoricalLoadStatus> historicalLoadStatus = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			historicalLoadStatus = packageService.getHistoricalLoadUploadStatus(clientIdfromHeader, historicalLoadId,
					clientAppDbJdbcTemplate);
			if (historicalLoadStatus != null) {
				dataResponse.setObject(historicalLoadStatus);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveHistoricalLoadUploadStatusDetails",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception ae) {
			logger.error("error while viewHistoricalLoadUploadStatus() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsForHistoricalLoad/{ilId}/{hId}", method = RequestMethod.GET)
	public DataResponse getJobResultsForHistoricalLoad(@PathVariable("ilId") Integer ilId,
			@PathVariable("hId") Long hId, @PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForHistoricalLoad(ilId, hId, userId, null,clientJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsForHistoricalLoadByDate/{ilId}/{hId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("ilId") Integer ilId, @PathVariable("hId") Long hId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForHistoricalLoadByDate(ilId, hId, userId, null,fromDate, toDate, clientJdbcTemplate);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/getCustomTempTablesForWebservice/{packageId}/{ilId}", method = RequestMethod.GET)
	public DataResponse getCustomTempTablesForWebservice(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("ilId") Integer ilId, HttpServletRequest request,
			Locale locale) {

		DataResponse dataResponse = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dataResponse = new DataResponse();
			List<Table> tables = fileService.getCustomTempTablesForWebservice(clientId, packageId, ilId,
					clientAppDbJdbcTemplate);
			dataResponse.setObject(tables);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Error while getting custom temp tables ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingTempTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param table
	 * @param locale
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getJoinWebServiceHeadersAndIlHeaders", method = RequestMethod.POST)
	public DataResponse getJoinWebServiceHeadersAndIlHeaders(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Table table, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Column> iLcolumnNames = null;
		Map<String, Object> mappingFilesHeaders = new HashMap<>();
		JdbcTemplate clientJdbcTemplate = null;
		List<WebServiceApi> webServiceApiList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			webServiceApiList = wSService.getWebServiceApiDetails(Long.valueOf(table.getWsconnId() == null ? 0 : table.getWsconnId()),
					              Long.valueOf(table.getIlId() == null ? 0 : table.getIlId()), clientIdfromHeader, clientAppDbJdbcTemplate);
			List<String> columnHeaders = new ArrayList<>();
			String tableName = table.getTableName();
			String originalFileName = table.getTableName();
			if( StringUtils.isBlank(table.getTableName()) )
			{
				ILInfo iLInfo = null;
				iLInfo = packageService.getILById(table.getIlId(), clientId, clientAppDbJdbcTemplate);
				tableName = iLInfo.getiL_table_name();
				originalFileName = iLInfo.getiL_name();
			}
			if( webServiceApiList.size() > 0 )
			{
				for (WebServiceApi webServiceApi : webServiceApiList)
				{
					JSONObject defaultMappingJSONObject = new JSONObject(webServiceApi.getDefaultMapping());
					if( defaultMappingJSONObject.length() > 0 )
					{
						iLcolumnNames = new ArrayList<Column>();
						JSONObject jsonObj = new JSONObject(webServiceApi.getDefaultMapping());
						Set<String> iLcolumnNamesFromMapping = (Set<String>) jsonObj.keySet();
						for (String key : iLcolumnNamesFromMapping)
						{
							Column column = new Column();
							column.setColumnName(key);
							column.setDataType("TEXT");
							if( columnHeaders.contains((String) jsonObj.get(key)) )
							{
								columnHeaders.add((String) jsonObj.get(key) + "_" + 1);
							}
							else
							{
								columnHeaders.add((String) jsonObj.get(key));
							}
							iLcolumnNames.add(column);
						}
					}
					else
					{
						if( StringUtils.isNotBlank(tableName) )
						{
							clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
							iLcolumnNames = packageService.getTableStructure(null, tableName, 0, clientId, clientJdbcTemplate);
							CommonUtils.closeDataSource(clientJdbcTemplate);
							clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
							columnHeaders = packageService.getColumnHeadersByQuery(table.getTableStructure(), clientJdbcTemplate, false);
						}

					}
				}
			}
			else
			{
				if( StringUtils.isNotBlank(tableName) )
				{
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					iLcolumnNames = packageService.getTableStructure(null, tableName, 0, clientId, clientJdbcTemplate);
					CommonUtils.closeDataSource(clientJdbcTemplate);
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
					columnHeaders = packageService.getColumnHeadersByQuery(table.getTableStructure(), clientJdbcTemplate, false);
				}
			}
			mappingFilesHeaders.put("originalFileheaders", columnHeaders);
			mappingFilesHeaders.put("iLcolumnNames", iLcolumnNames);
			mappingFilesHeaders.put("originalFileName", originalFileName);

			if( mappingFilesHeaders != null )
			{
				dataResponse.setObject(mappingFilesHeaders);
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Exception ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Error while getting custom temp tables ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingHeaders", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateTargetTableQuery", method = RequestMethod.POST)
	public DataResponse updateTargetTableQuery(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping iLConnectionMapping, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			packageService.updateTargetTableQuery(iLConnectionMapping.getiLquery(), clientId, iLConnectionMapping.getPackageId(), clientAppDbJdbcTemplate);
			packageService.updatePackageAdvancedField(iLConnectionMapping.getPackageId(), false, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Unable to update query", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText("Unable to update query");
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageId
	 * @param ilId
	 * @param il_connection_mapping_id
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getTempTablesAndWebServiceJoinUrls/{packageId}/{ilId}/{il_connection_mapping_id}", method = RequestMethod.GET)
	public DataResponse getTempTablesAndWebServiceJoinUrls(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("ilId") Integer ilId,
			@PathVariable("il_connection_mapping_id") Integer il_connection_mapping_id, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dataResponse = new DataResponse();
			List<Table> tables = packageService.getTempTablesAndWebServiceJoinUrls(clientId,
					Integer.parseInt(packageId), ilId, il_connection_mapping_id, clientAppDbJdbcTemplate);
			dataResponse.setObject(tables);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Error while getting custom temp tables ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingTempTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param webServiceId
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getWebServiceMasterById/{webServiceId}", method = RequestMethod.GET)
	public DataResponse getTempTablesAndWebServiceJoinUrls(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("webServiceId") Integer webServiceId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		WebService webService = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (webServiceId != null) {
				webService = packageService.getWebServiceMasterById(webServiceId, clientId, clientAppDbJdbcTemplate);
			}
			if (webService != null) {
				dataResponse.setObject(webService);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Error while getting web service master. ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingWebServiceMaster",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param userId
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/getDataSourceList", method = RequestMethod.GET)
	public DataResponse getDataSourceList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ClientDataSources> allDataSourceList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			allDataSourceList = packageService.getDataSourceList(clientIdfromHeader, clientAppDbJdbcTemplate);
			if (allDataSourceList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allDataSourceList);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.WebserviceNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while getDataSourceList() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWebservicesList",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param userId
	 * @param request
	 * @param mappingId
	 * @param dataSouceName
	 * @param dataSouceNameOther
	 * @param packageId
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/updateDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam("mappingId") Integer mappingId,
			@RequestParam("dataSouceName") String dataSouceName,
			@RequestParam("dataSouceNameOther") String dataSouceNameOther, @RequestParam("packageId") Integer packageId,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int update = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);

			if (dataSouceName.equals("-1")) {
				dataSouceName = dataSouceNameOther;
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
						dataSouceNameOther);
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
			}

			update = packageService.updateDataSourceDetails(mappingId, dataSouceName, dataSouceNameOther, packageId,
					clientAppDbJdbcTemplate);
			if (update != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while updateDataSourceDetails() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	

	/**
	 * @param userId
	 * @param request
	 * @param mappingId
	 * @param dataSouceName
	 * @param dataSouceNameOther
	 * @param packageId
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/updateWsDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateWsDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam("mappingId") Integer mappingId,
			@RequestParam("dataSouceName") String dataSouceName,
			@RequestParam("dataSouceNameOther") String dataSouceNameOther, 
			@RequestParam("packageId") Integer packageId,
			@RequestParam("wsApiUrl") String wsApiUrl,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int update = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);

			if (dataSouceName.equals("-1")) {
				dataSouceName = dataSouceNameOther;
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
						dataSouceNameOther);
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
			}

			update = packageService.updateWsDataSourceDetails(mappingId, dataSouceName, dataSouceNameOther, packageId,wsApiUrl,clientAppDbJdbcTemplate);
			if (update != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while updateDataSourceDetails() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	/**
	 * @param userId
	 * @param request
	 * @param mappingId
	 * @param dataSouceName
	 * @param dataSouceNameOther
	 * @param packageId
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/updateWsJoinDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateWsJoinDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, @RequestParam("mappingId") Integer mappingId,
			@RequestParam("dataSouceName") String dataSouceName,
			@RequestParam("dataSouceNameOther") String dataSouceNameOther, 
			@RequestParam("packageId") Integer packageId,
			@RequestParam("wsJoinUrls") String wsJoinUrls,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int update = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);

			if (dataSouceName.equals("-1")) {
				dataSouceName = dataSouceNameOther;
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
						dataSouceNameOther);
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
			}
			
			String  mappingIds  = packageService.getWsSourceMappingIdsByMappingId(mappingId,packageId,clientAppDbJdbcTemplate);
			
			String[] mappingIdsArray = mappingIds.split(",");
			
			int mappingIdsLength = mappingIdsArray.length;
			
			String[] wsJoinUrlsArray = wsJoinUrls.split(",");

			int wsJoinUrlsArrayLength = wsJoinUrlsArray.length;
			
			if(mappingIdsLength == wsJoinUrlsArrayLength )
			{
	           for(int i=0;i<mappingIdsLength;i++)
	           {
	        	   String url = wsJoinUrlsArray[i].trim();
	        	   int id = Integer.parseInt(mappingIdsArray[i]);
	        	   update = packageService.updateWsJoinDataSourceDetails(mappingId,id, dataSouceName, dataSouceNameOther, packageId,url,clientAppDbJdbcTemplate);
	           }
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			
			if (update != 0) {
				
				packageService.updateWsJoinUrlAtPackageSourceMapping(  mappingId,   packageId, wsJoinUrls.trim(), clientAppDbJdbcTemplate);
				
				packageService.updateDataSourceDetails(mappingId, dataSouceName, dataSouceNameOther, packageId, clientAppDbJdbcTemplate);
				
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while updateDataSourceDetails() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	/**
	 * @param clientId
	 * @param request
	 * @param response
	 * @param templateType
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/checkFordownloadIlCurrencyRateTemplate/{templateType}", method = RequestMethod.GET)
	public DataResponse checkFordownloadIlCurrencyRateTemplate(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("templateType") String templateType, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String fileName = Constants.ILCurrencyTemplate.IL_CURRENCY_RATE_TEMPLATE_FILE_NAME;
			String dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(fileName, "."));

			String filePath = dir + "/" + fileName;
			File csvFile = new File(filePath);
			if (csvFile != null && csvFile.exists() && !csvFile.isDirectory()) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noTemplateFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while checkFordownloadIlCurrencyRateTemplate()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param request
	 * @param response
	 * @param templateType
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/downloadIlCurrencyRateTemplate/{templateType}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadIlCurrencyRateTemplate(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("templateType") String templateType, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String dir = null;
		String filePath = null;
		byte[] file = null;
		HttpHeaders headers = new HttpHeaders();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String fileName = Constants.ILCurrencyTemplate.IL_CURRENCY_RATE_TEMPLATE_FILE_NAME;
			if (StringUtils.substringAfterLast(fileName, ".").equals(templateType)) {
				dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(fileName, "."));
				filePath = dir + "/" + fileName;
				InputStream io = new FileInputStream(new File(filePath));
				headers.add("filename", fileName);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				file = IOUtils.toByteArray(io);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while downloadIlTemplate()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileDownload", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
	}

	/**
	 * @param clientId
	 * @param sourceFileInfo
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/updateSourceFileInfo", method = RequestMethod.POST)
	public DataResponse updateSourceFileInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody SourceFileInfo sourceFileInfo, HttpServletRequest request, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			sourceFileInfo.setModification(modification);
			int sourceFileInfoId = packageService.updateSourceFileInfo(sourceFileInfo, clientAppDbJdbcTemplate);
			if (sourceFileInfoId != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updatedSourceFileInfoSuccessfully",
						null, locale));
				dataResponse.setObject(sourceFileInfoId);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingSourceFileInfo",
					null, locale));

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			
			logger.error("error while updateSourceFileInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingSourceFileInfo",
					null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param sourceFileInfo
	 * @param request
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/updatePackageExecutorSourceMappingInfo", method = RequestMethod.POST)
	public DataResponse updatePackageExecutorSourceMappingInfo(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody SourceFileInfo sourceFileInfo, HttpServletRequest request, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			sourceFileInfo.setModification(modification);
			int count = packageService.updatePackageExecutorSourceMappingInfo(sourceFileInfo, clientAppDbJdbcTemplate);
			if (count != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.text.updatedPackageExecutorSourceMappingInfoSuccessfully", null, locale));
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileUpdatingPackageExecutoSourceMappingInfo", null, locale));

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while updatePackageExecutorSourceMappingInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileUpdatingPackageExecutoSourceMappingInfo", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageExecution
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePackageExecutionUploadInfo", method = RequestMethod.POST)
	public DataResponse updatePackageExecutionUploadInfo(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updatePackageUploadInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			packageExecution.setModification(modification);
			updatePackageUploadInfo = scheduleService.updatePackageExecutionUploadInfo(packageExecution,
					clientAppDbJdbcTemplate);
			if (updatePackageUploadInfo != 0) {
				dataResponse.setObject(updatePackageUploadInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while updatePackageExecutionUploadInfo() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageExecution
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePackageExecutionStatus", method = RequestMethod.POST)
	public DataResponse updatePackageExecutionStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updatePackageExecutionInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			packageExecution.setModification(modification);
			updatePackageExecutionInfo = scheduleService.updatePackageExecutionStatus(packageExecution,
					clientAppDbJdbcTemplate);
			if (updatePackageExecutionInfo != 0) {
				dataResponse.setObject(updatePackageExecutionInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while updatePackageExecutionStatus() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageExecution
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePackageExecutionStatusInfo", method = RequestMethod.POST)
	public DataResponse updatePackageExecutionStatusInfo(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updatePackageExecutionInfo = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			packageExecution.setModification(modification);
			updatePackageExecutionInfo = scheduleService.updatePackageExecutionStatusInfo(packageExecution,
					clientAppDbJdbcTemplate);
			if (updatePackageExecutionInfo != 0) {
				dataResponse.setObject(updatePackageExecutionInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
			}

		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while updatePackageExecutionStatusInfo() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param executionId
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPackageExecutorSourceMappingInfoList", method = RequestMethod.POST)
	public DataResponse getPackageExecutorSourceMappingInfoList(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			packageExecutorMappingInfoList = packageService.getPackageExecutorSourceMappingInfoList(
					packageExecution.getExecutionId(), clientAppDbJdbcTemplate);
			if (packageExecutorMappingInfoList != null) {
				dataResponse.setObject(packageExecutorMappingInfoList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.unableToGetPackageExecutorMappingInfoList", null, locale));
			}
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getPackageExecutorSourceMappingInfoList() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource
					.getMessage("anvizent.message.error.text.unableToUpdatePackageExecutionUploadInfo", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param packageExecutorMappingInfo
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/runIL", method = RequestMethod.POST)
	public DataResponse runIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody PackageExecutorMappingInfo packageExecutorMappingInfo, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try { 
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			// creating folder when the folder does not exits.
			String jobFilesPath = tempPath + "JobFiles/";
			CommonUtils.createDir(jobFilesPath);

			Package userPackage = packageService.getPackageById((int) packageExecutorMappingInfo.getPackageExecution().getPackageId(), clientId, clientAppDbJdbcTemplate);
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			User user = new User();
			user.setUserId(clientId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			return executionprocessor.runIl(user,modification,packageExecutorMappingInfo.getPackageExecution().getTimeZone(),jobFilesPath, clientJdbcInstance
					      ,packageExecutorMappingInfo, deploymentType, userPackage);
			
		}  catch (Throwable ae) {
			packageService.logError(ae, request,clientAppDbJdbcTemplate);
			logger.error("error while getPackageExecutorSourceMappingInfo() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		
		return dataResponse;
	}
	@RequestMapping(value = "/runRBM", method = RequestMethod.POST)
	public DataResponse runRBM(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody BusinessModal businessModal, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try { 
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request); //"1010014";
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			// creating folder when the folder does not exits.
			String rContextParamsFilePath = tempPath + "RContextParamsFilePath/";
			CommonUtils.createDir(rContextParamsFilePath);

			
			businessModal = aiService.getBusinessModalInfoById(businessModal.getBmid(), clientAppDbJdbcTemplate);
			
			//need complete information
			
			User user = new User();
			user.setUserId(clientId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			return executionprocessor.runRJob(user,modification,rContextParamsFilePath, clientJdbcInstance,businessModal,rscriptPath);
			
		}  catch (Throwable ae) {
			packageService.logError(ae, request,clientAppDbJdbcTemplate);
			logger.error("error while runRBM() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		
		return dataResponse;
	}
	/**
	 * @param clientId
	 * @param dLId
	 * @param packageId
	 * @param executionId
	 * @param timeZone
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/runDL", method = RequestMethod.POST)
	public DataResponse runDL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("dLId") int dLId, @RequestParam("packageId") long packageId,
			@RequestParam("executionId") long executionId, @RequestParam("timeZone") String timeZone, 
			Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			// creating folder when the folder does not exits.
			String jobFilesPath = tempPath + "JobFiles/";
			CommonUtils.createDir(jobFilesPath);

			Package userPackage = packageService.getPackageById((int) packageId, clientId, clientAppDbJdbcTemplate);
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			User user = new User();
			user.setUserId(clientId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			return executionprocessor.runDl(user,modification,timeZone,jobFilesPath,clientJdbcInstance,executionId,deploymentType,userPackage,dLId);
	
		} catch (Throwable ae) {
			packageService.logError(ae, request,clientAppDbJdbcTemplate);
			dataResponse = new DataResponse();
			logger.error("error while getPackageExecutorSourceMappingInfo() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(executionMappingInfoComments.toString()+"\n"+ MinidwServiceUtil.getErrorMessageString(ae));
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param packageExecutorMappingInfo
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/runCustomPackage", method = RequestMethod.POST)
	public DataResponse runCustomPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecutorMappingInfo packageExecutorMappingInfo,
			Locale locale, HttpServletRequest request) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try{
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			PackageExecution  packageExecution = packageExecutorMappingInfo.getPackageExecution();
			String timeZone = packageExecutorMappingInfo.getPackageExecution().getTimeZone();
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			
			Package userPackage = packageService.getPackageById((int)packageExecution.getPackageId(), userId ,clientAppDbJdbcTemplate);
			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(userId);
			 
			return MiniDwJobUtil.runCp(userPackage,fileDao,scheduleDao,packageDao,user,clientJdbcInstance ,  deploymentType,  timeZone,  userDao,  packageExecutorMappingInfo);

		  }catch(Throwable e){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/executeCustomPackageTargetTable", method = RequestMethod.POST)
	public DataResponse executeCustomPackageTargetTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution,
			Locale locale, HttpServletRequest request) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try{
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String timeZone =  packageExecution.getTimeZone();
			 
			Package userPackage = packageService.getPackageById((int)packageExecution.getPackageId(), userId ,clientAppDbJdbcTemplate);
			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(userId);
			 
			return MiniDwJobUtil.executeCustomPackageTargetTable(clientJdbcInstance,userPackage,user,fileDao,
					  scheduleDao,timeZone,packageExecution.getExecutionId(),packageDao);
		 
		  }catch(Throwable e){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/executeCustomPackageDerivedTables", method = RequestMethod.POST)
	public DataResponse executeCustomPackageDerivedTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution,
			Locale locale, HttpServletRequest request) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		 
		try{ 
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String timeZone =  packageExecution.getTimeZone();
			
			Package userPackage = packageService.getPackageById((int)packageExecution.getPackageId(), userId ,clientAppDbJdbcTemplate);
			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(userId);
			  
			return  MiniDwJobUtil.executeCustomPackageDerivedTables(clientJdbcInstance,userPackage,user,fileDao,scheduleDao,timeZone,packageExecution.getExecutionId(),packageDao);
			 
		}catch(Throwable e){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
			}  finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/updateTargetTableInfo", method = RequestMethod.POST)
	public DataResponse updateTargetTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution,
			Locale locale, HttpServletRequest request) { 
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Modification modification=null;
		try{ 
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Package userPackage = packageService.getPackageById((int)packageExecution.getPackageId(), userId ,clientAppDbJdbcTemplate);
			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientIdfromHeader);
			user.setUserName(userId);
			  
		    modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);
			
			Map<String,Object> targetTableInfoStatus = packageExecution.getHavingSameColumsResultsMap();
			ClientData targetTableClientData = packageDao.getTargetTableInfoByPackage(user.getUserId(),userPackage.getPackageId(), clientAppDbJdbcTemplate);
			if(targetTableClientData != null){
				Table table = targetTableClientData.getUserPackage().getTable();
				table.setIsProcessed(Boolean.TRUE);
				table.setNoOfRecordsProcessed((int)targetTableInfoStatus.get("totalRecordsProcessed"));
				table.setNoOfRecordsFailed((int)targetTableInfoStatus.get("totalRecordsFailed"));
				table.setDuplicateRecords((int)targetTableInfoStatus.get("totalDuplicateRecordsFound"));
				table.setTotalRecords((int)targetTableInfoStatus.get("totalNoOfRecords")); 
				MiniDwJobUtil.updateTargetTableInfoStatus(table,packageDao,userPackage,user,modification,clientAppDbJdbcTemplate);
			}
			 
		}catch(Throwable e){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
			}  finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	/**
	 * @param userId
	 * @param packageId
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getILConnectionMappingDlInfoByPackageId", method = RequestMethod.POST)
	public DataResponse getILConnectionMappingDlInfoByPackageId(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		List<DLInfo> dLInfoList = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dLInfoList = packageService.getILConnectionMappingDlInfoByPackageId((int) packageExecution.getPackageId(),
					userId, clientAppDbJdbcTemplate);
			if (dLInfoList != null) {
				dataResponse.setObject(dLInfoList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.unableToGetDlInfoByPackageId", null, locale));
			}
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while getILConnectionMappingDlInfoByPackageId() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.unableToGetDlInfoByPackageId", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param packageExecution
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/runDDlayoutList", method = RequestMethod.POST)
	public DataResponse runDDlayoutList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				
				Modification modification = new Modification(new Date());
				modification.setModifiedBy(userId);
				modification.setCreatedBy(userId);
				
				User user = new User();
				user.setUserId(userId);
				user.setClientId(clientIdfromHeader);
				user.setUserName(userId);
				String runType = com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUNNOW;
				
				return MiniDwJobUtil.runCustomDataSets(packageDao,packageExecution.getDerivedTablesList(),user,clientJdbcTemplate,clientAppDbJdbcTemplate,fileDao,modification,runType);
			    
		}  catch (Throwable ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while runDDlayoutList() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}finally{
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param clientData
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePackageScheduleStatus", method = RequestMethod.POST)
	public DataResponse updatePackageScheduleStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody ClientData clientData, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			clientData.setModification(modification);
			clientData.setUserId(userId);
			packageService.updatePackageScheduleStatus(clientData, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.text.updatePackageScheduleStatusSuccessfully", null, locale));
		} catch (Exception ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while updatePackageScheduleStatus() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.unableToUpdatePackageScheduleStatusSuccessfully", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/**
	 * @param userId
	 * @param packageExecution
	 * @param locale
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdateIncrementalUpdate", method = RequestMethod.POST)
	public DataResponse updateIncrementalUpdate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody PackageExecution packageExecution, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				String schemaName = clientDbDetails.get("clientdb_schema").toString();
				if (packageExecution.getIncremtalUpdateList().size() > 0 && packageExecution.getIncremtalUpdateList() != null) {
					User user = new User();
					user.setUserId(userId);
					user.setClientId(clientIdfromHeader);
					user.setUserName(userId);
					 		
					MiniDwJobUtil.saveOrUpdateIncrementalUpdate(clientJdbcTemplate,schemaName,packageExecution.getIncremtalUpdateList(),scheduleDao,user);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.text.incrementalUpdateListSuccessfullyUpdated", null, locale));
				}else{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.text.notFoundIncrementalUpdateList", null, locale));
					}
			    
		} catch (Throwable e) {
			packageService.logError(e, null, clientAppDbJdbcTemplate);
			logger.error("error while saveOrUpdateIncrementalUpdate() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			
		}finally{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getFileSettingsInfo", method = RequestMethod.GET)
	public DataResponse getMaxFileSize(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
			if (fileSettings != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(fileSettings);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.maxFileSizeNotFound", null, locale));
			}

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while fileSettings() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToRetrieveMaxFileSize", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getS3BucketInfoByClientId", method = RequestMethod.GET)
	public DataResponse getS3BucketInfoByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		S3BucketInfo s3BucketInfo = null;
		try {
			
			s3BucketInfo = userService.getS3BucketInfoByClientId(clientId, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			if (s3BucketInfo == null) {
				throw new Exception("S3 Bucket details not mapped for this client");
			}
			dataResponse.setObject(s3BucketInfo);

		} catch (Throwable e) {
			packageService.logError(e, request, null);
			logger.error("error while getS3BucketInfoByClientId() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getS3BucketInfoFromClientId", method = RequestMethod.GET)
	public DataResponse getS3BucketInfoFromClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		S3BucketInfo s3BucketInfo = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			if (s3BucketInfo == null) {
				throw new Exception("S3 Bucket details not mapped for this client");
			}
			dataResponse.setObject(s3BucketInfo);

		} catch (Throwable e) {
			packageService.logError(e, request, null);
			logger.error("error while getS3BucketInfoByClientId() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/updateDatesForOnceRecurrence", method = RequestMethod.POST)
	public DataResponse updateDatesForOnceRecurrence(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Schedule scheduleOnce,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int updatedCount = packageService.updateDatesForOnceRecurrence(scheduleOnce, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(updatedCount);

		} catch (Throwable e) {
			packageService.logError(e, request, null);
			logger.error("error while getS3BucketInfoByClientId() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	

	@RequestMapping(value = "/viewFileErrorLogs/{batchId}", method = RequestMethod.GET)
	public DataResponse viewFileErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("batchId") String batchId, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String decryptedFilepath = null;
		try {
			String batchIdVal = batchId.replaceAll(":", "-")+".csv";
			File file =  new File(Constants.Config.ETL_JOBS_LOGS + File.separator + batchIdVal );
			if(file.exists()){
				decryptedFilepath =  file+"";
				if (StringUtils.isNotBlank(decryptedFilepath)) {
					List<List<String>> previewData = fileService.getCSVFileDataPreview(decryptedFilepath,",",100);
					dataResponse.setObject(previewData);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				}
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Error logs not found");
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request,null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToviewErrorLogs", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} 
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/runDDlayoutListCustomPackage", method = RequestMethod.POST)
	public DataResponse runDDlayoutListCustomPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam("runType") String runType,
			@RequestParam("tablesList") List<String> tableList, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		try {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				
				Modification modification = new Modification(new Date());
				modification.setModifiedBy(userId);
				modification.setCreatedBy(userId);
				
				User user = new User();
				user.setUserId(userId);
				user.setClientId(clientIdfromHeader);
				user.setUserName(userId);
				
				return executionprocessor.runCustomDataSetsInCustomPacakge(packageDao,tableList,user,clientJdbcTemplate,clientAppDbJdbcTemplate,fileDao,modification,runType);
			    
		}  catch (Throwable ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("error while runDDlayoutList() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}finally{
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/updatePackageName", method = RequestMethod.POST)
	public DataResponse updatePackageName(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request,
			@RequestParam("packageName") String packageName,
			@RequestParam("packageID") Integer packageID,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int update = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			
			update = packageService.updatePackageName(packageID,packageName,clientAppDbJdbcTemplate);

			if (update != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicatePackageName.code", null, null));
			message.setText(
					messageSource.getMessage("anvizent.message.validation.text.packageNameAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			logger.error("error while updateDataSourceDetails() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdating", null, locale) +" - " + MinidwServiceUtil.getErrorMessageString(e));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getUpdatedTargetMappedQuery", method = RequestMethod.POST)
	public DataResponse getUpdatedTargetMappedQuery(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,  @RequestParam("query") String query,
			 Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientJdbcTemplate = null;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			List<String> queries = stringToList(query, "&");
			queries = queries.stream().filter(q -> q.trim() != "undefined" && q.trim() != null).collect(Collectors.toList());
			packageService.getUpdatedTargetMappedQuery(queries,clientJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.targetTableUpdatedSuccessfully", null, locale));
		} catch (Exception ae) {
			packageService.logError(ae, request, clientJdbcTemplate);
			logger.error("Unable to update query", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/editTargetMappedQuery", method = RequestMethod.POST)
	public DataResponse editTargetMappedQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,  @RequestParam("query") String query,
			 Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		dataResponse.addMessage(message);
		JdbcTemplate clientJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(false);
			packageService.editTargetMappedQuery(query,clientJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.targetTableUpdatedSuccessfully", null, locale));
		} catch (Exception ae) {
			packageService.logError(ae, request, clientJdbcTemplate);
			logger.error("Unable to update query", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}	 

	@RequestMapping(value = "/updateRetryPaginationAtWsMapping", method = RequestMethod.POST)
	public DataResponse updatePackageExecutorSourceMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("id") int id, @RequestParam("retryPaginationData") String retryPaginationData, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			int updateCount = webServiceDao.updateRetryPaginationAtWsMapping(id, retryPaginationData, clientAppDbJdbcTemplate);
			if( updateCount != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updateRetryPagination", null, locale));
				dataResponse.setObject(updateCount);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			    message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoupdateRetryPagination", null, locale));
			}
		}
		catch ( Exception ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Unable to update Retry Pagination At Ws Mapping.", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/updateIncrementalDatePaginationAtWsMapping", method = RequestMethod.POST)
	public DataResponse updateIncrementalDatePaginationAtWsMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("id") int id, @RequestParam("incrementalPaginationData") String incrementalPaginationData, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			int updateCount = webServiceDao.updateIncrementalDatePaginationAtWsMapping(id, incrementalPaginationData, clientAppDbJdbcTemplate);
			if( updateCount != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updateIncrementalDatePagination", null, locale));
				dataResponse.setObject(updateCount);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoupdateIncrementalDatePagination", null, locale));
			}
		}
		catch ( Exception ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			logger.error("Unable to update incremental date Pagination At Ws Mapping.", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getSSLFilePathsInfo/{conId}", method = RequestMethod.GET)
	public DataResponse getSSLFilePathsInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("conId") Integer conId, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String sslFileNames = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if (conId != null) {
				sslFileNames = packageService.getSslFileNamesByConId(  conId,  clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updateIncrementalDatePagination", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(sslFileNames);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(ae.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
}