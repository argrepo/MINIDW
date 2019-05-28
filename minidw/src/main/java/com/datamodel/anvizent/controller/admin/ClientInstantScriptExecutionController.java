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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/admin/clientsInstantScriptExecution")
public class ClientInstantScriptExecutionController {
	protected static final Log LOGGER = LogFactory.getLog(ClientInstantScriptExecutionController.class);

	@Autowired
	@Qualifier("clientInstantScriptExecutionDataRestController")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@ModelAttribute("getClientList")
	public Map<Object, Object> getClientList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);

		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, "/activeClients",
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
				clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
			}
		}
		return clientMap;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView clientsInstantScriptExecution(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, HttpSession session, @ModelAttribute("tableScriptsForm") TableScriptsForm tableScripts,
			Locale locale) {
		LOGGER.debug("in clientsInstantScriptExecution()");
		CommonUtils.setActiveScreenName("tableScriptsForm", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getClientsInstantScriptExecutionData",user.getUserId());
		try {
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("clientInstantScriptExecution", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("clientInstantScriptExecution", dataResponse.getObject());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-admin:clientsInstantScriptExecution");
		return mv;
	}
	
	@RequestMapping(value = "/scriptExecution", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsExecution(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm,
			BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in scriptExecution()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse executeClientRespectiveScriptsDs =  restUtilities.postRestObject(request, "/executeClientRespectiveScripts",tableScriptsForm,user.getUserId());
		    if (executeClientRespectiveScriptsDs.getMessages().get(0).getCode().equals("SUCCESS")) {
		    	tableScriptsForm.getClientIdList().clear();
		    	tableScriptsForm.setSqlScript(null);
		    DataResponse dataResponse = restUtilities.getRestObject(request, "/getClientsInstantScriptExecutionData",user.getUserId());
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("clientInstantScriptExecution", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				mv.addObject("clientInstantScriptExecution", dataResponse.getObject());
			}
		    }else{
		    	mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", executeClientRespectiveScriptsDs.getMessages().get(0).getText());
		    }
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientsInstantScriptExecution");
		return mv;
	}

}
