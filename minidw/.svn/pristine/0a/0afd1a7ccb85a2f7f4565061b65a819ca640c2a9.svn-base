package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.CustomPackageForm;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.ScheduleResult;
import com.datamodel.anvizent.service.model.StandardPackageForm;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.datamodel.anvizent.validator.CustomPackageFormValidator;
import com.datamodel.anvizent.validator.StandardPackageFormValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/package")
public class PackageController { 

	protected static final Log LOGGER = LogFactory.getLog(PackageController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@Autowired
	StandardPackageFormValidator spValidator;
	@Autowired
	CustomPackageFormValidator cpValidator;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;
 
	
	@Autowired
	private ServletContext servletContext;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/standardpackage_old", method = RequestMethod.GET)
	public ModelAndView standardPackage(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Package> userPackageList = null;
		try {
			DataResponse packageListDataResponse = null;
			CommonUtils.setActiveScreenName("standardpackage", session);
			packageListDataResponse = restUtilities.getRestObject(request, "/getUserStandardPackages", user.getUserId());
			if (packageListDataResponse != null && packageListDataResponse.getHasMessages()) {
				if (packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					userPackageList = (List<Package>) packageListDataResponse.getObject();
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageListDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("isStandard", true);
			mv.addObject("userPackageList", userPackageList);
			mv.setViewName("tiles-anvizent-entry:package");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);;
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/custompackage", method = RequestMethod.GET)
	public ModelAndView customPackage(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Package> userPackageList = null;
		try {
			DataResponse packageListDataResponse = null;
			CommonUtils.setActiveScreenName("custompackage", session);
			packageListDataResponse = restUtilities.getRestObject(request, "/getUserCustomPackages", user.getUserId());
			if (packageListDataResponse != null && packageListDataResponse.getHasMessages()) {
				if (packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					userPackageList = (List<Package>) packageListDataResponse.getObject();
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageListDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("isStandard", false);
			mv.addObject("userPackageList", userPackageList);
			mv.setViewName("tiles-anvizent-entry:package");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jobResults/{packageId}", method = RequestMethod.GET)
	public ModelAndView viewResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("packageId") String packageId,
			@RequestParam(value = "source", required = false) String fromSource, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {


		User user = CommonUtils.getUserDetails(request, null, null);

		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages()) {
				if (userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				} else {
					redirectAttributes.addFlashAttribute("messagecode", userDataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", "Package details not found");
			}
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				jobResultForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				jobResultForm.setPackageName(userPackage.getPackageName());

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getJobResults/{" + packageId + "}", user.getUserId(), packageId);
			mv.addObject("viewResultList", dataResponse.getObject());
			if (StringUtils.isNotBlank(fromSource)) {
				jobResultForm.setPageMode(fromSource);
			}
			mv.setViewName("tiles-anvizent-entry:viewResults");

		} else {
			mv.setViewName("redirect:/adt/standardpackage"); 
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jobResults/{packageId}", method = RequestMethod.POST)
	public ModelAndView searchJobResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {


		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String packageId = jobResultForm.getPackageId();
			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getJobResultsByDate/{" + packageId + "}/{" + fromDate + "}/{" + toDate + "}",
					user.getUserId(), packageId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}

	/**
	 * 
	 * @param standardPackage
	 * @return
	 */
	@RequestMapping(value = "/standardPackage/new_old", method = RequestMethod.GET)
	public ModelAndView standardPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result) {

		standardPackageForm.setIndustryId("0");
		standardPackageForm.setIsStandard(true);
		mv.setViewName("tiles-anvizent-entry:standardPackage");

		return mv;
	}

	/**
	 * 
	 * @param standardPackageForm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/standardPackage/new_old", params = { "createStandardPackage" }, method = RequestMethod.POST)
	public ModelAndView createStandardPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("standardPackageForm") @Valid StandardPackageForm standardPackageForm, BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		standardPackageForm.setLocale(locale);
		spValidator.validate(standardPackageForm, result);
		try {
			if (result.hasErrors()) {
				mv.setViewName("tiles-anvizent-entry:standardPackage");
				return mv;
			}
			Package standardPackage = new Package();
			if (standardPackageForm.getIndustryId() != null) {
				standardPackage.setIndustry(new Industry(Integer.parseInt(standardPackageForm.getIndustryId())));
			}
			if (standardPackageForm.getPackageName() != null) {
				standardPackage.setPackageName(standardPackageForm.getPackageName().trim());
			}
			if(standardPackageForm.getTrailingMonths() != 0){
				standardPackage.setTrailingMonths(standardPackageForm.getTrailingMonths());
			}
			standardPackage.setIsStandard(standardPackageForm.getIsStandard());
			ClientData clientData = new ClientData();
			clientData.setUserPackage(standardPackage);

			// check the package name already exist or not

			LinkedHashMap<String, Object> dataResponse = (LinkedHashMap<String, Object>) restUtilities.postRestObject(request, "/createsPackage", clientData, Object.class, user.getUserId());

			List<LinkedHashMap<String, Object>> messages = (List<LinkedHashMap<String, Object>>) dataResponse.get("messages");

			if (messages != null && messages.size() > 0) {

				Map<String, Object> message = messages.get(0);
				String messagecode = message.get("code").toString();

				if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {

					Integer packageId = (Integer) dataResponse.get("object");
					standardPackageForm.setPackageId(packageId.toString());
					// to display message from rest service in jsp.
					String succesMsg = message.get("text").toString();
					mv.addObject("errors", succesMsg);
					mv.addObject("messagecode", messagecode);

					// get DLs of selected industry
					DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getClientsDLs", user.getUserId());
					if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
						if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							List<LinkedHashMap<String, Object>> map = (List<LinkedHashMap<String, Object>>) dlDataResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							List<DLInfo> userDlList = mapper.convertValue(map, new TypeReference<List<DLInfo>>() {
							});
							mv.addObject("userDlList", userDlList);
						} else {
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", dlDataResponse.getMessages().get(0).getText());
						}	
					}else{
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
						}
					mv.addObject("packageCreation", true);
				} else {
						String errorMsg = message.get("text").toString();
						result.rejectValue("packageName", errorMsg, errorMsg);
						mv.addObject("messagecode", messagecode);

				}
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:standardPackage");

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/standardPackage_old/edit/{packageId}", method = RequestMethod.GET)
	public ModelAndView editStandardPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @RequestParam(value = "source", required = false) String fromSource,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, Locale locale,RedirectAttributes redirectAttributes) {


		User user = CommonUtils.getUserDetails(request, null, null);

		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() ) {
				if ( userDataResponse.getMessages().get(0).getCode().equals("SUCCESS") ) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				standardPackageForm.setPackageName(userPackage.getPackageName());
			if (userPackage.getIsStandard() != null)
				standardPackageForm.setIsStandard(userPackage.getIsStandard());
			if (userPackage.getIndustry() != null)
				standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));
			if (userPackage.getTrailingMonths() != 0)
				standardPackageForm.setTrailingMonths(Integer.valueOf(userPackage.getTrailingMonths()));

			// get AllDls for the industry
			DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getClientsDLs", user.getUserId());
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if (dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("userDlList", dlDataResponse.getObject());
					
					/*DataResponse ilDataResponse = restUtilities.postRestObject(request, "/getClientIlsList", new LinkedMultiValueMap<>(), user.getUserId());
					
					if (ilDataResponse != null && ilDataResponse.getHasMessages()) {
						if (ilDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							mv.addObject("userIlList", ilDataResponse.getObject());
						} else {
							redirectAttributes.addFlashAttribute("messagecode", "FAILED");
							redirectAttributes.addFlashAttribute("errors", dlDataResponse.getMessages().get(0).getText());
						}
					}*/

				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dlDataResponse.getMessages().get(0).getText());
				}
			}else{
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getMappedModulesForStandardPackage/{" + packageId + "}", user.getUserId(),
					packageId);

			mv.addObject("mappedModuleSatandardPackage", dataResponse.getObject());

			mv.addObject("packageCreation", false);

			mv.setViewName("tiles-anvizent-entry:standardPackage");
			if (StringUtils.isNotBlank(fromSource)) {
				standardPackageForm.setPageMode(fromSource);
			}
		} else {
			mv.setViewName("redirect:/adt/standardpackage");
		}

		return mv;
	}

	/**
	 * 
	 * @param customPackageForm
	 * @return
	 */

	@RequestMapping(value = "/customPackage/new", method = RequestMethod.GET)
	public ModelAndView customPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result) {

		User user = CommonUtils.getUserDetails(request, null, null);

		DataResponse packageListDataResponse = restUtilities.getRestObject(request, "/getAllUserPackages", user.getUserId());
		List<Package> userPackageList = null;
		if (packageListDataResponse != null && packageListDataResponse.getHasMessages()
				&& packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			userPackageList = mapper.convertValue(packageListDataResponse.getObject(), new TypeReference<List<Package>>() {
			});
		} else {
			userPackageList = new ArrayList<>();
		}

		List<Package> scheduledPackageList = new ArrayList<>();
		for (Package userPackage : userPackageList) {
			//if (!userPackage.getIsStandard() && userPackage.getIsScheduled()) {
				scheduledPackageList.add(userPackage);
			//}
		}

		mv.addObject("scheduledPackageList", scheduledPackageList);
		customPackageForm.setIndustryId("0");
		customPackageForm.setIsStandard(false);
		customPackageForm.setIsFromExistingPackages(false);
		mv.setViewName("tiles-anvizent-entry:customPackage");
		return mv;
	}

	/**
	 * 
	 * @param customPackageForm
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customPackage/new", params = { "createCustomPackage" }, method = RequestMethod.POST)
	public ModelAndView createCustomPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("customPackageForm") @Valid CustomPackageForm customPackageForm, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		customPackageForm.setLocale(locale);
		cpValidator.validate(customPackageForm, result);
		try {
			if (result.hasErrors() || (customPackageForm.getIsFromExistingPackages()
					&& (customPackageForm.getExistingPackageId() == null || customPackageForm.getExistingPackageId() == 0))) {
				if (!result.hasErrors()) {
					result.rejectValue("existingPackageId", "Please select a package", "Please select a package");
				}

				DataResponse packageListDataResponse = restUtilities.getRestObject(request, "/getAllUserPackages", user.getUserId());
				List<Package> userPackageList = null;
				if (packageListDataResponse != null && packageListDataResponse.getHasMessages()
						&& packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackageList = mapper.convertValue(packageListDataResponse.getObject(), new TypeReference<List<Package>>() {
					});
				} else {
					userPackageList = new ArrayList<>();
				}

				List<Package> scheduledPackageList = new ArrayList<>();
				for (Package userPackage : userPackageList) {
					if (!userPackage.getIsStandard() && userPackage.getIsScheduled()) {
						scheduledPackageList.add(userPackage);
					}
				}
				mv.addObject("scheduledPackageList", scheduledPackageList);
				mv.setViewName("tiles-anvizent-entry:customPackage");

				return mv;
			}

			Package customPackage = new Package();
			if (customPackageForm.getIndustryId() != null) {
				customPackage.setIndustry(new Industry(Integer.parseInt(customPackageForm.getIndustryId())));
			}
			if (customPackageForm.getPackageName() != null) {
				customPackage.setPackageName(customPackageForm.getPackageName().trim());
			}

			if (customPackageForm.getIsFromExistingPackages()) {
				customPackage.setExistingPackageId(customPackageForm.getExistingPackageId());
			}
			customPackage.setIsStandard(customPackageForm.getIsStandard());
			ClientData clientData = new ClientData();
			clientData.setUserPackage(customPackage);

			// check the package name already exist or not

			LinkedHashMap<String, Object> dataResponse = (LinkedHashMap<String, Object>) restUtilities.postRestObject(request, "/createsPackage", clientData,
					Object.class, user.getUserId());

			List<LinkedHashMap<String, Object>> messages = (List<LinkedHashMap<String, Object>>) dataResponse.get("messages");
			if (messages != null && messages.size() > 0) {
				Map<String, Object> message = messages.get(0);

				String messagecode = message.get("code").toString();

				if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {

					Integer packageId = (Integer) dataResponse.get("object");
					customPackageForm.setPackageId(packageId.toString());

					// to display message from rest service in jsp.

					String succesMsg = message.get("text").toString();
					mv.addObject("errors", succesMsg);
					mv.addObject("messagecode", messagecode);

					if (customPackageForm.getIsFromExistingPackages()) {
						clientData.getUserPackage().setPackageId(packageId);
						LinkedHashMap<String, Object> dataResponse1 = (LinkedHashMap<String, Object>) restUtilities.postRestObject(request,
								"/importSourcesFromPackageToPackage", clientData, Object.class, user.getUserId());

						List<LinkedHashMap<String, Object>> messages1 = (List<LinkedHashMap<String, Object>>) dataResponse1.get("messages");

						if (messages1.get(0).get("code").equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)) {
							mv.clear();
							redirectAttributes.addFlashAttribute("errors", succesMsg);
							redirectAttributes.addFlashAttribute("messagecode", messagecode);
							mv.setViewName("redirect:/adt/package/customPackage/edit/" + packageId);
							return mv;
						} else {
							mv.addObject("errors", succesMsg + ". But unable to import sources from selected package");
							mv.addObject("messagecode", "FAILED");

						}

					} else {
						mv.clear();
						redirectAttributes.addFlashAttribute("errors", succesMsg);
						redirectAttributes.addFlashAttribute("messagecode", messagecode);
						mv.setViewName("redirect:/adt/package/customPackage/edit/" + packageId);
						return mv;
					}

					DataResponse clientDataResponse = restUtilities.getRestObject(request, "/getSourcesDetails/{client_Id}", user.getUserId(),
							user.getClientId());
					if (clientDataResponse != null && clientDataResponse.getHasMessages()
							&& clientDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) clientDataResponse.getObject();
						ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						clientData = mapper1.convertValue(map, new TypeReference<ClientData>() {
						});
						mv.addObject("clientData", clientData);
					}

					boolean isFlatFileOptionLocked = false;
					boolean isDataBaseOptionLocked = false;
					if (clientData != null) {
						isFlatFileOptionLocked = clientData.getIs_flat_file_locked();
						isDataBaseOptionLocked = clientData.getIs_database_locked();
					}
					mv.addObject("isFlatFileOptionLocked", isFlatFileOptionLocked);
					mv.addObject("isDataBaseOptionLocked", isDataBaseOptionLocked);

				} else {
						String errorMsg = message.get("text").toString();
						result.rejectValue("packageName", errorMsg, errorMsg);

				}
			}
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("scheduledPackageList", getScheduledPackageList(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:customPackage");

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customPackage/edit/{packageId}", method = RequestMethod.GET)
	public ModelAndView editCustomPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @RequestParam(value = "source", required = false) String fromSource,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		ClientData clientData = null;
		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages()) {
				if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				}   else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			}else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
				
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				customPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));

			if (userPackage.getPackageName() != null)
				customPackageForm.setPackageName(userPackage.getPackageName());

			if (userPackage.getIsStandard() != null)
				customPackageForm.setIsStandard(userPackage.getIsStandard());

			if (userPackage.getIndustry() != null)
				customPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

			customPackageForm.setIsFileHavingSameColumnNames(userPackage.getFilesHavingSameColumns());
			customPackageForm.setIsClientDbTables(userPackage.getIsClientDbTables());
			customPackageForm.setIsScheduled(userPackage.getIsScheduled());

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}", Map.class,
					user.getUserId(), packageId);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.get("object");

			try {
				List<ClientData> mappingInfo = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
				});
				mv.addObject("targetTableDirectMappingInfo", mappingInfo);

				List<Map<String, Object>> tempTablesList = restUtilities.getRestObject(request, "/getCustomFileTempTableMappings/{" + packageId + "}",
						List.class, user.getUserId(), packageId);

				if (tempTablesList == null || tempTablesList.size() == 0 || tempTablesList.size() != mappingInfo.size()) {
					customPackageForm.setTempTablesProcessed(false);
				} else {
					customPackageForm.setTempTablesProcessed(true);
				}

			} catch (Exception e) {
				LOGGER.error("", e);
			}

			if (userPackage.getTable() != null) {

				Table table = userPackage.getTable();
				if (table.getTableId() != null) {
					customPackageForm.setTargetTableId(String.valueOf(table.getTableId()));
				}
				if (table.getTableName() != null) {
					customPackageForm.setTargetTableName(table.getTableName());
					;
				}
				if (table.getIsProcessed() != null) {
					customPackageForm.setIsProcessed(table.getIsProcessed());
				}
				if (table.getNoOfRecordsProcessed() != null) {
					customPackageForm.setNoOfRecordsProcessed(table.getNoOfRecordsProcessed());
				}
				if (table.getNoOfRecordsFailed() != null) {
					customPackageForm.setNoOfRecordsFailed(table.getNoOfRecordsFailed());
				}
				if (table.getDuplicateRecords() != null) {
					customPackageForm.setDuplicateRecords(table.getDuplicateRecords());
				}
				if (table.getTotalRecords() != null) {
					customPackageForm.setTotalRecords(table.getTotalRecords());
				}

				mv.addObject("derivedTables", userPackage.getDerivedTables());

			}
			DataResponse clientDataResponse = restUtilities.getRestObject(request, "/getSourcesDetails/{client_Id}", user.getUserId(), user.getClientId());
			if (clientDataResponse != null && clientDataResponse.getHasMessages() && clientDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) clientDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				clientData = mapper1.convertValue(map, new TypeReference<ClientData>() {
				});
				mv.addObject("clientData", clientData);
			}
			boolean isFlatFileOptionLocked = false;
			boolean isDataBaseOptionLocked = false;
			if (clientData != null) {
				isFlatFileOptionLocked = clientData.getIs_flat_file_locked();
				isDataBaseOptionLocked = clientData.getIs_database_locked();
			}
			mv.addObject("isFlatFileOptionLocked", isFlatFileOptionLocked);
			mv.addObject("isDataBaseOptionLocked", isDataBaseOptionLocked);
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
			mv.setViewName("tiles-anvizent-entry:customPackage");
			if (StringUtils.isNotBlank(fromSource)) {
				customPackageForm.setPageMode(fromSource);
			}
		} else {
			mv.setViewName("redirect:/adt/package/custompackage");
		}

		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/standardPackage_old/addILSource/{packageId}/{dLId}/{iLId}", method = RequestMethod.GET)
	public ModelAndView addILSource(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("packageId") Integer packageId,
			@PathVariable("dLId") Integer dLId, @PathVariable("iLId") Integer iLId,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, RedirectAttributes redirectAttributes,Locale locale) {

		ClientData clientData = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		List<ILInfo> iLList = null;
		DLInfo dLInfo = null;
		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper.convertValue(map, new TypeReference<Package>() {
				});
				mv.addObject("userPackage", userPackage);
			}
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				standardPackageForm.setPackageName(userPackage.getPackageName());
			if (userPackage.getIsStandard() != null)
				standardPackageForm.setIsStandard(userPackage.getIsStandard());
			if (userPackage.getIndustry() != null)
				standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

			DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getDLsById/{" + dLId + "}", user.getUserId(), dLId);
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dlDataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					dLInfo = mapper1.convertValue(dls, new TypeReference<DLInfo>() {
					});
					mv.addObject("dLInfo", dLInfo);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dlDataResponse.getMessages().get(0).getText());
				}
			}else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllILS/{" + dLId + "}", user.getUserId(), dLId);
			if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
				if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					List<LinkedHashMap<String, Object>> iLs = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					iLList = mapper.convertValue(iLs, new TypeReference<List<ILInfo>>() {
					});
					mv.addObject("iLList", iLList);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			}else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			String clientId = user.getClientId();

			if (dLInfo != null) {
				if (dLInfo.getdL_name() != null) {
					standardPackageForm.setdLName(dLInfo.getdL_name());
				}
				standardPackageForm.setdLId(String.valueOf(dLInfo.getdL_id()));

			}
			if(iLList != null){
				for (ILInfo iLInfo : iLList) {
					if (iLInfo != null) {
						if (iLInfo.getiL_id() == iLId) {
							if (iLInfo.getiL_name() != null) {
								standardPackageForm.setiLName(String.valueOf(iLInfo.getiL_id()));
							}
						}

					}
				}
			}
			mv.addObject("defaultILId", iLId);

			DataResponse clientDataResponse = restUtilities.getRestObject(request, "/getSourcesDetails/{client_Id}", user.getUserId(), clientId);
			if (clientDataResponse != null && clientDataResponse.getHasMessages()) {
				if(clientDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) clientDataResponse.getObject();
					ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					clientData = mapper1.convertValue(map, new TypeReference<ClientData>() {
					});
					mv.addObject("clientData", clientData);
				}
			}else{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			boolean isFlatFileOptionLocked = false;
			boolean isDataBaseOptionLocked = false;
			if (clientData != null) {
				isFlatFileOptionLocked = clientData.getIs_flat_file_locked();
				isDataBaseOptionLocked = clientData.getIs_database_locked();
			}
			mv.addObject("isFlatFileOptionLocked", isFlatFileOptionLocked);
			mv.addObject("isDataBaseOptionLocked", isDataBaseOptionLocked);
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("webServiceList", getAllWebServices(request, response, mv));
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
			mv.setViewName("tiles-anvizent-entry:addILSource");
		} else {
			mv.setViewName("redirect:/adt/standardpackage");
		}
		return mv;
	}

	/**
	 * 
	 * delete package
	 */

	@SuppressWarnings("unchecked")
	public List<Database> getDatabaseTypes(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Database> databseList = null;

		databseList = new ArrayList<>();
		DataResponse databseTypesDataResponse = restUtilities.getRestObject(request, "/getDatabasesTypes", user.getUserId());
		List<LinkedHashMap<String, Object>> databseTypes = null;
		if (databseTypesDataResponse != null && databseTypesDataResponse.getHasMessages()
				&& databseTypesDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			databseTypes = (List<LinkedHashMap<String, Object>>) databseTypesDataResponse.getObject();
		} else {
			databseTypes = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : databseTypes) {
			Database databse = new Database();
			int id = Integer.parseInt(map.get("id").toString());
			int connector_id = Integer.parseInt(map.get("connector_id").toString());
			databse.setId(id);
			databse.setName(map.get("name").toString());
			databse.setConnector_id(connector_id);
			databse.setProtocal((String)map.get("protocal"));  
			databse.setConnectionStringParams((String)map.get("connectionStringParams"));  
			databse.setUrlFormat((String)map.get("urlFormat"));
			databseList.add(databse);
		}

		return databseList;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewIlSource/{packageId}/{dLId}/{iLId}", method = RequestMethod.GET)
	public ModelAndView viewIlSource(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("packageId") String packageId,
			@PathVariable("dLId") String dLId, @PathVariable("iLId") String iLId,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			DLInfo dLInfo = null;
			Package userPackage = null;
			if (packageId != null) {
				DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
				if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			}
			if (userPackage != null) {

				if (userPackage.getPackageId() != 0)
					standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
				if (userPackage.getPackageName() != null)
					standardPackageForm.setPackageName(userPackage.getPackageName());
				if (userPackage.getIsStandard() != null)
					standardPackageForm.setIsStandard(userPackage.getIsStandard());
				if (userPackage.getIndustry() != null)
					standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

				DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getDLsById/{" + dLId + "}", user.getUserId(), dLId);
				if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
					if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dlDataResponse.getObject();
						ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						dLInfo = mapper1.convertValue(dls, new TypeReference<DLInfo>() {
						});
						mv.addObject("dLInfo", dLInfo);
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dlDataResponse.getMessages().get(0).getText());
					}
					}else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
					
				if (dLInfo != null) {
					if (dLInfo.getdL_name() != null) {
						standardPackageForm.setdLName(dLInfo.getdL_name());
					}
					standardPackageForm.setdLId(String.valueOf(dLInfo.getdL_id()));

				}
				ILInfo iLInfo = restUtilities.getRestObject(request, "/getILById/{" + iLId + "}", ILInfo.class, user.getUserId(), iLId);
				if (iLInfo != null) {
					if (iLInfo.getiL_name() != null) {
						standardPackageForm.setiLName(iLInfo.getiL_name());
					}
					standardPackageForm.setiLId(String.valueOf(iLInfo.getiL_id()));

				} 
			}

			dataResponse = restUtilities.getRestObject(request, "/getILsConnectionMappingInfo/{" + iLId + "}/{" + dLId + "}/{" + packageId + "}",
					user.getUserId(), iLId, dLId, packageId);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if(dataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					List<LinkedHashMap<String, Object>> connectionMap = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<ILConnectionMapping> iLConnectionMapping = mapper.convertValue(connectionMap, new TypeReference<List<ILConnectionMapping>>() {
					});
					boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
					List<ILConnectionMapping> iLsConnectionMappingInfoList = new ArrayList<>();
					for (ILConnectionMapping connectionMapping : iLConnectionMapping) {
						if (connectionMapping.getiLConnection() != null) {
							ILConnection iLConnection = connectionMapping.getiLConnection();
							iLConnection.setWebApp(isWebApp);
							connectionMapping.setiLConnection(iLConnection);
							if(StringUtils.isNotBlank(connectionMapping.getiLConnection().getDbVariables())) {
								JSONObject dbVarObject = null;
								JSONArray dbVariablesArray = new JSONArray(connectionMapping.getiLConnection().getDbVariables());
								Map<String,String> dbVariablesMap = new HashMap<String, String>();
								for(int i=0;i<dbVariablesArray.length();i++) {
									dbVarObject = dbVariablesArray.getJSONObject(i);
									dbVariablesMap.put(dbVarObject.getString("key"), dbVarObject.getString("value"));
								}
								iLConnection.setDbVariablesList(dbVariablesMap);
							}
							
							if (StringUtils.isNotBlank(iLConnection.getServer())&& StringUtils.startsWith(iLConnection.getServer(), "{")) {
								JSONObject jsonObject =new JSONObject(iLConnection.getServer());
								JSONArray sourcemappingArray = jsonObject.getJSONArray("connectionParams");
								JSONObject sources =null;
								String protocal = iLConnection.getDatabase().getProtocal();
								protocal  = protocal.substring(protocal.indexOf("{"));
								for(int i=0; i < sourcemappingArray.length();i++) {
									sources = sourcemappingArray.getJSONObject(i);
									protocal = StringUtils.replace(protocal, "{"+sources.getString("placeholderKey")+"}", sources.getString("placeholderValue"));
								}
								iLConnection.setServer(protocal);
							}
						}
						iLsConnectionMappingInfoList.add(connectionMapping);
					}

					mv.addObject("iLConnectionMapping", iLsConnectionMappingInfoList);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}	
			}else{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		String referer = request.getHeader("Referer");
		mv.addObject("referer", referer);
		mv.addObject("allDataSourceList", getDataSourceList(request));
		mv.setViewName("tiles-anvizent-entry:viewIlSource");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewCustomPackageSource/{packageId}/{mapping_Id}", method = RequestMethod.GET)
	public ModelAndView viewCustomPackageSource(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("mapping_Id") String mapping_Id,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			Package userPackage = null;
			if (packageId != null) {
				DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
				if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			}
			if (userPackage != null) {

				if (userPackage.getPackageId() != 0)
					customPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
				if (userPackage.getPackageName() != null)
					customPackageForm.setPackageName(userPackage.getPackageName());
				if (userPackage.getIsStandard() != null)
					customPackageForm.setIsStandard(userPackage.getIsStandard());
				if (userPackage.getIndustry() != null)
					customPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

				customPackageForm.setIsScheduled(userPackage.getIsScheduled());
			}
			DataResponse iLConnectionMappingInfoDataResponse = restUtilities.getRestObject(request,
					"/getILConnectionMappingInfoByMappingId/{" + packageId + "}/{" + mapping_Id + "}", user.getUserId(), packageId, mapping_Id);
			if(iLConnectionMappingInfoDataResponse != null && iLConnectionMappingInfoDataResponse.getHasMessages()){
				if(iLConnectionMappingInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) iLConnectionMappingInfoDataResponse.getObject();
					ILConnectionMapping iLConnectionMapping = mapper.convertValue(map, new TypeReference<ILConnectionMapping>() {
					});
					if (iLConnectionMapping.getiLConnection() != null) {
						ILConnection iLConnection = iLConnectionMapping.getiLConnection();
						iLConnection.setWebApp(isWebApp);

						if(StringUtils.isNotBlank(iLConnectionMapping.getiLConnection().getDbVariables())) {
							JSONObject dbVarObject = null;
							JSONArray dbVariablesArray = new JSONArray(iLConnectionMapping.getiLConnection().getDbVariables());
							Map<String,String> dbVariablesMap = new HashMap<String, String>();
							for(int i=0;i<dbVariablesArray.length();i++) {
								dbVarObject = dbVariablesArray.getJSONObject(i);
								dbVariablesMap.put(dbVarObject.getString("key"), dbVarObject.getString("value"));
							}
							iLConnection.setDbVariablesList(dbVariablesMap);
						}
						
						if (StringUtils.isNotBlank(iLConnection.getServer())&& StringUtils.startsWith(iLConnection.getServer(), "{")) {
							JSONObject jsonObject =new JSONObject(iLConnection.getServer());
							JSONArray sourcemappingArray = jsonObject.getJSONArray("connectionParams");
							JSONObject sources =null;
							String protocal = iLConnection.getDatabase().getProtocal();
							protocal  = protocal.substring(protocal.indexOf("{"));
							for(int i=0; i < sourcemappingArray.length();i++) {
								sources = sourcemappingArray.getJSONObject(i);
								protocal = StringUtils.replace(protocal, "{"+sources.getString("placeholderKey")+"}", sources.getString("placeholderValue"));
							}
							iLConnection.setServer(protocal);
						}
						iLConnectionMapping.setiLConnection(iLConnection);
					}
					mv.addObject("ilConnectionMapping", iLConnectionMapping);
					String referer = request.getHeader("Referer");
					mv.addObject("isFromDerivedTables", userPackage.getIsClientDbTables());
					mv.addObject("referer", referer);
	
				}else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", iLConnectionMappingInfoDataResponse.getMessages().get(0).getText());
				}
			}else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.setViewName("tiles-anvizent-entry:viewCustomPackageSource");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@RequestMapping(value = "/derivedTable/new", method = RequestMethod.GET)
	public ModelAndView derivedTable(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result) {

		customPackageForm.setIndustryId("0");
		customPackageForm.setIsStandard(false);
		mv.addObject("timesZoneList", getTimesZoneList(request));
		mv.addObject("databseList", getDatabaseTypes(request));
		mv.setViewName("tiles-anvizent-entry:derivedTable");

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/derivedTable/new", params = { "createCustomPackage" }, method = RequestMethod.POST)
	public ModelAndView createDerivedTable(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("customPackageForm") @Valid CustomPackageForm customPackageForm, BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("databseList", getDatabaseTypes(request));
			if (result.hasErrors()) {
				mv.setViewName("tiles-anvizent-entry:derivedTable");
				return mv;
			}

			Package customPackage = new Package();
			if (customPackageForm.getIndustryId() != null) {
				customPackage.setIndustry(new Industry(Integer.parseInt(customPackageForm.getIndustryId())));
			}
			if (customPackageForm.getPackageName() != null) {
				customPackage.setPackageName(customPackageForm.getPackageName());
			}
			customPackage.setIsStandard(customPackageForm.getIsStandard());

			ClientData clientData = new ClientData();
			clientData.setUserPackage(customPackage);

			// TODO check the package name already exist or not

			LinkedHashMap<String, Object> dataResponse = (LinkedHashMap<String, Object>) restUtilities.postRestObject(request, "/createsPackage", clientData,
					Object.class, user.getUserId());

			List<LinkedHashMap<String, Object>> messages = (List<LinkedHashMap<String, Object>>) dataResponse.get("messages");

			if (messages != null && messages.size() > 0) {
				Map<String, Object> message = messages.get(0);

				String messagecode = message.get("code").toString();

				if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {

					Integer packageId = (Integer) dataResponse.get("object");
					customPackageForm.setPackageId(packageId.toString());
					customPackageForm.setIsClientDbTables(false);
					// to display message from rest service in jsp.
					String succesMsg = message.get("text").toString();
					mv.addObject("errors", succesMsg);
					mv.addObject("messagecode", messagecode);

				} else {
						String errorMsg = message.get("text").toString();
						result.rejectValue("packageName", errorMsg, errorMsg);

				}
			}

			Object object = restUtilities.getRestObject(request, "/getTargetTables/{" + customPackageForm.getIndustryId() + "}", Object.class, user.getUserId(),
					customPackageForm.getIndustryId());
			Map<String, Object> tagetTableDataResponse = (Map<String, Object>) object;
			List<String> tableDataResponse = (List<String>) tagetTableDataResponse.get("object");

			List<LinkedHashMap<String, Object>> sucessMsg = (List<LinkedHashMap<String, Object>>) tagetTableDataResponse.get("messages");

			if (sucessMsg != null && sucessMsg.size() > 0) {
				Map<String, Object> message = sucessMsg.get(0);

				String messagecode = message.get("code").toString();

				if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {
					mv.addObject("tableDataResponse", tableDataResponse);
				}

			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:derivedTable");

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/derivedTable/edit/{packageId}", method = RequestMethod.GET)
	public ModelAndView editDerivedTable(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("packageId") String packageId,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, RedirectAttributes redirectAttributes,Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages()) {
				if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				}  else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
				}
			}else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
		}
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				customPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));

			if (userPackage.getPackageName() != null)
				customPackageForm.setPackageName(userPackage.getPackageName());

			if (userPackage.getIsStandard() != null)
				customPackageForm.setIsStandard(userPackage.getIsStandard());

			if (userPackage.getIndustry() != null)
				customPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

			customPackageForm.setIsFileHavingSameColumnNames(userPackage.getFilesHavingSameColumns());
			customPackageForm.setIsClientDbTables(userPackage.getIsClientDbTables());
			customPackageForm.setIsScheduled(userPackage.getIsScheduled());

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}", Map.class,
					user.getUserId(), packageId);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.get("object");

			try {
				List<ClientData> mappingInfo = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
				});

				System.out.println("mappingInfo:====>" + mappingInfo);
				mv.addObject("targetTableDirectMappingInfo", mappingInfo);

				List<Map<String, Object>> tempTablesList = restUtilities.getRestObject(request, "/getCustomFileTempTableMappings/{" + packageId + "}",
						List.class, user.getUserId(), packageId);

				if (tempTablesList == null || tempTablesList.size() == 0 || tempTablesList.size() != mappingInfo.size()) {
					customPackageForm.setTempTablesProcessed(false);
				} else {
					customPackageForm.setTempTablesProcessed(true);
				}

			} catch (Exception e) {
				LOGGER.error("", e);
			}

			if (userPackage.getTable() != null) {

				Table table = userPackage.getTable();
				if (table.getTableId() != null) {
					customPackageForm.setTargetTableId(String.valueOf(table.getTableId()));
				}
				if (table.getTableName() != null) {
					customPackageForm.setTargetTableName(table.getTableName());
					;
				}
				if (table.getIsProcessed() != null) {
					customPackageForm.setIsProcessed(table.getIsProcessed());
				}
				if (table.getNoOfRecordsProcessed() != null) {
					customPackageForm.setNoOfRecordsProcessed(table.getNoOfRecordsProcessed());
				}
				if (table.getNoOfRecordsFailed() != null) {
					customPackageForm.setNoOfRecordsFailed(table.getNoOfRecordsFailed());
				}
				if (table.getDuplicateRecords() != null) {
					customPackageForm.setDuplicateRecords(table.getDuplicateRecords());
				}
				if (table.getTotalRecords() != null) {
					customPackageForm.setTotalRecords(table.getTotalRecords());
				}

				mv.addObject("derivedTables", userPackage.getDerivedTables());

			}
			Object object = restUtilities.getRestObject(request, "/getTargetTables/{" + customPackageForm.getIndustryId() + "}", Object.class, user.getUserId(),
					customPackageForm.getIndustryId());
			Map<String, Object> tagetTableDataResponse = (Map<String, Object>) object;
			List<String> tableDataResponse = (List<String>) tagetTableDataResponse.get("object");

			List<LinkedHashMap<String, Object>> sucessMsg = (List<LinkedHashMap<String, Object>>) tagetTableDataResponse.get("messages");

			if (sucessMsg != null && sucessMsg.size() > 0) {
				Map<String, Object> message = sucessMsg.get(0);

				String messagecode = message.get("code").toString();

				if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {
					mv.addObject("tableDataResponse", tableDataResponse);
				}

			}
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.setViewName("tiles-anvizent-entry:derivedTable");
		} else {
			mv.setViewName("redirect:/adt/package/custompackage");
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/easyQueryBuilderForStandardPackage", method = RequestMethod.POST)
	public ModelAndView easyQueryBuilderForStandardPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@RequestParam("schemaName") String schemaName, @RequestParam("packageId") String packageId, @RequestParam("dlId") int dLId,
			@RequestParam("iLId") int iLId, @RequestParam("connectionId") int connectionId,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Database database = null;
		try {

			Package userPackage = null;
			DLInfo dLInfo = null;
			if (packageId != null) {
				DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
				if (userDataResponse != null && userDataResponse.getHasMessages()) {
					if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						userPackage = mapper.convertValue(map, new TypeReference<Package>() {
						});
						mv.addObject("userPackage", userPackage);
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", userDataResponse.getMessages().get(0).getText());
					}	
				}else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			}
			if (userPackage != null) {

				if (userPackage.getPackageId() != 0)
					standardPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
				if (userPackage.getPackageName() != null)
					standardPackageForm.setPackageName(userPackage.getPackageName());
				if (userPackage.getIsStandard() != null)
					standardPackageForm.setIsStandard(userPackage.getIsStandard());
				if (userPackage.getIndustry() != null)
					standardPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

				DataResponse dlDataResponse = restUtilities.getRestObject(request, "/getDLsById/{" + dLId + "}", user.getUserId(), dLId);
				if (dlDataResponse != null && dlDataResponse.getHasMessages()) {
					if(dlDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						LinkedHashMap<String, Object> dls = (LinkedHashMap<String, Object>) dlDataResponse.getObject();
						ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						dLInfo = mapper1.convertValue(dls, new TypeReference<DLInfo>() {
						});
						mv.addObject("dLInfo", dLInfo);
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dlDataResponse.getMessages().get(0).getText());
					}
				} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				
				if (dLInfo != null) {
					if (dLInfo.getdL_name() != null) {
						standardPackageForm.setdLName(dLInfo.getdL_name());
					}
					standardPackageForm.setdLId(String.valueOf(dLInfo.getdL_id()));

				}
				ILInfo iLInfo = restUtilities.getRestObject(request, "/getILById/{" + iLId + "}", ILInfo.class, user.getUserId(), iLId);
				if (iLInfo != null) {
					if (iLInfo.getiL_name() != null) {
						standardPackageForm.setiLName(iLInfo.getiL_name());
					}
					standardPackageForm.setiLId(String.valueOf(iLInfo.getiL_id()));
				}
			}
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getILsConnectionByIdWithSchemas/{" + connectionId + "}", user.getUserId(),
					connectionId);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if(dataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> connectionMap = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ILConnection iLConnection = null;
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					iLConnection = mapper.convertValue(connectionMap, new TypeReference<ILConnection>() {
					});

					// get all schemas based on connection Id
					List<String> schemaList = CommonUtils.getAllSchemasFromDatabase(iLConnection);
					database = iLConnection.getDatabase();
					database.setSchemas(schemaList);
					iLConnection.setDatabase(database);
					iLConnection.setConnectionId(connectionId);
					mv.addObject("iLConnection", iLConnection);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			}else{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
				
			mv.addObject("isStandard", true);
			mv.addObject("packageId", packageId);
			mv.addObject("dLId", dLId);
			mv.addObject("iLId", iLId);
			mv.addObject("schemaName", schemaName);
			mv.addObject("isDDlayout", false);
			mv.addObject("isApisData", false);
			Object object = restUtilities.getRestObject(request, "/getIlEpicorQuery/{" + iLId + "}/{" + connectionId + "}", Object.class, user.getUserId(),
					iLId, connectionId);
			Map<String, Object> defalutQueryDataResponse = (Map<String, Object>) object;
			String defaultQuery = (String) defalutQueryDataResponse.get("object");
			mv.addObject("defaultQuery", defaultQuery);
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.setViewName("tiles-anvizent-entry:easyquerybuilder");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/easyQueryBuilderForCustomPackage/{packageId}/{connectionId}", method = RequestMethod.GET)
	public ModelAndView easyQueryBuilderForCustomPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("connectionId") int connectionId,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Database database = null;
		if (user != null) {
			try {
				mv.addObject("databseList", getDatabaseTypes(request));
				mv.setViewName("tiles-anvizent-entry:easyquerybuilder");
				Package userPackage = null;

				if (packageId != null) {
					DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
					if (userDataResponse != null && userDataResponse.getHasMessages()) {
						if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							userPackage = mapper.convertValue(map, new TypeReference<Package>() {
							});
							mv.addObject("userPackage", userPackage);
						} else {
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", userDataResponse.getMessages().get(0).getText());
						}
					}else{
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
						}
				}

				if (userPackage != null) {

					if (userPackage.getPackageId() != 0)
						customPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
					if (userPackage.getPackageName() != null)
						customPackageForm.setPackageName(userPackage.getPackageName());
					if (userPackage.getIsStandard() != null)
						customPackageForm.setIsStandard(userPackage.getIsStandard());
					if (userPackage.getIndustry() != null)
						customPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));

					customPackageForm.setIsScheduled(userPackage.getIsScheduled());
				}

				DataResponse dataResponse = restUtilities.getRestObject(request, "/getILsConnectionByIdWithSchemas/{" + connectionId + "}", user.getUserId(),
						connectionId);
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if(dataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						LinkedHashMap<String, Object> connectionMap = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						ILConnection iLConnection = mapper.convertValue(connectionMap, new TypeReference<ILConnection>() {
						});
						List<String> schemaList = CommonUtils.getAllSchemasFromDatabase(iLConnection);
						database = iLConnection.getDatabase();
						database.setSchemas(schemaList);
						iLConnection.setDatabase(database);
						iLConnection.setConnectionId(connectionId);
						mv.addObject("iLConnection", iLConnection);
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
					}else{
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
					
				// get all schemas based on connection Id

				mv.addObject("isCustom", true);
				mv.addObject("packageId", packageId);
				mv.addObject("isDDlayout", false);
				mv.addObject("isApisData", false);
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		} 
		else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/easyQueryBuilderForDerivedTables/{packageId}/{connectionId}", method = RequestMethod.GET)
	public ModelAndView easyQueryBuilderForDerivedTables(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("connectionId") int connectionId,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, RedirectAttributes redirectAttributes,Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Database database = null;
		if (user != null) {
			try {

				Package userPackage = null;

				if (packageId != null) {
					DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
					if (userDataResponse != null && userDataResponse.getHasMessages()) {
						if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							userPackage = mapper.convertValue(map, new TypeReference<Package>() {
							});
							mv.addObject("userPackage", userPackage);
						}  else {
							redirectAttributes.addFlashAttribute("messagecode", "FAILED");
							redirectAttributes.addFlashAttribute("errors", userDataResponse.getMessages().get(0).getText());
						}
					}else {
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				}
						

				if (userPackage != null) {
					if (userPackage.getPackageId() != 0)
						customPackageForm.setPackageId(String.valueOf(userPackage.getPackageId()));
					if (userPackage.getPackageName() != null)
						customPackageForm.setPackageName(userPackage.getPackageName());
					if (userPackage.getIsStandard() != null)
						customPackageForm.setIsStandard(userPackage.getIsStandard());
					if (userPackage.getIndustry() != null)
						customPackageForm.setIndustryId(String.valueOf(userPackage.getIndustry().getId()));
					customPackageForm.setIsScheduled(userPackage.getIsScheduled());
				}

				DataResponse dataResponse = restUtilities.getRestObject(request, "/getILsConnectionByIdWithSchemas/{" + connectionId + "}", user.getUserId(),
						connectionId);
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if(dataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						LinkedHashMap<String, Object> connectionMap = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						ILConnection iLConnection = mapper.convertValue(connectionMap, new TypeReference<ILConnection>() {
						});
						List<String> schemaList = CommonUtils.getAllSchemasFromDatabase(iLConnection);
						database = iLConnection.getDatabase();
						database.setSchemas(schemaList);
						iLConnection.setDatabase(database);
						iLConnection.setConnectionId(connectionId);
						mv.addObject("iLConnection", iLConnection);
					}else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				}else{
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
					
				// get all schemas based on connection Id
				mv.addObject("isCustom", false);
				mv.addObject("isDerived", true);
				mv.addObject("packageId", packageId);
				mv.addObject("isDDlayout", false);
				mv.addObject("isApisData", false);
				mv.addObject("databseList", getDatabaseTypes(request));
				mv.setViewName("tiles-anvizent-entry:easyquerybuilder");

			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		} else {
			//mv.setViewName("redirect:/login");
			mv.setViewName("redirect:/adt/package/custompackage");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteCustomTargetTable/{packageId}", method = RequestMethod.GET)
	public ModelAndView deleteCustomTargetTable(HttpServletRequest request, HttpServletResponse response, @PathVariable("packageId") Integer packageId,
			ModelAndView mv, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				Package userPackage = null;

				if (packageId != null) {
					DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
					if (userDataResponse != null && userDataResponse.getHasMessages()) {
						if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
							LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							userPackage = mapper.convertValue(map, new TypeReference<Package>() {
							});
							mv.addObject("userPackage", userPackage);
						}else {
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", userDataResponse.getMessages().get(0).getText());
						}
					}else{
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				}
				if (userPackage != null) {
					DataResponse dataResponse = restUtilities.getRestObject(request, "/deleteCustomTargetTable/{" + packageId + "}", user.getUserId(),
							packageId);

					List<Message> messages = dataResponse.getMessages();
					if (messages != null && messages.size() > 0) {
						Message message = messages.get(0);

						String messagecode = message.getCode().toString();

						if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(messagecode)) {

							mv.setViewName("redirect:/adt/package/derivedTable/edit/" + packageId);
						}

					}
				}

			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}

		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/archivedPackages", method = RequestMethod.GET)
	public ModelAndView activePackages(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("archivedPackages", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {

				DataResponse packageListresponse = restUtilities.getRestObject(request, "/getAllUserPackages", user.getUserId());

				List<Package> userPackageList = null;
				if (packageListresponse != null && packageListresponse.getHasMessages()) {
					if(packageListresponse.getMessages().get(0).getCode().equals("SUCCESS")){
						userPackageList = (List<Package>) packageListresponse.getObject();
						mv.addObject("userPackageList", userPackageList);
					}else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", packageListresponse.getMessages().get(0).getText());
					}
				}else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
				mv.setViewName("tiles-anvizent-entry:activePackage");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/purge", method = RequestMethod.GET)
	public ModelAndView purge(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("purge", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				DataResponse dataResponse = restUtilities.getRestObject(request, "/getClientILsandDLs", user.getUserId());
				if(dataResponse != null && dataResponse.getHasMessages()){
					if(dataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
						List<Table> clientStandardTables = (List<Table>) dataResponse.getObject();
						mv.addObject("clientStandardTables", clientStandardTables);
					}else{
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				}else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
				
				mv.setViewName("tiles-anvizent-entry:purge");

			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@RequestMapping(value = "/databaseConnection", method = RequestMethod.GET)
	public ModelAndView createNewConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,@ModelAttribute("clientDataSources") ClientDataSources clientDataSources, HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("databaseConnection", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getUserILConnections", user.getUserId());
			
			
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

					List<ILConnection> existingConnectionsList = new ArrayList<ILConnection>();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					List<?> list = (List<Map<String, Object>>) dataResponse.getObject();
					List<ILConnection> iLConnectionList = mapper.convertValue(list, new TypeReference<List<ILConnection>>() {
					});
					if (iLConnectionList != null && !iLConnectionList.isEmpty()) {
						for (ILConnection iLConnection : iLConnectionList) {
							if (iLConnection != null) {
								iLConnection.setWebApp(isWebApp);
								existingConnectionsList.add(iLConnection);
							}
						}
					}

					mv.addObject("existingConnections", existingConnectionsList);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
			mv.setViewName("tiles-anvizent-entry:databaseConnection");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}
	
	
	@RequestMapping(value = "/databaseNewConnection", method = RequestMethod.GET)
	public ModelAndView databaseNewConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,@ModelAttribute("clientDataSources") ClientDataSources clientDataSources, HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("databaseConnection", session);
		try {
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.addObject("timesZoneList", getTimesZoneList(request));
			mv.addObject("allDataSourceList", getDataSourceList(request));
			mv.setViewName("tiles-anvizent-newEntry:databaseNewConnection");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewResultsForCustomPackage/{packageId}", method = RequestMethod.GET)
	public ModelAndView viewResultsForCustomPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @RequestParam(value = "source", required = false) String fromSource,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {


		User user = CommonUtils.getUserDetails(request, null, null);

		Package userPackage = null;
		if (packageId != null) {
			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages()) {
				if(userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
					mv.addObject("userPackage", userPackage);
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", userDataResponse.getMessages().get(0).getText());
				}
			}else{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		}
				
		if (userPackage != null) {
			if (userPackage.getPackageId() != 0)
				jobResultForm.setPackageId(String.valueOf(userPackage.getPackageId()));
			if (userPackage.getPackageName() != null)
				jobResultForm.setPackageName(userPackage.getPackageName());

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/getJobResults/{" + packageId + "}", Map.class, user.getUserId(),
					packageId);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.get("object");

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});
			if (StringUtils.isNotBlank(fromSource)) {
				jobResultForm.setPageMode(fromSource);
			}
			mv.addObject("viewResultList", viewResultList);
			mv.setViewName("tiles-anvizent-entry:viewResultsForCustomPackage");

		} else {
			mv.setViewName("redirect:/adt/standardpackage");
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewResultsForCustomPackage/{packageId}", method = RequestMethod.POST)
	public ModelAndView searchResultsForCustomPackage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {


		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String packageId = jobResultForm.getPackageId();
			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			Map<String, Object> dataResponse = restUtilities.getRestObject(request,
					"/getJobResultsByDate/{" + packageId + "}/{" + fromDate + "}/{" + toDate + "}", Map.class, user.getUserId(), packageId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.get("object");

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResultsForCustomPackage");

		return mv;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getAllWebServices(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllWebServices", user.getUserId());

		Map<Object, Object> webServiceMap = new LinkedHashMap<>();
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			Map<Integer, String> map = (Map<Integer, String>) dataResponse.getObject();
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				webServiceMap.put(entry.getKey(), entry.getValue());
			}
		}
		return webServiceMap;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getTimesZoneList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getTimeZones", user.getUserId());
		List<TimeZones> zoneNames = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> timeZoneResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			zoneNames = mapper.convertValue(timeZoneResponse, new TypeReference<List<TimeZones>>() {
			});

		} else {
			zoneNames = new ArrayList<>();
		}

		Map<Object, Object> zonesMap = new LinkedHashMap<>();
		for (TimeZones timeZone : zoneNames) {
			zonesMap.put(timeZone.getZoneName(), timeZone.getZoneNameDisplay());
		}

		return zonesMap;
	}
 
	public List<Package> getScheduledPackageList(HttpServletRequest request){
		
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse packageListDataResponse = restUtilities.getRestObject(request, "/getAllUserPackages", user.getUserId());
		List<Package> userPackageList = null;
		if (packageListDataResponse != null && packageListDataResponse.getHasMessages()
				&& packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			userPackageList = mapper.convertValue(packageListDataResponse.getObject(), new TypeReference<List<Package>>() {
			});
		} else {
			userPackageList = new ArrayList<>();
		}

		List<Package> scheduledPackageList = new ArrayList<>();
		for (Package userPackage : userPackageList) {
			if (!userPackage.getIsStandard() && userPackage.getIsScheduled()) {
				scheduledPackageList.add(userPackage);
			}
		}
		return scheduledPackageList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ClientDataSources> getDataSourceList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<ClientDataSources> dataSourceList = null;

		dataSourceList = new ArrayList<>();
		DataResponse databseSourceDataResponse = restUtilities.getRestObject(request, "/getDataSourceList", user.getUserId());
		List<LinkedHashMap<String, Object>> dataSources = null;
		if (databseSourceDataResponse != null && databseSourceDataResponse.getHasMessages()
				&& databseSourceDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			dataSources = (List<LinkedHashMap<String, Object>>) databseSourceDataResponse.getObject();
		} else {
			dataSources = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : dataSources) {
			ClientDataSources dataSource = new ClientDataSources();
			dataSource.setDataSourceName(map.get("dataSourceName").toString());
			dataSourceList.add(dataSource);
		}

		return dataSourceList;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ddLayout", method = RequestMethod.GET)
	public ModelAndView ddLayout(HttpServletRequest request, HttpServletResponse response,  HttpSession session,ModelAndView mv,Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		List<DDLayout> ddLayoutTablesList = null;
		if (user != null) {
			DataResponse ddLayoutListDataResponse = null;
			CommonUtils.setActiveScreenName("ddLayout", session);
			ddLayoutListDataResponse = restUtilities.getRestObject(request, "/getClientDDlTables", user.getUserId());
			if (ddLayoutListDataResponse != null && ddLayoutListDataResponse.getHasMessages()) {
				if (ddLayoutListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ddLayoutTablesList = (List<DDLayout>) ddLayoutListDataResponse.getObject();
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", ddLayoutListDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.message.error.text.unabletogetddlayouttables", null, locale));
			}
			mv.addObject("ddLayoutTablesList", ddLayoutTablesList);
			mv.setViewName("tiles-anvizent-entry:ddLayout");
		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/easyQueryBuilderForDDLayout", method = RequestMethod.GET)
	public ModelAndView easyQueryBuilderForStandardPackage(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, ModelAndView mv,
			 Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
             if(user != null){
			
			DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", user.getUserId());
			
			if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages() && ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				   LinkedHashMap<String, Object>  clientDbDetails = ( LinkedHashMap<String, Object> )ddLayoutDataResponse.getObject();
				   if(clientDbDetails != null){
				   ILConnection ilConnection = new ILConnection();
				   List<String> schemaList  = new ArrayList<>();
				   schemaList.add(clientDbDetails.get("clientdb_schema").toString());
				   Database database = new Database();
				   ilConnection.setConnectionName("MYSQL");
				   ilConnection.setDataSourceName("MYSQL");
				   ilConnection.setConnectionId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
				   database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
			       ilConnection.setServer(clientDbDetails.get("region_hostname").toString()+":"+clientDbDetails.get("region_port").toString()+"/"+clientDbDetails.get("clientdb_schema").toString());
				   ilConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
				   ilConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
				   database.setSchema(clientDbDetails.get("clientdb_schema").toString());
				   database.setSchemas(schemaList);
				   ilConnection.setDatabase(database);
				   mv.addObject("iLConnection", ilConnection);
				   }
			   }
			 
			mv.addObject("isDDlayout", true);
			mv.addObject("isCustom", false);
			mv.addObject("isApisData", false);
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.setViewName("tiles-anvizent-entry:easyquerybuilder");
             }
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/scheduledResults/{packageId}", method = RequestMethod.GET)
	public ModelAndView getScheduledResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") int packageId,
			HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("scheduledResults", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		List<ScheduleResult> scheduledResults = null;
		if (user != null) {
			try {
				if (packageId != 0) {
					MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
					maps.add("packageId", packageId);
				DataResponse scheduleResult = restUtilities.postRestObject(request, "/getScheduleStartTime", maps, user.getUserId());
				
				if (scheduleResult != null && scheduleResult.getHasMessages()) {
					if (scheduleResult.getMessages().get(0).getCode().equals("SUCCESS")) {
						scheduledResults  =  (List<ScheduleResult>) scheduleResult.getObject();
						mv.addObject("packageId", packageId);
						mv.addObject("scheduledResults", scheduledResults);
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				}
			}
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
			mv.setViewName("tiles-anvizent-entry:scheduledResults");
		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}
	
	@RequestMapping(value = "/scheduledInfo", method = RequestMethod.POST)
	public ModelAndView getScheduleDetails(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@RequestParam("packageId") int packageId,@RequestParam("scheduleStartTime") String scheduleStartTime,
			HttpSession session, Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
			try {
				if (packageId != 0) {
					MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
					map.add("packageId", packageId);
					map.add("scheduleStartTime", scheduleStartTime);
				DataResponse scheduleResult = restUtilities.postRestObject(request, "/getScheduleDetails", map, user.getUserId());
				
				if (scheduleResult != null && scheduleResult.getHasMessages()) {
					if (scheduleResult.getMessages().get(0).getCode().equals("SUCCESS")) {
						
						mv.addObject("scheduledInfo1", scheduleResult.getObject());
						
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				}
				
			}
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
			mv.setViewName("tiles-anvizent-entry:scheduledResults");
		return mv;
	}
}
