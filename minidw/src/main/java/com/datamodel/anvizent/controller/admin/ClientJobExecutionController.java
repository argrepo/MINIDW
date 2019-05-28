
package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping(value = "/admin/clientJobExecutionParameters")
public class ClientJobExecutionController {
	protected static final Log LOGGER = LogFactory.getLog(ClientJobExecutionController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView clientJobExecution(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("clientJobExecutionParameters") ClientJobExecutionParameters clientJobExecutionParameters, Locale locale) {
		LOGGER.debug("in clientJobExecution()");
		CommonUtils.setActiveScreenName("clientJobExecutionParameters", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.getRestObject(request, "/clientJobExecutionParametersDetailsById",user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> middleObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ClientJobExecutionParameters clientJobExecParams = mapper.convertValue(middleObj,
							new TypeReference<ClientJobExecutionParameters>() {
							});
					
					clientJobExecutionParameters.setId(clientJobExecParams.getId());
					clientJobExecutionParameters.setSourceTimeZone(clientJobExecParams.getSourceTimeZone());
					clientJobExecutionParameters.setDestTimeZone(clientJobExecParams.getDestTimeZone());
					clientJobExecutionParameters.setNullReplaceValues(clientJobExecParams.getNullReplaceValues());
					clientJobExecutionParameters.setCaseSensitive(clientJobExecParams.getCaseSensitive());
					clientJobExecutionParameters.setInterval(clientJobExecParams.getInterval());
					
					
					mv.setViewName("tiles-anvizent-admin:clientJobExecutionParameters");
				} 
			} 

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-admin:clientJobExecutionParameters");
		return mv;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView addClientJobExecutionParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientJobExecutionParameters") ClientJobExecutionParameters clientJobExecutionParameters,  RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in addClientJobExecutionParameters ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveClientJobExecutionParameters", clientJobExecutionParameters,user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
			mv.setViewName("redirect:/admin/clientJobExecutionParameters");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}
	@SuppressWarnings("unchecked")
	@ModelAttribute("timesZoneList")
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

}
