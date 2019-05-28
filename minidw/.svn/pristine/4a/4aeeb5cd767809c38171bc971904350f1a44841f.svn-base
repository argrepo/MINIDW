package com.datamodel.anvizent.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.CustomPackageForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.StandardPackageForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Rajesh.Anthari
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adv/package")
public class AdvancedPackageController {

	protected static final Log LOGGER = LogFactory.getLog(AdvancedPackageController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/xReferenceIL", method = RequestMethod.GET)
	public ModelAndView xReferenceIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, BindingResult result, Locale locale) {
		LOGGER.debug("in viewXrefIlSource()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			List<ILInfo> ilsList = null;

			ilsList = restUtilities.getRestObject(request, "/getAllClientILsForXref", List.class, user.getUserId());

			mv.addObject("ilsList", ilsList);

			mv.setViewName("tiles-anvizent-entry:xreferenceIl");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/easyQueryBuilderForXref/{packageId}/{connectionId}/{dLId}/{iLId}/{dbSourceName}", method = RequestMethod.GET)
	public ModelAndView easyQueryBuilderForXref(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("connectionId") int connectionId, @PathVariable("dLId") int dLId,
			@PathVariable("iLId") int iLId, @PathVariable("dbSourceName") String dbSourceName,
			@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, BindingResult result, Locale locale) {
		LOGGER.debug("in easyQueryBuilderForXref()");

		User user = CommonUtils.getUserDetails(request, null, null);
		Database database = null;
		if (user != null) {
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

				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				DataResponse dataiLConnection = restUtilities.getRestObject(request, "/getILsConnectionById/{connectionId}", user.getUserId(), connectionId);
				LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
				ILConnection iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
				});

				// get all schemas based on connection Id
				List<String> schemaList = CommonUtils.getAllSchemasFromDatabase(iLConnection);
				database = iLConnection.getDatabase();
				database.setSchemas(schemaList);
				iLConnection.setDatabase(database);

				iLConnection.setConnectionId(connectionId);
				mv.addObject("iLConnection", iLConnection);
				mv.addObject("isXref", true);
				mv.addObject("dLId", dLId);
				mv.addObject("iLId", iLId);
				mv.addObject("packageId", packageId);
				mv.addObject("dbSourceName", dbSourceName);
				mv.setViewName("tiles-anvizent-entry:easyquerybuilder");

			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		} else {
			mv.setViewName("redirect:/login");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/xReferenceIL/{il_id}", method = RequestMethod.GET)
	public ModelAndView xReferenceIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("standardPackageForm") StandardPackageForm standardPackageForm, @PathVariable("il_id") String il_id, BindingResult result,
			Locale locale) {
		LOGGER.debug("in xReferenceIL()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			List<ILInfo> ilsList = null;

			ilsList = (List<ILInfo>) restUtilities.getRestObject(request, "/getAllClientILsForXref", List.class, user.getUserId());

			mv.addObject("ilsList", ilsList);
			mv.addObject("receivedIlId", il_id);
			mv.setViewName("tiles-anvizent-entry:xreferenceIl");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/getILAndSourceColumnsForXReferenceSplitting/{industryId}/{packageId}/{dL_Id}/{iL_Id}/{mappingId}", method = RequestMethod.GET)
	public ModelAndView getILAndSourceColumnsForXReference(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("industryId") Integer industryId, @PathVariable("packageId") Integer packageId, @PathVariable("dL_Id") Integer dL_Id,
			@PathVariable("iL_Id") Integer iL_Id, @PathVariable("mappingId") String mappingId, Locale locale) {
		LOGGER.info("in getILAndSourceColumnsForXReferenceSplitting()");
		try {

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("industryId", industryId);
			map.add("packageId", packageId);
			map.add("dL_Id", dL_Id);
			map.add("iL_Id", iL_Id);
			map.add("mappingId", mappingId);
			User user = CommonUtils.getUserDetails(request, null, null);

			ILInfo iLInfo = restUtilities.getRestObject(request, "/getILById/{" + iL_Id + "}", ILInfo.class, user.getUserId(), iL_Id);

			DataResponse dataResponse = (DataResponse) restUtilities.postRestObject(request, "/getILAndSourceColumnsForXReference", map, user.getUserId());

			if (dataResponse != null) {
				mv.addObject("iLAndSourceColumns", dataResponse.getObject());
			} else {
				mv.addObject("iLAndSourceColumns", "Source Columns are not available.");
			}

			mv.addObject("industryId", industryId);
			mv.addObject("packageId", packageId);
			mv.addObject("dL_Id", dL_Id);
			mv.addObject("iL_Id", iL_Id);
			mv.addObject("mappingId", mappingId);
			mv.addObject("iLInfo", iLInfo);

			mv.setViewName("tiles-anvizent-entry:xreferenceSplitting");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}
}
