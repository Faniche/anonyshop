package indi.faniche.anonyshop.user.test;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendFromYahoo {
    public static void main(String[] args) {
        // Sender's email ID needs to be mentioned
        String from = "summer19141523@yahoo.com";
        String pass = "tulsudafvnvgyspb";
        // Recipient's email ID needs to be mentioned.
        String to = "18340082971@163.com";
        String host = "smtp.mail.yahoo.com";

        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("账户激活");

            // Now set the actual message
//            message.setText("<a href='http://user.anonyshop.tech/confirm?userId=\" + 1 + \"'>点击这里的网址激活账户</a>");
            message.setContent("<a href='http://user.anonyshop.tech/confirm?userId=\" + 1 + \"'>点击这里的网址激活账户</a>", "text/html; charset=utf-8");
            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}