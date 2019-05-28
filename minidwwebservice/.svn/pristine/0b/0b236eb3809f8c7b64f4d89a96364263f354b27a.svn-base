package com.datamodel.anvizent.controller.errorController;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
/**
 * 
 * @author rakesh.gajula
 *
 */
public class CustomRestTemplateResponseErrorHandler implements ResponseErrorHandler {

private static final Log LOGGER = LogFactory.getLog(CustomRestTemplateResponseErrorHandler.class);

@Override
public boolean hasError(ClientHttpResponse response) throws IOException {
	LOGGER.info("in hasError()");
	boolean hasError = false;
	int rawStatusCode = response.getRawStatusCode();
	if (rawStatusCode != 200){
	    hasError = true;
	    }
	return hasError;
}

@Override
public void handleError(ClientHttpResponse response) throws IOException {
	LOGGER.info("in handleError()");
	throw new AnvizentRuntimeException(response.getRawStatusCode() +"custom Error");
}
}
