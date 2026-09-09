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

 
// お問い合わせ受信時に、管理者宛て（notify.to）へ通知メールを送るユーティリティ。
// 送信設定は /WEB-INF/mail.properties から読む（Gitには含めない。雛形は mail.properties.example）。
public class MailUtil {

    // 送信設定ファイルの場所（Webアプリ内の相対パス）
    private static final String CONFIG_PATH = "/WEB-INF/mail.properties";

    private MailUtil() {
    }

    // ContactStatusServlet#doPost から呼ばれる。
    // categoryName / content / fromEmail はメール本文に載せる内容。
    public static void sendInquiryNotification(ServletContext servletContext,
            String categoryName, String content, String fromEmail) {

        // ① 設定ファイルを読む。無ければ送信せず終了（お問い合わせ登録は済んでいるので画面はエラーにしない）
        Properties config = loadConfig(servletContext);
        if (config == null) {
            System.err.println("[MailUtil] mail.properties が見つからないため、通知メール送信をスキップしました。");
            return;
        }

        // ② 設定値を取り出す
        String smtpUser = config.getProperty("smtp.user");            // 送信元Gmailアドレス
        String smtpAppPassword = config.getProperty("smtp.appPassword"); // Gmailのアプリパスワード
        String notifyTo = config.getProperty("notify.to");           // 受信先（＝自分のメアド）

        // どれか欠けていたら送らない
        if (smtpUser == null || smtpAppPassword == null || notifyTo == null) {
            System.err.println("[MailUtil] mail.properties の設定が不足しているため、通知メール送信をスキップしました。");
            return;
        }

        // ③ GmailのSMTPサーバ設定（TLS）
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        final String user = smtpUser;
        final String appPassword = smtpAppPassword;

        // ④ ユーザー名＋アプリパスワードで認証するメールセッション
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, appPassword);
            }
        });

        try {
            // ⑤ メールを組み立てる（差出人・宛先・件名・本文）
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));                          // 差出人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notifyTo)); // 宛先＝自分
            message.setSubject("【PF_Reiki】新しいお問い合わせがあります");            // 件名
            message.setText(                                                        // 本文
                    "新しいお問い合わせが届きました。\n\n" +
                    "カテゴリ: " + categoryName + "\n" +
                    "お問い合わせ者のメールアドレス: " + fromEmail + "\n" +
                    "内容:\n" + content + "\n\n" +
                    "管理画面のお問い合わせ一覧から詳細を確認してください。"
            );

            // ⑥ 送信
            Transport.send(message);

        } catch (MessagingException e) {
            // 送信失敗してもここで握る（お問い合わせ登録自体は成立済み）
            e.printStackTrace();
        }
    }

    // mail.properties を読み込んで Properties にして返す。読めなければ null。
    private static Properties loadConfig(ServletContext servletContext) {
        // Webアプリ内の相対パス → OS上の実ファイルパスに変換
        String realPath = servletContext.getRealPath(CONFIG_PATH);
        if (realPath == null) {
            return null;
        }

        try (FileInputStream in = new FileInputStream(realPath)) {
            Properties props = new Properties();
            props.load(in);
            return props;

        } catch (IOException e) {
            // ファイルが無い等。呼び出し側で「送信スキップ」に倒す
            return null;
        }
    }
}
