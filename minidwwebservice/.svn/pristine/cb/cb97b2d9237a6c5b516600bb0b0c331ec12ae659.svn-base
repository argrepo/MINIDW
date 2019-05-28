package com.datamodel.anvizent.AmazonS3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class AmazonS3Util {
	protected static final Log logger = LogFactory.getLog(AmazonS3Util.class);

	public static final long DEFAULT_FILE_PART_SIZE = 10 * 1024 * 1024; // 10MB
	public static long FILE_PART_SIZE = DEFAULT_FILE_PART_SIZE;
	private static AmazonS3 s3Client;
	@SuppressWarnings("unused")
	private static TransferManager transferManager;

	public AmazonS3Util(String accessKey, String secretKey) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		s3Client = new AmazonS3Client(credentials);
		transferManager = new TransferManager(credentials);
	}

	
	public static void main() throws Exception{
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select now()");
			if ( rs.next() ) {
				System.out.println("Current Date:- " +rs.getString(1));
			} else {
				System.out.println("Empty ResultSet");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		} finally {
			if ( con != null && !con.isClosed()) {
				con.close();
			}
		}
		
	}
	
	public static Connection getConnection() throws Exception {
		Connection con = null;
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcUrl = "jdbc:mysql://192.168.0.135:4475/minidw_migr_7";
		con = DriverManager.getConnection(jdbcUrl, "almadmin", "Un!qu3Pa5$");
		return con;
	}
	
	public void putObjectAsMultiPart(String bucketName, File file, String key) throws Exception {
		putObjectAsMultiPart(bucketName, file, FILE_PART_SIZE, key);
	}

	public void putObjectAsMultiPart(String bucketName, File file, long partSize, String key) throws Exception {
		List<PartETag> partETags = new ArrayList<PartETag>();
		List<MultiPartFileUploader> uploaders = new ArrayList<MultiPartFileUploader>();

		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		long contentLength = file.length();

		// Step 2: Upload parts.
		long filePosition = 0;
		for (int i = 1; filePosition < contentLength; i++) {
			// Last part can be less than part size. Adjust part size.
			partSize = Math.min(partSize, (contentLength - filePosition));

			// Create request to upload a part.
			UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).withKey(key)
					.withUploadId(initResponse.getUploadId()).withPartNumber(i).withFileOffset(filePosition)
					.withFile(file).withPartSize(partSize);

			uploadRequest.setGeneralProgressListener(new UploadProgressListener(file, i, partSize));

			// Upload part and add response to our list.
			MultiPartFileUploader uploader = new MultiPartFileUploader(uploadRequest);
			uploaders.add(uploader);
			uploader.upload();

			filePosition += partSize;
		}

		for (MultiPartFileUploader uploader : uploaders) {
			uploader.join();
			partETags.add(uploader.getPartETag());
		}

		// Step 3: complete.
		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key,
				initResponse.getUploadId(), partETags);

		s3Client.completeMultipartUpload(compRequest);
		/*
		 * catch (Throwable t) { logger.error(
		 * "Unable to put object as multipart to Amazon S3 for file " + key, t);
		 * s3Client.abortMultipartUpload( new AbortMultipartUploadRequest(
		 * bucketName, key, initResponse.getUploadId())); }
		 */
	}

	// ...

	private class UploadProgressListener implements ProgressListener {

		File file;
		int partNo;
		long partLength;

		@SuppressWarnings("unused")
		UploadProgressListener(File file) {
			this.file = file;
		}

		@SuppressWarnings("unused")
		UploadProgressListener(File file, int partNo) {
			this(file, partNo, 0);
		}

		UploadProgressListener(File file, int partNo, long partLength) {
			this.file = file;
			this.partNo = partNo;
			this.partLength = partLength;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void progressChanged(ProgressEvent progressEvent) {
			switch (progressEvent.getEventCode()) {
			case ProgressEvent.STARTED_EVENT_CODE:
				logger.info("Upload started for file " + "\"" + file.getName() + "\"");
				break;
			case ProgressEvent.COMPLETED_EVENT_CODE:
				logger.info("Upload completed for file " + "\"" + file.getName() + "\"" + ", " + file.length()
						+ " bytes data has been transferred");
				break;
			case ProgressEvent.FAILED_EVENT_CODE:
				logger.info("Upload failed for file " + "\"" + file.getName() + "\"" + ", "
						+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
				break;
			case ProgressEvent.CANCELED_EVENT_CODE:
				logger.info("Upload cancelled for file " + "\"" + file.getName() + "\"" + ", "
						+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
				break;
			case ProgressEvent.PART_STARTED_EVENT_CODE:
				logger.info("Upload started at " + partNo + ". part for file " + "\"" + file.getName() + "\"");
				break;
			case ProgressEvent.PART_COMPLETED_EVENT_CODE:
				logger.info("Upload completed at " + partNo + ". part for file " + "\"" + file.getName() + "\"" + ", "
						+ (partLength > 0 ? partLength : progressEvent.getBytesTransferred())
						+ " bytes data has been transferred");
				break;
			case ProgressEvent.PART_FAILED_EVENT_CODE:
				logger.info("Upload failed at " + partNo + ". part for file " + "\"" + file.getName() + "\"" + ", "
						+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
				break;
			}
		}

	}

	private class MultiPartFileUploader extends Thread {

		private UploadPartRequest uploadRequest;
		private PartETag partETag;
		private AmazonS3 s3Client1;

		MultiPartFileUploader(UploadPartRequest uploadRequest) {
			this.s3Client1 = s3Client;
			this.uploadRequest = uploadRequest;
		}

		@Override
		public void run() {
			partETag = s3Client1.uploadPart(uploadRequest).getPartETag();
		}

		private PartETag getPartETag() {
			return partETag;
		}

		private void upload() {
			start();
		}

	}

	/*
	 * public static void main(String args[]){ AmazonS3Util s3Util = new
	 * AmazonS3Util("AKIAJDULPKHZZ4KGTYVA",
	 * "dfG1QfHkJvTrBLzm9D2GTPdzHxIFy/qe4ObbgyiK");
	 * System.out.println("started..."+new Date());
	 * s3Util.putObjectAsMultiPart("anvi-dwp", new
	 * File("C:/Users/rakesh.gajula/Desktop/IL_Product1_Stg.csv"),
	 * "8/4091/IL_Product1_Stg.csv"); System.out.println("ended..."+new Date());
	 * }
	 */

	/*static String secretKey = "7R4nD0jJzxllmfKU758IACXLH6kZTpRqySZll73X";
	static String accessKey = "AKIAJ7DXBCEPL3EYAIKQ";
	static String bucketName = "anvizds";*/
	//static String bucketName = "anvi-dwp";
	static String secretKey = "rn2TCANx148dXDWAbDSjRY0IbcbkuPlnq2XrIHXw";
	static String accessKey = "AKIAJ55BN7UL3TI3WDNQ";
	static String bucketName = "anvi-dwp";
	
	/*static String secretKey = "rn2TCANx148dXDWAbDSjRY0IbcbkuPlnq2XrIHXw";
	static String accessKey = "AKIAJ55BN7UL3TI3WDNQ";
	static String bucketName = "anvizdev";*/
	
	public static void main(String[] args) {
		/*try {
			//String name = downloadFileFromS3("4/603/RXTX4k4G4FuAGKzNgiK2XHKh_6v0ymh6lp9_7OM6xnGR3uL1cdcvKVuB2hA2AnsDZWF5U30lxwfDyMz4H8k7Vw");
			System.out.println("Hello -- > "); 
			getS3FilesList("XnIAInEDVMXQAAnUyjD9eLWrxkJpYYf3hqUS0xjapy7k1ypenuNrna2gzE2xPxpz2jAcZZeivjEtwZ5H1772rZV-FzzDddSItLDdXmx3_TM");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			downloadFileFromS3("P8_hPE1NmqjfIo_5CoTYmufzhflMKUuh9z76aXyZjzdGle4B0XyHo1AeznFbWXrhFt0QBMQYfn00X9rAcBjDQA");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		/*try {
			encryptS3File("datafile_U22_P0_M45_IL8_DL0_20170328_081441_62_20170329_121714_935.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			//String sql = EncryptionServiceImpl.getInstance().decrypt("qkw-Kc5EqfNcS5fWYnBBEvNp2ZOqU2G_pDgY79Xx8kzWhQ98Y4jtbIsw2hYCMdu8GmmVOQTBdeivn6XyIxfui7oMQRfgby0JroU7WwXnNDR04rhWyOj9zllX4nAmTHlqjqz-0EBOIITnnWcwZXy6gklIDMnVkbyS8uTWdDJEGZ-p8zkd1I6ULPtXMyDjQ1RSIlbAJxdzrTmCZk13k4wrfncE7cvfPb-iuyqVYrza12gvaei89RjeXT3hu0UzO9quk9inINuLTX0igRfmqU8XUnhF38eBcW3ZOf0CRYHP0bE4qenOCMrXUmJk70W6P12S5n798CShNf9vXxnWuAWK8vfG3kKW8LOyjr2ZkpLgSSefd5Wiq2OQKj4ZBWrOBVI4aK4kz4ZYSf13GPudsOcW5h951ykldCH7K8w3vcDl27gnSct89UKKKMdcfU0IYRLLRZV4w3qcAGxeKilbSwx_QSd8kKkmKc_q4xV7KgefWOFS4LQKAtQEQINIctNnu_-JLkgreW9_q-JluhozWxLXDuqb7ew5GFAf0osKQDTCL3vtr4gK_7voUJ3Awj9Br9ydVj1Va2nU5Lg9Kg_VRFgrGpipEoF26uyq9te7s9c17Dko5Of4sth5NULl0bpaPff-eGyY1hSuOqg6zG8BER1CtOEpFlkBprX3MgkQrBfQQZo7fxG9k1ghSRzR1R0a3mq0L5sx3FWkg9thV4Ma0zdm2rlenplB6xBUqNeZ7B58517O5l54hkg_9xIhUwiyhzkdsy1Al8vDGs9tSvXwzXREjf57hbeefahrhjxp5ZZhxLVqj5iJZKIF9y0NNjf2xZ6ujy_ek9agaS7c4fP-TtvbE0GhQ79gr9lGPAtJKUOjxvLbDLkqw1i_C7jZyqQNl0IUX6eZS_5km7p2hweT5f0ea0Rlg0wmm9lyS_pCOXnjlO1SOqhODWVErAKkvMo0yIJfV9Xrz8PZqoOjddYljld6cV_lyHeesqbtkHfGa9yHBVtMSGnAdnzBvoxcLiiywe_5956oywByX5zGsccdkfKoRPZsMHGT8uWzITY_uinHU9UWzSolFvIX_uaw0vHFcknbfvQ5W2KnoA1KLSM7z3ZkMmK5yFeL_jnTDO_idrupalSGQmh2dYKj_YAuvsqw2c3AdphUsCKBGFVYwbJVNhb-URcIKU6GW0mET-lPEg9H5e9wfDsYFp2SS_2IR1YKqHD4O_VDoNBestBn5h6g1khuZw0KfzliWJ2Iv3JyS5FIwTgQZvddAeEE6u-lMHwfr7_vpI27O7lyTuYT-XaEpEFz6ywv1ToWMWUucRFf9GoPctB34tLDfRJl7VXilHx5q8dOC_940-rXch7sT2-R0AhLOmS7Lg5aTWy4YYFogiPr55usksz8fTM3wjtklYA9WusYmCujlAhXmsnrp-rco4qoHmFnxLsJXQg0XT92W-dGOqW8GHP5kCxrO818dtSDpqZmqiJ1pP2jBgwBQY8b13sP8lIDyh4Otl2en-qGs94wBosIHuuc444c7dXqGpq6NOnHWNFQgRiChgmtgrhsumowtfiA2yiK8Yg7Cni0D0Ie_MtAu1Odqzk4JUsrQWiDDLGwWg2p8G1CS-xeqnr7sG4DMo_1W-jhc1cRvq5L1fLYm_Z0bri_bOCfSeDaUbe9cLlwwTYMn-AABC1BUU0b2zOvd_PZtF8SkYO6h3bYjcqg7AbJhSgPLIE0vAHkulFROIdfb1mNOTZRW38DCifFhOh--8gts5z1LuNwTVpEHgmGLK8OfZ0ia_yqIa8GM4iPk1siTSv6Y5QrxBiEIoYsbmRhkIk_jydNsoQTS1P0O_ZSsxuouTH_8iphI0Q2nMSiTc0MMPwgnIwL70M0me-YKpHVD0U880vnzKkQpOeH87ebBvLvFTWhr73_hutv_bxdDfZO6DcqW8gbbVQQaTAm8hRsyv6mHCs1L8vOXrvWBOlLf1371fvyKRByTcrd3EnC54gxAoRKnWxiX6IMK_Yl7VdXFiB_PIM4_1qkkVa7oGmNT0eyMRzZtrU1leCtwt9SyenAr--_dZq-4nj_m4EB-boJic9yhgXLpYnRjO3Vfb8ai6edpf2p_plmMdt0kHeC_-lZNBPL1U8tj1b9XzbWRGArt3oFynT5T7rIMVDHltNFMNiT53aCHivLHQlOIR6KmJxwdHz4uu9VhDBvpy-1TMJgGkD7LntPkkZO6Fw5Y0jHsgzbRy4rN1Tli02vUT0pibWdC9cOfwo27YGXa6R24b9c6q0cVNwQIL4_oZ68lEEptYTC2ks1kIIkHJOvHjcntn-cnJfLzuO0Ol-HbTMIdZ1oZjWLHbb5cqwm-l7wIdkFs_FU2s5DCx9TXDG375hrpstkYoeOmNLOn4YnsAraV_1zLnqoXWzAHwH4NE3PJvNeEh4");
			//String sql = EncryptionServiceImpl.getInstance().decrypt("P1zCLHwLmpy0efL9mUPB3g==");
			/*String sql = AESConverter.decrypt("VgNP/jDo+pmLebuOgaYy2g==");
			System.out.println("-"+sql+"-"); */
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void getS3FilesList(String logicalPath) {
		// TODO Auto-generated method stub
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		 ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest()
                 .withBucketName(bucketName)
                 .withPrefix(logicalPath));
		 
		 for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
             System.out.println(" - " + objectSummary.getKey() + "  " +
                                "(size = " + objectSummary.getSize() + ")");
         }
		 
         System.out.println();
	}
	
	

	public static String downloadFileFromS3(String filePathInS3) throws Exception {

		logger.debug("in downloadFileFromS3");
		String downloadFileDir = null;
		String downloadFilePath = null;
		File decryptedFile = null;
		if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)
				&& StringUtils.isNotBlank(bucketName)) {

			downloadFileDir = CommonUtils.createDir("C:\\Users\\rajesh.anthari\\Desktop\\s3\\");
			File encrtptedFile = null;
			FileInputStream instream = null;
			FileOutputStream outstream = null;
			File tempDecryptedFile = null;
			try {
				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
				AmazonS3 s3client = new AmazonS3Client(credentials);
				// Step 1 : download the encrypted file
				logger.debug("downloading started from S3..." + filePathInS3);
				S3Object obj = s3client.getObject(new GetObjectRequest(bucketName, filePathInS3));
				String dir = CommonUtils.createDir(Constants.Temp.getTempFileDir());
				//String tempFileName = StringUtils.substringAfterLast(obj.getKey(), "/");
				String tempFileName = obj.getKey();

				encrtptedFile = new File(dir + tempFileName);
				outstream = new FileOutputStream(encrtptedFile);
				System.out.println("copying stream into temp file...");
				IOUtils.copy(obj.getObjectContent(), outstream);
				System.out.println("copying completed...");
				logger.debug("downloading file completed..." + filePathInS3);
				String encryptedfileName = encrtptedFile.getName();
				// decrypt the file name
				String decryptedFileName = EncryptionServiceImpl.getInstance().decrypt(encryptedfileName);
				if (StringUtils.isNotBlank(decryptedFileName)) {
					decryptedFile = new File(downloadFileDir + decryptedFileName);
					// decrypt the file content
					tempDecryptedFile = EncryptionServiceImpl.getInstance().decryptFile(encrtptedFile);

					if (tempDecryptedFile != null) {
						writeIntoFile(tempDecryptedFile, decryptedFile);
					}
					downloadFilePath = downloadFileDir + decryptedFileName;
				}
			} finally {

				try {
					if (instream != null) {
						instream.close();
					}
					if (outstream != null) {
						outstream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (encrtptedFile != null) {
					encrtptedFile.delete();
				}
				if (tempDecryptedFile != null) {
					tempDecryptedFile.delete();
				}
			}

		}
		return downloadFilePath;
	}
	
	
	
	public static String dycryptS3File(String filePathInS3) throws Exception {

		logger.debug("in dycryptS3File");
		String downloadFileDir = null;
		String downloadFilePath = null;
		File decryptedFile = null;
		if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)
				&& StringUtils.isNotBlank(bucketName)) {

			downloadFileDir = CommonUtils.createDir("C:\\Users\\rajesh.anthari\\Desktop\\s3\\");
			File encrtptedFile = null;
			File tempDecryptedFile = null;
			try {
				String dir = "C:\\Users\\rajesh.anthari\\Desktop\\";
				String tempFileName = filePathInS3;

				encrtptedFile = new File(dir + tempFileName);
				logger.debug("downloading file completed..." + filePathInS3);
				String encryptedfileName = encrtptedFile.getName();
				// decrypt the file name
				String decryptedFileName = EncryptionServiceImpl.getInstance().decrypt(encryptedfileName);
				if (StringUtils.isNotBlank(decryptedFileName)) {
					decryptedFile = new File(downloadFileDir + decryptedFileName);
					// decrypt the file content
					System.out.println("Decryption Start Time -- > "+new Date());
					tempDecryptedFile = EncryptionServiceImpl.getInstance().decryptFile(encrtptedFile);
					System.out.println("Ending Time -- > "+new Date());

					if (tempDecryptedFile != null) {
						writeIntoFile(tempDecryptedFile, decryptedFile);
					}
					downloadFilePath = downloadFileDir + decryptedFileName;
				}
			} finally {

			}

		}
		return downloadFilePath;
	}
	

	public static String encryptS3File(String filePathInS3) throws Exception {

		logger.debug("in dycryptS3File");
		String downloadFilePath = null;
		if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)
				&& StringUtils.isNotBlank(bucketName)) {

			File encrtptedFile = null;
			File tempDecryptedFile = null;
			try {
				String dir = "C:\\Users\\Rajesh.anthari\\Desktop\\s3\\";
				String tempFileName = filePathInS3;

				encrtptedFile = new File(dir + tempFileName);
				logger.debug("downloading file completed..." + filePathInS3);
				tempDecryptedFile = EncryptionServiceImpl.getInstance().encryptFile(encrtptedFile);
				System.out.println(tempDecryptedFile);
				
			} finally {

			}

		}
		return downloadFilePath;
	}
	

	public static void writeIntoFile(File source, File dest) throws IOException {
		FileInputStream instream = null;
		FileOutputStream outstream = null;
		try {
			instream = new FileInputStream(source);
			outstream = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (outstream != null) {
				try {
					outstream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
