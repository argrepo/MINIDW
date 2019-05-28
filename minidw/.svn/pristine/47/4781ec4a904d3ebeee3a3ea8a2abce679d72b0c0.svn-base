package com.datamodel.anvizent.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/adt/hierarchical")
public class HierarchicalController {
	protected static final Log log = LogFactory.getLog(HierarchicalController.class);
	
	@Autowired
	@Qualifier("hierarchicalRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	@Autowired
	private MessageSource messageSource;
	
	String homeRedirectUrl = "/adt/hierarchical";

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView hierarchical(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			  ModelAndView mv, Locale locale) {
		mv.setViewName("tiles-anvizent-entry:hierarchicalData");
		return mv;
	}
	
	@RequestMapping(value = "/hierarchicalJobResults/{hierarchicalId}/{hierarchicalName}", method = RequestMethod.GET)
	public ModelAndView hierarchicalJobResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("hierarchicalId") Integer hierarchicalId,
			@PathVariable("hierarchicalName") String hierarchicalName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		if (hierarchicalId != 0 && hierarchicalName != null) {

			jobResultForm.setHierarchyId(hierarchicalId);
			jobResultForm.setHierarchyName(hierarchicalName);

			DataResponse dataResponse = restUtilities.getRestObject(request, "/hierarchicalJobResults/{hierarchicalId}/{hierarchicalName}", user.getUserId(),
					hierarchicalId, hierarchicalName);
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {
				mv.addObject("viewResultList", dataResponse.getObject());
			}
			mv.setViewName("tiles-anvizent-entry:viewResults");
			jobResultForm.setPageMode("hierarchical");
		} else {
			mv.setViewName(homeRedirectUrl);
		}
		return mv;
	}
	
	@RequestMapping(value = "/hierarchicalJobResults/{hierarchicalId}/{hierarchicalName}", method = RequestMethod.POST)
	public ModelAndView searchJobResultByDate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("hierarchicalId") Integer hierarchicalId,
			@PathVariable("hierarchicalName") String hierarchicalName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = restUtilities.getRestObject(request,
					"/hierarchicalJobResultsByDate/{ilId}/{hierarchicalName}/{fromDate}/{toDate}", user.getUserId(), hierarchicalId, hierarchicalName, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

			jobResultForm.setPageMode("hierarchical");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			log.error("",e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}

	
	
}
