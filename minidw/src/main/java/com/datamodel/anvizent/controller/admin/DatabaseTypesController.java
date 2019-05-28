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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientDatabaseMappingForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DatabaseForm;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author usharani.konda
 *
 */
@Controller
@RequestMapping(value = "/admin/database")
public class DatabaseTypesController {
	protected static final Log logger = LogFactory.getLog(DatabaseTypesController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView database(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("databaseForm") DatabaseForm databaseForm, BindingResult result,
			Locale locale) {
		logger.debug("in database()");
		CommonUtils.setActiveScreenName("database", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			databaseForm.setPageMode("list");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getDatabase", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("databaseConnector", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("databaseConnector", dataResponse.getObject());
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
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-admin:database");
		return mv;
	}

	@RequestMapping(value = { "/edit", "/update" }, method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/database");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getDBDetailsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("databaseForm") DatabaseForm databaseForm, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		logger.debug("in getDBDetailsById()");
		if (databaseForm.getId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.databaseIdisShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/database");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			mv.setViewName("redirect:/admin/database");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getDBDetailsById/{id}", user.getUserId(),
					databaseForm.getId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> databaseobj = (LinkedHashMap<String, Object>) dataResponse
							.getObject();
					Database database = mapper.convertValue(databaseobj, new TypeReference<Database>() {
					});

					databaseForm.setDatabaseName(database.getName());
					databaseForm.setIsActive(database.getIsActive());
					databaseForm.setId(database.getId());
					databaseForm.setPageMode("edit");
					databaseForm.setDriverName(database.getDriverName());
					databaseForm.setProtocal(database.getProtocal());
					databaseForm.setConnectionStringParams(database.getConnectionStringParams());
					databaseForm.setUrlFormat(database.getUrlFormat());
					databaseForm.setConJars(database.getConJars());
					mv.setViewName("tiles-anvizent-admin:database");
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

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView database(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("databaseForm") DatabaseForm databaseForm, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		logger.debug("in database update ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			Database database = new Database();
			database.setName(databaseForm.getDatabaseName());
			database.setIsActive(databaseForm.getIsActive());
			database.setId(databaseForm.getId());
			database.setDriverName(databaseForm.getDriverName());
			database.setProtocal(databaseForm.getProtocal());
			database.setConnectionStringParams(databaseForm.getConnectionStringParams());
			database.setUrlFormat(databaseForm.getUrlFormat());
			StringBuffer sb = new StringBuffer();
			String libPath = request.getServletContext().getRealPath("/WEB-INF/lib");
			if (databaseForm.getConnectorJars() != null && databaseForm.getConnectorJars().size() > 0) {
				for (MultipartFile file : databaseForm.getConnectorJars()) {
					if (!file.isEmpty()) {
						CommonUtils.multipartFileToLib(file, libPath);
						sb.append(file.getOriginalFilename()).append(",");
					}
				}
				if (databaseForm.getConJars() != null) {
					List<String> jarsList = databaseForm.getConJars();
					if (jarsList.size() > 0) {
						for (String jar : jarsList) {
							sb.append(jar).append(",");
						}
					}
				}
				String connectorJars = sb.toString();
				if (connectorJars.length() > 0)
					database.setConnectorJars(connectorJars.substring(0, connectorJars.length() - 1));
			}
			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateDatabase", database,
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
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
		mv.setViewName("redirect:/admin/database");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView databaseConnectorAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("databaseForm") DatabaseForm databaseForm, RedirectAttributes redirectAttributes,
			BindingResult result) {
		logger.debug("in databaseForm Add ()");
		mv.setViewName("tiles-anvizent-admin:database");
		databaseForm.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView databaseConnector(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("databaseForm") DatabaseForm databaseForm, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		logger.debug("in database Add ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			Database database = new Database();
			database.setName(databaseForm.getDatabaseName());
			database.setIsActive(databaseForm.getIsActive());
			database.setDriverName(databaseForm.getDriverName());
			database.setProtocal(databaseForm.getProtocal());
			database.setConnectionStringParams(databaseForm.getConnectionStringParams());
			database.setUrlFormat(databaseForm.getUrlFormat());
			StringBuffer sb = new StringBuffer();
			String libPath = request.getServletContext().getRealPath("/WEB-INF/lib");
			if (databaseForm.getConJars() == null) {
				if (databaseForm.getConnectorJars() != null && databaseForm.getConnectorJars().size() > 0) {
					for (MultipartFile file : databaseForm.getConnectorJars()) {
						if (!file.isEmpty()) {
							CommonUtils.multipartFileToLib(file, libPath);
							sb.append(file.getOriginalFilename()).append(",");
						}
					}
					String connectorJars = sb.toString();
					if (connectorJars.length() > 0)
						database.setConnectorJars(connectorJars.substring(0, connectorJars.length() - 1));
				}
			}
			DataResponse dataResponse = restUtilities.postRestObject(request, "/createDB", database, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.setViewName("redirect:/admin/database");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	@RequestMapping(value = "/clientMapping", method = RequestMethod.GET)
	public ModelAndView clientDatabaseMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientDatabaseMappingForm") ClientDatabaseMappingForm clientDatabaseMappingForm,
			BindingResult result) {
		logger.info("in clientDatabaseMapping()");
		clientDatabaseMappingForm.setPageMode("databases");
		mv.setViewName("tiles-anvizent-admin:clientDatabaseMapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientMapping", method = RequestMethod.POST)
	public ModelAndView getDatabases(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientDatabaseMappingForm") ClientDatabaseMappingForm clientDatabaseMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		logger.info("in clientDatabaseMapping()");

		if (clientDatabaseMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/database/clientMapping");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getDatabasesByClientId/{clientId}",
					user.getUserId(), clientDatabaseMappingForm.getClientId());
			List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			List<Database> databases = mapper.convertValue(data, new TypeReference<List<Database>>() {
			});

			clientDatabaseMappingForm.setDatabases(new ArrayList<>());
			List<String> selectedDatabaseList = clientDatabaseMappingForm.getDatabases();

			databases.forEach(database -> {
				selectedDatabaseList.add(database.getId() + "");
			});
			clientDatabaseMappingForm.setDatabaseId(0);

			mv.setViewName("tiles-anvizent-admin:clientDatabaseMapping");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.POST)
	public ModelAndView saveClientDatabaseMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientDatabaseMappingForm") ClientDatabaseMappingForm clientDatabaseMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		logger.info("in saveClientDatabaseMapping()");

		if (clientDatabaseMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/clientDatabaseMapping");
			return mv;
		}
		/* redirecting to default page with status */
		mv.setViewName("redirect:/admin/database/clientMapping");
		redirectAttributes.addFlashAttribute("messagecode", "ERROR");
		redirectAttributes.addFlashAttribute("errors",
				messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = null;
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", clientDatabaseMappingForm.getClientId());
			if (clientDatabaseMappingForm.getDatabases() != null
					&& clientDatabaseMappingForm.getDatabases().size() > 0) {
				clientDatabaseMappingForm.getDatabases().forEach(databaseId -> {
					map.add("databaseId", databaseId);
				});
			}
			dataResponse = restUtilities.postRestObject(request, "/saveClientDatabaseMapping", map, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.GET)
	public ModelAndView handleMappingGetRequest(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv) {
		mv.setViewName("redirect:/admin/database/clientMapping");
		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClients")
	public Map<Object, Object> getAllClientInfo(HttpServletRequest request) {

		// TODO
		long clientId = Long
				.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
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
				// TODO Add this condition
				if (clientId == cloudClient.getId()) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getAllDatabases")
	public Map<Object, Object> getAllDatabases(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllDatabases", user.getUserId());
		List<Database> database = null;
		if (dataResponse != null && dataResponse.getHasMessages()
				&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> databaseResponse = (List<LinkedHashMap<String, Object>>) dataResponse
					.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			database = mapper.convertValue(databaseResponse, new TypeReference<List<Database>>() {
			});
		} else {
			database = new ArrayList<>();
		}

		Map<Object, Object> databaseList = new LinkedHashMap<>();
		for (Database db : database) {
			databaseList.put(db.getId(), db.getName());
		}
		return databaseList;
	}
}
