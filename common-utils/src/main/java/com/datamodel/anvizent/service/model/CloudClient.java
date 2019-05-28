package com.datamodel.anvizent.service.model;

public class CloudClient {

	long id, licenseId, regionId, clusterId, partnerId;
	String clientName, emailDomainName, clientDescription, address, contactPersonEmergencyNumber, contactPersonEmail, contactPersonName, dateCreated,
			activationDate, lastModified, clientDbSchema, clientDbUserName, clientDbPassword, deploymentType;
	boolean isActive, isDruidEnabled;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(long licenseId) {
		this.licenseId = licenseId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getEmailDomainName() {
		return emailDomainName;
	}

	public void setEmailDomainName(String emailDomainName) {
		this.emailDomainName = emailDomainName;
	}

	public String getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPersonEmergencyNumber() {
		return contactPersonEmergencyNumber;
	}

	public void setContactPersonEmergencyNumber(String contactPersonEmergencyNumber) {
		this.contactPersonEmergencyNumber = contactPersonEmergencyNumber;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getClientDbSchema() {
		return clientDbSchema;
	}

	public void setClientDbSchema(String clientDbSchema) {
		this.clientDbSchema = clientDbSchema;
	}

	public String getClientDbUserName() {
		return clientDbUserName;
	}

	public void setClientDbUserName(String clientDbUserName) {
		this.clientDbUserName = clientDbUserName;
	}

	public String getClientDbPassword() {
		return clientDbPassword;
	}

	public void setClientDbPassword(String clientDbPassword) {
		this.clientDbPassword = clientDbPassword;
	}

	public String getDeploymentType() {
		return deploymentType;
	}

	public void setDeploymentType(String deploymentType) {
		this.deploymentType = deploymentType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isDruidEnabled() {
		return isDruidEnabled;
	}

	public void setDruidEnabled(boolean isDruidEnabled) {
		this.isDruidEnabled = isDruidEnabled;
	}

	@Override
	public String toString() {
		return "CloudClient [id=" + id + ", licenseId=" + licenseId + ", regionId=" + regionId + ", clusterId=" + clusterId + ", partnerId=" + partnerId
				+ ", clientName=" + clientName + ", emailDomainName=" + emailDomainName + ", clientDescription=" + clientDescription + ", address=" + address
				+ ", contactPersonEmergencyNumber=" + contactPersonEmergencyNumber + ", contactPersonEmail=" + contactPersonEmail + ", contactPersonName="
				+ contactPersonName + ", dateCreated=" + dateCreated + ", activationDate=" + activationDate + ", lastModified=" + lastModified
				+ ", clientDbSchema=" + clientDbSchema + ", clientDbUserName=" + clientDbUserName + ", clientDbPassword=" + clientDbPassword
				+ ", deploymentType=" + deploymentType + ", isActive=" + isActive + ", isDruidEnabled=" + isDruidEnabled + "]";
	}

}
