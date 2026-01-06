package gr.softeng.team21.memorydao;

import java.util.ArrayList;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;

public class EmailDAOMemory implements EmailDAO {
    private ArrayList<EmailMessage> inboxEmails = new ArrayList<EmailMessage>();

    private ArrayList<EmailMessage> sentEmails = new ArrayList<EmailMessage>();

    public ArrayList<EmailMessage> getInboxEmails(){return inboxEmails;}

    public ArrayList<EmailMessage> getSentEmails() {return sentEmails;}


    public  ArrayList<EmailMessage> getUnreadEmails(){
        ArrayList<EmailMessage> inbox = getInboxEmails();
        ArrayList<EmailMessage> unread = new ArrayList<EmailMessage>();
        for(EmailMessage msg: inbox){
            if(!msg.isRead()){
                unread.add(msg);
            }
        }

        return unread;
    }


    public ArrayList<EmailMessage> getReadEmails() {
        ArrayList<EmailMessage> inbox = getInboxEmails();
        ArrayList<EmailMessage> read = new ArrayList<EmailMessage>();
        for (EmailMessage msg : inbox) {
            if (msg.isRead()) {
                read.add(msg);
            }
        }
        return read;
    }


    public  ArrayList<EmailMessage> getUnrepliedEmails(){
        ArrayList<EmailMessage> inbox = getInboxEmails();
        ArrayList<EmailMessage> unreplied = new ArrayList<EmailMessage>();
        for(EmailMessage msg: inbox){
            if(!msg.isReplied()){
                unreplied.add(msg);
            }
        }

        return  unreplied;
    }


    public ArrayList<EmailMessage> getRepliedEmails() {
        ArrayList<EmailMessage> inbox = getInboxEmails();
        ArrayList<EmailMessage> replied = new ArrayList<EmailMessage>();
        for (EmailMessage msg : inbox) {
            if (msg.isReplied()) {
                replied.add(msg);
            }
        }
        return replied;
    }




    public void saveInboxEmails(EmailMessage msg){
        inboxEmails.add(msg);
    }


    public void saveSentEmails(EmailMessage msg){
        sentEmails.add(msg);
    }

    public boolean inInbox(EmailMessage msg){
        return inboxEmails.contains(msg);
    }

    public boolean inSent(EmailMessage msg){
        return sentEmails.contains(msg);
    }
}
