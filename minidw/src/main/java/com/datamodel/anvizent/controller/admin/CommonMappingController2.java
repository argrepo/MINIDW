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
import org.springframework.beans.factory.annotation.Value;
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
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/commonMappings2")
public class CommonMappingController2 {

	protected static final Log LOGGER = LogFactory.getLog(CommonMappingController2.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	private @Value("${service.contextPath:}") String contextPath;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/{client_id}", method = RequestMethod.GET)
	public ModelAndView getDefaultTemplatesInfo(@PathVariable("client_id") int client_id, @RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.info("in getDefaultTemplatesInfo()");
		try {
			defaultMappingInfo.setClientId(client_id);
			mv.addObject("defaultTemplates", getAllDefaultTemplatesInfo(String.valueOf(client_id),request));
			mv.addObject("name", name);
			mv.addObject("client_id", client_id);
			mv.addObject("urlVar","commonMappings2");
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
		mv.addObject("urlVar","commonMappings2");
		mv.addObject("defaultTemplates", getAllDefaultTemplatesInfo(String.valueOf(client_id),request));
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			int templateId = defaultMappingInfo.getTemplateId();
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("templateId", templateId);
			map.add("mappingType", "connector");
			map.add("client_id", -1);
			String url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			RestTemplate restTemplate = new RestTemplate();
		 	DataResponse   dataResponseConnector = restTemplate.postForObject(url, map, DataResponse.class);
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
			map1.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseVertical = restTemplate.postForObject(url, map1, DataResponse.class);
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
			map2.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseDls = restTemplate.postForObject(url, map2, DataResponse.class);
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
			map3.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseTableScripts = restTemplate.postForObject(url, map3, DataResponse.class);
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
			map4.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseWebService = restTemplate.postForObject(url, map4, DataResponse.class);
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
			map5.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseCurrencies = restTemplate.postForObject(url, map5, DataResponse.class);
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
			map6.add("client_id", -1);
			url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseS3Bucket = restTemplate.postForObject(url, map6, DataResponse.class);
			if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages()) {
				if (dataResponseS3Bucket.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("bucketId", dataResponseS3Bucket.getObject());
					if (Integer.parseInt(dataResponseS3Bucket.getObject().toString()) > 0) {
						LinkedMultiValueMap<String, Object> s3IdMap = new LinkedMultiValueMap<String, Object>();
						s3IdMap.add("id", dataResponseS3Bucket.getObject());
						s3IdMap.add("client_id", -1);
						url = contextPath+"/clientMigration/getS3BucketInfoById";
						 DataResponse   dataResponseS3Info = restTemplate.postForObject(url, s3IdMap, DataResponse.class);
						if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages() && dataResponseS3Info.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("s3bucketinfo", dataResponseS3Info.getObject());
						}
						
					}
					
					
				}
			}
			
			url = contextPath+"/clientMigration/getAllmappingInfoById";
			MultiValueMap<String, Object> map7 = new LinkedMultiValueMap<>();
			map7.add("client_id", client_id);
			DataResponse   dataResponse = restTemplate.postForObject(url, map7, DataResponse.class);
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
	
	
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveDefault/{tempId}/{client_id}", method = RequestMethod.GET)
	public String saveDefaultMappingInfoByTemplate(@PathVariable("client_id") int client_id ,@PathVariable("tempId") String tempId,HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.info("in getDefaultMappingInfoByTemplate()");
	//	mv.addObject("name", name);
		mv.addObject("client_id", client_id);
		mv.addObject("urlVar","commonMappings2");
		mv.addObject("defaultTemplates", getAllDefaultTemplatesInfo(String.valueOf(client_id),request));
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			int templateId = Integer.parseInt(tempId);;
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("templateId", templateId);
			map.add("mappingType", "connector");
			map.add("client_id", -1);
			String url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			RestTemplate restTemplate = new RestTemplate();
		 	DataResponse   dataResponseConnector = restTemplate.postForObject(url, map, DataResponse.class);
			if (dataResponseConnector != null && dataResponseConnector.getHasMessages()) {
				if (dataResponseConnector.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> connectorsList = (List<LinkedHashMap<String, Object>>) dataResponseConnector.getObject();
					List<Database> connectorsObjList = mapper.convertValue(connectorsList, new TypeReference<List<Database>>() {
					});
					MultiValueMap<String, Object> connectorsMap = new LinkedMultiValueMap<>();
					connectorsMap.add("client_Id", client_id);
					
					if (connectorsObjList != null && connectorsObjList.size() > 0) {
						for (Database con : connectorsObjList) {
							if (con.getIsDefault()) {
								connectorsMap.add("connectorId",con.getConnector_id());
							}
						}
					}
					
					url = "http://localhost:8080/minidwwebservice/clientMigration/saveClientConnectorMapping";
					restTemplate.postForObject(url, connectorsMap , DataResponse.class);
					
					
				}
			}

			MultiValueMap<String, Object> map1 = new LinkedMultiValueMap<>();
			map1.add("templateId", templateId);
			map1.add("mappingType", "vertical");
			map1.add("client_id", -1);
			 url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseVertical = restTemplate.postForObject(url, map1, DataResponse.class);
			if (dataResponseVertical != null && dataResponseVertical.getHasMessages()) {
				if (dataResponseVertical.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> verticalsList = (List<LinkedHashMap<String, Object>>) dataResponseVertical.getObject();
					List<Industry> verticalsObjList = mapper.convertValue(verticalsList, new TypeReference<List<Industry>>() {
					});
	
					ClientData clientData = new ClientData();
					clientData.setUserId(String.valueOf(client_id));
					List<Industry> verticals = new ArrayList<>();
					if (verticalsObjList != null && verticalsObjList.size() > 0) {
						for (Industry i : verticalsObjList) {
							if (i.getIsDefault()) {
								verticals.add(i);
							}
						}
					}
					clientData.setIndustries(verticals);
					url = "http://localhost:8080/minidwwebservice/clientMigration/saveClientVerticalMapping";
					restTemplate.postForObject(url, clientData , DataResponse.class);
				}
			}

			MultiValueMap<String, Object> map2 = new LinkedMultiValueMap<>();
			map2.add("templateId", templateId);
			map2.add("mappingType", "dl");
			map2.add("client_id", -1);
			 url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseDls = restTemplate.postForObject(url, map2, DataResponse.class);
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
					ETLAdmin etlAdmin =new ETLAdmin();
					etlAdmin.setdLInfo(dls);
					etlAdmin.setClientId(String.valueOf(client_id));
				url = "http://localhost:8080/minidwwebservice/clientMigration/saveDlClientidMapping";
				restTemplate.postForObject(url, etlAdmin , DataResponse.class);
				}
				
					
			}

			MultiValueMap<String, Object> map3 = new LinkedMultiValueMap<>();
			map3.add("templateId", templateId);
			map3.add("mappingType", "tablescript");
			map3.add("client_id", -1);
			 url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseTableScripts = restTemplate.postForObject(url, map3, DataResponse.class);
			if (dataResponseTableScripts != null && dataResponseTableScripts.getHasMessages()) {
				if (dataResponseTableScripts.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> tblScriptsList = (List<LinkedHashMap<String, Object>>) dataResponseTableScripts.getObject();
					List<TableScriptsForm> tblscriptsObjList = mapper.convertValue(tblScriptsList, new TypeReference<List<TableScriptsForm>>() {
					});
					 List<TableScripts> tableScriptList =new ArrayList<>();
					if (tblscriptsObjList != null && tblscriptsObjList.size() > 0) {
						for (TableScriptsForm tblScript : tblscriptsObjList) {
							if (tblScript.getIsDefault()) {
								TableScripts tableScripts =new TableScripts();
								tableScripts.setId(tblScript.getId());
								tableScripts.setCheckedScript(true);
								tableScriptList.add(tableScripts);
							}
						}
					}
					TableScriptsForm tableScriptsForm = new TableScriptsForm();
					tableScriptsForm.setClientId(client_id);
					tableScriptsForm.setTableScriptList(tableScriptList);
					url = "http://localhost:8080/minidwwebservice/clientMigration/saveClientTableScriptsMapping";
					restTemplate.postForObject(url, tableScriptsForm , DataResponse.class);
				}
			}

			MultiValueMap<String, Object> map4 = new LinkedMultiValueMap<>();
			map4.add("templateId", templateId);
			map4.add("mappingType", "webservice");
			map4.add("client_id", -1);
			 url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseWebService = restTemplate.postForObject(url, map4, DataResponse.class);
			if (dataResponseWebService != null && dataResponseWebService.getHasMessages()) {
				if (dataResponseWebService.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> wsList = (List<LinkedHashMap<String, Object>>) dataResponseWebService.getObject();
					List<WebServiceTemplateMaster> wsObjList = mapper.convertValue(wsList, new TypeReference<List<WebServiceTemplateMaster>>() {
					});
					MultiValueMap<String, Object> webserviceMap = new LinkedMultiValueMap<>();
					if (wsObjList != null && wsObjList.size() > 0) {
						for (WebServiceTemplateMaster ws : wsObjList) {
							if (ws.getIsDefault()) {
								webserviceMap.add("webServiceIds", ws.getId());
							}
						}
					}
					
					url = "http://localhost:8080/minidwwebservice/clientMigration/saveClientWebserviceMapping";
					webserviceMap.add("client_Id", client_id);
					restTemplate.postForObject(url, webserviceMap , DataResponse.class);
				}
			}
			
			
			
			MultiValueMap<String, Object> map5 = new LinkedMultiValueMap<>();
			map5.add("templateId", templateId);
			map5.add("mappingType", "currencies");
			map5.add("client_id", -1);
			 url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseCurrencies = restTemplate.postForObject(url, map5, DataResponse.class);
			if (dataResponseCurrencies != null && dataResponseCurrencies.getHasMessages()) {
				if (dataResponseCurrencies.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> verticalsList = (List<LinkedHashMap<String, Object>>) dataResponseCurrencies.getObject();
					List<ClientCurrencyMapping> currenciesObjList = mapper.convertValue(verticalsList, new TypeReference<List<ClientCurrencyMapping>>() {
					});
					ClientCurrencyMapping clientCurrencyMapping = null;
					
					if (currenciesObjList != null && currenciesObjList.size() > 0) {
						for (ClientCurrencyMapping i : currenciesObjList) {
							clientCurrencyMapping = i;
						}
						clientCurrencyMapping.setClientId(String.valueOf(client_id));
					}
				//	mv.addObject("currencies", currencies);
					url = "http://localhost:8080/minidwwebservice/clientMigration/createClientCurrencyMapping";
					restTemplate.postForObject(url, clientCurrencyMapping , DataResponse.class);
				}
				
			}
			
			MultiValueMap<String, Object> map6 = new LinkedMultiValueMap<>();
			map6.add("templateId", templateId);
			map6.add("mappingType", "s3Bucket");
			map6.add("client_id", -1);
			url = "http://localhost:8080/minidwwebservice/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseS3Bucket = restTemplate.postForObject(url, map6, DataResponse.class);
			if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages()) {
				if (dataResponseS3Bucket.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("bucketId", dataResponseS3Bucket.getObject());
					if (Integer.parseInt(dataResponseS3Bucket.getObject().toString()) > 0) {
						LinkedMultiValueMap<String, Object> s3IdMap = new LinkedMultiValueMap<String, Object>();
						s3IdMap.add("id", dataResponseS3Bucket.getObject());
						s3IdMap.add("client_id", -1);
						S3BucketInfo bucketInfo =new S3BucketInfo();
						bucketInfo.setClientId(client_id);
						Integer s3Id = (Integer) dataResponseS3Bucket.getObject();
						bucketInfo.setId(s3Id);
						url = "http://localhost:8080/minidwwebservice/clientMigration/saveClientMapping";
						 DataResponse   dataResponseS3Info = restTemplate.postForObject(url, bucketInfo , DataResponse.class);
						if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages() && dataResponseS3Info.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("s3bucketinfo", dataResponseS3Info.getObject());
						}
						
					}
					
					
				}
			}
			defaultMappingInfo.setPageMode("display");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin-plain:defaultMappingInfo");
		return null;
	}*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveDefaultMapping(HttpServletRequest request,  HttpServletResponse response,  ModelAndView mv,
			@ModelAttribute("defaultMappingInfo") DefaultMappingInfoForm defaultMappingInfo,  RedirectAttributes redirectAttributes,  Locale locale) {

		LOGGER.info("in saveDefaultMapping()");
		mv.setViewName("tiles-anvizent-admin-plain:defaultMappingInfo");
		defaultMappingInfo.setPageMode("closeWindow");
		String url = null;
		RestTemplate restTemplate = new RestTemplate();
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
				//	restUtilities.postRestObject(request, "/saveClientVerticalMapping", clientData, user.getUserId());
					url = contextPath+"/clientMigration/saveClientVerticalMapping";
					restTemplate.postForObject(url, clientData , DataResponse.class);
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
						//restUtilities.postRestObject(request, "/saveDlClientidMapping", etlAdmin, user.getUserId());
						url = contextPath+"/clientMigration/saveDlClientidMapping";
						restTemplate.postForObject(url, etlAdmin , DataResponse.class);
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
					//restUtilities.postRestObject(request, "/saveClientConnectorMapping", map, user.getUserId());
					url = contextPath+"/clientMigration/saveClientConnectorMapping";
					restTemplate.postForObject(url, map , DataResponse.class);
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
						//restUtilities.postRestObject(request, "/saveClientTableScriptsMapping", tableScriptsForm, user.getUserId());
						//restUtilities.postRestObject(request, "/clientTableScriptsExecution", tableScriptsForm, user.getUserId());
						
						url = contextPath+"/clientMigration/saveClientTableScriptsMapping";
						restTemplate.postForObject(url, tableScriptsForm , DataResponse.class);
						url = contextPath+"/clientMigration/clientTableScriptsExecution";
						restTemplate.postForObject(url, tableScriptsForm , DataResponse.class);
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
					//restUtilities.postRestObject(request, "/saveClientWebserviceMapping", map, user.getUserId());
					url = contextPath+"/clientMigration/saveClientWebserviceMapping";
					//webserviceMap.add("client_Id", client_id);
					restTemplate.postForObject(url, map , DataResponse.class);
				}
			}

			// client currency mapping
			if (!defaultMappingInfo.isSkipCurrency()) {
				if (defaultMappingInfo.getClientCurrencyMapping() != null && StringUtils.isNotBlank(defaultMappingInfo.getClientCurrencyMapping().getCurrencyType())) {
					defaultMappingInfo.getClientCurrencyMapping().setClientId(defaultMappingInfo.getClientId()+"");
					url = contextPath+"/clientMigration/createClientCurrencyMapping";
					restTemplate.postForObject(url, defaultMappingInfo.getClientCurrencyMapping() , DataResponse.class);
					
					//restUtilities.postRestObject(request, "/createClientCurrencyMapping", defaultMappingInfo.getClientCurrencyMapping(), user.getUserId());
				}
			}
			
			MultiValueMap<String, Object> map6 = new LinkedMultiValueMap<>();
			map6.add("templateId", defaultMappingInfo.getTemplateId());
			map6.add("mappingType", "s3Bucket");
			map6.add("client_id", -1);
			url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			 DataResponse   dataResponseS3Bucket = restTemplate.postForObject(url, map6, DataResponse.class);
			if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages()) {
				if (dataResponseS3Bucket.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("bucketId", dataResponseS3Bucket.getObject());
					if (Integer.parseInt(dataResponseS3Bucket.getObject().toString()) > 0) {
						LinkedMultiValueMap<String, Object> s3IdMap = new LinkedMultiValueMap<String, Object>();
						s3IdMap.add("id", dataResponseS3Bucket.getObject());
						s3IdMap.add("client_id", -1);
						S3BucketInfo bucketInfo =new S3BucketInfo();
						bucketInfo.setClientId(defaultMappingInfo.getClientId());
						Integer s3Id = (Integer) dataResponseS3Bucket.getObject();
						bucketInfo.setId(s3Id);
						url = contextPath+"/clientMigration/saveClientMapping";
						 DataResponse   dataResponseS3Info = restTemplate.postForObject(url, bucketInfo , DataResponse.class);
						if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages() && dataResponseS3Info.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("s3bucketinfo", dataResponseS3Info.getObject());
						}
						
					}
					
					
				}
			}
			MultiValueMap<String, Object> schedularMasterMap = new LinkedMultiValueMap<>();
			schedularMasterMap.add("templateId", defaultMappingInfo.getTemplateId());
			schedularMasterMap.add("mappingType", "schedularMaster");
			schedularMasterMap.add("client_id", -1);
			 url = contextPath+"/clientMigration/getDefaultTemplateMasterMappedData";
			DataResponse   dataResponseschedularMaster= restTemplate.postForObject(url, schedularMasterMap, DataResponse.class);
			if (dataResponseschedularMaster != null && dataResponseschedularMaster.getHasMessages()) {
				if (dataResponseschedularMaster.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<Integer> masterIds = (List<Integer>) dataResponseschedularMaster.getObject();
					MultiValueMap<String, Object> schedularMasterMap1 = new LinkedMultiValueMap<>();
					schedularMasterMap1.add("client_Id", defaultMappingInfo.getClientId());
					for (Integer integer : masterIds) {
						schedularMasterMap1.add("schedularMasterIds", integer);
					}
					
					url = contextPath+"/clientMigration/saveClientSchedularMapping";
					  restTemplate.postForObject(url, schedularMasterMap1 , DataResponse.class);
					 
					 MultiValueMap<String, Object> fileSettingMap = new LinkedMultiValueMap<>();
					 fileSettingMap.add("client_Id", defaultMappingInfo.getClientId());
					 fileSettingMap.add("multipart_file_enabled" ,1 );
						
						
						url = contextPath+"/clientMigration/updateFilesetting";
						  restTemplate.postForObject(url, fileSettingMap , DataResponse.class);
					
				}
			}
			
			// create ddl tables at client db
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", defaultMappingInfo.getClientId());
			//restUtilities.postRestObject(request, "/createDDlTablesAtClientDb",map,user.getUserId());
			

		} catch (Exception e) {
			//redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			//redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
	 
	  return mv;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getAllDefaultTemplatesInfo(String client_id,HttpServletRequest request) {
		Map<Object, Object> templatesInfo = new LinkedHashMap<>();
		
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("client_id",client_id);
		
		String url = contextPath+"/clientMigration/getAllDefaultTemplatesInfoPortal";

	 	DataResponse   dataResponse = restTemplate.postForObject(url, map, DataResponse.class);
	 
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
