package com.datamodel.anvizent.data.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.processor.RBModelProcessor;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.User;

@RestController
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL)
@CrossOrigin
public class StandardPackageDataController {

	protected static final Log LOGGER = LogFactory.getLog(StandardPackageDataController.class);

	private @Value("${webservice.connection.request.timeout:0}") int wsConnectionRequestTimeout;
	private @Value("${webservice.connect.timeout:0}") int wsConnectTimeout;
	private @Value("${webservice.read.timeout:0}") int wsReadTimeout;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("apisDataRestTemplateUtilities")
	private RestTemplateUtilities apisRestUtilities;

	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities scheduleRestUtilities;

	@Autowired
	StandardPackageProcessor standardPackageProcessor;

	@Autowired
	RBModelProcessor rBModelProcessor;
	
	@RequestMapping(value = "/getSourceInfo/{dlId}", method = RequestMethod.GET)
	public DataResponse getDlInfoForStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dlId") Integer dlId, HttpServletRequest request, Locale locale) {
		return restUtilities.getRestObject(request, "/getSourceInfo/{dlId}", clientId, dlId);
	}

	@RequestMapping(value = "/getSourceInfo/{dlId}/{iLId}", method = RequestMethod.GET)
	public DataResponse getILsInfoForStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dlId") Integer dlId, @PathVariable("iLId") Integer iLId, HttpServletRequest request,
			Locale locale) {
		return restUtilities.getRestObject(request, "/getSourceInfo/{dlId}/{iLId}", clientId, dlId, iLId);
	}

	@RequestMapping(value = "/runSources", method = RequestMethod.POST)
	public DataResponse runDLSources(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam(value = "schedulerType", required = false) String schedulerType, @RequestParam(value = "schedulerId", required = false) Integer schedulerId,
			@RequestParam(value = "timeZone", required = false) String timeZone, HttpServletRequest request, Locale locale, @RequestBody DLInfo dlInfo)
	{

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		User user = CommonUtils.getUserDetails(request, null, null);
		boolean isExecutionRequired = true;
		try
		{
			if( !dlInfo.isrJob() )
			{
				if( (dlInfo.getIlList() != null && dlInfo.getIlList().length > 0) || dlInfo.isDlExecutionRequired() )
				{
					/* retry pagination to null update */
					restUtilities.postRestObject(request, "/schedule/updateRetryPaginationToNull", dlInfo, clientId);
					
					if( schedulerType.equals(Constants.ScheduleType.RUN_WITH_SCHEDULER) )
					{
						isExecutionRequired = false;
						if( schedulerId == null || schedulerId == 0 )
						{
							ClientData clientData = new ClientData();

							Package userPackage = new Package();
							userPackage.setPackageId(0);
							clientData.setUserPackage(userPackage);

							Schedule schedule = new Schedule();
							schedule.setTimeZone(timeZone);
							clientData.setSchedule(schedule);
							clientData.setDlInfo(dlInfo);

							DataResponse saveSchedulerDataResponse = restUtilities.postRestObject(request, "/schedule/saveRunWithSchedulerInfo", clientData, clientId);
							if( saveSchedulerDataResponse != null && saveSchedulerDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
							{

							}
						}
					}

					dlInfo.setWsConnectionRequestTimeout(wsConnectionRequestTimeout);
					dlInfo.setWsReadTimeout(wsReadTimeout);
					dlInfo.setWsConnectTimeout(wsConnectTimeout);

					standardPackageProcessor.processStandardPackageExecutor(request, clientId, user, dlInfo, isExecutionRequired);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.runNowInitiated", null, locale));
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.text.unabletorunsources", null, locale));
				}
			}
			else
			{
				if( schedulerType.equals(Constants.ScheduleType.RUN_WITH_SCHEDULER) )
				{
					isExecutionRequired = false;
					if( schedulerId == null || schedulerId == 0 )
					{
						ClientData clientData = new ClientData();
						Schedule schedule = new Schedule();
						schedule.setTimeZone(timeZone);
						clientData.setSchedule(schedule);
						clientData.setDlInfo(dlInfo);

						restUtilities.postRestObject(request, "/schedule/saveRunWithSchedulerInfoForR", clientData, clientId);

					}
				}
				rBModelProcessor.processRBModelExecution(request, clientId, user, dlInfo, isExecutionRequired);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.runNowInitiated", null, locale));
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.text.unabletorunsources", null, locale) + t.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveDLSchedule", method = RequestMethod.POST)
	public DataResponse saveStandardPackageSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ClientData clientData, Locale locale, HttpServletRequest request, String jobType) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String timeZone = clientData.getSchedule().getTimeZone();

		if (StringUtils.isBlank(timeZone)) {
			clientData.getSchedule().setTimeZone(TimeZone.getDefault().getID());
		}
		ResponseEntity<DataResponse> responseEntity = restUtilities.postRestEntity(request, "/saveDLSchedule",
				clientData, DataResponse.class, clientId);

		if (responseEntity == null || responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
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

	@RequestMapping(value = "/unScheduleDL", method = RequestMethod.POST)
	public ResponseEntity<Object> unSchedule(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Schedule schedule, Locale locale, HttpServletRequest request) throws ParseException {

		return restUtilities.postRestEntity(request, "/unScheduleDL", schedule, Object.class, clientId);
	}
	
	@RequestMapping(value = "/saveDLTrailing", method = RequestMethod.POST)
	public DataResponse saveDLTrailing(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("dL_id") Integer dL_id, @RequestParam("trailingMonths") Integer trailingMonths, HttpServletRequest request,
			Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("dL_id", dL_id);
		map.add("trailingMonths", trailingMonths);
		return restUtilities.postRestObject(request, "/saveDLTrailingMapping",  map,clientId);
	}
	
	@RequestMapping(value = "/updateDLTrailing", method = RequestMethod.POST)
	public DataResponse updateDLTrailing(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("dL_id") Integer dL_id, @RequestParam("trailingMonths") Integer trailingMonths, HttpServletRequest request,
			Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("dL_id", dL_id);
		map.add("trailingMonths", trailingMonths);
		return restUtilities.postRestObject(request, "/updateDLTrailing",  map,clientId);
	}
	
	@RequestMapping(value = "/getTimeZones", method = RequestMethod.GET)
	public DataResponse getTimeZones(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			 HttpServletRequest request,Locale locale) {
		return restUtilities.getRestObject(request, "/getTimeZones", clientId);
	}
	
	@RequestMapping(value = "/getAllWebServices", method = RequestMethod.GET)
	public DataResponse getAllWebServices(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			 HttpServletRequest request,Locale locale) {
		return restUtilities.getRestObject(request, "/getAllWebServices", clientId);
	}
	
	@RequestMapping(value = "/getDataTypesList", method = RequestMethod.GET)
	public DataResponse getDataTypesList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			 HttpServletRequest request,Locale locale) {
		return restUtilities.getRestObject(request, "/getDataTypesList", clientId);
	}
	@RequestMapping(value = "/getPackageExecutionByPaginationId", method = RequestMethod.GET)
	public DataResponse getPackageExecutionByPaginationId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("dlId") int dlId,
			@RequestParam(value="offset") int offset,
			@RequestParam(value="limit") int limit, HttpServletRequest request,Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("dlId", dlId);
		map.add("offset", offset);
		map.add("limit", limit);
		return restUtilities.postRestObject(request, "/getPackageExecutionByPaginationId",map, clientId);
	}

}
