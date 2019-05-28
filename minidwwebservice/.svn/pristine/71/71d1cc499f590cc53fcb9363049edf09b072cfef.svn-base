package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
@RestController("CommmonDataRestController")
@RequestMapping(Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL)
@CrossOrigin
public class CommmonDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(CommmonDataRestController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	FileService fileService;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	
	@RequestMapping(value = "/downloadNewMinidwVersion", method = RequestMethod.GET)
	public DataResponse downloadNewMinidwVersion(HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		LOGGER.info("in downloadNewMinidwVersion()");
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		DataResponse dataResponse = new DataResponse();
		boolean isDownloaded = CommonUtils.downloadNewMinidwVersion();
		if (isDownloaded) {
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileDownloadingMinidwLatestVersion!", null, locale));
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
}
