package com.datamodel.anvizent.service.model;

public class FileSettings {
private String clientId;
private Integer maxFileSizeInMb;
private Long trailUserMaxFileSizeInMb;
private Boolean multiPartEnabled;
private Integer noOfRecordsPerFile;
private Modification modification;
private Boolean fileEncryption;

public String getClientId() {
	return clientId;
}
public void setClientId(String clientId) {
	this.clientId = clientId;
}
public Integer getMaxFileSizeInMb() {
	return maxFileSizeInMb;
}
public void setMaxFileSizeInMb(Integer maxFileSizeInMb) {
	this.maxFileSizeInMb = maxFileSizeInMb;
}
public Long getTrailUserMaxFileSizeInMb() {
	return trailUserMaxFileSizeInMb;
}
public void setTrailUserMaxFileSizeInMb(Long trailUserMaxFileSizeInMb) {
	this.trailUserMaxFileSizeInMb = trailUserMaxFileSizeInMb;
}
public Boolean getMultiPartEnabled() {
	return multiPartEnabled;
}
public void setMultiPartEnabled(Boolean multiPartEnabled) {
	this.multiPartEnabled = multiPartEnabled;
}
public Integer getNoOfRecordsPerFile() {
	return noOfRecordsPerFile;
}
public void setNoOfRecordsPerFile(Integer noOfRecordsPerFile) {
	this.noOfRecordsPerFile = noOfRecordsPerFile;
}
public Modification getModification() {
	return modification;
}
public void setModification(Modification modification) {
	this.modification = modification;
}
public Boolean getFileEncryption() {
	return fileEncryption;
}
public void setFileEncryption(Boolean fileEncryption) {
	this.fileEncryption = fileEncryption;
}
@Override
public String toString() {
	return "FileSettings [clientId=" + clientId + ", maxFileSizeInMb=" + maxFileSizeInMb + ", trailUserMaxFileSizeInMb="
			+ trailUserMaxFileSizeInMb + ", multiPartEnabled=" + multiPartEnabled + ", noOfRecordsPerFile="
			+ noOfRecordsPerFile + ", modification=" + modification + ", fileEncryption=" + fileEncryption + "]";
}
 

}
