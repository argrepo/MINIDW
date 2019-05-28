package com.datamodel.anvizent.data.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ScriptRunner;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.TableScriptsForm;



@RestController("clientInstantScriptExecutionDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin/instantScriptExcecution")
@CrossOrigin
public class ClientInstantScriptExecutionDataRestController {
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private ETLAdminService etlAdminService;

	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	protected static final Log LOGGER = LogFactory.getLog(ClientInstantScriptExecutionDataRestController.class);

	@RequestMapping(value = "/executeClientRespectiveScripts", method = RequestMethod.POST)
	public DataResponse executeClientRespectiveScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody TableScriptsForm tableScriptsForm,
			Locale locale,
			HttpServletRequest request) {
		LOGGER.debug("in executeClientRespectiveScripts()");

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String fileName=null;
      try{
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(clientId);
		tableScriptsForm.setModification(modification);
		tableScriptsForm.setExecutionType("Instant Execution");
		List<String> clientIdsList = tableScriptsForm.getClientIdList();
			if (clientIdsList != null && !StringUtils.isBlank(tableScriptsForm.getSqlScript())) { 
				fileName = CommonUtils.writeScriptFile(tableScriptsForm.getSqlScript());
				int instantScriptId = etlAdminService.clientsInstantScriptExecution(tableScriptsForm,commonJdbcTemplate);
				if (instantScriptId > 0) {
					for (String client : clientIdsList) {
						Connection con = null;
						String erroMsg = null;
						try {
							ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(client);
							JdbcTemplate clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
							con = clientAppDbJdbcTemplate.getDataSource().getConnection();
							if (con != null) {
								ScriptRunner runner = new ScriptRunner(con, false, false);
								erroMsg = runner.runScript(new BufferedReader(new FileReader(fileName)));
							}
							if (StringUtils.isEmpty(erroMsg)) {
								erroMsg = messageSource.getMessage(
										"anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale);
							}
						} catch (IOException | SQLException e) {
							erroMsg = MinidwServiceUtil.getErrorMessageString(e);
						} catch (Exception e) {
							erroMsg = MinidwServiceUtil.getErrorMessageString(e);
						} finally {
							if (con != null) {
								con.close();
							}
						}
						tableScriptsForm.setClientInstantExecutionId(tableScriptsForm.getClientsInstantScriptId());
						tableScriptsForm.setExecution_status_msg(erroMsg);
						tableScriptsForm.setExecution_status(false);
						tableScriptsForm.setClientId(Integer.parseInt(client));
						tableScriptsForm.setClientInstantExecutionId(instantScriptId);
						etlAdminService.instantscriptExecutionOfClient(tableScriptsForm, commonJdbcTemplate);
					}
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.text.unableToInsertInstantScript", null, locale));
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messageSource.getMessage("anvizent.message.success.text.tableScriptsExecutedSuccessfully", null, locale);
			 }else{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.instantScriptOrClientsAreEmpty", null, locale));
		}
       }catch(Exception e){
    	   message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
		   message.setText(MinidwServiceUtil.getErrorMessageString(e));
       }finally{
    	   try {
    		   if(fileName != null){
    			   Files.deleteIfExists(Paths.get(fileName));
    		   }
		 } catch (IOException e) {
			e.printStackTrace();
		}
       }
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getClientsInstantScriptExecutionData", method = RequestMethod.GET)
	public DataResponse getClientsInstantScriptExecutionData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,HttpServletRequest request,Locale locale) {

		LOGGER.info("in getClientInstantScriptExecutionData()");
		List<TableScriptsForm> tableScriptForm = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {

			tableScriptForm = etlAdminService.getClientsInstantScriptExecution(commonJdbcTemplate);
			if (tableScriptForm.size() > 0) {
				dataResponse.setObject(tableScriptForm);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.instantScriptExecutionDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/viewClientInstantExecutionResults", method = RequestMethod.POST)
	public DataResponse getInstantScriptExecutionOfClient(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody TableScriptsForm tableScriptsForm,
			Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in viewClientInstantExecutionResults()");
		List<TableScriptsForm> tableScriptForm = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			tableScriptForm = etlAdminService.getInstantScriptExecutionOfClient(tableScriptsForm.getClientsInstantScriptId(), commonJdbcTemplate);
			if (tableScriptForm.size() > 0) {
				dataResponse.setObject(tableScriptForm);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.instantExecutionResultsNotFound", null, locale));
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/viewSqlScriptByExecutionId", method = RequestMethod.POST)
	public DataResponse viewSqlScriptByExecutionId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody TableScriptsForm tableScriptsForm,
			Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getclientSpecificScriptExecution()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String sqlScript = null;
		try {
			sqlScript = etlAdminService.getSqlScriptByExecutionId(tableScriptsForm.getClientsInstantScriptId(), commonJdbcTemplate);
			if (sqlScript != null) {
				dataResponse.setObject(sqlScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.sqlScriptNotFoundForScriptId", null, locale) + tableScriptsForm.getClientsInstantScriptId());
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
}
