package com.onemenu.server.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.cache.SignUpTokenCache;
import com.onemenu.server.constant.ParameterConstant.ConfigKey;
import com.onemenu.server.entity.SignUpTokenObj;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.CustomerFeedback;
import com.onemenu.server.model.Driver;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.Restaurant;


public class EmailUtils {

    private static Logger LOGGER = Logger.getLogger(EmailUtils.class);

    // Gmail
    private static String onemenuMailHostName = "smtp.gmail.com";
    private static String onemenuMailAddress = "onemenu@onemenu.mobi";
    private static String onemenuPassword = "onemenu31415926";

    public static Boolean sendActiveEmail(Account account) {

        String toMail = account.getmEmail();

        if (TextUtils.isEmpty(toMail)) {
            return false;
        }
        
        SignUpTokenObj signUpTokenObj = new SignUpTokenObj();
        signUpTokenObj.account = account;
        signUpTokenObj.signUpTimestamp = TimestampUtil.getCurrentTimestamp();  //modify by file  统一使用新的时间工具类

        String signUpToken = SignUpTokenCache.generateSignUpToken(account.getmId());

        SignUpTokenCache.signUpTokenMap.put(signUpToken, signUpTokenObj);

        String url = "http://onemenu.mobi/app/account/activeEmail?signUpToken=" + signUpToken;

        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", onemenuMailHostName);
        mailProps.setProperty("mail.smtp.starttls.enable", "true");
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.smtp.quitwait", "false");
        mailProps.setProperty("mail.smtp.ssl.trust", "*");
        
        //获取邮箱
        //String _onemenuMailAddress = ConfigCache.getValue(ConfigKey.GMAIL_USER_NAME, "onemenu@onemenu.mobi");
        //密码
		//String onemenuPassword = ConfigCache.getValue(ConfigKey.GMAIL_PASSWORD, "onemenu31415926");
		
		Authenticator authenticator = new EmailAuthenticator(onemenuMailAddress , onemenuPassword);

        javax.mail.Session mailSession =
                javax.mail.Session.getDefaultInstance(mailProps, authenticator);
        // mailSession.setDebug(true);

        try {

            Address fromAddress = new InternetAddress(onemenuMailAddress);
            Address toAddress = new InternetAddress(toMail);
            // modify by file  本处不用使用StringBuffer，Stringbuffer 是线程安全的，性能不如StringBuilder（所有本身是线程安全的代码都不应该使用StringBuffer）
            //StringBuffer contentBuffer = new StringBuffer();
            StringBuilder contentBuilder = new StringBuilder();
            //结构清晰
            contentBuilder.append("<p>Hello, Dear Customer.</p>")
                          .append("<p>Welcome to One Menu! To activate your account, you have to confirm the below link,</p>")
                          .append("<a href='" + url + "'>Active onemenu account</a>")
                          .append("<p>Thank you for your participation</p>")
                          .append("<br>")
                          .append("<p>One Menu Team</p>")
                          .append("<p>[Please do not reply to this mail.]</p>");
            String content = contentBuilder.toString();

            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(fromAddress);
            msg.setSubject("[OneMenu] Active Account");
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");
            msg.setRecipient(RecipientType.TO, toAddress);

            /*
             * Transport transport = session.getTransport("smtp"); transport.connect("smtp.163.com",
             * userName, password); transport.sendMessage(msg,msg.getAllRecipients());
             * transport.close();
             */

            Transport.send(msg);
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("Send active email to " + toMail);

        } catch (MessagingException e) {
        	LOGGER.error("", e);
        }

        return true;

    }

    public static Boolean sendOrderFormToRestaurant(OrderForm orderForm) {
    	
    	
    	
        String toMail = orderForm.getmRestaurant().getmEmail();
        
        boolean hasNotEmail = TextUtils.isEmpty(toMail);

        if(LOGGER.isDebugEnabled()){
        	LOGGER.debug("begin  send mail ,Restaurant have not email? -"+hasNotEmail);
        }

        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", onemenuMailHostName);
        mailProps.setProperty("mail.smtp.starttls.enable", "true");
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.smtp.quitwait", "false");
        mailProps.setProperty("mail.smtp.ssl.trust", "*");
        
        //获取邮箱
        String _onemenuMailAddress = ConfigCache.getValue(ConfigKey.GMAIL_USER_NAME, "onemenu@onemenu.mobi");
       //密码
       //String onemenuPassword = ConfigCache.getValue(ConfigKey.GMAIL_PASSWORD, "onemenu31415926");

        Authenticator authenticator = new EmailAuthenticator(onemenuMailAddress, onemenuPassword);

        javax.mail.Session mailSession =
                javax.mail.Session.getDefaultInstance(mailProps, authenticator);
        // mailSession.setDebug(true);

        try {
        	
        	if(hasNotEmail)
        		toMail = _onemenuMailAddress;

            Address fromAddress = new InternetAddress(onemenuMailAddress);
            Address toAddress = new InternetAddress(toMail);
            Address bccAddress = new InternetAddress(_onemenuMailAddress);

            String content;
            try {
                content = FaxUtils.getOrderFormHtml(orderForm);
            } catch (IOException e) {
               LOGGER.error("", e);
                return false;
            } catch (URISyntaxException e) {
                LOGGER.error("", e);
                return false;
            }

            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(fromAddress);
            msg.setSubject("[OneMenu Order] " + orderForm.getmRestaurant().getmName());
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");
            
            msg.setRecipient(RecipientType.TO, toAddress);
            
            if(!hasNotEmail)
            	msg.setRecipient(RecipientType.BCC, bccAddress);

            /*
             * Transport transport = session.getTransport("smtp"); transport.connect("smtp.163.com",
             * userName, password); transport.sendMessage(msg,msg.getAllRecipients());
             * transport.close();
             */

            Transport.send(msg);
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("Send order form email to " + toMail);
            
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("send email end");

        } catch (MessagingException e) {
        	 LOGGER.error("", e);
        }

        return true;

    }
    
    public static Boolean sendOrderFormToDriver(OrderForm orderForm,BigDecimal oldTipsFee) {
    	
    	
    	
    	Driver driver = orderForm.getmDriver();
    	
    	if(driver==null){
    		LOGGER.error("driver is null");
    		return true;
    	}
    	
        //String toMail = driver.getmEmail();
        
    	Restaurant rest = orderForm.getmRestaurant();
       

        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", onemenuMailHostName);
        mailProps.setProperty("mail.smtp.starttls.enable", "true");
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.smtp.quitwait", "false");
        mailProps.setProperty("mail.smtp.ssl.trust", "*");
        
        //抄送邮箱
        String _onemenuMailAddress = "onemenu.us@gmail.com";

        Authenticator authenticator = new EmailAuthenticator(onemenuMailAddress, onemenuPassword);

        javax.mail.Session mailSession =
                javax.mail.Session.getDefaultInstance(mailProps, authenticator);

        try {
        	
        	//if(hasNotEmail)
        		//toMail = _onemenuMailAddress;

            Address fromAddress = new InternetAddress(onemenuMailAddress);
            Address toAddress = new InternetAddress(_onemenuMailAddress);
           // Address bccAddress = new InternetAddress(_onemenuMailAddress);

            StringBuilder builder = new StringBuilder();
            builder.append("<p>  orderCode:" + orderForm.getmCode() + "</p>")
			  .append("<p> orderPrice:" + orderForm.getmTotalFee() + "</p>")
			  .append("<p> reset tipsFee:" + orderForm.getmTipsFee() + "</p>")
			  .append("<p> driver tipsFee:" + oldTipsFee + "</p>")
			  .append("<p> driver name:" + driver.getmName() + "</p>")
			  .append("<p> driver email:" + driver.getmEmail() + "</p>")
			  .append("<p> restaurant name:" + rest==null?"":rest.getmName() + "</p>");
            
           

            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(fromAddress);
            msg.setSubject("[OneMenu Order] Merchant reset tipsFee");
            msg.setSentDate(new Date());
            msg.setContent(builder.toString(), "text/html;charset=utf-8");
            
            msg.setRecipient(RecipientType.TO, toAddress);
           // msg.setRecipient(RecipientType.BCC, bccAddress);

            Transport.send(msg);
           // if(LOGGER.isDebugEnabled())
            //	LOGGER.debug("Send order form email to " + toMail);
            
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("send email end");

        } catch (MessagingException e) {
        	 LOGGER.error("", e);
        }

        return true;

    }

    public static Boolean sendFeedbackEmail(CustomerFeedback customerFeedback) {

        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", onemenuMailHostName);
        mailProps.setProperty("mail.smtp.starttls.enable", "true");
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.smtp.quitwait", "false");
        mailProps.setProperty("mail.smtp.ssl.trust", "*");
        
        //获取邮箱
        //String onemenuMailAddress = ConfigCache.getValue(ConfigKey.GMAIL_USER_NAME, "onemenu@onemenu.mobi");
        //密码
        //String onemenuPassword = ConfigCache.getValue(ConfigKey.GMAIL_PASSWORD, "onemenu31415926");

        Authenticator authenticator = new EmailAuthenticator(onemenuMailAddress, onemenuPassword);

        javax.mail.Session mailSession =
                javax.mail.Session.getDefaultInstance(mailProps, authenticator);

        try {

            Address fromAddress = new InternetAddress(onemenuMailAddress);
            Address toAddress = new InternetAddress(onemenuMailAddress);
            // modify by file  本处不用使用StringBuffer，Stringbuffer 是线程安全的，性能不如StringBuilder（所有本身是线程安全的代码都不应该使用StringBuffer）
            //StringBuffer contentBuffer = new StringBuffer();
            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("<p>" + customerFeedback.getmCustomerEmail() + "</p>")
            			  .append("<p>" + customerFeedback.getmCustomerName() + ":</p>")
            			  .append("<p>" + customerFeedback.getmContent() + "</p>");
            String content = contentBuilder.toString();

            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(fromAddress);
            msg.setSubject("[OneMenu Feedback] ");
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");
            msg.setRecipient(RecipientType.TO, toAddress);

            /*
             * Transport transport = session.getTransport("smtp"); transport.connect("smtp.163.com",
             * userName, password); transport.sendMessage(msg,msg.getAllRecipients());
             * transport.close();
             */

            Transport.send(msg);
            if(LOGGER.isDebugEnabled())
            	LOGGER.debug("Send feedback email.");

        } catch (MessagingException e) {
        	LOGGER.error("", e);
        }

        return true;
    }
}
