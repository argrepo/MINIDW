package com.anvizent.amazon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.anvizent.encryptor.EncryptionUtility;
import com.datamodel.anvizent.common.exception.AmazonExeception;
import com.datamodel.anvizent.helper.Constants;

public class AmazonS3Utilities
{
	protected static final Log LOGGER = LogFactory.getLog(AmazonS3Utilities.class);
	private static final String PRIVATE_KEY = "anvizent";
	private static final String IV = "AnvizentDMT IV16";
	EncryptionUtility encryptionUtility;
	final String accessKey;
	final String secretKey;
	final String bucketName;
	AmazonS3 s3Client;
	
	private static int MAX_S3_CONNECTION_HIT_LIMIT = 2;
	static final int S3_CONNECTION_SLEEP_TIME = 5000;

	public AmazonS3Utilities(String accessKey, String secretKey, String bucketName) throws AmazonS3Exception
	{
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.bucketName = bucketName;
		s3Client = getAmazonS3Client(accessKey, secretKey);
		try
		{
			encryptionUtility = new EncryptionUtility(PRIVATE_KEY, IV);
		}
		catch ( UnsupportedEncodingException e )
		{
			throw new AmazonS3Exception("S3 Source File encryption/decryption failed: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("deprecation")
	private AmazonS3 getAmazonS3Client(String accessKey, String secretKey)
	{
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AmazonS3Client(credentials);
	}

	public void deleteFileFromS3Bucket(String filePath) throws Exception
	{
		LOGGER.debug("delete File From S3 Bucket()");
		s3Client.deleteObject(new DeleteObjectRequest(bucketName, filePath));
	}

	public String downloadFileFromS3(String filePathInS3, boolean isEncryptionEnabled) throws AmazonS3Exception
	{
		File encrtptedS3File = null;
		File decryptedFile = null;
		File tempDecryptedFile = null;
		String downloadFilePath = null;
		try
		{

			encrtptedS3File = downloadS3File(filePathInS3);
			String encryptedfileName = encrtptedS3File.getName();
			if( isEncryptionEnabled )
			{
				encryptionUtility = new EncryptionUtility(PRIVATE_KEY, IV);
				String decryptedFileName = encryptionUtility.decrypt(encryptedfileName);
				if( StringUtils.isNotBlank(decryptedFileName) )
				{
					String downloadFileDir = Constants.Temp.getTempFileDir() + "decryptedFiles/";
					FileUtils.forceMkdir(new File(downloadFileDir));
					decryptedFile = new File(downloadFileDir + decryptedFileName);
					LOGGER.debug("Is dyscryption enabled ? " + isEncryptionEnabled);
					String dir = Constants.Temp.getTempFileDir() + "downloadedFiles/";
					FileUtils.forceMkdir(new File(dir));
					String destinationFilePath = dir + FilenameUtils.getName(encrtptedS3File.getAbsolutePath());
					tempDecryptedFile = encryptionUtility.decryptFile(encrtptedS3File, destinationFilePath);
					if( tempDecryptedFile != null )
					{
						FileUtils.copyFile(tempDecryptedFile, decryptedFile);
					}
					downloadFilePath = downloadFileDir + decryptedFileName;
				}
			}
			else
			{
				downloadFilePath = encrtptedS3File.getAbsolutePath();
			}

		}
		catch ( Exception e )
		{
			throw new AmazonS3Exception(e.getMessage(), e);
		}
		finally
		{
			if( isEncryptionEnabled )
			{
				deleteQuietly(encrtptedS3File);
			}
			deleteQuietly(tempDecryptedFile);
		}
		return downloadFilePath;
	}

	public static boolean deleteQuietly(File file)
	{
		if( file == null )
		{
			return false;
		}
		try
		{
			if( file.isDirectory() )
			{
				FileUtils.cleanDirectory(file);
			}
		}
		catch ( Exception ignored )
		{
		}

		try
		{
			return file.delete();
		}
		catch ( Exception ignored )
		{
			return false;
		}
	}

	private File downloadS3File(String filePathInS3) throws IOException, AmazonServiceException, AmazonClientException, InterruptedException
	{

		File encrtptedFile = null;
		TransferManager tm = TransferManagerBuilder.standard().withS3Client(getAmazonS3Client(accessKey, secretKey)).withDisableParallelDownloads(false).withMinimumUploadPartSize(Long.valueOf(5 * MB)).withMultipartUploadThreshold(Long.valueOf(16 * MB)).withMultipartCopyPartSize(Long.valueOf(5 * MB))
				.withMultipartCopyThreshold(Long.valueOf(100 * MB)).withExecutorFactory(() -> createExecutorService(20)).build();
		try
		{
			String dir = Constants.Temp.getTempFileDir();
			FileUtils.forceMkdir(new File(dir));
			String tempFileName = filePathInS3;
			if( filePathInS3.indexOf("/") != -1 )
			{
				tempFileName = StringUtils.substringAfterLast(filePathInS3, "/");
			}
			encrtptedFile = new File(dir + tempFileName);
			/* Step 1 : download the encrypted file */
			LOGGER.info("downloading started from S3..." + filePathInS3);
			Download download = tm.download(bucketName, filePathInS3, encrtptedFile);
			download.waitForCompletion();
			LOGGER.debug("Download ...");
		}
		finally
		{
			if( tm != null )
			{
				tm.shutdownNow();
			}
		}
		return encrtptedFile;
	}

	@SuppressWarnings("unused")
	private File downloadS3FileOld(String filePathInS3) throws IOException
	{

		FileOutputStream outstream = null;
		File encrtptedFile = null;
		try
		{
			/* Step 1 : download the encrypted file */
			LOGGER.info("downloading started from S3..." + filePathInS3);
			S3Object obj = s3Client.getObject(new GetObjectRequest(bucketName, filePathInS3));
			String dir = Constants.Temp.getTempFileDir();
			FileUtils.forceMkdir(new File(dir));
			String tempFileName = obj.getKey();
			if( obj.getKey().indexOf("/") != -1 )
			{
				tempFileName = StringUtils.substringAfterLast(obj.getKey(), "/");
			}

			encrtptedFile = new File(dir + tempFileName);
			outstream = new FileOutputStream(encrtptedFile);
			LOGGER.debug("copying stream into temp file...");
			IOUtils.copy(obj.getObjectContent(), outstream);
			LOGGER.debug("Download ...");
		}
		finally
		{
			if( outstream != null )
			{
				outstream.close();
			}
		}
		return encrtptedFile;
	}

	public static final long DEFAULT_FILE_PART_SIZE = 10 * 1024 * 1024; /* 10MB */
	public static final long FILE_PART_SIZE = DEFAULT_FILE_PART_SIZE;
	public static int MB = 1024 * 1024;

	public void putObjectAsMultiPartOld(File file, String key) throws AmazonS3Exception
	{
		long partSize = FILE_PART_SIZE;
		List<PartETag> partETags = new ArrayList<PartETag>();
		List<MultiPartFileUploader> uploaders = new ArrayList<MultiPartFileUploader>();

		/* Step 1: Initialize. */
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		long contentLength = file.length();

		/* Step 2: Upload parts. */
		long filePosition = 0;
		for (int i = 1; filePosition < contentLength; i++)
		{
			/* Last part can be less than part size. Adjust part size. */
			partSize = Math.min(partSize, (contentLength - filePosition));

			/* Create request to upload a part.*/
			UploadPartRequest uploadRequest = new UploadPartRequest()
					.withBucketName(bucketName).withKey(key).withUploadId(initResponse.getUploadId()).withPartNumber(i).withFileOffset(filePosition).withFile(file).withPartSize(partSize);

			uploadRequest.setGeneralProgressListener(new UploadProgressListener(file, i, partSize));

			/* Upload part and add response to our list.*/
			MultiPartFileUploader uploader = new MultiPartFileUploader(uploadRequest, s3Client);
			uploaders.add(uploader);
			uploader.upload();

			filePosition += partSize;
		}

		for (MultiPartFileUploader uploader : uploaders)
		{
			try
			{
				uploader.join();
			}
			catch ( InterruptedException e )
			{
				throw new AmazonS3Exception("S3 File uploading failed", e);
			}
			partETags.add(uploader.getPartETag());
		}

		/* Step 3: complete.*/
		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key, initResponse.getUploadId(), partETags);

		s3Client.completeMultipartUpload(compRequest);
	}

	public void putObjectAsMultiPart(File file, String key, int retryCount) throws AmazonS3Exception
	{
		TransferManager tm = TransferManagerBuilder.standard()
				.withS3Client(getAmazonS3Client(accessKey, secretKey))
				.withDisableParallelDownloads(false)
				.withMinimumUploadPartSize(Long.valueOf(5 * MB))
				.withMultipartUploadThreshold(Long.valueOf(16 * MB))
				.withMultipartCopyPartSize(Long.valueOf(5 * MB))
				.withMultipartCopyThreshold(Long.valueOf(100 * MB))
				.withExecutorFactory(() -> createExecutorService(20))
				.build();
		PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
		request.setGeneralProgressListener(new UploadProgressListener(file));
		Upload upload = tm.upload(request);
		try
		{
			upload.waitForCompletion();
		}
		catch ( AmazonClientException amazonClientException )
		{
			if( amazonClientException instanceof SdkClientException ) {
				retryCount++;
				try {
					int sleepTime = (int) Math.pow(2, retryCount - 1) * S3_CONNECTION_SLEEP_TIME;
					LOGGER.debug("s3 connection retry seconds" + " -- " + sleepTime);
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					throw new AmazonExeception("Sleep timer omitted " + e.getMessage());
				}
				if(retryCount <= MAX_S3_CONNECTION_HIT_LIMIT) {
					putObjectAsMultiPart(file, key, retryCount);
				}else {
					throw new AmazonExeception(
							MAX_S3_CONNECTION_HIT_LIMIT + " retries completed for amazon s3 connection time out");
				}
			}else {
				throw new AmazonExeception("Unable to upload file, upload aborted. \n" + amazonClientException);
			}
		}
		catch ( InterruptedException e )
		{
			throw new AmazonExeception(e);
		}
		finally
		{
			if( tm != null )
			{
				tm.shutdownNow();
			}
		}

	}

	private ThreadPoolExecutor createExecutorService(int threadNumber)
	{
		ThreadFactory threadFactory = new ThreadFactory()
		{
			private int threadCount = 1;

			public Thread newThread(Runnable r)
			{
				LOGGER.info("createExecutorService ");
				Thread thread = new Thread(r);
				thread.setName("jsa-amazon-s3-transfer-manager-worker-" + threadCount++);
				return thread;
			}
		};
		return (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNumber, threadFactory);
	}

	public String uploadFileToS3(File originalFile, String s3FolderPath, boolean isEncryptionEnabled) throws AmazonS3Exception
	{
		File fileToBeUploaded = null;
		File encryptedFile = null;
		File encryptedTempFile = null;
		String s3filePath = null;

		try
		{

			String fileName = originalFile.getName();
			String fileExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + generateUniqueIdWithTimestamp();
			String logicalFileName = formattedFileName + fileExtention;
			LOGGER.debug("logicalFileName : " + logicalFileName);
			String encryptedFileName = null;
			if( isEncryptionEnabled )
			{
				encryptedFileName = encryptionUtility.encrypt(logicalFileName);
				/* Step 2 : create a new file with encrypted file name */
				String encryptedFileDir = Constants.Temp.getTempFileDir();
				if( !new File(encryptedFileDir).exists() )
				{
					new File(encryptedFileDir).mkdir();
				}
				String encryptedFilePath = encryptedFileDir + encryptedFileName;
				encryptedFile = new File(encryptedFilePath);

				/* Step 3: encrypt the original file content and write into encrypted file */
				String encryptedFolderPath = Constants.Temp.getTempFileDir() + "encryptedFile/";
				FileUtils.forceMkdir(new File(encryptedFolderPath));

				encryptedTempFile = encryptionUtility.encryptFile(originalFile, encryptedFolderPath + FilenameUtils.getName(originalFile.getAbsolutePath()));
				FileUtils.copyFile(encryptedTempFile, encryptedFile);
				fileToBeUploaded = encryptedFile;

				/* Step 5 : upload file to folder and set it to public */

				s3filePath = s3FolderPath + encryptedFileName;
			}
			else
			{
				encryptedFileName = logicalFileName;
				s3filePath = s3FolderPath + logicalFileName;
				fileToBeUploaded = originalFile;
			}

			/* upload encrypted file to S3 */
			LOGGER.debug("file upload started..." + new Date());
			putObjectAsMultiPart(fileToBeUploaded, s3filePath, 0);
			LOGGER.debug("file upload completed...");
			LOGGER.info("File uploaded to S3 :" + s3filePath);
		}
		catch ( AmazonS3Exception e )
		{
			e.printStackTrace();
			throw e;
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new AmazonS3Exception("S3 File uploading failed " + e.getMessage(), e);
		}
		finally
		{
			if( encryptedFile != null )
			{
				encryptedFile.delete();
			}
			if( encryptedTempFile != null )
			{
				encryptedTempFile.delete();
			}
		}
		return s3filePath;
	}

	public static String generateUniqueIdWithTimestamp()
	{

		String op = "";

		try
		{
			DateFormat format = new SimpleDateFormat("yyyyMMdd_hhmmss_SS");
			Date currentTime = new Date();

			op = format.format(currentTime);

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		return op;
	}

	public void createFolder(String bucketName, String folderName, AmazonS3 client)
	{
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, emptyContent, metadata);

		client.putObject(putObjectRequest);
	}

	public List<String> downloadFilesFromS3(String s3FolderPath, boolean isMultiPartEnabled, boolean isEncryptionRequired)
	{
		List<String> filePathList = new ArrayList<>();
		if( isMultiPartEnabled )
		{
			List<String> filesList = getS3FilesList(s3FolderPath);
			filesList.forEach(fileStr ->
			{
				filePathList.add(downloadFileFromS3(fileStr, isEncryptionRequired));
			});
		}
		else
		{
			filePathList.add(downloadFileFromS3(s3FolderPath, isEncryptionRequired));
		}
		return filePathList;
	}

	public List<String> getS3FilesList(String s3FolderPath)
	{
		List<String> list = new ArrayList<>();

		ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(s3FolderPath));

		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
		{
			list.add(objectSummary.getKey());
		}

		return list;
	}
	public List<String> getObjectslistFromFolder(String folderKey, boolean isMultipartEnable, boolean isEncryptionRequired) {
	     
	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderKey);
	    List<String> filepaths = new ArrayList<>();
	    ObjectListing objects = s3Client.listObjects(listObjectsRequest);
	    
        List<S3ObjectSummary> summaries = objects.getObjectSummaries();
        if(summaries.size() > 0){
	        for(S3ObjectSummary summary : summaries){
	        	filepaths.add(downloadFileFromS3(summary.getKey(), isEncryptionRequired));
	        }
        }
	    return filepaths;
	}

}
