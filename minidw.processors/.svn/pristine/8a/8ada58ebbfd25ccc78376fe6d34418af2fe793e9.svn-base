package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;

@Component
public class DataBaseProcessor {
	protected static final Log log = LogFactory.getLog(DataBaseProcessor.class);

	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	S3FileProcessor s3FileProcessor;
	@Autowired
	ParseErrorMessage parseErrorMessage;

	public void processDatabase(User user, String deploymentType, PackageExecution packageExecution,
			S3BucketInfo s3BucketInfo, FileSettings fileSettings, CustomRequest customRequest,
			ILConnectionMapping ilConnectionMappingData) throws PackageExecutionException {
		ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
		SourceFileInfo sourceFileInfo = null;
		File tempFile = null;
		String directoryPath = null;
		File originalFile = null;
		String csvFolderPath = Constants.Temp.getTempFileDir();
		String userId = decryptUserId(user.getUserId());
		Integer connectionMappingId = ilConnectionMappingData.getConnectionMappingId();
		Integer dLId = ilConnectionMappingData.getdLId();
		try {
			if (StringUtils.isBlank(ilConnectionMappingData.getiLquery())) {
				throw new PackageExecutionException("Query should not be empty. Please check the source queries.");
			}
			boolean isIncrementalUpdate = ilConnectionMappingData.getIsIncrementalUpdate();
			String incrementalUpdateDate = null;
			Integer ilId = ilConnectionMappingData.getiLId();
			boolean isMultiPartEnabled = fileSettings.getMultiPartEnabled();
			if (isIncrementalUpdate) {
				incrementalUpdateDate = metaDataFetch.dateForIncrementalUpdateQuery(ilId,
						ilConnectionMappingData.getiLConnection().getConnectionId(),"database", connectionMappingId,customRequest);
			}
			String s3LogicalDirPath = getS3LogincalPath(userId, connectionMappingId, ilId, dLId);

			/* Source File Execution Started */
			String message = String.format(" Database source writing started for IL Mapping Id : %s ILId : %d",
					connectionMappingId, ilId);
			log.info(message);
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);

			HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(csvFolderPath,
					incrementalUpdateDate, ilConnectionMappingData,
					ilConnectionMappingData.getiLConnection().getDatabase().getConnector_id(), isIncrementalUpdate,
					null, isMultiPartEnabled, fileSettings.getNoOfRecordsPerFile());

			message = String.format(" Database source writing completed for IL mapping id : %s IL id : %d",
					connectionMappingId, ilId);
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
			/* Source File Execution Completed */

			Path tempDir = (Path) processedMap.get("tempDir");
			String maxDate = (String) processedMap.get("maxDate");

			ilConnectionMappingData.setClientId(userId);

			if (isMultiPartEnabled) {
				originalFile = tempDir.toFile();
				directoryPath = originalFile.getAbsolutePath();
			} else {
				Path tempFileName = (Path) processedMap.get("tempFile");
				tempFile = tempFileName.toFile();
				directoryPath = FilenameUtils.getFullPath(tempFile.getAbsolutePath());
				s3LogicalDirPath += ".csv";
				originalFile = new File(directoryPath + s3LogicalDirPath);
				tempFile.renameTo(originalFile);
			}
			String startTime = commonProcessor.getFormattedDateString();
			message = String.format("\n Database  file uploading started %s for IL mapping id: %d IL id %d",
					startTime, connectionMappingId, ilId);
			log.info(message);
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
			sourceFileInfo = s3FileProcessor.getS3UploadedFileInfo(s3BucketInfo, originalFile, userId,
					ilConnectionMappingData.getPackageId(), user.getUserName(), connectionMappingId, deploymentType,
					s3LogicalDirPath, isMultiPartEnabled, fileSettings.getFileEncryption());
			String endTime = commonProcessor.getFormattedDateString();
			message = String.format("\n Database  file uploading completed for %s for IL mapping id: %d IL id %d",
					endTime, connectionMappingId, ilId);
			log.info(message);
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);

			sourceFileInfo.setRowCount(clientDBProcessor.getSourceCount());
			sourceFileInfo.setIncrementalDateValue(maxDate);
			sourceFileInfo.setIncrementalUpdate(true);
			sourceFileInfo.setFileSize(clientDBProcessor.getSourceFileSize() + "");
			sourceFileInfo.setMultiPartFile(isMultiPartEnabled);
			if (s3BucketInfo != null) {
				sourceFileInfo.setS3BucketInfo(s3BucketInfo);
			} else {
				S3BucketInfo s3BucketInfo2 = new S3BucketInfo();
				s3BucketInfo2.setId(0);
				sourceFileInfo.setS3BucketInfo(s3BucketInfo2);
			}
			sourceFileInfo.setStorageType(ilConnectionMappingData.getStorageType());
			sourceFileInfo.setPackageExecution(commonProcessor.getUploadStatus(packageExecution.getExecutionId(),
					Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.", packageExecution.getTimeZone()));
			sourceFileInfo.setDelimeter(Constants.FileTypeDelimiter.CSV_DELIMITER);
			sourceFileInfo.setFileType(Constants.FileType.CSV);
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			sourceFileInfo.setModification(modification);

			metaDataFetch.saveSourceFileInfo(sourceFileInfo, customRequest);

			metaDataFetch.saveExecutionSourceMappingInfo(packageExecution, sourceFileInfo, customRequest);
			message = "\n Multipart enabled : %s \n Database source \n IL id :" + ilId
					+ " \n S3/Local file path is:%s\n file size: %s";

			message = String.format(message, isMultiPartEnabled, sourceFileInfo.getFilePath(),
					FileUtils.byteCountToDisplaySize(originalFile.length()));
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | QueryParcingException
				| SQLException | IOException | AmazonS3Exception | OnpremFileCopyException | NumberFormatException e) {
			throw new PackageExecutionException(e);
		} finally {
			if (StringUtils.isNotBlank(directoryPath)) {
				try {
					FileUtils.forceDelete(new File(directoryPath));
				} catch (Exception e) {
					log.info("Unable to delete created csv for " + parseErrorMessage.getErrorMessageString(e));
				}
			}
		}
	}

	private String getS3LogincalPath(String userId, Integer connectionMappingId, Integer ilId, Integer dLId) {
		String s3LogicalDirPath = "datafiles_U" + userId + "_M" + connectionMappingId;
		if (ilId != null && ilId != 0) {
			s3LogicalDirPath += "_IL" + ilId + "_" + "DL" + dLId;
		}
		s3LogicalDirPath += "_" + com.anvizent.minidw.service.utils.helper.CommonUtils.generateUniqueIdWithTimestamp();
		return s3LogicalDirPath;
	}

	public String decryptUserId(String userId) {
		try {
			return Integer.parseInt(userId)+"";
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		try {
			userId = EncryptionServiceImpl.getInstance().decrypt(userId);
			String[] userIdAndLocale = StringUtils.split(userId, '#');
			userId = userIdAndLocale[0];
		} catch (Exception e) {
			log.error("error ",e);
		}
		return userId;

	}

}
