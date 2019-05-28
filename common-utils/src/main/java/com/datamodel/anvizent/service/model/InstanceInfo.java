package com.datamodel.anvizent.service.model;

public class InstanceInfo {
	private String instanceId;
	private String name;
	private String privateIpAddress;
	private String publicIpAddress;
	private String instanceState;
	private String instanceType;
	private String availabilityZone;
	private AwsCredentials aws;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrivateIpAddress() {
		return privateIpAddress;
	}
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
	public String getPublicIpAddress() {
		return publicIpAddress;
	}
	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
	}
	public String getInstanceState() {
		return instanceState;
	}
	public void setInstanceState(String instanceState) {
		this.instanceState = instanceState;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getAvailabilityZone() {
		return availabilityZone;
	}
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	public AwsCredentials getAws() {
		return aws;
	}
	public void setAws(AwsCredentials aws) {
		this.aws = aws;
	}
	@Override
	public String toString() {
		return "InstanceInfo [instanceId=" + instanceId + ", name=" + name + ", privateIpAddress=" + privateIpAddress + ", publicIpAddress=" + publicIpAddress
				+ ", instanceState=" + instanceState + ", instanceType=" + instanceType + ", availabilityZone=" + availabilityZone + ", aws=" + aws + "]";
	}
		
}
