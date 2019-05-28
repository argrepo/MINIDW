package com.anvizent.minidw.service.utils.processor;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;

@Component
public class FlatFileProcessor {
	protected static final Log log = LogFactory.getLog(FlatFileProcessor.class);

	@Autowired
	MetaDataFetch metaDataFetch;

	@Autowired
	CommonProcessor commonProcessor;

	public void processFlatFile(User user, PackageExecution packageExecution, ILConnectionMapping ilConnectionMapping,
			CustomRequest customRequest) {
		try {
			SourceFileInfo sourceFileInfo = new SourceFileInfo();
			sourceFileInfo.setS3BucketInfo(new S3BucketInfo());
			sourceFileInfo.getS3BucketInfo().setId((int) ilConnectionMapping.getS3BucketId());
			sourceFileInfo.setStorageType(ilConnectionMapping.getStorageType());
			sourceFileInfo.setFilePath(ilConnectionMapping.getFilePath());
			sourceFileInfo.setIlConnectionMappingId(ilConnectionMapping.getConnectionMappingId());

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());

			sourceFileInfo.setModification(modification);
			sourceFileInfo.setSourceFileId(ilConnectionMapping.getSourceFileInfoId());
			Integer ilId = ilConnectionMapping.getiLId();
			
			String message = String.format("\n IL id: %d IL mapping id : %d \n S3/Local file path is: %s", ilId,
					ilConnectionMapping.getConnectionMappingId(), ilConnectionMapping.getFilePath());
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
			log.info(message);

			sourceFileInfo.setPackageExecution(commonProcessor.getUploadStatus(packageExecution.getExecutionId(),
					Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.", packageExecution.getTimeZone()));
			metaDataFetch.saveExecutionSourceMappingInfo(packageExecution, sourceFileInfo, customRequest);
		} catch (Throwable t) {
			throw new PackageExecutionException(t);
		}

	}

}
