package com.anvizent.packagerunner.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

@RestController
@RequestMapping("/")
public class IndexController {
	private static Log log = LogFactory.getLog(IndexController.class);
	@RequestMapping(value ="/", method = RequestMethod.GET)
	public DataResponse getIndexPage() {
		log.info("in method origin");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		message.setText("Package runner successfully running");
		return dataResponse;
	}

}