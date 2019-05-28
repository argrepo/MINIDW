/**
 * 
 */
package com.datamodel.anvizent.controller;

import java.io.IOException;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rakesh.gajula
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/package")
public class ScheduleController {

	protected static final Log LOGGER = LogFactory.getLog(ScheduleController.class);

	private @Value("${run.with.scheduler.or.runnow:runwithscheduler}") String runWithSchedulerOrRunnow;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public ModelAndView schedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale
			,RedirectAttributes redirectAttributes) {

		User user = CommonUtils.getUserDetails(request, null, null);
		CommonUtils.setActiveScreenName("schedule", session);

		try {

			List<LinkedHashMap<String, Object>> packageList = restUtilities.getRestObject(request, "/schedule/getPackagesForScheduling", List.class,
					user.getUserId());
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<Package> packageListForScheduling = mapper.convertValue(packageList, new TypeReference<List<Package>>() {
			});
			mv.addObject("packageListForScheduling", packageListForScheduling);
			mv.addObject("runWithSchedulerOrRunnow", runWithSchedulerOrRunnow);
			mv.setViewName("tiles-anvizent-entry:schedule");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/unSchedule", method = RequestMethod.GET)
	public ModelAndView unSchedule(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale
			,RedirectAttributes redirectAttributes) {

		User user = CommonUtils.getUserDetails(request, null, null);
		CommonUtils.setActiveScreenName("schedule", session);

		try {

			List<LinkedHashMap<String, Object>> packageList = restUtilities.getRestObject(request, "/schedule/getPackagesForScheduling", List.class,user.getUserId());
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<Package> packageListForScheduling = mapper.convertValue(packageList, new TypeReference<List<Package>>() {
			});
			mv.addObject("packageListForScheduling", packageListForScheduling);
			mv.setViewName("tiles-anvizent-entry:schedule");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
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
	@ModelAttribute("timeZonesList")
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
	
	@RequestMapping(value = "/viewCurrencyLodJobResults", method = RequestMethod.GET)
	public ModelAndView viewCurrencyLodJobResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@RequestParam(value = "source", required = false) String fromSource, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {

		    User user = CommonUtils.getUserDetails(request, null, null);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/schedule/getJobResults", user.getUserId());
			mv.addObject("viewResultList", dataResponse.getObject());
			jobResultForm.setPageMode("CurrencyLoad");
			mv.setViewName("tiles-anvizent-entry:viewResults");
	   	    return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchCurrencyLodJobResults", method = RequestMethod.POST)
	public ModelAndView searchCurrencyLodJobResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = restUtilities.getRestObject(request, "/schedule/getJobResultsByDate/{" + fromDate + "}/{" + toDate + "}",
					user.getUserId(), fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}
	
	@RequestMapping(value = "/scheduleStatus", method = RequestMethod.GET)
	public ModelAndView scheduleStatus(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale,RedirectAttributes redirectAttributes) throws IOException {
		String deploymentType = (String)SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		if ( StringUtils.isNotBlank(deploymentType) && deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_HYBRID)) {
			mv.setViewName("tiles-anvizent-entry:scheduleStatus");
		} else {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", "Client Scheduler will be available only for hybrid clients");
			mv.setViewName("redirect:/adt/package/schedule");
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/scheduleJobInfo", method = RequestMethod.GET)
	public ModelAndView scheduleJobInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale,RedirectAttributes redirectAttributes) throws IOException {
		String deploymentType = (String)SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		if ( StringUtils.isNotBlank(deploymentType) && deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_HYBRID)) {
			mv.setViewName("tiles-anvizent-entry:scheduleJobInfo");
		} else {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", "Client Scheduler will be available only for hybrid clients");
			mv.setViewName("redirect:/adt/package/schedule");
		}
		
		return mv;
	}
}
