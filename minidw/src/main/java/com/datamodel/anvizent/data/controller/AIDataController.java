package com.datamodel.anvizent.data.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.AISourceDefinition;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.User;
@RestController("AIDataController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/aiJobs")
@CrossOrigin
public class AIDataController {

	@Autowired
	@Qualifier("aiRestTemplateUtilities")
	private RestTemplateUtilities aiRestUtilities;
	protected static final Log LOG = LogFactory.getLog(AIDataController.class);
	
	
	
	@RequestMapping(value = "/saveBusinessModalInfo", method = RequestMethod.POST)
	public DataResponse saveAiDataInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody BusinessModal businessModal, HttpServletRequest request) {
		
		return aiRestUtilities.postRestObject(request, "/saveBusinessModalInfo", businessModal, clientId);
	}
	
	@RequestMapping(value = "/getBusinessInfoById", method = RequestMethod.POST)
	public DataResponse getBusinessInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getBusinessInfoById", map, clientId);
	}
	@RequestMapping(value = "/getAIStagingTableInfoByBMID", method = RequestMethod.POST)
	public DataResponse getAIStagingTableInfoByBMID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("businessCaseName") String businessCaseName, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("businessCaseName", businessCaseName);
		return aiRestUtilities.postRestObject(request, "/getAIStagingTableInfoByBMID", map, clientId);
	}
	@RequestMapping(value = "/deleteBusinessInfoById", method = RequestMethod.POST)
	public DataResponse deleteBusinessInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/deleteBusinessInfoById", map, clientId);
	}


	
	@RequestMapping(value = "/saveAISourceDefinition", method = RequestMethod.POST)
	public DataResponse saveEltJobTagInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AISourceDefinition aiSourceDefinition, HttpServletRequest request) {
		return aiRestUtilities.postRestObject(request, "/saveAISourceDefinition", aiSourceDefinition, clientId);
	}

	@RequestMapping(value = "/aiSourceDefinition/{sourceDefId}", method = RequestMethod.GET)
	public DataResponse getAISourceDefinition(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("sourceDefId") Integer sourceDefId, Locale locale, HttpServletRequest request) {
		return aiRestUtilities.getRestObject(request, "/aiSourceDefinition/{sourceDefId}", clientId, sourceDefId);
	}
	
	@RequestMapping(value = "/deleteAISourceDefinition/{sourceDefId}", method = RequestMethod.DELETE)
	public DataResponse deleteAISourceDefinition(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("sourceDefId") Integer sourceDefId, Locale locale, HttpServletRequest request) {
		return aiRestUtilities.deleteRestObject(request, "/deleteAISourceDefinition/{sourceDefId}", clientId, sourceDefId);
	}
	
	@RequestMapping(value = "/saveAIFileUpload/{businessName}", method = RequestMethod.POST)
	public DataResponse saveAIFileUpload(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable(value = "businessName") String businessName,
			@RequestParam(value = "jobFile", required = false) List<MultipartFile> jobFile,
			
		    HttpServletRequest request,Locale locale) {
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		boolean isUploaded = Boolean.FALSE;
		MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
		try {
			if (jobFile != null) {
				if (jobFile.size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : jobFile) {
						if(file.getOriginalFilename() != "") {
							String tempFileName = tempFolderName + file.getOriginalFilename();
							try {
								CommonUtils.createFile(tempFolderName, tempFileName, file);
							} catch (Exception e) {
								e.printStackTrace();
							}
							filesMap.add("files", new FileSystemResource(tempFileName));
						}
						
					}
					filesMap.add("businessName", businessName);
					DataResponse uploadedFileResponse = aiRestUtilities.postRestObject(request,
							"/uploadAIFiles", filesMap, user.getUserId());
					if (uploadedFileResponse.getHasMessages()
							&& uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText("success");
					}
				}
			} else {
				isUploaded = false;
			}

		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.addMessages(messages);
		}
		messages.add(message);
		dataResponse.addMessages(messages);
		dataResponse.setObject(isUploaded);
		return dataResponse;
	}

	
	@RequestMapping(value = "/saveAIJobUploadInfo", method = RequestMethod.POST)
	public DataResponse saveAIJobUploadFileTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody AIJobUploadInfo aiUploadInfo,HttpServletRequest request,
			Locale locale) {
		
		return	aiRestUtilities.postRestObject(request, "/saveAIJobUploadInfo", aiUploadInfo,clientId);

	}
	
	@RequestMapping(value = "/saveAiContextParameters", method = RequestMethod.POST)
	public DataResponse saveAiContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody AIContextParameter aiContextParameter, HttpServletRequest request) {
		
		return aiRestUtilities.postRestObject(request, "/saveAiContextParameters", aiContextParameter, clientId);
	}
	
	@RequestMapping(value = "/getAiContextParametersById", method = RequestMethod.POST)
	public DataResponse getAiContextParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getAiContextParametersById", map, clientId);
	}
	
	@RequestMapping(value = "/deleteAiContextParametersById", method = RequestMethod.POST)
	public DataResponse deleteAiContextParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/deleteAiContextParametersById", map, clientId);
	}
	
	@RequestMapping(value = "/saveAiModelInfo", method = RequestMethod.POST)
	public DataResponse saveAiModelInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody AIModel aiModel, HttpServletRequest request) {
		
		return aiRestUtilities.postRestObject(request, "/saveAiModelInfo", aiModel, clientId);
	}
	@RequestMapping(value = "/geAiModelInfoById", method = RequestMethod.POST)
	public DataResponse geAiModelInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/geAiModelInfoById", map, clientId);
	}
	
	@RequestMapping(value = "/deleteAiModelInfoById", method = RequestMethod.POST)
	public DataResponse deleteAiModelInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/deleteAiModelInfoById", map, clientId);
	}
	
	@RequestMapping(value = "/getAIUploadedJobById", method = RequestMethod.POST)
	public DataResponse getAIUploadedJobById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getAIUploadedJobById", map, clientId);
	}
	
	@RequestMapping(value = "/deleteAIUploadedJobById", method = RequestMethod.POST)
	public DataResponse deleteUploadedJobById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/deleteAIUploadedJobById", map, clientId);
	}

	
	@RequestMapping(value = "/getTablePreview", method = RequestMethod.POST)
	public DataResponse getTablePreview(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("query") String query, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("query", query);
		return aiRestUtilities.postRestObject(request, "/getTablePreview", map, clientId);
	}
	
	@RequestMapping(value = "/savePackageSchedule", method = RequestMethod.POST)
	public DataResponse savePackageSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody ClientData clientData, HttpServletRequest request) {
		
		return aiRestUtilities.postRestObject(request, "/savePackageSchedule", clientData, clientId);
	}
	
	@RequestMapping(value = "/getExecutionInfoByBusinessId", method = RequestMethod.POST)
	public DataResponse getExecutionInfoByBusinessId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getExecutionInfoByBusinessId", map, clientId);
	}
	@RequestMapping(value = "/aiModelInfoList", method = RequestMethod.GET)
	public DataResponse getAlModelinfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		return aiRestUtilities.getRestObject(request, "/getAiModelInfo",
				user.getUserId());
	}
	
	@RequestMapping(value = "/saveCommonAIFileUpload", method = RequestMethod.POST)
	public DataResponse saveCommonAIFileUpload(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "jobFile", required = false) List<MultipartFile> jobFile,
			
		    HttpServletRequest request,Locale locale) {
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		boolean isUploaded = Boolean.FALSE;
		MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
		try {
			if (jobFile != null) {
				if (jobFile.size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : jobFile) {
						if(file.getOriginalFilename() != "") {
							String tempFileName = tempFolderName + file.getOriginalFilename();
							try {
								CommonUtils.createFile(tempFolderName, tempFileName, file);
							} catch (Exception e) {
								e.printStackTrace();
							}
							filesMap.add("files", new FileSystemResource(tempFileName));
						}
						
					}
					DataResponse uploadedFileResponse = aiRestUtilities.postRestObject(request,
							"/uploadCommonAIFiles", filesMap, user.getUserId());
					if (uploadedFileResponse.getHasMessages()
							&& uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText("Uploaded Successfully");
					}
				}
			} else {
				isUploaded = false;
			}

		} catch (Exception e) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.addMessages(messages);
		}
		messages.add(message);
		dataResponse.addMessages(messages);
		dataResponse.setObject(isUploaded);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getSourceQueryById", method = RequestMethod.POST)
	public DataResponse getSourceQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getSourceQueryById", map, clientId);
	}
	
	@RequestMapping(value = "/getAIErrorLogInfo", method = RequestMethod.POST)
	public DataResponse getAIErrorLogInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("errorLogName") String errorLogName, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("errorLogName", errorLogName);
		return aiRestUtilities.postRestObject(request, "/getAIErrorLogInfo", map, clientId);
	}
	
	@RequestMapping(value = "/getExecutionCommentsById", method = RequestMethod.POST)
	public DataResponse getExecutionCommentsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return aiRestUtilities.postRestObject(request, "/getExecutionCommentsById", map, clientId);
	}
	@RequestMapping(value = "/deleteCommonJob", method = RequestMethod.POST)
	public DataResponse deleteCommonJob(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("fileName") String fileName, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("fileName", fileName);
		return aiRestUtilities.postRestObject(request, "/deleteCommonJob", map, clientId);
	}

	
	

}
