package com.datamodel.anvizent.data.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ELTCommandConstants;
import com.datamodel.anvizent.helper.ScriptRunner;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.WSService;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ELTClusterLogsInfo;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
@RestController("user_packageServiceDataRestController_1")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class PackageServiceDataRestController_1 {

	protected static final Log LOGGER = LogFactory.getLog(PackageServiceDataRestController_1.class);


	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private WSService wSService;
	
	@Autowired
	private ETLAdminService etlAdminService;
	
	@Autowired
	CommonProcessor commonProcessor;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	ClientDbCredentials clientDbCredentials;
	private @Value("${elt.logs:}") String eltLogsPath;
	/**
	 * 
	 * @param clientId
	 * @param clientInfo(packageName,isStandard,industry
	 *            of Package properties are required)
	 * @return
	 */

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getUserStandardPackages", method = RequestMethod.GET)
	public DataResponse getUserStandardPackages(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Package> packageList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			/*
			 * To Get standard packages, pass the 2nd parameter as true to below
			 * method
			 */
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageList = packageService.getUserPackages(clientId, true, clientAppDbJdbcTemplate);

			dataResponse.setObject(packageList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
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
	@RequestMapping(value = "/getUserCustomPackages", method = RequestMethod.GET)
	public DataResponse getUserCustomPackages(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Package> packageList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			/*
			 * To Get custom packages, pass the 2nd parameter as true to below
			 * method
			 */
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageList = packageService.getUserPackages(clientId, false, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getTablesStructure/{industryId}/{dLOrILName}", method = RequestMethod.GET)
	public DataResponse getTablesStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, @PathVariable("dLOrILName") String dLOrILName,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		int id = 0;
		List<Column> columnList = null;
		if (StringUtils.isNotBlank(dLOrILName)) {
			id = Integer.parseInt(industryId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.verticalOrTableNameMustNotBeEmpty",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}

		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Boolean isTableExist = fileService.isTableExists(clientId, null, dLOrILName, clientJdbcTemplate);
			if (isTableExist) {
				columnList = packageService.getTableStructure(null, dLOrILName, id, clientId, clientJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(columnList);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource
						.getMessage("anvizent.message.error.text.selectedTableDoesNotExistInDatabase", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentCorewsException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileConnectingToClientSchema", new Object[]{ae.getLocalizedMessage()}, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getAllUserPackages", method = RequestMethod.GET)
	public DataResponse getUserPackages(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Package> packageList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			packageList = packageService.getAllUserPackages(clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(packageList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getDatabasesTypes", method = RequestMethod.GET)
	public DataResponse getDatabaseTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		List<Database> databaseTypes = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			databaseTypes = packageService.getClientDatabaseTypes(clientIdfromHeader, clientAppDbJdbcTemplate);

			dataResponse.setObject(databaseTypes);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getILSWithStatus/{DL_Id}/{packageId}", method = RequestMethod.GET)
	public DataResponse getILsWithStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("DL_Id") String DL_Id, @PathVariable("packageId") String packageId,
			HttpServletRequest request, Locale locale) {

		List<ILInfo> ilList = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int id = 0;
		int package_Id = 0;
		if (DL_Id != null && DL_Id.matches("[0-9]+") && StringUtils.isNotBlank(packageId)
				&& StringUtils.isNumeric(packageId)) {
			id = Integer.parseInt(DL_Id);
			package_Id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.moduleOrPackageNameShouldNotBeEmpty",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilList = userService.getAllILs(id, clientAppDbJdbcTemplate);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);

			for (ILInfo iLInfo : ilList) {
				boolean isILMapped = packageService.isILMapped(userId, package_Id, iLInfo.getiL_id(),id,
						clientIdfromHeader, clientAppDbJdbcTemplate);
				if (isILMapped) {
					iLInfo.setiLStatus(Constants.Status.STATUS_DONE);
				} else {
					iLInfo.setiLStatus(Constants.Status.STATUS_PENDING);
				}

			}

			if (ilList != null && ilList.size() > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(ilList);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.iLDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveILInformation", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
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
	@RequestMapping(value = "/getUserILConnections", method = RequestMethod.GET)
	public DataResponse getUserILConnections(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		List<ILConnection> ilConnections = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ilConnections = packageService.getILConnections(clientIdfromHeader, clientId,
					clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
			dataResponse.setObject(ilConnections);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/createsPackage", method = RequestMethod.POST)
	public DataResponse createPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData clientData, Locale locale, HttpServletRequest request) {

		// TODO validations need to be done for required fields to create a
		// package

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if (clientData != null) {
				if (clientData.getUserPackage() != null && clientData.getUserPackage().getIndustry() != null
						&& !StringUtils.isBlank(clientData.getUserPackage().getPackageName()) 
						) {

					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
					clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

					clientData.setUserId(clientId);
					Package userPackage = clientData.getUserPackage();
					userPackage.setIsScheduled(Boolean.FALSE);
					userPackage.setIsMapped(Boolean.FALSE);
					userPackage.setScheduleStatus(Constants.Status.STATUS_PENDING);
					clientData.setUserPackage(userPackage);
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId);
					clientData.setModification(modification);
					int packageId = packageService.createPackage(clientData, clientAppDbJdbcTemplate);
					if (packageId != 0) {
						dataResponse.setObject(new Integer(packageId));
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource
								.getMessage("anvizent.message.success.text.packageCreatedSuccesfully", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreatePackage",
								null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}

				} else {
					message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
					message.setText(messageSource
							.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale)
							.replace("?", "packageName/industry/isStandard/trailingMonths"));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicatePackageName.code", null, null));
			message.setText(
					messageSource.getMessage("anvizent.message.validation.text.packageNameAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToCreatePackage", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllDLS/{industryId}", method = RequestMethod.GET)
	public DataResponse getAllDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, HttpServletRequest request, Locale locale) {

		List<DLInfo> dlList = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int id = 0;
		if (industryId != null && industryId.matches("[0-9]+")) {
			id = Integer.parseInt(industryId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetID", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dlList = userService.getAllDLs(id, clientAppDbJdbcTemplate);

			if (dlList != null && dlList.size() > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(dlList);
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
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getAllILS/{DL_Id}", method = RequestMethod.GET)
	public DataResponse getAllILS(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("DL_Id") String DL_Id, HttpServletRequest request, Locale locale) {

		List<ILInfo> ilList = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int id = 0;
		if (DL_Id != null && DL_Id.matches("[0-9]+")) {
			id = Integer.parseInt(DL_Id);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetID", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilList = userService.getAllILs(id, clientAppDbJdbcTemplate);

			if (ilList != null && ilList.size() > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(ilList);
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
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getPackagesById/{packageId}", method = RequestMethod.GET)
	public DataResponse getPackageById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request, Locale locale) {

		int id = 0;
		Package userPackage = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (packageId != null && packageId.matches("[0-9]+")) {
			id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.invalidPackageDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			userPackage = packageService.getPackageById(id, clientId, clientAppDbJdbcTemplate);
			if (userPackage != null) {
				if (!userPackage.getIsStandard()) {
					// get main target table info
					ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, id,
							clientAppDbJdbcTemplate);
					if (clientData != null) {
						userPackage.setTable(clientData.getUserPackage().getTable());

						Table targetTable = clientData.getUserPackage().getTable();
						// get derived target table info
						List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
								userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
						userPackage.setDerivedTables(derivedTables);
					}

				}
				dataResponse.setObject(userPackage);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.packageDetailsNotFound", null, locale));
			}
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/createsILConnection", method = RequestMethod.POST)
	public DataResponse createsILConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnection ilConnection, Locale locale, HttpServletRequest request) {

		// TODO validations need to be done for required fields to create a
		// package

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {

			boolean valid = true;

			if (valid) {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				ilConnection.setClientId(clientId);
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				ilConnection.setModification(modification);

				if (ilConnection.getDataSourceName().equals("-1")) {
					String dataFrom = "db creation";
					ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
							ilConnection.getDataSourceNameOther());
					clientDataSource.setDataSourceFrom(dataFrom);
					clientDataSource.setModification(modification);
					packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
					ilConnection.setDataSourceName(ilConnection.getDataSourceNameOther());
				}

				int conId = packageService.createILConnection(ilConnection, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.connectionCreatedSuccessfully",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(conId);
			} else {
				message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
				message.setText(messageSource
						.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale)
						.replace("?", "connectionName/username/password/server"));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.connectionNameAlreadyExist",
					null, locale));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToCreateConnection", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveILsConnectionMapping", method = RequestMethod.POST)
	public DataResponse saveILsConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping ilConnectionMapping, Locale locale, HttpServletRequest request) {

		// TODO validations need to be done for required fields

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		if (ilConnectionMapping != null) {
			if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsHavingParentTable()
					&& !ilConnectionMapping.getIsWebservice()) {
				if (StringUtils.isBlank(ilConnectionMapping.getTypeOfCommand())
						|| StringUtils.isBlank(ilConnectionMapping.getiLquery())
						|| (ilConnectionMapping.getIsIncrementalUpdate() != null
								&& ilConnectionMapping.getIsIncrementalUpdate()
								&& StringUtils.isBlank(ilConnectionMapping.getMaxDateQuery()))
						|| ilConnectionMapping.getPackageId() == null) {
					message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
					message.setText(messageSource
							.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale)
							.replace("?", "typeOfCommand/iL_query/iLId/dLId/packageId"));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}
			} else if (!ilConnectionMapping.getIsFlatFile() && ilConnectionMapping.getIsHavingParentTable()
					&& !ilConnectionMapping.getIsWebservice()) {

				if (!StringUtils.isNotBlank(ilConnectionMapping.getParent_table_name())
						|| ilConnectionMapping.getPackageId() == null) {
					message.setCode(messageSource.getMessage("anvizent.message.required.code", null, null));
					message.setText(messageSource
							.getMessage("anvizent.message.validation.text.propertyShouldNotbeEmptyOrNull", null, locale)
							.replace("?", "typeOfCommand/iL_query/iLId/dLId/packageId"));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}

			}
		} else {
			return dataResponse;
		}
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			Integer dLid = null;
			ilConnectionMapping.setClientId(clientId);
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			ilConnectionMapping.setModification(modification);
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Package userPackage = packageService.getPackageById(ilConnectionMapping.getPackageId(), clientId,
					clientAppDbJdbcTemplate);

			if (userPackage.getIsStandard()) {
				if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsWebservice()) {
					ilConnectionMapping
							.setiLquery(EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getiLquery()));
					if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()))
						ilConnectionMapping.setMaxDateQuery(
								EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getMaxDateQuery()));

					String procedureParameters = ilConnectionMapping.getProcedureParameters();
					if (StringUtils.isNotEmpty(procedureParameters))
						ilConnectionMapping.setProcedureParameters(EncryptionServiceImpl.getInstance()
								.encrypt(ilConnectionMapping.getProcedureParameters()));
				}

				dLid = ilConnectionMapping.getdLId();
				ILInfo iLInfo = packageService.getILById(ilConnectionMapping.getiLId(), clientId,
						clientAppDbJdbcTemplate);

				if (ilConnectionMapping.getIsWebservice()) {
					String joinUrls = new String();
					if (ilConnectionMapping.isJoinWebService()) {
						if(StringUtils.isBlank(ilConnectionMapping.getWebserviceJoinUrls()))
						{
						List<Table> tables = fileService.getCustomTempTablesForWebservice(clientId,
								String.valueOf(ilConnectionMapping.getPackageId()), ilConnectionMapping.getiLId(),
								clientAppDbJdbcTemplate);
						for (Table table : tables) {
							joinUrls += table.getWebServiceJoin().getWebServiceUrl() + ",";
						}
						String modifiedUrls = joinUrls.substring(0, joinUrls.length() - 1);
						ilConnectionMapping.setWebserviceJoinUrls(modifiedUrls);
					    }
					 }
					String dataSource = wSService
							.getWebServiceConnectionDetails(Long.parseLong(ilConnectionMapping.getWsConId() + ""),
									clientIdfromHeader, clientAppDbJdbcTemplate)
							.getDataSourceName();
					ilConnectionMapping.setIlSourceName(dataSource);
				}
				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				String storageType = Constants.Config.STORAGE_TYPE_S3;

				
				if (StringUtils.isNotBlank(deploymentType) && deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
					storageType = Constants.Config.STORAGE_TYPE_LOCAL;
				}
				ilConnectionMapping.setStorageType(storageType);

				if (StringUtils.isNotBlank(ilConnectionMapping.getIlSourceName())
						&& ilConnectionMapping.getIlSourceName().equals("-1")) {
					ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader),
							ilConnectionMapping.getDataSourceNameOther());

					if (ilConnectionMapping.getIsWebservice()) {
						clientDataSource.setDataSourceFrom(Constants.SourceType.WEBSERVICE);
					} else if (ilConnectionMapping.getIsFlatFile()) {
						clientDataSource.setDataSourceFrom(Constants.SourceType.FLATFILE);
					} else if (!ilConnectionMapping.getIsFlatFile()) {
						clientDataSource.setDataSourceFrom(Constants.SourceType.DATABASE);
					} else {
						clientDataSource.setDataSourceFrom("Unknown");
					}

					clientDataSource.setModification(modification);
					packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
					ilConnectionMapping.setIlSourceName(ilConnectionMapping.getDataSourceNameOther());
				}

				int connectionMappingId = packageService.saveILConnectionMapping(ilConnectionMapping,
						clientAppDbJdbcTemplate);
				// update package mapping IL mapping status if all ILs are
				// mapped
				if (ilConnectionMapping.getIsWebservice()) {
					if (ilConnectionMapping.isJoinWebService()) {
						packageService.updateIlConnectionWebServiceMapping(connectionMappingId,
								ilConnectionMapping.getiLId(), clientId, ilConnectionMapping.getPackageId(),
								modification, ilConnectionMapping.getProcedureParameters(), clientAppDbJdbcTemplate);
					} else {
						packageService.updateWsApiIlConnectionWebServiceMapping(ilConnectionMapping.getWebService(),
								connectionMappingId, modification, clientId, clientAppDbJdbcTemplate);
					}
				}

				// update package mapping IL mapping status if all ILs are
				// mapped
				updatePackageStatus(clientId, ilConnectionMapping.getPackageId().intValue(), dLid, modification,
						clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.iLisMapped", null, locale)
						.replace("?", iLInfo.getiL_name()));
			} else {
				if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsHavingParentTable()) {
					/*
					 * To delete targetTable mapping when adding a source after
					 * process of mapping
					 */
					/*if (!userPackage.getIsStandard() && userPackage.getIsMapped()) {
						clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

							if (userPackage.getIsMapped() && !userPackage.getFilesHavingSameColumns()) {
								ClientData clientData = packageService.getTargetTableInfoByPackage(clientId,
										ilConnectionMapping.getPackageId(), clientAppDbJdbcTemplate);
								userPackage.setTable(clientData.getUserPackage().getTable());

								Table targetTable = clientData.getUserPackage().getTable();
								// get derived target table info
								List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(
										clientId, userPackage.getPackageId(), targetTable.getTableId(),
										clientAppDbJdbcTemplate);
								userPackage.setDerivedTables(derivedTables);

								packageService.deleteCustomTablesBYPackageId(userPackage, clientId, clientJdbcTemplate,clientAppDbJdbcTemplate);
							}


					}*/

					ilConnectionMapping
							.setiLquery(EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getiLquery()));
					if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()))
						ilConnectionMapping.setMaxDateQuery(
								EncryptionServiceImpl.getInstance().encrypt(ilConnectionMapping.getMaxDateQuery()));
				}

				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				String storageType = Constants.Config.STORAGE_TYPE_S3;
				if (StringUtils.isNotBlank(deploymentType)
						&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
					storageType = Constants.Config.STORAGE_TYPE_LOCAL;
				}
				ilConnectionMapping.setStorageType(storageType);

				if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsHavingParentTable()) {
					packageService.saveILConnectionMapping(ilConnectionMapping, clientAppDbJdbcTemplate);
				}

				if (!ilConnectionMapping.getIsFlatFile() && ilConnectionMapping.getIsHavingParentTable()) {
					packageService.saveDerivedTableMappingInfo(ilConnectionMapping, clientAppDbJdbcTemplate);
				}
				if (ilConnectionMapping.getIsFlatFile()) {
					packageService.saveILConnectionMapping(ilConnectionMapping, clientAppDbJdbcTemplate);
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.sourceDetailsAreSaved", null, locale));
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.unableToCreateConnection", null, locale));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileIlSourceMapping", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public void updatePackageStatus(String clientId, int packageId, Integer dLId, Modification modification,
			JdbcTemplate clientAppDbJdbcTemplate) {

		ClientData clientData = new ClientData();
		clientData.setUserId(clientId);
		modification.setModifiedBy(modification.getCreatedBy());
		modification.setModifiedTime(modification.getCreatedTime());
		clientData.setModification(modification);
		Package userPackage = packageService.getPackageById(packageId, clientId, clientAppDbJdbcTemplate);
		if (userPackage.getIsStandard()) {
			userPackage.setIsMapped(Boolean.TRUE);
			clientData.setUserPackage(userPackage);
			packageService.updatePackageMappingStatus(clientData, clientAppDbJdbcTemplate);
		}
	}

	@RequestMapping(value = "/getILsConnectionById/{connectionId}", method = RequestMethod.GET)
	public DataResponse getILsConnectionById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") String connectionId, HttpServletRequest request, Locale locale) {

		int id = 0;
		ILConnection iLConnection = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (StringUtils.isNotBlank(connectionId) && connectionId.matches("[0-9]+")) {
			id = Integer.parseInt(connectionId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			iLConnection = packageService.getILConnectionById(id, clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(iLConnection);
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

		return dataResponse;
	}

	@RequestMapping(value = "/getILsConnectionByIdWithSchemas/{connectionId}", method = RequestMethod.GET)
	public DataResponse getILConnectionByIdWithSchemas(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") String connectionId, HttpServletRequest request, Locale locale) {

		int id = 0;
		ILConnection iLConnection = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Database database = null;
		if (StringUtils.isNotBlank(connectionId) && connectionId.matches("[0-9]+")) {
			id = Integer.parseInt(connectionId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			iLConnection = packageService.getILConnectionById(id, clientId, clientAppDbJdbcTemplate);
			List<String> schemaList = null;
			database = iLConnection.getDatabase();
			database.setSchemas(schemaList);
			iLConnection.setDatabase(database);
			dataResponse.setObject(iLConnection);
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

		return dataResponse;
	}

	@RequestMapping(value = "/getPackagesByIdWithOutStatusFlag/{packageId}", method = RequestMethod.GET)
	public DataResponse getPackageByIdWithOutStatusFlag(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request, Locale locale) {

		int id = 0;
		Package userPackage = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

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
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			userPackage = packageService.getPackageByIdWithOutStatusFlag(id, clientId, clientAppDbJdbcTemplate);
			if (userPackage != null) {
				dataResponse.setObject(userPackage);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
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
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getILsConnectionMappingInfo/{Il_Id}/{DL_Id}/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Il_Id") String Il_Id,
			@PathVariable("DL_Id") String DL_Id, @PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {

		List<ILConnectionMapping> ilConnectionMappingList = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		int ilId = 0;
		int packageId = 0;
		int dlId = 0;

		if (Il_Id != null && Il_Id.matches("[0-9]+") && DL_Id != null && DL_Id.matches("[0-9]+") && Package_id != null
				&& Package_id.matches("[0-9]+")) {
			ilId = Integer.parseInt(Il_Id);
			dlId = Integer.parseInt(DL_Id);
			packageId = Integer.parseInt(Package_id);
			
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<ClientData> mappingInfoList = packageService.getILConnectionMappingInfoByPackage(userId,
					clientIdfromHeader, packageId, clientAppDbJdbcTemplate,false);

			for (ClientData mappingInfo : mappingInfoList) {
				
				ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
				
				if (ilConnectionMapping.getiLId() == ilId && ilConnectionMapping.getdLId() == dlId) {
					ilConnectionMappingList.add(MinidwServiceUtil.getILConnectionMapping(mappingInfo));
				}

			}
			dataResponse.setObject(ilConnectionMappingList);
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
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/getILsConnectionMappingInfoByPackage/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		int packageId = 0;

		if (StringUtils.isNotBlank(Package_id)) {
			packageId = Integer.parseInt(Package_id);

		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<ClientData> mappingList = packageService.getILConnectionMappingInfoByPackage(userId,
					clientIdfromHeader, packageId, clientAppDbJdbcTemplate);
			mappingList.sort((s1,s2) -> s1.getIlConnectionMapping().getConnectionMappingId() - s2.getIlConnectionMapping().getConnectionMappingId() );
			for (ClientData clientData : mappingList) {
				ILConnectionMapping ilConnectionMapping = clientData.getIlConnectionMapping();
				if (ilConnectionMapping != null) {
					if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsHavingParentTable()
							&& !ilConnectionMapping.getIsWebservice()) {
						if (ilConnectionMapping.getiLquery() != null) {
							ilConnectionMapping.setiLquery(
									EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getiLquery()));
						}
						if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery())) {
							ilConnectionMapping.setMaxDateQuery(
									EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getMaxDateQuery()));
						}
						if (ilConnectionMapping.getiLConnection().getConnectionType() != null) {
							ilConnectionMapping.getiLConnection().setConnectionType(EncryptionServiceImpl.getInstance()
									.decrypt(ilConnectionMapping.getiLConnection().getConnectionType()));
						}
						if (ilConnectionMapping.getiLConnection().getUsername() != null) {
							ilConnectionMapping.getiLConnection().setUsername(EncryptionServiceImpl.getInstance()
									.decrypt(ilConnectionMapping.getiLConnection().getUsername()));
						}
						if (ilConnectionMapping.getiLConnection().getPassword() != null) {
							ilConnectionMapping.getiLConnection().setPassword(EncryptionServiceImpl.getInstance()
									.decrypt(ilConnectionMapping.getiLConnection().getPassword()));
						}
						if (ilConnectionMapping.getiLConnection().getServer() != null) {
							ilConnectionMapping.getiLConnection().setServer(EncryptionServiceImpl.getInstance()
									.decrypt(ilConnectionMapping.getiLConnection().getServer()));
						}

						String procedureParameters = ilConnectionMapping.getProcedureParameters();

						if (StringUtils.isNotEmpty(procedureParameters)) {
							if (procedureParameters != null) {
								ilConnectionMapping.setProcedureParameters(
										EncryptionServiceImpl.getInstance().decrypt(procedureParameters));
							}

						}

					}
					if (ilConnectionMapping.getIsFlatFile()) {
						String storageType = ilConnectionMapping.getStorageType();
						if (storageType == null)
							storageType = Constants.Config.STORAGE_TYPE_S3;

						if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)
								&& StringUtils.contains(ilConnectionMapping.getFilePath(), "/")) {
							String encryptedFileName = StringUtils.substringAfterLast(ilConnectionMapping.getFilePath(),
									"/");
							String originalFileName = EncryptionServiceImpl.getInstance().decrypt(encryptedFileName);
							ilConnectionMapping.setFilePath(originalFileName);
						} else if (storageType.equals(Constants.Config.STORAGE_TYPE_S3) && !StringUtils.contains(ilConnectionMapping.getFilePath(), "/")) {
							if(ilConnectionMapping.isEncryptionRequired()){
								String originalFileName = EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getFilePath());
								ilConnectionMapping.setFilePath(originalFileName);
							}else{
								ilConnectionMapping.setFilePath(ilConnectionMapping.getFilePath());
							}
							
						} else if (storageType.equals(Constants.Config.STORAGE_TYPE_LOCAL)) {
							ilConnectionMapping.setFilePath(FilenameUtils.getName(ilConnectionMapping.getFilePath()));
						}
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
			dataResponse.setObject(mappingList);
		} catch (AnvizentRuntimeException | IllegalArgumentException ie) {
			packageService.logError(ie, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			//MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;

	}

	/**
	 * @param clientId
	 * @param locale
	 * @param request
	 * @param sourceFileInfo
	 * @return
	 */
	@RequestMapping(value = "/checkUploadDependancyForCustompackage", method = RequestMethod.POST)
	public DataResponse checkUploadDependancyForCustompackage(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@RequestBody SourceFileInfo sourceFileInfo) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		try {

			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Integer packageId = sourceFileInfo.getFileInfo().getPackageId();
			Package userpackage = packageService.getPackageById(packageId, clientId,
					clientAppDbJdbcTemplate);

			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			if (!userpackage.getIsStandard() && userpackage.getIsMapped()) {
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
					if (userpackage.getFilesHavingSameColumns()) {
						userpackage.setTable(packageService.getTargetTableInfoByPackage(clientId,packageId, clientAppDbJdbcTemplate)
										.getUserPackage().getTable());
						List<Column> seletedColumns = packageService.getTableStructure(null,
								userpackage.getTable().getTableName(), userpackage.getIndustry().getId(), clientId,
								clientJdbcTemplate);
						userpackage.getTable().setColumns(seletedColumns);
						boolean filesHavingSameColumns = true;
						List<String> headersList = sourceFileInfo.getFileInfo().getHeaders();
						String errorColumn = "";
						for (Column column : seletedColumns) {
							String selectedColumn = column.getColumnName();
							if (!headersList.contains(selectedColumn)) {
								errorColumn = selectedColumn;
								filesHavingSameColumns = false;
								break;
							}
						}

						if (!filesHavingSameColumns) {
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage(
									"anvizent.message.validation.text.filesarenothavingsamecolumns", new Object[]{ errorColumn}, locale));
						}
					} else {
						Integer ilConnectionMappingId = sourceFileInfo.getIlConnectionMappingId();
						
						if (ilConnectionMappingId == null || ilConnectionMappingId == 0 ) {
							ClientData clientData = packageService.getTargetTableInfoByPackage(clientId, sourceFileInfo.getFileInfo().getPackageId(), clientAppDbJdbcTemplate);
							userpackage.setTable(clientData.getUserPackage().getTable());

							Table targetTable = clientData.getUserPackage().getTable();
							// get derived target table info
							List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
									userpackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
							userpackage.setDerivedTables(derivedTables);
							//packageService.deleteCustomTablesBYPackageId(userpackage, clientId, clientJdbcTemplate,clientAppDbJdbcTemplate);							
						} else {
							// get temp table info 
							clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
							
							
							Table tempTable = fileService.getCustomTempTableByMappingId(clientId, packageId+"", ilConnectionMappingId, clientAppDbJdbcTemplate);
							
							String tempTableName = tempTable.getTableName();
							List<Column> seletedColumns = packageService.getTableStructure(null,
									tempTableName, 0, null,
									clientStagingJdbcTemplate);
							
							
							boolean filesHavingSameColumns = true;
							List<String> headersList = sourceFileInfo.getFileInfo().getHeaders();
							//String errorColumn = "";
							/*for (Column column : seletedColumns) {
								String selectedColumn = column.getColumnName();
								if (!headersList.contains(selectedColumn)) {
									//errorColumn = selectedColumn;
									filesHavingSameColumns = false;
									break;
								}
							}*/
							
							for (String header : headersList) {
								for (Column column : seletedColumns) {
									String selectedColumn = column.getColumnName();
									if (!header.equals(selectedColumn)) {
										filesHavingSameColumns = false;
										break;
									}
								}
							}
							

							if (!filesHavingSameColumns) {
								
								// drop table and create a new temp table with same name
								packageService.dropTable(null, tempTableName, clientStagingJdbcTemplate);
								// creating new temp table 
								Table table = fileService.processTempTableForFile(sourceFileInfo.getFileInfo());
								table.setTableName(tempTableName);
								ClientData clientData = new ClientData();
								clientData.setUserPackage(new Package());
								clientData.getUserPackage().setTable(table);
								clientData.getUserPackage().setPackageId(packageId);
								packageService.createTargetTable(clientData, clientStagingJdbcTemplate);
								// update isAdvance column
								packageService.updatePackageAdvancedField(packageId, true, clientAppDbJdbcTemplate);
								/* updating headers in file info table */
								fileService.updateFileHeaderByFileId(sourceFileInfo.getFileInfo().getFileHeaders(), clientId, packageId, tempTable.getFileId(), clientAppDbJdbcTemplate);
								message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.PROCEED_FOR_MAPPING);
								message.setText("Some of the structal changes happened. would you like to proceed for mapping columns ?");
								
							}
						}
					}
			}
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.fileUploadingFailed", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	/**
	 * @param clientId
	 * @param locale
	 * @param request
	 * @param sourceFileInfo
	 * @return
	 */
	@RequestMapping(value = "/saveUploadedFileInfo", method = RequestMethod.POST)
	public DataResponse saveUploadedFileInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestBody SourceFileInfo sourceFileInfo) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Package userpackage = packageService.getPackageById(sourceFileInfo.getPackageId(), clientId,clientAppDbJdbcTemplate);
			// fileService.saveUploadedFileInfo(sourceFileInfo);
			if (!userpackage.getIsStandard()) {
				fileService.insertFileColumnDetails(sourceFileInfo.getFileInfo(), clientAppDbJdbcTemplate);
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.addMessage(message);
			dataResponse.setObject(sourceFileInfo.getFilePath());
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.fileUploadingFailed", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveSourceFileInfo", method = RequestMethod.POST)
	public DataResponse saveSourceFileInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestBody SourceFileInfo sourceFileInfo) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Package userpackage = packageService.getPackageById(sourceFileInfo.getPackageId(), clientId,clientAppDbJdbcTemplate);
			int sourceFileInfoId = packageService.updateSourceFileInfo(sourceFileInfo, clientAppDbJdbcTemplate);
			FileInfo fileInfo = sourceFileInfo.getFileInfo();
			fileInfo.setSourceFileInfoId(sourceFileInfoId);
			if (!userpackage.getIsStandard()) {
				fileService.insertFileColumnDetails(fileInfo, clientAppDbJdbcTemplate);
			}
			if (sourceFileInfoId != -1) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.addMessage(message);
				dataResponse.setObject(sourceFileInfoId);
			}
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.fileUploadingFailed", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getFilesInfoByPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse getFileInfoByPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @PathVariable("packageId") String packageId) {

		Integer id = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		if (StringUtils.isNumeric(packageId)) {
			id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingId", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}

		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<FileInfo> fileList = fileService.getFileInfoByPackage(clientId, id, clientAppDbJdbcTemplate);
			for (FileInfo fileInfo : fileList) {
				String filename = fileInfo.getFilePath();
				filename = FilenameUtils.getName(filename);
				try {
					filename = EncryptionServiceImpl.getInstance().decrypt(filename);
				} catch (Throwable e) {
				}
				fileInfo.setFileName(filename);
			}
			dataResponse = new DataResponse();
			dataResponse.setObject(fileList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveMappingTablesInfo", method = RequestMethod.POST)
	public DataResponse saveMappingTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData clientData, Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Column> seletedColumns = clientData.getUserPackage().getTable().getColumns();
		List<String> originalColumns = clientData.getUserPackage().getTable().getOriginalColumnNames();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			if (clientData != null) {
				if (clientData.getUserPackage() != null) {
					// TODO check all the files having same columns otherwise
					// break here

					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
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
						String clientSchemaName = clientJdbcInstance.getClientDbCredentials().get("clientdb_schema")
								.toString();
						if (StringUtils.isNotBlank(clientSchemaName)) {
							clientData.getUserPackage().getTable().setSchemaName(clientSchemaName);

							// create target table in client schema here
							packageService.createTargetTable(clientData, clientJdbcTemplate);
							int targetTableId = packageService.saveTargetTableInfo(clientData, clientAppDbJdbcTemplate);
							if (targetTableId != 0) {
								List<String> aliasColumn = new ArrayList<String>();
								for (Column seletedColumn : seletedColumns) {
									aliasColumn.add(seletedColumn.getColumnName());
								}
								clientData.getUserPackage().getTable().setTableId(targetTableId);
								// save target table alias names
								boolean valid = aliasColumn.equals(originalColumns);
								if (!valid)
									packageService.saveTargetTableAliasNames(clientData, clientAppDbJdbcTemplate);
							}

							dataResponse.setObject(new Integer(targetTableId));
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
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingClientSchema", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						}
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
					return dataResponse;
				}
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileSaveMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);

			message.setText(MinidwServiceUtil.getErrorMessageString(t));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveMultipleTablesMappingInfo/{packageId}/{industryId}", method = RequestMethod.POST)
	public DataResponse saveMultipleTableMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);

			LOGGER.info("pakcage id : " + packageId);

			if (StringUtils.isNotEmpty(packageId)) {
				String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader,
						clientAppDbJdbcTemplate);
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientIdfromHeader, clientAppDbJdbcTemplate);
				if ( s3BucketInfo == null ) {
					s3BucketInfo = new S3BucketInfo();
				}
				Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();

				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();

				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				boolean isCustomTempTablesDeleted = fileService.deleteCustomTempTables(packageId, clientId,clientAppDbJdbcTemplate,clientJdbcTemplate, s3BucketInfo);

				if (!isCustomTempTablesDeleted) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToProcessRequest", null,
							locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
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

					} else if (!ilConnectionMapping.getIsFlatFile() && ilConnectionMapping.getIsHavingParentTable()) {}
					
					
					// file processing start for both database and flat file.
					if (ilConnectionMapping.getFilePath() != null) {
						String s3FilePath = ilConnectionMapping.getFilePath() ;
						boolean encryptionRequired = fileSettings.getFileEncryption();
						List<String> filesList = MinidwServiceUtil.downloadFilesFromS3(s3FilePath, clientIdfromHeader, deploymentType, false, s3BucketInfo,encryptionRequired);
						String filePath = filesList.get(0);

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
						fileInfo.setSourceFileInfoId((int)ilConnectionMapping.getSourceFileInfoId());
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
						if (!userPackage.getIsStandard()) {
							// get main target table info
							ClientData clientData1 = packageService.getTargetTableInfoByPackage(clientId, Integer.parseInt(packageId),
									clientAppDbJdbcTemplate);
							if (clientData1 != null) {
								userPackage.setTable(clientData1.getUserPackage().getTable());

								Table targetTable = clientData1.getUserPackage().getTable();
								// get derived target table info
								List<TableDerivative> derivedTables = fileService.getCustomTargetDerivativeTables(clientId,
										userPackage.getPackageId(), targetTable.getTableId(), clientAppDbJdbcTemplate);
								userPackage.setDerivedTables(derivedTables);
								if(StringUtils.isNotBlank(clientData1.getUserPackage().getTable().getTableName())) {
									packageService.updatePackageAdvancedField(Integer.parseInt(packageId), true, clientAppDbJdbcTemplate);
								}
							}
							
						}

						packageService.createTargetTable(clientData, clientJdbcTemplate);
						fileService.insertTempTableMapping(fileInfo, table, modification, clientAppDbJdbcTemplate);
						String storageType = ilConnectionMapping.getStorageType();
						if (storageType == null)
							storageType = Constants.Config.STORAGE_TYPE_S3;

						try {
							FileUtils.deleteQuietly(new File(filePath));
						} catch (Throwable e) {
						}
					}
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.targetTableNameAlreadyExist",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileSaveMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileSaveMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}


	@RequestMapping(value = "/updateFileHavingSameColumns/{packageId}/{filesHavingSameColumns}", method = RequestMethod.GET)
	public DataResponse updateFilesHavingSameColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @PathVariable("packageId") String packageId,
			@PathVariable("filesHavingSameColumns") Boolean filesHavingSameColumns) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Integer id = null;
		if (StringUtils.isNumeric(packageId)) {
			id = Integer.parseInt(packageId);
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingId", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			int i = fileService.updateFilesHavingSameColumns(clientId, id, filesHavingSameColumns,
					clientAppDbJdbcTemplate);
			if (i > 0) {
				dataResponse = new DataResponse();
				dataResponse.setObject(new Integer(i));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToUpdateFile", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingFile", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientUserDeatils", method = RequestMethod.GET)
	public DataResponse getClientUserDeatils(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<User> userList = packageService.getClientUserDeatils(clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(userList);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchUsers", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileFetchingUsers", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientIlsList", method = RequestMethod.POST)
	public DataResponse getClientIlsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		messages.add(message);
		dataResponse.addMessages(messages);
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<ILInfo> ilInfoList = packageService.getClientIlsList(clientIdfromHeader, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(ilInfoList);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchIlsList", null, locale));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileFetchingClientIls", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientUserIds", method = RequestMethod.GET)
	public DataResponse getClientUserIds(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);

		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {

			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<String> userList = packageService.getClientUserIds(clientId, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(userList);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchUsers", null, locale));

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileFetchingUsers", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientSchemaName", method = RequestMethod.GET)
	public DataResponse getClientSchemaName(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			if (clientDbDetails != null) {
				dataResponse.setObject(clientDbDetails);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.clientDbDetailsAreNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchClientDbDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileFetchClientDbDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientDDlTables", method = RequestMethod.GET)
	public DataResponse getClientDDlTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<DDLayout> dDLayoutList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			dDLayoutList = packageService.getDDlayoutTablesList(clientIdfromHeader, clientId, clientAppDbJdbcTemplate);
			if (dDLayoutList != null) {
				dataResponse.setObject(dDLayoutList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchDdlTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileFetchingDdlTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDDlTableView", method = RequestMethod.POST)
	public DataResponse getDDlTableView(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DDLayout dDLayoutTable = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			dDLayoutTable = packageService.getDDlayoutTable(clientIdfromHeader, dDLayout.getId(), userId,
					clientAppDbJdbcTemplate);
			if (dDLayoutTable != null) {
				dataResponse.setObject(dDLayoutTable);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.ddlDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchDdlTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccuredWhileFetchingDdlTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/viewDDlTableSelectQry", method = RequestMethod.POST)
	public DataResponse viewDDlTableSelectQry(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DDLayout dDLayoutTable = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			dDLayout.setClientId(clientIdfromHeader);
			dDLayoutTable = packageService.viewDDlTableSelectQry(userId, dDLayout, clientAppDbJdbcTemplate);
			if (dDLayoutTable != null) {
				dataResponse.setObject(dDLayoutTable);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchDdlTableSelectQuery", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateDDlayoutTable", method = RequestMethod.POST)
	public DataResponse updateDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			dDLayout.setModification(modification);

			DDLayout dDLayoutTable = packageService.getDDlayoutTable(clientIdfromHeader, dDLayout.getId(), userId,
					clientAppDbJdbcTemplate);
			if (dDLayoutTable != null) {
				if (dDLayoutTable.isDdlEditable()) {
					count = packageService.updateDDlayoutTable(clientIdfromHeader, dDLayout, userId,
							clientAppDbJdbcTemplate);
					if (count != -1) {
						dataResponse.setObject(count);
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.error.text.queryIsUpdated", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(messageSource.getMessage("anvizent.message.text.unableToUpdateDdlTable", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.text.youDontHaveAccessToEditThisDdlQuery", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.ddlDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.unableToUpdateDdlTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileUpdatingDdlTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteDDlayoutTable", method = RequestMethod.POST)
	public DataResponse deleteDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		int count = -1;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			DDLayout dDLayoutTable = packageService.getDDlayoutTable(clientIdfromHeader, dDLayout.getId(), userId,
					clientAppDbJdbcTemplate);
			if (dDLayoutTable != null) {
				if (dDLayoutTable.isDdlEditable()) {

					boolean tableExist = fileService.isTableExists(userId, null, dDLayoutTable.getTableName(),
							clientJdbcTemplate);
					if (tableExist) {
						packageService.dropDDlayoutTable(userId, dDLayoutTable, clientJdbcTemplate);
						count = packageService.deleteDDlayoutTable(userId, dDLayoutTable, clientAppDbJdbcTemplate);
						if (count != -1) {
							List<DDLayout> dDLayoutList = packageService.getDDlayoutTablesList(clientIdfromHeader,
									userId, clientAppDbJdbcTemplate);
							dataResponse.setObject(dDLayoutList);
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							message.setText(messageSource.getMessage("anvizent.message.text.targetTableIsDeleted", new Object[]{ dDLayoutTable.getTableName()}, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						}
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(
								"target table " + dDLayoutTable.getTableName() + " doesn't exist in client db.");
						messages.add(message);
						dataResponse.setMessages(messages);
					}
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.text.youDontHaveAccessToDeleteThisDdl", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.ddlDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.unableToDeleteDdlTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileDeletingDdlTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/runDDlayoutTable", method = RequestMethod.POST)
	public DataResponse runDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DDLayout ddlayout = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		int count = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ddlayout = packageService.getDDlayoutTable(clientIdfromHeader, dDLayout.getId(), userId,
					clientAppDbJdbcTemplate);
			boolean tableExist = fileService.isTableExists(userId, null, ddlayout.getTableName(), clientJdbcTemplate);
			if (tableExist) {
				String errorMessage = "";
				try {
					count = commonProcessor.runDDlayoutTable(ddlayout, clientJdbcTemplate);
				} catch (BadSqlGrammarException e) {
					packageService.logError(e, request, clientAppDbJdbcTemplate);
					errorMessage = e.getRootCause().getMessage();
				} catch (SQLException e) {
					packageService.logError(e, request, clientAppDbJdbcTemplate);
					errorMessage = e.getMessage();
				}
				ddlayout.setErrorMessage(errorMessage);
				ddlayout.setRunType("DDL Layout");
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(userId);
				ddlayout.setModification(modification);
				ddlayout.setInsertedCount(count);
				packageService.updateDDlayoutTableAuditLogs(userId, ddlayout, clientAppDbJdbcTemplate);
				if (errorMessage != null && !errorMessage.isEmpty()) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.text.ddlRefreshFailedReason", null, locale) + errorMessage);
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.text.RecordsInsertedIntoTargetTable", new Object[]{count,ddlayout.getTableName()}, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(ae.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/viewDDlTableResults", method = RequestMethod.POST)
	public DataResponse viewDDlTableResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody DDLayout dDLayout, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<DDLayout> dDLayoutTableList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			dDLayout.setClientId(clientIdfromHeader);
			dDLayoutTableList = packageService.getDDlayoutTableAuditLogs(userId, dDLayout, clientAppDbJdbcTemplate);
			if (dDLayoutTableList != null) {
				dataResponse.setObject(dDLayoutTableList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileFetchingDdlTablesAuditLogs", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getFlatFilePreview/{packageId}/{mappingId}", method = RequestMethod.GET)
	public DataResponse getFlatFilePreview(@PathVariable("mappingId") String mappingId, @PathVariable("packageId") String packageId,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale) {
		ILConnectionMapping ilConnectionMapping = null;
		String decryptedFilepath = null;
		String fileType = null;
		String delimeter = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ilConnectionMapping = packageService.getILConnectionMappingInfoForPreview(Integer.parseInt(mappingId), Integer.parseInt(packageId), clientId, clientAppDbJdbcTemplate);
			if (ilConnectionMapping != null && ilConnectionMapping.getIsFlatFile() && ilConnectionMapping.getFilePath() != null) {
				String s3FilePath = ilConnectionMapping.getFilePath();
				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, clientAppDbJdbcTemplate);
				String deploymentType = Constants.Config.DEPLOYMENT_TYPE_CLOUD;
				if(ilConnectionMapping.getStorageType().equals(Constants.Config.STORAGE_TYPE_LOCAL)){
					deploymentType = Constants.Config.DEPLOYMENT_TYPE_ONPREM;
				}
				List<String> downloadedFilesFromS3 = MinidwServiceUtil.downloadFilesFromS3(s3FilePath, clientIdfromHeader, deploymentType, false, s3BucketInfo, ilConnectionMapping.isEncryptionRequired());
				if (downloadedFilesFromS3 != null && downloadedFilesFromS3.size() > 0) {
					decryptedFilepath = downloadedFilesFromS3.get(0);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null,
							locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				fileType = ilConnectionMapping.getFileType();
				delimeter = ilConnectionMapping.getDelimeter();
				if (StringUtils.isNotBlank(decryptedFilepath)) {
					List<List<String>> previewData = fileService.getFileDataPreview(decryptedFilepath, fileType,
							delimeter);
					dataResponse.setObject(previewData);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.mappingNotFoundForWithPackageId", new Object[]{ mappingId, packageId }, locale));
			}
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(ae));
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(t));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			if (StringUtils.isNotBlank(decryptedFilepath)) {
				FileUtils.deleteQuietly(new File(decryptedFilepath));
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getUploadAndExecutionStatusComments", method = RequestMethod.POST)
	public DataResponse getExecutionStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("executionId") int executionId,@RequestParam("uploadOrExecution") String uploadOrExecution, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String executionComments = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			if(!uploadOrExecution.equals("all")){
				executionComments = packageService.getUploadAndExecutionStatusComments(executionId, uploadOrExecution,clientAppDbJdbcTemplate);
			}else{
				executionComments = packageService.getUploadStatusAndExecutionStatusComments(executionId,clientAppDbJdbcTemplate);	
			}
			
			if (executionComments != null) {
				dataResponse.setObject(executionComments);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.executionStatusCommentsNotAvailable", null, locale));
			}
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getPackageExecutionTargetTableInfo", method = RequestMethod.POST)
	public DataResponse getPackageExecutionTargetTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpSession session, @RequestBody PackageExecution  packageExecution, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<PackageExecution> packageExecutionList = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if(packageExecution.getExecutionId() != 0)
			packageExecutionList = packageService.getPackageExecutionTargetTableInfo(packageExecution.getExecutionId(),clientAppDbJdbcTemplate);
			String timeZone = CommonUtils.getTimeZoneFromHeader(request);
			if (packageExecutionList.size() > 0 ) {
				for ( PackageExecution packageExecute: packageExecutionList ) {
					packageExecute.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecute.getExecutionStartDate(), timeZone));
					packageExecute.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecute.getLastExecutedDate(), timeZone));
				}
				dataResponse.setObject(packageExecutionList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.packageExecutionTargetTableInfoNotAvailable", null, locale));
			}
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getPackageExecutionSourceMappingInfo", method = RequestMethod.POST)
	public DataResponse getPackageExecutionSourceMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody PackageExecution  packageExecution, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<PackageExecution> packageExecutionList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if(packageExecution.getExecutionId() != 0)
			packageExecutionList = packageService.getPackageExecutionSourceMappingInfo(packageExecution.getExecutionId(),clientAppDbJdbcTemplate);
			if (packageExecutionList.size() > 0 ) {
				dataResponse.setObject(packageExecutionList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.packageExecutionSourceMappingInfoNotAvailable", null, locale));
			}
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/clientTableScriptsInstantExecution", method = RequestMethod.POST)
	public DataResponse clientTableScriptsInstantExecution(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@RequestBody TableScriptsForm tableScriptsForm) {


		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Connection con = null;
		String status = "success";
		String statusMsg = "";
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));

			try {
				if (!tableScriptsForm.getSchemaName().equalsIgnoreCase("Main")) {
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				} else {
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				}
				con = clientJdbcTemplate.getDataSource().getConnection();
				if (con != null) {
					ScriptRunner runner = new ScriptRunner(con, false, false);
					String fileName = CommonUtils.writeScriptFile(tableScriptsForm.getSqlScript());
					runner.runScript(new BufferedReader(new FileReader(fileName)));
					statusMsg = "Successfully executed";
					message.setCode(messageSource.getMessage("anvizent.message.success.code", null, null));
					message.setText(messageSource.getMessage(
							"anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale));
				}

			} catch (Exception e) {
				status = "failed";
				statusMsg = e.getLocalizedMessage();
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(messageSource.getMessage("anvizent.message.failed.code", null, null));
				message.setText("Execution failed with below reason <br><b>" + e.getLocalizedMessage());
			} catch (Throwable t) {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			} finally {
				CommonUtils.closeConnection(con);
			}

			etlAdminService.updateInstantTableScriptExecution(tableScriptsForm, status, statusMsg, modification,
					clientAppDbJdbcTemplate);

			messages.add(message);
			dataResponse.setMessages(messages);

		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while clientTableScriptsInstantExecution() ", e);
			message.setCode(messageSource.getMessage("anvizent.message.error.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.error.text.executionFailed", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e2) {
			}
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/getPreviousExecutedScripts", method = RequestMethod.POST)
	public DataResponse getPreviousExecutedScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestBody TableScriptsForm tableScripts, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScriptsForm> prevoiusExcutetedScriptList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			prevoiusExcutetedScriptList = etlAdminService.getPreviousExecutedScripts(tableScripts.getClientId(),
					clientAppDbJdbcTemplate);
			if (prevoiusExcutetedScriptList != null) {
				dataResponse.setObject(prevoiusExcutetedScriptList);
				message.setCode(messageSource.getMessage("anvizent.message.success.code", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(messageSource.getMessage("anvizent.message.error.code", null, null));
				message.setText(messageSource.getMessage(
						"anvizent.message.error.text.unableToRetrievePreviousExecutedTableScriptList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} /*
			 * catch (Exception ae) { packageService.logError(ae, request);
			 * LOGGER.error("error while getTableScriptsMappingByClient() ",
			 * ae); message.setCode(messageSource.getMessage(
			 * "anvizent.message.error.code", null, locale));
			 * message.setText(messageSource.getMessage(
			 * "anvizent.message.error.text.unableToRetrievePreviousExecutedTableScriptList",
			 * null, locale)); messages.add(message); }
			 */ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getELTClusterLogs", method = RequestMethod.POST)
	public DataResponse getELTClusterLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody String  eltClientDbDetails, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/eltLogsDataResponse/{initiateId}", method = RequestMethod.GET)
	public DataResponse getEltLogsDataResponse(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("initiateId") int initiateId, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			final String uri = eltLogsPath +"getLogs";
			   RestTemplate restTemplate = new RestTemplate();
			   // Add the Jackson message converter
			   restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			   // create request body
			   JSONObject json = new JSONObject();
			   JSONObject json2 = new JSONObject();
			   JSONObject json3 = new JSONObject();
			   json.put("id", initiateId);
			   
			   json3.put("host",clientDbCredentials.getHostname());
			   json3.put("portNumber",clientDbCredentials.getPortnumber());
			   json3.put("userName", clientDbCredentials.getClientDbUsername());
			   json3.put("password", EncryptionServiceImpl.getInstance().encrypt(clientDbCredentials.getClientDbPassword()));
			   json3.put("privateKey", ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
			   json3.put("iv", ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);
			   
			   
			   json2.put("rdbmsConnection", json3);
			   json2.put("appDBName", clientDbCredentials.getClientDbSchema());
			   
			   json.put("clientDBDetails", json2);
			   
			   System.out.println(json);

			   // set headers
			   HttpHeaders headers = new HttpHeaders();
			   headers.setContentType(MediaType.APPLICATION_JSON);
			   HttpEntity<Object> entity = new HttpEntity<Object>(json.toString(), headers);

			   // send request and parse result
			   ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);

			  dataResponse.setObject(response.getBody());
			
			
			
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getELTIntiateResponse/{executionId}", method = RequestMethod.GET)
	public DataResponse getELTIntiateResponse(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("executionId") int executionId,Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<ELTClusterLogsInfo> eltclusterLogs = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			eltclusterLogs = packageService.getEltInitiateInfo(executionId,clientAppDbJdbcTemplate);
			if (eltclusterLogs != null) {
				dataResponse.setObject(eltclusterLogs);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.executionStatusCommentsNotAvailable", null, locale));
			}
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
}
