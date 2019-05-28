package com.datamodel.anvizent.controller;

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
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ELTClusterLogsInfo;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/package")
public class PackageExecutionController {

	protected static final Log LOGGER = LogFactory.getLog(PackageExecutionController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	
	@Autowired
	private MessageSource messageSource;

 
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/packageExecute/{packageId}", method = RequestMethod.GET)
	public ModelAndView packageExecute(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") int packageId,@RequestParam(value="packageName",required=false) String packageName,Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<PackageExecution> packageExecutionList = null;
		try {
			if (packageId != 0) {
				MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
				maps.add("packageId", packageId);
			
			DataResponse packageExecutionDataResponse = null;
			packageExecutionDataResponse = restUtilities.postRestObject(request, "/getPackageExecution",maps, user.getUserId());
			if (packageExecutionDataResponse != null && packageExecutionDataResponse.getHasMessages()) {
				if (packageExecutionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					packageExecutionList = (List<PackageExecution>) packageExecutionDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<?> l = (List<Map<String, Object>>) packageExecutionDataResponse.getObject();
					packageExecutionList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					String timeZone = (String)session.getAttribute(Constants.Config.TIME_ZONE);
					for ( PackageExecution packageExecution: packageExecutionList ) {
						packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
						packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));
						
						packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
						packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
						packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
						packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
					}
					
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("packageId", packageId);
			mv.addObject("packageName", packageName);
			mv.addObject("packageExecutionList", packageExecutionList);
			mv.setViewName("tiles-anvizent-entry:packageExecution");
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/packageExecutionByPagination/{packageId}", method = RequestMethod.GET)
	public ModelAndView packageExecutionByPagination(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") int packageId,@RequestParam(value="packageName",required=false) String packageName,
			@RequestParam(value="offset") int offset,
			@RequestParam(value="limit") int limit,
			Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<PackageExecution> packageExecutionList = null;
		int count=0;
		try {
			if (packageId != 0) {
				
				MultiValueMap<Object, Object> countmap = new LinkedMultiValueMap<>();
				countmap.add("packageId", packageId);
				
				DataResponse packageExecutionCountDataResponse = null;
				packageExecutionCountDataResponse = restUtilities.postRestObject(request, "/getPackageExecutionCount",countmap, user.getUserId());
				if (packageExecutionCountDataResponse != null && packageExecutionCountDataResponse.getHasMessages()) {
					if (packageExecutionCountDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						count =  (int) packageExecutionCountDataResponse.getObject();
					} else {
						mv.addObject("messagecode", "FAILED");
						mv.addObject("errors", packageExecutionCountDataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
				int totalCount = 0;
				int paginationCount = count/10;
				int remainingRecords = count - paginationCount*10;
				
				if(remainingRecords == 0){
					totalCount = paginationCount;
				}else{
					totalCount = paginationCount+1;
				}
				
				MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
				maps.add("packageId", packageId);
				maps.add("offset", offset);
				maps.add("limit", limit);
			
			DataResponse packageExecutionDataResponse = null;
			packageExecutionDataResponse = restUtilities.postRestObject(request, "/getPackageExecutionByPagination",maps, user.getUserId());
			if (packageExecutionDataResponse != null && packageExecutionDataResponse.getHasMessages()) {
				if (packageExecutionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					packageExecutionList = (List<PackageExecution>) packageExecutionDataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<?> l = (List<Map<String, Object>>) packageExecutionDataResponse.getObject();
					packageExecutionList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					String timeZone = (String)session.getAttribute(Constants.Config.TIME_ZONE);
					for ( PackageExecution packageExecution: packageExecutionList ) {
						packageExecution.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getUploadStartDate(), timeZone));
						packageExecution.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastUploadedDate(), timeZone));
						
						packageExecution.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getExecutionStartDate(), timeZone));
						packageExecution.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getLastExecutedDate(), timeZone));
						packageExecution.setDruidStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidStartDate(), timeZone));
						packageExecution.setDruidEndDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageExecution.getDruidEndDate(), timeZone));
					}
					
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			mv.addObject("packageId", packageId);
			mv.addObject("packageName", packageName);
			mv.addObject("packageExecutionList", packageExecutionList);
			mv.addObject("totalCount", totalCount == 0 ? 1 : totalCount);
			mv.setViewName("tiles-anvizent-entry:packageExecution");
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/executionInfo/{executionId}/{uploadOrExecution}", method = RequestMethod.GET)
	public ModelAndView executionInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelAndView mv,
			@PathVariable("executionId") int executionId,@PathVariable("uploadOrExecution") String uploadOrExecution,Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<ELTClusterLogsInfo> viewEltInitateList = null;
		DataResponse eltLogsDataResponse = null;
		long initiateId = 0;
		try {
			if (executionId != 0) {
				MultiValueMap<Object, Object> maps = new LinkedMultiValueMap<>();
				maps.add("executionId", executionId);
				maps.add("uploadOrExecution", uploadOrExecution);
			
			DataResponse packageExecutionDataResponse = null;
			packageExecutionDataResponse = restUtilities.postRestObject(request, "/getUploadAndExecutionStatusComments",maps, user.getUserId());
			
			/*MultiValueMap<Object, Object> mapId = new LinkedMultiValueMap<>();
			mapId.add("executionId", executionId);*/
			StringBuilder errorLogsLinks = new StringBuilder();
			
			DataResponse eltIntiateResponse = restUtilities.getRestObject(request, "/getELTIntiateResponse/{executionId}", user.getUserId(),executionId);
			if (eltIntiateResponse != null && eltIntiateResponse.getHasMessages()) {
				if (eltIntiateResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<?> l = (List<Map<String, Object>>) eltIntiateResponse.getObject();

					viewEltInitateList = mapper.convertValue(l, new TypeReference<List<ELTClusterLogsInfo>>() {
					});
					if(viewEltInitateList.size() > 0)
						{
							errorLogsLinks.append("\n");
							errorLogsLinks.append("ELT Error Logs ");
							for (ELTClusterLogsInfo eltInitiateInfo : viewEltInitateList)
							{
								if( eltInitiateInfo.getDlId() != 0 || eltInitiateInfo.getIlId() != 0 )
								{
									if( eltInitiateInfo.getEltInitiateId() != 0 )
									{
										initiateId = eltInitiateInfo.getEltInitiateId();
										eltLogsDataResponse = restUtilities.getRestObject(request, "/eltLogsDataResponse/{initiateId}", user.getUserId(), initiateId);
										ArrayList<String> logValues = null;
										if( eltLogsDataResponse != null )
										{
											LinkedHashMap<String, Object> results = (LinkedHashMap<String, Object>) eltLogsDataResponse.getObject();
											logValues = (ArrayList<String>) results.get("data");
											if( eltInitiateInfo.getDlId() != 0 ) errorLogsLinks.append("\n");
											errorLogsLinks.append("ELT Logs For DLID - " + eltInitiateInfo.getDlId());
											if( eltInitiateInfo.getIlId() != 0 ) errorLogsLinks.append(" ILID - " + eltInitiateInfo.getIlId());
											for (String link : logValues)
											{
												errorLogsLinks.append("\n" + "<a target=\"_blank\" href=" + link + ">" + link + "</a>");
											}

										}
										else
										{
											mv.addObject("messagecode", "FAILED");
											mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
										}
									}
								}
							}
						}
				}
			}
			
			
			if (packageExecutionDataResponse != null && packageExecutionDataResponse.getHasMessages()) {
				if (packageExecutionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("packageExecutionResponse", packageExecutionDataResponse.getObject()+"\n"+errorLogsLinks);
					
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", packageExecutionDataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			
			mv.setViewName("packageExecutionInfo");
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

}	