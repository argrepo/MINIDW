package com.datamodel.anvizent.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author mahender.aleveni
 *
 */
@Controller
@RequestMapping(value = "/admin/clientTableScripts")
public class TableScriptsController {
	protected static final Log LOGGER = LogFactory.getLog(TableScriptsController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView clientTableScripts(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("tableScripts") TableScriptsForm tableScripts, BindingResult result, Locale locale) {
		LOGGER.debug("in clientTableScripts()");
		CommonUtils.setActiveScreenName("clientTableScripts", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getTableScripts", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("tableScriptsList", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("tableScriptsList", dataResponse.getObject());
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

		tableScripts.setPageMode("list");
		mv.setViewName("tiles-anvizent-admin:clientTableScripts");
		return mv;

	}

	@RequestMapping(value = { "/edit", "/add", "/addForm", "/update" }, method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/clientTableScripts");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScripts") TableScriptsForm tableScripts, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getTableScriptsMappingById", tableScripts, user.getUserId());

			mv.setViewName("redirect:/admin/clientTableScripts");

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					TableScriptsForm tableScript = mapper.convertValue(map, new TypeReference<TableScriptsForm>() {
					});

					tableScripts.setIs_Active(tableScript.getIs_Active());
					tableScripts.setScriptName(tableScript.getScriptName());
					tableScripts.setSqlScript(tableScript.getSqlScript());
					tableScripts.setSchemaName(tableScript.getSchemaName());
					tableScripts.setClientId(tableScript.getClientId());
					tableScripts.setScriptTypeName(tableScript.getScriptTypeName());
					tableScripts.setScriptDescription(tableScript.getScriptDescription());
					tableScripts.setVersion(tableScript.getVersion());
					mv.setViewName("tiles-anvizent-admin:clientTableScripts");
					tableScripts.setPageMode("edit");
				} else {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScripts") TableScriptsForm tableScripts, RedirectAttributes redirectAttributes, BindingResult result) {
		LOGGER.debug("in clientTableScripts Add ()");
		mv.setViewName("tiles-anvizent-admin:clientTableScripts");
		tableScripts.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/addForm", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsAddFrom(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScripts") TableScriptsForm tableScripts, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in clientTableScripts Add ()");
		User user = CommonUtils.getUserDetails(request, null, null);
		mv.setViewName("tiles-anvizent-admin:clientTableScripts");
		try {

			String scriptType = tableScripts.getScriptTypeName();
			if (!scriptType.equals("Client Specific")) {
				tableScripts.setScriptTypeName(scriptType);
				tableScripts.setClientId(0);
			} else {
				tableScripts.setScriptTypeName(scriptType);
				tableScripts.setClientId(tableScripts.getClientId());
			}

			DataResponse dataResponse = restUtilities.postRestObject(request, "/addTableScriptsMapping", tableScripts, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {

				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.setViewName("redirect:/admin/clientTableScripts");
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
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
	public ModelAndView clientTableScriptsUpdate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScripts") TableScriptsForm tableScripts, RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {

		LOGGER.debug("in clientTableScripts update ()");
		mv.setViewName("tiles-anvizent-admin:clientTableScripts");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			String scriptType = tableScripts.getScriptTypeName();
			if (!scriptType.equals("Client Specific")) {
				tableScripts.setScriptTypeName(scriptType);
				tableScripts.setClientId(0);
			} else {
				tableScripts.setScriptTypeName(scriptType);
				tableScripts.setClientId(tableScripts.getClientId());
			}

			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateTableScriptsMapping", tableScripts, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {

				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.setViewName("redirect:/admin/clientTableScripts");
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
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

	@ModelAttribute("getTargetSchemaList")
	public Map<Object, Object> getTargetSchemaList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		Map<Object, Object> getTargetSchemaMap = new LinkedHashMap<>();

		getTargetSchemaMap.put("select", "select");
		getTargetSchemaMap.put("Main", "Main");
		getTargetSchemaMap.put("Staging", "Staging");

		return getTargetSchemaMap;
	}

	@ModelAttribute("getScriptTypeList")
	public Map<Object, Object> getScriptTypeList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		Map<Object, Object> getScriptTypeList = new LinkedHashMap<>();

		//getScriptTypeList.put("select", "select");
		getScriptTypeList.put("Default", "Default");
		//getScriptTypeList.put("Client Specific", "Client Specific");

		return getScriptTypeList;
	}

	@RequestMapping(value = "/mapping", method = RequestMethod.GET)
	public ModelAndView clientTableScriptsMapping(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result) {
		LOGGER.debug("in clientTableScripts()");
		CommonUtils.setActiveScreenName("clientTableScriptsmapping", session);
		mv.setViewName("tiles-anvizent-admin:clientTableScriptsMapping");
		return mv;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mapping", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsMappingList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, Locale locale) {
		LOGGER.debug("in clientTableScriptsMappingList()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getTableScriptsByClient", tableScriptsForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				List<LinkedHashMap<String, Object>> script = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<TableScripts> tableScriptsList = mapper.convertValue(script, new TypeReference<List<TableScripts>>() {
				});

				DataResponse object1 = restUtilities.postRestObject(request, "/getTableScriptsMappingByClient", tableScriptsForm, user.getUserId());

				List<TableScripts> tableScriptsList1 = null;
				if (object1 != null && object1.getHasMessages() && object1.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> script1 = (List<LinkedHashMap<String, Object>>) object1.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					tableScriptsList1 = mapper1.convertValue(script1, new TypeReference<List<TableScripts>>() {
					});
				} else {
					tableScriptsList1 = new ArrayList<>();
				}

				Iterator<TableScripts> iter = (Iterator<TableScripts>) tableScriptsList.iterator();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
				while (iter.hasNext()) {
					TableScripts tb = iter.next();
					for (TableScripts tableScript1 : tableScriptsList1) {
						if (tb.getId() == tableScript1.getId()) {
							tb.setCheckedScript(tableScript1.isCheckedScript());
							tb.setPriority(tableScript1.getPriority());
							tb.setExecuted(tableScript1.isExecuted());
							tb.setError(tableScript1.isError());
							Modification modification = new Modification();
							if (tableScript1.getModified_Date() != null) {
								modification.setModifiedDateTime(sdf.parse(tableScript1.getModified_Date()));
							}
							tb.setModification(modification);
						}

					}
				}

				tableScriptsForm.setTableScriptList(tableScriptsList);

				tableScriptsForm.setPageMode("list");

			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientTableScriptsMapping");
		return mv;
	}

	@RequestMapping(value = "/mapping/save", method = RequestMethod.GET)
	public ModelAndView clientTableScriptsMappingSaveRedirect(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
		mv.setViewName("redirect:/admin/clientTableScripts/mapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mapping/save", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsMappingSave(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in clientTableScriptsMappingSave()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			if (tableScriptsForm.getTableScriptList() != null) {

				DataResponse dataResponse = restUtilities.postRestObject(request, "/saveClientTableScriptsMapping", tableScriptsForm, user.getUserId());

				if (dataResponse.getHasMessages()) {

					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

						DataResponse dataResponseExecution = restUtilities.postRestObject(request, "/clientTableScriptsExecution", tableScriptsForm,
								user.getUserId());

						if (dataResponseExecution != null && dataResponseExecution.getHasMessages()) {

							if (dataResponseExecution.getMessages().get(0).getCode().equals("SUCCESS")) {
								mv.addObject("messagecode", "SUCCESS");
								mv.addObject("errors", dataResponseExecution.getMessages().get(0).getText());
							} else {
								mv.addObject("messagecode", "FAILED");
								mv.addObject("errors", dataResponseExecution.getMessages().get(0).getText());
							}

						} else {
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", "Unable to execute table scripts");
						}

						DataResponse object = restUtilities.postRestObject(request, "/getTableScriptsByClient", tableScriptsForm, user.getUserId());
						List<TableScripts> tableScriptsList = null;
						if (object != null && object.getHasMessages() && object.getMessages().get(0).getCode().equals("SUCCESS")) {
							List<LinkedHashMap<String, Object>> script = (List<LinkedHashMap<String, Object>>) object.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							tableScriptsList = mapper.convertValue(script, new TypeReference<List<TableScripts>>() {
							});
						} else {
							tableScriptsList = new ArrayList<>();
						}

						DataResponse object1 = restUtilities.postRestObject(request, "/getTableScriptsMappingByClient", tableScriptsForm, user.getUserId());
						List<TableScripts> tableScriptsList1 = null;
						if (object1 != null && object1.getHasMessages() && object1.getMessages().get(0).getCode().equals("SUCCESS")) {
							List<LinkedHashMap<String, Object>> script1 = (List<LinkedHashMap<String, Object>>) object1.getObject();
							ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							tableScriptsList1 = mapper1.convertValue(script1, new TypeReference<List<TableScripts>>() {
							});
						} else {
							tableScriptsList1 = new ArrayList<>();
						}

						Iterator<TableScripts> iter = (Iterator<TableScripts>) tableScriptsList.iterator();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
						while (iter.hasNext()) {
							TableScripts tb = iter.next();
							for (TableScripts tableScript1 : tableScriptsList1) {
								if (tb.getId() == tableScript1.getId()) {
									tb.setCheckedScript(tableScript1.isCheckedScript());
									tb.setPriority(tableScript1.getPriority());
									tb.setExecuted(tableScript1.isExecuted());
									tb.setError(tableScript1.isError());
									Modification modification = new Modification();
									if (tableScript1.getModified_Date() != null) {
										modification.setModifiedDateTime(sdf.parse(tableScript1.getModified_Date()));
									}
									tb.setModification(modification);
								}

							}
						}

						tableScriptsForm.setTableScriptList(tableScriptsList);

						tableScriptsForm.setPageMode("list");

					} else {

						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
						tableScriptsForm.setClientId(0);

					}

				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors",
							messageSource.getMessage("anvizent.package.label.youarenotmappedanyscriptpleaseaddtablescriptforyourclient", null, locale));
					tableScriptsForm.setClientId(0);
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.youarenotmappedanyscriptpleaseaddtablescriptforyourclient", null, locale));
				tableScriptsForm.setClientId(0);
			}

			mv.setViewName("tiles-anvizent-admin:clientTableScriptsMapping");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
			tableScriptsForm.setClientId(0);
		}

		return mv;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ModelAttribute("getClientList")
	public Map<Object, Object> getClientList(HttpServletRequest request) {

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

	@RequestMapping(value = "/execution", method = RequestMethod.GET)
	public ModelAndView clientTableScriptExecution(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result) {

		LOGGER.debug("in clientTableScriptExecution()");

		mv.setViewName("tiles-anvizent-admin:clientTableScriptExecution");

		return mv;

	}

	@RequestMapping(value = "/mapped/listByClient", method = RequestMethod.GET)
	public ModelAndView clientTableScriptsMappedListByClientGet(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result) {

		LOGGER.debug("in clientTableScriptExecution()");

		mv.setViewName("redirect:/admin/clientTableScripts/execution");

		return mv;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/mapped/listByClient", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsMappedListByClient(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, Locale locale) {
		LOGGER.debug("in clientTableScriptsMappingListForExecute()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getTableScriptsMappingByClient", tableScriptsForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				List<LinkedHashMap<String, Object>> script1 = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<TableScripts> tableScriptsList = mapper1.convertValue(script1, new TypeReference<List<TableScripts>>() {
				});
				tableScriptsForm.setTableScriptList(tableScriptsList);
			}
			tableScriptsForm.setPageMode("list");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientTableScriptExecution");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/execution", method = RequestMethod.POST)
	public ModelAndView clientTableScriptsExecution(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.debug("in clientTableScriptsExecution()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			restUtilities.postRestObject(request, "/clientTableScriptsExecution", tableScriptsForm, user.getUserId());

			DataResponse object1 = restUtilities.postRestObject(request, "/getTableScriptsMappingByClient", tableScriptsForm, user.getUserId());
			List<LinkedHashMap<String, Object>> script1 = (List<LinkedHashMap<String, Object>>) object1.getObject();
			ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<TableScripts> tableScriptsList = mapper1.convertValue(script1, new TypeReference<List<TableScripts>>() {
			});

			tableScriptsForm.setTableScriptList(tableScriptsList);

			tableScriptsForm.setPageMode("list");

			mv.setViewName("tiles-anvizent-admin:clientTableScriptExecution");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
			tableScriptsForm.setClientId(0);
		}

		return mv;
	}

	@RequestMapping(value = "/instantExecution", method = RequestMethod.GET)
	public ModelAndView instantExecution(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result) {
		LOGGER.debug("in instantExecution()");
		CommonUtils.setActiveScreenName("instantExecution", session);
		mv.setViewName("tiles-anvizent-admin:clientTableScriptInstantExecution");
		return mv;

	}

	@RequestMapping(value = "/instantExecution", method = RequestMethod.POST)
	public ModelAndView instantExecution(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.debug("in clientTableScriptsExecution()");

		User user = CommonUtils.getUserDetails(request, null, null);
		mv.setViewName("tiles-anvizent-admin:clientTableScriptInstantExecution");
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/clientTableScriptsInstantExecution", tableScriptsForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.setViewName("redirect:/admin/clientTableScripts/instantExecution");
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					Integer clientId = tableScriptsForm.getClientId();
					tableScriptsForm = new TableScriptsForm();
					tableScriptsForm.setClientId(clientId);
					redirectAttributes.addFlashAttribute("tableScriptsForm", tableScriptsForm);
				} else {
					mv.addObject("messagecode", "FAILED");
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

	@RequestMapping(value = "/instantExecution/previous", method = RequestMethod.POST)
	public ModelAndView previousExecutedScripts(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("tableScriptsForm") TableScriptsForm tableScriptsForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.debug("in previousExecutedScripts()");
		User user = CommonUtils.getUserDetails(request, null, null);
		redirectAttributes.addFlashAttribute(tableScriptsForm);
		mv.setViewName("redirect:/admin/clientTableScripts/instantExecution");
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getPreviousExecutedScripts", tableScriptsForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}

				redirectAttributes.addFlashAttribute("prevoiusExcutetedScriptList", dataResponse.getObject());
				redirectAttributes.addFlashAttribute("displayPrevious", "yes");

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

}
