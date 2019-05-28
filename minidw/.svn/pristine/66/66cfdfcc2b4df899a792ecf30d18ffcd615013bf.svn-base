package com.anvizent.client.scheduler.util;

import java.util.Properties;

import javax.mail.MessagingException;
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
	
	
	public void sendSchedulersInterruption(String errorInfo,String accessKey,String clientIds,String metaInfo) throws Exception{
		
		String htmlContent = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta name='viewport' content='width=device-width'/><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style>*{margin:0;padding:0;}*{font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;}img{max-width: 100%;}.collapse{margin:0;padding:0;}body{-webkit-font-smoothing:antialiased; -webkit-text-size-adjust:none; width: 100%!important; height: 100%;}a{color: #2BA6CB;}.btn{text-decoration:none;color: #FFF;background-color: #666;padding:10px 16px;font-weight:bold;margin-right:10px;text-align:center;cursor:pointer;display: inline-block;}p.callout{padding:15px;background-color:#ECF8FF;margin-bottom: 15px;}.callout a{font-weight:bold;color: #2BA6CB;}table.social{background-color: #ebebeb;}.social .soc-btn{padding: 3px 7px;font-size:12px;margin-bottom:10px;text-decoration:none;color: #FFF;font-weight:bold;display:block;text-align:center;}a.fb{background-color: #3B5998!important;}a.tw{background-color: #1daced!important;}a.gp{background-color: #DB4A39!important;}a.ms{background-color: #000!important;}.sidebar .soc-btn{display:block;width:100%;}table.head-wrap{width: 100%;}.header.container table td.logo{padding: 15px;}.header.container table td.label{padding: 15px; padding-left:0px;}table.footer-wrap{width: 100%;clear:both!important;}.footer-wrap .container td.content p{border-top: 1px solid rgb(215,215,215); padding-top:15px;}.footer-wrap .container td.content p{font-size:10px;font-weight: bold;}h1,h2,h3,h4,h5,h6{font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; line-height: 1.1; margin-bottom:15px; color:#000;}h1 small, h2 small, h3 small, h4 small, h5 small, h6 small{font-size: 60%; color: #6f6f6f; line-height: 0; text-transform: none;}h1{font-weight:200; font-size: 44px;}h2{font-weight:200; font-size: 37px;}h3{font-weight:500; font-size: 27px;}h4{font-weight:500; font-size: 23px;}h5{font-weight:900; font-size: 17px;}h6{font-weight:900; font-size: 14px; text-transform: uppercase; color:#444;}.collapse{margin:0!important;}p, ul{margin-bottom: 10px; font-weight: normal; font-size:14px; line-height:1.6;}p.lead{font-size:17px;}p.last{margin-bottom:0px;}ul li{margin-left:5px;list-style-position: inside;}ul.sidebar{background:#ebebeb;display:block;list-style-type: none;}ul.sidebar li{display: block; margin:0;}ul.sidebar li a{text-decoration:none;color: #666;padding:10px 16px;margin-right:10px;cursor:pointer;border-bottom: 1px solid #777777;border-top: 1px solid #FFFFFF;display:block;margin:0;}ul.sidebar li a.last{border-bottom-width:0px;}ul.sidebar li a h1,ul.sidebar li a h2,ul.sidebar li a h3,ul.sidebar li a h4,ul.sidebar li a h5,ul.sidebar li a h6,ul.sidebar li a p{margin-bottom:0!important;}.container{display:block!important;max-width:600px!important;margin:0 auto!important; /* makes it centered */clear:both!important;}.content{padding:15px;max-width:600px;margin:0 auto;display:block;}.content table{width: 100%;}.column{width: 300px;float:left;}.column tr td{padding: 15px;}.column-wrap{padding:0!important; margin:0 auto; max-width:600px!important;}.column table{width:100%;}.social .column{width: 280px;min-width: 279px;float:left;}.clear{display: block; clear: both;}@media only screen and (max-width: 600px){a[class='btn']{display:block!important; margin-bottom:10px!important; background-image:none!important; margin-right:0!important;}div[class='column']{width: auto!important; float:none!important;}table.social div[class='column']{width:auto!important;}}</style></head><body bgcolor='#FFFFFF'><table class='body-wrap'><tr><td class='container' bgcolor='#FFFFFF'><div class='content'><table><tr><td><h3>Hi Team</h3><p class='lead'>Hybrid Scheduler unable to update the data due to webservice connectivity issue.</p><h4> Date:- #systemDate </h4> <h3> Scheduler details:- </h3><table class='body-wrap'><tr><td bgcolor='#FFFFFF'>Access Key</td><td bgcolor='#FFFFFF'>#AccessKey</td></tr><tr><td bgcolor='#FFFFFF'>Client Ids</td><td bgcolor='#FFFFFF'>#ClientIds</td></tr><tr><td bgcolor='#FFFFFF'>Meta info</td><td bgcolor='#FFFFFF'>#metaInfo</td></tr></table><br/><h3> Error Information:- </h3><pre>#errorInfo</pre><br/><h3> System Information:- </h3><pre>#systemInfo</pre><br><br><br><p>Thank you, <br>Anvizent Team,</p></td></tr></table></div></td></tr></table></body></html>";
		htmlContent = StringUtils.replace(htmlContent, "#AccessKey", accessKey);
		htmlContent = StringUtils.replace(htmlContent, "#ClientIds", clientIds);
		htmlContent = StringUtils.replace(htmlContent, "#metaInfo", metaInfo);
		htmlContent = StringUtils.replace(htmlContent, "#errorInfo", errorInfo);
		htmlContent = StringUtils.replace(htmlContent, "#systemDate", TimeZoneDateHelper.getFormattedDateString());
		htmlContent = StringUtils.replace(htmlContent, "#systemInfo", getSystemInfo());
		
		sendEmail(htmlContent, "Hybrid scheduler failed - " + accessKey);
		
	}
	
	public void sendSchedulerRestartActivation(String accessKey,String clientIds,String metaInfo) throws Exception{
		
		String htmlContent = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta name='viewport' content='width=device-width'/><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/><style>*{margin:0;padding:0;}*{font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;}img{max-width: 100%;}.collapse{margin:0;padding:0;}body{-webkit-font-smoothing:antialiased; -webkit-text-size-adjust:none; width: 100%!important; height: 100%;}a{color: #2BA6CB;}.btn{text-decoration:none;color: #FFF;background-color: #666;padding:10px 16px;font-weight:bold;margin-right:10px;text-align:center;cursor:pointer;display: inline-block;}p.callout{padding:15px;background-color:#ECF8FF;margin-bottom: 15px;}.callout a{font-weight:bold;color: #2BA6CB;}table.social{background-color: #ebebeb;}.social .soc-btn{padding: 3px 7px;font-size:12px;margin-bottom:10px;text-decoration:none;color: #FFF;font-weight:bold;display:block;text-align:center;}a.fb{background-color: #3B5998!important;}a.tw{background-color: #1daced!important;}a.gp{background-color: #DB4A39!important;}a.ms{background-color: #000!important;}.sidebar .soc-btn{display:block;width:100%;}table.head-wrap{width: 100%;}.header.container table td.logo{padding: 15px;}.header.container table td.label{padding: 15px; padding-left:0px;}table.body-wrap{}table.footer-wrap{width: 100%;clear:both!important;}.footer-wrap .container td.content p{border-top: 1px solid rgb(215,215,215); padding-top:15px;}.footer-wrap .container td.content p{font-size:10px;font-weight: bold;}h1,h2,h3,h4,h5,h6{font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; line-height: 1.1; margin-bottom:15px; color:#000;}h1 small, h2 small, h3 small, h4 small, h5 small, h6 small{font-size: 60%; color: #6f6f6f; line-height: 0; text-transform: none;}h1{font-weight:200; font-size: 44px;}h2{font-weight:200; font-size: 37px;}h3{font-weight:500; font-size: 27px;}h4{font-weight:500; font-size: 23px;}h5{font-weight:900; font-size: 17px;}h6{font-weight:900; font-size: 14px; text-transform: uppercase; color:#444;}.collapse{margin:0!important;}p, ul{margin-bottom: 10px; font-weight: normal; font-size:14px; line-height:1.6;}p.lead{font-size:17px;}p.last{margin-bottom:0px;}ul li{margin-left:5px;list-style-position: inside;}ul.sidebar{background:#ebebeb;display:block;list-style-type: none;}ul.sidebar li{display: block; margin:0;}ul.sidebar li a{text-decoration:none;color: #666;padding:10px 16px;margin-right:10px;cursor:pointer;border-bottom: 1px solid #777777;border-top: 1px solid #FFFFFF;display:block;margin:0;}ul.sidebar li a.last{border-bottom-width:0px;}ul.sidebar li a h1,ul.sidebar li a h2,ul.sidebar li a h3,ul.sidebar li a h4,ul.sidebar li a h5,ul.sidebar li a h6,ul.sidebar li a p{margin-bottom:0!important;}.container{display:block!important;max-width:600px!important;margin:0 auto!important; /* makes it centered */clear:both!important;}.content{padding:15px;max-width:600px;margin:0 auto;display:block;}.content table{width: 100%;}.column{width: 300px;float:left;}.column tr td{padding: 15px;}.column-wrap{padding:0!important; margin:0 auto; max-width:600px!important;}.column table{width:100%;}.social .column{width: 280px;min-width: 279px;float:left;}.clear{display: block; clear: both;}@media only screen and (max-width: 600px){a[class='btn']{display:block!important; margin-bottom:10px!important; background-image:none!important; margin-right:0!important;}div[class='column']{width: auto!important; float:none!important;}table.social div[class='column']{width:auto!important;}}</style></head><body bgcolor='#FFFFFF'><table class='body-wrap'><tr><td class='container' bgcolor='#FFFFFF'><div class='content'><table><tr><td><h3>Hi Team</h3><p class='lead'><b>Hybrid Scheduler restarted</b> after webservice connectivity</p><h4> Date:- #systemDate </h4> <h3> Scheduler details:- </h3><table class='body-wrap'><tr><td bgcolor='#FFFFFF'>Access Key</td><td bgcolor='#FFFFFF'>#AccessKey</td></tr><tr><td bgcolor='#FFFFFF'>Client Ids</td><td bgcolor='#FFFFFF'>#ClientIds</td></tr><tr><td bgcolor='#FFFFFF'>Meta info</td><td bgcolor='#FFFFFF'>#metaInfo</td></tr></table><br/><h3> System Information:- </h3><pre>#systemInfo</pre><br><br><p>Thank you, <br>Anvizent Team,</p></td></tr></table></div></td></tr></table></body></html>";
		htmlContent = StringUtils.replace(htmlContent, "#AccessKey", accessKey);
		htmlContent = StringUtils.replace(htmlContent, "#ClientIds", clientIds);
		htmlContent = StringUtils.replace(htmlContent, "#metaInfo", metaInfo);
		htmlContent = StringUtils.replace(htmlContent, "#systemDate", TimeZoneDateHelper.getFormattedDateString());
		htmlContent = StringUtils.replace(htmlContent, "#systemInfo", getSystemInfo());
		
		sendEmail(htmlContent, "Hybrid scheduler Restarted - " + accessKey);
		
	}
	
	
	void sendEmail(String body,String subject) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(toAddress);
		if ( ccAddress != null && ccAddress.length > 0 ) {
			helper.setCc(ccAddress);
		}
		helper.setSubject(subject);
		helper.setText(body, true);
		sender.send(message);
	}
	
	String getSystemInfo() {
		String nameOS = "os.name";
		String versionOS = "os.version";
		String architectureOS = "os.arch";
		StringBuilder sysInfo = new StringBuilder();
		sysInfo.append("\nName of the OS: " + System.getProperty(nameOS));
		sysInfo.append("\nVersion of the OS: " + System.getProperty(versionOS));
		sysInfo.append("\nArchitecture of The OS: " + System.getProperty(architectureOS));
		return sysInfo.toString();
	}
	
	
	
}
