package com.datamodel.anvizent.helper.minidw;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.SourceFileInfo;

@Component
public class PackageExecutionStatusUpload {
   
	protected static final Log logger = LogFactory.getLog(PackageExecutionStatusUpload.class); 
	public PackageExecutionStatusUpload(){
		logger.info("In PackageExecutionStatusUpload...");
	}
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;
	
	/**
	 * @param executionOrUploadStatus
	 * @param executionOrUploadComments
	 * @param encUserId
	 * @param packageExecution
	 * @param customRequest
	 */
	public void updatePackageExecutionUploadInfo(String executionOrUploadStatus,String executionOrUploadComments, 
			String encUserId, PackageExecution packageExecution, CustomRequest customRequest){
		 PackageExecution packExecution = MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(),executionOrUploadStatus, executionOrUploadComments,packageExecution.getTimeZone());
		 packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionUploadInfo",  packExecution, encUserId);
	}
	
    /**
     * @param executionOrUploadStatus
     * @param executionOrUploadComments
     * @param sourceFileInfo
     * @param encUserId
     * @param packageExecution
     * @param customRequest
     */
	public void updatePackageExecutorSourceMappingInfo(String executionOrUploadStatus,String executionOrUploadComments,SourceFileInfo sourceFileInfo, 
    		String encUserId, PackageExecution packageExecution, CustomRequest customRequest){
   	 sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(), executionOrUploadStatus,executionOrUploadComments,packageExecution.getTimeZone()));
	     packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutorSourceMappingInfo", sourceFileInfo, encUserId);
	} 
    
    /**
     * @param sourceFileInfo
     * @param encUserId
     * @param packageExecution
     * @param customRequest
     * @return
     */
     public DataResponse updateSourceFileInfo(SourceFileInfo sourceFileInfo, 
    		String encUserId, PackageExecution packageExecution, CustomRequest customRequest){
	   return packageRestUtilities.postRestObject(customRequest, "/updateSourceFileInfo", sourceFileInfo, encUserId);
   
    }
}
