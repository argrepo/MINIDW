package com.datamodel.anvizent.controller.errorController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
/**
 * 
 * @author rakesh.gajula
 *
 */

@ControllerAdvice
public class AnvizentRestServiceExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource  messageSource;
	
	private static final Log LOGGER = LogFactory.getLog(AnvizentRestServiceExceptionHandler.class);
   /**
    * 
    * @param req
    * @param ex
    * @return
    */
	@ExceptionHandler(AnvizentRuntimeException.class)
    ResponseEntity<Object> handleRestControllerException(WebRequest req, RuntimeException ex,Locale locale) {
		
		LOGGER.info("in AnvizentRestServiceExceptionHandler from "+req);
    	DataResponse dataResponse = new DataResponse();
    	Message message = new Message();
    	message.setCode(messageSource.getMessage("anvizent.message.error.500.code", null, null));
    	message.setText(messageSource.getMessage("anvizent.message.error.500.text.internalServerOccuredPleaseContactSystemAdministrator", null, null));
    	List<Message> messages = new ArrayList<>();
    	messages.add(message);
    	dataResponse.setMessages(messages);
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
            return handleExceptionInternal(ex, dataResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }
	@ExceptionHandler(AnvizentDuplicateFileNameException.class)
	ResponseEntity<DataResponse> handleDuplicateNameException(WebRequest req, RuntimeException ex,Locale locale){
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		if(ex.getMessage().equals(messageSource.getMessage("anvizent.message.error.duplicatePackageName.code", null, null))){
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicatePackageName.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.validation.text.packageNameAlreadyExist", null, null));
		}
		if(ex.getMessage().equals(messageSource.getMessage("anvizent.message.error.duplicateConnectionName.code", null, null))){
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicateConnectionName.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.validation.text.connectionNameAlreadyExist", null, null));
		}
		if(ex.getMessage().equals(messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null))){
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicateTargetTableName.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.validation.text.targetTableAlreadyExist", null, null));
		}
		if(ex.getMessage().equals(messageSource.getMessage("anvizent.message.error.duplicateFileName.code", null, null))){
			message.setCode(messageSource.getMessage("anvizent.message.error.duplicateFileName.code", null, null));
			message.setText(messageSource.getMessage("anvizent.message.validation.text.fileNameAlreadyExistForThisPackage", null, null));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return new ResponseEntity<DataResponse>(dataResponse, HttpStatus.OK);
	}
	
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        if(status.value()==404){
	        responseBody.put("path",request.getDescription(false));
	        responseBody.put("message",status.getReasonPhrase());
        }
        return new ResponseEntity<Object>(responseBody,status);
    }
}
