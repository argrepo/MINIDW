package com.anvizent.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class MultiPartFileUploader extends Thread {

	
	private UploadPartRequest uploadRequest;
	private PartETag partETag;
	private AmazonS3 s3Client;

	MultiPartFileUploader(UploadPartRequest uploadRequest,AmazonS3 s3Client) {
		this.s3Client = s3Client;
		this.uploadRequest = uploadRequest;
	}

	@Override
	public void run() {
		partETag = s3Client.uploadPart(uploadRequest).getPartETag();
	}

	protected PartETag getPartETag() {
		return partETag;
	}

	protected void upload() {
		start();
	}

}
