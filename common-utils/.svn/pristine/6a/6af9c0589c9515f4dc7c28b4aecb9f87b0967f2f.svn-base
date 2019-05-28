package com.datamodel.anvizent.errorhandler;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;

public class MyResponseErrorHandler implements ResponseErrorHandler {
	private static final Log LOGGER = LogFactory.getLog(MyResponseErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    	LOGGER.error("Response error: Status code : "+response.getStatusCode()+" Status Text : "+ response.getStatusText() );
    	throw new AnvizentRuntimeException(response.getStatusCode()+"");
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }
}
