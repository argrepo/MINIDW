package com.anvizent.minidw.service.utils;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.security.EncryptionServiceImpl;

public class EmailSender {
	JavaMailSenderImpl sender = new JavaMailSenderImpl();
	String[] toAddress = null;
	String[] ccAddress = null;
	
	public EmailSender(Properties mailProperties) {
		String toList = mailProperties.getProperty("anvizent.mail.to");
		if ( StringUtils.isNotBlank(toList) ) {
			toAddress = StringUtils.split(toList,",");
		}
		String ccList = mailProperties.getProperty("anvizent.mail.cc");
		if ( StringUtils.isNotBlank(ccList) ) {
			ccAddress = StringUtils.split(ccList,",");
		}
		sender.setJavaMailProperties(mailProperties);
		sender.setUsername(mailProperties.getProperty("anvizent.mail.smtp.username"));
		try {
			String password = mailProperties.getProperty("anvizent.mail.smtp.password");
			password = EncryptionServiceImpl.getInstance().decrypt(password);
			sender.setPassword(password);
		} catch (Exception e) {
			new SchedulerException("Unable to decrypt mail password");
		}
		
	}
	 
	public void sendUploadErrorDetails(String dlName,String errorInfo,String clientId,int executionId,int packageId ,int dlId,String enviromentURL) throws Exception{
		
		String packageIdOrDlId = "";
		if(packageId == 0)
		{
			packageIdOrDlId += " DL Id :"+dlId;
		}else{
			packageIdOrDlId += " Package Id :"+packageId;
		}
		
		 StringBuilder detailedInfo = new StringBuilder();
		 detailedInfo.append("Hi Team,").append("\r\n\n\n");
		 detailedInfo.append("Execution Failed for ").append(dlName).append("\r\n");
		 detailedInfo.append("Detailed Information is as given below:").append("\r\n");
		 detailedInfo.append("Date:-").append(TimeZoneDateHelper.getFormattedDateString()).append("\r\n");
		 detailedInfo.append("Environment :-").append(enviromentURL).append("\r\n");
		 detailedInfo.append("Details :-").append(" clientId: "+clientId+packageIdOrDlId+" ExecutionId:"+executionId).append("\r\n\n");
		 detailedInfo.append(errorInfo).append("\r\n\n\n");
		 detailedInfo.append("Thank you,").append("\r\n");
		 detailedInfo.append("Anvizent Team.").append("\r\n");
		 
		 sendEmail(detailedInfo.toString(), "Alert - Upload Failed"+" clientId: "+clientId+packageIdOrDlId+" ExecutionId:"+executionId);
		
	}
  public void sendExecutionErrorDetails(String dlName,String errorInfo,String clientId,int executionId,int packageId ,int dlId,String enviromentURL) throws Exception{
	    String packageIdOrDlId = "";
	    if(packageId == 0)
		{
			packageIdOrDlId += " DL Id :"+dlId;
		}else{
			packageIdOrDlId += " Package Id :"+packageId;
		}
	 
		 StringBuilder detailedInfo = new StringBuilder();
		 detailedInfo.append("Hi Team,").append("\r\n\n\n");
		 detailedInfo.append("Execution Failed for ").append(dlName).append("\r\n");
		 detailedInfo.append("Detailed Information is as given below:").append("\r\n");
		 detailedInfo.append("Date:-").append(TimeZoneDateHelper.getFormattedDateString()).append("\r\n");
		 detailedInfo.append("Environment :-").append(enviromentURL).append("\r\n");
		 detailedInfo.append("Details :-").append(" clientId: "+clientId+packageIdOrDlId+" ExecutionId:"+executionId).append("\r\n\n");
		 detailedInfo.append(errorInfo).append("\r\n\n\n");
		 detailedInfo.append("Thank you,").append("\r\n");
		 detailedInfo.append("Anvizent Team.").append("\r\n");
		
		 sendEmail(detailedInfo.toString(), "Alert - Execution Failed"+" clientId: "+clientId+packageIdOrDlId+" ExecutionId:"+executionId);
		
	}
	
	void sendEmail(String body,String subject) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(toAddress);
		if ( ccAddress != null && ccAddress.length > 0 ) {
			helper.setCc(ccAddress);
		}
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/plain");
		message.setText(body);
		helper.setSubject(subject);
		helper.setText(body, true);
		sender.send(message);
	}
	
	
}
