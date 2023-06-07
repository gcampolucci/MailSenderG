import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailService {

    private final String username;
    private final String password;
    private final String sender;
    private final String receiver;

    private final Properties prop;

    public EmailService(String host, int port, String username, String password, String sender, String receiver) {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        this.username = username;
        this.password = password;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static void main(String... args) {

        String host = args[0];
        String port = args[1];
        String user = args[2];
        String pass = args[3];
        String from = args[4];
        String to = args[5];

        try {
            new EmailService(host, Integer.parseInt(port), user, pass, from, to)
                    .sendMail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMail() throws Exception {

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

        message.setSubject("Mail di prova - test server smpt");

        String msg = "Questa e' una mail di prova per testare il nuovo server smtp.";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

}
