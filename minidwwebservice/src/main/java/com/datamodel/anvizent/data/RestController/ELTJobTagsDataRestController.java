package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.ELTJobTagsService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;

@RestController("ELTServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/eltconfig")
@CrossOrigin
public class ELTJobTagsDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(ELTJobTagsDataRestController.class);

	
	@Autowired
	MessageSource messageSource;
	@Autowired
	ELTJobTagsService eltService;
	
	@Autowired
	FileService fileService;

	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	
	@RequestMapping(value = "/eltJobTag", method = RequestMethod.GET)
	public DataResponse getEltJobsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<EltJobTagInfo> eltJobsList = null;
		try {
			eltJobsList = eltService.getEltJobTagList(commonJdbcTemplate);
			if (eltJobsList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(eltJobsList);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Unable to fetch data");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/eltJobTag/{tagId}", method = RequestMethod.GET)
	public DataResponse getEltJobInfoByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("tagId") long tagId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		EltJobTagInfo eltJobInfo = null;
		try {
			eltJobInfo = eltService.getEltJobTagInfoById(tagId, commonJdbcTemplate);
			if (eltJobInfo != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(eltJobInfo);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Job tag info not found");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/eltJobTag/{tagId}", method = RequestMethod.DELETE)
	public DataResponse deleteEltJobInfoByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("tagId") long tagId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			eltService.deleteEltJobTagInfo(tagId, commonJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveEltJobTagInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobInfoByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltJobTagInfo eltJobInfo,HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		int id = 0;
		try {
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eltJobInfo.setModification(modification);
			
			id = eltService.saveEltJobTagInfo(eltJobInfo, commonJdbcTemplate);
			if (id > 0 ) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(id);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Job tag info updation failed");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	

	@RequestMapping(value = "/eltJobTag/job", method = RequestMethod.GET)
	public DataResponse getEltJobTagJobsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<EltJobTagInfo> eltJobsList = null;
		try {
			eltJobsList = eltService.getEltJobTagList(commonJdbcTemplate);
			if (eltJobsList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(eltJobsList);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Unable to fetch data");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveEltJobMappingInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltJobInfo eltJobInfo,HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		int mondifiedCount = 0;
		try {
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eltJobInfo.setModification(modification);
			
			mondifiedCount = eltService.saveEltJobMappingInfo(eltJobInfo, commonJdbcTemplate);
			if (mondifiedCount > 0 ) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(eltJobInfo);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Job tag info updation failed");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteDerivedByMappingId/{derivedId}", method = RequestMethod.GET)
	public DataResponse getEltJobMappingInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("derivedId") long derivedId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			eltService.deleteDerivedByMappingId(derivedId, commonJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/saveEltJobSequenceInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobSequenceInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<EltJobInfo> eltJobInfo, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int create = 0;
		try {
			create = eltService.updateEltJobSequenceInfo(eltJobInfo, commonJdbcTemplate);
			if (create != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
}
