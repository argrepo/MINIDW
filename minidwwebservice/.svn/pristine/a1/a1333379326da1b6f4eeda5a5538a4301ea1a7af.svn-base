package com.datamodel.anvizent.data.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

@RestController("datApiV1RestController")
@RequestMapping("/dataapi/v1")
@CrossOrigin(origins = "*")
public class DataApiV1RestController {

	private static final Log logger = LogFactory.getLog(DataApiV1RestController.class);

	@Autowired
	PackageService packageService;
	@Autowired
	UserDetailsService userService;
	
	@RequestMapping(value = "/apiList", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> apiList( HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		HttpStatus httpStatus = HttpStatus.OK;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<Map<String, String>> apisDetails = new ArrayList<>();
		try {
			String clientId = getCLientFromRequest(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			List<ApisDataInfo> apisDetailList = packageService.getApisDetails(clientAppDbJdbcTemplate);
			for (ApisDataInfo apisDetail : apisDetailList) {
				if ( apisDetail.getActive() ) {
					Map<String, String> apiInfo = new HashMap<>();
					apiInfo.put("apiName", apisDetail.getApiName());
					apiInfo.put("endPointUri", StringUtils.substringBeforeLast(request.getRequestURL().toString(), "/") + "/" + EncryptionServiceImpl.getInstance().encrypt(apisDetail.getEndPointUrl()));
					apiInfo.put("methodType", apisDetail.getMethodType().toUpperCase());
					apiInfo.put("desciption", apisDetail.getApiDescription());
					apisDetails.add(apiInfo);
				}
			}
			if (apisDetails.size() == 0) {
				httpStatus = HttpStatus.NO_CONTENT;
			}
			
		} catch (Exception e) {
			httpStatus = HttpStatus.BAD_REQUEST;
			getUnAuthorisedResponse(response, MinidwServiceUtil.getErrorMessageString(e), httpStatus);
		}

		return new ResponseEntity<>(apisDetails, httpStatus);
	}

	
	@RequestMapping(value = "/{apiName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getRequest(
			@PathVariable(value = "apiName") String apiName,
			@RequestParam(value = "offset", required=false) Integer offset,
			@RequestParam(value = "limit", required=false) Integer limit,
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		return getRequestData(apiName, offset, limit, "get", response, request);
	}

	@RequestMapping(value = "/{apiName}", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> postRequest(@PathVariable(value = "apiName") String apiName,
			@RequestParam(value = "offset", required=false) Integer offset,
			@RequestParam(value = "limit", required=false) Integer limit,
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		return getRequestData(apiName, offset, limit, "post", response, request);
	}
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@RequestMapping(value = "/{apiName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> putRequest(@PathVariable(value = "apiName") String apiName,
			@RequestParam(value = "offset", required=false) Integer offset,
			@RequestParam(value = "limit", required=false) Integer limit,
			HttpServletRequest request, HttpServletResponse response, Locale locale) {
		return getRequestData(apiName, offset, limit, "put", response, request);
	}
	
	
	private ResponseEntity<Map<String, Object>> getRequestData(String apiName,Integer offset,Integer limit,String requestedMethodType,HttpServletResponse response,HttpServletRequest request) {

		Map<String, Object> mData = new HashMap<>();
		mData.put("data", new ArrayList<>());
		HttpStatus httpStatus = HttpStatus.OK;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			if ( limit == null || limit < 1 || limit > 1000) {
				limit = 1000;
			}
			if ( offset == null || offset < 0 ) {
				offset = 0;
			}
			String endPointName = decryptedEndPointName(apiName);
			if ( endPointName == "") {
				httpStatus = HttpStatus.BAD_REQUEST;
				mData.put("message", "Invalid end point");
				return new ResponseEntity<>(mData, httpStatus);
			}
			String clientId = getCLientFromRequest(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ApisDataInfo apisDataInfo = packageService.getApistDetailsByEndPointName(endPointName, clientAppDbJdbcTemplate);
			
			if(apisDataInfo == null) {
				httpStatus = HttpStatus.NOT_FOUND;
				mData.put("message", "Api details not found");
				return new ResponseEntity<>(mData, httpStatus);
			} else {
				String methodType = apisDataInfo.getMethodType();
				String apiQuery = apisDataInfo.getApiQuery();
				
				if ( !methodType.toLowerCase().equals(requestedMethodType) ) {
					httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
					mData.put("message", "Invalid Method type");
					return new ResponseEntity<>(mData, httpStatus);
				} else {
					List<Map<String, Object>> data = getData(clientJdbcInstance.getClientJdbcTemplate(), apiQuery, offset, limit);
					mData.put("data", data);
					if ( data == null || data.size() == 0 ) {
						httpStatus = HttpStatus.OK;
						mData.put("message", "Data not found");
					}
				}
				
				
				
			}
			
		} catch (Exception e) {
			httpStatus = HttpStatus.BAD_REQUEST;
			getUnAuthorisedResponse(response, MinidwServiceUtil.getErrorMessageString(e), httpStatus);
			mData.put("message", MinidwServiceUtil.getErrorMessageString(e));
		}

		return new ResponseEntity<Map<String, Object>>(mData, httpStatus);
	}

	
	public String decryptedEndPointName(String encEndPointName) {
		try {
			return EncryptionServiceImpl.getInstance().decrypt(encEndPointName);
		} catch (Exception e) {
			logger.error("encEndPointName " + encEndPointName,e);
		}
		return "";
	}
	private void getUnAuthorisedResponse(HttpServletResponse response, String errorMsg, HttpStatus httpStatus) {
		try {
			response.getOutputStream().write(errorMsg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setStatus(httpStatus.value());
	}
	
	
	public List<Map<String, Object>> getData(JdbcTemplate jdbcTemplate, String apiQuery,int offset, int limit) {
		
		final String apiQueryWithLimits = "select * from ("+apiQuery+") d limit " + offset + "," + limit;
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(apiQueryWithLimits);
		logger.info(queryForList);
		return queryForList;
	}
	
	public String getCLientFromRequest(HttpServletRequest request) {
		String xAuthCodeHdr = request.getHeader("X-Authentication-code");
		String xAuthCodeReq = request.getParameter("X-Authentication-code");
		return xAuthCodeHdr != null ? xAuthCodeHdr : xAuthCodeReq;
	}

}
