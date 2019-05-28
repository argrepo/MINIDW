package com.anvizent.amazon;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;

public class UploadProgressListener implements ProgressListener {
	protected static final Log LOGGER = LogFactory.getLog(UploadProgressListener.class);
	File file;
	int partNo;
	long partLength;

	UploadProgressListener(File file) {
		this.file = file;
	}

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
			LOGGER.debug("Upload started for file " + "\"" + file.getName() + "\"");
			break;
		case ProgressEvent.COMPLETED_EVENT_CODE:
			LOGGER.debug("Upload completed for file " + "\"" + file.getName() + "\"" + ", " + file.length()
					+ " bytes data has been transferred");
			break;
		case ProgressEvent.FAILED_EVENT_CODE:
			LOGGER.debug("Upload failed for file " + "\"" + file.getName() + "\"" + ", "
					+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
			break;
		case ProgressEvent.CANCELED_EVENT_CODE:
			LOGGER.debug("Upload cancelled for file " + "\"" + file.getName() + "\"" + ", "
					+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
			break;
		case ProgressEvent.PART_STARTED_EVENT_CODE:
			LOGGER.debug("Upload started at " + partNo + ". part for file " + "\"" + file.getName() + "\"");
			break;
		case ProgressEvent.PART_COMPLETED_EVENT_CODE:
			LOGGER.debug("Upload completed at " + partNo + ". part for file " + "\"" + file.getName() + "\"" + ", "
					+ (partLength > 0 ? partLength : progressEvent.getBytesTransferred())
					+ " bytes data has been transferred");
			break;
		case ProgressEvent.PART_FAILED_EVENT_CODE:
			LOGGER.debug("Upload failed at " + partNo + ". part for file " + "\"" + file.getName() + "\"" + ", "
					+ progressEvent.getBytesTransferred() + " bytes data has been transferred");
			break;
		}
	}

}