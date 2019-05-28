package com.anvizent.minidw.service.utils.processor;

import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;

public class StandardPackageExecutor extends Thread {

	User user;
	DLInfo dlInfo;
	S3BucketInfo s3BucketInfo;
	FileSettings fileSettings;
	CustomRequest customRequest;
	PackageExecution packageExecution;
	StandardPackageProcessor standardPackageProcessor  = null;
	boolean isExecutionrequired;
	public StandardPackageExecutor(StandardPackageProcessor standardPackageProcessor, PackageExecution packageExecution, User user, DLInfo dlInfo, S3BucketInfo s3BucketInfo, FileSettings fileSettings,
			                        CustomRequest customRequest){
		this.dlInfo = dlInfo;
		this.user = user;
		this.s3BucketInfo = s3BucketInfo;
		this.fileSettings = fileSettings;
		this.customRequest = customRequest;
		this.standardPackageProcessor = standardPackageProcessor;
		this.packageExecution = packageExecution;
	}
	
	@Override
	public void run(){
		try{
			standardPackageProcessor.processStandardPackageExecution(packageExecution,user, dlInfo, s3BucketInfo, fileSettings, customRequest);
		}catch(Exception e){
			throw new UploadandExecutionFailedException(e);
		}
		
	}
	
	
}
