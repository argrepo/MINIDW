package com.anvizent.minidw.service.utils.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;

@Component
public class ILProcessor
{
	protected static final Log logger = LogFactory.getLog(ILProcessor.class);

	public ILProcessor()
	{
		logger.info("In ILProcessor...");
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
	public void processIL(DLInfo dlInfo, PackageExecution packageExecution, CustomRequest customRequest)
	{
		List<PackageExecutorMappingInfo> packageExecutorMappingInfoList = null;
		Set<String> totalTablesSet = new HashSet<String>();

		if( packageExecution.isJobExecutionRequired() )
		{

			packageExecutorMappingInfoList = metaDataFetch.getPackageExecutorSourceMappingInfoList(packageExecution, customRequest);

			if( packageExecutorMappingInfoList != null && !packageExecutorMappingInfoList.isEmpty() )
			{
				for (PackageExecutorMappingInfo packageExecutorMappingInfo : packageExecutorMappingInfoList)
				{
					metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "Execution started with mapping id " + packageExecutorMappingInfo.getId(), packageExecution, customRequest);
					packageExecution.setIlId(packageExecutorMappingInfo.getPackageExecution().getIlId());
					packageExecutorMappingInfo.setPackageExecution(packageExecution);
					String tableName = metaDataFetch.runIl(packageExecutorMappingInfo, customRequest);
					totalTablesSet.add(tableName);
					metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "Execution complted for mapping id " + packageExecutorMappingInfo.getId(), packageExecution, customRequest);

					try
					{
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, " Cross-Reference exceution started for ilId " + packageExecutorMappingInfo.getPackageExecution().getIlId(), packageExecution, customRequest);
						crossReferenceProcessor.crossReferencesByIlId(packageExecution, packageExecutorMappingInfo.getPackageExecution().getIlId(), customRequest);
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, " Cross-Reference exceution completed for ilId " + packageExecutorMappingInfo.getPackageExecution().getIlId(), packageExecution, customRequest);
					}
					catch ( Throwable e )
					{
						String status = String.format(" \n Cross-Reference execution failed for IL id: %d ", packageExecutorMappingInfo.getPackageExecution().getIlId());
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, status + e, packageExecution, customRequest);
					}
				}
			}
			else
			{
				throw new UploadandExecutionFailedException("\n IL execution failed.Unable to get executor mapping info list.");
			}

		}
	}

}
