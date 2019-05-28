package com.datamodel.anvizent.controller;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.StandardPackageForm;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.datamodel.anvizent.validator.StandardPackageFormValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Import(AppProperties.class)
@RequestMapping(value="/adt/standardpackage")
public class StandardPackageController {
	
	protected static final Log LOGGER = LogFactory.getLog(StandardPackageController.class);
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	private @Value("${run.with.scheduler.or.runnow:runwithscheduler}") String runWithSchedulerOrRunnow;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@Autowired
	StandardPackageFormValidator spValidator;
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView standardPackage(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result,
			RedirectAttributes redirectAttributes, ModelAndView mv, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Package userPackage = null;
		try{ 
			CommonUtils.setActiveScreenName("standardpackage", session);
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getStandardPackage", user.getUserId());
			if (userDataResponse != null && userDataResponse.getHasMessages() ) {
				if ( userDataResponse.getMessages().get(0).getCode().equals("SUCCESS") ) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {});
					mv.addObject("userPackage", userPackage);
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		if (userPackage != null) {
			if (userPackage.getPackageId() >= 0)
				standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				standardPackageForm.setPackageName(userPackage.getPackageName());
			if (userPackage.getIsStandard() != null)
				standardPackageForm.setIsStandard(userPackage.getIsStandard());
			if (userPackage.getIndustry() != null)
				standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));
			if (userPackage.getTrailingMonths() != 0)
				standardPackageForm.setTrailingMonths(Integer.valueOf(userPackage.getTrailingMonths()));

			// get AllDls for the industry
			DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getClientStandardPackageDLs", user.getUserId());
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if (dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("userDlList", dlDataResponse.getObject());
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dlDataResponse.getMessages().get(0).getText());
				}
			}else{
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getMappedModulesForStandardPackage/{" + userPackage.getPackageId() + "}", user.getUserId(),
					userPackage.getPackageId());

			 mv.addObject("mappedModuleSatandardPackage", dataResponse.getObject());
			 mv.addObject("packageCreation", false);
			 //mv.addObject("timeZonesList", getTimesZoneList(request));
			 mv.addObject("runWithSchedulerOrRunnow", runWithSchedulerOrRunnow);
			 mv.setViewName("tiles-anvizent-entry:standardPackage");
			} else {
				mv.setViewName("redirect:/adt/standardpackage");
			}
		}catch(Throwable t){
			
		}
		return mv;
	}
	
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/addILSource/{packageId}/{dLId}/{iLId}", method = RequestMethod.GET)
	public ModelAndView addILSource(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("packageId") Integer packageId,
			@PathVariable("dLId") Integer dLId, @PathVariable("iLId") Integer iLId,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, RedirectAttributes redirectAttributes,Locale locale) {

		ClientData clientData = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		List<ILInfo> iLList = null;
		DLInfo dLInfo = null;
		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getStandardPackage", user.getUserId());
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper.convertValue(map, new TypeReference<Package>() {});
				mv.addObject("userPackage", userPackage);
			}
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				standardPackageForm.setPackageName(userPackage.getPackageName());
			if (userPackage.getIsStandard() != null)
				standardPackageForm.setIsStandard(userPackage.getIsStandard());
			if (userPackage.getIndustry() != null)
				standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

			DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getDLsById/{" + dLId + "}", user.getUserId(), dLId);
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dlDataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					dLInfo = mapper1.convertValue(dls, new TypeReference<DLInfo>() {});
					mv.addObject("dLInfo", dLInfo);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dlDataResponse.getMessages().get(0).getText());
				}
			}else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllILS/{" + dLId + "}", user.getUserId(), dLId);
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					List<LinkedHashMap<String, Object>> iLs = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					iLList = mapper.convertValue(iLs, new TypeReference<List<ILInfo>>() {});
					mv.addObject("iLList", iLList);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			}else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			String clientId = user.getClientId();

			if (dLInfo != null) {
				if (dLInfo.getdL_name() != null) {
					standardPackageForm.setdLName(dLInfo.getdL_name());
				}
				standardPackageForm.setdLId(String.valueOf(dLInfo.getdL_id()));
			}
			if(iLList != null){
				for (ILInfo iLInfo : iLList) {
					if (iLInfo != null) {
						if (iLInfo.getiL_id() == iLId) {
							if (iLInfo.getiL_name() != null) {
								standardPackageForm.setiLName(String.valueOf(iLInfo.getiL_id()));
							}
						}
					}
				}
			}
			mv.addObject("defaultILId", iLId);

			DataResponse clientDataResponse = restUtilities.getRestObject(request, "/getSourcesDetails/{client_Id}", user.getUserId(), clientId);
			if (clientDataResponse != null && clientDataResponse.getHasMessages()) {
				if(clientDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) clientDataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					clientData = mapper1.convertValue(map, new TypeReference<ClientData>() {});
					mv.addObject("clientData", clientData);
				}
			}else{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			boolean isFlatFileOptionLocked = false;
			boolean isDataBaseOptionLocked = false;
			if (clientData != null) {
				isFlatFileOptionLocked = clientData.getIs_flat_file_locked();
				isDataBaseOptionLocked = clientData.getIs_database_locked();
			}
			mv.addObject("isFlatFileOptionLocked", isFlatFileOptionLocked);
			mv.addObject("isDataBaseOptionLocked", isDataBaseOptionLocked);
			mv.addObject("timesZoneList", getTimesZoneList(request));
			//mv.addObject("webServiceList", getAllWebServices(request, response, mv));
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
			mv.setViewName("tiles-anvizent-entry:addILSource");
		} else {
			mv.setViewName("redirect:/adt/standardpackage");
		}
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getTimesZoneList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getTimeZones", user.getUserId());
		List<TimeZones> zoneNames = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> timeZoneResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			zoneNames = mapper.convertValue(timeZoneResponse, new TypeReference<List<TimeZones>>() {
			});

		} else {
			zoneNames = new ArrayList<>();
		}
		Map<Object, Object> zonesMap = new LinkedHashMap<>();
		for (TimeZones timeZone : zoneNames) {
			zonesMap.put(timeZone.getZoneName(), timeZone.getZoneNameDisplay());
		}
		return zonesMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getAllWebServices(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllWebServices", user.getUserId());

		Map<Object, Object> webServiceMap = new LinkedHashMap<>();
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			Map<Integer, String> map = (Map<Integer, String>) dataResponse.getObject();
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				webServiceMap.put(entry.getKey(), entry.getValue());
			}
		}
		return webServiceMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Database> getDatabaseTypes(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Database> databseList = null;

		databseList = new ArrayList<>();
		DataResponse databseTypesDataResponse = restUtilities.getRestObject(request, "/getDatabasesTypes", user.getUserId());
		List<LinkedHashMap<String, Object>> databseTypes = null;
		if (databseTypesDataResponse != null && databseTypesDataResponse.getHasMessages()
				&& databseTypesDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			databseTypes = (List<LinkedHashMap<String, Object>>) databseTypesDataResponse.getObject();
		} else {
			databseTypes = new ArrayList<>();
		}
		for (LinkedHashMap<String, Object> map : databseTypes) {
			Database databse = new Database();
			int id = Integer.parseInt(map.get("id").toString());
			int connector_id = Integer.parseInt(map.get("connector_id").toString());
			databse.setId(id);
			databse.setName(map.get("name").toString());
			databse.setConnector_id(connector_id);
			databse.setProtocal((String)map.get("protocal"));  
			databse.setUrlFormat((String)map.get("urlFormat"));
			databse.setConnectionStringParams((String)map.get("connectionStringParams"));
			databseList.add(databse);
		}
		return databseList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientDataSources> getDataSourceList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<ClientDataSources> dataSourceList = null;

		dataSourceList = new ArrayList<>();
		DataResponse databseSourceDataResponse = restUtilities.getRestObject(request, "/getDataSourceList", user.getUserId());
		List<LinkedHashMap<String, Object>> dataSources = null;
		if (databseSourceDataResponse != null && databseSourceDataResponse.getHasMessages()
				&& databseSourceDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			dataSources = (List<LinkedHashMap<String, Object>>) databseSourceDataResponse.getObject();
		} else {
			dataSources = new ArrayList<>();
		}
		for (LinkedHashMap<String, Object> map : dataSources) {
			ClientDataSources dataSource = new ClientDataSources();
			dataSource.setDataSourceName(map.get("dataSourceName").toString());
			dataSourceList.add(dataSource);
		}
		return dataSourceList;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getSourceInfo/{dlId}", method=RequestMethod.GET) 
	public ModelAndView getDlInfoForStandardPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("dlId") Integer dLId, @ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, RedirectAttributes redirectAttributes,Locale locale){
		User user = CommonUtils.getUserDetails(request, null, null);
		List<ClientData> clientDataInfo = null;
		try{
			DataResponse  dataResponse = restUtilities.getRestObject(request, "/getSourceInfo/{dlId}", user.getUserId(), dLId);
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				List<LinkedHashMap<String, Object>> timeZoneResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				 clientDataInfo = mapper.convertValue(timeZoneResponse, new TypeReference<List<ClientData>>() {
				});
				 mv.addObject("clientData", clientDataInfo);
				 mv.addObject("runSp", true);
				 mv.addObject("packageCreation", false);
				 mv.setViewName("tiles-anvizent-entry:standardPackage");
				} else {
					mv.setViewName("redirect:/adt/standardpackage");
				}
		}catch(Throwable t){
			
		}
		return mv;
	}
	
	
	@ModelAttribute("hours")
	public List<String> hours() {
		List<String> hours = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			StringBuilder hour = new StringBuilder();
			if (i <= 9) {
				hour.append("0");
			}
			hour.append(i);
			String hh = hour.toString();
			hours.add(hh);
		}
		return hours;
	}

	@ModelAttribute("minutes")
	public List<String> minutes() {
		List<String> minutes = new ArrayList<>();
		for (int i = 0; i < 60; i++) {
			StringBuilder minute = new StringBuilder();
			if (i <= 9) {
				minute.append("0");
			}
			minute.append(i);
			String mm = minute.toString();
			minutes.add(mm);
		}
		return minutes;
	}

	@ModelAttribute("weekdays")
	public List<String> weekdays() {
		List<String> weekdays = new ArrayList<>();
		String[] days = new DateFormatSymbols().getWeekdays();
		for (int i = 1; i < days.length; i++) {
			String day = days[i];
			weekdays.add(day);
		}
		if (weekdays.contains("Sunday")) {
			weekdays.remove("Sunday");
			weekdays.add("Sunday");
		}

		return weekdays;
	}

	@ModelAttribute("daysOfMonth")
	public Map<Integer, String> daysOfMonth() {
		Map<Integer, String> daysOfMonth = new HashMap<>();

		for (int i = 1; i <= 31; i++) {

			daysOfMonth.put(i, i + getDayNumberSuffix(i));
		}
		return daysOfMonth;
	}

	private String getDayNumberSuffix(int day) {
		if (day >= 11 && day <= 13) {
			return "th";
		}
		switch (day % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
		}
	}

	@ModelAttribute("monthsOfYear")
	public Map<Integer, String> monthsOfYear() {
		Map<Integer, String> monthsOfYear = new HashMap<>();

		for (int i = 1; i <= Month.values().length; i++) {
			monthsOfYear.put(i, StringUtils.capitalize(Month.of(i).toString().toLowerCase()));
		}
		return monthsOfYear;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/executionResults/{dlId}", method = RequestMethod.GET)
	public ModelAndView standardPackage(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv,
			@PathVariable("dlId") int dlId, @RequestParam(value="dlName",required=false) String dlName,Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<PackageExecution> packageExecutionList = null;
		try {
			if (dlId != 0) {
				MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
				maps.add("dlId", dlId);
			
			DataResponse packageExecutionDataResponse = null;
			packageExecutionDataResponse = restUtilities.postRestObject(request, "/getStandardPackageExecution",maps, user.getUserId());
			if (packageExecutionDataResponse != null && packageExecutionDataResponse.getHasMessages()) {
				if (packageExecutionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					packageExecutionList = (List<PackageExecution>) packageExecutionDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<?> l = (List<Map<String, Object>>) packageExecutionDataResponse.getObject();
					packageExecutionList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					String timeZone = (String)session.getAttribute(Constants.Config.TIME_ZONE);
					for ( PackageExecution packageExecution: packageExecutionList ) {
						packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
						packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));
						
						packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
						packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
						packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
						packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
					}
					
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("packageId", 0);
			mv.addObject("packageName", "Standard Package");
			mv.addObject("dlId", dlId);
			mv.addObject("dlName", dlName);
			mv.addObject("packageExecutionList", packageExecutionList);
			mv.setViewName("tiles-anvizent-entry:packageExecution");
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/executionResultsByPagination/{dlId}", method = RequestMethod.GET)
	public ModelAndView executionResultsByPagination(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv,
			@PathVariable("dlId") int dlId, @RequestParam(value="dlName",required=false) String dlName,
			@RequestParam(value="offset") int offset,
			@RequestParam(value="limit") int limit,
			Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<PackageExecution> packageExecutionList = null;
		int count=0;
		try {
			if (dlId != 0) {
				MultiValueMap<Object, Object> countmap = new LinkedMultiValueMap<>();
				countmap.add("dlId", dlId);
				
				DataResponse packageExecutionCountDataResponse = null;
				packageExecutionCountDataResponse = restUtilities.postRestObject(request, "/getStandardPackageExecutionCount",countmap, user.getUserId());
				if (packageExecutionCountDataResponse != null && packageExecutionCountDataResponse.getHasMessages()) {
					if (packageExecutionCountDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						count =  (int) packageExecutionCountDataResponse.getObject();
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", packageExecutionCountDataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
				int totalCount = 0;
				int paginationCount = count/10;
				int remainingRecords = count - paginationCount*10;
				
				if(remainingRecords == 0){
					totalCount = paginationCount;
				}else{
					totalCount = paginationCount+1;
				}
				
				MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
				maps.add("dlId", dlId);
				maps.add("offset", offset);
				maps.add("limit", limit);
			
			DataResponse packageExecutionDataResponse = null;
			packageExecutionDataResponse = restUtilities.postRestObject(request, "/getStandardPackageExecutionByPagination",maps, user.getUserId());
			if (packageExecutionDataResponse != null && packageExecutionDataResponse.getHasMessages()) {
				if (packageExecutionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					packageExecutionList = (List<PackageExecution>) packageExecutionDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<?> l = (List<Map<String, Object>>) packageExecutionDataResponse.getObject();
					packageExecutionList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					String timeZone = (String)session.getAttribute(Constants.Config.TIME_ZONE);
					for ( PackageExecution packageExecution: packageExecutionList ) {
						packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
						packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));
						
						packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
						packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
						packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
						packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
					}
					
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("packageId", 0);
			mv.addObject("packageName", "Standard Package");
			mv.addObject("dlId", dlId);
			mv.addObject("dlName", dlName);
			mv.addObject("packageExecutionList", packageExecutionList);
			mv.addObject("totalCount", totalCount == 0 ? 1 : totalCount);
			
			mv.setViewName("tiles-anvizent-entry:packageExecution");
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewjobResults/{dlId}", method = RequestMethod.GET)
	public ModelAndView viewResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,  @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			@PathVariable("dlId") String dlId,
			@RequestParam(value = "source", required = false) String fromSource,
			@RequestParam(value = "dl_Name", required = false) String dl_Name,
			BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {

		User user = CommonUtils.getUserDetails(request, null, null);
         
		Package userPackage = null;
		int packageId = 0;
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages()) {
				if (userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {});
					mv.addObject("userPackage", userPackage);
				} else {
					redirectAttributes.addFlashAttribute("messagecode", userDataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", "Package details not found");
			}
		if (userPackage != null) {
				jobResultForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				jobResultForm.setPackageName(userPackage.getPackageName());
			
			jobResultForm.setDlId(dlId);
			jobResultForm.setDlName(dl_Name);

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getStandardPackageJobResults/{" + packageId + "}?dlId={"+ dlId +"}", user.getUserId(), packageId, dlId);
			mv.addObject("viewResultList", dataResponse.getObject());
			if (StringUtils.isNotBlank(fromSource)) {
				jobResultForm.setPageMode(fromSource);
			}
			mv.setViewName("tiles-anvizent-entry:viewResults");

		} else {
			mv.setViewName("redirect:/adt/standardpackage");
		}

		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewjobResults/{dlId}", method = RequestMethod.POST)
	public ModelAndView searchJobResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("dlId") String dlId,
			@RequestParam(value = "source", required = false) String fromSource,
			@RequestParam(value = "dl_Name", required = false) String dl_Name,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {


		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String packageId = jobResultForm.getPackageId() == null ? "0" : jobResultForm.getPackageId();
			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getStandardPackageJobResultsByDate/{packageId}/{dlId}/{fromDate}/{toDate}",
					user.getUserId(), packageId, dlId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}
	

}
