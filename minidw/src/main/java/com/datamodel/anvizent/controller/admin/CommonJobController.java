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
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CommonJob;
import com.datamodel.anvizent.service.model.CommonJobForm;
import com.datamodel.anvizent.service.model.DataResponse;
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
@RequestMapping(value = "/admin/commonJob")
public class CommonJobController {
	protected static final Log LOGGER = LogFactory.getLog(CommonJobController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;
	

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView commonJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("commonJobForm") CommonJobForm commonJobForm, Locale locale) {
		LOGGER.debug("in commonJob()");
		CommonUtils.setActiveScreenName("commonJob", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getCommonJobInfo", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("commonJobdetails", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("commonJobdetails", dataResponse.getObject());
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
		commonJobForm.setPageMode("list");
		mv.setViewName("tiles-anvizent-admin:commonJob");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addCommonJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("commonJobForm") CommonJobForm commonJobForm, Locale locale) {
		LOGGER.debug("in addCommonJob()");
		commonJobForm.setPageMode("add");
		commonJobForm.setIsActive(true);
		mv.setViewName("tiles-anvizent-admin:commonJob");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveCommonJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("commonJobForm") CommonJobForm commonJobForm, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in saveCommonJob()");

		if (commonJobForm.getJobType() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.validation.text.jobTypeCannotBeEmpty", null, locale));
			mv.setViewName("redirect:/admin/commonJob");
			return mv;
		}
		try {
			Boolean isUploaded = false;
			String tempFileName = null;
			String tempFolderName = null;
			User user = CommonUtils.getUserDetails(request, null, null);
			MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();

			tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
			if (commonJobForm.getJobFile() != null) {
				tempFileName = tempFolderName + commonJobForm.getJobFile().getOriginalFilename();
				CommonUtils.createFile(tempFolderName, tempFileName, commonJobForm.getJobFile());
				filesMap.add("files", new FileSystemResource(tempFileName));
				filesMap.add("isCommonJob","yes");

				DataResponse uploadedFileResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", filesMap, user.getUserId());
				if (uploadedFileResponse != null && uploadedFileResponse.getHasMessages() && uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					isUploaded = true;
				}
			}
			if (isUploaded) {
				CommonJob commonJob = new CommonJob();
				commonJob.setJobType(commonJobForm.getJobType());
				commonJob.setJobFileName(commonJobForm.getJobFile().getOriginalFilename());
				commonJob.setIsActive(commonJobForm.getIsActive());
				DataResponse dataResponse = restUtilities.postRestObject(request, "/saveCommonJobInfo", commonJob, user.getUserId());
				if (dataResponse != null && dataResponse.getHasMessages()) {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.failedToUploadJobFile", null, locale));
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/commonJob");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editCommonJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("commonJobForm") CommonJobForm commonJobForm, Locale locale) {
		LOGGER.debug("in editCommonJob()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getCommonJobInfoById/{id}", user.getUserId(), commonJobForm.getId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) dataResponse.getObject();
					CommonJob commonJob = mapper.convertValue(data, new TypeReference<CommonJob>() {
					});
					commonJobForm.setId(commonJob.getId());
					commonJobForm.setJobType(commonJob.getJobType());
					commonJobForm.setJobFileName(commonJob.getJobFileName());
					commonJobForm.setIsActive(commonJob.getIsActive());
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
		commonJobForm.setPageMode("edit");
		mv.setViewName("tiles-anvizent-admin:commonJob");
		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateCommonJob(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("commonJobForm") CommonJobForm commonJobForm, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in updateCommonJob()");

		if (commonJobForm.getJobType() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.validation.text.jobTypeCannotBeEmpty", null, locale));
			mv.setViewName("redirect:/admin/commonJob");
			return mv;
		}
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			CommonJob commonJob = new CommonJob();
			commonJob.setId(commonJobForm.getId());
			commonJob.setJobType(commonJobForm.getJobType());
			commonJob.setIsActive(commonJobForm.getIsActive());
			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateCommonJobInfo", commonJob, user.getUserId());
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
		mv.setViewName("redirect:/admin/commonJob");
		return mv;
	}

	@RequestMapping(value = { "/edit", "/update", "/save" }, method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/commonJob");
		return mv;
	}

}
