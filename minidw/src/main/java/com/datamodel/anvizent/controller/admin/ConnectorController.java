package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
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
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientConnectorMappingForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.ConnectorForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author usharani.konda, apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/connector")
public class ConnectorController {
	protected static final Log LOGGER = LogFactory.getLog(ConnectorController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView connector(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("connectorForm") ConnectorForm connectorForm, BindingResult result, Locale locale) {
		LOGGER.debug("in connector()");
		CommonUtils.setActiveScreenName("connector", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			connectorForm.setPageMode("list");

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getconnector", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("connector", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("connector", dataResponse.getObject());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:connector");
		return mv;
	}

	@RequestMapping(value = { "/edit", "/update" }, method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/connector");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getConnectorDetailsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("connectorForm") ConnectorForm connectorForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.debug("in getConnectorDetailsById()");
		if (connectorForm.getId() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.connectorIdisShouldNotBeEmpty", null, locale));
			mv.setViewName("redirect:/admin/connector");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			connectorForm.setPageMode("edit");
			mv.setViewName("redirect:/admin/connector");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getConnectorDetailsById/{id}", user.getUserId(), connectorForm.getId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> databaseobj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					Database database = mapper.convertValue(databaseobj, new TypeReference<Database>() {
					});
					connectorForm.setId(database.getId());
					connectorForm.setConnectorName(database.getConnectorName());
					connectorForm.setIsActive(database.getIsActive());
					connectorForm.setConnectorId(database.getConnector_id());
					mv.setViewName("tiles-anvizent-admin:connector");

				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView connectorUpdate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("connectorForm") ConnectorForm connectorForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.debug("in connector update ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			Database database = new Database();
			database.setConnectorName(connectorForm.getConnectorName());
			database.setConnector_id(connectorForm.getConnectorId());
			database.setIsActive(connectorForm.getIsActive());
			database.setId(connectorForm.getId());
			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateConnector", database, user.getUserId());
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
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/connector");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView connectorAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("connectorForm") ConnectorForm connectorForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.debug("in connectorForm Add ()");
		mv.setViewName("tiles-anvizent-admin:connector");
		connectorForm.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addConnector(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("connectorForm") ConnectorForm connectorForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.debug("in connector Add ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			Database database = new Database();

			database.setConnectorName(connectorForm.getConnectorName());
			database.setConnector_id(connectorForm.getConnectorId());
			database.setIsActive(connectorForm.getIsActive());

			DataResponse dataResponse = restUtilities.postRestObject(request, "/createConnector", database, user.getUserId());
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
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/connector");
		return mv;
	}

	@RequestMapping(value = "/clientMapping", method = RequestMethod.GET)
	public ModelAndView clientConnectorMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("clientConnectorMappingForm") ClientConnectorMappingForm clientConnectorMappingForm, BindingResult result) {
		LOGGER.info("in clientConnectorMapping()");
		CommonUtils.setActiveScreenName("connectorclientMapping", session);
		mv.setViewName("tiles-anvizent-admin:clientConnectorMapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientMapping", method = RequestMethod.POST)
	public ModelAndView getClientMappedDatabases(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientConnectorMappingForm") ClientConnectorMappingForm clientConnectorMappingForm, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		LOGGER.info("in getClientMappedDatabases()");

		if (clientConnectorMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/connector/clientMapping");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getConnectorsByClientId/{client_Id}", user.getUserId(),
					clientConnectorMappingForm.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				List<LinkedHashMap<String, Object>> obj = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				List<Database> databasesList = mapper.convertValue(obj, new TypeReference<List<Database>>() {
				});
				Map<Object, Object> connectors = new HashMap<Object, Object>();
				for (Database db : databasesList) {
					connectors.put(db.getConnector_id(), db.getConnectorName());
				}
				mv.addObject("getAllConnectors", connectors);
			}

			DataResponse dataResponse1 = restUtilities.getRestObject(request, "/getDatabaseMappedConnectors/{client_Id}", user.getUserId(),
					clientConnectorMappingForm.getClientId(), clientConnectorMappingForm.getDatabaseId());
			if (dataResponse1 != null && dataResponse1.getHasMessages() && dataResponse1.getMessages().get(0).getCode().equals("SUCCESS")) {
				List<LinkedHashMap<String, Object>> obj = (List<LinkedHashMap<String, Object>>) dataResponse1.getObject();
				List<Database> databasesList = mapper.convertValue(obj, new TypeReference<List<Database>>() {
				});
				clientConnectorMappingForm.setConnectors(new ArrayList<>());
				List<String> selectedConnectorsList = clientConnectorMappingForm.getConnectors();
				databasesList.forEach(database -> {
					selectedConnectorsList.add(database.getConnector_id() + "");
				});
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("tiles-anvizent-admin:clientConnectorMapping");
		return mv;
	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.GET)
	public ModelAndView handleMappingGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/connector/clientMapping");
		return mv;
	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.POST)
	public ModelAndView saveClientConnectorMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientConnectorMappingForm") ClientConnectorMappingForm clientConnectorMappingForm, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		LOGGER.info("in saveClientConnectorMapping()");

		if (clientConnectorMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/connector/clientMapping");
			return mv;
		}

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", clientConnectorMappingForm.getClientId());
			if (clientConnectorMappingForm.getConnectors() != null && clientConnectorMappingForm.getConnectors().size() > 0) {
				clientConnectorMappingForm.getConnectors().forEach(connectorId -> {
					map.add("connectorId", connectorId);
				});
			}

			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveClientConnectorMapping", map, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			clientConnectorMappingForm.setClientId(0);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/connector/clientMapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getAllDatabases")
	public Map<Object, Object> getAllDatabases(HttpServletRequest request) {
		Map<Object, Object> databaseList = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllDatabases", user.getUserId());
		if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> databaseResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<Database> database = mapper.convertValue(databaseResponse, new TypeReference<List<Database>>() {
			});
			for (Database db : database) {
				databaseList.put(db.getId(), db.getName());
			}
		} else {
			return new HashMap<Object, Object>();
		}
		return databaseList;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClients")
	public Map<Object, Object> getAllClientInfo(HttpServletRequest request) {
		
		//TODO
		long clientId = Long.valueOf((String)SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
		User user = CommonUtils.getUserDetails(request, null, null);

		String activeClientsUrl = "/activeClients";
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,new LinkedMultiValueMap<>(),  DataResponse.class, user.getUserId());
		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients  = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});
			
			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				//TODO Add this condition
				if ( clientId == cloudClient.getId() ) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}

}
