package gr.softeng.team21.domain;

import java.util.ArrayList;
public interface EmailRepository {

    ArrayList<EmailMessage> getInboxEmails();
    void saveInboxEmails(EmailMessage msg);
    ArrayList<EmailMessage> getSentEmails();
    void  saveSentEmails(EmailMessage msg);

}
