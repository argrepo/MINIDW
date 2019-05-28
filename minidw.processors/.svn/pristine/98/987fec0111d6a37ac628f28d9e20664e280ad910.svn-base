package com.anvizent.minidw.service.utils.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;

@Component
public class StandardPackageProcessor
{

	protected static final Log logger = LogFactory.getLog(StandardPackageProcessor.class);

	@Autowired
	MessageSource messageSource;
	@Autowired
	ILProcessor ilProcessor;
	@Autowired
	DLProcessor dlProcessor;
	@Autowired
	FlatFileProcessor flatFileProcessor;
	@Autowired
	DataBaseProcessor dataBaseProcessor;
	@Autowired
	WebServiceProcessor webServiceProcessor;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	CrossReferenceProcessor crossReferenceProcessor;

	public StandardPackageProcessor()
	{
		logger.info("In StandardPackageProcessor...");
	}

	public StandardPackageExecutor processStandardPackageExecutor(HttpServletRequest request, String encUserId, User user, DLInfo dlInfo, boolean isExecutionrequired)
	{
		CustomRequest customRequest = getCustomRequest(request, user);
		S3BucketInfo s3BucketInfo = metaDataFetch.getS3BucketInfo(customRequest);
		// (S3BucketInfo)  SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
		FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request, Constants.Config.FILE_SETTINGS_INFO);
		return processStandardPackageExecution(user, dlInfo, s3BucketInfo, fileSettings, customRequest, isExecutionrequired);
	}

	public StandardPackageExecutor processStandardPackageExecutor(PackageExecution packageExecution, S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest, String encUserId, User user, DLInfo dlInfo)
	{
		StandardPackageExecutor standardPackageExecutor = new StandardPackageExecutor(this, packageExecution, user, dlInfo, s3BucketInfo, fileSettings, customRequest);
		standardPackageExecutor.start();
		return standardPackageExecutor;
	}

	public void processStandardPackageExecution(PackageExecution packageExecution, User user, DLInfo dlInfo, S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest)
	{
		try
		{
			Package userPackage = metaDataFetch.getPackageDetails(customRequest, null);
			String runType = packageExecution.getRunType();
			packageExecution.setUploadOrExecution("U");
			if( runType.equals(Constants.jobType.IL) || runType.equals(Constants.jobType.ALL) )
			{
				metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, " Source upload started.", packageExecution, customRequest);
				runIlSources(user, s3BucketInfo, fileSettings, customRequest, packageExecution, userPackage, dlInfo);
				metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.COMPLETED, " Source upload Completed.", packageExecution, customRequest);
			}
			packageExecution.setUploadOrExecution("E");
			processILDLExecution(packageExecution, dlInfo, customRequest);
		}
		catch ( Throwable e )
		{
			logger.error("", e);
			if( packageExecution.getUploadOrExecution().equals("U") )
			{
				metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.FAILED, "\n" + e.getMessage(), packageExecution, customRequest);
				throw new PackageExecutionException(e);
			}
			else
			{
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.FAILED, "\n" + e.getMessage(), packageExecution, customRequest);
				throw new PackageExecutionException(e);
			}
		}
	}

	public DLInfo getDlInfoWithValidation(int dlId, PackageExecution packageExecution, CustomRequest customRequest)
	{
		DLInfo dlInfo = metaDataFetch.getSourcesByDlId(dlId, customRequest);
		determinePackageExecution(dlInfo, packageExecution);
		return dlInfo;
	}

	public void determinePackageExecution(DLInfo dlInfo, PackageExecution packageExecution)
	{

		String runType = Constants.jobType.ALL;
		if( dlInfo.getIlList() != null && dlInfo.getIlList().length > 0 )
		{
			runType = Constants.jobType.ALL;
		}
		else if( dlInfo.getIlList().length == 0 )
		{
			runType = Constants.jobType.DL;
			packageExecution.setRunType(runType);
			packageExecution.setUploadStatus(Constants.ExecutionStatus.IGNORED);
			packageExecution.setUploadComments(Constants.ExecutionStatus.IGNORED);
		}
	}

	public void processStandardPackageExecution(PackageExecution packageExecution, User user, int dlId, S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest)
	{
		DLInfo dlInfo = metaDataFetch.getSourcesByDlId(dlId, customRequest);
		processStandardPackageExecution(packageExecution, user, dlInfo, s3BucketInfo, fileSettings, customRequest);
	}

	public List<String> processILDLExecution(PackageExecution packageExecution, DLInfo dlInfo, CustomRequest customRequest)
	{
		String runType = packageExecution.getRunType();
		List<String> tablesList = new ArrayList<>();
		if( packageExecution.isJobExecutionRequired() )
		{
			boolean ilExecuted = false;
			if( runType.equals(Constants.jobType.IL) || runType.equals(Constants.jobType.ALL) )
			{
				ilExecuted = true;
				metaDataFetch.saveExecutionInfo(Constants.ExecutionStatus.STARTED, "\nIL execution started.", packageExecution, customRequest);
				ilProcessor.processIL(dlInfo, packageExecution, customRequest);
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "\nIL execution Completed.", packageExecution, customRequest);
			}
			if( runType.equals(Constants.jobType.DL) || runType.equals(Constants.jobType.ALL) )
			{
				if( ilExecuted )
				{
					metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "\nDL execution started.", packageExecution, customRequest);
				}
				else
				{
					metaDataFetch.saveExecutionInfo(Constants.ExecutionStatus.STARTED, "\nDL execution started.", packageExecution, customRequest);
				}
				tablesList = processDl(packageExecution, dlInfo, customRequest);
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "\nDL execution Completed.", packageExecution, customRequest);
			}
			else
			{
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, "\nDL execution IGNORED.", packageExecution, customRequest);
			}
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.COMPLETED, "", packageExecution, customRequest);
		}
		return tablesList;
	}

	public StandardPackageExecutor processStandardPackageExecution(User user, DLInfo dlInfo, S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest, boolean isExecutionrequired)
	{

		String runType = Constants.jobType.IL;
		if( dlInfo.getIlList().length > 0 && dlInfo.isDlExecutionRequired() )
		{
			runType = Constants.jobType.ALL;
		}
		else if( dlInfo.getIlList().length > 0 )
		{
			runType = Constants.jobType.IL;
		}
		else if( dlInfo.isDlExecutionRequired() )
		{
			runType = Constants.jobType.DL;
		}

		PackageExecution packageExecutionInfo = new PackageExecution();
		packageExecutionInfo.setPackageId(0);
		packageExecutionInfo.setRunType(runType);
		if( dlInfo.getDdlayoutList() != null && dlInfo.getDdlayoutList().size() > 0 )
		{
			List<String> ddlsList = new ArrayList<>();
			for (DDLayout ddl : dlInfo.getDdlayoutList())
			{
				ddlsList.add("" + ddl.getId());
			}
			packageExecutionInfo.setDdlToRun(String.join(",", ddlsList));
		}
		if( runType.equals(Constants.jobType.DL) )
		{
			packageExecutionInfo.setUploadStatus(Constants.ExecutionStatus.IGNORED);
			packageExecutionInfo.setUploadComments(Constants.ExecutionStatus.IGNORED);
		}
		else
		{
			packageExecutionInfo.setUploadStatus(Constants.ExecutionStatus.STARTED);
			packageExecutionInfo.setUploadComments("");
		}
		packageExecutionInfo.setUploadStartDate(commonProcessor.getFormattedDateString());
		packageExecutionInfo.setLastUploadedDate(commonProcessor.getFormattedDateString());
		packageExecutionInfo.setTimeZone(TimeZone.getDefault().getID());
		if( isExecutionrequired )
		{
			packageExecutionInfo.setInitiatedFrom(Constants.ScheduleType.RUNNOW);
		}
		else
		{
			packageExecutionInfo.setInitiatedFrom(Constants.ScheduleType.RUN_WITH_SCHEDULER);
		}
		packageExecutionInfo.setJobExecutionRequired(isExecutionrequired);
		packageExecutionInfo.setDlId(dlInfo.getdL_id());
		metaDataFetch.saveUploadInfo(packageExecutionInfo, customRequest);
		return processStandardPackageExecutor(packageExecutionInfo, s3BucketInfo, fileSettings, customRequest, customRequest.getEncUserId(), user, dlInfo);

	}

	/**
	 * @param request
	 * @param user
	 * @return
	 */
	public CustomRequest getCustomRequest(HttpServletRequest request, User user)
	{
		String browserDetails = CommonUtils.getClientBrowserDetails(request);
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String webServiceContextUrl = (String) SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, user.getClientId(), user.getClientId(), browserDetails, deploymentType, null, user.getUserId());
		return customRequest;
	}

	public void runIlSources(User user, S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest, PackageExecution packageExecution, Package userPackage, DLInfo dlInfo) throws Exception
	{
		try
		{
			List<ILConnectionMapping> ilConnectionMappings = metaDataFetch.getPackageSourceMappingListByIds(customRequest, dlInfo, null);

			if( ilConnectionMappings != null && ilConnectionMappings.size() > 0 )
			{

				String noOfSourcesString = " Total no of sources: " + ilConnectionMappings.size() + "\n";

				metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, noOfSourcesString, packageExecution, customRequest);
				int count = 0;
				for (ILConnectionMapping ilMappingInfo : ilConnectionMappings)
				{

					if( !ilMappingInfo.getIsFlatFile() && !ilMappingInfo.getIsWebservice() )
					{
						dataBaseProcessor.processDatabase(user, customRequest.getDeploymentType(), packageExecution, s3BucketInfo, fileSettings, customRequest, ilMappingInfo);
					}
					else if( ilMappingInfo.getIsFlatFile() )
					{
						flatFileProcessor.processFlatFile(user, packageExecution, ilMappingInfo, customRequest);
					}
					else if( ilMappingInfo.getIsWebservice() )
					{
						ilMappingInfo.setWsConnectionRequestTimeout(dlInfo.getWsConnectionRequestTimeout());
						ilMappingInfo.setWsReadTimeout(dlInfo.getWsReadTimeout());
						ilMappingInfo.setWsConnectTimeout(dlInfo.getWsConnectTimeout());
						String wsFileType = ilMappingInfo.getWebService().getFileType();
						if(wsFileType == null )
						{
							ilMappingInfo.getWebService().setFileType("csv");;
						}
						webServiceProcessor.processWebservice(user, userPackage, customRequest.getDeploymentType(), packageExecution, s3BucketInfo, fileSettings, ilMappingInfo, customRequest);
					}
					count++;
					noOfSourcesString = " No of sources uploaded: " + count +"\n";
					metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, noOfSourcesString, packageExecution, customRequest);
				}
			}
		}
		catch ( PackageExecutionException pe )
		{
			throw new Exception(pe);
		}
	}

	List<String> processDl(PackageExecution packageExecution, DLInfo dlInfo, CustomRequest customRequest)
	{
		List<Map<String, Object>> incremtalUpdateList = new ArrayList<Map<String, Object>>();
		StringBuilder executionMappingInfoComments = new StringBuilder();
		Set<String> totalTablesSet = new HashSet<>();
		List<String> totalTablesList = null;
		if( packageExecution.isJobExecutionRequired() && dlInfo.isDlExecutionRequired() )
		{
			Map<String, Object> dlprocessList = dlProcessor.processDL(dlInfo.getdL_id(), 0, packageExecution, customRequest);
			if( dlprocessList != null )
			{
				executionMappingInfoComments.append((String) (dlprocessList.get("executionMappingInfoComments")));
				totalTablesSet.add((String) (dlprocessList.get("dlTableName")));
			}
			else
			{
				throw new UploadandExecutionFailedException("DL execution failed for package id : " + 0 + "dl id : " + dlInfo.getdL_id() + "execution Id:" + packageExecution.getExecutionId());
			}
			Set<Integer> ddLTableSet = new HashSet<Integer>();
			Set<String> ddLTableNamesSet = new HashSet<String>();
			dlInfo.getDdlayoutList().forEach(ddl ->
			{
				ddLTableSet.add(ddl.getId());
				ddLTableNamesSet.add(ddl.getTableName());
			});
			totalTablesList = dlProcessor.processCustomTableSets(0, ddLTableSet, ddLTableNamesSet, packageExecution, customRequest);
			totalTablesList.addAll(totalTablesSet);
			if( totalTablesList != null )
			{
				packageExecution.setDerivedTablesList(totalTablesList);
				metaDataFetch.reloadAnvizentTableAccessEndPoint(packageExecution, customRequest);
				metaDataFetch.alertsAndThreshould(packageExecution, customRequest);
				metaDataFetch.druidIntegration(packageExecution, customRequest);
			}
		}
		if( packageExecution.isJobExecutionRequired() )
		{
			if( incremtalUpdateList != null && incremtalUpdateList.size() > 0 )
			{
				packageExecution.setIncremtalUpdateList(incremtalUpdateList);
				metaDataFetch.saveUpdateIncrementalDates(packageExecution, customRequest);
			}
		}
		return totalTablesList;
	}

}
