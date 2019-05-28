/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rakesh.gajula
 *
 */
@RestController("user_userServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/userProfile")
@CrossOrigin
public class UserServiceDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(UserServiceDataRestController.class);

	@Autowired
	private UserDetailsService userService;

	@Autowired
	private PackageService packageService;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getUserActivationKeyStatus", method = RequestMethod.GET)
	public ResponseEntity<Boolean> getUserActivationKeyStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		LOGGER.info("in getUserActivationKeyStatus()");
		Boolean userKeyStatus = Boolean.FALSE;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			userKeyStatus = userService.getUserActivationKeyStatus(clientId, commonJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			e.printStackTrace();
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<Boolean>(userKeyStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public ResponseEntity<User> getUserDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		LOGGER.info("in getUserDetails()");
		User user = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			e.printStackTrace();
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	/**
	 * 
	 * @param industryId
	 * @return
	 */
	@RequestMapping(value = "/getIndustryById/{industryId}", method = RequestMethod.GET)
	public ResponseEntity<Industry> getIndustryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, HttpServletRequest request) {

		LOGGER.info("in getIndustryById()");
		int id = 0;
		Industry industry = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		if (industryId != null && industryId.matches("[0-9]+")) {
			id = Integer.parseInt(industryId);
		} else {
			return new ResponseEntity<Industry>(industry, HttpStatus.NO_CONTENT);
		}
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			industry = userService.getIndustryById(id, commonJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage("ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return new ResponseEntity<Industry>(industry, HttpStatus.OK);
	}

	/**
	 * 
	 * @param clientId
	 * @param industryId
	 * @return
	 * @throws AnvizentCorewsException
	 */
	@RequestMapping(value = "/getClientSchemaName", method = RequestMethod.GET)
	public ResponseEntity<String> getClientSchemaName(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		LOGGER.info("in getClientSchemaName()");
		String schemaName = "";
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			schemaName = clientDbDetails.get("clientdb_schema").toString();
		} catch (AnvizentCorewsException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(schemaName, HttpStatus.OK);
	}

	@RequestMapping(value = "/isClientValid", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isClientValid(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		Boolean isAuthenticated = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			isAuthenticated = userService.isClientValid(clientId, commonJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			throw new AnvizentRuntimeException(ae);
		} catch (AnvizentCorewsException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage("ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return new ResponseEntity<Boolean>(isAuthenticated, HttpStatus.OK);
	}

	@RequestMapping(value = "/activeClients", method = RequestMethod.POST)
	public DataResponse getActiveClientsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<CloudClient> activeClientsList = null;
		try {
			activeClientsList = userService.getActiveClientsList();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(activeClientsList);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

}
