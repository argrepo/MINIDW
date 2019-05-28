package com.anvizent.minidw.service.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.InstanceInfo;

public class AwsEC2Utilities {
	
	public AmazonEC2 getAmazonEc2Client(AwsCredentials awsCredential) {
		AWSCredentials credentials = new BasicAWSCredentials(awsCredential.getAccessKey(), awsCredential.getSecretKey());
		AWSCredentialsProvider credentialsProvider = new  AWSStaticCredentialsProvider(credentials);//aws = new 
		return  AmazonEC2ClientBuilder.standard()
                .withRegion(awsCredential.getRegion()).withCredentials(credentialsProvider)
                .build();
	}
	
	public void describeInstanceStatus(AmazonEC2 ec2 ) {
		DescribeInstanceStatusResult instanceResult = ec2.describeInstanceStatus();
		System.out.println(instanceResult);
		
	}
	
	public InstanceInfo getInstanceDetails(AwsCredentials awsCredential, String instanceId ) throws AmazonEC2Exception {
		AmazonEC2 ec2 = getAmazonEc2Client(awsCredential);
		DescribeInstancesResult instanceResult = ec2.describeInstances();
		Reservation reservation = instanceResult.getReservations().stream().filter(reserva -> reserva.getInstances().get(0).getInstanceId().equals(instanceId) ).findAny().orElse(null);
		if (reservation == null ) {
			throw new AmazonEC2Exception("Instance " + instanceId + " details not found");
		}
		System.out.println(reservation);
		Instance instance = reservation.getInstances().get(0);
		InstanceInfo instanceInfo = new InstanceInfo();
		instanceInfo.setAws(awsCredential);
		instanceInfo.setInstanceId(instanceId);
		instanceInfo.setInstanceType(instance.getInstanceType());
		instanceInfo.setPrivateIpAddress(instance.getPrivateIpAddress());
		instanceInfo.setPublicIpAddress(instance.getPublicIpAddress());
		instanceInfo.setName(instance.getKeyName());
		instanceInfo.setInstanceState(instance.getState().getName());
		instanceInfo.setAvailabilityZone( instance.getPlacement().getAvailabilityZone() );
		return instanceInfo;
	}
	
	public String startInstanceStatus(AwsCredentials awsCredential,String instanceId) {
		AmazonEC2 ec2 = getAmazonEc2Client(awsCredential);
		StartInstancesRequest startInstancesRequest = new StartInstancesRequest();
		startInstancesRequest.withInstanceIds(instanceId);
		StartInstancesResult result = ec2.startInstances(startInstancesRequest);
		return result.getStartingInstances().get(0).getCurrentState().getName();
	}
	
	public String stopInstanceStatus(AwsCredentials awsCredential,String instanceId) {
		AmazonEC2 ec2 = getAmazonEc2Client(awsCredential);
		StopInstancesRequest stopInstancesRequest = new StopInstancesRequest();
		stopInstancesRequest.withInstanceIds(instanceId);
		StopInstancesResult result = ec2.stopInstances(stopInstancesRequest);
		return result.getStoppingInstances().get(0).getCurrentState().getName();
		
	}
}

