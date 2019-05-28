package com.datamodel.anvizent.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.QueryBuilderForm;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author srinu.chinka
 *
 */

@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/package")

public class QueryBuilderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuilderController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryBuilder/{packageId}/{industryId}", method = RequestMethod.GET)
	public ModelAndView getCustomTempTables(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			@ModelAttribute("queryBuilderForm") QueryBuilderForm queryBuilderForm, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Package userPackage = null;
		LOGGER.info(" packageId : {}, User id :  {} ", packageId, user.getUserId());

		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/getCustomTempTables/" + packageId, Map.class, user.getUserId());

			List<?> list = (List<?>) dataResponse.get("object");

			List<Table> tables = mapper.convertValue(list, new TypeReference<List<Table>>() {
			});

			LOGGER.info("Tables : {} ", tables);

			mv.addObject("customTempTables", tables);

			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper1.convertValue(map, new TypeReference<Package>() {
				});
				mv.addObject("userPackage", userPackage);
			}
			

			queryBuilderForm.setIndustryId(userPackage.getIndustry().getId() + "");
			queryBuilderForm.setPackageId(packageId);
			queryBuilderForm.setIsstaging(true);
			queryBuilderForm.setAdvanced(userPackage.getIsAdvanced());
			if (userPackage.getTable() != null && StringUtils.isNotBlank(userPackage.getTable().getTableName())) {
				
				DataResponse targetTableQueryResponse = restUtilities.getRestObject(request, "/getTargetTableQuery/{packageId}", user.getUserId(), packageId);
				
				String targetTableQuery = (String) targetTableQueryResponse.getObject();
				
				mv.addObject("targetTableName", userPackage.getTable().getTableName());
				if (targetTableQuery != null) {
					mv.addObject("targetTableQuery", targetTableQuery);
				}
				
			}
			mv.addObject("isFromDerivedTables", userPackage.getIsClientDbTables());
			mv.setViewName("tiles-anvizent-entry:queryBuilder");

		} catch (Exception e) {
			LOGGER.error("Error while getting the custom temp tables data from the rest service ", e);

			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.errorWhileGettingCustTempTablesDataFromRestService", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/derivedTableQueryBuilder/{packageId}/{industryId}", method = RequestMethod.GET)
	public ModelAndView derivedTableQueryBuilder(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId,
			@ModelAttribute("queryBuilderForm") QueryBuilderForm queryBuilderForm, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Package userPackage = null;
		LOGGER.info(" packageId : {}, User id :  {} ", packageId, user.getUserId());

		try {

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/derivedTableMappingInfo/" + packageId + "/" + industryId, Map.class,
					user.getUserId());

			List<?> list = (List<?>) dataResponse.get("object");

			List<Table> tables = mapper.convertValue(list, new TypeReference<List<Table>>() {
			});

			LOGGER.info("Tables : {} ", tables);

			mv.addObject("customTempTables", tables);

			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper1.convertValue(map, new TypeReference<Package>() {
				});
				mv.addObject("userPackage", userPackage);
			}

			queryBuilderForm.setIndustryId(userPackage.getIndustry().getId() + "");
			queryBuilderForm.setPackageId(packageId);
			queryBuilderForm.setIsstaging(false);

			mv.setViewName("tiles-anvizent-entry:queryBuilder");

		} catch (Exception e) {
			LOGGER.error("Error while getting the custom temp tables data from the rest service ", e);
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.errorWhileGettingCustTempTablesDataFromRestService", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customTempTablesForWebservice/{packageId}/{industryId}/{ilId}/{webserviceconid}/{dlId}", method = RequestMethod.GET)
	public ModelAndView getCustomTempTablesForWebservice(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, @PathVariable("ilId") Integer ilId,
			@PathVariable("webserviceconid") Integer webserviceconid, @PathVariable("dlId") Integer dlId,
			@ModelAttribute("queryBuilderForm") QueryBuilderForm queryBuilderForm, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Package userPackage = null;
		LOGGER.info(" packageId : {}, User id :  {} ", packageId, user.getUserId());

		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> dataResponse = restUtilities.getRestObject(request, "/getCustomTempTablesForWebservice/" + packageId + "/" + ilId, Map.class,
					user.getUserId());

			List<?> list = (List<?>) dataResponse.get("object");

			List<Table> tables = mapper.convertValue(list, new TypeReference<List<Table>>() {
			});

			mv.addObject("customTempTables", tables);

			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper1.convertValue(map, new TypeReference<Package>() {
				});
				mv.addObject("userPackage", userPackage);
			}

			queryBuilderForm.setIndustryId(userPackage.getIndustry().getId() + "");
			queryBuilderForm.setPackageId(packageId);
			queryBuilderForm.setIsstaging(true);

			mv.addObject("packageId", packageId);
			mv.addObject("webserviceconid", webserviceconid);
			mv.addObject("ilId", ilId);
			mv.addObject("dlId", dlId);
			mv.setViewName("tiles-anvizent-entry:queryBuilder");
			mv.addObject("isFromWebServiceJoin", true);

		} catch (Exception e) {
			LOGGER.error("Error while getting the custom temp tables data from the rest service ", e);

			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.errorWhileGettingCustTempTablesDataFromRestService", null, locale));
			e.printStackTrace();
		}

		return mv;
	}
}
