package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.DataValidationService;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;

@RestController
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/etlAdmin")
@CrossOrigin
public class DataValidationAdminServiceRestController {
	protected static final Logger logger = LoggerFactory.getLogger(DataValidationAdminServiceRestController.class);

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private DataValidationService dataValidationService;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private ETLAdminService etlAdminService;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping(value="/getDataValidationInfo", method = RequestMethod.POST)
	public DataResponse getAllDataValidations(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("validationTypeId") Integer validationTypeId,
			                      HttpServletRequest request, Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<DataValidation> dataValidationList =null; 
		try{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			dataValidationList = dataValidationService.getValidationInfo(validationTypeId,clientAppDbJdbcTemplate);
			
			dataResponse.setObject(dataValidationList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			
		}catch(Throwable t){
			logger.error("error while getAllDataValidations: "+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
		
	}
	
	@RequestMapping(value="/createDataValidation", method=RequestMethod.POST)
	public DataResponse createDataValidation(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,  @RequestBody DataValidation dataValidation,
			                       HttpServletRequest request, Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int createValdationId = -1;
		int dlCount = -1;
		int ilCount = -1;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			if(dataValidation!= null){
				Modification modification = new Modification(new Date());
				modification.setModifiedDateTime(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				dataValidation.setModification(modification);
				dataValidation.setDatabaseConnectorId(dataValidation.getDatabaseConnectorId() == null ? 0 : dataValidation.getDatabaseConnectorId()); 
				
				createValdationId = dataValidationService.saveDataValidation(dataValidation, clientAppDbJdbcTemplate);
				
				if(createValdationId > 0 && dataValidation.getDlInfoList() != null && dataValidation.getDlInfoList().size() > 0){
					dlCount = dataValidationService.saveScriptDlMapping(createValdationId, dataValidation, clientAppDbJdbcTemplate);
				}
				else if(createValdationId > 0 && dataValidation.getIlInfo() != null){
					ilCount = dataValidationService.saveScriptIlMapping(createValdationId, dataValidation, clientAppDbJdbcTemplate);
				}
				/*if(createValdationId > 0 && (dlCount > 0 || ilCount > 0)) {
					dataValidation.setScriptId(createValdationId);
					dataValidationService.saveDataValidationContextParams(dataValidation, clientAppDbJdbcTemplate);
				}*/
				
			}
			if(createValdationId > 0 && (dlCount > 0 || ilCount > 0)){
				dataResponse.setObject(dlCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationCreatedsuccessfully",null, locale));
				messages.add(message);
			}
			else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			    message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationCreationFailed",null, locale));
			    messages.add(message);
			}
			
		}catch(Throwable t){
			logger.error("error while saving datavalidation..."+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationCreationFailed",null, locale)+t.getMessage());
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			t.printStackTrace();
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/getValidationScriptData/{scriptId}", method=RequestMethod.GET)
	public DataResponse getValidationScriptData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("scriptId") int scriptId,
			    HttpServletRequest request, Locale locale){
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			DataValidation dataValidation = new DataValidation();
			JdbcTemplate clientAppDbJdbcTemplate = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			dataValidation = dataValidationService.getValidationScriptDataById(scriptId, clientAppDbJdbcTemplate);
			/*if(dataValidation != null) {
				List<String> contextParams = dataValidationService.getContextParamsByValidationId(dataValidation.getScriptId(), clientAppDbJdbcTemplate);
				dataValidation.setContextParamsList(contextParams);
			}
			if(StringUtils.isNotEmpty(dataValidation.getJobFileNames())) {
				String[] jars = dataValidation.getJobFileNames().split(",");
				 dataValidation.setJobFiles(Arrays.asList(jars));
			}*/
			
			
			//dlInfo = dataValidationService.getDLInfoByValidationScriptId(scriptId, clientAppDbJdbcTemplate);
			
			if(dataValidation != null){
				dataResponse.setObject(dataValidation);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyLoadedValidationScripts",  null, locale));
				messages.add(message);
			}
			
		}catch(Exception e){
			logger.error("error while reading validationdata  "+e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",null, locale)+e.getMessage());
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/updateValidationScript", method=RequestMethod.POST)
	public DataResponse updateValidationScriptData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,  @RequestBody DataValidation dataValidation,
            HttpServletRequest request, Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int updateValidationCount= -1;
		int dlCount = -1;
		int ilCount =-1;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			if(dataValidation != null && dataValidation.getScriptId() > 0){
				Modification modification = new Modification(new Date());
				modification.setModifiedDateTime(new Date());
				modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				modification.setModifiedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
				dataValidation.setModification(modification);
				updateValidationCount = dataValidationService.updateDataValidationScript(dataValidation, clientAppDbJdbcTemplate);
				
				if(updateValidationCount > 0 && dataValidation.getDlInfoList() != null && dataValidation.getDlInfoList().size() > 0){
					dataValidationService.deleteScriptDlMapping(dataValidation.getScriptId(), clientAppDbJdbcTemplate);
					dlCount = dataValidationService.saveScriptDlMapping(dataValidation.getScriptId(), dataValidation, clientAppDbJdbcTemplate);
				}
				else if(updateValidationCount > 0 && dataValidation.getIlInfo() != null){
					dataValidationService.deleteScriptilMapping(dataValidation.getScriptId(), clientAppDbJdbcTemplate);
					ilCount = dataValidationService.saveScriptIlMapping(dataValidation.getScriptId(), dataValidation, clientAppDbJdbcTemplate);
				}
				
				/*if(updateValidationCount > 0 && (dlCount > 0 || ilCount > 0)) {
					dataValidationService.deleteValidationContextParams(dataValidation.getScriptId(), clientAppDbJdbcTemplate);
					dataValidationService.saveDataValidationContextParams(dataValidation, clientAppDbJdbcTemplate);
				}*/
				
			}
			if(updateValidationCount > 0 && (dlCount > 0 || ilCount > 0)){
				dataResponse.setObject(dlCount);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationUpdatedsuccessfully",null, locale));
				messages.add(message);
			}
			else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.dataValidationUpdationFailed",null, locale));
				messages.add(message);
			}
			
		}catch(Throwable t){
			logger.error("error while saving datavalidation..."+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.dataValidationUpdationFailed",null, locale)+t.getMessage());
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getClientMappingILInfo", method = RequestMethod.GET)
	public DataResponse getILInfoByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {

		logger.debug("in getClientMappingILInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ILInfo> ilInfo = etlAdminService.getILInfoByClientId(Integer.parseInt(clientIdfromHeader),	clientAppDbJdbcTemplate);
			dataResponse.setObject(ilInfo);
			if (ilInfo != null && ilInfo.size() > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.errorWhileGettingILs", null, locale));
			}
		}catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getClientMappingDLInfo", method = RequestMethod.GET)
	public DataResponse getDLInfoByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {

		logger.debug("in getClientMappingDLInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<DLInfo> dlInfo = etlAdminService.getDLInfoByClientId(Integer.parseInt(clientIdfromHeader), clientAppDbJdbcTemplate);
			dataResponse.setObject(dlInfo);
			if (dlInfo != null && dlInfo.size() > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDLs", null, locale));
			}
		}catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/viewValidationScriptByScriptId", method = RequestMethod.POST)
	public DataResponse getValidationScriptBySCriptId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale,
			@RequestParam("scriptId") Integer scriptId){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DataValidation dataValidation = null;
		 try{
			 String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				dataValidation = dataValidationService.getValidationScriptByScriptId(scriptId, clientAppDbJdbcTemplate);
				
				if(dataValidation != null){
					dataResponse.setObject(dataValidation);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyLoadedValidationScripts",  null, locale));
					messages.add(message);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.validationScripts",  null, locale));
					messages.add(message);
				}
			}catch(Exception e){
				logger.error("error while reading validationdata  "+e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",null, locale)+e.getMessage());
				messages.add(message);
				MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
			}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getValidationTypeJobsandDependencyJars/{validationtypeId}", method = RequestMethod.GET)
	public DataResponse validationTypeJobsandDependencyJars(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("validationtypeId") int validationTypeId, HttpServletRequest request, Locale locale) {
		
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DataValidationType dataValidation = null;
		
		 try{
			 String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				dataValidation = dataValidationService.getDataValidationTypeJobsandDependencyJars(validationTypeId, clientAppDbJdbcTemplate);
				
				if(dataValidation != null){
					dataResponse.setObject(dataValidation);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyLoadedValidationScripts",  null, locale));
					messages.add(message);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unavilableDataValidationForDependencyJars",  null, locale));
					messages.add(message);
					
				}
			}catch(Exception e){
				logger.error("error while reading validationdata  "+e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",null, locale)+e.getMessage());
				messages.add(message);
				MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
			}
		dataResponse.setMessages(messages);
		
		return dataResponse;
	}
	
	@RequestMapping(value="/getDataValidationTypesByvalidationId/{id}", method = RequestMethod.GET)
	public DataResponse getDataValidationTypesByvalidationId(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,@PathVariable("id")Integer id,HttpServletRequest request,Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate =null;
		JdbcTemplate clientAppDbJdbcTemplate =null;
		List<DataValidationType> dataValidationTypeList = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			dataValidationTypeList = dataValidationService.getDataValidationTypesByvalidationId(clientIdfromHeader,id, clientAppDbJdbcTemplate);
			if(dataValidationTypeList != null && dataValidationTypeList.size() > 0){
				dataResponse.setObject(dataValidationTypeList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDataValidationTypeList", null, locale));
				messages.add(message);
			}
		} catch (AnvizentRuntimeException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidationTypes", null, locale));
			messages.add(message);
		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidationTypes", null, locale));
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value="/getAllDataValidationTypes", method = RequestMethod.GET)
	public DataResponse getDataValidationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,HttpServletRequest request,Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate =null;
		JdbcTemplate clientAppDbJdbcTemplate =null;
		List<DataValidationType> dataValidationTypeList = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			dataValidationTypeList = dataValidationService.getAllDataValidationTypes(clientIdfromHeader, clientAppDbJdbcTemplate);
			if(dataValidationTypeList != null && dataValidationTypeList.size() >0){
				dataResponse.setObject(dataValidationTypeList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
			}else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.noRecordsFound", null, locale));
				messages.add(message);
			}
		} catch (AnvizentRuntimeException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidationTypes", null, locale));
			messages.add(message);
		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidationTypes", null, locale));
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveDataValidationTypes", method = RequestMethod.POST)
	public DataResponse saveDataValidationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody DataValidationType dataValidationType, HttpServletRequest request, Locale locale) {
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int dataValidationTypeId = -1;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int contextParamsValue = -1;
			if(dataValidationType!= null){
				if(dataValidationType.getValidationTypeId()>0){
					dataValidationTypeId = dataValidationService.updateDataValidationTypes(dataValidationType, clientAppDbJdbcTemplate);
				}else{
					dataValidationTypeId = dataValidationService.saveDataValidationTypes(dataValidationType, clientAppDbJdbcTemplate);
				}
				
				if(dataValidationTypeId > 0){
					dataValidationType.setValidationTypeId(dataValidationTypeId);
					Modification modification = new Modification(new Date());
					modification.setCreatedBy(clientId);
					dataValidationType.setModification(modification);
					modification.setModifiedBy(clientId);
					modification.setModifiedTime(CommonDateHelper.formatDateAsString(new Date()));
					
					int deleteDataValidationContextParam = dataValidationService.deleteValidationContextParams(dataValidationTypeId,clientAppDbJdbcTemplate);
					if(deleteDataValidationContextParam >=0){
						contextParamsValue = dataValidationService.saveDataValidationContextParams(dataValidationType, clientAppDbJdbcTemplate);
					}
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreatedsuccessfully",null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				    message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreationFailed",null, locale));
				    messages.add(message);
				    dataResponse.setMessages(messages);
				}
			}
			if(contextParamsValue > 0){
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreatedsuccessfully",null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			    message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreationFailed",null, locale));
			    messages.add(message);
			    dataResponse.setMessages(messages);
			}
			
		}catch(Throwable t){
			logger.error("error while saving datavalidationType..."+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreationFailed",null, locale)+t.getMessage());
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			t.printStackTrace();
		}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/getDataValidationTypeById/{id}", method = RequestMethod.GET)
	public DataResponse getDataValidationTypeById(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,@PathVariable("id")int id,HttpServletRequest request,Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DataValidationType dataValidationTypes = null;
		 try{
			 String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				dataValidationTypes = dataValidationService.getDataValidationTypeNameById(id, clientAppDbJdbcTemplate);
				
				if(dataValidationTypes != null){
					dataResponse.setObject(dataValidationTypes);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyLoadedValidationScripts",  null, locale));
					messages.add(message);
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",  null, locale));
					messages.add(message);
				}
			}catch(Exception e){
				logger.error("error while reading validationdata  "+e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveDataValidationInfo",null, locale)+e.getMessage());
				messages.add(message);
				MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
			}
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value="/getDatavalidation", method=RequestMethod.GET)
	public DataResponse getDataValidation(@PathVariable(Constants.PathVariables.CLIENT_ID)String clientId,HttpServletRequest request,Locale locale){
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientJdbcTemplate =null;
		JdbcTemplate clientAppDbJdbcTemplate =null;
		List<Map<String, String>> dataValidation = null;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			dataValidation = dataValidationService.getDataValidation(clientAppDbJdbcTemplate);
			
			Map<Object, Object> dataValidationTypeMap = new LinkedHashMap<>();
			if(dataValidation != null){
			for (Map<String, String> validation : dataValidation) {
					for(Map.Entry<String, String>entry :validation.entrySet()){
						dataValidationTypeMap.put(entry.getKey(), entry.getValue());
					}
			}
			dataResponse.setObject(dataValidationTypeMap);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			}
		} catch (AnvizentRuntimeException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidation", null, locale));
			messages.add(message);
		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingDataValidation", null, locale));
			messages.add(message);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.addMessages(messages);
		return dataResponse;
	}
}
