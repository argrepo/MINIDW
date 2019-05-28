package com.datamodel.anvizent.common.spring.email;

import java.util.Date;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Portal email services.
 * 
 * @author bryanross
 *
 */
final public class EmailServiceImpl implements EmailService {
	protected final static Log LOG = LogFactory.getLog(EmailServiceImpl.class);

	private JavaMailSender mailSender;
	private EmailConfig emailConfig;
	private String appId;
	private VelocityEngine velocity;

	public EmailServiceImpl(String appId, JavaMailSender mailSender, EmailConfig emailConfig) {
		this.appId = appId;
		this.mailSender = mailSender;
		this.emailConfig = emailConfig;
	}

	public EmailServiceImpl(String appId, JavaMailSender mailSender, EmailConfig emailConfig, VelocityEngine velocityEngine) {
		this(appId, mailSender, emailConfig);
		this.velocity = velocityEngine;
	}

	/**
	 * Alert the administrator.
	 * 
	 * @param message
	 */
	public void alertAdministrator(String message) throws EmailException {
		alertAdministrator(message, null);
	}

	/**
	 * Alert the administrator.
	 * 
	 * @param message
	 * @param ex
	 */
	public void alertAdministrator(String message, Throwable ex) throws EmailException {
		SimpleMailMessage email = null;
		try {
			email = new SimpleMailMessage();
			if (!StringUtils.isEmpty(emailConfig.getAdministrator()) && !StringUtils.isEmpty(emailConfig.getFrom())) {
				email.setTo(emailConfig.getAdministrator());
				email.setFrom(emailConfig.getFrom());
				email.setSubject(String.format("ALERT: %s", appId));
				StringBuilder sb = new StringBuilder();
				sb.append(message).append("\n\n");
				if (ex != null)
					sb.append(ex.toString());
				email.setText(sb.toString());
				this.mailSender.send(email);
			}
		} catch (MailException e) {
			String msg = "Unable to prepare administrator alert: " + message;
			LOG.error(msg, e);
			throw new EmailException(msg, e);
		}
	}

	@Override
	public void sendVelocityEmail(final VelocityEmailMessage velocityEmailMessage, final Map<String, Object> modelMap) throws EmailException {

		if (velocity == null || velocityEmailMessage == null) {
			throw new IllegalArgumentException("Invalid parameter");
		}

		try {

			MimeMessagePreparator preparator = new MimeMessagePreparator() {

				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(velocityEmailMessage.getTo());

					if (velocityEmailMessage.getCc() != null && velocityEmailMessage.getCc().length > 0) {
						message.setCc(velocityEmailMessage.getCc());
					}

					if (velocityEmailMessage.getBcc() != null && velocityEmailMessage.getBcc().length > 0) {
						message.setBcc(velocityEmailMessage.getBcc());
					}

					if (message.getMimeMessage().getFrom() == null || message.getMimeMessage().getFrom().length == 0) {

						message.setFrom(EmailConfig.DO_NOT_REPLY, EmailConfig.DO_NOT_REPLY_ALIAS);

					}

					message.setSubject(velocityEmailMessage.getSubject());
					message.setSentDate(new Date());
					String text = VelocityEngineUtils.mergeTemplateIntoString(velocity, velocityEmailMessage.getTemplate(), velocityEmailMessage.getEncoding(),
							modelMap);
					message.setText(text, true);
				}
			};

			mailSender.send(preparator);

		} catch (MailException e) {
			String msg = "Unable to prepare send velocity email: " + velocityEmailMessage.getTemplate();
			LOG.error(msg, e);
			throw new EmailException(msg, e);
		}
	}
}
