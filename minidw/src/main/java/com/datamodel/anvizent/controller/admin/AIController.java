package com.datamodel.anvizent.controller.admin;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;

@Controller
@RequestMapping(value = "/adt/package/ai")
public class AIController {
	protected static final Log LOGGER = LogFactory.getLog(AIController.class);

	@Autowired
	@Qualifier("aiRestTemplateUtilities")
	private RestTemplateUtilities aiRestUtilities;
	
	
	
	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping(value = "/businessModels", method = RequestMethod.GET)
	public ModelAndView businessModels(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) throws IOException {
		LOGGER.debug("in businessModels()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getBusinessModelInfo",
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("businessModel", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		CommonUtils.setActiveScreenName("businessModel", session);
		mv.setViewName("tiles-anvizent-entry:aiBusinessProblem");
		return mv;
	}
	

	@RequestMapping(value = "/aiSourceDefinitionList",method = RequestMethod.GET)
	public ModelAndView eltConfigTags(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,Locale locale) {
		CommonUtils.setActiveScreenName("aiSourceDefinition", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/aiSourceDefinition", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("aiSourceDefList", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:aiSourceDefinition");
		return mv;
	}
	
	/*@RequestMapping(value = "/aiJobsUpload", method = RequestMethod.GET)
	public ModelAndView aiJobsUpload(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) throws IOException {
		LOGGER.debug("in aiJobsUpload()");
		User user = CommonUtils.getUserDetails(request, null, null);
		CommonUtils.setActiveScreenName("aiJobsUpload", session);
		try {
			DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getBusinessModelInfo",
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("businessModel", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		
		mv.setViewName("tiles-anvizent-admin:aiJobsUpload");
		return mv;
	}*/
	
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("aiBussinessModalList")
	public List<BusinessModal> getAIBussinessList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<BusinessModal> bussinessMoadlList = new ArrayList<>();

		DataResponse bussinessMoadlDataResponse = aiRestUtilities.getRestObject(request, "/getBusinessModelInfo",
				user.getUserId());
		List<LinkedHashMap<String, Object>> bussinessModal = null;
		if (bussinessMoadlDataResponse != null && bussinessMoadlDataResponse.getHasMessages()
				&& bussinessMoadlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			bussinessModal = (List<LinkedHashMap<String, Object>>) bussinessMoadlDataResponse.getObject();
		} else {
			bussinessModal = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : bussinessModal) {
			BusinessModal businessModal = new BusinessModal();
			int bmid = Integer.parseInt(map.get("bmid").toString());
			businessModal.setBmid(bmid);
			businessModal.setBusinessProblem(map.get("businessProblem").toString());
			businessModal.setaIStagingTable(map.get("aIStagingTable").toString());
			bussinessMoadlList.add(businessModal);
		}

		return bussinessMoadlList;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("aiBussinessModalSet")
	public Set<String> aiBussinessModalSet(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Set<String> bussinessMoadlList = new HashSet<>();

		DataResponse bussinessMoadlDataResponse = aiRestUtilities.getRestObject(request, "/getBusinessModelInfo",
				user.getUserId());
		List<LinkedHashMap<String, Object>> bussinessModal = null;
		if (bussinessMoadlDataResponse != null && bussinessMoadlDataResponse.getHasMessages()
				&& bussinessMoadlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			bussinessModal = (List<LinkedHashMap<String, Object>>) bussinessMoadlDataResponse.getObject();
		} else {
			bussinessModal = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : bussinessModal) {
			//BusinessModal businessModal = new BusinessModal();
			//businessModal.setBusinessProblem(map.get("businessProblem").toString());
			//businessModal.setaIStagingTable(map.get("aIStagingTable").toString());
			bussinessMoadlList.add(map.get("businessProblem").toString());
		}

		return bussinessMoadlList;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("aiBussinessusecaseStagingTable")
	public Set<String> aiBussinessusecaseStagingTable(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Set<String> bussinessMoadlList = new HashSet<>();

		DataResponse bussinessMoadlDataResponse = aiRestUtilities.getRestObject(request, "/getBusinessModelInfo",
				user.getUserId());
		List<LinkedHashMap<String, Object>> bussinessModal = null;
		if (bussinessMoadlDataResponse != null && bussinessMoadlDataResponse.getHasMessages()
				&& bussinessMoadlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			bussinessModal = (List<LinkedHashMap<String, Object>>) bussinessMoadlDataResponse.getObject();
		} else {
			bussinessModal = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : bussinessModal) {
			//BusinessModal businessModal = new BusinessModal();
			//businessModal.setBusinessProblem(map.get("businessProblem").toString());
			//businessModal.setaIStagingTable(map.get("aIStagingTable").toString());
			bussinessMoadlList.add(map.get("aIStagingTable").toString());
		}

		return bussinessMoadlList;
	}
	
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("aiContextParamList")
	public List<AIContextParameter> getAIContextParameters(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<AIContextParameter> aiContextParamList = new ArrayList<>();

		DataResponse aiContextParamDataResponse = aiRestUtilities.getRestObject(request, "/getAiContextParameters",
				user.getUserId());
		List<LinkedHashMap<String, Object>> aiContextParameters = null;
		if (aiContextParamDataResponse != null && aiContextParamDataResponse.getHasMessages()
				&& aiContextParamDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			aiContextParameters = (List<LinkedHashMap<String, Object>>) aiContextParamDataResponse.getObject();
		} else {
			aiContextParameters = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : aiContextParameters) {
			AIContextParameter aiContext = new AIContextParameter();
			int pid = Integer.parseInt(map.get("pid").toString());
			aiContext.setPid(pid);
			aiContext.setParamName(map.get("paramName").toString());
			aiContext.setParamValue(map.get("paramValue").toString());
			aiContextParamList.add(aiContext);
		}

		return aiContextParamList;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("aiModalInfoList")
	public List<AIModel> getAiModelInfo(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<AIModel> aiModalInfoList = new ArrayList<>();

		DataResponse aiModalDataResponse = aiRestUtilities.getRestObject(request, "/getAiModelInfo",
				user.getUserId());
		List<LinkedHashMap<String, Object>> aiModalInfo = null;
		if (aiModalDataResponse != null && aiModalDataResponse.getHasMessages()
				&& aiModalDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			aiModalInfo = (List<LinkedHashMap<String, Object>>) aiModalDataResponse.getObject();
		} else {
			aiModalInfo = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : aiModalInfo) {
			AIModel aiModal = new AIModel();
			int aId = Integer.parseInt(map.get("id").toString());
			aiModal.setId(aId);
			aiModal.setaIModelName(map.get("aIModelName").toString());
			aiModalInfoList.add(aiModal);
		}

		return aiModalInfoList;
	}


@RequestMapping(value = "/aiContextParameters", method = RequestMethod.GET)
public ModelAndView aiContextParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
		HttpSession session, Locale locale) throws IOException {
	LOGGER.debug("in aiContextParameters()");
	User user = CommonUtils.getUserDetails(request, null, null);
	try {
		DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getAiContextParameters",
				user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("aiContextParameters", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}
		} else {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
	} catch (Exception e) {
		mv.addObject("messagecode", "FAILED");
		mv.addObject("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		LOGGER.error("", e);
	}
	CommonUtils.setActiveScreenName("aiContextParameters", session);
	mv.setViewName("tiles-anvizent-entry:aiContextParameters");
	return mv;
}

@RequestMapping(value = "/aiModeList", method = RequestMethod.GET)
public ModelAndView aiModeList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
		HttpSession session, Locale locale) throws IOException {
	LOGGER.debug("in AI Models()");
	User user = CommonUtils.getUserDetails(request, null, null);
	try {
		DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getAiModelInfo",
				user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("aiModels", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}
		} else {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
	} catch (Exception e) {
		mv.addObject("messagecode", "FAILED");
		mv.addObject("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		LOGGER.error("", e);
	}
	CommonUtils.setActiveScreenName("aiModel", session);
	mv.setViewName("tiles-anvizent-entry:aiModel");
	return mv;
}

@RequestMapping(value = "/aiJobsUpload", method = RequestMethod.GET)
public ModelAndView aiJobsUploadList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
		HttpSession session, Locale locale) throws IOException {
	LOGGER.debug("in aiJobsUploadList()");
	User user = CommonUtils.getUserDetails(request, null, null);
	CommonUtils.setActiveScreenName("aiJobsUpload", session);
	try {
		DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getAIJobsUploadList",
				user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("aiJobsUploadList", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}
		} else {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
	} catch (Exception e) {
		mv.addObject("messagecode", "FAILED");
		mv.addObject("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		LOGGER.error("", e);
	}
	mv.setViewName("tiles-anvizent-entry:aiJobsUpload");
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

/*@SuppressWarnings("unchecked")
@ModelAttribute("timeZonesList")
public Map<Object, Object> getTimesZoneList(HttpServletRequest request) {

	User user = CommonUtils.getUserDetails(request, null, null);
	DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getTimeZones", user.getUserId());
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
}*/

@RequestMapping(value = "/aiCommonJobs", method = RequestMethod.GET)
public ModelAndView aiCommonJobs(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
		HttpSession session, Locale locale) throws IOException {
	LOGGER.debug("in aiCommonJobs()");
	User user = CommonUtils.getUserDetails(request, null, null);
	try {
		DataResponse dataResponse = aiRestUtilities.getRestObject(request, "/getaiCommonJobs",
				user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("FileNames", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}
		} else {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
	} catch (Exception e) {
		mv.addObject("messagecode", "FAILED");
		mv.addObject("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		LOGGER.error("", e);
	}
	CommonUtils.setActiveScreenName("aiCommonJobs", session);
	mv.setViewName("tiles-anvizent-entry:aiCommonJob");
	return mv;
}


@RequestMapping(value = "/getrJobExecutionInfo/{businessName}/{modelName}", method = RequestMethod.GET)
public ModelAndView getrJobExecutionInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
		@PathVariable("businessName") String businessName,
		@PathVariable("modelName") String modelName,
		HttpSession session, Locale locale) throws IOException {
	LOGGER.debug("in getrJobExecutionInfo()");
	User user = CommonUtils.getUserDetails(request, null, null);
	try {
		MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
		maps.add("businessName", businessName);
		maps.add("modelName", modelName);
		DataResponse dataResponse = aiRestUtilities.postRestObject(request, "/getrJobExecutionInfo",maps,user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("rJobExecution", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}
		} else {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
	} catch (Exception e) {
		mv.addObject("messagecode", "FAILED");
		mv.addObject("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		LOGGER.error("", e);
	}
	mv.setViewName("tiles-anvizent-entry:rJobExecution");
	return mv;
}

}
