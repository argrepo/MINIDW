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

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientConnectorMappingForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/admin")
public class ChangeClientController {

	protected static final Log LOGGER = LogFactory.getLog(ChangeClientController.class);

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;


	@RequestMapping(value = "/clientchange", method = RequestMethod.GET)
	public ModelAndView clientchange(HttpServletRequest request,
			@ModelAttribute("clientConnectorMappingForm") ClientConnectorMappingForm clientConnectorMappingForm,
			HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale) {
		LOGGER.debug("in clientchange()");
		String clientId = (String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID);
		clientConnectorMappingForm.setClientId(Integer.valueOf(clientId));
		CommonUtils.setActiveScreenName("clientchange", session);
		mv.setViewName("tiles-anvizent-admin:clientchange");
		return mv;
	}
	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClientsForChangeRequest")
	public Map<Object, Object> getAllClientInfoForChangeRequest(HttpServletRequest request) {

		String activeClientsUrl = "/activeClients";
		User user = CommonUtils.getUserDetails(request, null, null);
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
				clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
			}
		}
		return clientMap;
	}

}
