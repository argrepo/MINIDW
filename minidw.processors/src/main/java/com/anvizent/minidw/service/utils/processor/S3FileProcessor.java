package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.amazon.AmazonS3Utilities;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;

@Component
public class S3FileProcessor {
	protected static final Log log = LogFactory.getLog(S3FileProcessor.class);
	
	@Autowired
	CommonProcessor commonProcessor;
	
	public List<String> downloadFilesFromS3(String filePath, String clientId, String deploymentType,
			boolean isMultiPartEnabled,boolean isEncryptionRequired, S3BucketInfo s3BucketInfo) throws AmazonS3Exception, OnpremFileCopyException, IOException {
		List<String> filesList = null;
		if (StringUtils.isNotBlank(deploymentType)
				&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			 String directoryPath = Constants.Temp.getTempFileDir() + "/LocalFileCopyDir/";
			if(isMultiPartEnabled){
				filePath = StringUtils.removeEnd(filePath, "/");
				filesList = new ArrayList<>();
				String fileName = FilenameUtils.getBaseName(filePath);
				String destPath = directoryPath + fileName;
				File destFolder = new File(destPath); 
				FileUtils.copyDirectory(new File(filePath),destFolder, false);
				if(destFolder.isDirectory()){
					for(File file : destFolder.listFiles()){
						filesList.add(file.getAbsolutePath());
					}
				}else{
					//TODO throw exception if it is not a directory.
					throw new OnpremFileCopyException("destination path is not a directory.the path is : "+destFolder);
				}
			}else{
				filesList = new ArrayList<>();
				String fileName = FilenameUtils.getName(filePath);
				String fileExtention = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + commonProcessor.generateUniqueIdWithTimestamp();
				String logicalFileName = formattedFileName + fileExtention;
				String destPath = directoryPath+logicalFileName;
				FileUtils.copyFile(new File(filePath),new File(destPath));
				filesList.add(destPath);
			}
		} else {
			if (s3BucketInfo != null) {
				AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
						s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
				filesList = amazonS3Utilities.downloadFilesFromS3(filePath, isMultiPartEnabled,isEncryptionRequired);
			} else {
				filesList = new ArrayList<>();
				filesList.add(filePath);
			}
		}
		return filesList;
	}
	
	public SourceFileInfo getS3UploadedFileInfo(S3BucketInfo s3BucketInfo, File originalFile, String userId,
			Integer packegeId, String userName, Integer ilConnectionMappingId, String deploymentType,
			String s3LogicalDirPath, boolean isMultiPartRequired,boolean isEncryptionRequired) throws OnpremFileCopyException, AmazonS3Exception, IOException {

		if (originalFile == null) {
			throw new OnpremFileCopyException("Source file should not be empty");
		}
		String fileType = FilenameUtils.getExtension(originalFile.getAbsolutePath());
		SourceFileInfo sourceFileInfo = null;
		String storageType = Constants.Config.STORAGE_TYPE_S3;
		if (StringUtils.isNotBlank(deploymentType)
				&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			storageType = Constants.Config.STORAGE_TYPE_LOCAL;
		}
		if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			if (s3BucketInfo != null) {
				sourceFileInfo = uploadFileToS3(originalFile, s3BucketInfo, isMultiPartRequired, s3LogicalDirPath,isEncryptionRequired);
				sourceFileInfo.setBucketName(s3BucketInfo.getBucketName());
			} else {
				throw new OnpremFileCopyException("S3 file upload failed (Bucket details not found).");
			}
		} else {
			String destinationPath = null;
			if ( StringUtils.isBlank(com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_LOCATION) ) {
				destinationPath = Constants.Temp.getTempFileDir()+"/";
			} else {
				destinationPath = com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_LOCATION+"/";
			}
			
			destinationPath += s3LogicalDirPath+"/";
			if(isMultiPartRequired){
				FileUtils.copyDirectory(originalFile,new File(destinationPath));
			} else{
				String fileName = originalFile.getName();
				String fileExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
				String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_"+ generateUniqueIdWithTimestamp();
				String logicalFileName = formattedFileName + fileExtention;
				destinationPath += logicalFileName;
				FileUtils.copyFile(originalFile, new File(destinationPath));
			}
			sourceFileInfo = new SourceFileInfo();
			sourceFileInfo.setFilePath(destinationPath);
			sourceFileInfo.setFileSize("0");
		}

		String fileName = FilenameUtils.getName(originalFile.getAbsolutePath());
		if (originalFile.isDirectory()) {
			sourceFileInfo.setFileName(fileName);
		} else {
			sourceFileInfo.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
		}
		sourceFileInfo.setUserId(Integer.parseInt(userId));
		sourceFileInfo.setFileType(fileType);
		sourceFileInfo.setDelimeter(",");
		sourceFileInfo.setPackageId(packegeId);
		sourceFileInfo.setStorageType(storageType);
		sourceFileInfo.setIlConnectionMappingId(ilConnectionMappingId);
		sourceFileInfo.setS3BucketInfo(s3BucketInfo);
		sourceFileInfo.setEncryptionRequired(isEncryptionRequired);
		
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(userName);
		sourceFileInfo.setModification(modification);
		return sourceFileInfo;
	}
	
	public SourceFileInfo uploadFileToS3(File originalFile, S3BucketInfo s3BucketInfo,
			boolean isMultiPartRequired, String s3LogicalDirPath,boolean isEncryptionRequired) throws OnpremFileCopyException, AmazonS3Exception {

		SourceFileInfo sourceFileInfo = null;
		String fileSizeInBytes = "";
		int fileSize = 0;
		if (isMultiPartRequired) {
			if (originalFile.isDirectory()) {
				s3LogicalDirPath += "/";
				if (originalFile.listFiles() != null) {
					sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setUploadStartTime(commonProcessor.getFormattedDateString());
					sourceFileInfo.setFilePath(s3LogicalDirPath);
					for (File file : originalFile.listFiles()) {
						if (!file.isDirectory()) {
							uploadFileToS3(file, s3BucketInfo, s3LogicalDirPath,isEncryptionRequired);
							fileSize = fileSize + Math.round(file.length());
						}
					}
					sourceFileInfo.setFileSize(String.valueOf(fileSize));
					sourceFileInfo.setUploadEndTime(commonProcessor.getFormattedDateString());
					sourceFileInfo.setMultiPartFile(true);
				} else {
					throw new OnpremFileCopyException("Multipart files not found");
				}
			} else {
				throw new OnpremFileCopyException("Multipart folder not found");
			}
		} else {
			sourceFileInfo = uploadFileToS3(originalFile, s3BucketInfo, "",isEncryptionRequired);
			fileSizeInBytes  = String.valueOf(Math.round(originalFile.length()));
			sourceFileInfo.setFileSize(fileSizeInBytes);
			sourceFileInfo.setMultiPartFile(false);
		}
		return sourceFileInfo;
	}
	
	public SourceFileInfo uploadFileToS3(File originalFile, S3BucketInfo s3BucketInfo, String s3FolderName,boolean isEncryptionRequired)
			throws OnpremFileCopyException, AmazonS3Exception {
		SourceFileInfo sourceFileInfo = new SourceFileInfo();
		if (StringUtils.isNotBlank(s3BucketInfo.getAccessKey()) && StringUtils.isNotBlank(s3BucketInfo.getSecretKey())
				&& StringUtils.isNotBlank(s3BucketInfo.getBucketName())) {
			if (originalFile != null) {
				AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
						s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
				// String folderName = ""; //userId + SUFFIX + packegeId +
				// SUFFIX;
				sourceFileInfo.setUploadStartTime(commonProcessor.getFormattedDateString());
				log.info("file upload started..." + new Date());
				String s3filePath = amazonS3Utilities.uploadFileToS3(originalFile, s3FolderName, isEncryptionRequired);
				log.info("file upload completed..." + new Date());
				sourceFileInfo.setUploadEndTime(commonProcessor.getFormattedDateString());
				log.info("File uploaded to S3 :" + s3filePath);
				sourceFileInfo.setFilePath(s3filePath);
			} else {
				throw new AmazonS3Exception("Invalid details for s3 upload");
			}
		} else {
			throw new AmazonS3Exception("Invalid s3 bucket details ");
		}
		return sourceFileInfo;
	}
	
	public String generateUniqueIdWithTimestamp() {
		String op = "";

		try {
			DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SS");
			Date currentTime = new Date();

			op = format.format(currentTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return op;
	}

}
