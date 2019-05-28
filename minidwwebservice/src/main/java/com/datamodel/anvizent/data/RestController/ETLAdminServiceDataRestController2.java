/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import static com.anvizent.minidw.service.utils.MinidwServiceUtil.getErrorMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateStatusUpdationException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.helper.ScriptRunner;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.MigrationService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rakesh.gajula
 *
 */
@RestController("clientMigrationRestController")
@RequestMapping("/clientMigration")
@CrossOrigin
public class ETLAdminServiceDataRestController2 {

	protected static final Log LOGGER = LogFactory.getLog(ETLAdminServiceDataRestController2.class);

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
	MigrationService migrationService;
	
	@Autowired
	PackageService packageService;
	
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	ExecutionProcessor executionProcessor;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@Autowired
	RestTemplate restTemplate; 
 
	@RequestMapping(value="/getAllDefaultTemplatesInfoPortal", method=RequestMethod.POST, produces =  "application/json")
	public DataResponse s3fileDownload(@RequestParam(value="client_id") String client_id, Locale locale){
		DataResponse dataResponse = new DataResponse(); 
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance("-1");
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<DefaultTemplates> defaultTemplates = etlAdminService
					.getAllDefaultTemplatesInfo(clientAppDbJdbcTemplate);
			if (defaultTemplates != null && defaultTemplates.size() > 0) {
				dataResponse.setObject(defaultTemplates);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.defaultTemplatesNotFound", null, locale));
			}

		}   catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getDefaultTemplateMasterMappedData", method = RequestMethod.POST)
	public DataResponse getDefaultTemplateMasterMappedData(
			@RequestParam("client_id") int client_id,
			@RequestParam("templateId") int templateId, @RequestParam("mappingType") String mappingType, Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(String.valueOf(client_id));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			DataResponse data = etlAdminService.getDefaultTemplateMasterMappedData(templateId, mappingType,
					clientAppDbJdbcTemplate);
			if (data != null) {
				dataResponse.setObject(data.getObject());
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			}
		}  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getS3BucketInfoById", method = RequestMethod.POST)
	public DataResponse getS3BucketInfoById(@RequestParam("client_id") int client_id,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		S3BucketInfo s3Info = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(String.valueOf(client_id));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			s3Info = etlAdminService.getS3BucketInfoById(id, clientAppDbJdbcTemplate);
			if (s3Info != null) {
				dataResponse.setObject(s3Info);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveS3Details", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/getAllmappingInfoById", method = RequestMethod.POST)
	public DataResponse getAllmappingInfo(@RequestParam("client_id") int client_id, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(String.valueOf(client_id));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			AllMappingInfo allMappingInfo = etlAdminService.getAllmappingInfoById(client_id, clientAppDbJdbcTemplate,commonJdbcTemplate);
			dataResponse.setObject(allMappingInfo);
			if (allMappingInfo != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingAllMappingInfo",
						null, locale));
			}
		}  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveClientVerticalMapping", method = RequestMethod.POST)
	public DataResponse saveClientVerticalMapping(@RequestBody ClientData clientData, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientData.getUserId());
			 clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

		     Modification modification = new Modification(new Date());
			modification.setCreatedBy(request.getHeader("Browser-Detail"));
			clientData.setModification(modification);
			int update = 0;
			etlAdminService.deleteClientVerticalMappingById(Integer.parseInt(clientData.getUserId()), clientAppDbJdbcTemplate);

			update = etlAdminService.saveClientVerticalMapping(clientData, clientAppDbJdbcTemplate);
			etlAdminService.deleteClientVerticalMappedDLsByClientId(Integer.parseInt(clientData.getUserId()), clientAppDbJdbcTemplate);

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
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/saveDlClientidMapping", method = RequestMethod.POST)
	public DataResponse saveClientAndDls(@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int mappingCount = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(eTLAdmin.getClientId());
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
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
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveClientConnectorMapping", method = RequestMethod.POST)
	public DataResponse saveClientConnectorMapping( @RequestParam("client_Id") String client_Id, @RequestParam("connectorId") List<String> connectorIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(client_Id);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
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
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveClientWebserviceMapping", method = RequestMethod.POST)
	public DataResponse saveClientWebserviceMapping(@RequestParam("client_Id") int client_Id, @RequestParam("webServiceIds") List<Integer> webServiceIds, HttpServletRequest request, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(String.valueOf(client_Id));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
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
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/createClientCurrencyMapping", method = RequestMethod.POST)
	public DataResponse createClientCurrencyMapping(@RequestBody ClientCurrencyMapping clientCurrencyMapping, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance("-1");
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			//modification.setCreatedBy(clientId);
			//modification.setModifiedBy(clientId);
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

			LOGGER.error("error while createClientCurrencyMapping() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
			messages.add(message);
		}
		catch ( AnvizentCorewsException e )
		{

			LOGGER.error("error while createClientCurrencyMapping() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorOccuredWhileConnectingToClientSchemaerrorDetails", null, locale) + " " + e.getLocalizedMessage());
			messages.add(message);
		}
		catch ( Throwable t )
		{
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveClientMapping", method = RequestMethod.POST)
	public DataResponse saveClientMapping(@RequestBody S3BucketInfo s3BucketInfo, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try
		{
			
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance("-1");
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			//modification.setCreatedBy(clientId);
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
		catch ( Throwable t )
		{
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveClientTableScriptsMapping", method = RequestMethod.POST)
	public DataResponse saveClientTableScriptsMapping(Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScripts)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int saveCount = -1;

		int delete = 0;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(tableScripts.getClientId().toString());
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( tableScripts != null )
			{

				Modification modification = new Modification(new Date());
				//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
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
			getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/clientTableScriptsExecution", method = RequestMethod.POST)
	public DataResponse clientTableScriptsExecution(Locale locale, HttpServletRequest request, @RequestBody TableScriptsForm tableScriptsForm)
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
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(tableScriptsForm.getClientId().toString());
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
					//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));

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
			getErrorMessage(message, "ERROR", t);
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
			//modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			int update = 0;
			//etlAdminService.deleteClientWSMappings(client_Id, clientJdbcTemplate);
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
			getErrorMessage(message, "ERROR", t);
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

			Modification modification = new Modification(new Date());
			int update = 0;
			update = etlAdminService.updateFilesetting( id,  clientAppDbJdbcTemplate);
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
			getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
}