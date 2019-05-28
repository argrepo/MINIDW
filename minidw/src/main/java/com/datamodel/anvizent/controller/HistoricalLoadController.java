package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.data.controller.RunHistoricalLoad;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/adt/package/historicalLoad")
public class HistoricalLoadController {

	protected static final Log LOGGER = LogFactory.getLog(HistoricalLoadController.class);

	private @Value("${history.load.days.intervals:30,60,90}") String historyLoadTimeInterval;
	private Map<Object, Object> contextParamsMap =  new LinkedHashMap<>();
	
	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageControllerRestUtilities;

	
	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities restScheduleUtilities;
	@Autowired
	private MessageSource messageSource;
	
	@Autowired private ServletContext servletContext;
	
	final String homeRedirectUrl = "redirect:/adt/package/historicalLoad";
	final String homeTilesUrl = "tiles-anvizent-entry:historicalLoad";
	

	
	private Map<Integer, RunHistoricalLoad> historyJobThreadObjects = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView historicalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, Locale locale) {

		CommonUtils.setActiveScreenName("historicalLoad", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			DataResponse dataResponse = packageControllerRestUtilities.getRestObject(request, "/getHistoricalLoad", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					if (dataResponse.getMessages().get(0).getText().equalsIgnoreCase("yes")) {
						List<LinkedHashMap<String, Object>> historicalLoadFormDataResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
						ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						List<HistoricalLoadForm> historicalLoad = null;
						historicalLoad = mapper1.convertValue(historicalLoadFormDataResponse, new TypeReference<List<HistoricalLoadForm>>() {
						});
						int length = historicalLoad.size();
						for (int i = 0; i < length; i++) {
							if (historicalLoad.get(i).isRunning()) {
								RunHistoricalLoad runningJob = historyJobThreadObjects.get(historicalLoad.get(i).getId());
								if (runningJob == null || !runningJob.isRunning()) {
									LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
									form.add("historicalLoadId", historicalLoad.get(i).getId());
									form.add("runningStatus", false);
									restScheduleUtilities.postRestObject(request, "/updateHistoricalLoadRunningStatus", form, user.getUserId());
									System.out.println("manually updated" + historicalLoad.get(i).getId());
									historicalLoad.get(i).setRunning(false);
								}
							}
						}
						mv.addObject("historicalLoadList", historicalLoad);
					} else {
						mv.addObject("historicalLoadList", dataResponse.getObject());
					}

				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}

		historicalLoadForm.setPageMode("list");

		mv.setViewName(homeTilesUrl);
		return mv;

	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView clientTableScriptsAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, RedirectAttributes redirectAttributes, BindingResult result) {
		mv.setViewName(homeTilesUrl);
		boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse userConnectionDataResponse = packageControllerRestUtilities.getRestObject(request, "/getUserILConnections", user.getUserId());
		
		List<ILConnection> existingConnectionsList = new ArrayList<ILConnection>();
		if (userConnectionDataResponse != null && userConnectionDataResponse.getHasMessages()) {
			if (userConnectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				@SuppressWarnings("unchecked")
				List<?> list = (List<Map<String, Object>>) userConnectionDataResponse.getObject();
				List<ILConnection> iLConnectionList = mapper.convertValue(list, new TypeReference<List<ILConnection>>() {});
				if(iLConnectionList != null && !iLConnectionList.isEmpty()){
					for(ILConnection iLConnection:iLConnectionList){
						if(iLConnection != null){
							iLConnection.setWebApp(isWebApp);
						}
						existingConnectionsList.add(iLConnection);
					}
				}
			}  
		}  
		mv.addObject("existingConnections", existingConnectionsList);
		historicalLoadForm.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/saveHistoricalLoad", historicalLoadForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("fetchAllIlInfo")
	public Map<Object, Object> getAllIlInfo(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingILsInfo", user.getUserId());
		List<LinkedHashMap<String, Object>> ils = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ils = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
		}

		if (ils == null || ils.size() == 0) {
			return new HashMap<Object, Object>();
		}
		Map<Object, Object> ilList = new LinkedHashMap<>();
		ils.forEach(map -> {
			ilList.put(map.get("iL_id").toString(), map.get("iL_name").toString());
		});

		return ilList;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("ilConnections")
	public Map<Object, Object> getIlConnection(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
		DataResponse userConnectionDataResponse = packageControllerRestUtilities.getRestObject(request, "/getUserILConnections", user.getUserId());
		Map<Object, Object> iLConnectionListMap = new LinkedHashMap<>();
		if (userConnectionDataResponse != null && userConnectionDataResponse.getHasMessages()) {
			if (userConnectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<?> list = (List<Map<String, Object>>) userConnectionDataResponse.getObject();
				List<ILConnection> iLConnectionList = mapper.convertValue(list, new TypeReference<List<ILConnection>>() {});
				
				if(iLConnectionList != null && !iLConnectionList.isEmpty()){
					for(ILConnection iLConnection:iLConnectionList){
						if ( !isWebApp || iLConnection.isAvailableInCloud()) {
							iLConnectionListMap.put(iLConnection.getConnectionId(), iLConnection.getConnectionName());
						}
					}
				}
				
				
			}  
		}
		return iLConnectionListMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView editHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, Locale locale) {

		if (historicalLoadForm == null || historicalLoadForm.getId() == null) {
			mv.setViewName(homeRedirectUrl);
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			HistoricalLoadForm historicalLoad = null;

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("loadId", historicalLoadForm.getId());

			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/getHistoricalLoadDetailsById", map, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("historicalLoadList", dataResponse.getObject());

					LinkedHashMap<String, Object> historicalLoadFormDataResponse = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					historicalLoad = mapper1.convertValue(historicalLoadFormDataResponse, new TypeReference<HistoricalLoadForm>() {
					});

					if (historicalLoad != null) {

						historicalLoadForm.setClientId(historicalLoad.getClientId());
						historicalLoadForm.setIlId(historicalLoad.getIlId());
						historicalLoadForm.setDataSourceName(historicalLoad.getDataSourceName());
						historicalLoadForm.setHistoricalFromDate(historicalLoad.getHistoricalFromDate());
						historicalLoadForm.setHistoricalToDate(historicalLoad.getHistoricalToDate());
						historicalLoadForm.setHistiricalQueryScript(historicalLoad.getHistiricalQueryScript());
						historicalLoadForm.setLoadInterval(historicalLoad.getLoadInterval());
						historicalLoadForm.setMultipartEnabled(historicalLoad.isMultipartEnabled());
						historicalLoadForm.setNoOfRecordsPerFile(historicalLoad.getNoOfRecordsPerFile());
						historicalLoadForm.setConnectorId(historicalLoad.getConnectorId());
					}

				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			historicalLoadForm.setPageMode("edit");
			mv.setViewName(homeTilesUrl);
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}

		return mv;
	}

	@RequestMapping(value = "/run", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView runHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		if (historicalLoadForm == null || historicalLoadForm.getId() == null) {
			mv.setViewName(homeRedirectUrl);
			return mv;
		}

		User user = CommonUtils.getUserDetails(request, null, null);
		startExecution(historicalLoadForm.getId(), redirectAttributes, user.getUserId(), locale, request,user.getUserName());
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@RequestMapping(value = "/runMultiple", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView runMultipleHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		if (historicalLoadForm == null || historicalLoadForm.getIds() == null) {
			mv.setViewName(homeRedirectUrl);
			return mv;
		}

		User user = CommonUtils.getUserDetails(request, null, null);

		for (Integer hid : historicalLoadForm.getIds()) {
			startExecution(hid, redirectAttributes, user.getUserId(), locale, request,user.getUserName());
		}

		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@SuppressWarnings("unchecked")
	private void startExecution(int hid, RedirectAttributes redirectAttributes, String userId, Locale locale, HttpServletRequest request,String userName) {

		try {

			HistoricalLoadForm historicalLoad = null;
			User user = CommonUtils.getUserDetails(request, null, null);
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("loadId", hid);
			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/getHistoricalLoadDetailsById", map, userId);

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> historicalLoadFormDataResponse = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					historicalLoad = mapper1.convertValue(historicalLoadFormDataResponse, new TypeReference<HistoricalLoadForm>() {
					});
					if (historicalLoad != null) {
						RunHistoricalLoad runningJob = historyJobThreadObjects.get(hid);
						if (!historicalLoad.isExecuted()) {
							if (runningJob == null || !runningJob.isRunning()) {
								boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
								String browserDetails = CommonUtils.getClientBrowserDetails(request);
								String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
								String webServiceContextUrl = (String) SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
								S3BucketInfo s3BucketInfo = getS3BucketInfo( request, userId);//(S3BucketInfo) SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
								FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request,Constants.Config.FILE_SETTINGS_INFO);
								
								RunHistoricalLoad worker = new RunHistoricalLoad(hid, packageControllerRestUtilities, restScheduleUtilities, userId, LOGGER,
										messageSource, locale, browserDetails, deploymentType , isWebApp,s3BucketInfo,userName,user.getClientId(),webServiceContextUrl,fileSettings);
								worker.start();
								historyJobThreadObjects.remove(hid);
								historyJobThreadObjects.put(hid, worker);
								redirectAttributes.addFlashAttribute("messagecode", "SUCCESS");
								redirectAttributes.addFlashAttribute("errors",
										messageSource.getMessage("anvizent.package.label.initiattedSuccessfully", null, locale));
							} else {
								redirectAttributes.addFlashAttribute("messagecode", "FAILED");
								redirectAttributes.addFlashAttribute("errors", "Job already initiated");
							}
						} else {
							redirectAttributes.addFlashAttribute("messagecode", "FAILED");
							redirectAttributes.addFlashAttribute("errors",
									messageSource.getMessage("anvizent.package.label.historyLoadAlreadyCompleted", null, locale));
						}
					} else {
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors",
								messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}

				} else {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
	}
	@SuppressWarnings({ "unchecked" })
	private S3BucketInfo getS3BucketInfo(HttpServletRequest request, String userId) throws Exception
	{
		S3BucketInfo s3BucketInfo = null;
		DataResponse s3BucketInfoDataResponse = packageControllerRestUtilities.getRestObject(request, "/getS3BucketInfoFromClientId", userId);
		if( s3BucketInfoDataResponse != null && s3BucketInfoDataResponse.getHasMessages() )
		{
			if( s3BucketInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
			{
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) s3BucketInfoDataResponse.getObject();
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				s3BucketInfo = mapper.convertValue(map, new TypeReference<S3BucketInfo>()
				{
				});
			}
			else
			{
				throw new Exception("s3 bucket info not found for client id:" + userId);
			}
		}
		return s3BucketInfo;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/stop", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView stopHistoricalLoadJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		if (historicalLoadForm == null || historicalLoadForm.getId() == null) {
			mv.setViewName(homeRedirectUrl);
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			HistoricalLoadForm historicalLoad = null;

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("loadId", historicalLoadForm.getId());
			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/getHistoricalLoadDetailsById", map, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

					LinkedHashMap<String, Object> historicalLoadFormDataResponse = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					historicalLoad = mapper1.convertValue(historicalLoadFormDataResponse, new TypeReference<HistoricalLoadForm>() {
					});

					if (historicalLoad != null && historicalLoad.isRunning()) {
						RunHistoricalLoad runningJob = historyJobThreadObjects.get(historicalLoadForm.getId());

						if (runningJob != null && runningJob.isRunning()) {
							runningJob.terminate();
							historyJobThreadObjects.remove(historicalLoadForm.getId());
							redirectAttributes.addFlashAttribute("messagecode", "SUCCESS");
							redirectAttributes.addFlashAttribute("errors",
									messageSource.getMessage("anvizent.package.label.historyLoadSuccessfullyTerminated", null, locale));
						} else {
							LinkedMultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
							form.add("historicalLoadId", historicalLoadForm.getId());
							form.add("runningStatus", false);
							restScheduleUtilities.postRestObject(request, "/updateHistoricalLoadRunningStatus", form, user.getClientId());
							redirectAttributes.addFlashAttribute("messagecode", "FAILED");
							redirectAttributes.addFlashAttribute("errors",
									messageSource.getMessage("anvizent.package.label.historyLoadAlreadyStopped", null, locale));
						}
					} else {
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors",
								messageSource.getMessage("anvizent.package.label.historyLoadIsNotRunning", null, locale));
					}

				} else {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/updateHistoricalLoad", historicalLoadForm, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}

		mv.setViewName(homeRedirectUrl);
		return mv;
	}


	@RequestMapping(value = "/clone", method = RequestMethod.POST)
	public ModelAndView cloneHistoricalLoad(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("historicalLoadForm") HistoricalLoadForm historicalLoadForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("loadId", historicalLoadForm.getId());

			DataResponse dataResponse = packageControllerRestUtilities.postRestObject(request, "/cloneHistoricalLoadDetailsById", map, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}

		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@RequestMapping(value = "/jobResultsForHistoricalLoad/{ilId}/{hId}/{ilName}", method = RequestMethod.GET)
	public ModelAndView viewResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("ilId") Integer ilId,
			@PathVariable("hId") Long hId, @PathVariable("ilName") String ilName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		if (ilId != null && ilName != null) {

			jobResultForm.setIlId(ilId);
			jobResultForm.setPackageId(Long.toString(hId));
			jobResultForm.setIlName(ilName);

			DataResponse dataResponse = packageControllerRestUtilities.getRestObject(request, "/getJobResultsForHistoricalLoad/{ilId}/{hId}", user.getUserId(),
					ilId, hId);
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {
				mv.addObject("viewResultList", dataResponse.getObject());
			}

			mv.setViewName("tiles-anvizent-entry:viewResults");
			jobResultForm.setPageMode("historical");
		} else {
			mv.setViewName(homeRedirectUrl);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jobResultsForHistoricalLoad/{ilId}/{hId}/{ilName}", method = RequestMethod.POST)
	public ModelAndView searchJobResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("ilId") Integer ilId,
			@PathVariable("hId") Long hId, @PathVariable("ilName") String ilName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = packageControllerRestUtilities.getRestObject(request,
					"/getJobResultsForHistoricalLoadByDate/{ilId}/{hId}/{fromDate}/{toDate}", user.getUserId(), ilId, hId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

			jobResultForm.setPageMode("historical");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}
	@SuppressWarnings("unchecked")
	@ModelAttribute("allDataSourceList")
	public Map<Object, Object> getDataSourceList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<ClientDataSources> dataSourceList = null;

		dataSourceList = new ArrayList<>();
		DataResponse databseSourceDataResponse = packageControllerRestUtilities.getRestObject(request, "/getDataSourceList", user.getUserId());
		if (databseSourceDataResponse != null && databseSourceDataResponse.getHasMessages()
				&& databseSourceDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			
			List<LinkedHashMap<String, Object>> dataSourceResponse = (List<LinkedHashMap<String, Object>>) databseSourceDataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			dataSourceList = mapper.convertValue(dataSourceResponse, new TypeReference<List<ClientDataSources>>() {
			});
			
		} else {
			dataSourceList = new ArrayList<>();
		}

		Map<Object, Object> dataSourcesMap = new LinkedHashMap<>();
		for (ClientDataSources dataSource : dataSourceList) {
			dataSourcesMap.put(dataSource.getDataSourceName(),dataSource.getDataSourceName());
		}
		return dataSourcesMap;
	}
	
	@ModelAttribute("loadInterVal")
	public Map<Object, Object> loadInterVal(HttpServletRequest request) {
		if (contextParamsMap.isEmpty()) {
			synchronized(contextParamsMap) {
				if (contextParamsMap.isEmpty()) {
					contextParamsMap.clear();
					String[] intervalDats = StringUtils.split(historyLoadTimeInterval, ",");
					for (String day:intervalDats) {
						if (StringUtils.isNotBlank(day)) {
							contextParamsMap.put(day, day);
						}
					}
				}
			}
		}
		return contextParamsMap;
	}

}
