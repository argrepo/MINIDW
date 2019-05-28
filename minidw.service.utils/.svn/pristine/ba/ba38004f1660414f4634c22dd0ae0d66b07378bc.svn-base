/*package com.anvizent.minidw.service.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.datamodel.anvizent.service.model.EmailDetails;

public class EmailUtility {
	EmailDetails emailDetails = new EmailDetails();
	
	
	void sendMail(String body) {
		sendMail(body, "from minidw");
	}
	

	void sendMail(String body, String subject) {
		sendMail(body, subject, true);
	}
	
	
	void sendMail(String body, String subject, boolean isHtml) {
		emailDetails.getMessageDetails().setBody(body);
		emailDetails.getMessageDetails().setSubject(subject);
		emailDetails.getMessageDetails().setHtml(isHtml);
		emailDetails.setProperties(getMailProperties());
		sendMail();
	}
	
	
	public void sendMail() {
		if (emailDetails != null) {
			Session session = getSession(emailDetails.getProperties(), emailDetails);

			MimeMessage message = getMessage(session, emailDetails.getMessageDetails().getBody(), emailDetails);

			try {
				Transport.send(message);
				System.out.println("message sent successfully");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public Properties getMailProperties() {
		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		return props;
	}

	
	private Session getSession(Properties props, EmailDetails emailDetails) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailDetails.getFromAddress(), emailDetails.getPassword());
			}
		});
		return session;
	}
	

	private MimeMessage getMessage(Session session, String body, EmailDetails emailDetails) {
		MimeMessage message = new MimeMessage(session);

		String systemAddress;
		try {
			systemAddress = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e1) {
			systemAddress = "IP Address not available";
		}
		
		
		try {
			String totalBody = "Message: " + body + "\n" + "System Address: " + systemAddress;
			if (emailDetails.getMessageDetails().getBccList() != null) {
				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailDetails.getMessageDetails().getBccList()));
			}
			if (emailDetails.getMessageDetails().getCcList() != null) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(emailDetails.getMessageDetails().getCcList()));
			}
			if (emailDetails.getReplyToAddress() != null) {
				message.setReplyTo(InternetAddress.parse(emailDetails.getReplyToAddress()));
			}
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDetails.getMessageDetails().getToList()));
			message.setSubject(emailDetails.getMessageDetails().getSubject());
			message.setText(totalBody);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}
	
	public static void main(String[] args) {
		EmailUtility emailUtility = new EmailUtility();
		emailUtility.getEmailDetails().setFromAddress("rajesh.anthari@anvizent.com");
		emailUtility.getEmailDetails().setPassword("S7Zq4GT*S$");
		emailUtility.getEmailDetails().getMessageDetails().setToList("rajesh.anthari@arghainc.com");
		emailUtility.sendMail("<b> Hello </b> Rajesh, This is sample email for testing", "Sample Mail 1 ", true);
		
		SimpleMailMessage smt = new SimpleMailMessage();
		
		
		
		
	}
	
	public JavaMailSender getJavaMailSender() {
	    JavaMailSender mailSender = new JavaMailSenderImpl;
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("my.gmail@gmail.com");
	    mailSender.setPassword("password");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	

	public EmailDetails getEmailDetails() {
		return emailDetails;
	}

	public void setEmailDetails(EmailDetails emailDetails) {
		this.emailDetails = emailDetails;
	}

	
}
*/