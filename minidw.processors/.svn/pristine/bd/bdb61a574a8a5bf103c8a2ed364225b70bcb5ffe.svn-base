package com.anvizent.minidw.service.utils.processor;

import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.User;
/**
 * @author mahender.alaveni
 * 
 * created date : 15-11-2018
 * 
 * RBModelProcessor is used to execute r file
 *
 */
@Component
public class RBModelProcessor
{

	protected static final Log logger = LogFactory.getLog(RBModelProcessor.class);

	@Autowired
	MessageSource messageSource;
	@Autowired
	ILProcessor ilProcessor;
	@Autowired
	RProcessor rProcessor;
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

	public RBModelProcessor()
	{
		logger.info("In RModelProcessor...");
	}

	public void processRBModelExecution(HttpServletRequest request, String encUserId, User user, DLInfo dlInfo, boolean isExecutionrequired)
	{

		CustomRequest customRequest = getCustomRequest(request, user);

		PackageExecution packageExecutionInfo = new PackageExecution();
		packageExecutionInfo.setPackageId(0);
		packageExecutionInfo.setRunType(Constants.jobType.R);

		packageExecutionInfo.setUploadStatus(Constants.ExecutionStatus.IGNORED);
		packageExecutionInfo.setUploadComments(Constants.ExecutionStatus.IGNORED);

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
		processStandardPackageExecutor(packageExecutionInfo, customRequest, customRequest.getEncUserId(), user, dlInfo);

	}

	public RBModelExecutor processStandardPackageExecutor(PackageExecution packageExecution, CustomRequest customRequest, String encUserId, User user, DLInfo dlInfo)
	{
		RBModelExecutor rBModelExecutor = new RBModelExecutor(this, packageExecution, user, dlInfo, customRequest);
		rBModelExecutor.start();
		return rBModelExecutor;
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

	public void processRModelExecution(PackageExecution packageExecution, User user, DLInfo dlInfo, CustomRequest customRequest)
	{
		try
		{
			processRBMExecution(packageExecution, dlInfo, customRequest);
		}
		catch ( Throwable e )
		{
			logger.error("", e);
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.FAILED, e.getMessage(), packageExecution, customRequest);
		}
	}

	public void processRBMExecution(PackageExecution packageExecution, DLInfo dlInfo, CustomRequest customRequest)
	{
		if( packageExecution.isJobExecutionRequired() )
		{
			metaDataFetch.saveExecutionInfo(Constants.ExecutionStatus.STARTED, "\n R Job Execution started. \n", packageExecution, customRequest);
			BusinessModal businessModal = new BusinessModal();
			businessModal.setBmid(dlInfo.getdL_id());
			rProcessor.processRJob(businessModal, packageExecution, customRequest);
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.COMPLETED, "", packageExecution, customRequest);
		}
	}
}
