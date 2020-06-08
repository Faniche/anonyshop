package indi.faniche.anonyshop.util;

/* File:   EmailTemplate.java
 * -------------------------
 * Author: faniche
 * Date:   5/8/20
 */

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailUtil {

    private static final String SMTP_HOST_NAME = "smtp.mail.yahoo.com";
    private static final int SMTP_HOST_PORT = 587;
    private static final String SMTP_AUTH_USER = "summer19141523@yahoo.com";
    private static final String SMTP_AUTH_PWD = "tulsudafvnvgyspb";
    private static String receiver;
    private static String subject;
    private static String content;
    private static Properties props = new Properties();

    public static void init() {
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.user", SMTP_AUTH_USER);
        props.put("mail.smtp.password", SMTP_AUTH_PWD);
        props.put("mail.smtp.port", SMTP_HOST_PORT);
        props.put("mail.smtp.auth", "true");

    }

    public static void sendMail() throws MessagingException {
        init();
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_AUTH_USER));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject(subject);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        Transport.send(message);
    }

    public static void setReceiver(String receiverParam) {
        receiver = receiverParam;
    }

    public static void setSubject(String subjectParam) {
        subject = subjectParam;
    }

    public static void setContent(String contentParam) {
        content = contentParam;
    }
}

