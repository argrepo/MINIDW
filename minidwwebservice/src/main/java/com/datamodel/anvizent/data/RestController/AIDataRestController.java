package com.datamodel.anvizent.data.RestController;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.AIService;
import com.datamodel.anvizent.service.ScheduleService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIJobExecutionInfo;
import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.AISourceDefinition;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;


@RestController("AIDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/aiJobs")
@CrossOrigin
public class AIDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(AIDataRestController.class);

	
	@Autowired
	AIService aiService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping(value = "/aiSourceDefinition", method = RequestMethod.GET)
	public DataResponse aiSourceDefinition(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<AISourceDefinition> aiSourceDefList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiSourceDefList = aiService.getAiSourceDefinitionList(clientAppDbJdbcTemplate);
			if (aiSourceDefList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(aiSourceDefList);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Unable to fetch data");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/aiSourceDefinition/{sourceDefId}", method = RequestMethod.GET)
	public DataResponse getEltJobInfoByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("sourceDefId") int sourceDefId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		AISourceDefinition aiSourceDefinitionInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiSourceDefinitionInfo = aiService.getAiSourceInfoById(sourceDefId, clientAppDbJdbcTemplate);
			if (aiSourceDefinitionInfo != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(aiSourceDefinitionInfo);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("AI Source info not found");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteAISourceDefinition/{sourceDefId}", method = RequestMethod.DELETE)
	public DataResponse deleteAISourceDefinitionBYId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("sourceDefId") int sourceDefId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiService.deleteAISourceDefinitionBYId(sourceDefId, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveAISourceDefinition", method = RequestMethod.POST)
	public DataResponse saveAISourceDefinition(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AISourceDefinition aiSourceDefinition,HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int id = 0;
		try {
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			aiSourceDefinition.setModification(modification);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			//aiSourceDefinition.getAiModalId().toString();
			if(aiSourceDefinition.getSourceDefId() == null)
			    id = aiService.saveAISourceDefinition(aiSourceDefinition, clientAppDbJdbcTemplate);
			else
				id = aiService.updateSourceDefinition(aiSourceDefinition, clientAppDbJdbcTemplate);	
			if (id > 0 ) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(id);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Job tag info updation failed");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveBusinessModalInfo", method = RequestMethod.POST)
	public DataResponse saveBusinessModalInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody BusinessModal businessModal, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		 
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(false, false);
		
			int bmId = 0;
			
			String dropStagingQuery = "DROP TABLE IF EXISTS "  +businessModal.getaIStagingTable();

			String createStagingQuery = businessModal.getaIStagingTableScript();

			String[] dropAndCreateStagingQueries = {dropStagingQuery,createStagingQuery};

			aiService.dropAndCreateTable(dropAndCreateStagingQueries, clientStagingJdbcTemplate);


		    String dropAiQuery = "DROP TABLE IF EXISTS "  +businessModal.getaIILTable();

			String createAiQuery = businessModal.getaIILTableScript();

			String[] dropAndCreateAiQueries = {dropAiQuery,createAiQuery};

			aiService.dropAndCreateTable(dropAndCreateAiQueries, clientJdbcTemplate);


			 String dropAolQuery = "DROP TABLE IF EXISTS " +businessModal.getaIOLTable();

			String createAolQuery = businessModal.getaIOLTableScript();

			String[] dropAndCreateAolQueries = {dropAolQuery,createAolQuery};

			aiService.dropAndCreateTable(dropAndCreateAolQueries, clientJdbcTemplate);
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			businessModal.setModification(modification);
			
			bmId = aiService.saveBusinessModal(businessModal, clientAppDbJdbcTemplate);
			
			/*if(businessModal.getBmid() != null) {
				 aiService.deleteBusinessAIMapping(businessModal.getBmid(),clientAppDbJdbcTemplate);
				 aiService.saveBusinessAIMapping(businessModal,clientAppDbJdbcTemplate);
			}else {
				 businessModal.setBmid(bmId);
				 aiService.saveBusinessAIMapping(businessModal,clientAppDbJdbcTemplate);
			}*/
			if (bmId != 0) {
				dataResponse.setObject(bmId);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", ae);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getBusinessModelInfo", method = RequestMethod.GET)
	public DataResponse getBusinessModelInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<BusinessModal> businessModalInfo = aiService.getBusinessModelInfo(clientAppDbJdbcTemplate);
			dataResponse.setObject(businessModalInfo);
			if (businessModalInfo != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList",
						null, locale));
			}
		}
			  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getBusinessInfoById", method = RequestMethod.POST)
	public DataResponse getBusinessInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		BusinessModal businessModal = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			businessModal = aiService.getBusinessModalInfoById(id, clientAppDbJdbcTemplate);
			if (businessModal != null) {
				dataResponse.setObject(businessModal);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/getAIStagingTableInfoByBMID", method = RequestMethod.POST)
	public DataResponse getAIStagingTableInfoByBMID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("businessCaseName") String businessCaseName, HttpServletRequest request, Locale locale) {
		String stagingTablename = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			stagingTablename = aiService.getAIStagingTableByBusinessCaseName(businessCaseName, clientAppDbJdbcTemplate);
			if (stagingTablename != null) {
				dataResponse.setObject(stagingTablename);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/deleteBusinessInfoById", method = RequestMethod.POST)
	public DataResponse deleteBusinessInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int delete = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			delete = aiService.deleteBusinessInfoById(id, clientAppDbJdbcTemplate);
			if (delete != 0) {
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null,
						locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}

	
	@RequestMapping(value = "/uploadAIFiles", method = RequestMethod.POST)
	public DataResponse uploadAIFiles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale,
			@RequestParam("businessName") String businessName,
			@RequestParam("files") MultipartFile[] multipartFiles) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		File tempFile = null;
		try {
			if (multipartFiles != null && multipartFiles.length == 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			for (int i = 0; i < multipartFiles.length; i++) {
				MultipartFile multipartFile = multipartFiles[i];
				LOGGER.info(multipartFile.getOriginalFilename());
				// logger.info(Constants.);

				try {
					tempFile = CommonUtils.multipartToFile(multipartFile);
					String dir = CommonUtils.getAIFolderPath(StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), "."));
						dir = CommonUtils.getAIJobsPath();
						String pathName = dir + "/" + businessName;
						File file = new File(pathName);
						if (!file.exists()) {
							file.mkdirs();
						}
						String filePath = dir + "/"+businessName +"/" +multipartFile.getOriginalFilename();
						File existedFile = new File(filePath);
						FileCopyUtils.copy(tempFile,new File(filePath));
						
					//File existedFile = new File(dir + "/"+businessName +"/" + multipartFile.getOriginalFilename());
					// Taking backup of existed jar file
					if (existedFile.exists()) {
						String backupFolder = dir + Constants.Config.AI_BACKUP_FOLDER;
						CommonUtils.createDir(backupFolder);
						FileCopyUtils.copy(existedFile,
								new File(backupFolder + multipartFile.getOriginalFilename() + "_" + CommonDateHelper.formatDateAsTimeStamp(new Date())));
					}

				} finally {
					if (tempFile != null) {
						tempFile.delete();
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dlMasterCreatedOrUpdatedSuccessfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/saveAIJobUploadInfo", method = RequestMethod.POST)
	public DataResponse saveAIJobUploadInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AIJobUploadInfo aiJobUploadInfo, HttpServletRequest request, Locale locale) {
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int rJobId = -1;
		try{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			aiJobUploadInfo.setModification(modification);
			if(aiJobUploadInfo!= null){
				if(aiJobUploadInfo.getRid() != null){
					aiService.updateAIJobUploadInfo(aiJobUploadInfo, clientAppDbJdbcTemplate);
					rJobId = aiJobUploadInfo.getRid();
				}else{
					rJobId = aiService.saveAIJobUploadInfo(aiJobUploadInfo, clientAppDbJdbcTemplate);
				}
				
				if(rJobId > 0){
					
					if(aiJobUploadInfo.getRid() != null) {
						 aiService.deleteAIGenericContextParams(rJobId,clientAppDbJdbcTemplate);
					     aiService.deleteAISpecificContextParam(rJobId,clientAppDbJdbcTemplate);
					}
						aiJobUploadInfo.setRid(rJobId);
						int contetParamGenericMapId = aiService.saveAIGenericContextParams(aiJobUploadInfo, clientAppDbJdbcTemplate);
						int contetParamSpecificMapId = aiService.saveAISpecificContextParams(aiJobUploadInfo, clientAppDbJdbcTemplate);
						
						if(contetParamGenericMapId > 0 && contetParamSpecificMapId > 0) {
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreatedsuccessfully",null, locale));
						}
					
				}else{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				    message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreationFailed",null, locale));
				}
			}
			
		}catch(Throwable t){
			LOGGER.error("error while saving datavalidationType..."+t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dataValidationTypeCreationFailed",null, locale)+t.getMessage());
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			t.printStackTrace();
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	


	
	@RequestMapping(value = "/saveAiContextParameters", method = RequestMethod.POST)
	public DataResponse saveAiContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AIContextParameter aiContextParameter, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int save = 0;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			aiContextParameter.setModification(modification);
			 
			save = aiService.saveAiContextParameters(aiContextParameter, clientAppDbJdbcTemplate);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", ae);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getAiContextParameters", method = RequestMethod.GET)
	public DataResponse getAiContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<AIContextParameter> aIContextParameters = aiService.getAiContextParameters(clientAppDbJdbcTemplate);
			dataResponse.setObject(aIContextParameters);
			if (aIContextParameters != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList",
						null, locale));
			}
		}
			  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getAiContextParametersById", method = RequestMethod.POST)
	public DataResponse getAiContextParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		AIContextParameter aiContextParameter = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiContextParameter = aiService.getAiContextParametersById(id, clientAppDbJdbcTemplate);
			if (aiContextParameter != null) {
				dataResponse.setObject(aiContextParameter);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/deleteAiContextParametersById", method = RequestMethod.POST)
	public DataResponse deleteAiContextParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int delete = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			delete = aiService.deleteAiContextParametersById(id, clientAppDbJdbcTemplate);
			if (delete != 0) {
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null,
						locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/saveAiModelInfo", method = RequestMethod.POST)
	public DataResponse saveAiModelInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AIModel aiModel, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		int save = 0;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			// userObjectRemoved
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedBy(clientId);
			modification.setModifiedDateTime(new Date());
			aiModel.setModification(modification);
			 
			save = aiService.saveAiModel(aiModel, clientAppDbJdbcTemplate);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccesfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", ae);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getAiModelInfo", method = RequestMethod.GET)
	public DataResponse getAiModelInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<AIModel> aiModels = aiService.getAiModelInfo(clientAppDbJdbcTemplate);
			dataResponse.setObject(aiModels);
			if (aiModels != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList",
						null, locale));
			}
		}
			  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/geAiModelInfoById", method = RequestMethod.POST)
	public DataResponse geAiModelInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		AIModel aiModel = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiModel = aiService.getAiModelInfoById(id, clientAppDbJdbcTemplate);
			if (aiModel != null) {
				dataResponse.setObject(aiModel);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/deleteAiModelInfoById", method = RequestMethod.POST)
	public DataResponse deleteAiModelInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int delete = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			delete = aiService.deleteAiModelInfoById(id, clientAppDbJdbcTemplate);
			if (delete != 0) {
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null,
						locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/getAIJobsUploadList", method = RequestMethod.GET)
	public DataResponse getAIJobsUploadList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<AIJobUploadInfo> aiJobUploadList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiJobUploadList = aiService.getAIJobsUploadList(clientAppDbJdbcTemplate);
			if (aiJobUploadList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(aiJobUploadList);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Unable to fetch data");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getAIUploadedJobById", method = RequestMethod.POST)
	public DataResponse getAIUploadedJobById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		AIJobUploadInfo aiJobUploadInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			aiJobUploadInfo = aiService.getAIUploadedJobById(id, clientAppDbJdbcTemplate);
			if (aiJobUploadInfo != null) {
				dataResponse.setObject(aiJobUploadInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/deleteAIUploadedJobById", method = RequestMethod.POST)
	public DataResponse deleteAIUploadedJobById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int delete = 0;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			delete = aiService.deleteAIUploadedJobById(id, clientAppDbJdbcTemplate);
			if (delete != 0) {
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null,
						locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/getTablePreview", method = RequestMethod.POST)
	public DataResponse getTablePreview(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("query") String query,
			Locale locale, HttpServletRequest request) throws AnvizentCorewsException {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		Message message = new Message();
		List<Object> tablePreviewList = null;
		String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
		ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
		clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(false, false);
		
			if (StringUtils.isNotBlank(query)) {
				try {
					tablePreviewList = aiService.getTablePreview(query,clientJdbcTemplate);

					dataResponse.setObject(tablePreviewList);
				} catch (AnvizentRuntimeException ae) {
					LOGGER.error("",ae);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(MinidwServiceUtil.getErrorMessageString(ae));
					messages.add(message);
					dataResponse.setMessages(messages);
				} catch (Exception e) {
					LOGGER.error("",e);
					LOGGER.error("error while getTablePreview() ", e);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(MinidwServiceUtil.getErrorMessageString(e));
					messages.add(message);
					dataResponse.setMessages(messages);

				}
			}
			return dataResponse;
		}

	@RequestMapping(value = "/savePackageSchedule", method = RequestMethod.POST)
	public DataResponse saveScheduleInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody ClientData clientData, Locale locale,
			HttpServletRequest request) {
		
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		//long packageId = clientData.getUserPackage().getPackageId();
		Integer scheduleId = clientData.getSchedule().getScheduleId();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			clientData.setUserId(userId);
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Schedule scheduleInfo = clientData.getSchedule();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			modification.setIsActive(true);
			scheduleInfo.setModification(modification);
			clientData.setModification(modification);
			
		 
				String cronExpression = generateCronExpression(scheduleInfo);
				if (StringUtils.isBlank(cronExpression)) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					//message.setText("Unable to schedule with given inputs");
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToScheduleWithGivenInputs",
							null, locale));
					return dataResponse;
				}
				
				if (clientData.getSchedule().getRecurrencePattern().equals("once")) {
					clientData.getSchedule().setTimeZone(TimeZone.getDefault().getID());
					clientData.getSchedule().setScheduleStartDate(null);
					clientData.getSchedule().setScheduleStartTime(null);
					clientData.getSchedule().setScheduleEndDate(null);
				}
				
				// save or update schedule
				if (clientData.getSchedule().getScheduleId() != null && clientData.getSchedule().getScheduleId() != 0) {
					scheduleId = clientData.getSchedule().getScheduleId();
					clientData.getUserPackage().setScheduleType("Reschedule");
					scheduleService.updateSchedule(clientData, clientAppDbJdbcTemplate);
				} else {
					clientData.getUserPackage().setScheduleType("Schedule");
					scheduleId = scheduleService.saveSchedule(clientData, clientAppDbJdbcTemplate);
					clientData.getSchedule().setScheduleId((int)scheduleId);
				}
				clientData.getUserPackage().setIsScheduled(Boolean.TRUE);
				clientData.getUserPackage().setScheduleStatus(Constants.Status.STATUS_DONE);
				clientData.getSchedule().setScheduleId((int)scheduleId);
				clientData.setModification(modification);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.packageScheduleCreatedSuccessfully",
								new Object[] { clientData.getUserPackage().getPackageName() }, locale));
				
				//get cron expression using schedule
			
		} catch (Throwable e) {
			LOGGER.error("", e);;
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		
		
		return dataResponse;
	}
	
	public String generateCronExpression(Schedule schedule) throws Exception {

		String dateTime = schedule.getScheduleStartTime();
		String cronTemplate = "0 {minutes} {hours} {dayofthemonth} {month} {dayoftheweek}";
		
		if (schedule.getScheduleStartDate() != null 
				&& schedule.getRecurrencePattern() != null) {
			LOGGER.info("RecurrencePattern selected" + schedule.getRecurrencePattern());


			if (schedule.getRecurrencePattern().equals("hourly")) {
				
				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				// 0 13 0/1 {dayofthemonth} {month} {dayoftheweek}
				if (StringUtils.isNotBlank(schedule.getTypeOfHours())) {
					if (schedule.getTypeOfHours().equals("every")) {
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/" + schedule.getHoursToRun());
					} else {
						cronTemplate = StringUtils.replace(cronTemplate, "{hours}", schedule.getHoursToRun());
					}
				} else {
					cronTemplate = StringUtils.replace(cronTemplate, "{hours}", "0/1");
				}
				// 0 13 0/1 * {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "*");
				// 0 13 0/1 * * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 0/1 * * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			} else if (schedule.getRecurrencePattern().equals("daily")) {

				// 0 13 {hours} {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				// 0 13 15 {dayofthemonth} {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				// 0 13 15 0/1 {month} {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "1/1");
				// 0 13 15 0/1 * {dayoftheweek}
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				// 0 13 15 0/1 * *
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			}

			else if (schedule.getRecurrencePattern().equals("weekly")) {

				
				// 0 09 15 ? * MONDAY,FRIDAY
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", "?");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "*");
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", schedule.getDaysToRun());
				
				schedule.setCronExpression(cronTemplate);

			}

			else if (schedule.getRecurrencePattern().equals("monthly")) {


				// in case if start date and day of month does not match
				int dayofmonth = Integer.parseInt(schedule.getDayOfMonth());

				// String daysTorun = schedule.getDaysToRun();
				String monthsToRun = schedule.getMonthsToRun();


				// 0 30 15 17 1/1 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", dayofmonth+"");
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", "1/" + monthsToRun);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");
				
				schedule.setCronExpression(cronTemplate);
			}

			else if (schedule.getRecurrencePattern().equals("yearly")) {

				// 0 30 15 17 2 ?
				cronTemplate = StringUtils.replace(cronTemplate, "{minutes}", dateTime.split(":")[1]);
				cronTemplate = StringUtils.replace(cronTemplate, "{hours}", dateTime.split(":")[0]);
				cronTemplate = StringUtils.replace(cronTemplate, "{dayofthemonth}", schedule.getDayOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{month}", schedule.getMonthOfYear());
				cronTemplate = StringUtils.replace(cronTemplate, "{dayoftheweek}", "?");

				schedule.setCronExpression(cronTemplate);

			}
		} else {
			throw new Exception("Invalid Start date/Recurrence Pattern");
		}
		return cronTemplate;
	}
	
	@RequestMapping(value = "/getExecutionInfoByBusinessId", method = RequestMethod.POST)
	public DataResponse getExecutionInfoByBusinessId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		List<PackageExecution> packageExecution = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			packageExecution = aiService.getExecutionInfoByBusinessId(id, clientAppDbJdbcTemplate);
			if (packageExecution != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(packageExecution);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/uploadCommonAIFiles", method = RequestMethod.POST)
	public DataResponse uploadCommonAIFiles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale,
			@RequestParam("files") MultipartFile[] multipartFiles) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		File tempFile = null;
		try {
			if (multipartFiles != null && multipartFiles.length == 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			for (int i = 0; i < multipartFiles.length; i++) {
				MultipartFile multipartFile = multipartFiles[i];
				LOGGER.info(multipartFile.getOriginalFilename());
				try {
					tempFile = CommonUtils.multipartToFile(multipartFile);
					String dir = CommonUtils.getAIFolderPath(StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), "."));
						dir = CommonUtils.getAIJobsPath();
						String filePath =  Constants.Config.AI_COMMON_JOBS +multipartFile.getOriginalFilename();
						File existedFile = new File(filePath);
						FileCopyUtils.copy(tempFile,new File(filePath));
						
					//File existedFile = new File(dir + "/"+businessName +"/" + multipartFile.getOriginalFilename());
					// Taking backup of existed jar file
					if (existedFile.exists()) {
						String backupFolder = dir + Constants.Config.AI_BACKUP_FOLDER;
						CommonUtils.createDir(backupFolder);
						FileCopyUtils.copy(existedFile,
								new File(backupFolder + multipartFile.getOriginalFilename() + "_" + CommonDateHelper.formatDateAsTimeStamp(new Date())));
					}

				} finally {
					if (tempFile != null) {
						tempFile.delete();
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.dlMasterCreatedOrUpdatedSuccessfully", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getSourceQueryById", method = RequestMethod.POST)
	public DataResponse getSourceQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		String query = "";
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			query = aiService.getSourceQueryById(id, clientAppDbJdbcTemplate);
			if (query != null && query != "") {
				dataResponse.setObject(query);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetail", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}

		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getrJobExecutionInfo", method = RequestMethod.POST)
	public DataResponse getAiContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam(value="businessName") String businessName,@RequestParam(value="modelName") String modelName,Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientStagingDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientStagingDbJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			List<AIJobExecutionInfo> aIJobExecution = aiService.getrJobExecutionInfo(businessName,modelName,clientStagingDbJdbcTemplate);
			if (aIJobExecution != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(aIJobExecution);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList",
						null, locale));
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getAIErrorLogInfo", method = RequestMethod.POST)
	public DataResponse getAIErrorLogInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale, @RequestParam("errorLogName") String errorLogName)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		FileInputStream fstream = null;
		BufferedReader br = null;
		try
		{
			String dir = Constants.Config.AI_ERROR_LOGS;
			String filePath = dir + "/" + errorLogName;
			File file = new File(filePath);
			StringBuilder strBuilder = new StringBuilder();
			if( file.exists() )
			{
				fstream = new FileInputStream(file);
				br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;

				/* read log line by line */
				while ((strLine = br.readLine()) != null)
				{
					/* parse strLine to obtain what you want */
					strBuilder.append(strLine);
					strBuilder.append("\n");
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(strBuilder);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.fileSourcePathNotFound", null, locale));
			}

		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			if( fstream != null )
			{
				try
				{
					fstream.close();
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
			if( br != null )
			{
				try
				{
					br.close();
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getExecutionCommentsById", method = RequestMethod.POST)
	public DataResponse getExecutionCommentsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		String comments = "";
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			comments = aiService.getExecutionCommentsById(id, clientAppDbJdbcTemplate);
			if (comments != null && comments != "") {
				dataResponse.setObject(comments);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetComments", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetComments", null,
					locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}

		return dataResponse;
	}
	@RequestMapping(value = "/getaiCommonJobs", method = RequestMethod.GET)
	public DataResponse getaiCommonJobs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		List<String> FileNames = new ArrayList<>();
		Message message = new Message();
		try {
			File  folder = new File(Constants.Config.AI_COMMON_JOBS);
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	FileNames.add(file.getName());
			    }
			}
			
			dataResponse.setObject(FileNames);
			if (FileNames != null && FileNames.size()>0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetList",
						null, locale));
			}
		}
			  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/deleteCommonJob", method = RequestMethod.POST)
	public DataResponse deleteCommonJob(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("fileName") String fileName, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		boolean delete = false;
		try {
			File  file = new File(Constants.Config.AI_COMMON_JOBS +fileName);
			delete = file.delete();
			if (delete) {
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null,
						locale));
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToDelete", null,
						locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	}




