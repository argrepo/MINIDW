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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.DataValidationEnum;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationForm;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/admin/adminDataValidation")
public class DataValidationAdminController {
	protected static final Logger logger = LoggerFactory.getLogger(DataValidationAdminController.class);
	
	@Autowired
	@Qualifier("dataValidationServicesRestTemplateUtilities")
	private RestTemplateUtilities restTemplateUtilities;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	private MessageSource messageSource;
	final String homeRedirectUrl = "redirect:/admin/adminDataValidation";
	
	@RequestMapping(value = "/getBusinessCasesValidation", method = RequestMethod.GET)
	public ModelAndView businessCaseValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, BindingResult result, Locale locale){
		
		  logger.info("in businessCaseValidation()");
		  CommonUtils.setActiveScreenName("addBusinessCaseValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{
			  MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			  map.add("validationTypeId", DataValidationEnum.BUSINESS_CASE.ordinal());
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/getDataValidationInfo", map, user.getUserId());
			  if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.addObject("validationScriptInfo", dataResponse.getObject());
						dataValidationForm.setPageMode("list");
					} else {
						mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
						mv.addObject("validationScriptInfo", dataResponse.getObject());
						mv.addObject("dataValidationTypes", getDataValidationTypes(request,DataValidationEnum.BUSINESS_CASE.ordinal()));
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			 
		  }catch(Throwable t){
			    mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			  logger.error("error : "+t);
		  }
		  mv.setViewName("tiles-anvizent-admin:addBusinessCaseValidation");
		return mv;
	}
	
	@RequestMapping(value="/addBusinessCasesValidation", method = RequestMethod.GET)
	public ModelAndView addBusinessCase(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, BindingResult result, Locale locale){
		  logger.info(" rediredt to addBusinessCase... ");
		  dataValidationForm.setPageMode("addBusinessCases");
		  dataValidationForm.setActive(true);
		  mv.addObject("clientDLInfo", getDLInfoByClientId(request));
		  mv.addObject("contextParameters", getContextParameters(request));
		  mv.addObject("dataValidationTypes", getDataValidationTypes(request,DataValidationEnum.BUSINESS_CASE.ordinal()));
		  mv.setViewName("tiles-anvizent-admin:addBusinessCaseValidation");
		return mv;
	}
	
	@RequestMapping(value="/addBusinessCasesValidation" , method = RequestMethod.POST)
	public ModelAndView addBusinessCaseValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		  logger.info("in addBusinessCaseValidation()");
		  CommonUtils.setActiveScreenName("addBusinessCaseValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{
			  DataValidation dataValidation = new DataValidation();
			   dataValidation.setValidationName(dataValidationForm.getValidationName());
			   dataValidation.setValidationScripts(dataValidationForm.getValidationScripts());
			   dataValidation.setValidationId(DataValidationEnum.BUSINESS_CASE.ordinal());
			   dataValidation.setActive(dataValidationForm.isActive());
			   dataValidation.setValidationTypeId(dataValidationForm.getValidationTypeId());
			   dataValidation.setPreparedStatement(dataValidationForm.isPreparedStatement());
			  if(dataValidationForm.getDlList() != null && dataValidationForm.getDlList().size() > 0){
				  List<DLInfo> dlInfo = new ArrayList<DLInfo>();
				  dataValidationForm.getDlList().forEach(dl -> {
					  dlInfo.add(new DLInfo(Integer.parseInt(dl), null));
				  });
				  dataValidation.setDlInfoList(dlInfo);
			  }
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/createDataValidation", dataValidation, user.getUserId());
			  if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.setViewName(homeRedirectUrl+"/getBusinessCasesValidation");
					}
					else{
						  dataValidationForm.setPageMode("addBusinessCases");
						  dataValidationForm.setActive(true);
						  
						  mv.setViewName(homeRedirectUrl+"/addBusinessCasesValidation");
					}
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
		  }catch(Throwable t){
			  logger.error("error : "+t);
			   mv.addObject("messagecode", "FAILED");
			   mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		  }
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/editBusinessCasesValidation", method = RequestMethod.POST)
	public ModelAndView editBusinessCaseValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		logger.info("in businessCaseValidation()");
		  CommonUtils.setActiveScreenName("addBusinessCaseValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{			 
			  if(dataValidationForm.getScriptId() == null){
				  redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.label.scriptIdShouldnotbeEmpty", null, locale));
					mv.setViewName(homeRedirectUrl+"/getBusinessCasesValidation");
					return mv;
			  }
			  DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getValidationScriptData/{scriptId}", user.getUserId(), dataValidationForm.getScriptId());
			  ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			  if (dataResponse != null && dataResponse.getHasMessages()) {
				  if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					  LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dataResponse.getObject();
						DataValidation dataValidation = mapper.convertValue(dls, new TypeReference<DataValidation>() {
						});
						
						dataValidationForm.setDlList(new ArrayList<String>());
						List<String> selectedDLList = dataValidationForm.getDlList();
						dataValidation.getDlInfoList().forEach(dlinfo -> {
							selectedDLList.add(dlinfo.getdL_id() + "");
						});
						
						dataValidationForm.setValidationName(dataValidation.getValidationName());
						dataValidationForm.setValidationId(dataValidation.getValidationId());
						dataValidationForm.setValidationScripts(dataValidation.getValidationScripts());
						dataValidationForm.setScriptId(dataValidation.getScriptId());
						dataValidationForm.setPreparedStatement(dataValidation.isPreparedStatement());
						dataValidationForm.setDlList(selectedDLList);
						dataValidationForm.setActive(dataValidation.isActive());
						dataValidationForm.setModification(dataValidation.getModification());
						dataValidationForm.setValidationTypeId(dataValidation.getValidationTypeId());
						dataValidationForm.setPageMode("editpreload");
						 dataValidationForm.setPageMode("editBusinessCases");
						 dataValidationForm.setActive(true);
						 mv.addObject("contextParameters", getContextParameters(request));
						 mv.addObject("clientDLInfo", getDLInfoByClientId(request));
						 mv.addObject("dataValidationTypes", getDataValidationTypes(request,DataValidationEnum.BUSINESS_CASE.ordinal()));
						mv.setViewName("tiles-anvizent-admin:addBusinessCaseValidation");
				  }else{
					   logger.info(" data response returned FAILED status ");
						redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					}
			  }else{
				  mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				 mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			  }
			mv.setViewName("tiles-anvizent-admin:addBusinessCaseValidation");
		  }catch(Throwable t){
			  logger.error("error : "+t);
			  mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			  mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		  }
		return mv;
	}
	
	@RequestMapping(value="/updateBusinessCasesValidation",method=RequestMethod.POST)
	public ModelAndView updateBusinessCasesValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		  CommonUtils.setActiveScreenName("addBusinessCaseValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
			try{
				DataValidation dataValidation = new DataValidation();
			   dataValidation.setScriptId(dataValidationForm.getScriptId());
			   dataValidation.setValidationName(dataValidationForm.getValidationName());
			   dataValidation.setValidationScripts(dataValidationForm.getValidationScripts());
			   dataValidation.setValidationId(DataValidationEnum.BUSINESS_CASE.ordinal());
			   dataValidation.setValidationTypeId(dataValidationForm.getValidationTypeId());
			   dataValidation.setPreparedStatement(dataValidationForm.isPreparedStatement());
			   dataValidation.setActive(dataValidationForm.isActive());
			  if(dataValidationForm.getDlList() != null && dataValidationForm.getDlList().size() > 0){
				  List<DLInfo> dlInfo = new ArrayList<DLInfo>();
				  dataValidationForm.getDlList().forEach(dl -> {
					  dlInfo.add(new DLInfo(Integer.parseInt(dl), null));
				  });
				  dataValidation.setDlInfoList(dlInfo);
			  }
			  
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/updateValidationScript", dataValidation, user.getUserId());
			  if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						dataValidationForm.setActive(true);
						mv.setViewName(homeRedirectUrl+"/getBusinessCasesValidation");
						redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					} else {
						mv.addObject("messagecode", com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
		}catch(Throwable t){
			logger.error("error while updateBusinessCasesValidation "+t);
			mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getDLInfoByClientId(HttpServletRequest request){
		User user = CommonUtils.getUserDetails(request, null, null);
		Map<Object, Object> dlList = new LinkedHashMap<>();
		try{
			DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getClientMappingDLInfo",	user.getUserId());
			List<DLInfo> dlInfoList = new ArrayList<>();
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				 List<LinkedHashMap<String, Object>> verticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				 ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				 dlInfoList = mapper.convertValue(verticalResponse, new TypeReference<List<DLInfo>>() {});
				 if (dlInfoList == null || dlInfoList.size() == 0) {
						return new HashMap<Object, Object>();
					}
					for(DLInfo dl: dlInfoList){
						dlList.put(dl.getdL_id(), dl.getdL_name());
					}
			}
		}catch(Throwable t){
			logger.error("error while getDLInfo: "+t);
		}
		return dlList;
	}
	
	@RequestMapping(value = "/getPreloadValidations", method = RequestMethod.GET)
	public ModelAndView preLoadValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, BindingResult result, Locale locale){
		
		  logger.info("in preLoadValidation()");
		  CommonUtils.setActiveScreenName("addPreloadValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{
			  MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			  map.add("validationTypeId", DataValidationEnum.PRE_LOAD.ordinal());
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/getDataValidationInfo", map, user.getUserId());
			  if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.addObject("validationScriptInfo", dataResponse.getObject());
						dataValidationForm.setPageMode("list");
					} else {
						mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
						mv.addObject("validationScriptInfo", dataResponse.getObject());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			 
		  }catch(Throwable t){
			    mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			  logger.error("error : "+t);
		  }
		  mv.setViewName("tiles-anvizent-admin:addPreloadValidation");
		return mv;
	}
	
	
	
	@RequestMapping(value = "/addPreload", method = RequestMethod.GET)
	public ModelAndView addPreload(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm,
			BindingResult result, Locale locale) {
			logger.info(" rediredt to addBusinessCase... ");
			dataValidationForm.setPageMode("addpreload");
			dataValidationForm.setActive(true);
			mv.addObject("clientILInfo", getILInfoByClientId(request));
			mv.addObject("connectorsInfo", getConnectorsByClientId(request));
			mv.addObject("contextParameters", getContextParameters(request));
			mv.addObject("dataValidationTypes", getDataValidationTypes(request,DataValidationEnum.PRE_LOAD.ordinal()));
			mv.setViewName("tiles-anvizent-admin:addPreloadValidation");
		return mv;
	}
	
	@RequestMapping(value="/addPreloadValidation" , method = RequestMethod.POST)
	public ModelAndView addPreloadValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		  logger.info("in addPreloadValidation()");
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{
			  DataValidation dataValidation = new DataValidation();
			   dataValidation.setValidationName(dataValidationForm.getValidationName());
			   dataValidation.setValidationScripts(dataValidationForm.getValidationScripts());
			   dataValidation.setValidationId(DataValidationEnum.PRE_LOAD.ordinal());
			   dataValidation.setActive(dataValidationForm.isActive());
			   dataValidation.setDatabaseConnectorId(dataValidationForm.getDatabaseConnectorId());
			   dataValidation.setDatabaseConnectorName(dataValidationForm.getDatabaseConnectorName());
			   dataValidation.setValidationTypeId(dataValidationForm.getValidationTypeId());
			  if(dataValidationForm.getIlId() != null && dataValidationForm.getIlId() > 0){
				  ILInfo ilInfo = new ILInfo();
				  ilInfo.setiL_id(dataValidationForm.getIlId());
				  dataValidation.setIlInfo(ilInfo);
			  }
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/createDataValidation", dataValidation, user.getUserId());
			  if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.setViewName(homeRedirectUrl+"/getPreloadValidations");
					}
					else{
						  dataValidationForm.setPageMode("addpreload");
						  dataValidationForm.setActive(true);
						  mv.setViewName(homeRedirectUrl+"/addPreload");
					}
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
		  }catch(Throwable t){
			  logger.error("error : "+t);
			   mv.addObject("messagecode", "FAILED");
			   mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		  }
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getILInfoByClientId(HttpServletRequest request){
		User user = CommonUtils.getUserDetails(request, null, null);
		Map<Object, Object> ilList = new LinkedHashMap<>();
		try{
			DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getClientMappingILInfo",	user.getUserId());
			List<ILInfo> ilInfoList = new ArrayList<>();
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				 List<LinkedHashMap<String, Object>> verticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				 ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				 ilInfoList = mapper.convertValue(verticalResponse, new TypeReference<List<ILInfo>>() {});
				 if (ilInfoList == null || ilInfoList.size() == 0) {
						return new HashMap<Object, Object>();
					}
					for(ILInfo il: ilInfoList){
						ilList.put(il.getiL_id(), il.getiL_name());
					}
			}
		}catch(Throwable t){
			logger.error("error while getDLInfo: "+t);
		}
		return ilList;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getConnectorsByClientId(HttpServletRequest request){
		User user = CommonUtils.getUserDetails(request, null, null);
		Map<Object, Object> connectors = new LinkedHashMap<Object, Object>();
		try{
			DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getDatabasesTypes", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<LinkedHashMap<String, Object>> obj = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
				List<Database> databasesList = mapper.convertValue(obj, new TypeReference<List<Database>>() {});
				 if (databasesList == null || databasesList.size() == 0) {
						return new HashMap<Object, Object>();
					}
					for(Database db: databasesList){
						connectors.put(db.getId(), db.getName());
					}
			}
		}catch(Throwable t){
			logger.error("error while getDLInfo: "+t);
		}
		return connectors;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/editPreloadValidation", method = RequestMethod.POST)
	public ModelAndView editPreloadValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		logger.info("in editPreloadValidation()");
		  CommonUtils.setActiveScreenName("addPreloadValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		  try{			 
			  if(dataValidationForm.getScriptId() == null){
				  redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.label.scriptIdShouldnotbeEmpty", null, locale));
					mv.setViewName(homeRedirectUrl+"/getPreloadValidations");
					return mv;
			  }
			  DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getValidationScriptData/{scriptId}", user.getUserId(), dataValidationForm.getScriptId());
			  ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			  if (dataResponse != null && dataResponse.getHasMessages()) {
				  if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					  LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dataResponse.getObject();
						DataValidation dataValidation = mapper.convertValue(dls, new TypeReference<DataValidation>() {
						});
						
						dataValidationForm.setDlList(new ArrayList<String>());
						List<String> selectedDLList = dataValidationForm.getDlList();
						dataValidation.getDlInfoList().forEach(dlinfo -> {
							selectedDLList.add(dlinfo.getdL_id() + "");
						});
						
						dataValidationForm.setValidationName(dataValidation.getValidationName());
						dataValidationForm.setValidationId(dataValidation.getValidationId());
						dataValidationForm.setValidationScripts(dataValidation.getValidationScripts());
						dataValidationForm.setScriptId(dataValidation.getScriptId());
						dataValidationForm.setPreparedStatement(dataValidation.isPreparedStatement());
						dataValidationForm.setDlList(selectedDLList);
						dataValidationForm.setActive(dataValidation.isActive());
						dataValidationForm.setModification(dataValidation.getModification());
						dataValidationForm.setIlId(dataValidation.getIlId());
						dataValidationForm.setDatabaseConnectorId(dataValidation.getDatabaseConnectorId());
						dataValidationForm.setValidationTypeId(dataValidation.getValidationTypeId());
						dataValidationForm.setPageMode("editpreload");
						dataValidationForm.setActive(true);
						 mv.addObject("clientILInfo", getILInfoByClientId(request));
						 mv.addObject("connectorsInfo", getConnectorsByClientId(request));
						 mv.addObject("contextParameters", getContextParameters(request));
						 mv.addObject("dataValidationTypes", getDataValidationTypes(request,DataValidationEnum.PRE_LOAD.ordinal()));
						mv.setViewName("tiles-anvizent-admin:addPreloadValidation");
				  }else{
					   logger.info(" data response returned FAILED status ");
						redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					}
			  }else{
				  mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				 mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			  }
			mv.setViewName("tiles-anvizent-admin:addPreloadValidation");
		  }catch(Throwable t){
			  logger.error("error : "+t);
			  mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			  mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		  }
		return mv;
	}
	
	@RequestMapping(value="/updatePreloadValidation",method=RequestMethod.POST)
	public ModelAndView updatePreloadValidation(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dataValidationForm") DataValidationForm dataValidationForm, RedirectAttributes redirectAttributes, BindingResult result, Locale locale){
		  CommonUtils.setActiveScreenName("addPreloadValidation", session);
		  User user = CommonUtils.getUserDetails(request, null, null);
		try{
			 DataValidation dataValidation = new DataValidation();
			   dataValidation.setScriptId(dataValidationForm.getScriptId());
			   dataValidation.setValidationName(dataValidationForm.getValidationName());
			   dataValidation.setValidationScripts(dataValidationForm.getValidationScripts());
			   dataValidation.setValidationId(DataValidationEnum.PRE_LOAD.ordinal());
			   dataValidation.setActive(dataValidationForm.isActive());
			   dataValidation.setDatabaseConnectorId(dataValidationForm.getDatabaseConnectorId());
			   dataValidation.setDatabaseConnectorName(dataValidationForm.getDatabaseConnectorName());
			   dataValidation.setValidationTypeId(dataValidationForm.getValidationTypeId());
			   if(dataValidationForm.getIlId() != null && dataValidationForm.getIlId() > 0){
					  ILInfo ilInfo = new ILInfo();
					  ilInfo.setiL_id(dataValidationForm.getIlId());
					  dataValidation.setIlInfo(ilInfo);
				  }
			  
			  DataResponse dataResponse = restTemplateUtilities.postRestObject(request, "/updateValidationScript", dataValidation, user.getUserId());
			  if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						dataValidationForm.setActive(true);
						mv.setViewName(homeRedirectUrl+"/getPreloadValidations");
						redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					} else {
						mv.addObject("messagecode", com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
		}catch(Throwable t){
			logger.error("error while updateBusinessCasesValidation "+t);
			mv.addObject("messagecode",  com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getContextParameters(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getContextParameters", user.getUserId());
		List<ContextParameter> contextParams = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> verticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			contextParams = mapper.convertValue(verticalResponse, new TypeReference<List<ContextParameter>>() {
			});

		} else {
			contextParams = new ArrayList<>();
		}

		Map<Object, Object> contextParamsMap = new LinkedHashMap<>();
		for (ContextParameter contextParam : contextParams) {
			contextParamsMap.put(contextParam.getParamId(), contextParam.getParamName());
		}

		return contextParamsMap;
	}
	
	public Map<Object, Object> getDataValidationTypes(HttpServletRequest request,Integer validationId){
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restTemplateUtilities.getRestObject(request, "/getDataValidationTypesByvalidationId/{id}", user.getUserId(),validationId);
		List<DataValidationType> dataValidationTypes = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, Object>> response = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			dataValidationTypes = mapper.convertValue(response, new TypeReference<List<DataValidationType>>() {
			});

		} else {
			dataValidationTypes =  new ArrayList<>();
		}
		Map<Object, Object> dataValidationTypeMap = new LinkedHashMap<>();
		for (DataValidationType validation : dataValidationTypes) {
			dataValidationTypeMap.put(validation.getValidationTypeId(), validation.getValidationTypeName());
		}
		return dataValidationTypeMap;
	}
	

	@RequestMapping(value = "/getDataValidationType", method = RequestMethod.GET)
	public ModelAndView createDataValidationType(HttpServletRequest request, HttpSession session,
			HttpServletResponse response, ModelAndView mv, Locale locale) {
		 mv.setViewName("tiles-anvizent-admin:dataValidationType");
		return mv;
	}

}
