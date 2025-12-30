package gr.softeng.team21.dao;

import java.util.ArrayList;

import gr.softeng.team21.domain.EmailMessage;

public interface EmailDAO {

    public ArrayList<EmailMessage> getInboxEmails();

     ArrayList<EmailMessage> getSentEmails();

      ArrayList<EmailMessage> getUnreadEmails();

     ArrayList<EmailMessage> getReadEmails();
      ArrayList<EmailMessage> getUnrepliedEmails();

     ArrayList<EmailMessage> getRepliedEmails();

     void saveInboxEmails(EmailMessage msg);


     void saveSentEmails(EmailMessage msg);

     boolean inInbox(EmailMessage msg);

     boolean inSent(EmailMessage msg);

}
