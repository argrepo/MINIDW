package com.anvizent.minidw.service.utils.processor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;

@Component
public class CustomPackageProcessor {
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	FlatFileProcessor flatFileProcessor;
	@Autowired
	DataBaseProcessor dataBaseProcessor;
	
	
	public void packageProcessor(HttpServletRequest request, int packageId, String clientId, boolean packageExecutionRequired)
	{
		User user = (User) SessionHelper.getSesionAttribute(request, "principal");
		S3BucketInfo s3BucketInfo = metaDataFetch.getS3BucketInfo(getCustomRequest(request,user));//(S3BucketInfo) SessionHelper.getSesionAttribute(request, Constants.Config.S3_BUCKET_INFO);
		FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request, Constants.Config.FILE_SETTINGS_INFO);
		String browserDetails = CommonUtils.getClientBrowserDetails(request);
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String webServiceContextUrl = (String) SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		packageProcessor(packageId, clientId, packageExecutionRequired, s3BucketInfo, fileSettings, browserDetails, deploymentType, webServiceContextUrl, user);
	}
	
	public void packageProcessor(int packageId, String clientId, boolean packageExecutionRequired,  S3BucketInfo s3BucketInfo, FileSettings fileSettings,String browserDetails, String deploymentType, String webServiceContextUrl, User user ) {
		CustomRequest customRequest = getCustomRequest(browserDetails, deploymentType, webServiceContextUrl, user);
		packageProcessor(packageId, clientId, packageExecutionRequired, s3BucketInfo, fileSettings, user, customRequest);
	}

	public void packageProcessor(int packageId, String clientId,boolean packageExecutionRequired, S3BucketInfo s3BucketInfo, FileSettings fileSettings,User user,CustomRequest customRequest) {
		
		// upload sources
			// create execution records 
			// fetch all sources meta info
			// update each source 
			// update upload status 
			// if everything okay the return execution_id
			// otherwise return zero
		
		
		// execute sources
			// if execution_id >0 and packageExecutionRequired true 
				// then 
				// separate method: 
				// check for uploaded source count
				// if same headers yes 
				// 		{}
				// if headers are different 
				// 		insert sources into temp tables 
				//		execute custom query 
				// 		update target table
				// if everything okay 
				// anvizent url call
				// alerts api call 
				// druid call
		
	}
	
	public CustomRequest getCustomRequest(HttpServletRequest request, User user) {
		String browserDetails = CommonUtils.getClientBrowserDetails(request);
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String webServiceContextUrl = (String) SessionHelper.getSesionAttribute(request,
				Constants.Config.WEBSERVICE_CONTEXT_URL);
		return getCustomRequest(browserDetails, deploymentType, webServiceContextUrl, user);
	}
	
	public CustomRequest getCustomRequest(String browserDetails,String deploymentType, String webServiceContextUrl,User user) {
		return getCustomRequest(browserDetails, deploymentType, webServiceContextUrl, user.getClientId(), user.getUserId());
	}
	
	public CustomRequest getCustomRequest(String browserDetails,String deploymentType, String webServiceContextUrl,String clientId,String userId) {
		CustomRequest customRequest = new CustomRequest(webServiceContextUrl, clientId, clientId,
				browserDetails, deploymentType, null, userId);
		return customRequest;
	}
	
	
}
