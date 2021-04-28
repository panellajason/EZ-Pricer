package codingsharks.ezpricer.random;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import javax.mail.Session;
import codingsharks.ezpricer.fragments.NotificationsFragment;

public class EmailNotification extends javax.mail.Authenticator{
    private final Context context;
    private String SUBJECT = "This is a test email";
    private String MESSAGE = "This is a test email. We will soon be sending notifications regarding price drops.";
    private String purpose;
    private Session session;

    private final String userEmail = Objects.requireNonNull(
            FirebaseAuth.getInstance().getCurrentUser()).getEmail();

    public EmailNotification(Context context, String purpose) {
        this.context = context;
        this.purpose = purpose;
    }

    public EmailNotification(Context context, String subject, String message) {
        this.context = context;
        this.SUBJECT = subject;
        this.MESSAGE = message;
    }

    //TODO: create constructor for non-Fragment contexts

    public void sendEmail() {
        createMessage();
        SendMail sm = new SendMail(context, userEmail, SUBJECT, MESSAGE);
        sm.execute();
    }

    private void createMessage() {
        //TODO: create if/else statements for purposeto get proper subject and message string
        if ("PRICE DROP".equals(purpose)) {
            MESSAGE = "";
        }
    }
}
