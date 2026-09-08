package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;

 
public class MailUtil {

    private static final String CONFIG_PATH = "/WEB-INF/mail.properties";

    private MailUtil() {
    }

    public static void sendInquiryNotification(ServletContext servletContext,
            String categoryName, String content, String fromEmail) {

        Properties config = loadConfig(servletContext);
        if (config == null) {
            System.err.println("[MailUtil] mail.properties が見つからないため、通知メール送信をスキップしました。");
            return;
        }

        String smtpUser = config.getProperty("smtp.user");
        String smtpAppPassword = config.getProperty("smtp.appPassword");
        String notifyTo = config.getProperty("notify.to");

        if (smtpUser == null || smtpAppPassword == null || notifyTo == null) {
            System.err.println("[MailUtil] mail.properties の設定が不足しているため、通知メール送信をスキップしました。");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        final String user = smtpUser;
        final String appPassword = smtpAppPassword;

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, appPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notifyTo));
            message.setSubject("【PF_Reiki】新しいお問い合わせがあります");
            message.setText(
                    "新しいお問い合わせが届きました。\n\n" +
                    "カテゴリ: " + categoryName + "\n" +
                    "お問い合わせ者のメールアドレス: " + fromEmail + "\n" +
                    "内容:\n" + content + "\n\n" +
                    "管理画面のお問い合わせ一覧から詳細を確認してください。"
            );

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadConfig(ServletContext servletContext) {
        String realPath = servletContext.getRealPath(CONFIG_PATH);
        if (realPath == null) {
            return null;
        }

        try (FileInputStream in = new FileInputStream(realPath)) {
            Properties props = new Properties();
            props.load(in);
            return props;

        } catch (IOException e) {
            return null;
        }
    }
}
