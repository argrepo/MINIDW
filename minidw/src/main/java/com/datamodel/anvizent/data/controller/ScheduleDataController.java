
/**
 * 
 */
package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.anvizent.client.scheduler.listner.ClientScheduler;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.Schedule;

/**
 * @author rajesh.anthari
 *
 */

@RestController("user_scheduleServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/package/schedule")
@CrossOrigin
public class ScheduleDataController {
	protected static final Log LOGGER = LogFactory.getLog(ScheduleDataController.class);
	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities; 
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;
	
	@Autowired
	@Qualifier("scheduleServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	/*@Autowired 
	private ServletContext servletContext;*/
	
	@Autowired
	private MessageSource messageSource;
	
	PropertiesPlaceHolder propertiesPlaceHolder = null;
	
	@Autowired
	ClientScheduler clientScheduler;

	/**
	 * get the packages of the client which have all the ILs are mapped for
	 * atleast one DL
	 * 
	 * @param clientId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getPackagesForScheduling", method = RequestMethod.GET)
	public ResponseEntity<List<Package>> getPackagesForScheduling(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {

		return restUtilities.getRestEntity(request, "/getPackagesForScheduling", (Class<List<Package>>) (Object) List.class, clientId);
	}

	/**
	 * @throws ParseException
	 * 
	 * 
	 */

	@RequestMapping(value = "/savePackageSchedule", method = RequestMethod.POST)
	public DataResponse savePackageScheduleOld(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData,
			Locale locale, @RequestParam(value = "reloadUrl", required = false) String reloadUrl,
			@RequestParam(value = "jobType", required = false) String jobType, HttpServletRequest request) throws ParseException {

		String jobTypeParam = "";
		if (jobType != null) {
			jobTypeParam = "&jobType=" + jobType;
		}
		String timeZone = clientData.getSchedule().getTimeZone();

		if (StringUtils.isBlank(timeZone)) {
			clientData.getSchedule().setTimeZone(TimeZone.getDefault().getID());
		}
		ResponseEntity<DataResponse> responseEntity = null;
		if ( clientData.getSchedule().getRecurrencePattern().equalsIgnoreCase("runnow") ) {
			responseEntity = restUtilitieslb.postRestEntity(request, "/savePackageSchedule?reloadUrl=" + reloadUrl + jobTypeParam, clientData, DataResponse.class, clientId);
		} else {
			responseEntity = restUtilities.postRestEntity(request, "/savePackageSchedule?reloadUrl=" + reloadUrl + jobTypeParam, clientData, DataResponse.class, clientId);
		}
		
		DataResponse dataResponse = null;
		if ( responseEntity == null || responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)  ) {
			Message message = new Message();
			List<Message> messages = new ArrayList<>();
			messages.add(message);
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.serverNotReachable", null, locale));
			dataResponse = new DataResponse();
			dataResponse.setMessages(messages);
		} else {
			dataResponse = responseEntity.getBody();
		}
		
		return dataResponse;
		
	}
	
	@RequestMapping(value = "/savePackagesSchedule", method = RequestMethod.POST)
	public ResponseEntity<Object> savePackagesSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<ClientData> clientDataList, Locale locale, @RequestParam(value = "reloadUrl", required = false) String reloadUrl,
			HttpServletRequest request) throws ParseException {


		return restUtilities.postRestEntity(request, "/savePackagesSchedule?reloadUrl=" + reloadUrl, clientDataList, Object.class, clientId);
	}

	@RequestMapping(value = "/unSchedule", method = RequestMethod.POST)
	public ResponseEntity<Object> unSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Schedule schedule, Locale locale,
			HttpServletRequest request) throws ParseException {

		schedule.setUserId(clientId);
		return restUtilities.postRestEntity(request, "/unSchedule", schedule, Object.class, clientId);
	}
	
	
	/**
	 * scheduled pending packages to client scheduler
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getScheduleCurrent", method = RequestMethod.GET)
	public DataResponse getScheduleCurrent(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getScheduleCurrent", clientId);

	}

	
	/**
	 * to server scheduler
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUploadedFileInfo", method = RequestMethod.GET)
	public ResponseEntity<List<ClientData>> getUploadedFileInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		return restUtilities.getRestEntity(request, "/getUploadedFileInfo", (Class<List<ClientData>>) (Object) List.class, clientId);
	}


	@RequestMapping(value = "/getILConnectionMappingS3DetailsInfoByPackage/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getILConnectionMappingS3DetailsInfoByPackage/{Package_id}", clientId, Package_id);
	}
	
	@RequestMapping(value="/uploadFileIntoCurrencyLoad", method= RequestMethod.POST)
	public DataResponse uploadFileIntoCurrencyLoad(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file,
			@RequestParam("flatFileType") String flatFileType,
			@RequestParam("delimeter") String delimeter)  {
		
	MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();

	String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
	String tempFileName = null;
	String tempFolderName = null;

	DataResponse dataResponse = new DataResponse();
	List<Message> messages = new ArrayList<Message>();
	Message message = new Message();
	try {
		tempFolderName = Constants.TempUpload.getTempFileDir(clientId);
		tempFileName = tempFolderName + file.getOriginalFilename();
		CommonUtils.createFile(tempFolderName, tempFileName, file);
		
		if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			map.add("file", new FileSystemResource(tempFileName));
		} 
		else {
			map.add("file", new File(""));
			map.add("localFilePath", tempFileName);
		}
		
		map.add("flatFileType", flatFileType);
		map.add("delimeter", delimeter);

		dataResponse = restUtilities.postRestObject(request, "/uploadFileIntoCurrencyLoad", map, clientId);
		if ( dataResponse == null || !dataResponse.getHasMessages() ) {
			dataResponse = new DataResponse(); 
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.fileUploadingFailed", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
	} catch (Exception e) {
		e.printStackTrace();
		message.setCode("ERROR");
		message.setText(messageSource.getMessage("anvizent.message.text.fileUploadingFailed", null, locale));
		messages.add(message);
		dataResponse.setMessages(messages);

		return dataResponse;
	} finally {
		if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			CommonUtils.removeFile(tempFolderName, tempFileName);
		}
	}

	return dataResponse;
	
}
	
}
