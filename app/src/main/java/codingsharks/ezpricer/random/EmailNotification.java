package codingsharks.ezpricer.random;

import javax.mail.Session;
import codingsharks.ezpricer.fragments.BarcodeFragment;

public class EmailNotification extends javax.mail.Authenticator{

    private final BarcodeFragment context;
    private final String EMAIL = "codingsharks2021@gmail.com";  //reciver email
    private final String SUBJECT = "This is a test email";
    private final String MESSAGE = "This is a test email. We will soon be sending notifications regarding price drops.";
    private String purpose;
    private Session session;

    public EmailNotification(BarcodeFragment context, String purpose) {
        this.context = context;
        this.purpose = purpose;
    }

    //TODO: create constructor for non-Fragment contexts

    public void sendEmail() {
        getPurpose();
        SendMail sm = new SendMail(context, EMAIL, SUBJECT, MESSAGE);
        sm.execute();
    }

    private void getPurpose() {
        //TODO: create if/else statements for purposeto get proper subject and message string
    }
}
