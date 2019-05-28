package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.AllMappingInfoForm;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DefaultMappingInfoForm;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/commonMappings")
public class CommonMappingController {

	protected static final Log LOGGER = LogFactory.getLog(CommonMappingController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/{client_id}", method = RequestMethod.GET)
	public ModelAndView getDefaultTemplatesInfo(@PathVariable("client_id") int client_id, @RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.info("in getDefaultTemplatesInfo()");
		try {
			defaultMappingInfo.setClientId(client_id);
			mv.addObject("defaultTemplates", getAllDefaultTemplatesInfo(request));
			mv.addObject("name", name);
			mv.addObject("client_id", client_id);
			mv.addObject("urlVar","commonMappings");
			defaultMappingInfo.setPageMode("display");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin-plain:defaultMappingInfo");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{name}/{client_id}", method = RequestMethod.POST)
	public ModelAndView getDefaultMappingInfoByTemplate(@PathVariable("client_id") int client_id ,@PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.info("in getDefaultMappingInfoByTemplate()");
		mv.addObject("name", name);
		mv.addObject("client_id", client_id);
		mv.addObject("urlVar","commonMappings");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			int templateId = defaultMappingInfo.getTemplateId();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("templateId", templateId);
			map.add("mappingType", "connector");
			DataResponse dataResponseConnector = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map, user.getUserId());
			if (dataResponseConnector != null && dataResponseConnector.getHasMessages()) {
				if (dataResponseConnector.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> connectorsList = (List<LinkedHashMap<String, Object>>) dataResponseConnector.getObject();
					List<Database> connectorsObjList = mapper.convertValue(connectorsList, new TypeReference<List<Database>>() {
					});
					List<Database> connectors = new ArrayList<>();
					if (connectorsObjList != null && connectorsObjList.size() > 0) {
						for (Database con : connectorsObjList) {
							if (con.getIsDefault()) {
								connectors.add(con);
							}
						}
					}
					mv.addObject("connector", connectors);
				}
			}

			MultiValueMap<String, Object> map1 = new LinkedMultiValueMap<>();
			map1.add("templateId", templateId);
			map1.add("mappingType", "vertical");
			DataResponse dataResponseVertical = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map1, user.getUserId());
			if (dataResponseVertical != null && dataResponseVertical.getHasMessages()) {
				if (dataResponseVertical.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> verticalsList = (List<LinkedHashMap<String, Object>>) dataResponseVertical.getObject();
					List<Industry> verticalsObjList = mapper.convertValue(verticalsList, new TypeReference<List<Industry>>() {
					});
					List<Industry> verticals = new ArrayList<>();
					if (verticalsObjList != null && verticalsObjList.size() > 0) {
						for (Industry i : verticalsObjList) {
							if (i.getIsDefault()) {
								verticals.add(i);
							}
						}
					}
					mv.addObject("verticals", verticals);
				}
			}

			MultiValueMap<String, Object> map2 = new LinkedMultiValueMap<>();
			map2.add("templateId", templateId);
			map2.add("mappingType", "dl");
			DataResponse dataResponseDls = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map2, user.getUserId());
			if (dataResponseDls != null && dataResponseDls.getHasMessages()) {
				if (dataResponseDls.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> dlsList = (List<LinkedHashMap<String, Object>>) dataResponseDls.getObject();
					List<DLInfo> dlsObjList = mapper.convertValue(dlsList, new TypeReference<List<DLInfo>>() {
					});
					List<DLInfo> dls = new ArrayList<>();
					if (dlsObjList != null && dlsObjList.size() > 0) {
						for (DLInfo dl : dlsObjList) {
							if (dl.getIsDefault()) {
								dls.add(dl);
							}
						}
					}
					mv.addObject("dlInfo", dls);
				}
			}

			MultiValueMap<String, Object> map3 = new LinkedMultiValueMap<>();
			map3.add("templateId", templateId);
			map3.add("mappingType", "tablescript");
			DataResponse dataResponseTableScripts = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map3, user.getUserId());
			if (dataResponseTableScripts != null && dataResponseTableScripts.getHasMessages()) {
				if (dataResponseTableScripts.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> tblScriptsList = (List<LinkedHashMap<String, Object>>) dataResponseTableScripts.getObject();
					List<TableScriptsForm> tblscriptsObjList = mapper.convertValue(tblScriptsList, new TypeReference<List<TableScriptsForm>>() {
					});
					List<TableScriptsForm> tblScripts = new ArrayList<>();
					if (tblscriptsObjList != null && tblscriptsObjList.size() > 0) {
						for (TableScriptsForm tblScript : tblscriptsObjList) {
							if (tblScript.getIsDefault()) {
								tblScripts.add(tblScript);
							}
						}
					}
					mv.addObject("tableScriptsList", tblScripts);
				}
			}

			MultiValueMap<String, Object> map4 = new LinkedMultiValueMap<>();
			map4.add("templateId", templateId);
			map4.add("mappingType", "webservice");
			DataResponse dataResponseWebService = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map4, user.getUserId());
			if (dataResponseWebService != null && dataResponseWebService.getHasMessages()) {
				if (dataResponseWebService.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> wsList = (List<LinkedHashMap<String, Object>>) dataResponseWebService.getObject();
					List<WebServiceTemplateMaster> wsObjList = mapper.convertValue(wsList, new TypeReference<List<WebServiceTemplateMaster>>() {
					});
					List<WebServiceTemplateMaster> webservices = new ArrayList<>();
					if (wsObjList != null && wsObjList.size() > 0) {
						for (WebServiceTemplateMaster ws : wsObjList) {
							if (ws.getIsDefault()) {
								webservices.add(ws);
							}
						}
					}
					mv.addObject("webServices", webservices);
				}
			}
			
			
			
			MultiValueMap<String, Object> map5 = new LinkedMultiValueMap<>();
			map5.add("templateId", templateId);
			map5.add("mappingType", "currencies");
			DataResponse dataResponseCurrencies = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData", map5, user.getUserId());
			if (dataResponseCurrencies != null && dataResponseCurrencies.getHasMessages()) {
				if (dataResponseCurrencies.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> verticalsList = (List<LinkedHashMap<String, Object>>) dataResponseCurrencies.getObject();
					List<ClientCurrencyMapping> currenciesObjList = mapper.convertValue(verticalsList, new TypeReference<List<ClientCurrencyMapping>>() {
					});
					List<ClientCurrencyMapping> currencies = new ArrayList<>();
					if (currenciesObjList != null && currenciesObjList.size() > 0) {
						for (ClientCurrencyMapping i : currenciesObjList) {
								currencies.add(i);
						}
					}
					mv.addObject("currencies", currencies);
				}
			}
			
			MultiValueMap<String, Object> map6 = new LinkedMultiValueMap<>();
			map6.add("templateId", templateId);
			map6.add("mappingType", "s3Bucket");
			DataResponse dataResponseS3Bucket = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map6, user.getUserId());
			if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages()) {
				if (dataResponseS3Bucket.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("bucketId", dataResponseS3Bucket.getObject());
					if (Integer.parseInt(dataResponseS3Bucket.getObject().toString()) > 0) {
						LinkedMultiValueMap<String, Object> s3IdMap = new LinkedMultiValueMap<String, Object>();
						s3IdMap.add("id", dataResponseS3Bucket.getObject());
						DataResponse dataResponseS3Info = restUtilities.postRestObject(request, "/getS3BucketInfoById", s3IdMap, user.getUserId());
						if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages() && dataResponseS3Info.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("s3bucketinfo", dataResponseS3Info.getObject());
						}
						
					}
					
					
				}
			}
			
			MultiValueMap<String, Object> map7 = new LinkedMultiValueMap<>();
			map7.add("templateId", templateId);
			map7.add("mappingType", "schedularMaster");
			DataResponse dataResponseSchedular = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map7, user.getUserId());
			if (dataResponseSchedular != null && dataResponseSchedular.getHasMessages()) {
				if (dataResponseSchedular.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<Integer> dataResponseSchedularId = (List<Integer>) dataResponseSchedular.getObject();
					SchedulerMaster schedulerMaster = new SchedulerMaster();
					schedulerMaster.setId(dataResponseSchedularId.get(0));
						DataResponse dataResponseschedulerMaster = restUtilities.postRestObject(request, "/getSchedulerServerMasterById", schedulerMaster, user.getUserId());
						if (dataResponseschedulerMaster != null && dataResponseschedulerMaster.getHasMessages() && dataResponseschedulerMaster.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("schedularMaster", dataResponseschedulerMaster.getObject());
						
						
					}
					
					
				}
			}
			
			MultiValueMap<String, Object> map8 = new LinkedMultiValueMap<>();
			DataResponse dataResponsegetFileSettings = restUtilities.getRestObject(request,
					"/getFileSettingsList", user.getUserId());
			mv.addObject("fileSettings", dataResponsegetFileSettings.getObject());
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllmappingInfoById/{client_Id}", user.getUserId(),
					defaultMappingInfo.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					AllMappingInfo allMappingInfo = mapper.convertValue(obj, new TypeReference<AllMappingInfo>() {
					});
					AllMappingInfoForm allMappingInfoForm = new AllMappingInfoForm();
					allMappingInfoForm.setNumberOfVerticals(allMappingInfo.getNumberOfVerticals());
					allMappingInfoForm.setNumberOfTableScripts(allMappingInfo.getNumberOfTableScripts());
					allMappingInfoForm.setNumberOfDatabases(allMappingInfo.getNumberOfDatabases());
					allMappingInfoForm.setNumberOfConnectors(allMappingInfo.getNumberOfConnectors());
					allMappingInfoForm.setNumberOfDLs(allMappingInfo.getNumberOfDLs());
					allMappingInfoForm.setNumberOfWebServices(allMappingInfo.getNumberOfWebServices());
					allMappingInfoForm.setNumberOfCurrencies(allMappingInfo.getNumberOfCurrencies());
					allMappingInfoForm.setNumberOfS3BuckeMappings(allMappingInfo.getNumberOfS3BuckeMappings());
					allMappingInfoForm.setNumberOfSchedulers(allMappingInfo.getNumberOfSchedulers());
					defaultMappingInfo.setAllMappingInfoForm(allMappingInfoForm);
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			defaultMappingInfo.setPageMode("display");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin-plain:defaultMappingInfo");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveDefaultMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo, RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.info("in saveDefaultMapping()");
		mv.setViewName("tiles-anvizent-admin-plain:defaultMappingInfo");
		defaultMappingInfo.setPageMode("closeWindow");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			// client vertical mapping
			if (!defaultMappingInfo.getSkipVerticals()) {
				if (defaultMappingInfo.getVerticals() != null && defaultMappingInfo.getVerticals().size() > 0) {
					ClientData clientData = new ClientData();
					clientData.setUserId(defaultMappingInfo.getClientId().toString());
					List<Industry> industries = new ArrayList<>();
					defaultMappingInfo.getVerticals().forEach(verticalId -> {
						industries.add(new Industry(Integer.parseInt(verticalId), null));
					});
					clientData.setIndustries(industries);
					restUtilities.postRestObject(request, "/saveClientVerticalMapping", clientData, user.getUserId());
				}
			}

			// client DL mapping
			if (!defaultMappingInfo.getSkipDLs()) {
				if (defaultMappingInfo.getdLInfo() != null && defaultMappingInfo.getdLInfo().size() > 0) {
					List<DLInfo> dlInfoList = defaultMappingInfo.getdLInfo();
					List<DLInfo> dLInfoModifiedList = new ArrayList<>();
					if (dlInfoList != null) {
						for (DLInfo dl : dlInfoList) {
							if (dl.getIsDefault() && dl.getdL_id() != 0) {
								dLInfoModifiedList.add(dl);
							}
						}
					}
					ETLAdmin etlAdmin = new ETLAdmin();
					etlAdmin.setClientId(defaultMappingInfo.getClientId().toString());
					etlAdmin.setdLInfo(dLInfoModifiedList);
					if (dLInfoModifiedList != null && dLInfoModifiedList.size() > 0) {
						restUtilities.postRestObject(request, "/saveDlClientidMapping", etlAdmin, user.getUserId());
					}
				}
			}

			// client Connector Mapping
			if (!defaultMappingInfo.getSkipConnectors()) {
				if (defaultMappingInfo.getConnectors() != null && defaultMappingInfo.getConnectors().size() > 0) {
					MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
					map.add("client_Id", defaultMappingInfo.getClientId());
					defaultMappingInfo.getConnectors().forEach(connectorId -> {
						map.add("connectorId", connectorId);
					});
					restUtilities.postRestObject(request, "/saveClientConnectorMapping", map, user.getUserId());
				}
			}

			// client table script Mapping
			if (!defaultMappingInfo.getSkipTableScripts()) {
				if (defaultMappingInfo.getTableScripts() != null && defaultMappingInfo.getTableScripts().size() > 0) {
					TableScriptsForm tableScriptsForm = new TableScriptsForm();
					tableScriptsForm.setClientId(defaultMappingInfo.getClientId());
					List<TableScripts> tableScriptsList = defaultMappingInfo.getTableScripts();
					List<TableScripts> tableScriptsModifiedList = new ArrayList<>();
					if (tableScriptsList != null) {
						for (TableScripts tblscript : tableScriptsList) {
							if (tblscript.getIsDefault() && tblscript.getId() != 0) {
								tableScriptsModifiedList.add(tblscript);
							}
						}
					}
					tableScriptsForm.setTableScriptList(tableScriptsModifiedList);
					if (tableScriptsModifiedList != null && tableScriptsModifiedList.size() > 0) {
						restUtilities.postRestObject(request, "/saveClientTableScriptsMapping", tableScriptsForm, user.getUserId());
						restUtilities.postRestObject(request, "/clientTableScriptsExecution", tableScriptsForm, user.getUserId());
					}
				}
			}

			// client webservice mapping
			if (!defaultMappingInfo.getSkipWebServices()) {
				if (defaultMappingInfo.getWebServices() != null && defaultMappingInfo.getWebServices().size() > 0) {
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					map.add("client_Id", defaultMappingInfo.getClientId());
					for (String wsId : defaultMappingInfo.getWebServices()) {
						map.add("webServiceIds", wsId);
					}
					restUtilities.postRestObject(request, "/saveClientWebserviceMapping", map, user.getUserId());
				}
			}
			

			// client currency mapping
			if (!defaultMappingInfo.isSkipCurrency()) {
				if (defaultMappingInfo.getClientCurrencyMapping() != null && StringUtils.isNotBlank(defaultMappingInfo.getClientCurrencyMapping().getCurrencyType())) {
					defaultMappingInfo.getClientCurrencyMapping().setClientId(defaultMappingInfo.getClientId()+"");
					restUtilities.postRestObject(request, "/createClientCurrencyMapping", defaultMappingInfo.getClientCurrencyMapping(), user.getUserId());
				}
			}

			// client scheduler mapping
			if(!defaultMappingInfo.isSkipSchedulers()) {
				if(defaultMappingInfo.getSchedulerId() != null) {
					
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					map.add("client_Id", defaultMappingInfo.getClientId());
					map.add("schedularMasterIds", defaultMappingInfo.getSchedulerId());
					restUtilities.postRestObject(request, "/saveClientSchedularMapping", map, user.getUserId());
					
				}
							
			}
			
			//S3 Bucket Mapping
			if(!defaultMappingInfo.isSkipS3Bucket()) {
				if(defaultMappingInfo.getBucketId() != null) {
					S3BucketInfo s3Bucket = new S3BucketInfo();
					s3Bucket.setId(Integer.parseInt(defaultMappingInfo.getBucketId()));
					s3Bucket.setClientId(defaultMappingInfo.getClientId());
					
					restUtilities.postRestObject(request, "/saveClientMapping", s3Bucket , user.getUserId());
					
				}
				
				
			}
			
			//Update FileSettings
			if(defaultMappingInfo.getFileSettingId() != null) {
				
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", defaultMappingInfo.getClientId());
			map.add("multipart_file_enabled", defaultMappingInfo.getFileSettingId());
			restUtilities.postRestObject(request, "/updateFilesetting", map, user.getUserId());
			
			}
			// create ddl tables at client db
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", defaultMappingInfo.getClientId());
			restUtilities.postRestObject(request, "/createDDlTablesAtClientDb",map,user.getUserId());
			

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
	
	  return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("defaultTemplates")
	public Map<Object, Object> getAllDefaultTemplatesInfo(HttpServletRequest request) {
		Map<Object, Object> templatesInfo = new LinkedHashMap<>();
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllDefaultTemplatesInfo", user.getUserId());
		
		if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> templateResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<DefaultTemplates> defaultTemplates = mapper.convertValue(templateResponse, new TypeReference<List<DefaultTemplates>>() {
			});
			if (defaultTemplates != null && defaultTemplates.size() > 0) {
				for (DefaultTemplates defaultTemplate : defaultTemplates) {
					if (defaultTemplate.isActive()) {
						templatesInfo.put(defaultTemplate.getTemplateId(), defaultTemplate.getTemplateName());
					}
				}
			}
		} else {
			return new HashMap<>();
		}
		return templatesInfo;
	}
	
}
