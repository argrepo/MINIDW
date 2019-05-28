package com.anvizent.minidw.service.utils.processor;

import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.User;
/**
 * @author mahender.alaveni
 * 
 * created date : 15-11-2018
 * 
 * RBModelExecutor is used to execute r file
 *
 */
public class RBModelExecutor extends Thread
{

	User user;
	DLInfo dlInfo;
	CustomRequest customRequest;
	PackageExecution packageExecution;
	RBModelProcessor rBModelProcessor = null;
	boolean isExecutionrequired;

	public RBModelExecutor(RBModelProcessor rBModelProcessor, PackageExecution packageExecution, User user, DLInfo dlInfo, CustomRequest customRequest)
	{
		this.dlInfo = dlInfo;
		this.user = user;
		this.customRequest = customRequest;
		this.rBModelProcessor = rBModelProcessor;
		this.packageExecution = packageExecution;
	}

	@Override
	public void run()
	{
		try
		{
			rBModelProcessor.processRModelExecution(packageExecution, user, dlInfo, customRequest);
		}
		catch ( Exception e )
		{
			throw new UploadandExecutionFailedException(e);
		}

	}

}
