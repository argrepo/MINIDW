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
import org.springframework.core.io.FileSystemResource;
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
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DashboardLayoutForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ETLJobContextParam;
import com.datamodel.anvizent.service.model.EltJobTagInfo;
import com.datamodel.anvizent.service.model.EltLoadParameters;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Kpi;
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
@RequestMapping(value = "/admin/dashBoardLayoutMaster")
public class DashboardLayoutController {
	protected static final Log LOGGER = LogFactory.getLog(DashboardLayoutController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("eltRestTemplateUtilities")
	private RestTemplateUtilities eltRestUtilities;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView dashBoardLayoutMaster(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, BindingResult result, Locale locale) {
		LOGGER.debug("in dashBoardLayoutMaster()");
		CommonUtils.setActiveScreenName("dashBoardLayoutMaster", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getDlInfo", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("dlInfo", dataResponse.getObject());
					dashboardLayoutForm.setPageMode("list");
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("dlInfo", dataResponse.getObject());
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

		mv.setViewName("tiles-anvizent-admin:dashBoardLayoutMaster");

		return mv;
	}

	@RequestMapping(value = { "/edit", "/update" }, method = RequestMethod.GET)
	public ModelAndView dashBoardLayout(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView dashBoardLayoutEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in dashBoardLayoutMaster Edit ()");

		if (dashboardLayoutForm.getDlId() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.label.dliShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/dashBoardLayoutMaster");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);

		redirectAttributes.addFlashAttribute("messagecode", "FAILED");
		redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster");

		try {

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getDLData/{dlId}", user.getUserId(), dashboardLayoutForm.getDlId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

					LinkedHashMap<String, Object> ils = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ETLAdmin etlAdmin = mapper.convertValue(ils, new TypeReference<ETLAdmin>() {
					});

					dashboardLayoutForm.setIlList(new ArrayList<>());
					List<String> selectedIlList = dashboardLayoutForm.getIlList();
					dashboardLayoutForm.setKpiList(new ArrayList<>());
					List<String> selectedKpiList = dashboardLayoutForm.getKpiList();
					dashboardLayoutForm.setContextParamList(new ArrayList<>());
					List<String> selectedContextParamList = dashboardLayoutForm.getContextParamList();

					etlAdmin.getiLInfo().forEach(il -> {
						selectedIlList.add(il.getiL_id() + "");
					});

					etlAdmin.getKpiInfo().forEach(kpi -> {
						selectedKpiList.add(kpi.getKpiId() + "");
					});

					dashboardLayoutForm.setDlName(etlAdmin.getdLInfo().get(0).getdL_name());
					dashboardLayoutForm.setDlTableName(etlAdmin.getdLInfo().get(0).getdL_table_name());
					dashboardLayoutForm.setDlDescription(etlAdmin.getdLInfo().get(0).getDescription());
					dashboardLayoutForm.setActive(etlAdmin.getdLInfo().get(0).isIs_Active());
					dashboardLayoutForm.setJobName(etlAdmin.getdLInfo().get(0).getJobName());
					dashboardLayoutForm.setVerticalName(etlAdmin.getdLInfo().get(0).getIndustry().getId());
					dashboardLayoutForm.setExistingFileNames(etlAdmin.getFileNames());
					dashboardLayoutForm.setJobfileNames(etlAdmin.getDlInfo().getJobFileNames());
					dashboardLayoutForm.setVersion(etlAdmin.getdLInfo().get(0).getVersion());
					dashboardLayoutForm.setJobExecutionType(etlAdmin.getdLInfo().get(0).getJobExecutionType());
					dashboardLayoutForm.setJobTagId(etlAdmin.getdLInfo().get(0).getJobTagId());
					dashboardLayoutForm.setLoadParameterId(etlAdmin.getdLInfo().get(0).getLoadParameterId());
					dashboardLayoutForm.setMasterParameterId(etlAdmin.getdLInfo().get(0).getMasterParameterId());
					
					if(dashboardLayoutForm.getJobExecutionType().equals("T")){
						etlAdmin.geteTLJobContextParamList().forEach(contextParam -> {
							selectedContextParamList.add(contextParam.getParamId() + "");
						});
						String type = "dl";
						mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, type));	
					}
					
					dashboardLayoutForm.setPageMode("edit");
					mv.setViewName("tiles-anvizent-admin:dashBoardLayoutMaster");
				} else {
					LOGGER.info(" data response returned FAILED status ");
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				LOGGER.info(" data response is empty");
			}
		} catch (Exception e) {
			LOGGER.info("Exception occured while retrieving DL info");
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView dashBoardLayoutAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, RedirectAttributes redirectAttributes, BindingResult result) {
		LOGGER.debug("in dashBoardLayoutMaster Add ()");
		String type = "dl";
		mv.addObject("jarFilesList", CommonUtils.getAvailableJarsList(request, restUtilities, type));
		dashboardLayoutForm.setPageMode("add");
		dashboardLayoutForm.setActive(true);
		mv.setViewName("tiles-anvizent-admin:dashBoardLayoutMaster");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView dashBoardLayoutInsert(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in dashBoardLayoutMaster Add ()");

		/* to handle failure cases */
		mv.setViewName("tiles-anvizent-admin:dashBoardLayoutMaster");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ETLAdmin etlAdmin = new ETLAdmin();

			DLInfo dLInfo = new DLInfo();
			dLInfo.setdL_name(dashboardLayoutForm.getDlName());
			dLInfo.setdL_table_name(dashboardLayoutForm.getDlTableName());
			dLInfo.setDescription(dashboardLayoutForm.getDlDescription());
			dLInfo.setJobName(dashboardLayoutForm.getJobName());
			dLInfo.setIs_Active(dashboardLayoutForm.isActive());
			dLInfo.setJobFileNames(dashboardLayoutForm.getJobfileNames());
			dLInfo.setVersion(dashboardLayoutForm.getVersion());
			dLInfo.setJobExecutionType(dashboardLayoutForm.getJobExecutionType());
			dLInfo.setJobTagId(dashboardLayoutForm.getJobTagId());
			dLInfo.setLoadParameterId(dashboardLayoutForm.getLoadParameterId());
			dLInfo.setMasterParameterId(dashboardLayoutForm.getMasterParameterId());
			Industry industry = new Industry();
			industry.setId(dashboardLayoutForm.getVerticalName());
			dLInfo.setIndustry(industry);

			etlAdmin.setDlInfo(dLInfo);
			if (dashboardLayoutForm.getKpiList() != null && dashboardLayoutForm.getKpiList().size() > 0) {
				List<Kpi> kpiList = new ArrayList<>();
				dashboardLayoutForm.getKpiList().forEach(kpi -> {
					kpiList.add(new Kpi(Integer.parseInt(kpi), null));
				});
				etlAdmin.setKpiInfo(kpiList);
			}

			if (dashboardLayoutForm.getIlList() != null && dashboardLayoutForm.getIlList().size() > 0) {
				List<ILInfo> ilList = new ArrayList<>();
				dashboardLayoutForm.getIlList().forEach(ilId -> {
					ilList.add(new ILInfo(Integer.parseInt(ilId), null));
				});
				etlAdmin.setiLInfo(ilList);
			}
			if (dashboardLayoutForm.getContextParamList() != null && dashboardLayoutForm.getContextParamList().size() > 0) {
				List<ETLJobContextParam> contextParamList = new ArrayList<>();
				dashboardLayoutForm.getContextParamList().forEach(contextParam -> {
					contextParamList.add(new ETLJobContextParam(Integer.parseInt(contextParam), null));
				});
				etlAdmin.seteTLJobContextParamList(contextParamList);
			}

			List<FileSystemResource> multiPartFiles = new ArrayList<>();
			if (dashboardLayoutForm.getJarFiles() != null && dashboardLayoutForm.getJarFiles().size() > 0) {
				String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());

				dashboardLayoutForm.getJarFiles().forEach(file -> {
					try {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						multiPartFiles.add(new FileSystemResource(tempFileName));
					} catch (Exception e) {
						System.out.println("error while " + e.getMessage());
					}
				});
			}

			boolean isUploaded = false;
			if (multiPartFiles.size() > 0) {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				multiPartFiles.forEach(multiPartFile -> {
					map.add("files", multiPartFile);
				});

				DataResponse dataResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", map, user.getUserId());
				if (dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					isUploaded = true;
				}

			} else {
				isUploaded = true;
			}

			if (isUploaded) {

				DataResponse dataResponse = restUtilities.postRestObject(request, "/createDlMaster", etlAdmin, user.getUserId());
				if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.setViewName("redirect:/admin/dashBoardLayoutMaster");
					}
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}

			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.message.label.failedToUploadJarFiles", null, locale));

			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView dashBoardLayoutUpdate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("dashboardLayoutForm") DashboardLayoutForm dashboardLayoutForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in dashBoardLayoutUpdate update ()");

		/* to handle failure cases */
		dashboardLayoutForm.setPageMode("edit");
		mv.setViewName("tiles-anvizent-admin:dashBoardLayoutMaster");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ETLAdmin etlAdmin = new ETLAdmin();

			DLInfo dLInfo = new DLInfo();
			dLInfo.setdL_id(dashboardLayoutForm.getDlId());
			dLInfo.setdL_name(dashboardLayoutForm.getDlName());
			dLInfo.setdL_table_name(dashboardLayoutForm.getDlTableName());
			dLInfo.setDescription(dashboardLayoutForm.getDlDescription());
			dLInfo.setJobName(dashboardLayoutForm.getJobName());
			dLInfo.setIs_Active(dashboardLayoutForm.isActive());
			dLInfo.setJobFileNames(dashboardLayoutForm.getJobfileNames());
			dLInfo.setVersion(dashboardLayoutForm.getVersion());
			dLInfo.setJobExecutionType(dashboardLayoutForm.getJobExecutionType());
			dLInfo.setJobTagId(dashboardLayoutForm.getJobTagId());
			dLInfo.setLoadParameterId(dashboardLayoutForm.getLoadParameterId());
			dLInfo.setMasterParameterId(dashboardLayoutForm.getMasterParameterId());
			
			
			Industry industry = new Industry();
			industry.setId(dashboardLayoutForm.getVerticalName());
			dLInfo.setIndustry(industry);

			etlAdmin.setDlInfo(dLInfo);

			if (dashboardLayoutForm.getKpiList() != null && dashboardLayoutForm.getKpiList().size() > 0) {
				List<Kpi> kpiList = new ArrayList<>();
				dashboardLayoutForm.getKpiList().forEach(kpi -> {
					kpiList.add(new Kpi(Integer.parseInt(kpi), null));
				});
				etlAdmin.setKpiInfo(kpiList);
			}

			if (dashboardLayoutForm.getIlList() != null && dashboardLayoutForm.getIlList().size() > 0) {
				List<ILInfo> ilList = new ArrayList<>();
				dashboardLayoutForm.getIlList().forEach(ilId -> {
					ilList.add(new ILInfo(Integer.parseInt(ilId), null));
				});
				etlAdmin.setiLInfo(ilList);
			}

			if (dashboardLayoutForm.getContextParamList() != null && dashboardLayoutForm.getContextParamList().size() > 0) {
				List<ETLJobContextParam> contextParamList = new ArrayList<>();
				dashboardLayoutForm.getContextParamList().forEach(contextParam -> {
					contextParamList.add(new ETLJobContextParam(Integer.parseInt(contextParam), null));
				});
				etlAdmin.seteTLJobContextParamList(contextParamList);
			}

			List<FileSystemResource> multiPartFiles = new ArrayList<>();
			if (dashboardLayoutForm.getJarFiles() != null && dashboardLayoutForm.getJarFiles().size() > 0) {
				String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());

				dashboardLayoutForm.getJarFiles().forEach(file -> {
					try {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						multiPartFiles.add(new FileSystemResource(tempFileName));
					} catch (Exception e) {
						System.out.println("error while " + e.getMessage());
					}
				});

			}

			boolean isUploaded = false;
			if (multiPartFiles.size() > 0) {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				multiPartFiles.forEach(multiPartFile -> {
					map.add("files", multiPartFile);
				});

				DataResponse dataResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", map, user.getUserId());
				if (dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					isUploaded = true;
				}

			} else {
				isUploaded = true;
			}

			if (isUploaded) {

				DataResponse dataResponse = restUtilities.postRestObject(request, "/updateDlMaster", etlAdmin, user.getUserId());
				if (dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.setViewName("redirect:/admin/dashBoardLayoutMaster");
						redirectAttributes.addFlashAttribute("messagecode", "SUCCESS");
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}

			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.message.label.failedToUploadJarFiles", null, locale));

			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getExistingVerticals")
	public Map<Object, Object> getExistingVerticals(HttpServletRequest request) {
		List<Industry> vertical = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingVerticals", user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> verticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			vertical = mapper.convertValue(verticalResponse, new TypeReference<List<Industry>>() {
			});
		} else {
			return new HashMap<>();
		}

		Map<Object, Object> varticalList = new LinkedHashMap<>();
		for (Industry industry : vertical) {
			if (industry.getIsActive())
				varticalList.put(industry.getId(), industry.getName());
		}
		return varticalList;

	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getAllKpis")
	public Map<Object, Object> getAllKpis(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllKpis", user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			return (Map<Object, Object>) dataResponse.getObject();
		} else {
			return new HashMap<>();
		}
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("fetchAllIlInfo")
	public Map<Object, Object> getAllIlInfo(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingILsInfo", user.getUserId());
		List<LinkedHashMap<String, Object>> ils = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ils = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
		}

		if (ils == null || ils.size() == 0) {
			return new HashMap<Object, Object>();
		}
		Map<Object, Object> ilList = new LinkedHashMap<>();
		ils.forEach(map -> {
			ilList.put(map.get("iL_id").toString(), map.get("iL_name").toString());
		});

		return ilList;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getContextParameters")
	public Map<Object, Object> getContextParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getContextParameters", user.getUserId());
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
