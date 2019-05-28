/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.anvizent.amazon.AmazonS3Utilities;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateStatusUpdationException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.helper.ScriptRunner;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.MigrationService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.ClientConfigurationSettings;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.ClientSpecificILDLJars;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CommonJob;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CurrencyList;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.ERP;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ETLJobContextParam;
import com.datamodel.anvizent.service.model.EncryptWebserviceAuthParams;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.GeneralSettings;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Internalization;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Kpi;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.MultiClientInsertScriptsExecution;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.ServerConfigurations;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.TemplateMigration;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.VersionUpgrade;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceILMapping;

/**
 * @author rakesh.gajula
 * Modified by mahender.alaveni
 * date:- 09/01/2019
 *
 */
@RestController("user_etlAdminServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin")
@CrossOrigin
public class ETLAdminServiceDataRestController
{

	protected static final Log LOGGER = LogFactory.getLog(ETLAdminServiceDataRestController.class);

	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ETLAdminService etlAdminService;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	FileService fileService;
	@Autowired
	private PackageService packageService;
	@Autowired
	MigrationService migrationService;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	ExecutionProcessor executionProcessor;
	@Autowired
	private CommonDao commonDao;
	 
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "/createContextParameter/{parameterName}", method = RequestMethod.POST)
	public DataResponse createContextParameter(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("parameterName") String parameterName, HttpServletRequest request, Locale locale)
	{
		int count = 0;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		int existContextParameter = 0;
		ETLAdmin eTLAdmin = new ETLAdmin();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( parameterName != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				eTLAdmin.setModification(modification);

				existContextParameter = etlAdminService.getExistContextParameter(parameterName, clientAppDbJdbcTemplate);
				if( existContextParameter == 0 )
				{
					count = etlAdminService.createContextParameter(eTLAdmin, parameterName, clientAppDbJdbcTemplate);
					dataResponse.setObject(count);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterCreatedsuccessfully", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					dataResponse.setObject(count);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterAlreadyExist", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.parametershouldNotBeEmpty", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			LOGGER.error("error while createContextParameter() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateContextParameter", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createContextParams", method = RequestMethod.POST)
	public DataResponse createContextParams(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ContextParameter contextParameter, HttpServletRequest request, Locale locale)
	{
		int count = 0;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		int existContextParameter = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			contextParameter.setModification(modification);

			existContextParameter = etlAdminService.createContextParams(contextParameter, clientAppDbJdbcTemplate);
			if( existContextParameter > 0 )
			{
				dataResponse.setObject(count);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterCreatedsuccessfully", null, locale));
			}
			else
			{
				dataResponse.setObject(count);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateContextParameter", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getContextParameters", method = RequestMethod.GET)
	public DataResponse getContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ContextParameter> contextParameters = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			contextParameters = etlAdminService.getContextParameters(0, clientAppDbJdbcTemplate);
			if( contextParameters != null && contextParameters.size() > 0 )
			{
				dataResponse.setObject(contextParameters);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveContextParameters", null, locale));
			}
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while getContextParameters() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveContextParameters", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);

		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getContextParameterById/{id}", method = RequestMethod.GET)
	public DataResponse getContextParameterById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") Integer id, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ContextParameter contextParameters = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			contextParameters = etlAdminService.getContextParametersById(id, clientAppDbJdbcTemplate);
			if( contextParameters != null )
			{
				dataResponse.setObject(contextParameters);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveContextParameters", null, locale));
			}
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while getContextParameters() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveContextParameters", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getContextParamValue", method = RequestMethod.GET)
	public DataResponse getContextParamValue(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Map<String, Object>> contextParameters = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			contextParameters = etlAdminService.getContextParamValue(0, clientAppDbJdbcTemplate);
			dataResponse.setObject(contextParameters);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			t.printStackTrace();
			LOGGER.error("error while getContextParamValue() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveContextParameterDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateContextParams", method = RequestMethod.POST)
	public DataResponse updateContextParams(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ContextParameter contextParameter, HttpServletRequest request, Locale locale)
	{
		int count = 0;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		int existContextParameter = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			contextParameter.setModification(modification);

			existContextParameter = etlAdminService.updateContextParams(contextParameter, clientAppDbJdbcTemplate);
			if( existContextParameter > 0 )
			{
				dataResponse.setObject(count);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterUpdatedsuccessfully", null, locale));
			}
			else
			{
				dataResponse.setObject(count);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updateToUpdateContextParameter", null, locale));
			}

		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.success.text.ContextParameterAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while updateContextParams() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.success.text.updateToUpdateContextParameter", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDlInfo", method = RequestMethod.GET)
	public DataResponse getDlInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Industry> industryList = null;
		List<DLInfo> dlInfo = null;
		List<DLInfo> dlInfoList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			industryList = userService.getAllIndustries(clientAppDbJdbcTemplate);
			for (Industry industry : industryList)
			{
				dlInfo = userService.getAllDLs(industry.getId(), clientAppDbJdbcTemplate);
				dlInfoList.addAll(dlInfo);
			}

			dataResponse.setObject(dlInfoList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while getDlInfo() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDlInfo", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getIlInfo/{dlId}", method = RequestMethod.GET)
	public DataResponse getIlInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("dlId") String dlId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ILInfo> ilList = null;
		try
		{
			int diID = Integer.parseInt(dlId);
			ilList = userService.getAllILs(diID, commonJdbcTemplate);
			dataResponse.setObject(ilList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while getIlInfo() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveIlInfo", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveEtlDlIlMapping", method = RequestMethod.POST)
	public DataResponse saveEtlDlIlMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int mappingCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eTLAdmin.setModification(modification);
			etlAdminService.deleteEtlDlIlMappingByDlId(eTLAdmin, clientAppDbJdbcTemplate);
			mappingCount = etlAdminService.saveEtlDlIlMapping(eTLAdmin, clientAppDbJdbcTemplate);
			dataResponse.setObject(mappingCount);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.EtlDlIlMappingSavedSuccesfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while saveEtlDlIlMapping() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDLILMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/uploadIlOrDlFiles", method = RequestMethod.POST)
	public DataResponse uploadIlOrDlFiles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale, @RequestParam("files") MultipartFile[] multipartFiles, @RequestParam(value = "isCommonJob", required = false) String isCommonJob)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		File tempFile = null;
		try
		{
			if( multipartFiles != null && multipartFiles.length == 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			for (int i = 0; i < multipartFiles.length; i++)
			{
				MultipartFile multipartFile = multipartFiles[i];
				LOGGER.info(multipartFile.getOriginalFilename());
				// logger.info(Constants.);

				try
				{
					tempFile = CommonUtils.multipartToFile(multipartFile);
					String dir = CommonUtils.getETLFolderPath(StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), "."));
					if( StringUtils.isNotBlank(isCommonJob) && isCommonJob.equalsIgnoreCase("yes") )
					{
						dir = CommonUtils.getCommonEtlJobsPath();
					}
					File existedFile = new File(dir + "/" + multipartFile.getOriginalFilename());
					// Taking backup of existed jar file
					if( existedFile.exists() )
					{
						String backupFolder = dir + Constants.Config.ETL_BACKUP_FOLDER;
						CommonUtils.createDir(backupFolder);
						FileCopyUtils.copy(existedFile, new File(backupFolder + multipartFile.getOriginalFilename() + "_" + CommonDateHelper.formatDateAsTimeStamp(new Date())));
					}

					if( StringUtils.isNotBlank(dir) )
					{
						fileService.uploadFileIntoServer(tempFile, clientId, dir);
					}

				}
				finally
				{
					if( tempFile != null )
					{
						tempFile.delete();
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dlMasterCreatedOrUpdatedSuccessfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createDl", method = RequestMethod.POST)
	public DataResponse createDl(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int createdlId = 0;
		int contextParamCount = 0;
		int dlMappingCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		// int industryId = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eTLAdmin.setModification(modification);
			createdlId = etlAdminService.saveDlInfo(eTLAdmin, clientAppDbJdbcTemplate);
			DLInfo dlinfo = new DLInfo();
			dlinfo.setdL_id(createdlId);
			/*
			 * for(Industry industry:user.getIndustries()){ industryId =
			 * industry.getId(); }
			 */
			eTLAdmin.setDlInfo(dlinfo);
			if( createdlId > 0 )
			{
				contextParamCount = etlAdminService.saveEtlDlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
			}
			if( contextParamCount > 0 )
			{
				dlMappingCount = etlAdminService.saveEtlDlJobsmapping(eTLAdmin, clientAppDbJdbcTemplate);
			}

			dataResponse.setObject(dlMappingCount);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.DlCreatedSuccesfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveDlClientidMapping", method = RequestMethod.POST)
	public DataResponse saveClientAndDls(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int mappingCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			eTLAdmin.setModification(modification);

			int id = Integer.parseInt(eTLAdmin.getClientId());
			etlAdminService.deleteDlClientidMapping(id, clientAppDbJdbcTemplate);
			mappingCount = etlAdminService.saveDlClientidMapping(eTLAdmin, clientAppDbJdbcTemplate);

			if( mappingCount > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.DlClientIdMappingSavedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveClientDLMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDlClientidMapping/{Id}", method = RequestMethod.GET)
	public DataResponse getDlClientidMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("Id") String Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<DLInfo> dlInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int clientId = Integer.parseInt(Id);
			dlInfoList = etlAdminService.getDlClientidMapping(clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(dlInfoList);
			if( dlInfoList != null && dlInfoList.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.thereIsNoClientVerticalMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getVerticalMappedDLsByClientId/{client_id}", method = RequestMethod.GET)
	public DataResponse getVerticalMappedDLsByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("client_id") String client_id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<DLInfo> dlInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int clientId = Integer.parseInt(client_id);
			dlInfoList = etlAdminService.getVerticalMappedDLsByClientId(clientId, clientAppDbJdbcTemplate);
			dataResponse.setObject(dlInfoList);
			if( dlInfoList != null && dlInfoList.size() > 0 )
			{
				dataResponse.setObject(dlInfoList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				// message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDLsList",
				// null, locale));
				message.setText(messageSource.getMessage("anvizent.message.error.text.thereIsNoClientVerticalMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getClientSourceDetails/{Id}", method = RequestMethod.GET)
	public DataResponse getClientSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("Id") String Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ETLAdmin etlAdmin = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( userId != null )
			{
				int clientId = Integer.parseInt(Id);
				etlAdmin = etlAdminService.getClientSourceDetails(clientId, clientAppDbJdbcTemplate);
			}
			if( etlAdmin != null )
			{
				dataResponse.setObject(etlAdmin);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.noFlatFileAndDatabaseLocked", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDLData/{dl_id}", method = RequestMethod.GET)
	public DataResponse getDLData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("dl_id") int dlId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ETLAdmin etlAdmin = new ETLAdmin();
		List<DLInfo> dlData = null;
		List<ILInfo> ilData = null;
		List<Kpi> kpiData = null;
		List<ETLJobContextParam> contextparams = null;
		DLInfo dlInfo = new DLInfo();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dlData = etlAdminService.getDLInfoById(dlId, clientAppDbJdbcTemplate);
			ilData = etlAdminService.getAllILs(dlId, clientAppDbJdbcTemplate);
			kpiData = etlAdminService.getAllKpi(dlId, clientAppDbJdbcTemplate);
			etlAdmin.setdLInfo(dlData);
			etlAdmin.setiLInfo(ilData);
			etlAdmin.setKpiInfo(kpiData);
			String jobFileNames = etlAdminService.getJobFilesByDLId(dlId, clientAppDbJdbcTemplate);
			String[] fileNames = null;
			List<String> jobFileNamesList = new ArrayList<>();
			if( jobFileNames != null )
			{
				fileNames = jobFileNames.split(",");
				for (String s : fileNames)
				{
					jobFileNamesList.add(s);
				}
			}
			etlAdmin.setFileNames(jobFileNamesList);
			dlInfo.setJobFileNames(jobFileNames);
			etlAdmin.setDlInfo(dlInfo);
			// etlAdmin.setFileNames(etlAdminService.getFilesByDLId(dlId));
			contextparams = etlAdminService.getParamsByDLId(dlId, clientAppDbJdbcTemplate);
			etlAdmin.seteTLJobContextParamList(contextparams);
			dataResponse.setObject(etlAdmin);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateDL", method = RequestMethod.POST)
	public DataResponse updateDL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updateDL = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			eTLAdmin.setModification(modification);
			updateDL = etlAdminService.updateDLInfo(eTLAdmin, clientAppDbJdbcTemplate);
			if( updateDL > 0 && eTLAdmin.getFileNames() != null )
			{
				etlAdminService.deleteDLFileInfo(eTLAdmin, clientAppDbJdbcTemplate);
				etlAdminService.saveEtlDlJobsmapping(eTLAdmin, clientAppDbJdbcTemplate);
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.iLUpdatedSuccesfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateFileSettings", method = RequestMethod.POST)
	public DataResponse updateFileSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody FileSettings fileSettings, HttpServletRequest request, Locale locale)
	{
		int count = 0;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			fileSettings.setModification(modification);
			count = etlAdminService.updateFileSettings(fileSettings, clientAppDbJdbcTemplate);
			if( count > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.MaxFileSizeUpdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.maxFileSizeUpdationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteIL/{il_id}", method = RequestMethod.GET)
	public DataResponse deleteIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("il_id") int ilId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int count = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			count = etlAdminService.deleteILById(ilId, clientAppDbJdbcTemplate);
			if( count > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ILDeleted", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else if( count == 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.errorWhileDeleingIL", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteDL/{dl_id}", method = RequestMethod.GET)
	public DataResponse deleteDL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("dl_id") int dlId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			count = etlAdminService.deleteDLById(dlId, clientAppDbJdbcTemplate);
			if( count > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.DLDeleted", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else if( count == 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.errorWhileDeleingDL", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTemplate", method = RequestMethod.GET)
	public DataResponse getTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{
		Message message = new Message();
		List<Map<String, String>> ilTemp = null;
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilTemp = etlAdminService.getTemplate(clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(ilTemp);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/uploadILAndXrefTemplate", method = RequestMethod.POST)
	public DataResponse uploadILAndXrefTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestParam("iLTemplate") MultipartFile iLTemplateFile,
			@RequestParam(value = "xrefTemplate", required = false) MultipartFile xrefTemplateFile, @RequestParam("iLName") String iLName, @RequestParam("iLID") String ilID, @RequestParam("isSameAsIL") Boolean isSameAsIL, @RequestParam("isInsert") Boolean isInsert)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		File iLFile = null;
		File xrefFile = null;
		ClientData clientData = new ClientData();
		// userObjectRemoved
		File iLTempFile = null;
		File xrefTempFile = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			// checking whether the il_file is empty or not
			if( iLTemplateFile != null )
			{
				iLTempFile = CommonUtils.multipartToFile(iLTemplateFile);
				String flatFileType = StringUtils.substringAfterLast(iLTemplateFile.getOriginalFilename(), ".");
				String delimeter = null;
				if( flatFileType.equals(Constants.FileType.CSV) )
				{
					delimeter = ",";
				}
				// get file headers
				List<String> iL_headers = fileService.getHeadersFromFile(iLTempFile.getAbsolutePath(), flatFileType, delimeter, null);
				if( iL_headers.size() > 0 )
				{
					if( iL_headers.size() == 1 && iL_headers.get(0).length() == 0 )
					{
						message.setCode("ERROR_IL");
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
						return dataResponse;
					}
				}
			}
			if( !isSameAsIL )
			{
				// checking whether the xref_file is empty or not
				if( xrefTemplateFile != null )
				{
					xrefTempFile = CommonUtils.multipartToFile(xrefTemplateFile);
					String flatFileType = StringUtils.substringAfterLast(xrefTemplateFile.getOriginalFilename(), ".");
					String delimeter = null;
					if( flatFileType.equals(Constants.FileType.CSV) )
					{
						delimeter = ",";
					}
					// get file headers
					List<String> xrefHeaders = fileService.getHeadersFromFile(xrefTempFile.getAbsolutePath(), flatFileType, delimeter, null);
					if( xrefHeaders.size() > 0 )
					{
						if( xrefHeaders.size() == 1 && xrefHeaders.get(0).length() == 0 )
						{
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR_XREF);
							message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
							return dataResponse;
						}
					}
				}
			}
			if( isSameAsIL && iLTemplateFile.getSize() > 0 )
			{
				iLFile = CommonUtils.multipartToFile(iLTemplateFile);
				String iLTemplateDir = CommonUtils.getILCsvTemplatePath();
				String xrefTemplateDir = CommonUtils.getXRefILCsvTemplatePath();
				if( StringUtils.isNotBlank(iLTemplateDir) && StringUtils.isNotBlank(xrefTemplateDir) )
				{
					fileService.uploadFileIntoServer(iLFile, clientId, iLTemplateDir);
					fileService.uploadFileIntoServer(iLFile, clientId, xrefTemplateDir);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.templateUploadedSuccessfully", null, locale));
				}
			}
			else if( !isSameAsIL && iLTemplateFile.getSize() > 0 && xrefTemplateFile.getSize() > 0 )
			{
				iLFile = CommonUtils.multipartToFile(iLTemplateFile);
				xrefFile = CommonUtils.multipartToFile(xrefTemplateFile);
				String iLTemplateDir = CommonUtils.getILCsvTemplatePath();
				String xrefTemplateDir = CommonUtils.getXRefILCsvTemplatePath();
				if( StringUtils.isNotBlank(iLTemplateDir) && StringUtils.isNotBlank(xrefTemplateDir) )
				{
					fileService.uploadFileIntoServer(iLFile, clientId, iLTemplateDir);
					fileService.uploadFileIntoServer(xrefFile, clientId, xrefTemplateDir);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.templatesUploadedSuccessfully", null, locale));
				}
			}

			if( isInsert )
			{
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				clientData.setModification(modification);
				ILInfo ilinfo = new ILInfo();
				ilinfo.setiL_id(Integer.parseInt(ilID));
				ilinfo.setiL_name(iLTemplateFile.getOriginalFilename());
				ilinfo.setxRef_IL_name(xrefTemplateFile.getOriginalFilename());
				clientData.setIlInfo(ilinfo);
				etlAdminService.addNewILAndXrefTemplateInfo(clientData, clientAppDbJdbcTemplate);
			}
			else
			{
				Modification modification = new Modification(new Date());
				modification.setModifiedBy(clientId);
				clientData.setModification(modification);
				ILInfo ilinfo = new ILInfo();
				ilinfo.setiL_id(Integer.parseInt(ilID));
				ilinfo.setiL_name(iLTemplateFile.getOriginalFilename());
				ilinfo.setxRef_IL_name(xrefTemplateFile.getOriginalFilename());
				clientData.setIlInfo(ilinfo);
				etlAdminService.updateILAndXrefTemplateInfo(clientData, clientAppDbJdbcTemplate);
			}

			List<Map<String, String>> templates = etlAdminService.getTemplate(clientAppDbJdbcTemplate);
			dataResponse.setObject(templates);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{

			if( iLTempFile != null )
			{
				iLTempFile.delete();
			}
			if( xrefTempFile != null )
			{
				xrefTempFile.delete();
			}
			if( iLFile != null )
			{
				iLFile.delete();
			}
			if( xrefFile != null )
			{
				xrefFile.delete();
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getTopErrorLog", method = RequestMethod.GET)
	public DataResponse getTopErrorLog(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{

		List<ErrorLog> errorLogs = null;
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			errorLogs = etlAdminService.getTopErrorLog(clientAppDbJdbcTemplate);
			message.setCode(messageSource.getMessage("anvizent.message.suscess.code", null, locale));
			dataResponse.setObject(errorLogs);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientErrorLogById", method = RequestMethod.POST)
	public DataResponse getClientErrorLogById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ErrorLog errorLog, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();

		int id = Integer.parseInt(errorLog.getId());
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ErrorLog errorLog1 = etlAdminService.getClientErrorLogById(id, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
			dataResponse.setObject(errorLog1);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/searchClientErrorLog", method = RequestMethod.POST)
	public DataResponse searchClientErrorLog(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ErrorLog errorLog, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ErrorLog> errorLogList = etlAdminService.searchClientErrorLog(errorLog, clientAppDbJdbcTemplate);
			if( errorLogList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(errorLogList);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noErrorsFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/createWebService", method = RequestMethod.POST)
	public DataResponse createWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webservice, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			webservice.setModification(modification);
			int webserviceList = etlAdminService.createWebService(webservice, clientAppDbJdbcTemplate);
			if( webserviceList != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(webserviceList);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveWebservice", null, locale));
			}
			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/createNewVertical", method = RequestMethod.POST)
	public DataResponse createNewVertical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Industry industry, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			industry.setModification(modification);
			int create = etlAdminService.createNewVertical(industry, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.verticalCreatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateVertical", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.verticalNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/getExistingVerticals", method = RequestMethod.GET)
	public DataResponse getExistingVerticals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Industry> industries = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			industries = etlAdminService.getExistingVerticals(clientAppDbJdbcTemplate);
			if( industries != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(industries);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noVerticalFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getActiveVerticals", method = RequestMethod.GET)
	public DataResponse getActiveVerticals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Industry> industries = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			industries = etlAdminService.getExistingVerticals(clientAppDbJdbcTemplate);
			if( industries != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(industries);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noVerticalFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getVerticalDetailsById", method = RequestMethod.POST)
	public DataResponse getVerticalDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Industry industry, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Industry industryDetails = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			industryDetails = etlAdminService.getVerticalDetailsById(industry.getId(), clientAppDbJdbcTemplate);
			if( industryDetails != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(industryDetails);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.verticalDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/updateVerticalById", method = RequestMethod.POST)
	public DataResponse updateVerticalById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Industry industry, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			industry.setModification(modification);
			int update = etlAdminService.updateVerticalById(industry, clientAppDbJdbcTemplate);
			if( update != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.verticalUpdatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdateVertical", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.verticalNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getExistingkpis", method = RequestMethod.GET)
	public DataResponse getExistingkpis(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Kpi> kpis = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			kpis = etlAdminService.getExistingkpis(clientAppDbJdbcTemplate);
			if( kpis != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(kpis);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noKpiFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createNewKpi", method = RequestMethod.POST)
	public DataResponse createNewKpi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Kpi kpi, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			kpi.setModification(modification);
			int create = etlAdminService.createNewKpi(kpi, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.kpiCreatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.KpiCreationFailed", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.kpiNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getKpiDetailsById", method = RequestMethod.POST)
	public DataResponse getKpiDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Kpi kpi, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Kpi kpiDetails = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			kpiDetails = etlAdminService.getKpiDetailsById(kpi.getKpiId(), clientAppDbJdbcTemplate);
			if( kpiDetails != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(kpiDetails);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.kpiDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateKpiById", method = RequestMethod.POST)
	public DataResponse updateKpiById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Kpi kpi, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			kpi.setModification(modification);
			int update = etlAdminService.updateKpiById(kpi, clientAppDbJdbcTemplate);
			if( update != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.kpiUpdatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdateKpi", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.kpiNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getExistingILsInfo", method = RequestMethod.GET)
	public DataResponse getExistingILsInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ILInfo> ilInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilInfo = etlAdminService.getExistingILsInfo(clientAppDbJdbcTemplate);
			if( ilInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(ilInfo);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noILFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getILInfoById", method = RequestMethod.POST)
	public DataResponse getILInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("iLId") String iLId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		ETLAdmin etlAdmin = new ETLAdmin();
		ILInfo ilInfo = null;
		List<ETLJobContextParam> contextparams = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int ilId = Integer.parseInt(iLId);
			ilInfo = etlAdminService.getILInfoById(ilId, clientAppDbJdbcTemplate);

			if( ilInfo == null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.iLDetailsNotFound", null, locale));
			}

			if( ilInfo.getJobExecutionType().equals("T") )
			{
				contextparams = etlAdminService.getContextParamsByILId(ilId, clientAppDbJdbcTemplate);
				etlAdmin.seteTLJobContextParamList(contextparams);
				dataResponse.setObject(etlAdmin);
				String jobFileNames = etlAdminService.getJobFilesByILId(ilId, clientAppDbJdbcTemplate);
				String[] fileNames = null;
				List<String> jobFileNamesList = new ArrayList<>();
				if( jobFileNames != null )
				{
					fileNames = jobFileNames.split(",");
					for (String s : fileNames)
					{
						jobFileNamesList.add(s);
					}
				}
				etlAdmin.setFileNames(jobFileNamesList);
				ilInfo.setJobFileNames(jobFileNames);
			}
			;

			etlAdmin.setIlInfo(ilInfo);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(etlAdmin);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientSpecificILInfoById", method = RequestMethod.POST)
	public DataResponse getClientSpecificILInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("iLId") String iLId, @RequestParam("client_id") String client_id, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ILInfo ilInfo = new ILInfo();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int ilId = Integer.parseInt(iLId);
			int clientid = Integer.parseInt(client_id);
			ilInfo = etlAdminService.getClientSpecificILInfoById(ilId, clientid, clientAppDbJdbcTemplate);
			dataResponse.setObject(ilInfo);
			if( ilInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.iLDetailsNotFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateILDetailsById", method = RequestMethod.POST)
	public DataResponse updateILDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			eTLAdmin.getIlInfo().setModification(modification);
			eTLAdmin.setModification(modification);

			int updateILInfo = etlAdminService.updateILDetailsById(eTLAdmin.getIlInfo(), clientAppDbJdbcTemplate);
			/*
			 * etlAdminService.deleteILJobFileInfo(eTLAdmin.getIlInfo().getiL_id
			 * ()); saveJobFiles = etlAdminService.saveILJobFileInfo(eTLAdmin);
			 */
			if( eTLAdmin.getIlInfo().getJobExecutionType().equals("T") )
			{
				if( updateILInfo > 0 )
				{
					etlAdminService.deleteILContextParams(eTLAdmin.getIlInfo().getiL_id(), clientAppDbJdbcTemplate);
					etlAdminService.saveEtlIlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
				}
			}

			if( updateILInfo > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.iLUpdatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdateILInfo", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.iLNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/createNewIL", method = RequestMethod.POST)
	public DataResponse createNewIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveContextParams = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			eTLAdmin.getIlInfo().setModification(modification);
			eTLAdmin.setModification(modification);

			int createIlId = etlAdminService.saveIlInfo(eTLAdmin, clientAppDbJdbcTemplate);
			eTLAdmin.getIlInfo().setiL_id(createIlId);
			if( eTLAdmin.getIlInfo().getJobExecutionType().equals("T") )
			{
				if( createIlId != 0 )
				{
				  //saveJobFiles =  etlAdminService.saveILJobFileInfo(eTLAdmin);
				  saveContextParams =  etlAdminService.saveEtlIlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
				}
			}

			if( createIlId > 0 || saveContextParams > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.IlCreatedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingILInfo", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.iLNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getVerticalsByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getVerticalsByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Industry> verticals = etlAdminService.getVerticalsByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(verticals);
			if( verticals != null && verticals.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingVerticals", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientVerticalMapping", method = RequestMethod.POST)
	public DataResponse saveClientVerticalMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			clientData.setModification(modification);
			int update = 0;
			etlAdminService.deleteClientVerticalMappingById(Integer.parseInt(clientData.getUserId()), clientAppDbJdbcTemplate);
			// if(delete > 0){
			update = etlAdminService.saveClientVerticalMapping(clientData, clientAppDbJdbcTemplate);
			etlAdminService.deleteClientVerticalMappedDLsByClientId(Integer.parseInt(clientData.getUserId()), clientAppDbJdbcTemplate);
			// }
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientVerticalMappingSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientVerticalMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllDatabases", method = RequestMethod.GET)
	public DataResponse getAllDatabases(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Database> databases = etlAdminService.getAllDatabases(clientAppDbJdbcTemplate);
			dataResponse.setObject(databases);
			if( databases != null && databases.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDatabases", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDatabasesByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getDatabasesByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Database> databases = etlAdminService.getDatabasesByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(databases);
			if( databases != null && databases.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDatabases", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getClientsByDatabaseId/{databaseId}", method = RequestMethod.GET)
	public DataResponse getClientsByDatabaseId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("databaseId") String database_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Integer> clients = etlAdminService.getClientsByDatabaseId(Integer.parseInt(database_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(clients);
			if( clients != null && clients.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingClients", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientDatabaseMapping", method = RequestMethod.POST)
	public DataResponse saveClientDatabaseMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("client_Id") String client_Id, @RequestParam("databaseId") List<String> databaseId, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			String columnName = "client_id";
			int update = 0;
			etlAdminService.deleteClientDatabaseMappingById(Integer.parseInt(client_Id), columnName, clientAppDbJdbcTemplate);
			// if(delete > 0){
			update = etlAdminService.saveClientDatabaseMapping(Integer.parseInt(client_Id), databaseId, modification, clientAppDbJdbcTemplate);
			// int deleteClientConnectorMapping
			etlAdminService.deleteClientConnectorMapping(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			// }
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientDatabaseMappingSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientDatabaseMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveDatabaseClientMapping", method = RequestMethod.POST)
	public DataResponse saveDatabaseClientMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("database_Id") String database_Id, @RequestParam("clientId") List<String> clientIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			String columnName = "database_id";
			int update = 0;
			// int delete =
			etlAdminService.deleteClientDatabaseMappingById(Integer.parseInt(database_Id), columnName, clientAppDbJdbcTemplate);
			// if(delete > 0){
			update = etlAdminService.saveDatabaseClientMapping(Integer.parseInt(database_Id), clientIds, modification, clientAppDbJdbcTemplate);
			// }
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.databaseClientMappingSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientDatabaseMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDatabaseMappedConnectors/{client_Id}", method = RequestMethod.GET)
	public DataResponse getDatabaseMappedConnectors(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Database> databases = etlAdminService.getDatabaseMappedConnectors(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(databases);
			if( databases != null && databases.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingConnectors", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientConnectorMapping", method = RequestMethod.POST)
	public DataResponse saveClientConnectorMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("client_Id") String client_Id, @RequestParam("connectorId") List<String> connectorIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			int update = 0;
			etlAdminService.deleteClientConnectorMappingById(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			update = etlAdminService.saveClientConnectorMapping(Integer.parseInt(client_Id), connectorIds, modification, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientConnectorMappingSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientConnectorMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getConnectorsByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getConnectorsByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<Database> databases = etlAdminService.getConnectorsByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(databases);
			if( databases != null && databases.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingConnectors", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getILInfoByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getILInfoByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ILInfo> ilInfo = etlAdminService.getILInfoByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(ilInfo);
			if( ilInfo != null && ilInfo.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingILs", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveclientSpecificIL", method = RequestMethod.POST)
	public DataResponse saveclientSpecificIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			clientData.setModification(modification);
			int insert = 0;
			insert = etlAdminService.saveClientSpecificIL(clientData, clientAppDbJdbcTemplate);
			if( insert > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificILInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificILInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateclientSpecificIL", method = RequestMethod.POST)
	public DataResponse updateclientSpecificIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			clientData.setModification(modification);
			int update = 0;
			update = etlAdminService.updateClientSpecificIL(clientData, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificILInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificILInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateclientSpecificILToDefault", method = RequestMethod.POST)
	public DataResponse updateclientSpecificIToDefault(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			clientData.setModification(modification);
			int update = 0;
			update = etlAdminService.updateClientSpecificILToDefault(clientData, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificILInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificILInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDLInfoByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getDLInfoByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<DLInfo> dlInfo = etlAdminService.getDLInfoByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(dlInfo);
			if( dlInfo != null && dlInfo.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDLs", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getClientSpecificDLInfoById", method = RequestMethod.POST)
	public DataResponse getClientSpecificDLInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("dLId") String dL_Id, @RequestParam("client_id") String client_id, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		DLInfo dlInfo = new DLInfo();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int dLId = Integer.parseInt(dL_Id);
			int clientid = Integer.parseInt(client_id);
			dlInfo = etlAdminService.getClientSpecificDLInfoById(dLId, clientid, clientAppDbJdbcTemplate);
			dataResponse.setObject(dlInfo);
			if( dlInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dLDetailsNotFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientSpecificDLInfo", method = RequestMethod.POST)
	public DataResponse saveclientSpecificDLInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			clientData.setModification(modification);
			int insert = 0;
			insert = etlAdminService.saveClientSpecificDLInfo(clientData, clientAppDbJdbcTemplate);
			if( insert > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificDLInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificDLInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateClientSpecificDLInfo", method = RequestMethod.POST)
	public DataResponse updateClientSpecificDLInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			clientData.setModification(modification);
			int update = 0;
			update = etlAdminService.updateClientSpecificDLInfo(clientData, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificDLInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificDLInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateclientSpecificDLToDefault", method = RequestMethod.POST)
	public DataResponse updateclientSpecificDLToDefault(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			clientData.setModification(modification);
			int update = 0;
			update = etlAdminService.updateClientSpecificDLToDefault(clientData, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSpecificDLInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificDLInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllmappingInfoById/{client_Id}", method = RequestMethod.GET)
	public DataResponse getAllmappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") int client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			AllMappingInfo allMappingInfo = etlAdminService.getAllmappingInfoById(client_Id, clientAppDbJdbcTemplate, commonJdbcTemplate);
			dataResponse.setObject(allMappingInfo);
			if( allMappingInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAllMappingInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCommonJobInfo", method = RequestMethod.GET)
	public DataResponse getCommonJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<CommonJob> commonJobInfo = etlAdminService.getCommonJobInfo(clientAppDbJdbcTemplate);
			dataResponse.setObject(commonJobInfo);
			if( commonJobInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingCommonJobInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveCommonJobInfo", method = RequestMethod.POST)
	public DataResponse saveCommonJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody CommonJob commonJob, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			commonJob.setModificaion(modification);

			int save = etlAdminService.saveCommonJobInfo(commonJob, clientAppDbJdbcTemplate);
			if( save > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.commonJobInfoSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingCommonJobInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCommonJobInfoById/{id}", method = RequestMethod.GET)
	public DataResponse getCommonJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") int id, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			CommonJob commonJobInfo = etlAdminService.getCommonJobInfoById(id, clientAppDbJdbcTemplate);
			dataResponse.setObject(commonJobInfo);
			if( commonJobInfo != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingCommonJobInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateCommonJobInfo", method = RequestMethod.POST)
	public DataResponse updateCommonJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody CommonJob commonJob, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			commonJob.setModificaion(modification);

			int update = etlAdminService.updateCommonJobInfo(commonJob, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.commonJobInfoUpdatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingCommonJobInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllWebServices", method = RequestMethod.GET)
	public DataResponse getAllWebServices(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<Integer, String> allWebservices = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			allWebservices = etlAdminService.getAllWebServices(clientAppDbJdbcTemplate);
			if( allWebservices != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allWebservices);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.WebserviceNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getWebServiceAuthDetailsById", method = RequestMethod.POST)
	public DataResponse getWebServiceAuthDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		WebService allWebservices = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			allWebservices = etlAdminService.getWebServiceAuthDetailsById(Integer.parseInt(webService.getWeb_service_id()), clientAppDbJdbcTemplate);
			if( allWebservices != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allWebservices);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.WebserviceNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/updateWebServicesById", method = RequestMethod.POST)
	public DataResponse updateWebServicesById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			webService.setModification(modification);
			etlAdminService.updateWebServicesById(webService, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveWsILMappingDetails", method = RequestMethod.POST)
	public DataResponse saveWsILMappingDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceILMapping webServiceILMapping, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int update = 0, save = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			webServiceILMapping.setModification(modification);

			List<WebServiceApi> updateDetails = new ArrayList<>();
			List<WebServiceApi> newlyAddedDetails = new ArrayList<>();
			if( webServiceILMapping.getWebServiceApis() != null && webServiceILMapping.getWebServiceApis().size() > 0 )
			{
				for (WebServiceApi wsApi : webServiceILMapping.getWebServiceApis())
				{
					if( wsApi.getId() != null )
					{
						updateDetails.add(wsApi);
					}
					else
					{
						newlyAddedDetails.add(wsApi);
					}
				}
				// update exsisting WS IL Api mappings
				if( updateDetails != null && updateDetails.size() > 0 )
				{
					webServiceILMapping.setWebServiceApis(updateDetails);
					update = etlAdminService.updateWsILMappingDetails(webServiceILMapping, clientAppDbJdbcTemplate);
				}

				// insert newly added WS IL Api mappings
				if( newlyAddedDetails != null && newlyAddedDetails.size() > 0 )
				{
					webServiceILMapping.setWebServiceApis(newlyAddedDetails);
					save = etlAdminService.saveWsILMappingDetails(webServiceILMapping, clientAppDbJdbcTemplate);
				}

				if( update > 0 || save > 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.webServiceILMappingsSavedSuccessfully", null, locale));
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingWebserviceILMappings", null, locale));
				}
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingWebserviceILMappings", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			LOGGER.error("error while saveWsIlMappingDetails() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.apiNameAlreadyExists", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getWSILMappingDetailsById", method = RequestMethod.POST)
	public DataResponse getWSILMappingDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("wsTemplateId") int wsTemplateId, @RequestParam("iLId") int iLId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<WebServiceApi> mappingDetails = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			mappingDetails = etlAdminService.getWSILMappingDetailsById(wsTemplateId, iLId, clientAppDbJdbcTemplate);
			if( mappingDetails != null && mappingDetails.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWsIlmappings", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		dataResponse.setObject(mappingDetails);
		return dataResponse;
	}

	@RequestMapping(value = "/deleteWSILMappingDetailsById", method = RequestMethod.POST)
	public DataResponse deleteWSILMappingDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("id") int id, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int delete = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			delete = etlAdminService.deleteWSILMappingDetailsById(id, clientAppDbJdbcTemplate);
			if( delete > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.webServiceILMappingDeletedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhiledeletingWebserviceILMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getWebserviceClients", method = RequestMethod.POST)
	public DataResponse getWebserviceClients(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> wbserviceClients = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( webService.getWeb_service_id() != null )
			{
				int webserviceId = Integer.parseInt(webService.getWeb_service_id());
				wbserviceClients = etlAdminService.getWebserviceClients(webserviceId, clientAppDbJdbcTemplate);
				if( wbserviceClients != null )
				{
					dataResponse.setObject(wbserviceClients);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.NoClientsMapping", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveWebserviceClientMapping", method = RequestMethod.POST)
	public DataResponse saveWebserviceClientMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestParam("webserviceId") Integer webserviceId, @RequestParam("checkedClients") String checkedClients)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveCount = -1;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( webserviceId != null )
			{
				ETLAdmin eTLAdmin = new ETLAdmin();
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				eTLAdmin.setModification(modification);
				etlAdminService.deleteWebserviceClientMapping(webserviceId, clientAppDbJdbcTemplate);
				saveCount = etlAdminService.saveWebserviceClientMapping(webserviceId, checkedClients, eTLAdmin, clientAppDbJdbcTemplate);
			}
			if( saveCount > 0 )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.webServiceClientMappingSuccessfullyUpdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.webServiceClientMappingNotSuccessfullyUpdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getAllKpis", method = RequestMethod.GET)
	public DataResponse getAllWebKPIs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		Map<Integer, String> allWebservices = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			allWebservices = etlAdminService.getAllKpis(clientAppDbJdbcTemplate);
			if( allWebservices != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(allWebservices);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.kpiNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getKpiListByDlId", method = RequestMethod.POST)
	public DataResponse getKpiListByDlId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DLInfo dLInfo, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> dLInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( dLInfo != null )
			{
				dLInfoList = etlAdminService.getKpiListByDlId(dLInfo.getdL_id(), clientAppDbJdbcTemplate);
				if( dLInfoList != null )
				{
					dataResponse.setObject(dLInfoList);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.NoClientsMapping", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveDlKpiMapping", method = RequestMethod.POST)
	public DataResponse saveDlKpiMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestParam("dlId") Integer dlId, @RequestParam("checkedKpis") String checkedKpis, @RequestParam("checkedIds") String checkedKpiIds)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveCount = -1;

		List<Kpi> kpiLists = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ETLAdmin eTLAdmin = new ETLAdmin();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eTLAdmin.setModification(modification);
			List<String> kpiIdList = new ArrayList<String>(Arrays.asList(checkedKpiIds.split(",")));
			for (String kpiid : kpiIdList)
			{
				Kpi kpi = new Kpi();
				kpi.setKpiId(Integer.parseInt(kpiid));
				kpiLists.add(kpi);
			}
			eTLAdmin.setKpiInfo(kpiLists);
			etlAdminService.deleteDlKpiMapping(dlId, clientAppDbJdbcTemplate);
			saveCount = etlAdminService.saveDlKpiMapping(dlId, checkedKpiIds, eTLAdmin, clientAppDbJdbcTemplate);

			if( saveCount > 0 )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dlKpiMappingsuccessfullyupdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dlKpiMappingNotsuccessfullyupdated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDefaultQuery", method = RequestMethod.POST)
	public DataResponse getDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		List<ILConnectionMapping> defaultQueriesList = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			defaultQueriesList = etlAdminService.getDefaultQuery(databaseTypeId, clientAppDbJdbcTemplate);
			if( defaultQueriesList != null )
			{
				dataResponse.setObject(defaultQueriesList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDefaultQueriesList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getIlQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse getIlQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String ilQuery = etlAdminService.getIlQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
			if( ilQuery != null )
			{
				dataResponse.setObject(ilQuery);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveILQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getIlincrementalQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse getIlincrementalQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String ilincrQuery = etlAdminService.getIlincrementalQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
			if( ilincrQuery != null )
			{
				dataResponse.setObject(ilincrQuery);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveIncrementalUpdateQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/gethistoryLoadById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse gethistoryLoadById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String ilQuery = etlAdminService.gethistoryLoadById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
			if( ilQuery != null )
			{
				dataResponse.setObject(ilQuery);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveHistoryLoad", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/geMaxDateQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse geMaxDateQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String ilQuery = etlAdminService.geMaxDateQueryById(ilId, databaseTypeId, clientAppDbJdbcTemplate);
			if( ilQuery != null )
			{
				dataResponse.setObject(ilQuery);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveMaxDateQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getNotMappedILsByDBTypeId", method = RequestMethod.POST)
	public DataResponse getNotMappedILs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request)
	{

		Map<Integer, String> notMappedIlList = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			notMappedIlList = etlAdminService.getNotMappedILsByDBTypeId(databaseTypeId, clientAppDbJdbcTemplate);
			if( notMappedIlList != null )
			{
				dataResponse.setObject(notMappedIlList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetNotMappedILs", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveILDefaultQuery", method = RequestMethod.POST)
	public DataResponse saveILDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("iLId") Integer iLId, @RequestParam("id") Integer id, @RequestParam("iLquery") String iLquery, @RequestParam("iLIncrementalUpdateQuery") String iLIncrementalUpdateQuery,
			@RequestParam("historicalLoad") String historicalLoad, @RequestParam("maxDateQuery") String maxDateQuery, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			iLConnectionMapping.setModification(modification);
			iLConnectionMapping.setiLquery(iLquery);
			iLConnectionMapping.setiLIncrementalUpdateQuery(iLIncrementalUpdateQuery);
			iLConnectionMapping.setHistoricalLoad(historicalLoad);
			iLConnectionMapping.setMaxDateQuery(maxDateQuery);
			iLConnectionMapping.setiLId(iLId);

			iLConnectionMapping.setiLConnection(new ILConnection());
			iLConnectionMapping.getiLConnection().setDatabase(new Database());

			iLConnectionMapping.getiLConnection().getDatabase().setId(id);

			int save = etlAdminService.saveILDefaultQuery(iLConnectionMapping, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
				messages.add(message);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateDefaultQuery", null, locale));
				messages.add(message);
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.alreadyExist", null, locale));
			messages.add(message);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/editILDefaultQuery", method = RequestMethod.POST)
	public DataResponse editILDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("ilid") Integer ilid, @RequestParam("databaseTypeId") Integer databaseTypeId, HttpServletRequest request, Locale locale)
	{
		ILConnectionMapping ilQueries = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ilQueries = etlAdminService.editILDefaultQuery(ilid, databaseTypeId, clientAppDbJdbcTemplate);
			if( ilQueries != null )
			{
				dataResponse.setObject(ilQueries);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.defaultQueryDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/updateILDefaultQuery", method = RequestMethod.POST)
	public DataResponse updateILDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping ilconnectionMapping, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int ilQueriesUpdate;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			ilconnectionMapping.setModification(modification);

			ilQueriesUpdate = etlAdminService.updateILDefaultQuery(ilconnectionMapping, clientAppDbJdbcTemplate);
			List<ILConnectionMapping> defaultQueriesList = etlAdminService.getDefaultQuery(ilconnectionMapping.getiLConnection().getDatabase().getId(), clientAppDbJdbcTemplate);
			dataResponse.setObject(defaultQueriesList);
			if( ilQueriesUpdate != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.unableToUpdateDefaultQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDatabase", method = RequestMethod.GET)
	public DataResponse getDatabase(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Database> databaseList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			databaseList = etlAdminService.getDatabase(clientAppDbJdbcTemplate);
			if( databaseList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(databaseList);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDatabaseList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDBDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getDatabaseById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") Integer id, Locale locale, HttpServletRequest request)
	{

		Database databaseConnector = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			databaseConnector = etlAdminService.getDBDetailsById(id, clientAppDbJdbcTemplate);
			if( databaseConnector != null )
			{
				dataResponse.setObject(databaseConnector);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.databaseDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getconnector", method = RequestMethod.GET)
	public DataResponse getconnector(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Database> connectorList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			connectorList = etlAdminService.getconnector(clientAppDbJdbcTemplate);
			if( connectorList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(connectorList);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getConnectorDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getConnectorDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") Integer id, Locale locale, HttpServletRequest request)
	{

		Database connector = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			connector = etlAdminService.getConnectorDetailsById(id, clientAppDbJdbcTemplate);
			if( connector != null )
			{
				dataResponse.setObject(connector);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.connectorDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateConnector", method = RequestMethod.POST)
	public DataResponse updateConnector(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int connectorUpdate;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			database.setModification(modification);
			connectorUpdate = etlAdminService.updateConnector(database, clientAppDbJdbcTemplate);
			if( connectorUpdate != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.unableToUpdateConnector", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/createConnector", method = RequestMethod.POST)
	public DataResponse createConnector(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			database.setModification(modification);
			int save = etlAdminService.createConnector(database, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateConnector", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.ConnectorNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScripts", method = RequestMethod.GET)
	public DataResponse getTableScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScriptsForm> tableScriptsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			tableScriptsList = etlAdminService.getTableScripts(clientAppDbJdbcTemplate);
			if( tableScriptsList != null )
			{
				dataResponse.setObject(tableScriptsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveTableScripts", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/addOrUpdateTableScripts", method = RequestMethod.POST)
	public DataResponse addOrUpdateTableScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveOrUpdateCount = -1;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId);
				tableScripts.setModification(modification);
				saveOrUpdateCount = etlAdminService.addTableScripts(tableScripts, clientAppDbJdbcTemplate);
				if( tableScripts.getIs_Update() )
				{
					saveOrUpdateCount = etlAdminService.updateTableScripts(tableScripts, clientAppDbJdbcTemplate);
				}

			}

			if( saveOrUpdateCount > 0 )
			{
				dataResponse.setObject(saveOrUpdateCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptAddedOrUpdatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotAddedOrUpdatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createDlMaster", method = RequestMethod.POST)
	public DataResponse createDlMaster(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int createdlId = -1;
		int dlMappingCount = -1;

		int kpiMappingCount = -1;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( eTLAdmin != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				eTLAdmin.setModification(modification);
				createdlId = etlAdminService.saveDlInfo(eTLAdmin, clientAppDbJdbcTemplate);
				DLInfo dlinfo = new DLInfo();
				dlinfo.setdL_id(createdlId);
				eTLAdmin.getDlInfo().setdL_id(createdlId);
				;
				eTLAdmin.setDlId(createdlId);
				if( createdlId > 0 )
				{
					dlMappingCount = etlAdminService.saveEtlDlIlMapping(eTLAdmin, clientAppDbJdbcTemplate);
				}
				if( dlMappingCount > 0 )
				{
					kpiMappingCount = etlAdminService.saveDlKpiMapping(createdlId, "", eTLAdmin, clientAppDbJdbcTemplate);
				}

				if( eTLAdmin.getDlInfo().getJobExecutionType().equals("T") )
				{
					if( kpiMappingCount > 0 )
					{
						etlAdminService.saveEtlDlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
					}
				}
			}
			if( kpiMappingCount > 0 )
			{
				dataResponse.setObject(dlMappingCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.dlMastercreatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateDlMaster", method = RequestMethod.POST)
	public DataResponse updateDlMaster(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int dlModifiedCount = -1;
		// int dlMappingCount = -1;

		int ilMappingCount = -1;
		int kpiMappingCount = -1;
		int contextparamMappingCount = -1;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( eTLAdmin != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				eTLAdmin.setModification(modification);
				eTLAdmin.setDlId(eTLAdmin.getDlInfo().getdL_id());
				dlModifiedCount = etlAdminService.updateDLInfo(eTLAdmin, clientAppDbJdbcTemplate);

				if( dlModifiedCount > 0 )
				{
					etlAdminService.deleteEtlDlIlMappingByDlId(eTLAdmin, clientAppDbJdbcTemplate);
					ilMappingCount = etlAdminService.saveEtlDlIlMapping(eTLAdmin, clientAppDbJdbcTemplate);

				}
				if( ilMappingCount > 0 )
				{
					etlAdminService.deleteDlKpiMapping(eTLAdmin.getDlInfo().getdL_id(), clientAppDbJdbcTemplate);
					kpiMappingCount = etlAdminService.saveDlKpiMapping(eTLAdmin.getDlInfo().getdL_id(), "", eTLAdmin, clientAppDbJdbcTemplate);
				}
				if( eTLAdmin.getDlInfo().getJobExecutionType().equals("T") )
				{
					if( kpiMappingCount > 0 )
					{
						etlAdminService.deleteDLContextParams(eTLAdmin.getDlInfo().getdL_id(), clientAppDbJdbcTemplate);
						contextparamMappingCount = etlAdminService.saveEtlDlContextParams(eTLAdmin, clientAppDbJdbcTemplate);
					}
				}

				/*
				 * if (contextparamMappingCount > 0) { if
				 * (!eTLAdmin.getFileNames().isEmpty() &&
				 * eTLAdmin.getFileNames() != null) { for (String fileName :
				 * eTLAdmin.getFileNames()) { if (!fileName.isEmpty())
				 * etlAdminService.deleteEtlDlJobsmapping(eTLAdmin.getDlInfo().
				 * getdL_id(), fileName); } dlMappingCount =
				 * etlAdminService.saveEtlDlJobsmapping(eTLAdmin); }
				 * 
				 * }
				 */
			}
			if( kpiMappingCount > 0 )
			{
				dataResponse.setObject(contextparamMappingCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.dlMasterCreationOrUpdationSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dlMasterCreationOrUpdationFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScriptsByClient", method = RequestMethod.POST)
	public DataResponse getTableScriptsByClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScripts> tableScriptsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			tableScriptsList = etlAdminService.getTableScriptsByClient(tableScripts.getClientId(), clientAppDbJdbcTemplate);

			if( tableScriptsList != null )
			{
				dataResponse.setObject(tableScriptsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dataNotFoundForTheClient", null, locale) + tableScripts.getClientId());
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/addTableScriptsMapping", method = RequestMethod.POST)
	public DataResponse addTableScriptsMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveCount = -1;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				tableScripts.setModification(modification);
				saveCount = etlAdminService.addTableScriptsMapping(tableScripts, clientAppDbJdbcTemplate);

			}
			if( saveCount > 0 )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptAddedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotAddedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScriptsMappingById", method = RequestMethod.POST)
	public DataResponse getTableScriptsMappingById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		TableScriptsForm tableScript = null;
		int executedCount = 0;
		// int mappedCount = 0;
		try
		{
			JdbcTemplate clientAppDbJdbcTemplate = null;
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{

				// executedCount =
				// etlAdminService.getExecutedtableScriptCount(tableScripts.getId());
				// mappedCount =
				// etlAdminService.getMappedTableScriptCount(tableScripts.getId());

				if( executedCount > 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.thiscriptisalreadyexecutedPleaseaddnewscript", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}
				/*
				 * if(mappedCount > 0){
				 * message.setCode(messageSource.getMessage(
				 * "anvizent.message.error.code", null, locale));
				 * message.setText("This script is alerady mapped to "
				 * +mappedCount+
				 * " clients. Please remove All Mappings to edit this script.");
				 * messages.add(message); dataResponse.setMessages(messages);
				 * return dataResponse;
				 * 
				 * }
				 */

			}
			tableScript = etlAdminService.getTableScriptsMappingById(tableScripts, clientAppDbJdbcTemplate);
			if( tableScript != null )
			{
				dataResponse.setObject(tableScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScriptViewById", method = RequestMethod.POST)
	public DataResponse getTableScriptViewById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		TableScriptsForm tableScript = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			tableScript = etlAdminService.getTableScriptsMappingById(tableScripts, clientAppDbJdbcTemplate);
			if( tableScript != null )
			{
				dataResponse.setObject(tableScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateTableScriptsMapping", method = RequestMethod.POST)
	public DataResponse updateTableScriptsMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int saveCount = -1;

		int executedCount = 0;
		int mappedCount = 0;
		try
		{
			JdbcTemplate clientAppDbJdbcTemplate = null;
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{

				// userObjectRemoved

				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				tableScripts.setModification(modification);

				if( executedCount > 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.thiscriptisalreadyexecutedPleaseaddnewscript", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}

				if( mappedCount > 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.thisScriptIsAlreadyMappedRemoveMappingsToEdit", new Object[] { mappedCount }, locale));

					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;

				}

				saveCount = etlAdminService.updateScriptHistoryTable(tableScripts.getId(), clientAppDbJdbcTemplate);
				saveCount = etlAdminService.updateTableScriptsMapping(tableScripts, clientAppDbJdbcTemplate);
			}
			if( saveCount > 0 )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptUpdatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotUpdatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateDatabase", method = RequestMethod.POST)
	public DataResponse updateDatabase(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int databaseUpdate;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			database.setModification(modification);
			databaseUpdate = etlAdminService.updateDatabase(database, clientAppDbJdbcTemplate);
			if( databaseUpdate != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.updatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.unableToUpdateDatabase", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.databaseNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/createDB", method = RequestMethod.POST)
	public DataResponse createDB(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		// userObjectRemoved
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			database.setModification(modification);
			int save = etlAdminService.createDB(database, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateDatabase", null, locale));
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.databaseNameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDatabasesTypes", method = RequestMethod.GET)
	public DataResponse getDatabaseTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		List<Database> databaseTypes = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Message message = new Message();
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			databaseTypes = packageService.getDatabaseTypes(clientAppDbJdbcTemplate);
			// userObjectRemoved

			dataResponse.setObject(databaseTypes);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveClientTableScriptsMapping", method = RequestMethod.POST)
	public DataResponse saveClientTableScriptsMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int saveCount = -1;

		int delete = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{
				// userObjectRemoved
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				tableScripts.setModification(modification);
				delete = etlAdminService.deleteClientTableScriptsMapping(tableScripts.getClientId(), clientAppDbJdbcTemplate, commonJdbcTemplate);
				saveCount = etlAdminService.saveClientTableScriptsMapping(tableScripts, clientAppDbJdbcTemplate, commonJdbcTemplate);
			}
			if( saveCount > 0 || (delete > 0 && saveCount == 0) )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptAddedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else if( delete == 0 && saveCount == 0 )
			{
				dataResponse.setObject(saveCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noTableScriptWasSelected", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotAddedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScriptsMappingByClient", method = RequestMethod.POST)
	public DataResponse getTableScriptsMappingByClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScripts> tableScriptsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			tableScriptsList = etlAdminService.getTableScriptsMappingByClient(tableScripts.getClientId(), clientAppDbJdbcTemplate);

			if( tableScriptsList != null )
			{
				dataResponse.setObject(tableScriptsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptsNotExecutedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/clientTableScriptsExecution", method = RequestMethod.POST)
	public DataResponse clientTableScriptsExecution(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScriptsForm)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScripts> tableScriptLists = new ArrayList<>();
		int count = 0;
		Connection con = null;
		JdbcTemplate clientMainJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientMainJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScriptsForm != null )
			{
				List<TableScripts> tableScriptList = tableScriptsForm.getTableScriptList();
				List<Integer> scriptIds = new ArrayList<>();
				for (TableScripts tableScript : tableScriptList)
				{
					if( tableScript.isCheckedScript() )
					{
						scriptIds.add(tableScript.getId());
					}
				}

				if( scriptIds.size() > 0 )
				{

					tableScriptLists = etlAdminService.getSqlScriptByScriptIds(tableScriptsForm.getClientId(), scriptIds, commonJdbcTemplate);

					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));

					for (TableScripts tableScript : tableScriptLists)
					{
						try
						{
							if( tableScript.getTargetSchema().equalsIgnoreCase("Main") )
							{
								con = clientMainJdbcTemplate.getDataSource().getConnection();
							}
							else
							{
								con = clientStagingJdbcTemplate.getDataSource().getConnection();
							}

							if( con != null )
							{
								ScriptRunner runner = new ScriptRunner(con, false, false);
								String fileName = CommonUtils.writeScriptFile(tableScript.getSqlScript());
								String errorMessages = runner.runScript(new BufferedReader(new FileReader(fileName)));
								System.out.println("errorMessages  -- > " + errorMessages);
								etlAdminService.updateTableScriptsMappingIsExecuted(tableScriptsForm.getClientId(), tableScript.getId(), true, modification, clientAppDbJdbcTemplate, commonJdbcTemplate);
								etlAdminService.updateTableScriptsMappingIsError(tableScriptsForm.getClientId(), tableScript.getId(), false, modification, clientAppDbJdbcTemplate, commonJdbcTemplate);
								count++;
							}

						}
						catch ( Throwable e )
						{
							LOGGER.error("", e);
							packageService.logError(e, request, clientAppDbJdbcTemplate);
							etlAdminService.updateTableScriptsMappingIsNotExecutedErrorMsg(tableScriptsForm.getClientId(), tableScript.getId(), e.getMessage(), modification, commonJdbcTemplate);
							etlAdminService.updateTableScriptsMappingIsError(tableScriptsForm.getClientId(), tableScript.getId(), true, modification, clientAppDbJdbcTemplate, commonJdbcTemplate);
						}
						finally
						{
							if( con != null && !con.isClosed() )
							{
								con.close();
							}
						}
					}

					if( count > 0 )
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}
					else
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptsNotExecutedSuccessfully", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
					}

				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptsNotExecutedSuccessfully", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			try
			{
				if( con != null && !con.isClosed() )
				{
					con.close();
				}
			}
			catch ( Exception e2 )
			{
			}
			CommonUtils.closeDataSource(clientMainJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getWebserviceByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getWebserviceByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("client_Id") String client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<WebService> webservices = etlAdminService.getWebserviceByClientId(Integer.parseInt(client_Id), clientAppDbJdbcTemplate);
			dataResponse.setObject(webservices);
			if( webservices != null && webservices.size() > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhilegetWebserviceByClientId", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientWebserviceMapping", method = RequestMethod.POST)
	public DataResponse saveClientWebserviceMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("client_Id") int client_Id, @RequestParam("webServiceIds") List<Integer> webServiceIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			int update = 0;
			etlAdminService.deleteClientWSMappings(client_Id, clientAppDbJdbcTemplate);
			update = etlAdminService.saveClientWebserviceMapping(client_Id, webServiceIds, modification, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientWebserviceMappingSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingClientWebserviceMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getTableScriptsMappingIsNotExecutedErrorMsg", method = RequestMethod.POST)
	public DataResponse getTableScriptsMappingIsNotExecutedErrorMsg(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody TableScriptsForm tableScriptsForm, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String errorMsg = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			errorMsg = etlAdminService.getTableScriptsMappingIsNotExecutedErrorMsg(tableScriptsForm.getClientId(), tableScriptsForm.getId(), clientAppDbJdbcTemplate);
			if( errorMsg != null )
			{
				dataResponse.setObject(errorMsg);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorFoundWhileGettingErrorMsgs", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveDefaultTemplateMasterMappingData", method = RequestMethod.POST)
	public DataResponse updateConnectorsAsDefault(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam(value = "masterIds") List<Integer> masterIds, @RequestParam("templateId") int templateId, @RequestParam("mappingType") String mappingType, HttpServletRequest request,
			Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		String messageText = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));

			etlAdminService.deleteDefaultTemplateMasterMappedData(templateId, mappingType, clientAppDbJdbcTemplate);
			int create = etlAdminService.saveDefaultTemplateMasterMappingData(templateId, mappingType, masterIds, modification, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				if( mappingType.equals("vertical") ) messageText = messageSource.getMessage("anvizent.message.success.text.defaultVerticalsSavedSuccessfully", null, locale);
				else if( mappingType.equals("connector") ) messageText = messageSource.getMessage("anvizent.message.success.text.defaultConnectorsSavedSuccessfully", null, locale);
				else if( mappingType.equals("dl") ) messageText = messageSource.getMessage("anvizent.message.success.text.defaultDLsSavedSuccessfully", null, locale);
				else if( mappingType.equals("tablescript") ) messageText = messageSource.getMessage("anvizent.message.success.text.defaultTablescriptsSavedSuccessfully", null, locale);
				else if( mappingType.equals("webservice") ) messageText = messageSource.getMessage("anvizent.message.success.text.defaultWebServicesSavedSuccessfully", null, locale);
				else if( mappingType.equals("s3Bucket") ) messageText = messageSource.getMessage("anvizent.message.success.text.s3BucketSavedSuccessfully", null, locale);
				else if( mappingType.equals("schedularMaster") ) messageText = messageSource.getMessage("anvizent.message.success.text.schedulerMasterSavedSuccessfully", null, locale);
				else messageText = messageSource.getMessage("anvizent.message.success.text.fileMultiPartSavedSuccessfully", null, locale);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				if( mappingType.equals("vertical") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultVerticals", null, locale);
				else if( mappingType.equals("connector") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultConnector", null, locale);
				else if( mappingType.equals("dl") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultDls", null, locale);
				else if( mappingType.equals("tablescript") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultTablescripts", null, locale);
				else if( mappingType.equals("webservice") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultWebServices", null, locale);
				else if( mappingType.equals("s3Bucket") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultS3Bucket", null, locale);
				else if( mappingType.equals("schedularMaster") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultSchedulerMasterClient", null, locale);
				else if( mappingType.equals("fileMultiPart") ) messageText = messageSource.getMessage("anvizent.message.error.text.errorWhileSavingDefaultFileMultiPart", null, locale);
				else messageText = messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		message.setText(messageText);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/clientTableScriptsInstantExecution", method = RequestMethod.POST)
	public DataResponse clientTableScriptsInstantExecution(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScriptsForm)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Connection con = null;
		String status = "success";
		String statusMsg = "";
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));

			try
			{
				if( !tableScriptsForm.getSchemaName().equalsIgnoreCase("Main") )
				{
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				}
				else
				{
					clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				}
				con = clientJdbcTemplate.getDataSource().getConnection();
				if( con != null )
				{
					ScriptRunner runner = new ScriptRunner(con, false, false);
					String fileName = CommonUtils.writeScriptFile(tableScriptsForm.getSqlScript());
					runner.runScript(new BufferedReader(new FileReader(fileName)));
					statusMsg = "Successfully executed";
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale));
				}

			}
			catch ( Exception e )
			{
				status = "failed";
				statusMsg = e.getLocalizedMessage();
				packageService.logError(e, request, clientAppDbJdbcTemplate);
				message.setCode(messageSource.getMessage("anvizent.message.failed.code", null, null));
				message.setText(messageSource.getMessage("anvizent.message.text.executionFailedWithBelowReason", null, locale) + e.getLocalizedMessage());
			}
			catch ( Throwable t )
			{
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			}
			finally
			{
				CommonUtils.closeConnection(con);
			}

			etlAdminService.updateInstantTableScriptExecution(tableScriptsForm, status, statusMsg, modification, clientAppDbJdbcTemplate);

			messages.add(message);
			dataResponse.setMessages(messages);

		}
		catch ( Exception e )
		{
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while clientTableScriptsInstantExecution() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.executionFailed", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			try
			{
				if( con != null && !con.isClosed() )
				{
					con.close();
				}
			}
			catch ( Exception e2 )
			{
			}
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getPreviousExecutedScripts", method = RequestMethod.POST)
	public DataResponse getPreviousExecutedScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TableScriptsForm> prevoiusExcutetedScriptList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			prevoiusExcutetedScriptList = etlAdminService.getPreviousExecutedScripts(tableScripts.getClientId(), clientAppDbJdbcTemplate);
			if( prevoiusExcutetedScriptList != null )
			{
				dataResponse.setObject(prevoiusExcutetedScriptList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrievePreviousExecutedTableScriptList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getInstantTableScriptsIsNotExecutedErrorMsg", method = RequestMethod.POST)
	public DataResponse getInstantTableScriptsIsNotExecutedErrorMsg(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody TableScriptsForm tableScriptsForm, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String errorMsg = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			errorMsg = etlAdminService.getInstantTableScriptsIsNotExecutedErrorMsg(tableScriptsForm.getId(), tableScriptsForm.getClientId(), clientAppDbJdbcTemplate);
			if( errorMsg != null )
			{
				dataResponse.setObject(errorMsg);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorFoundWhileGettingErrorMsgs", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPreviousTableScriptView", method = RequestMethod.POST)
	public DataResponse getPreviousTableScriptView(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String tableScript = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			tableScript = etlAdminService.getPreviousTableScriptView(tableScripts.getId(), tableScripts.getClientId(), clientAppDbJdbcTemplate);
			if( tableScript != null )
			{
				dataResponse.setObject(tableScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.tableScriptNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getAllDefaultTemplatesInfo", method = RequestMethod.GET)
	public DataResponse getAllDefaultTemplatesInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<DefaultTemplates> defaultTemplates = etlAdminService.getAllDefaultTemplatesInfo(clientAppDbJdbcTemplate);
			if( defaultTemplates != null && defaultTemplates.size() > 0 )
			{
				dataResponse.setObject(defaultTemplates);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.defaultTemplatesNotFound", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/createDefaultTemplate", method = RequestMethod.POST)
	public DataResponse createDefaultTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DefaultTemplates defaultTemplates, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			defaultTemplates.setModification(modification);

			int newTemplateId = etlAdminService.createDefaultTemplate(defaultTemplates, clientAppDbJdbcTemplate);
			if( newTemplateId != -1 )
			{
				dataResponse.setObject(newTemplateId);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.defaultTemplateCreatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.defaultTemplatesNotFound", null, locale));
			}

		}
		catch ( AnvizentDuplicateFileNameException av )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.defaultTemplateAlreadyExits", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateDefaultTemplate", method = RequestMethod.POST)
	public DataResponse updateDefaultTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DefaultTemplates defaultTemplates, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			modification.setModifiedDateTime(new Date());
			defaultTemplates.setModification(modification);

			int templateId = etlAdminService.updateDefaultTemplate(defaultTemplates, clientAppDbJdbcTemplate);
			if( templateId != 0 )
			{
				dataResponse.setObject(defaultTemplates.getTemplateId());
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.defaultTemplateupdatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.defaultTemplatesNotFound", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDefaultTemplateMasterMappedData", method = RequestMethod.POST)
	public DataResponse getDefaultTemplateMasterMappedData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("templateId") int templateId, @RequestParam("mappingType") String mappingType, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			DataResponse data = etlAdminService.getDefaultTemplateMasterMappedData(templateId, mappingType, clientAppDbJdbcTemplate);
			if( data != null )
			{
				dataResponse.setObject(data.getObject());
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDefaultTemplateInfoById/{templateId}", method = RequestMethod.GET)
	public DataResponse getDefaultTemplateInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("templateId") int templateId, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			DefaultTemplates defaultTemplate = etlAdminService.getDefaultTemplateInfoById(templateId, clientAppDbJdbcTemplate);
			if( defaultTemplate != null )
			{
				dataResponse.setObject(defaultTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhileGettingDefaultTemplateInformation", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllServerConfigurations", method = RequestMethod.GET)
	public DataResponse getAllServerConfigurations(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ServerConfigurations> serverConfigList = etlAdminService.getAllServerConfigurations(clientAppDbJdbcTemplate);
			if( serverConfigList != null && serverConfigList.size() > 0 )
			{
				dataResponse.setObject(serverConfigList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.serverConfigurationDetailsNotFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/saveServerConfigurationDetails", method = RequestMethod.POST)
	public DataResponse saveServerConfigurationDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ServerConfigurations serverConfigurations, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);

			int save = etlAdminService.saveServerConfigurationDetails(serverConfigurations, modification, clientAppDbJdbcTemplate);
			if( save > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.serverConfigSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingServerConfig", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/updateServerConfigurationDetails", method = RequestMethod.POST)
	public DataResponse updateServerConfigurationDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ServerConfigurations serverConfigurations, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedDateTime(new Date());

			int save = etlAdminService.updateServerConfigurationDetails(serverConfigurations, modification, clientAppDbJdbcTemplate);
			if( save > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.serverConfigUpdatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingServerConfig", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getServerConfigurationDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse updateServerConfigurationDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") int id, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations sc = etlAdminService.getServerConfigurationDetailsById(id, clientAppDbJdbcTemplate);
			if( sc != null )
			{
				dataResponse.setObject(sc);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveServerConfigdetails", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getILsByWSMappingId", method = RequestMethod.POST)
	public DataResponse getILsByWSMappingId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("wsTemplateId") int wsTemplateId, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ILInfo> iLInfo = etlAdminService.getILsByWSMappingId(wsTemplateId, clientAppDbJdbcTemplate);
			if( iLInfo != null && iLInfo.size() > 0 )
			{
				dataResponse.setObject(iLInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noILFound", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAvailableJarsList", method = RequestMethod.GET)
	public DataResponse getAvailableJarsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try
		{
			List<Map<String, Object>> jarFiles = CommonUtils.getListOfEtlJars();

			if( jarFiles != null && jarFiles.size() > 0 )
			{
				dataResponse.setObject(jarFiles);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.jarFilesAreNotAvailable", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveGeneralSettings", method = RequestMethod.POST)
	public DataResponse saveGeneralSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody GeneralSettings generalSettings, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			generalSettings.setModification(modification);

			int save = etlAdminService.saveGeneralSettings(generalSettings, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.settingsSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingSettings", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSettingsInfoByID/{client_id}", method = RequestMethod.POST)
	public DataResponse getSettingsInfoByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @PathVariable("client_id") int client_id, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		GeneralSettings settingsInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			settingsInfo = etlAdminService.getSettingsInfoByID(client_id, clientAppDbJdbcTemplate);
			if( settingsInfo != null )
			{
				dataResponse.setObject(settingsInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateGeneralSettings", method = RequestMethod.POST)
	public DataResponse updateGeneralSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody GeneralSettings generalSettings, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			generalSettings.setModification(modification);
			int updateInfo = etlAdminService.updateGeneralSettings(generalSettings, clientAppDbJdbcTemplate);
			if( updateInfo != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.generalSettingsUpdatedSuccessfully", null, locale));
			}
			else
			{
				int save = etlAdminService.saveGeneralSettings(generalSettings, clientAppDbJdbcTemplate);
				if( save != 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.generalSettingsUpdatedSuccessfully", null, locale));
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				}

			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/downloadJar/{jarName}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadJar(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, HttpServletResponse response, @PathVariable("jarName") String jarName, Locale locale)
	{

		byte[] file = null;
		String dir = null;
		String filePath = null;
		HttpHeaders headers = new HttpHeaders();
		try
		{

			if( StringUtils.isNotBlank(jarName) )
			{
				jarName += ".jar";
				dir = CommonUtils.getETLFolderPath("jar");
				filePath = dir + "/" + jarName;
				InputStream io = new FileInputStream(new File(filePath));

				headers.add("filename", jarName);
				file = IOUtils.toByteArray(io);
			}

		}
		catch ( AnvizentRuntimeException ae )
		{
			// packageService.logError(ae, request);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage("ERROR", t);
		}
		return new ResponseEntity<byte[]>(file, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/saveclientConfigSettings", method = RequestMethod.POST)
	public DataResponse saveclientConfigSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientConfigurationSettings clientConfigurationSettings, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(request.getHeader("Browser-Detail"));
			clientConfigurationSettings.setModification(modification);
			int save = 0;
			if( clientConfigurationSettings.getId() != 0 )
			{
				save = etlAdminService.updateclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.saveclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);

			}
			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.configurationSettingsSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingSettings", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getConfigSettingsInfoByID/{client_id}", method = RequestMethod.POST)
	public DataResponse getConfigSettingsInfoByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @PathVariable("client_id") int client_id, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ClientConfigurationSettings settingsInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			settingsInfo = etlAdminService.getConfigSettingsInfoByID(client_id, clientAppDbJdbcTemplate);
			if( settingsInfo != null )
			{
				dataResponse.setObject(settingsInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveClientConfigInfo", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateclientConfigSettings", method = RequestMethod.POST)
	public DataResponse updateclientConfigSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientConfigurationSettings clientConfigurationSettings, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			clientConfigurationSettings.setModification(modification);
			int updateInfo = etlAdminService.updateclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
			if( updateInfo != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.configurationSettingsUpdatedSuccessfully", null, locale));
			}
			else
			{
				int save = etlAdminService.saveclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
				if( save != 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.configurationSettingsSavedSuccessfully", null, locale));
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.updationFailed", null, locale));
				}

			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/testServerConnection", method = RequestMethod.POST)
	public DataResponse connectionTest(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnection iLconnection, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		Connection con = null;
		if( iLconnection != null )
		{
			try
			{
				con = CommonUtils.connectDatabase(iLconnection);
				if( con != null )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMadeTheConnection", new Object[] { iLconnection.getDatabase().getName() }, locale));
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.failedToConnectServer", new Object[] { iLconnection.getDatabase().getName() }, locale));
				}
			}
			catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e )
			{
				LOGGER.error("error while connectionTest()");
				// packageService.logError(e, request);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileTestingConnection", null, locale));
			}
			catch ( Throwable t )
			{
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			}
			finally
			{
				if( con != null )
				{
					try
					{
						con.close();
					}
					catch ( SQLException e )
					{
						LOGGER.error("", e);
					}
				}
			}
			dataResponse.setObject(message);
		}
		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/migrateUserToUser/{packageIds}/{clientUserId}", method = RequestMethod.GET)
	public DataResponse migrateUserToUser(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale, @PathVariable("packageIds") String packageIds, @PathVariable("clientUserId") String clientUserId)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		try
		{

			String packageQuery = "select * from package where package_id in(" + packageIds + ") and user_id =" + clientUserId;

			JSONObject finalJsonObject = new JSONObject();

			List<Map<String, Object>> tableData = packageService.getTableData(packageQuery, "", null);
			if( tableData != null && !tableData.isEmpty() )
			{
				JSONArray jsonList = new JSONArray();
				for (Map<String, Object> data : tableData)
				{

					JSONObject jsonObject = new JSONObject();

					for (Map.Entry<String, Object> entry : data.entrySet())
					{

						String key = entry.getKey();
						Object value = entry.getValue();

						// avoid incremental key
						if( key.equals("package_id") )
						{

							String ilConnectionQuery = "SELECT * FROM  il_connection_mapping where Package_id =" + value + " ";

							JSONArray ilConJsonList = new JSONArray();

							List<Map<String, Object>> ilConnectiontableData = packageService.getTableData(ilConnectionQuery, "", null);

							if( ilConnectiontableData != null && !ilConnectiontableData.isEmpty() )
							{

								for (Map<String, Object> map : ilConnectiontableData)
								{

									JSONObject ilConJsonObject = new JSONObject();

									for (Map.Entry<String, Object> ilConnectionEntry : map.entrySet())
									{

										String ilConnectionKey = ilConnectionEntry.getKey();
										Object ilConnectionValue = ilConnectionEntry.getValue();

										if( ilConnectionKey.equals("isWebService") )
										{

											if( ilConnectionValue.toString().equalsIgnoreCase("true") )
											{

												String ilConnectionMappingId = map.get("id").toString();

												String wsmappingQry = "SELECT * FROM  il_connection_web_service_mapping where il_connection_mapping_id =" + ilConnectionMappingId + "";

												List<Map<String, Object>> wsmappingData = packageService.getTableData(wsmappingQry, "", null);

												if( wsmappingData != null && !wsmappingData.isEmpty() )
												{

													ilConJsonObject.put("IlConnectionWsMapping", getJsonArray(wsmappingData));
												}
											}
										}

										if( data.get("isStandard").toString().equalsIgnoreCase("false") )
										{

											if( map.get("isFlatFile").toString().equalsIgnoreCase("true") )
											{

												String fileHeaderInfoQry = "SELECT * FROM  file_headers_info where file_path =  '" + map.get("filePath") + "'";

												List<Map<String, Object>> fileHeaderInfoTableData = packageService.getTableData(fileHeaderInfoQry, "", null);

												if( fileHeaderInfoTableData != null && !fileHeaderInfoTableData.isEmpty() )
												{

													ilConJsonObject.put("FileHeadersInfo", getJsonArray(fileHeaderInfoTableData));
												}
											}

										}

										ilConJsonObject.put(ilConnectionKey, ilConnectionValue);

									}
									ilConJsonList.put(ilConJsonObject);
								}
								jsonObject.put("IlConnectionMappingList", ilConJsonList);

							}

							if( data.get("isStandard").toString().equalsIgnoreCase("false") )
							{

								String customPackageTargetTableInfoQry = "SELECT * FROM  custom_package_target_table_info where package_Id =" + value + "";

								List<Map<String, Object>> cpTargetTableInfoTableData = packageService.getTableData(customPackageTargetTableInfoQry, "", null);

								if( cpTargetTableInfoTableData != null && !cpTargetTableInfoTableData.isEmpty() )
								{

									jsonObject.put("CustomPackageTargetTableInfo", getJsonArray(cpTargetTableInfoTableData));
								}

								String cpTargetTblQry = "SELECT * FROM  custom_package_target_table_query where packageId =" + value + "";

								List<Map<String, Object>> cpTargetTblQryTableData = packageService.getTableData(cpTargetTblQry, "", null);

								if( cpTargetTblQryTableData != null && !cpTargetTblQryTableData.isEmpty() )
								{

									jsonObject.put("CustomPackageTargetTableQuery", getJsonArray(cpTargetTblQryTableData));
								}

								String cpTargetTblDerivativesQry = "SELECT * FROM  custom_package_target_table_derivatives where package_Id =" + value + "";

								List<Map<String, Object>> cpTargetTblDerivativesTableData = packageService.getTableData(cpTargetTblDerivativesQry, "", null);

								if( cpTargetTblDerivativesTableData != null && !cpTargetTblDerivativesTableData.isEmpty() )
								{

									jsonObject.put("CustomPackageTargetTableDerivatives", getJsonArray(cpTargetTblDerivativesTableData));
								}

								String cpFileTempTablesMappingQry = "SELECT * FROM  file_temp_tables_mapping where packageId =" + value + "";

								List<Map<String, Object>> cpFileTempTablesMappingTableData = packageService.getTableData(cpFileTempTablesMappingQry, "", null);

								if( cpFileTempTablesMappingTableData != null && !cpFileTempTablesMappingTableData.isEmpty() )
								{

									jsonObject.put("CustomPackageFileTempTablesMapping", getJsonArray(cpFileTempTablesMappingTableData));
								}

							}

						}
						else
						{
							jsonObject.put(key, value);
						}
					}
					jsonList.put(jsonObject);
				}
				finalJsonObject.put("packageList", jsonList);

				// mahender check once
				if( finalJsonObject.keySet().size() > 0 )
				{
					dataResponse.setObject(finalJsonObject);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrievePackageTablesData", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	private JSONArray getJsonArray(List<Map<String, Object>> listOfMapObject)
	{
		JSONArray jSONArray = new JSONArray();
		for (Map<String, Object> map : listOfMapObject)
		{

			JSONObject jsonObject = new JSONObject();
			for (Map.Entry<String, Object> entry : map.entrySet())
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				jsonObject.put(key, value);
			}
			jSONArray.put(jsonObject);
		}
		return jSONArray;
	}

	@RequestMapping(value = "/getUsersByClientId/{client_Id}", method = RequestMethod.GET)
	public DataResponse getUsersByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, @PathVariable("client_Id") String clientId, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Map<Integer, String> usersList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			usersList = etlAdminService.getUsersByClientId(Integer.parseInt(clientId), clientAppDbJdbcTemplate);
			if( usersList != null )
			{
				dataResponse.setObject(usersList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveUsers", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getUserPackageList/{clientUserId}", method = RequestMethod.GET)
	public DataResponse getUserPackageList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, @PathVariable("clientUserId") String clientUserId, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Package> userPackageList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			userPackageList = etlAdminService.userPackageList(Integer.parseInt(clientUserId), clientAppDbJdbcTemplate);
			if( userPackageList != null )
			{
				dataResponse.setObject(userPackageList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveUsers", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveCurrencyIntegration", method = RequestMethod.POST)
	public DataResponse saveCurrencyIntegration(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody CurrencyIntegration currencyIntegration, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			currencyIntegration.setModification(modification);
			if( currencyIntegration.getId() != 0 )
			{
				save = etlAdminService.updateCurrencyIntegration(currencyIntegration, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.saveCurrencyIntegration(currencyIntegration, clientAppDbJdbcTemplate);
			}

			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.currencyIntegrationSave", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.currencyIntegrationSaveError", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCurrencyIntegration", method = RequestMethod.GET)
	public DataResponse getCurrencyIntegration(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			CurrencyIntegration currencyIntegrationList = etlAdminService.getCurrencyIntegration(clientAppDbJdbcTemplate);

			if( currencyIntegrationList != null )
			{

				String jobFileNames = currencyIntegrationList.getJobfile_names();
				String clientSpecificJobfileNames = currencyIntegrationList.getClientSpecificJobfile_names();
				String[] fileNames = null;
				String[] clientSpecificFileNames = null;
				List<String> jobFileNamesList = new ArrayList<>();
				List<String> clientSpecificJobFileNamesList = new ArrayList<>();
				if( jobFileNames != null )
				{
					fileNames = jobFileNames.split(",");
					for (String str : fileNames)
					{
						jobFileNamesList.add(str);
					}
				}
				if( clientSpecificJobfileNames != null )
				{
					clientSpecificFileNames = clientSpecificJobfileNames.split(",");
					for (String list : clientSpecificFileNames)
					{
						clientSpecificJobFileNamesList.add(list);
					}
				}

				currencyIntegrationList.setJobFileNames(jobFileNamesList);
				currencyIntegrationList.setJobfile_names(jobFileNames);
				currencyIntegrationList.setClientSpecificJobFileNames(clientSpecificJobFileNamesList);
				currencyIntegrationList.setClientSpecificJobfile_names(clientSpecificJobfileNames);
				dataResponse.setObject(currencyIntegrationList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveServerClientDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getInstantRunInfo", method = RequestMethod.POST)
	public DataResponse getInstantRunInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody CurrencyIntegration currencyIntegration, Locale locale

	)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{

			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			CurrencyIntegration currencyIntegrationList = etlAdminService.getCurrencyIntegration(clientAppDbJdbcTemplate);
			currencyIntegrationList.setStartDate(currencyIntegration.getStartDate());
			currencyIntegrationList.setEndDate(currencyIntegration.getEndDate());
			executionProcessor.execureCurrencyLoad(clientAppDbJdbcTemplate, clientJdbcInstance, currencyIntegrationList);
			if( currencyIntegrationList != null )
			{
				dataResponse.setObject(currencyIntegrationList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}

		}
		catch ( TalendJobNotFoundException ae )
		{
			LOGGER.error("error while getCurrencyIntegration() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getLocalizedMessage());
			messages.add(message);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsForDefaultCurrencyLoad", method = RequestMethod.GET)
	public DataResponse getJobResultsForHistoricalLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		try
		{
			resultlist = packageService.getJobResultsForDefaultCurrencyLoad(clientJdbcTemplate);
		}
		catch ( AnvizentRuntimeException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getJobResultsForDefaultCurrencyLoad/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		try
		{
			resultlist = packageService.getJobResultsForDefaultCurrencyLoad(fromDate, toDate, clientJdbcTemplate);

		}
		catch ( AnvizentRuntimeException ae )
		{
			// packageService.logError(ae, request);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}

	@RequestMapping(value = "/getAllClientsByServer/{serverId}", method = RequestMethod.GET)
	public DataResponse getAllClientsByServer(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("serverId") int serverId, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations serverConfigurations = etlAdminService.getServerConfigurationDetailsById(serverId, clientAppDbJdbcTemplate);

			if( serverConfigurations != null )
			{
				ILConnection ilConnection = CommonUtils.getSourceIlConnection(serverConfigurations);
				JdbcTemplate jdbcTemplate = CommonUtils.getClientJdbcTemplate(ilConnection);
				List<String> clientList = etlAdminService.getAllClientsByServer(jdbcTemplate, clientAppDbJdbcTemplate);

				if( clientList != null )
				{
					dataResponse.setObject(clientList);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveServerClientDetails", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/getDependencyDetailsForSourceServerClient", method = RequestMethod.POST)
	public DataResponse getDependencyDetailsForSourceServerClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration templateMigration, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		List<Object> userDbWsConList = new ArrayList<>();

		List<Package> packageList = null;

		List<User> usersList = null;
		List<ILConnection> dbConnectionList = null;
		List<WebServiceConnectionMaster> wsConnectionList = null;
		List<ClientSpecificILDLJars> clientSpecificILJarsList = null;
		List<ClientSpecificILDLJars> clientSpecificDLJarsList = null;
		List<Industry> templatesList = null;
		List<TableScripts> tableScriptsList = null;

		Map<String, List<Package>> sourceDestinationPackagesMap = new HashMap<String, List<Package>>();
		Map<String, List<User>> sourceDestinationUserMap = new HashMap<String, List<User>>();
		Map<String, List<ILConnection>> sourceDestinationDbConMap = new HashMap<String, List<ILConnection>>();
		Map<String, List<WebServiceConnectionMaster>> sourceDestinationWsConMap = new HashMap<String, List<WebServiceConnectionMaster>>();
		Map<String, List<ClientSpecificILDLJars>> sourceDestinationDlJarsMap = new HashMap<String, List<ClientSpecificILDLJars>>();
		Map<String, List<ClientSpecificILDLJars>> sourceDestinationIlJarsMap = new HashMap<String, List<ClientSpecificILDLJars>>();
		Map<String, List<Industry>> sourceDestinationTemplatesMap = new HashMap<String, List<Industry>>();
		Map<String, List<TableScripts>> sourceDestinationTableScriptsMap = new HashMap<String, List<TableScripts>>();

		List<ILConnection> destinationDbConnectionList = null;
		List<WebServiceConnectionMaster> destinationWsConnectionList = null;
		List<Package> destinationPackageList = null;
		List<User> detinationUsersList = null;
		List<ClientSpecificILDLJars> destinationClientSpecificILJarsList = null;
		List<ClientSpecificILDLJars> destinationClientSpecificDLJarsList = null;
		List<Industry> destinationTemplatesList = null;
		List<TableScripts> destinationTableScriptsList = null;
		List<Package> modifiedDestinationPackageList = new ArrayList<Package>();
		List<ILConnection> modifiedDestinationDbConnectionList = new ArrayList<ILConnection>();
		List<WebServiceConnectionMaster> modifiedDestinationWsConnectionList = new ArrayList<WebServiceConnectionMaster>();
		List<Industry> modifiedDestinationTemplatesList = new ArrayList<Industry>();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations sourceServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getServerId()), clientAppDbJdbcTemplate);
			ServerConfigurations destinationServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);

			if( sourceServerConfigurations != null )
			{

				ILConnection sourceIlConnection = CommonUtils.getSourceIlConnection(sourceServerConfigurations);
				JdbcTemplate sourceJdbcTemplate = CommonUtils.getClientJdbcTemplate(sourceIlConnection);

				packageList = etlAdminService.getPackageList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				usersList = etlAdminService.getUsersList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				dbConnectionList = etlAdminService.getDatabaseConnectionList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				wsConnectionList = etlAdminService.getWsConnectionList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				clientSpecificILJarsList = etlAdminService.getClientSpecificILJarsList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				clientSpecificDLJarsList = etlAdminService.getClientSpecificDLJarsList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				templatesList = etlAdminService.getTemplatesList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);
				tableScriptsList = etlAdminService.getTableScriptsList(sourceJdbcTemplate, templateMigration.getClientId(), clientAppDbJdbcTemplate);

				ILConnection destinationIlConnection = CommonUtils.getDestinationIlConnection(destinationServerConfigurations);
				JdbcTemplate destinationJdbcTemplate = CommonUtils.getClientJdbcTemplate(destinationIlConnection);

				destinationPackageList = etlAdminService.getPackageList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				detinationUsersList = etlAdminService.getUsersList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationDbConnectionList = etlAdminService.getDatabaseConnectionList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationWsConnectionList = etlAdminService.getWsConnectionList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationClientSpecificILJarsList = etlAdminService.getClientSpecificILJarsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationClientSpecificDLJarsList = etlAdminService.getClientSpecificDLJarsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationTemplatesList = etlAdminService.getTemplatesList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationTableScriptsList = etlAdminService.getTableScriptsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);

				for (Package pa : destinationPackageList)
				{
					Package pack = pa;
					pack.setUser(detinationUsersList);
					modifiedDestinationPackageList.add(pack);
				}

				sourceDestinationPackagesMap.put("sourcePackagesList", packageList);
				sourceDestinationPackagesMap.put("destinationPackageList", modifiedDestinationPackageList);
				userDbWsConList.add(sourceDestinationPackagesMap);

				sourceDestinationUserMap.put("sourceUsersList", usersList);
				sourceDestinationUserMap.put("destinationUsersList", detinationUsersList);
				userDbWsConList.add(sourceDestinationUserMap);

				for (ILConnection iLConnection : destinationDbConnectionList)
				{
					ILConnection iLCon = iLConnection;
					iLCon.setUser(detinationUsersList);
					modifiedDestinationDbConnectionList.add(iLCon);
				}

				sourceDestinationDbConMap.put("sourceDbConnectionList", dbConnectionList);
				sourceDestinationDbConMap.put("destinationDbConnectionList", modifiedDestinationDbConnectionList);
				userDbWsConList.add(sourceDestinationDbConMap);

				for (WebServiceConnectionMaster wsConnectionMaster : destinationWsConnectionList)
				{
					WebServiceConnectionMaster wsCon = wsConnectionMaster;
					wsCon.setUser(detinationUsersList);
					modifiedDestinationWsConnectionList.add(wsCon);
				}

				sourceDestinationWsConMap.put("sourceWsConnectionList", wsConnectionList);
				sourceDestinationWsConMap.put("destinationWsConnectionList", modifiedDestinationWsConnectionList);
				userDbWsConList.add(sourceDestinationWsConMap);

				sourceDestinationDlJarsMap.put("sourceSpecificDlJarsList", clientSpecificDLJarsList);
				// sourceDestinationDlJarsMap.put("destinationSpecificDlJarsList",
				// destinationClientSpecificDLJarsList);
				userDbWsConList.add(sourceDestinationDlJarsMap);

				sourceDestinationIlJarsMap.put("sourceSpecificIlJarsList", clientSpecificILJarsList);
				// sourceDestinationIlJarsMap.put("destinationSpecificIlJarsList",
				// destinationClientSpecificILJarsList);
				userDbWsConList.add(sourceDestinationIlJarsMap);

				/*
				 * for(Industry industry : destinationTemplatesList ){ Industry
				 * ind = industry; ind.setUser(detinationUsersList);
				 * modifiedDestinationTemplatesList.add(ind); }
				 */

				sourceDestinationTemplatesMap.put("sourceSpecificTemplatesList", templatesList);
				// sourceDestinationTemplatesMap.put("destinationSpecificTemplatesList",
				// modifiedDestinationTemplatesList);
				userDbWsConList.add(sourceDestinationTemplatesMap);

				sourceDestinationTableScriptsMap.put("sourceSpecificTableScriptsList", tableScriptsList);
				// sourceDestinationTableScriptsMap.put("destinationSpecificTableScriptsList",
				// destinationTableScriptsList);
				userDbWsConList.add(sourceDestinationTableScriptsMap);

				dataResponse.setObject(userDbWsConList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getDependencyDetailsForPackages()", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveDependencyDetailsForPackages",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDependencyDetailsForDestinationServerClient", method = RequestMethod.POST)
	public DataResponse getDependencyDetailsForDestinationServerClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration templateMigration, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		List<Object> userDbWsConList = new ArrayList<>();

		List<ILConnection> destinationDbConnectionList = null;
		List<WebServiceConnectionMaster> destinationWsConnectionList = null;
		List<Package> destinationPackageList = null;
		List<User> detinationUsersList = null;
		List<ClientSpecificILDLJars> destinationClientSpecificILJarsList = null;
		List<ClientSpecificILDLJars> destinationClientSpecificDLJarsList = null;
		List<Industry> destinationTemplatesList = null;
		List<TableScripts> destinationTableScriptsList = null;

		Map<String, List<Package>> sourceDestinationPackagesMap = new HashMap<String, List<Package>>();
		Map<String, List<User>> sourceDestinationUserMap = new HashMap<String, List<User>>();
		Map<String, List<ILConnection>> sourceDestinationDbConMap = new HashMap<String, List<ILConnection>>();
		Map<String, List<WebServiceConnectionMaster>> sourceDestinationWsConMap = new HashMap<String, List<WebServiceConnectionMaster>>();
		Map<String, List<ClientSpecificILDLJars>> sourceDestinationDlJarsMap = new HashMap<String, List<ClientSpecificILDLJars>>();
		Map<String, List<ClientSpecificILDLJars>> sourceDestinationIlJarsMap = new HashMap<String, List<ClientSpecificILDLJars>>();
		Map<String, List<Industry>> sourceDestinationTemplatesMap = new HashMap<String, List<Industry>>();
		Map<String, List<TableScripts>> sourceDestinationTableScriptsMap = new HashMap<String, List<TableScripts>>();

		try
		{
			JdbcTemplate clientAppDbJdbcTemplate = null;
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations destinationServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);

			if( destinationServerConfigurations != null )
			{

				ILConnection destinationIlConnection = CommonUtils.getDestinationIlConnection(destinationServerConfigurations);
				JdbcTemplate destinationJdbcTemplate = CommonUtils.getClientJdbcTemplate(destinationIlConnection);

				destinationPackageList = etlAdminService.getPackageList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				detinationUsersList = etlAdminService.getUsersList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationDbConnectionList = etlAdminService.getDatabaseConnectionList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationWsConnectionList = etlAdminService.getWsConnectionList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationClientSpecificILJarsList = etlAdminService.getClientSpecificILJarsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationClientSpecificDLJarsList = etlAdminService.getClientSpecificDLJarsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationTemplatesList = etlAdminService.getTemplatesList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);
				destinationTableScriptsList = etlAdminService.getTableScriptsList(destinationJdbcTemplate, templateMigration.getDestinationClientId(), clientAppDbJdbcTemplate);

				sourceDestinationPackagesMap.put("destinationPackagesList", destinationPackageList);
				userDbWsConList.add(sourceDestinationPackagesMap);

				sourceDestinationUserMap.put("destinationUsersList", detinationUsersList);
				userDbWsConList.add(sourceDestinationUserMap);

				sourceDestinationDbConMap.put("destinationDbConnectionList", destinationDbConnectionList);
				userDbWsConList.add(sourceDestinationDbConMap);

				sourceDestinationWsConMap.put("destinationWsConnectionList", destinationWsConnectionList);
				userDbWsConList.add(sourceDestinationWsConMap);

				sourceDestinationDlJarsMap.put("destinationSpecificDlJarsList", destinationClientSpecificDLJarsList);
				userDbWsConList.add(sourceDestinationDlJarsMap);

				sourceDestinationIlJarsMap.put("destinationSpecificIlJarsList", destinationClientSpecificILJarsList);
				userDbWsConList.add(sourceDestinationIlJarsMap);

				sourceDestinationTemplatesMap.put("destinationSpecificVerticalsList", destinationTemplatesList);
				userDbWsConList.add(sourceDestinationTemplatesMap);

				sourceDestinationTableScriptsMap.put("destinationSpecificTableScriptsList", destinationTableScriptsList);
				userDbWsConList.add(sourceDestinationTableScriptsMap);

				dataResponse.setObject(userDbWsConList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getDependencyDetailsForPackages()", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveDependencyDetailsForPackages",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/migrateUserFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migrateUserFromSourceToDestination(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration templateMigration, Locale locale, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		try
		{
			JdbcTemplate clientAppDbJdbcTemplate = null;
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations sourceServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getServerId()), clientAppDbJdbcTemplate);
			ServerConfigurations destinationServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);

			if( sourceServerConfigurations != null && destinationServerConfigurations != null )
			{

				Modification modification = new Modification(new Date());
				templateMigration.setModification(modification);

				ILConnection sourceIlConnection = CommonUtils.getSourceIlConnection(sourceServerConfigurations);
				JdbcTemplate sourceJdbcTemplate = CommonUtils.getClientJdbcTemplate(sourceIlConnection);

				ILConnection destinationIlConnection = CommonUtils.getSourceIlConnection(destinationServerConfigurations);
				JdbcTemplate destinationJdbcTemplate = CommonUtils.getClientJdbcTemplate(destinationIlConnection);

				Connection sourceConnection = null;
				Connection destinationConnection = null;

				PreparedStatement selectStatement = null;
				PreparedStatement insertStatement = null;
				ResultSet resultSet = null;

				try
				{
					sourceConnection = sourceJdbcTemplate.getDataSource().getConnection();
					destinationConnection = destinationJdbcTemplate.getDataSource().getConnection();

					selectStatement = sourceConnection.prepareStatement("select * from user where userId in(" + templateMigration.getSourceUserIds() + ")");

					resultSet = selectStatement.executeQuery();

					insertStatement = destinationConnection.prepareStatement(CommonUtils.createInsertSql(resultSet.getMetaData(), "user"));

					int batchSize = 0;
					while (resultSet.next())
					{
						CommonUtils.setParameters(insertStatement, resultSet, templateMigration);
						insertStatement.addBatch();
						batchSize++;

						if( batchSize >= 1000 )
						{
							insertStatement.executeBatch();
							batchSize = 0;
						}
					}

					insertStatement.executeBatch();
				}
				finally
				{
					JdbcUtils.closeResultSet(resultSet);

					JdbcUtils.closeStatement(insertStatement);
					JdbcUtils.closeStatement(selectStatement);

					JdbcUtils.closeConnection(destinationConnection);
					JdbcUtils.closeConnection(sourceConnection);
				}

				dataResponse.setObject("");
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMigrated", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while migrateUserFromSourceToDestination()", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(ae.getMessage()); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	/*
	 * @SuppressWarnings("null")
	 * 
	 * @RequestMapping(value = "/migratePackagesFromSourceToDestination", method
	 * = RequestMethod.POST) public DataResponse
	 * migratePackagesFromSourceToDestination(@PathVariable(Constants.
	 * PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration
	 * templateMigration, Locale locale, HttpServletRequest request) {
	 * 
	 * LOGGER.debug("in migratePackagesFromSourceToDestination"); DataResponse
	 * dataResponse = new DataResponse(); List<Message> messages = new
	 * ArrayList<>(); Message message = new Message();
	 * 
	 * try { JdbcTemplate clientAppDbJdbcTemplate = null; String
	 * clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
	 * ClientJdbcInstance clientJdbcInstance =
	 * userService.getClientJdbcInstance(clientIdfromHeader);
	 * clientAppDbJdbcTemplate =
	 * clientJdbcInstance.getClientAppdbJdbcTemplate(); ServerConfigurations
	 * sourceServerConfigurations =
	 * etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(
	 * templateMigration.getServerId()), clientAppDbJdbcTemplate);
	 * ServerConfigurations destinationServerConfigurations =
	 * etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(
	 * templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);
	 * 
	 * if (sourceServerConfigurations != null && destinationServerConfigurations
	 * != null) {
	 * 
	 * ILConnection sourceIlConnection =
	 * CommonUtils.getSourceIlConnection(sourceServerConfigurations);
	 * JdbcTemplate sourceJdbcTemplate =
	 * CommonUtils.getClientJdbcTemplate(sourceIlConnection) ;
	 * 
	 * ILConnection destinationIlConnection =
	 * CommonUtils.getSourceIlConnection(destinationServerConfigurations);
	 * JdbcTemplate destinationJdbcTemplate =
	 * CommonUtils.getClientJdbcTemplate(destinationIlConnection) ;
	 * 
	 * Modification modification = new Modification(new Date());
	 * templateMigration.setModification(modification);
	 * 
	 * Connection sourceConnection = null; Connection destinationConnection =
	 * null;
	 * 
	 * PreparedStatement ilConSelectStatement = null; ResultSet
	 * ilConSelectResultSet = null; ResultSet ilConDestinationResultSet = null;
	 * PreparedStatement wsConSelectStatement = null; ResultSet
	 * wsConSelectResultSet = null; PreparedStatement packageSelectStatement =
	 * null; ResultSet packageSelectResultSet = null; PreparedStatement
	 * packageInsertStatement = null; ResultSet packageDestinationResultSet =
	 * null; PreparedStatement ilConnectionSelectStatement = null; ResultSet
	 * ilConResultSet = null; PreparedStatement
	 * ilConnectionInsertStatement=null; PreparedStatement
	 * wsConMasterSelectStatement =null; ResultSet wsConMasterResultSet = null;
	 * PreparedStatement wsConMasterInsertStatement =null; ResultSet
	 * wsConMasterDestinationResultSet= null; PreparedStatement
	 * wsTemMasterSelectStatement = null; ResultSet wsTemMasterResultSet = null;
	 * PreparedStatement wsTemMasterInsertStatement = null; ResultSet
	 * wsTemMasterDestinationResultSet =null; PreparedStatement
	 * tAuthreqParamsSelectStatement=null; ResultSet tAuthreqParamsResultSet
	 * =null; PreparedStatement tAuthreqParamsInsertStatement = null;
	 * PreparedStatement wsApiMappingSelectStatement =null; ResultSet
	 * wsApiMappingMasterResultSet =null; PreparedStatement
	 * wsApiMappingInsertStatement = null; PreparedStatement
	 * wsConMasterUpdateStatement =null; PreparedStatement
	 * ilConWsMappingSelectStatement = null; ResultSet ilConWsMappingResultSet =
	 * null; PreparedStatement ilConWsMappingInsertStatement = null; ResultSet
	 * ilConWsMappingDestinationResultSet = null; PreparedStatement
	 * ilConWsMappingFileHeaderInfoSelectStatement = null; ResultSet
	 * ilConWsMappingFileHeaderInfoResultSet =null; PreparedStatement
	 * ilConWsMappingFileHeaderInfoInsertStatement =null; ResultSet
	 * ilConWsMappingFileHeaderInfoDestinationResultSet =null; PreparedStatement
	 * ilConWsMappingUpdateStatement=null; PreparedStatement
	 * ilConnectionUpdateStatement = null; PreparedStatement
	 * dbConnectionSelectStatement=null; ResultSet dbConnectionResultSet =null;
	 * PreparedStatement dbConnectionInsertStatement=null; ResultSet
	 * dbConnectionDestinationResultSet =null; PreparedStatement
	 * dbTypeSelectStatement =null; ResultSet dbTypeResultSet =null;
	 * PreparedStatement dbTypeInsertStatement=null; ResultSet
	 * dbTypeDestinationResultSet =null; PreparedStatement
	 * defaultQryHistorySelectStatement=null; ResultSet
	 * defaultQryHistoryResultSet=null; PreparedStatement
	 * ilConMasterUpdateStatement=null; PreparedStatement
	 * cpFileTempTblMappingSelectStatement=null; ResultSet
	 * cpFileTempTblMappingResultSet=null; PreparedStatement
	 * cpFileTempTblMappingInsertStatement=null; ResultSet
	 * cpFileTempTblMappingDestinationResultSet=null; PreparedStatement
	 * fileheaderInfoSelectStatement=null; ResultSet
	 * fileheaderInfoResultSet=null; ResultSet
	 * fileheaderInfoDestinationResultSet=null; PreparedStatement
	 * cpFileTempTblMappingUpdateStatement=null; PreparedStatement
	 * cpFileTempTblMappingCreateStatement=null; PreparedStatement
	 * cpTarTblInfoSelectStatement=null; ResultSet cpTarTblInfoResultSet=null;
	 * PreparedStatement cpTarTblInfoInsertStatement=null; PreparedStatement
	 * cpTarTblInfoCreateStatement=null; ResultSet
	 * cpTarTblInfoDestinationResultSet=null; PreparedStatement
	 * cpTarTblDerivativesSelectStatement=null; ResultSet
	 * cpTarTblDerivativesResultSet=null; PreparedStatement
	 * cpTarTblDerivativesInsertStatement=null; PreparedStatement
	 * cpTarTblDerivativesCreateStatement=null;
	 * 
	 * String ilWsConMappingIds = ""; Map<String,Object> packageMigrationStatus
	 * = new LinkedHashMap<String,Object>(); Map<Integer,Integer>
	 * ilConnectionMap = null; Map<Integer,Integer> wsConnectionMap = null;
	 * Map<String,Object> packageMap; try { String[] userArray =
	 * templateMigration.getDestinationUserIds().split(",");
	 * 
	 * for(int i = 0 ; i < userArray.length ; i++){
	 * 
	 * sourceConnection = sourceJdbcTemplate.getDataSource().getConnection();
	 * destinationConnection =
	 * destinationJdbcTemplate.getDataSource().getConnection();
	 * templateMigration.setUserId(userArray[i]);
	 * 
	 * // Il Connections ilConSelectStatement =
	 * sourceConnection.prepareStatement(
	 * "select * from il_connection where connection_id in (select distinct connection_id from il_connection_mapping where package_id in ("
	 * +templateMigration.getPackageIds()+"))"); ilConSelectResultSet =
	 * ilConSelectStatement.executeQuery(); while(ilConSelectResultSet != null
	 * && ilConSelectResultSet.next()){ ilConnectionMap =
	 * CommonUtils.getIlConnectionMap(ilConSelectResultSet,templateMigration
	 * ,destinationConnection,false) ; } // ws Connections wsConSelectStatement
	 * = sourceConnection.prepareStatement(
	 * "select * from ws_connections_mst where id in (select distinct webservice_Id from il_connection_mapping where package_id in ("
	 * +templateMigration.getPackageIds()+"))"); wsConSelectResultSet =
	 * wsConSelectStatement.executeQuery();
	 * 
	 * while(wsConSelectResultSet != null && wsConSelectResultSet.next()){
	 * wsConnectionMap =
	 * CommonUtils.getWsConnectionMap(wsConSelectResultSet,templateMigration
	 * ,destinationConnection,false) ; }
	 * 
	 * String[] packageArray = templateMigration.getPackageIds().split(",");
	 * 
	 * for(int j = 0 ; j < packageArray.length ; j++){ // Package Migration
	 * packageSelectStatement = sourceConnection.prepareStatement(
	 * "select * from package where package_id = ? ");
	 * packageSelectStatement.setInt(1, Integer.parseInt(packageArray[j]) );
	 * packageSelectResultSet = packageSelectStatement.executeQuery();
	 * 
	 * while(packageSelectResultSet != null && packageSelectResultSet.next()){
	 * try{
	 * 
	 * // getPackageMap here packageMap =
	 * CommonUtils.getPackageMap(packageSelectResultSet, templateMigration,
	 * destinationConnection, false); // IlConnection Mapping Migration
	 * if(packageMap != null){
	 * 
	 * ilConnectionSelectStatement = sourceConnection.prepareStatement(
	 * "SELECT * FROM  il_connection_mapping where Package_id = ?");
	 * ilConnectionSelectStatement.setInt(1, Integer.parseInt(packageArray[j])
	 * ); ilConResultSet = ilConnectionSelectStatement.executeQuery();
	 * templateMigration.setPackageId((Integer)packageMap.get("packageId"));
	 * 
	 * while(ilConResultSet != null && ilConResultSet.next()){
	 * 
	 * ilConnectionInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * ilConResultSet.getMetaData(),"il_connection_mapping"),Statement.
	 * RETURN_GENERATED_KEYS); Map<String,Object> ilMappingIdWsConIdDbConIdMap =
	 * CommonUtils.setIlConnectionParameters(ilConnectionInsertStatement,
	 * ilConResultSet, templateMigration,ilConnectionMap,wsConnectionMap);
	 * ilConnectionInsertStatement.executeUpdate(); ilConDestinationResultSet =
	 * ilConnectionInsertStatement.getGeneratedKeys();
	 * 
	 * while(ilConDestinationResultSet != null &&
	 * ilConDestinationResultSet.next()){
	 * templateMigration.setIlConMappingId(ilConDestinationResultSet.getInt(1));
	 * // standard package
	 * if((Boolean)packageMap.get("isStandard").equals(true)){
	 * 
	 * // standard package ws connection
	 * if((Boolean)ilMappingIdWsConIdDbConIdMap.get("isWebService").equals(true)
	 * ){
	 * 
	 * 
	 * // IlConnection web service Mapping Migration
	 * ilConWsMappingSelectStatement = sourceConnection.prepareStatement(
	 * "SELECT * FROM il_connection_web_service_mapping where il_connection_mapping_id = ?  and packageId = ?"
	 * ); ilConWsMappingSelectStatement.setObject(1,
	 * (Long)ilMappingIdWsConIdDbConIdMap.get("ilMappingId") );
	 * ilConWsMappingSelectStatement.setInt(2,
	 * Integer.parseInt(packageArray[j])); ilConWsMappingResultSet =
	 * ilConWsMappingSelectStatement.executeQuery();
	 * 
	 * while(ilConWsMappingResultSet != null && ilConWsMappingResultSet.next()){
	 * 
	 * // wS temp table creation in destination server
	 * if(ilMappingIdWsConIdDbConIdMap.get("isJoinWebService").equals(true)){ //
	 * get clientStagingschema name ClientJdbcInstance sourceClientJdbcInstance
	 * = new ClientJdbcInstance(templateMigration.getClientId(), restTemplate,
	 * sourceServerConfigurations.getClientDbDetailsEndPoint(),true);
	 * JdbcTemplate sourceClientStagingJdbcTemplate =
	 * sourceClientJdbcInstance.getClientJdbcTemplate(); Map<String, Object>
	 * sourceClientDbDetails =
	 * sourceClientJdbcInstance.getClientDbCredentials();
	 * templateMigration.setSourceClientStagingDbSchemaname(
	 * sourceClientDbDetails.get("clientdb_staging_schema").toString());
	 * 
	 * JdbcTemplate destinationClientStagingJdbcTemplate = new
	 * ClientJdbcInstance( templateMigration.getClientId(), restTemplate,
	 * destinationServerConfigurations.getClientDbDetailsEndPoint(),true).
	 * getClientJdbcTemplate(); ClientJdbcInstance
	 * destinationClientStagingJdbcInstance = new
	 * ClientJdbcInstance(templateMigration.getDestinationClientId(),
	 * restTemplate,
	 * destinationServerConfigurations.getClientDbDetailsEndPoint(),true);
	 * Map<String, Object> destinationClientStagingDbDetails =
	 * destinationClientStagingJdbcInstance.getClientDbCredentials();
	 * templateMigration.setDestinationClientStagingDbSchemaname(
	 * destinationClientStagingDbDetails.get("clientdb_staging_schema").toString
	 * ()); // this statement use for both join and with out join
	 * ilConWsMappingInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * ilConWsMappingResultSet.getMetaData(),"il_connection_web_service_mapping"
	 * ),Statement.RETURN_GENERATED_KEYS); Map<String,Object>
	 * ilConJoinWsMappingMap = CommonUtils.setIlConJoinWsMappingParameters(
	 * ilConWsMappingInsertStatement, ilConWsMappingResultSet,
	 * templateMigration); ilConWsMappingInsertStatement.executeUpdate();
	 * ilConWsMappingDestinationResultSet =
	 * ilConWsMappingInsertStatement.getGeneratedKeys();
	 * 
	 * while(ilConWsMappingDestinationResultSet != null &&
	 * ilConWsMappingDestinationResultSet.next()){ ilWsConMappingIds +=
	 * ilConWsMappingDestinationResultSet.getInt(1)+","; //file header info for
	 * ws service ilConWsMappingFileHeaderInfoSelectStatement =
	 * sourceConnection.prepareStatement(
	 * "SELECT * FROM file_headers_info where packageId = ? and id = ?");
	 * ilConWsMappingFileHeaderInfoSelectStatement.setInt(1,
	 * Integer.parseInt(packageArray[j]));
	 * ilConWsMappingFileHeaderInfoSelectStatement.setObject(2,
	 * (Long)ilConJoinWsMappingMap.get("fileId"));
	 * ilConWsMappingFileHeaderInfoResultSet =
	 * ilConWsMappingFileHeaderInfoSelectStatement.executeQuery();
	 * 
	 * while(ilConWsMappingFileHeaderInfoResultSet != null &&
	 * ilConWsMappingFileHeaderInfoResultSet.next()){
	 * 
	 * ilConWsMappingFileHeaderInfoInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * ilConWsMappingFileHeaderInfoResultSet.getMetaData(),"file_headers_info"),
	 * Statement.RETURN_GENERATED_KEYS);
	 * CommonUtils.setFileheaderInfoParameters(
	 * ilConWsMappingFileHeaderInfoInsertStatement,
	 * ilConWsMappingFileHeaderInfoResultSet, templateMigration);
	 * ilConWsMappingFileHeaderInfoInsertStatement.executeUpdate();
	 * ilConWsMappingFileHeaderInfoDestinationResultSet =
	 * ilConWsMappingFileHeaderInfoInsertStatement.getGeneratedKeys();
	 * 
	 * while(ilConWsMappingFileHeaderInfoDestinationResultSet != null &&
	 * ilConWsMappingFileHeaderInfoDestinationResultSet.next()){
	 * 
	 * ilConWsMappingUpdateStatement = destinationConnection.prepareStatement(
	 * "UPDATE il_connection_web_service_mapping SET fileId = ?  WHERE id = ?");
	 * ilConWsMappingUpdateStatement.setInt(1,
	 * ilConWsMappingFileHeaderInfoDestinationResultSet.getInt(1) );
	 * ilConWsMappingUpdateStatement.setInt(2,
	 * ilConWsMappingDestinationResultSet.getInt(1));
	 * ilConWsMappingUpdateStatement.executeUpdate(); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * List<Column> columns = packageService.getTableStructure(null,
	 * ilConJoinWsMappingMap.get("tempTableName").toString(), 0, "",
	 * sourceClientStagingJdbcTemplate); ClientData clientData =
	 * CommonUtils.getClientData(ilConJoinWsMappingMap.get("tempTableName").
	 * toString(),columns); String tableStructure =
	 * CommonUtils.buildMySQLCreateTable(clientData);
	 * 
	 * 
	 * Connection destinationClientStagingConnection =
	 * destinationClientStagingJdbcTemplate.getDataSource().getConnection();
	 * 
	 * if(tableStructure != null){ PreparedStatement
	 * ilConWsMappingCreateStatement =
	 * destinationClientStagingConnection.prepareStatement(tableStructure);
	 * ilConWsMappingCreateStatement.executeUpdate(); }
	 * 
	 * 
	 * }else{ ilConWsMappingInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * ilConWsMappingResultSet.getMetaData(),"il_connection_web_service_mapping"
	 * ),Statement.RETURN_GENERATED_KEYS);
	 * CommonUtils.setIlConWsMappingParameters(ilConWsMappingInsertStatement,
	 * ilConWsMappingResultSet, templateMigration);
	 * ilConWsMappingInsertStatement.executeUpdate(); } }
	 * if(ilMappingIdWsConIdDbConIdMap.get("isJoinWebService").equals(true)){
	 * //update wsConMappingId in ilconnection mapping
	 * if(!ilWsConMappingIds.equals("") && !ilWsConMappingIds.isEmpty()){
	 * ilConnectionUpdateStatement = destinationConnection.prepareStatement(
	 * "UPDATE il_connection_mapping SET procedure_parameters = ?  WHERE id = ?"
	 * ); ilConnectionUpdateStatement.setString(1,
	 * ilWsConMappingIds.substring(0, ilWsConMappingIds.length() - 1));
	 * ilConnectionUpdateStatement.setInt(2,ilConDestinationResultSet.getInt(1))
	 * ; ilConnectionUpdateStatement.executeUpdate(); ilWsConMappingIds = ""; }
	 * } }
	 * 
	 * if(!(Boolean)ilMappingIdWsConIdDbConIdMap.get("isFlatFile").equals(true))
	 * { templateMigration.setDbConTypeId((Integer)ilMappingIdWsConIdDbConIdMap.
	 * get("destinationIlConnectionId")); // if it is database for standard
	 * package defaultQryHistorySelectStatement =
	 * sourceConnection.prepareStatement(
	 * "SELECT * FROM  il_historical_load where connector_id = ? and client_id = ?"
	 * ); defaultQryHistorySelectStatement.setObject(1,
	 * (Long)ilMappingIdWsConIdDbConIdMap.get("ilConnectionId"));
	 * defaultQryHistorySelectStatement.setInt(2,
	 * Integer.parseInt(templateMigration.getServerId()));
	 * defaultQryHistoryResultSet =
	 * defaultQryHistorySelectStatement.executeQuery();
	 * 
	 * while(defaultQryHistoryResultSet != null &&
	 * defaultQryHistoryResultSet.next()){ PreparedStatement
	 * defaultQryHistoryInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * defaultQryHistoryResultSet.getMetaData(),"il_historical_load"),Statement.
	 * RETURN_GENERATED_KEYS);
	 * CommonUtils.setDefaultQryParameters(defaultQryHistoryInsertStatement,
	 * defaultQryHistoryResultSet, templateMigration);
	 * defaultQryHistoryInsertStatement.executeUpdate(); } }
	 * if(packageMigrationStatus == null){ packageMigrationStatus.put(
	 * "successfully migrated.","Source Package Id :"+packageArray[j]+
	 * " Destination User: "+userArray[i]); } } else{ // custom package start
	 * here
	 * 
	 * //file temp tables mapping cpFileTempTblMappingSelectStatement =
	 * sourceConnection.prepareStatement(
	 * "select * from file_temp_tables_mapping where packageId = ? and il_mapping_id = ?"
	 * ); cpFileTempTblMappingSelectStatement.setObject(1,
	 * Integer.parseInt(packageArray[j]));
	 * cpFileTempTblMappingSelectStatement.setObject(2,
	 * (Long)ilMappingIdWsConIdDbConIdMap.get("ilMappingId") );
	 * cpFileTempTblMappingResultSet =
	 * cpFileTempTblMappingSelectStatement.executeQuery();
	 * 
	 * while(cpFileTempTblMappingResultSet != null &&
	 * cpFileTempTblMappingResultSet.next()){ try{
	 * cpFileTempTblMappingInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * cpFileTempTblMappingResultSet.getMetaData(),"file_temp_tables_mapping"),
	 * Statement.RETURN_GENERATED_KEYS); Map<String,Object>
	 * ilConJoinWsMappingMap = CommonUtils.setCpFileTempTblMappingParameters(
	 * cpFileTempTblMappingInsertStatement, cpFileTempTblMappingResultSet,
	 * templateMigration); cpFileTempTblMappingInsertStatement.executeUpdate();
	 * cpFileTempTblMappingDestinationResultSet =
	 * cpFileTempTblMappingInsertStatement.getGeneratedKeys();
	 * while(cpFileTempTblMappingDestinationResultSet != null &&
	 * cpFileTempTblMappingDestinationResultSet.next()){ //file header info
	 * fileheaderInfoSelectStatement = sourceConnection.prepareStatement(
	 * "select * from file_headers_info where packageId = ? and id = ? ");
	 * fileheaderInfoSelectStatement.setObject(1,
	 * Integer.parseInt(packageArray[j]));
	 * fileheaderInfoSelectStatement.setObject(2,
	 * (Long)ilConJoinWsMappingMap.get("cpTempTableFileId"));
	 * fileheaderInfoResultSet = fileheaderInfoSelectStatement.executeQuery();
	 * while(fileheaderInfoResultSet != null && fileheaderInfoResultSet.next()){
	 * PreparedStatement fileheaderInfoInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * fileheaderInfoResultSet.getMetaData(),"file_headers_info"),Statement.
	 * RETURN_GENERATED_KEYS);
	 * CommonUtils.setFileheaderInfoParameters(fileheaderInfoInsertStatement,
	 * fileheaderInfoResultSet, templateMigration);
	 * fileheaderInfoInsertStatement.executeUpdate();
	 * fileheaderInfoDestinationResultSet =
	 * fileheaderInfoInsertStatement.getGeneratedKeys();
	 * while(fileheaderInfoDestinationResultSet != null &&
	 * fileheaderInfoDestinationResultSet.next()){ //update file temp tables
	 * mapping with file id cpFileTempTblMappingUpdateStatement =
	 * destinationConnection.prepareStatement(
	 * "UPDATE file_temp_tables_mapping SET fileId = ?  WHERE id = ?");
	 * cpFileTempTblMappingUpdateStatement.setInt(1,
	 * fileheaderInfoDestinationResultSet.getInt(1));
	 * cpFileTempTblMappingUpdateStatement.setInt(2,
	 * cpFileTempTblMappingDestinationResultSet.getInt(1));
	 * cpFileTempTblMappingUpdateStatement.executeUpdate(); } }
	 * 
	 * // get clientStagingschema name JdbcTemplate
	 * sourceClientStagingJdbcTemplate = new ClientJdbcInstance(
	 * templateMigration.getClientId(), restTemplate,
	 * sourceServerConfigurations.getClientDbDetailsEndPoint(),true).
	 * getClientJdbcTemplate(); ClientJdbcInstance sourceClientJdbcInstance =
	 * new ClientJdbcInstance(templateMigration.getClientId(), restTemplate,
	 * sourceServerConfigurations.getClientDbDetailsEndPoint(),true);
	 * Map<String, Object> sourceClientDbDetails =
	 * sourceClientJdbcInstance.getClientDbCredentials();
	 * 
	 * List<Column> columns =
	 * packageService.getTableStructure(sourceClientDbDetails.get(
	 * "clientdb_staging_schema").toString(),
	 * ilConJoinWsMappingMap.get("cpTempTable").toString(), 0, "",
	 * sourceClientStagingJdbcTemplate); ClientData clientData =
	 * CommonUtils.getClientData(ilConJoinWsMappingMap.get("cpTempTable").
	 * toString(),columns); String tableStructure =
	 * CommonUtils.buildMySQLCreateTable(clientData);
	 * 
	 * JdbcTemplate destinationClientStagingJdbcTemplate = new
	 * ClientJdbcInstance( templateMigration.getDestinationClientId(),
	 * restTemplate,
	 * destinationServerConfigurations.getClientDbDetailsEndPoint(),true).
	 * getClientJdbcTemplate(); Connection destinationClientStagingConnection =
	 * destinationClientStagingJdbcTemplate.getDataSource().getConnection();
	 * 
	 * if(tableStructure != null){ cpFileTempTblMappingCreateStatement =
	 * destinationClientStagingConnection.prepareStatement(tableStructure);
	 * cpFileTempTblMappingCreateStatement.executeUpdate(); } } }catch(Exception
	 * e){ LOGGER.error("error while  custom package file temp tables mapping :"
	 * , e); packageMigrationStatus.put("custom package file temp table exist:"
	 * +e.getMessage(),"Source Package Id :"+packageArray[j]+
	 * " Destination User: "+userArray[i]); } }
	 * 
	 * // custom package target table query PreparedStatement
	 * cpTargetTblQrySelectStatement = sourceConnection.prepareStatement(
	 * "select * from custom_package_target_table_query where packageId = ?");
	 * cpTargetTblQrySelectStatement.setObject(1,
	 * Integer.parseInt(packageArray[j])); ResultSet cpTargetTblQryResultSet =
	 * cpTargetTblQrySelectStatement.executeQuery();
	 * 
	 * while(cpTargetTblQryResultSet != null && cpTargetTblQryResultSet.next()){
	 * try{ PreparedStatement cpTargetTblQryInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * cpTargetTblQryResultSet.getMetaData(),"custom_package_target_table_query"
	 * ),Statement.RETURN_GENERATED_KEYS);
	 * CommonUtils.setCpTargetTblQryParameters(cpTargetTblQryInsertStatement,
	 * cpTargetTblQryResultSet, templateMigration);
	 * cpTargetTblQryInsertStatement.executeUpdate();
	 * 
	 * }catch(Exception e){ LOGGER.error(
	 * "error while  custom package target table query :", e);
	 * packageMigrationStatus.put("custom package target table exist:"
	 * +e.getMessage(),"Source Package Id :"+packageArray[j]+
	 * " Detination User: "+userArray[i]); } }
	 * 
	 * // custom package target table info cpTarTblInfoSelectStatement =
	 * sourceConnection.prepareStatement(
	 * "select * from custom_package_target_table_info where package_Id = ?");
	 * cpTarTblInfoSelectStatement.setObject(1,
	 * Integer.parseInt(packageArray[j])); cpTarTblInfoResultSet =
	 * cpTarTblInfoSelectStatement.executeQuery();
	 * 
	 * 
	 * ClientJdbcInstance destinationClientJdbcInstance = new
	 * ClientJdbcInstance(templateMigration.getDestinationClientId(),
	 * restTemplate,
	 * destinationServerConfigurations.getClientDbDetailsEndPoint());
	 * Map<String, Object> destinationClientDbDetails =
	 * destinationClientJdbcInstance.getClientDbCredentials();
	 * templateMigration.setDestinationClientDbSchemaname(
	 * destinationClientDbDetails.get("clientdb_schema").toString());
	 * 
	 * while(cpTarTblInfoResultSet != null && cpTarTblInfoResultSet.next()){
	 * try{ cpTarTblInfoInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * cpTarTblInfoResultSet.getMetaData(),"custom_package_target_table_info"),
	 * Statement.RETURN_GENERATED_KEYS); String targetTableName =
	 * CommonUtils.setTarTblInfoParameters(cpTarTblInfoInsertStatement,
	 * cpTarTblInfoResultSet, templateMigration);
	 * cpTarTblInfoInsertStatement.executeUpdate();
	 * 
	 * // create target table from source to destination in client schema
	 * 
	 * // get client db schema name JdbcTemplate sourceClientJdbcTemplate = new
	 * ClientJdbcInstance( templateMigration.getClientId(), restTemplate,
	 * sourceServerConfigurations.getClientDbDetailsEndPoint()).
	 * getClientJdbcTemplate(); ClientJdbcInstance sourceClientJdbcInstance =
	 * new ClientJdbcInstance(templateMigration.getClientId(), restTemplate,
	 * sourceServerConfigurations.getClientDbDetailsEndPoint()); Map<String,
	 * Object> sourceClientDbDetails =
	 * sourceClientJdbcInstance.getClientDbCredentials(); List<Column> columns =
	 * packageService.getTableStructure(sourceClientDbDetails.get(
	 * "clientdb_schema").toString(), targetTableName, 0, "",
	 * sourceClientJdbcTemplate);
	 * 
	 * ClientData clientData =
	 * CommonUtils.getClientData(targetTableName,columns); String
	 * targetTableStructure = CommonUtils.buildMySQLCreateTable(clientData);
	 * 
	 * JdbcTemplate destinationClientJdbcTemplate = new ClientJdbcInstance(
	 * templateMigration.getDestinationClientId(), restTemplate,
	 * destinationServerConfigurations.getClientDbDetailsEndPoint()).
	 * getClientJdbcTemplate(); Connection destinationClientConnection =
	 * destinationClientJdbcTemplate.getDataSource().getConnection();
	 * 
	 * if(targetTableStructure != null){ cpTarTblInfoCreateStatement =
	 * destinationClientConnection.prepareStatement(targetTableStructure);
	 * cpTarTblInfoCreateStatement.executeUpdate(); }
	 * 
	 * 
	 * cpTarTblInfoDestinationResultSet =
	 * cpTarTblInfoInsertStatement.getGeneratedKeys();
	 * while(cpTarTblInfoDestinationResultSet != null &&
	 * cpTarTblInfoDestinationResultSet.next()){
	 * templateMigration.setTargetTblInfoId(cpTarTblInfoDestinationResultSet.
	 * getInt(1));
	 * 
	 * //custom package target table derivatives
	 * cpTarTblDerivativesSelectStatement = sourceConnection.prepareStatement(
	 * "select * from custom_package_target_table_derivatives where package_Id = ?"
	 * ); cpTarTblDerivativesSelectStatement.setObject(1,
	 * Integer.parseInt(packageArray[j])); cpTarTblDerivativesResultSet =
	 * cpTarTblDerivativesSelectStatement.executeQuery();
	 * 
	 * while(cpTarTblDerivativesResultSet != null &&
	 * cpTarTblDerivativesResultSet.next()){ try{
	 * cpTarTblDerivativesInsertStatement =
	 * destinationConnection.prepareStatement(CommonUtils.createInsertSql(
	 * cpTarTblDerivativesResultSet.getMetaData(),
	 * "custom_package_target_table_derivatives"),Statement.
	 * RETURN_GENERATED_KEYS); String derivativeTableName =
	 * CommonUtils.setCpTarTblDerivativesParameters(
	 * cpTarTblDerivativesInsertStatement, cpTarTblDerivativesResultSet,
	 * templateMigration); cpTarTblDerivativesInsertStatement.executeUpdate();
	 * 
	 * List<Column> derivativeTableColumns =
	 * packageService.getTableStructure(sourceClientDbDetails.get(
	 * "clientdb_schema").toString(), derivativeTableName, 0, "",
	 * sourceClientJdbcTemplate);
	 * 
	 * ClientData derivativeTableClientData =
	 * CommonUtils.getClientData(derivativeTableName,derivativeTableColumns);
	 * String derivativeTableStructure =
	 * CommonUtils.buildMySQLCreateTable(derivativeTableClientData);
	 * 
	 * if(derivativeTableStructure != null){ cpTarTblDerivativesCreateStatement
	 * = destinationClientConnection.prepareStatement(derivativeTableStructure);
	 * cpTarTblDerivativesCreateStatement.executeUpdate(); }
	 * 
	 * 
	 * }catch(Exception e){ LOGGER.error(
	 * "error while  custom package target table derivatives:", e);
	 * packageMigrationStatus.put("Target Derived Table Exist."+e.getMessage(),
	 * "Source Package Id :"+packageArray[j]+" Destination User: "
	 * +userArray[i]); } } } }catch(Exception e){ LOGGER.error(
	 * "error while  custom package target table :", e);
	 * packageMigrationStatus.put("Target Table Table Exist:"+e.getMessage(),
	 * "Source Package Id :"+packageArray[j]+" Destination User: "
	 * +userArray[i]); } } } } } } if(packageMigrationStatus.isEmpty() &&
	 * packageMap != null){ packageMigrationStatus.put("successfully migrated.",
	 * "Source Package Id :"+packageArray[j]+" Destination User: "
	 * +userArray[i]); } }catch(Exception e){ LOGGER.error(
	 * "error while package creation in destination server:", e);
	 * packageMigrationStatus.put(e.getMessage(),"Source Package Id :"
	 * +packageArray[j]+" Destination User: "+userArray[i]); } }
	 * 
	 * } }
	 * 
	 * }finally{ //closed prepareStatement and ResultSet .
	 * JdbcUtils.closeStatement(packageSelectStatement);
	 * JdbcUtils.closeResultSet(packageSelectResultSet);
	 * JdbcUtils.closeStatement(packageInsertStatement);
	 * JdbcUtils.closeResultSet(packageDestinationResultSet);
	 * JdbcUtils.closeStatement(ilConnectionSelectStatement);
	 * JdbcUtils.closeResultSet(ilConResultSet);
	 * JdbcUtils.closeStatement(ilConnectionInsertStatement);
	 * JdbcUtils.closeResultSet(ilConDestinationResultSet);
	 * JdbcUtils.closeStatement(wsConMasterSelectStatement);
	 * JdbcUtils.closeResultSet(wsConMasterResultSet);
	 * JdbcUtils.closeStatement(wsConMasterInsertStatement);
	 * JdbcUtils.closeResultSet(wsConMasterDestinationResultSet);
	 * JdbcUtils.closeStatement(wsTemMasterSelectStatement);
	 * JdbcUtils.closeResultSet(wsTemMasterResultSet);
	 * JdbcUtils.closeStatement(wsTemMasterInsertStatement);
	 * JdbcUtils.closeResultSet(wsTemMasterDestinationResultSet);
	 * JdbcUtils.closeStatement(tAuthreqParamsSelectStatement);
	 * JdbcUtils.closeResultSet(tAuthreqParamsResultSet);
	 * JdbcUtils.closeStatement(tAuthreqParamsInsertStatement);
	 * JdbcUtils.closeStatement(wsApiMappingSelectStatement);
	 * JdbcUtils.closeResultSet(wsApiMappingMasterResultSet);
	 * JdbcUtils.closeStatement(wsApiMappingInsertStatement);
	 * JdbcUtils.closeStatement(wsConMasterUpdateStatement);
	 * JdbcUtils.closeStatement(ilConWsMappingSelectStatement);
	 * JdbcUtils.closeResultSet(ilConWsMappingResultSet);
	 * JdbcUtils.closeStatement(ilConWsMappingInsertStatement);
	 * JdbcUtils.closeResultSet(ilConWsMappingDestinationResultSet);
	 * JdbcUtils.closeStatement(ilConWsMappingFileHeaderInfoSelectStatement);
	 * JdbcUtils.closeResultSet(ilConWsMappingFileHeaderInfoResultSet);
	 * JdbcUtils.closeStatement(ilConWsMappingFileHeaderInfoInsertStatement);
	 * JdbcUtils.closeResultSet(ilConWsMappingFileHeaderInfoDestinationResultSet
	 * ); JdbcUtils.closeStatement(ilConWsMappingUpdateStatement);
	 * JdbcUtils.closeStatement(ilConnectionUpdateStatement);
	 * JdbcUtils.closeStatement(dbConnectionSelectStatement);
	 * JdbcUtils.closeResultSet(dbConnectionResultSet);
	 * JdbcUtils.closeStatement(dbConnectionInsertStatement);
	 * JdbcUtils.closeResultSet(dbConnectionDestinationResultSet);
	 * JdbcUtils.closeStatement(dbTypeSelectStatement);
	 * JdbcUtils.closeResultSet(dbTypeResultSet);
	 * JdbcUtils.closeStatement(dbTypeInsertStatement);
	 * JdbcUtils.closeResultSet(dbTypeDestinationResultSet);
	 * JdbcUtils.closeStatement(defaultQryHistorySelectStatement);
	 * JdbcUtils.closeResultSet(defaultQryHistoryResultSet);
	 * JdbcUtils.closeStatement(ilConMasterUpdateStatement);
	 * JdbcUtils.closeStatement(cpFileTempTblMappingSelectStatement);
	 * JdbcUtils.closeResultSet(cpFileTempTblMappingResultSet);
	 * JdbcUtils.closeStatement(cpFileTempTblMappingInsertStatement);
	 * JdbcUtils.closeResultSet(cpFileTempTblMappingDestinationResultSet);
	 * JdbcUtils.closeStatement(fileheaderInfoSelectStatement);
	 * JdbcUtils.closeResultSet(fileheaderInfoResultSet);
	 * JdbcUtils.closeResultSet(fileheaderInfoDestinationResultSet);
	 * JdbcUtils.closeStatement(cpFileTempTblMappingUpdateStatement);
	 * JdbcUtils.closeStatement(cpFileTempTblMappingCreateStatement);
	 * JdbcUtils.closeStatement(cpTarTblInfoSelectStatement);
	 * JdbcUtils.closeResultSet(cpTarTblInfoResultSet);
	 * JdbcUtils.closeStatement(cpTarTblInfoInsertStatement);
	 * JdbcUtils.closeStatement(cpTarTblInfoCreateStatement);
	 * JdbcUtils.closeResultSet(cpTarTblInfoDestinationResultSet);
	 * JdbcUtils.closeStatement(cpTarTblDerivativesSelectStatement);
	 * JdbcUtils.closeResultSet(cpTarTblDerivativesResultSet);
	 * JdbcUtils.closeStatement(cpTarTblDerivativesInsertStatement);
	 * JdbcUtils.closeStatement(cpTarTblDerivativesCreateStatement); //close
	 * source and destination connections.
	 * JdbcUtils.closeConnection(destinationConnection);
	 * JdbcUtils.closeConnection(sourceConnection);
	 * 
	 * } if(packageMigrationStatus != null){
	 * dataResponse.setObject(packageMigrationStatus);
	 * message.setCode(messageSource.getMessage("anvizent.message.success.code",
	 * null, locale)); message.setText("migrated.");
	 * dataResponse.setMessages(messages); } }
	 * 
	 * } catch (Throwable t) { MinidwServiceUtil.getErrorMessage(message,
	 * "ERROR", t); } messages.add(message); dataResponse.setMessages(messages);
	 * return dataResponse; }
	 */
	@RequestMapping(value = "/migrateDbConFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migrateDbConFromSourceToDestination(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration templateMigration, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ServerConfigurations sourceServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getServerId()), clientAppDbJdbcTemplate);
			ServerConfigurations destinationServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);

			if( sourceServerConfigurations != null && destinationServerConfigurations != null )
			{

				Modification modification = new Modification(new Date());
				templateMigration.setModification(modification);

				ILConnection sourceIlConnection = CommonUtils.getSourceIlConnection(sourceServerConfigurations);
				JdbcTemplate sourceJdbcTemplate = CommonUtils.getClientJdbcTemplate(sourceIlConnection);

				ILConnection destinationIlConnection = CommonUtils.getSourceIlConnection(destinationServerConfigurations);
				JdbcTemplate destinationJdbcTemplate = CommonUtils.getClientJdbcTemplate(destinationIlConnection);

				Connection sourceConnection = null;
				Connection destinationConnection = null;

				PreparedStatement ilConSelectStatement = null;
				ResultSet ilConSelectResultSet = null;
				Map<Integer, Integer> ilConnectionMap = null;

				try
				{
					String[] userArray = templateMigration.getDestinationUserIds().split(",");
					for (int i = 0; i < userArray.length; i++)
					{
						templateMigration.setUserId(userArray[i]);
						sourceConnection = sourceJdbcTemplate.getDataSource().getConnection();
						destinationConnection = destinationJdbcTemplate.getDataSource().getConnection();
						// Il Connections
						ilConSelectStatement = sourceConnection.prepareStatement("select * from il_connection where connection_id in(" + templateMigration.getSourceDbConIds() + ")");
						ilConSelectResultSet = ilConSelectStatement.executeQuery();
						while (ilConSelectResultSet != null && ilConSelectResultSet.next())
						{
							ilConnectionMap = CommonUtils.getIlConnectionMap(ilConSelectResultSet, templateMigration, destinationConnection, false);
						}
					}
				}
				finally
				{
					JdbcUtils.closeStatement(ilConSelectStatement);
					JdbcUtils.closeResultSet(ilConSelectResultSet);
					JdbcUtils.closeConnection(destinationConnection);
					JdbcUtils.closeConnection(sourceConnection);
				}
				if( ilConnectionMap != null )
				{
					dataResponse.setObject(ilConnectionMap);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMigrated", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while migrateDbConFromSourceToDestination()",
		 * ae); message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(ae.getMessage()); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/migrateWsConFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migrateWsConFromSourceToDestination(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TemplateMigration templateMigration, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ServerConfigurations sourceServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getServerId()), clientAppDbJdbcTemplate);
			ServerConfigurations destinationServerConfigurations = etlAdminService.getServerConfigurationDetailsById(Integer.parseInt(templateMigration.getDestinationServerId()), clientAppDbJdbcTemplate);

			if( sourceServerConfigurations != null && destinationServerConfigurations != null )
			{

				Modification modification = new Modification(new Date());
				templateMigration.setModification(modification);

				ILConnection sourceIlConnection = CommonUtils.getSourceIlConnection(sourceServerConfigurations);
				JdbcTemplate sourceJdbcTemplate = CommonUtils.getClientJdbcTemplate(sourceIlConnection);

				ILConnection destinationIlConnection = CommonUtils.getSourceIlConnection(destinationServerConfigurations);
				JdbcTemplate destinationJdbcTemplate = CommonUtils.getClientJdbcTemplate(destinationIlConnection);

				Connection sourceConnection = null;
				Connection destinationConnection = null;

				PreparedStatement wsConSelectStatement = null;
				ResultSet wsConSelectResultSet = null;
				Map<Integer, Integer> wsConnectionMap = null;

				try
				{

					String[] userArray = templateMigration.getDestinationUserIds().split(",");
					for (int i = 0; i < userArray.length; i++)
					{
						templateMigration.setUserId(userArray[i]);
						sourceConnection = sourceJdbcTemplate.getDataSource().getConnection();
						destinationConnection = destinationJdbcTemplate.getDataSource().getConnection();

						sourceConnection = sourceJdbcTemplate.getDataSource().getConnection();
						destinationConnection = destinationJdbcTemplate.getDataSource().getConnection();
						// ws Connections
						wsConSelectStatement = sourceConnection.prepareStatement("select * from ws_connections_mst where id in (" + templateMigration.getSourceWsConIds() + ")");
						wsConSelectResultSet = wsConSelectStatement.executeQuery();

						while (wsConSelectResultSet != null && wsConSelectResultSet.next())
						{
							wsConnectionMap = CommonUtils.getWsConnectionMap(wsConSelectResultSet, templateMigration, destinationConnection, false);
						}
					}
				}
				finally
				{
					JdbcUtils.closeStatement(wsConSelectStatement);
					JdbcUtils.closeResultSet(wsConSelectResultSet);
					JdbcUtils.closeConnection(destinationConnection);
					JdbcUtils.closeConnection(sourceConnection);
				}
				if( wsConnectionMap != null )
				{
					dataResponse.setObject(wsConnectionMap);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMigrated", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while migrateDbConFromSourceToDestination()",
		 * ae); message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(ae.getMessage()); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getVersionUpgrade", method = RequestMethod.GET)
	public DataResponse getVersionUpgrade(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<VersionUpgrade> versionUpgradeList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			versionUpgradeList = etlAdminService.getVersionUpgrade(clientAppDbJdbcTemplate);
			if( versionUpgradeList != null )
			{
				dataResponse.setObject(versionUpgradeList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getVersionUpgrade() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails",
		 * null, locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createVersionUpgrade", method = RequestMethod.POST)
	public DataResponse createVersionUpgrade(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody VersionUpgrade versionUpgrade, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		// userObjectRemoved
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			versionUpgrade.setModification(modification);
			int save = 0;
			VersionUpgrade versionUpgrade1 = etlAdminService.getVersionUpgradeDetailsByVersionNumber(versionUpgrade.getVersionNumber(), clientAppDbJdbcTemplate);

			if( versionUpgrade1 != null && versionUpgrade.getVersionId() != versionUpgrade1.getVersionId() )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.versionNumberAlreadyExists", null, locale));
				return dataResponse;
			}

			if( versionUpgrade.isLatestVersion() )
			{
				etlAdminService.updateAllVersionsToOld(clientAppDbJdbcTemplate);
			}
			if( versionUpgrade.getVersionId() != 0 )
			{
				save = etlAdminService.updateVersionUpgrade(versionUpgrade, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.createVersionUpgrade(versionUpgrade, clientAppDbJdbcTemplate);
			}
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateVersionUpgrade", null, locale));
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while createDB() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unableToCreateVersionUpgrade", null,
		 * locale)); }
		 */catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getVersionUpgradeDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getVersionUpgradeDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") int id, Locale locale, HttpServletRequest request)
	{

		VersionUpgrade verUpgrade = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			verUpgrade = etlAdminService.getVersionUpgradeDetailsById(id, clientAppDbJdbcTemplate);
			if( verUpgrade != null )
			{
				dataResponse.setObject(verUpgrade);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.versionUpgradeDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getVersionUpgradeDetailsById() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unableToRetrieveVersionUpgradeDetails",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientCurrencyMapping", method = RequestMethod.GET)
	public DataResponse getClientCurrencyMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ClientCurrencyMapping> clientCurrencyMapping1 = null;
		List<ClientCurrencyMapping> updatedClientCurrencyMapping = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			String selectedClientId = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientCurrencyMapping1 = etlAdminService.getClientCurrencyMapping(clientAppDbJdbcTemplate);

			if( clientCurrencyMapping1 != null )
			{
				updatedClientCurrencyMapping = new ArrayList<>();
				for (ClientCurrencyMapping ccm : clientCurrencyMapping1)
				{
					if( ccm.getClientId().equals(selectedClientId) )
					{
						updatedClientCurrencyMapping.add(ccm);
					}
				}
				dataResponse.setObject(updatedClientCurrencyMapping);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveClientCurrenctMappingDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getClientCurrencyMapping() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveClientCurrenctMappingDetails",
		 * null, locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createClientCurrencyMapping", method = RequestMethod.POST)
	public DataResponse createClientCurrencyMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientCurrencyMapping clientCurrencyMapping, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		// userObjectRemoved
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			clientCurrencyMapping.setModification(modification);

			ClientJdbcInstance respectiveClientJdbcInstance = userService.getClientJdbcInstance(clientCurrencyMapping.getClientId());
			clientJdbcTemplate = respectiveClientJdbcInstance.getClientJdbcTemplate();

			int save = etlAdminService.createClientCurrencyMapping(clientCurrencyMapping, clientJdbcTemplate, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoCreateClientCurrenctMappingDetails", null, locale));
			}
		}
		catch ( AnvizentDuplicateStatusUpdationException ae )
		{
			// packageService.logError(ae, request);
			LOGGER.error("error while createClientCurrencyMapping() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		}
		catch ( AnvizentCorewsException e )
		{
			// packageService.logError(e, request);
			LOGGER.error("error while createClientCurrencyMapping() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileConnectingToClientSchemaerrorDetails", null, locale) + " " + e.getLocalizedMessage());
			messages.add(message);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getclientCurrencyMappingDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getclientCurrencyMappingDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") int id, Locale locale, HttpServletRequest request)
	{

		ClientCurrencyMapping clientCurrencyMapInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientCurrencyMapInfo = etlAdminService.getclientCurrencyMappingDetailsById(id, clientAppDbJdbcTemplate);
			if( clientCurrencyMapInfo != null )
			{
				dataResponse.setObject(clientCurrencyMapInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoGetClientCurrenctMappingDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getclientCurrencyMappingDetailsById() " ,
		 * ae); message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoGetClientCurrenctMappingDetails",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getclientCurrencyMappingDetailsByClientId/{CLIENT_ID}", method = RequestMethod.GET)
	public DataResponse getclientCurrencyMappingDetailsByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("CLIENT_ID") int id, Locale locale, HttpServletRequest request)
	{

		ClientCurrencyMapping clientCurrencyMapInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			String selectedClientId = CommonUtils.getClientIDFromHeader(request);

			if( selectedClientId == null || !selectedClientId.equals(id + "") )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoGetClientCurrenctMappingDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientCurrencyMapInfo = etlAdminService.getclientCurrencyMappingDetailsByClientId(id, clientAppDbJdbcTemplate);
			if( clientCurrencyMapInfo != null )
			{
				dataResponse.setObject(clientCurrencyMapInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoGetClientCurrenctMappingDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getclientCurrencyMappingDetailsById() " ,
		 * ae); message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoGetClientCurrenctMappingDetails",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/createCurrencyMapping", method = RequestMethod.POST)
	public DataResponse createCurrencyMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientCurrencyMapping clientCurrencyMapping, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			clientCurrencyMapping.setModification(modification);
			int newTemplateId = 0;
			if( clientCurrencyMapping.getId() != 0 )
			{
				newTemplateId = etlAdminService.updateCurrencyMapping(clientCurrencyMapping, clientAppDbJdbcTemplate);
			}
			else
			{
				newTemplateId = etlAdminService.createCurrencyMapping(clientCurrencyMapping, clientAppDbJdbcTemplate);
			}

			if( newTemplateId != -1 )
			{
				dataResponse.setObject(newTemplateId);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while createCurrencyMapping() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unableToSaveDetails", null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDefaultCurrencyInfoById/{templateId}", method = RequestMethod.GET)
	public DataResponse getDefaultCurrencyInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("templateId") int templateId, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ClientCurrencyMapping currencyMap = etlAdminService.getDefaultCurrencyInfoById(templateId, clientAppDbJdbcTemplate);
			if( currencyMap != null )
			{
				dataResponse.setObject(currencyMap);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhileGettingDefaultTemplateInformation", null, locale));
			}
		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getDefaultTemplateInfoById() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.errorwhileGettingDefaultTemplateInformation",
		 * null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.GET)
	public DataResponse getCurrencyList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<CurrencyList> currencyList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			currencyList = etlAdminService.getCurrencyList(clientAppDbJdbcTemplate);
			if( currencyList != null )
			{
				dataResponse.setObject(currencyList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveCurrencyList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPropertyKeyValueFromminidwWebService", method = RequestMethod.GET)
	public DataResponse getPropertyKeyValueFromminidwWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Map<String, String> servicePropertiesData = null;

		try
		{
			servicePropertiesData = MinidwServiceUtil.getServicePropertiesKeyValuePairs();
			if( servicePropertiesData != null )
			{
				dataResponse.setObject(servicePropertiesData);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error(
		 * "error while getPropertyKeyValueFromminidwWebService() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale)); messages.add(message);
		 * }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/savePropertiesKeyValuePairs", method = RequestMethod.POST)
	public DataResponse savePropertiesKeyValuePairs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Internalization internalization, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			save = etlAdminService.savePropertiesKeyValuePairs(internalization, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while createCurrencyMapping() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unableToSaveDetails", null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getPropertiesList", method = RequestMethod.GET)
	public DataResponse getPropertiesList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Internalization> propertiesListData = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			propertiesListData = etlAdminService.getPropertiesList(clientAppDbJdbcTemplate);
			if( propertiesListData != null )
			{
				dataResponse.setObject(propertiesListData);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
	  catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getpropertiesKeyValuePairsById", method = RequestMethod.POST)
	public DataResponse getpropertiesKeyValuePairsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("id") Integer id, HttpServletRequest request, Locale locale)
	{
		Internalization propertiesInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			propertiesInfo = etlAdminService.getpropertiesKeyValuePairsById(id, clientAppDbJdbcTemplate);
			if( propertiesInfo != null )
			{
				Map<String, Object> properies = new HashMap<>();
				properies.put("servicePropertiesData", MinidwServiceUtil.getServicePropertiesKeyValuePairs());
				properies.put("savedproperties", propertiesInfo);
				dataResponse.setObject(properies);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrievePropertiesList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getS3InfoList", method = RequestMethod.GET)
	public DataResponse getS3InfoList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<S3BucketInfo> s3InfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			s3InfoList = etlAdminService.getS3InfoList(clientAppDbJdbcTemplate);
			if( s3InfoList != null )
			{
				dataResponse.setObject(s3InfoList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3infoList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveS3BucketInfo", method = RequestMethod.POST)
	public DataResponse saveS3BucketInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody S3BucketInfo s3BucketInfo, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			s3BucketInfo.setModification(modification);
			AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(), s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
			Path paths = Files.createTempFile("sampletestfile", ".csv");
			amazonS3Utilities.uploadFileToS3(paths.toFile(), "/", false);

			save = etlAdminService.saveS3BucketInfo(s3BucketInfo, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.success.text.bucketnameAlreadyExist", null, locale));
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getS3BucketInfoById", method = RequestMethod.POST)
	public DataResponse getS3BucketInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("id") Integer id, HttpServletRequest request, Locale locale)
	{
		S3BucketInfo s3Info = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			s3Info = etlAdminService.getS3BucketInfoById(id, clientAppDbJdbcTemplate);
			if( s3Info != null )
			{
				dataResponse.setObject(s3Info);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3Details", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getBucketList", method = RequestMethod.GET)
	public DataResponse getBucketList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Map<Integer, String> s3BucketList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			s3BucketList = etlAdminService.getBucketList(clientAppDbJdbcTemplate);
			if( s3BucketList != null )
			{
				dataResponse.setObject(s3BucketList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3infoList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while timeZoneList() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveS3infoList", null,
		 * locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getScheduledMasterInfoList", method = RequestMethod.GET)
	public DataResponse getScheduledMasterInfoList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<SchedulerMaster> schedulerMasterList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			schedulerMasterList = etlAdminService.getScheduledMasterInfoList(clientAppDbJdbcTemplate);
			if( schedulerMasterList != null )
			{
				dataResponse.setObject(schedulerMasterList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3infoList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while timeZoneList() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveS3infoList", null,
		 * locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getFileSettingsList", method = RequestMethod.GET)
	public DataResponse getFileSettingsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<FileSettings> fileSettingsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			fileSettingsList = etlAdminService.getFileSettingsList(clientAppDbJdbcTemplate);
			if( fileSettingsList != null )
			{
				dataResponse.setObject(fileSettingsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3infoList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while timeZoneList() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveS3infoList", null,
		 * locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientMapping", method = RequestMethod.POST)
	public DataResponse saveClientMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody S3BucketInfo s3BucketInfo, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			s3BucketInfo.setModification(modification);
			save = etlAdminService.saveClientMapping(s3BucketInfo, clientAppDbJdbcTemplate);
			if( save != 0 )
			{
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while saveClientMapping() ", ae);
		 * message.setCode(messageSource.getMessage(
		 * "anvizent.message.error.code", null, locale));
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unableToSaveDetails", null, locale)); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveMiddleLevelManagerInfo", method = RequestMethod.POST)
	public DataResponse createMiddleLevelManager(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody MiddleLevelManager middleLevelManager, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int save = 0;
			if( middleLevelManager.getId() != 0 )
			{
				save = etlAdminService.updateMiddleLevelManager(middleLevelManager, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.createMiddleLevelManager(middleLevelManager, clientAppDbJdbcTemplate);
			}
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToMiddleLevelManagerInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getMiddleLevelManagerDetailsById", method = RequestMethod.GET)
	public DataResponse getMiddleLevelManagerDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{

		MiddleLevelManager middleLevelManager = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			middleLevelManager = etlAdminService.getMiddleLevelManagerDetailsById(clientAppDbJdbcTemplate);
			if( middleLevelManager != null )
			{
				dataResponse.setObject(middleLevelManager);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.versionUpgradeDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getHybridClientsGrouping", method = RequestMethod.GET)
	public DataResponse getHybridClientsGrouping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<HybridClientsGrouping> hybridClientsList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			hybridClientsList = etlAdminService.getHybridClientsGrouping(clientAppDbJdbcTemplate);
			if( hybridClientsList != null )
			{
				dataResponse.setObject(hybridClientsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		/*
		 * catch (Exception ae) { packageService.logError(ae, request);
		 * LOGGER.error("error while getVersionUpgrade() ", ae);
		 * message.setCode(com.anvizent.client.data.to.csv.path.converter.
		 * constants.Constants.Config.ERROR);
		 * message.setText(messageSource.getMessage(
		 * "anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails",
		 * null, locale)); messages.add(message); }
		 */ catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveHybridClientsGroupingInfo", method = RequestMethod.POST)
	public DataResponse saveHybridClientsGroupingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody HybridClientsGrouping hybridClientsGrouping, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int save = 0;
			hybridClientsGrouping.setAccessKey("Rajesh");
			if( hybridClientsGrouping.getId() != 0 )
			{
				save = etlAdminService.updateHybridClientsGroupingInfo(hybridClientsGrouping, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.createHybridClientsGroupingInfo(hybridClientsGrouping, clientAppDbJdbcTemplate);
			}

			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToHybridGroupingInfo", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getHybridClientGroupingDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getHybridClientGroupingDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") long id, Locale locale, HttpServletRequest request)
	{

		HybridClientsGrouping hybridClientsGrouping = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			hybridClientsGrouping = etlAdminService.getHybridClientGroupingDetailsById(id, clientAppDbJdbcTemplate);
			if( hybridClientsGrouping != null )
			{
				dataResponse.setObject(hybridClientsGrouping);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.versionUpgradeDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientJobExecutionParameters", method = RequestMethod.POST)
	public DataResponse createClientJobExecutionParams(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientJobExecutionParameters clientJobExecutionParameters, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			clientJobExecutionParameters.setModification(modification);

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int save = 0;
			if( clientJobExecutionParameters.getId() != 0 )
			{
				save = etlAdminService.updateClientJobExecutionParams(clientJobExecutionParameters, clientAppDbJdbcTemplate);
			}
			else
			{
				save = etlAdminService.createClientJobExecutionParams(clientJobExecutionParameters, clientAppDbJdbcTemplate);
			}
			if( save != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToJobExecutionParamterDetails", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/clientJobExecutionParametersDetailsById", method = RequestMethod.GET)
	public DataResponse getclientJobExecutionParametersDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request)
	{

		ClientJobExecutionParameters clientJobExecutionParameters = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJobExecutionParameters = etlAdminService.getclientJobExecutionParametersDetailsById(clientAppDbJdbcTemplate);
			if( clientJobExecutionParameters != null )
			{
				dataResponse.setObject(clientJobExecutionParameters);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.versionUpgradeDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getMasterVerticals", method = RequestMethod.POST)
	public DataResponse getMasterVerticals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestParam("verticalIds") List<Integer> verticalIds, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Industry> industries = new ArrayList<>();

		try
		{
			industries = migrationService.getMasterVerticals(verticalIds, commonJdbcTemplate);
			if( industries != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(industries);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noVerticalFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveNotMappedVerticals", method = RequestMethod.POST)
	public DataResponse saveNotMappedVerticals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Industry industry, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int create = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			create = migrationService.saveNotMappedVerticals(industry, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			LOGGER.error("error while saveEtlDlIlMapping() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDLILMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getMasterDataBases", method = RequestMethod.POST)
	public DataResponse getMasterDatabases(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestParam("databaseIds") List<Integer> databaseIds, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Database> databases = new ArrayList<>();

		try
		{
			databases = migrationService.getMasterDataBases(databaseIds, commonJdbcTemplate);
			if( databases != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(databases);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noVerticalFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveNotMappedDatabases", method = RequestMethod.POST)
	public DataResponse saveNotMappedDatabases(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int create = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			create = migrationService.saveNotMappedDatabases(database, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			LOGGER.error("error while saveNotMappedDatabases() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDLILMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getMasterConnectors", method = RequestMethod.POST)
	public DataResponse getMasterConnectors(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestParam("connectorsIds") List<Integer> connectorsIds, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<Database> databases = new ArrayList<>();

		try
		{
			databases = migrationService.getMasterConnectors(connectorsIds, commonJdbcTemplate);
			if( databases != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(databases);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveNotMappedConnectors", method = RequestMethod.POST)
	public DataResponse saveNotMappedConnectors(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Database database, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int create = 0;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			create = migrationService.saveNotMappedConnectors(database, clientAppDbJdbcTemplate);
			if( create != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/executeInsertScripts", method = RequestMethod.POST)
	public DataResponse executeInsertScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody MultiClientInsertScriptsExecution multiClientInsertScriptsExecution, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Connection sourceConnection = null;
		String messageInfo = "";
		try
		{
			sourceConnection = commonJdbcTemplate.getDataSource().getConnection();
			messageInfo = etlAdminService.executeInsertScripts(multiClientInsertScriptsExecution, sourceConnection, commonJdbcTemplate, clientId);
			if( messageInfo != "" )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageInfo);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/createDDlTablesAtClientDb", method = RequestMethod.POST)
	public DataResponse createddlTablesAtClientDb(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("client_Id") int client_Id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate jdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

			ClientJdbcInstance jdbcInstance = userService.getClientJdbcInstance("-1");
			jdbcTemplate = jdbcInstance.getClientAppdbJdbcTemplate();

			String ddlTablesApiUrl = authenticationEndPointUrl + "/getDashboardDDL_List/" + clientIdfromHeader;

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("X-Auth-Client-Token", AESConverter.encrypt(clientIdfromHeader));

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<Map> ddlTablesApiReponse = restTemplate.exchange(ddlTablesApiUrl, HttpMethod.POST, entity, Map.class);

			if( ddlTablesApiReponse != null )
			{
				Map<String, Object> map = (Map<String, Object>) ddlTablesApiReponse.getBody();

				String status = (String) map.get("status");
				if( status.equals("Success") )
				{
					List<String> ddlTableList = (List<String>) map.get("DDL_List");

					if( ddlTableList != null && ddlTableList.size() > 0 )
					{
						StringJoiner ddlTables = new StringJoiner(",");
						for (String ddlTableName : ddlTableList)
						{
							ddlTables.add("'" + ddlTableName + "'");
						}

						String getScriptsQuery = "select ddl_script from ddl_script where ddl_name in(" + ddlTables + ")";

						List<String> ddlTableScripts = etlAdminService.getDDlTableCreateScripts(getScriptsQuery, jdbcTemplate);

						if( ddlTableScripts != null && ddlTableScripts.size() > 0 )
						{
							String[] createQuerys = ddlTableScripts.toArray(new String[ddlTableScripts.size()]);

							etlAdminService.createDDlTables(createQuerys, clientJdbcTemplate);
						}
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerServerMasterById", method = RequestMethod.POST)
	public DataResponse getSchedulerServerMasterById(HttpServletRequest request, @RequestBody SchedulerMaster schedulerServerMaster, Locale locale)
	{
		LOGGER.debug("in getSchedulerServerMasterById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerMaster serverInfo = null;
		try
		{
			serverInfo = commonDao.getSchedulerMasterById(schedulerServerMaster.getId());
			if( serverInfo != null )
			{
				dataResponse.setObject(serverInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Details not found");
			}

		}
		catch ( Exception e )
		{
			LOGGER.error("error while getSchedulerServerMasterById() ", e);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientSchedularMapping", method = RequestMethod.POST)
	public DataResponse saveClientSchedularMapping(@RequestParam("client_Id") int client_Id, @RequestParam("schedularMasterIds") List<Integer> schedularMasterIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate = null;
		try
		{

			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance("-1");
			clientJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			// modification.setCreatedBy(clientId + "\n" +
			// request.getHeader("Browser-Detail"));
			int update = 0;
			// etlAdminService.deleteClientWSMappings(client_Id,
			// clientJdbcTemplate);
			update = etlAdminService.saveSchedularMastereMapping(client_Id, schedularMasterIds, modification, clientJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.clientSchedularMasterSavedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingSchedularMasterMapping", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateFilesetting", method = RequestMethod.POST)
	public DataResponse updateFilesetting(@RequestParam("client_Id") int client_Id, @RequestParam("multipart_file_enabled") Integer id, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(String.valueOf(client_Id));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			int update = 0;
			update = etlAdminService.updateFilesetting(id, clientAppDbJdbcTemplate);
			if( update > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.FilesettingUpdatedSuccessfully", null, locale));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingFilesetting", null, locale));
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/transeferDataERPtoClient", method = RequestMethod.POST)
	public DataResponse transeferDataERPtoClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("erpName") String erpName,
			@RequestParam("erpVersion") String erpVersion,
			@RequestParam("referenceClientId") String referenceClientId, 
			@RequestParam("referenceClientName") String referenceClientName, 
			@RequestParam("referenceUserId") String referenceUserId,
			Locale locale,
			HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate masterDbJdbcTemplate = null;
		try
		{

			ETLAdmin etlAdmin = new ETLAdmin();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			etlAdmin.setModification(modification);

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance masterJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			masterDbJdbcTemplate = masterJdbcInstance.getClientAppdbJdbcTemplate();
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(referenceClientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
		 
			int erpId = etlAdminService.getErpIdFromNameAndVersion(masterDbJdbcTemplate,erpName, erpVersion);
			
			String[] clientInfoArray = {referenceUserId,"yes",referenceClientId,referenceClientName,"ACTIVE"};
			
			Map<String, Integer> insertStatus = etlAdminService.transeferDataERPtoClient(referenceClientId,referenceClientName, erpId, referenceUserId, null, clientAppDbJdbcTemplate);
			LOGGER.info("insert ERP data to new clinet : " + insertStatus);
			if( insertStatus.size() > 0 )
			{
				etlAdminService.transeferDataERPtoClientForMasterTables(etlAdminService.getMasterTablesList(masterDbJdbcTemplate),erpId,referenceClientId, masterDbJdbcTemplate);
				etlAdminService.wishListToWishListAccessOnlyMapping( clientInfoArray ,clientAppDbJdbcTemplate);
				etlAdminService.updateErpToClientMigrationMapping(masterDbJdbcTemplate, Integer.parseInt(referenceClientId), erpId, Integer.parseInt(referenceUserId), etlAdmin);
			
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.datainsertederptoclientsuccessfuly", null, locale));
				dataResponse.setObject(insertStatus);
			}

		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(t.getMessage());
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getWishList", method = RequestMethod.GET)
	public DataResponse getWishListData(HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			List<Map<String, Object>>  wishListData = etlAdminService.getWishList(null);
			if(!wishListData.isEmpty()) {
				dataResponse.setObject(wishListData);
			}
		}catch( Throwable t ) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegettingwishlistdata", null, locale));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getERPList", method = RequestMethod.GET)
	public DataResponse getERPList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ERP> erpList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			erpList = etlAdminService.getERPList(clientAppDbJdbcTemplate);
			if( erpList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(erpList);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noErpFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/activeClients", method = RequestMethod.GET)
	public DataResponse getActiveClientsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<CloudClient> activeClientsList = null;
		try
		{
			activeClientsList = userService.getActiveClientsList();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(activeClientsList);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	@RequestMapping(value = "/getCmAndCsTablesInfoForClientToERP", method = RequestMethod.GET)
	public DataResponse getCmAndCsTablesInfoForClientToERP(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("referenceClientId") String referenceClientId,
			HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Map<String,Object> cmAndCsTablesInfoForClientToERP = new HashMap<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate masterDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			
			ClientJdbcInstance masterJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			masterDbJdbcTemplate = masterJdbcInstance.getClientAppdbJdbcTemplate();
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(referenceClientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
		    List<String> cmAndCstableList = etlAdminService.getCmAndCstableList(masterDbJdbcTemplate);   
 
		    for(String tableName : cmAndCstableList)
		    {
		    	cmAndCsTablesInfoForClientToERP.put(tableName, getTableColumnNamesAndData(tableName,clientAppDbJdbcTemplate,0));
		    }
		    
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(cmAndCsTablesInfoForClientToERP);
			
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getCmAndCsTablesInfoForERPToClient", method = RequestMethod.GET)
	public DataResponse getCmAndCsTablesInfoForERPToClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("erpId") int erpId,
			HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Map<String,Object> cmAndCsTablesInfoForClientToERP = new HashMap<>();
		JdbcTemplate masterDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			
			ClientJdbcInstance masterJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			masterDbJdbcTemplate = masterJdbcInstance.getClientAppdbJdbcTemplate();
			
		    List<String> cmAndCstableList = etlAdminService.getErpTableList(masterDbJdbcTemplate); 
		    
		    for(String tableName : cmAndCstableList)
		    {
		    	cmAndCsTablesInfoForClientToERP.put(tableName, getTableColumnNamesAndData(tableName,masterDbJdbcTemplate,erpId));
		    }
		    
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(cmAndCsTablesInfoForClientToERP);
			
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	public List<Object> getTableColumnNamesAndData(String tableName, JdbcTemplate clientAppDbJdbcTemplate,int erpId)
	{
		List<Object> previewtableList = null;
		String query = null;
		try
		{
			 if(erpId == 0)
			 {
				 query =  "select * from " + tableName;
			 }
			 else
			 {
				 query =  "select * from " + tableName + " where erp_id="+erpId;
			 }
			previewtableList = clientAppDbJdbcTemplate.query(query, new ResultSetExtractor<List<Object>>()
			{
				@Override
				public List<Object> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<Object> previewtableList = new ArrayList<>();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					while (rs.next())
					{
						Map<String,Object> tableRow = new HashMap<String,Object>();
						for (int i = 1; i <= columnCount; i++)
						{
							tableRow.put(rsmd.getColumnLabel(i), rs.getString(i));
						}
						previewtableList.add(tableRow);
					}
					return previewtableList;
				}
			});
		}
		catch ( DataAccessException ae )
		{
			LOGGER.error("Error while getDatabase", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return previewtableList;
	}

	@RequestMapping(value = "/mapClientToErp", method = RequestMethod.GET)
	public DataResponse mapClientToErp(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("erpId") int erpId, @RequestParam("referenceClientId") String referenceClientId, HttpServletRequest request)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Map<String, Object> cmAndCsTablesInfoForClientToERP = new HashMap<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate masterDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);

			ClientJdbcInstance masterJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			masterDbJdbcTemplate = masterJdbcInstance.getClientAppdbJdbcTemplate();

			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(referenceClientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			List<String> materTableList = etlAdminService.getMasterTablesList(masterDbJdbcTemplate);
			
			for (String tableName : materTableList)
			{
				String selectCountQuery = null;
				String selectQuery = null;
				if(tableName.equals("erp_minidwm_client_currency_mapping"))
				{
					selectCountQuery	= "select count(*) from " + tableName.replace("erp_", "")+" where clientId ="+referenceClientId;
					selectQuery = " ( select * ,"+ erpId +" from " + tableName.replace("erp_", "")+" where clientId ="+referenceClientId + " ) ";
				}
				else
				{
					selectCountQuery	= "select count(*) from " + tableName.replace("erp_", "")+" where client_id ="+Integer.parseInt(referenceClientId);
					selectQuery = " ( select * ,"+ erpId +" from " + tableName.replace("erp_", "")+" where client_id ="+referenceClientId + " ) ";
				}
				int selectQueryCount = masterDbJdbcTemplate.queryForObject(selectCountQuery, Integer.class);
				if( selectQueryCount > 0 )
				{
					masterDbJdbcTemplate.execute("truncate table " + tableName  + ";");
					masterDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
					String insertQry = "INSERT INTO " + tableName + selectQuery ;
					int count  = masterDbJdbcTemplate.update(insertQry);
					masterDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
					System.out.println(count +" Records inserted into table  " +tableName);
				}
			} 
			
			List<String> erpCmAndCstableList = etlAdminService.getErpTableList(masterDbJdbcTemplate);
			
			for (String tableName : erpCmAndCstableList)
			{
				String selectQuery = "select count(*) from " + tableName.replace("erp_", "");
				int selectQueryCount = clientAppDbJdbcTemplate.queryForObject(selectQuery, Integer.class);
				if( selectQueryCount > 0 )
				{
					masterDbJdbcTemplate.execute("truncate table " + tableName  + ";");
					String select = " ( select * ,"+ erpId +" from " + tableName.replace("erp_", "") + " ) ";
					List<Map<String,Object>> rows = clientAppDbJdbcTemplate.queryForList(select);
					int count = 0;
					for (Map<String,Object> row : rows) {
						StringBuffer insertQry = new StringBuffer();
						StringJoiner placeholders = new StringJoiner(",");
						StringJoiner columns = new StringJoiner(",");
						int size  = row.entrySet().size();
						int i = 1 ;
						for(Map.Entry<String,Object> entry : row.entrySet())
						{
							placeholders.add("?");
							if( i != size )
							{
								columns.add("`"+entry.getKey()+"`");
							}
							else
							{
								columns.add("`"+"erp_id"+"`");
							}
							i++;
						}
						insertQry.append("INSERT INTO " + tableName+ "("+ columns +")"+ " values ( ");
						insertQry.append(placeholders.toString()).append(" ); ");
					    masterDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
					   masterDbJdbcTemplate.execute(insertQry.toString(),new PreparedStatementCallback<Boolean>(){  
						    @Override  
						    public Boolean doInPreparedStatement(PreparedStatement ps)  
						            throws SQLException, DataAccessException {  
						        int  i = 1;
						    	for(Map.Entry<String,Object> entry : row.entrySet())
								{
						    		if( entry.getValue() == null )
									{
										ps.setObject(i++, null);
									}
						    		else if( entry.getValue() instanceof Boolean )
									{
										ps.setObject(i++, (boolean) entry.getValue());
									}
						    		else if( entry.getValue() instanceof Integer )
									{
										ps.setObject(i++,  entry.getValue());
									}
									else
									{
										ps.setObject(i++,entry.getValue().toString());
									}
								}
						        return ps.execute();  
						     }  
						    });  
					   masterDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
						 count++;
					}
					System.out.println(count +" Records inserted into table  " +tableName);
				}
				 
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Successfully mapped.");
			dataResponse.setObject(cmAndCsTablesInfoForClientToERP);

		}
		catch ( Throwable t )
		{
			t.printStackTrace();
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	@RequestMapping(value = "/getUserListFromClient", method = RequestMethod.GET)
	public DataResponse getUserListFromClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, 
			@RequestParam("referenceClientId") String referenceClientId,HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(referenceClientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			userList = etlAdminService.getUserListFromClient(clientAppDbJdbcTemplate,Integer.parseInt(referenceClientId));
			if( userList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(userList);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("No users found.");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/encryptWebServiceAuthParams", method = RequestMethod.POST)
	public DataResponse encryptWebServiceAuthParams(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EncryptWebserviceAuthParams encryptWebserviceAuthParams, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			String tableName = encryptWebserviceAuthParams.getTableName();
			String whereConditionColumnInfo = encryptWebserviceAuthParams.getWhereConditionColumnInfo();
			String columnInfo = encryptWebserviceAuthParams.getColumnInfo();
			
			StringJoiner columnInfoStringJoiner = new StringJoiner(",");
			
			JSONObject columnInfoJSONObject = new JSONObject(columnInfo);
			Iterator<String> clomnInfokeys = columnInfoJSONObject.keys();
			while (clomnInfokeys.hasNext())
			{
				String key = clomnInfokeys.next();
				String value = columnInfoJSONObject.getString(key);
				String encryptValue = EncryptionServiceImpl.getInstance().encrypt(value);
				columnInfoStringJoiner.add("`"+key+"`='"+encryptValue+"'");
			}
			
            StringJoiner whereConditionColumnInfoStringJoiner = new StringJoiner(",");
			
			JSONObject whereConditionColumnInfoJSONObject = new JSONObject(whereConditionColumnInfo);
	
			Iterator<String> whereConditionColumnKeys = whereConditionColumnInfoJSONObject.keys();
			while (whereConditionColumnKeys.hasNext())
			{
				String key = whereConditionColumnKeys.next();
				String value = whereConditionColumnInfoJSONObject.getString(key);
				whereConditionColumnInfoStringJoiner.add("`"+key+"`='"+value+"'");
			}
			
			String query = "UPDATE `"+tableName +"` SET "+ columnInfoStringJoiner.toString() +" WHERE "+ whereConditionColumnInfoStringJoiner.toString() ;
			
			int count = etlAdminService.encryptWebServiceAuthParams(clientAppDbJdbcTemplate, query);
			if( count != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Encrypted successfully.");
				messages.add(message);
				dataResponse.setObject(userList);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("query not executed.");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
}