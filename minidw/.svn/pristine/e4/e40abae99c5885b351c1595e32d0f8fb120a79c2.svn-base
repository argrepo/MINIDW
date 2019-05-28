package com.datamodel.anvizent.controller.admin;

import java.util.LinkedHashMap;
import java.util.Locale;

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
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ServerConfigurations;
import com.datamodel.anvizent.service.model.ServerConfigurationsForm;
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
@RequestMapping(value = "/admin/serverConfigurations")
public class ServerConfigurationsController {
	protected static final Log LOGGER = LogFactory.getLog(ServerConfigurationsController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getAllServerConfigurations(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("serverConfigurationsForm") ServerConfigurationsForm serverConfigurationsForm, Locale locale) {
		LOGGER.debug("in getAllServerConfigurations()");

		CommonUtils.setActiveScreenName("serverConfiguration", session);

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllServerConfigurations", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("serverConfigList", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

			serverConfigurationsForm.setPageMode("list");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:serverConfiguration");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addNewServerConfiguration(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("serverConfigurationsForm") ServerConfigurationsForm serverConfigurationsForm, Locale locale) {
		LOGGER.debug("in addNewServerConfiguration()");
		serverConfigurationsForm.setPageMode("add");
		mv.setViewName("tiles-anvizent-admin:serverConfiguration");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveServerConfiguration(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("serverConfigurationsForm") ServerConfigurationsForm serverConfigurationsForm, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in saveServerConfiguration()");

		mv.setViewName("tiles-anvizent-admin:serverConfiguration");
		if (serverConfigurationsForm.getId() == 0)
			serverConfigurationsForm.setPageMode("add");
		else
			serverConfigurationsForm.setPageMode("edit");

		try {
			User user = CommonUtils.getUserDetails(request, null, null);

			ILConnection iLConnection = new ILConnection();
			iLConnection.setServer(serverConfigurationsForm.getIpAddress() + ":" + serverConfigurationsForm.getPortNumber());
			iLConnection.setUsername(serverConfigurationsForm.getUserName());
			iLConnection.setPassword(serverConfigurationsForm.getServerPassword());
			Database db = new Database();
			db.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
			db.setName(serverConfigurationsForm.getServerName());
			iLConnection.setDatabase(db);

			DataResponse dataResponse = restUtilities.postRestObject(request, "/testServerConnection", iLConnection, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					DataResponse saveDataResponse = null;
					if (serverConfigurationsForm.getId() == 0) {
						// insert new server configuration details
						saveDataResponse = restUtilities.postRestObject(request, "/saveServerConfigurationDetails", serverConfigurationsForm, user.getUserId());
						serverConfigurationsForm.setPageMode("add");
					} else {
						// edit existing server configuration details
						saveDataResponse = restUtilities.postRestObject(request, "/updateServerConfigurationDetails", serverConfigurationsForm,
								user.getUserId());
						serverConfigurationsForm.setPageMode("edit");
					}
					if (saveDataResponse != null && saveDataResponse.getHasMessages()) {

						if (saveDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.setViewName("redirect:/admin/serverConfigurations");
							redirectAttributes.addFlashAttribute("messagecode", saveDataResponse.getMessages().get(0).getCode());
							redirectAttributes.addFlashAttribute("errors", saveDataResponse.getMessages().get(0).getText());
						} else {
							mv.addObject("messagecode", saveDataResponse.getMessages().get(0).getCode());
							mv.addObject("errors", saveDataResponse.getMessages().get(0).getText());
						}
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
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
			e.printStackTrace();
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editServerConfiguration(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("serverConfigurationsForm") ServerConfigurationsForm serverConfigurationsForm, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in editServerConfiguration()");

		try {
			if (serverConfigurationsForm.getId() != 0) {
				User user = CommonUtils.getUserDetails(request, null, null);
				DataResponse dataResponse = restUtilities.getRestObject(request, "/getServerConfigurationDetailsById/{id}", user.getUserId(),
						serverConfigurationsForm.getId());
				if (dataResponse != null && dataResponse.getHasMessages()) {

					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						ServerConfigurations sc = mapper.convertValue(map, new TypeReference<ServerConfigurations>() {
						});
						serverConfigurationsForm.setId(sc.getId());
						serverConfigurationsForm.setServerName(sc.getServerName());
						serverConfigurationsForm.setShortName(sc.getShortName());
						serverConfigurationsForm.setDescription(sc.getDescription());
						serverConfigurationsForm.setIpAddress(sc.getIpAddress());
						serverConfigurationsForm.setPortNumber(sc.getPortNumber());
						serverConfigurationsForm.setMinidwSchemaName(sc.getMinidwSchemaName());
						serverConfigurationsForm.setAnvizentSchemaName(sc.getAnvizentSchemaName());
						serverConfigurationsForm.setUserName(sc.getUserName());
						serverConfigurationsForm.setServerPassword(sc.getServerPassword());
						serverConfigurationsForm.setActiveStatus(sc.isActiveStatus());
						serverConfigurationsForm.setClientDbDetailsEndPoint(sc.getClientDbDetailsEndPoint());
					} else {
						mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
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
		serverConfigurationsForm.setPageMode("edit");
		mv.setViewName("tiles-anvizent-admin:serverConfiguration");
		return mv;
	}

	@RequestMapping(value = "/testConnection", method = RequestMethod.POST)
	public ModelAndView testServerConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("serverConfigurationsForm") ServerConfigurationsForm serverConfigurationsForm, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in testServerConnection()");

		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			ILConnection iLconnection = new ILConnection();
			iLconnection.setServer(serverConfigurationsForm.getIpAddress() + ":" + serverConfigurationsForm.getPortNumber());
			iLconnection.setUsername(serverConfigurationsForm.getUserName());
			iLconnection.setPassword(serverConfigurationsForm.getServerPassword());
			Database db = new Database();
			db.setName(serverConfigurationsForm.getServerName());
			db.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
			iLconnection.setDatabase(db);

			DataResponse dataResponse = restUtilities.postRestObject(request, "/testServerConnection", iLconnection, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		serverConfigurationsForm.setPageMode("edit");
		mv.setViewName("tiles-anvizent-admin:serverConfiguration");
		return mv;
	}

	@RequestMapping(value = { "/save", "/testConnection", "/edit" }, method = RequestMethod.GET)
	public ModelAndView handleServerConfigGetRequests(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/serverConfigurations");
		return mv;
	}

}
