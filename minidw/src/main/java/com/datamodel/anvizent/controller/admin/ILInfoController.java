
package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ETLJobContextParam;
import com.datamodel.anvizent.service.model.EltJobTagInfo;
import com.datamodel.anvizent.service.model.EltLoadParameters;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IlInfoForm;
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
@RequestMapping(value = "/admin/iLInfo")
public class ILInfoController {
	protected static final Log LOGGER = LogFactory.getLog(ILInfoController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;
	
	@Autowired
	@Qualifier("eltRestTemplateUtilities")
	private RestTemplateUtilities eltRestUtilities;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getExistingILsInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("ilInfoForm") IlInfoForm ilInfoForm, HttpSession session, Locale locale) {
		LOGGER.debug("in getExistingILsInfo()");
		CommonUtils.setActiveScreenName("iLInfo", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingILsInfo", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("iLInfoList", dataResponse.getObject());
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
		ilInfoForm.setPageMode("iLsList");
		mv.setViewName("tiles-anvizent-admin:iLInfo");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getILInfoById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("ilInfoForm") IlInfoForm ilInfoForm, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getILInfoById()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ilInfoForm.setPageMode("iLsList");
			mv.setViewName("redirect:/admin/iLInfo");

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("iLId", ilInfoForm.getiLId());
			DataResponse dataResponse = restUtilities.postRestObject(request, "/getILInfoById", map, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					ETLAdmin etlAdmin = mapper.convertValue(data, new TypeReference<ETLAdmin>() {
					});
					ilInfoForm.setiLName(etlAdmin.getIlInfo().getiL_name());
					ilInfoForm.setVersion(etlAdmin.getIlInfo().getVersion());
					ilInfoForm.setiLType(etlAdmin.getIlInfo().getiLType());
					ilInfoForm.setiLDescription(etlAdmin.getIlInfo().getDescription());
					ilInfoForm.setiLTableName(etlAdmin.getIlInfo().getiL_table_name());
					ilInfoForm.setXrefILTableName(etlAdmin.getIlInfo().getXref_il_table_name());
					ilInfoForm.setiLPurgeScript(etlAdmin.getIlInfo().getPurgeScript());
					ilInfoForm.setJobName(etlAdmin.getIlInfo().getJobName());
					ilInfoForm.setUploadedJobFileNames(etlAdmin.getIlInfo().getJobFileNames());
					ilInfoForm.setJobFileNames(etlAdmin.getFileNames());
					ilInfoForm.setActive(etlAdmin.getIlInfo().getIsActive());
					ilInfoForm.setContextParamsList(new ArrayList<>());
					
					ilInfoForm.setJobExecutionType(etlAdmin.getIlInfo().getJobExecutionType());
					ilInfoForm.setJobTagId(etlAdmin.getIlInfo().getJobTagId());
					ilInfoForm.setLoadParameterId(etlAdmin.getIlInfo().getLoadParameterId());
					ilInfoForm.setMasterParameterId(etlAdmin.getIlInfo().getMasterParameterId());
					
					List<String> selectedContextParams = ilInfoForm.getContextParamsList();
					if (etlAdmin.geteTLJobContextParamList() != null) {
						etlAdmin.geteTLJobContextParamList().forEach(contextParam -> {
							selectedContextParams.add(contextParam.getParamId() + "");
						});	
					}
					String type = "il";
					mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, type));
					ilInfoForm.setPageMode("editIL");
					mv.setViewName("tiles-anvizent-admin:iLInfo");
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

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateILDetailsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("ilInfoForm") IlInfoForm ilInfoForm, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getILInfoById()");
		User user = CommonUtils.getUserDetails(request, null, null);
		Boolean isUploaded = false;
		mv.setViewName("tiles-anvizent-admin:iLInfo");
		ilInfoForm.setPageMode("editIL");
		try {
			MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
			if (ilInfoForm.getJobFile() != null) {
				if (ilInfoForm.getJobFile().size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : ilInfoForm.getJobFile()) {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						filesMap.add("files", new FileSystemResource(tempFileName));
					}
					DataResponse uploadedFileResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", filesMap, user.getUserId());
					if (uploadedFileResponse.getHasMessages() && uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
					}
				}
			} else {
				isUploaded = true;
			}

			if (isUploaded) {
				ETLAdmin eTLAdmin = new ETLAdmin();
				ILInfo iLInfo = new ILInfo();
				iLInfo.setiL_id(ilInfoForm.getiLId());
				iLInfo.setiL_name(ilInfoForm.getiLName());
				iLInfo.setiL_table_name(ilInfoForm.getiLTableName());
				iLInfo.setXref_il_table_name(ilInfoForm.getXrefILTableName());
				iLInfo.setDescription(ilInfoForm.getiLDescription());
				iLInfo.setJobName(ilInfoForm.getJobName());
				iLInfo.setPurgeScript(ilInfoForm.getiLPurgeScript());
				iLInfo.setiLType(ilInfoForm.getiLType());
				iLInfo.setIsActive(ilInfoForm.isActive());
				iLInfo.setJobFileNames(ilInfoForm.getUploadedJobFileNames());
				iLInfo.setVersion(ilInfoForm.getVersion());
				iLInfo.setJobExecutionType(ilInfoForm.getJobExecutionType());
				iLInfo.setJobTagId(ilInfoForm.getJobTagId());
				iLInfo.setLoadParameterId(ilInfoForm.getLoadParameterId());
				iLInfo.setMasterParameterId(ilInfoForm.getMasterParameterId());
				eTLAdmin.setIlInfo(iLInfo);
				List<ETLJobContextParam> contextParams = new ArrayList<>();
				if (ilInfoForm.getContextParamsList() != null) {
					for (String paramId : ilInfoForm.getContextParamsList()) {
						ETLJobContextParam contextParam = new ETLJobContextParam();
						contextParam.setParamId(Integer.parseInt(paramId));
						contextParams.add(contextParam);
					}
				}
				eTLAdmin.seteTLJobContextParamList(contextParams);

				DataResponse dataResponse = restUtilities.postRestObject(request, "/updateILDetailsById", eTLAdmin, user.getUserId());
				if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					mv.setViewName("redirect:/admin/iLInfo");
					ilInfoForm.setPageMode("iLsList");
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
				}
			} else {
				mv.addObject("messagecode", "ERROR");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "ERROR");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
			mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
		}
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addNewIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @ModelAttribute("ilInfoForm") IlInfoForm ilInfoForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in addNewIL()");
		String type = "il";
		mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, type));
		ilInfoForm.setPageMode("createIL");
		ilInfoForm.setActive(true);
		mv.setViewName("tiles-anvizent-admin:iLInfo");
		return mv;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createNewIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("ilInfoForm") IlInfoForm ilInfoForm, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in createNewIL()");
		User user = CommonUtils.getUserDetails(request, null, null);
		Boolean isUploaded = false;
		mv.setViewName("tiles-anvizent-admin:iLInfo");
		ilInfoForm.setPageMode("createIL");
		try {
			MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
			if (ilInfoForm.getJobFile() != null) {
				if (ilInfoForm.getJobFile().size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : ilInfoForm.getJobFile()) {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						filesMap.add("files", new FileSystemResource(tempFileName));
					}
					DataResponse uploadedFileResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", filesMap, user.getUserId());
					if (uploadedFileResponse.getHasMessages() && uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
					}
				}
			} else {
				isUploaded = true;
			}
			if (isUploaded) {

				ETLAdmin eTLAdmin = new ETLAdmin();
				ILInfo iLInfo = new ILInfo();
				iLInfo.setiL_name(ilInfoForm.getiLName());
				iLInfo.setiL_table_name(ilInfoForm.getiLTableName());
				iLInfo.setXref_il_table_name(ilInfoForm.getXrefILTableName());
				iLInfo.setDescription(ilInfoForm.getiLDescription());
				iLInfo.setJobName(ilInfoForm.getJobName());
				iLInfo.setPurgeScript(ilInfoForm.getiLPurgeScript());
				iLInfo.setiLType(ilInfoForm.getiLType());
				iLInfo.setIsActive(ilInfoForm.isActive());
				iLInfo.setJobFileNames(ilInfoForm.getUploadedJobFileNames());
				iLInfo.setVersion(ilInfoForm.getVersion());
				iLInfo.setJobExecutionType(ilInfoForm.getJobExecutionType());
				iLInfo.setJobTagId(ilInfoForm.getJobTagId());
				iLInfo.setLoadParameterId(ilInfoForm.getLoadParameterId());
				iLInfo.setMasterParameterId(ilInfoForm.getMasterParameterId());
				eTLAdmin.setIlInfo(iLInfo);
				List<ETLJobContextParam> contextParams = new ArrayList<>();
				if(ilInfoForm.getContextParamsList() != null){
					for (String paramId : ilInfoForm.getContextParamsList()) {
						ETLJobContextParam contextParam = new ETLJobContextParam();
						contextParam.setParamId(Integer.parseInt(paramId));
						contextParams.add(contextParam);
					}
					eTLAdmin.seteTLJobContextParamList(contextParams);
				}
				

				DataResponse dataResponse = restUtilities.postRestObject(request, "/createNewIL", eTLAdmin, user.getUserId());
				if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					mv.setViewName("redirect:/admin/iLInfo");
					ilInfoForm.setPageMode("iLsList");
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
				}
			} else {
				mv.addObject("messagecode", "ERROR");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "ERROR");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, "il"));
			e.printStackTrace();
		}
		return mv;
	}

	@RequestMapping(value = { "/update", "/create", "/edit" }, method = RequestMethod.GET)
	public ModelAndView allrediections(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		mv.setViewName("redirect:/admin/iLInfo");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getContextParameters")
	public Map<Object, Object> getContextParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		Map<Object, Object> contextParamsMap = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getContextParameters", user.getUserId());
		List<ContextParameter> contextParams = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> verticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			contextParams = mapper.convertValue(verticalResponse, new TypeReference<List<ContextParameter>>() {
			});
			for (ContextParameter contextParam : contextParams) {
				contextParamsMap.put(contextParam.getParamId(), contextParam.getParamName());
			}
		} else {
			return new LinkedHashMap<>();
		}
		return contextParamsMap;
	}
	
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("getJobTags")
	public Map<Object, Object> getJobTags(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		Map<Object, Object> map = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = eltRestUtilities.getRestObject(request, "/eltJobTag", user.getUserId());
		List<EltJobTagInfo> contextParams = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> eltResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			contextParams = mapper.convertValue(eltResponse, new TypeReference<List<EltJobTagInfo>>() {
			});
			for (EltJobTagInfo elt : contextParams) {
				map.put(elt.getTagId(), elt.getTagName());
			}
		} else {
			return new LinkedHashMap<>();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("getLoadParameters")
	public Map<Object, Object> getLoadParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		Map<Object, Object> map = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = eltRestUtilities.getRestObject(request, "/getLoadParametersList", user.getUserId());
		List<EltLoadParameters> contextParams = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> eltResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			contextParams = mapper.convertValue(eltResponse, new TypeReference<List<EltLoadParameters>>() {
			});
			for (EltLoadParameters elt : contextParams) {
				map.put(elt.getId(), elt.getName());
			}
		} else {
			return new LinkedHashMap<>();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("getMasterConfiguration")
	public Map<Object, Object> getMasterConfiguration(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		Map<Object, Object> map = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = eltRestUtilities.getRestObject(request, "/masterConfig", user.getUserId());
		List<EltMasterConfiguration> masterConfigs = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> eltResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			masterConfigs = mapper.convertValue(eltResponse, new TypeReference<List<EltMasterConfiguration>>() {
			});
			for (EltMasterConfiguration master : masterConfigs) {
				map.put(master.getId(), master.getName());
			}
		} else {
			return new LinkedHashMap<>();
		}
		return map;
	}
}
