package utils;

import utils.settingsEngine.SettingsEngine;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

public class EmailAttachmentSenderUtils extends Thread{

    private String filePath;

    public EmailAttachmentSenderUtils(String filePath) {
        this.filePath = filePath;
    }

    public void run(){
        String to = SettingsEngine.getInstance().getSettings().sendTo;

        final String username = SettingsEngine.getInstance().getSettings().senderLogin;
        final String password = SettingsEngine.getInstance().getSettings().senderPassword;

        String host = SettingsEngine.getInstance().getSettings().smtpHostAdress;
        String port = SettingsEngine.getInstance().getSettings().smtpHostPort;

        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Database backup");
            message.setSentDate(new Date());
            message.setText("Database backup");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();
            String filename = filePath;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.setHeader("XPriority", "1");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
