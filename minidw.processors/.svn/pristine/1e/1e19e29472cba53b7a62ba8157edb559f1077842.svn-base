package com.anvizent.minidw.service.utils.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.PackageExecution;
/**
 * @author mahender.alaveni
 * 
 * created date : 15-11-2018
 * 
 * RProcessor is used to execute r file
 *
 */
@Component
public class RProcessor
{
	protected static final Log logger = LogFactory.getLog(RProcessor.class);

	public RProcessor()
	{
		logger.info("In RProcessor...");
	}

	@Autowired
	CommonProcessor commonProcessor;

	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	CrossReferenceProcessor crossReferenceProcessor;

	/**
	 * @param request
	 * @param user
	 * @param packageExecution
	 * @param locale
	 * @param customRequest
	 */
	public void processRJob(BusinessModal businessModal, PackageExecution packageExecution, CustomRequest customRequest)
	{
		if( packageExecution.isJobExecutionRequired() )
		{
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "", packageExecution, customRequest);

			String statusMessage = metaDataFetch.runRBM(businessModal, customRequest);

			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, statusMessage, packageExecution, customRequest);

			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "R Job Execution complted.", packageExecution, customRequest);
		}
	}

}
