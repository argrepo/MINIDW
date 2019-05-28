package com.anvizent.minidw.service.utils.processor;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.PackageExecution;

@Component
public class CrossReferenceProcessor
{
	protected static final Log logger = LogFactory.getLog(CrossReferenceProcessor.class);
	
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	MetaDataFetch metaDataFetch;
	
	
	public CrossReferenceProcessor() {
		logger.info("In CrossReferenceProcessor...");
	}
	
	public void crossReferencesByIlId(PackageExecution packageExecution, int ilId, CustomRequest customRequest) {
		List<CrossReferenceLogs> crossReferenceLogs = null;
		try {
			crossReferenceLogs = metaDataFetch.getCrossReferencesByIlId(ilId, customRequest);
			processCrossReferences(packageExecution, crossReferenceLogs, customRequest);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private void processCrossReferences(PackageExecution packageExecution, List<CrossReferenceLogs> crossReferenceLogs, CustomRequest customRequest)
	{
		for(CrossReferenceLogs crossReference: crossReferenceLogs) {
			try {
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "  Execution started for cross reference : " + crossReference.getConditionName(), packageExecution, customRequest);				
					metaDataFetch.processCrossReference(crossReference, customRequest);
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "  Execution completed for cross reference : " + crossReference.getConditionName(), packageExecution, customRequest);
			}catch(Throwable t) {
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, " \n Execution failed for cross reference " + crossReference.getConditionName() + "\n "+t, packageExecution, customRequest);
				throw new RuntimeException("Xref execution failed for cross reference  " + crossReference.getConditionName() + " ilId " + crossReference.getIlId());
			}
		}
	}
	

} 
