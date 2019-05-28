
package com.datamodel.anvizent.controller.admin;

import java.util.HashMap;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@RequestMapping(value = "/admin/hybridClientsGrouping")
public class HybridClientsGroupingController {
	protected static final Log LOGGER = LogFactory.getLog(HybridClientsGroupingController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView hybridClientsGrouping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping,
			Locale locale) {
		LOGGER.debug("in hybridClientsGrouping()");
		CommonUtils.setActiveScreenName("hybridClientsGrouping", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			hybridClientsGrouping.setPageMode("list");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getHybridClientsGrouping",
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("hybridClientMappingInfo", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-admin:hybridClientsGrouping");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView hybridClientsGroupingAdd(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping,
			RedirectAttributes redirectAttributes) {
		LOGGER.debug("in hybridClientsGrouping()");
		mv.setViewName("tiles-anvizent-admin:hybridClientsGrouping");
		hybridClientsGrouping.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addHybridClientsGrouping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in addHybridClientsGrouping ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveHybridClientsGroupingInfo",
					hybridClientsGrouping, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
			mv.setViewName("redirect:/admin/hybridClientsGrouping");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClients")
	public Map<Object, Object> getAllClientInfo(HttpServletRequest request) {

		// TODO
		User user = CommonUtils.getUserDetails(request, null, null);

		String activeClientsUrl = "/activeClients";
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
				new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});

			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				if (cloudClient.getDeploymentType().equals(Constants.Config.DEPLOYMENT_TYPE_HYBRID)) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getHybridClientGroupingDetailsById(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,@ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getHybridClientGroupingDetailsById()");
		if (hybridClientsGrouping.getId() == 0) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.versionIdisShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/hybridClientsGrouping");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			mv.setViewName("redirect:/admin/hybridClientsGrouping");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getHybridClientGroupingDetailsById/{id}",
					user.getUserId(), hybridClientsGrouping.getId());
			
			/*DataResponse dataResponseHybrid = restUtilities.getRestObject(request, "/getHybridClientGroupingDetailsByAccessKey/{accessKey}",
					user.getUserId(), hybridClientsGrouping.getAccessKey());*/

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> hybridObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					HybridClientsGrouping hybridInfo = mapper.convertValue(hybridObj,
							new TypeReference<HybridClientsGrouping>() {
							});
					hybridClientsGrouping.setName(hybridInfo.getName());
					hybridClientsGrouping.setAccessKey(hybridInfo.getAccessKey());
					hybridClientsGrouping.setClientId(hybridInfo.getClientId());
					hybridClientsGrouping.setDescription(hybridInfo.getDescription());
					hybridClientsGrouping.setActive(hybridInfo.isActive());
					hybridClientsGrouping.setLastModifiedDate(hybridInfo.getLastModifiedDate());
					hybridClientsGrouping.setIntervalType(hybridInfo.getIntervalType());
					hybridClientsGrouping.setIntervalTime(hybridInfo.getIntervalTime());
					hybridClientsGrouping.setPackageThreadCount(hybridInfo.getPackageThreadCount());
					hybridClientsGrouping.setPageMode("edit");
					;
					mv.setViewName("tiles-anvizent-admin:hybridClientsGrouping");
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}

}
