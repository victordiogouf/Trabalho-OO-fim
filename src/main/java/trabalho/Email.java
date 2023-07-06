package trabalho;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
    private static final String APPLICATION_NAME = "Trabalho OO";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static void send(String email, String subject, String body) throws Exception {
        try {
            // Set up the HTTP transport and credentials
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = authorize(httpTransport);

            // Build the Gmail service
            Gmail service = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Create the email
            MimeMessage mimeMessage = createEmail(
                Environment.MAIL_DOMAIN, 
                email,
                subject,
                body);

            // Send the email
            sendMessage(service, Environment.MAIL_USER_ID, mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static Credential authorize(HttpTransport httpTransport) throws Exception {
        // Set up authorization flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, Environment.MAIL_CLIENT_ID, Environment.MAIL_CLIENT_SECRET,
                Collections.singleton(GmailScopes.GMAIL_SEND))
                .build();

        // Exchange refresh token for access token
        GoogleTokenResponse tokenResponse = new GoogleTokenResponse();
        tokenResponse.setRefreshToken(Environment.MAIL_REFRESH_TOKEN);
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(Environment.MAIL_CLIENT_ID, Environment.MAIL_CLIENT_SECRET)
                .build()
                .setFromTokenResponse(tokenResponse);

        return credential;
    }

    private static MimeMessage createEmail(String from, String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Environment.MAIL_DOMAIN, Environment.MAIL_PASSWORD);
            }
        });

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(body);
        return email;
    }

    private static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = com.google.api.client.util.Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        service.users().messages().send(userId, message).execute();
    }
}
