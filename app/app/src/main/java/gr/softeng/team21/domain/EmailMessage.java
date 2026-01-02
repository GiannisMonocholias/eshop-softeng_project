package gr.softeng.team21.domain;

public class EmailMessage {
    private EmailAddress from;
    private EmailAddress to;
    private String subject;
    private String body;
    private boolean isRead = false;
    private boolean isReplied = false;
    private boolean replyMessage = false;



    private Date dateSent;


    public EmailMessage(){}


    public EmailMessage(EmailAddress from, EmailAddress to, String subject, String body, Date dateSent) {
        if (from == null || to == null || subject == null || body == null) {
            throw new IllegalArgumentException("EmailMessage arguments cannot be null");
        }
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.dateSent = dateSent;
    }

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = replied;
    }


    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(boolean replyMessage) {
        this.replyMessage = replyMessage;
    }


    public EmailAddress getFrom() {
        return from;
    }

    public void setFrom(EmailAddress from) {
        if(from != null)
            this.from = from;
        else
            throw new IllegalArgumentException("Argument to cannot be null");
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if(body != null)
            this.body = body;
        else
            throw  new IllegalArgumentException("Argument body cannot be null");
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if(subject != null)
            this.subject = subject;
        else
            throw  new IllegalArgumentException("Argument subject cannot be null");
    }

    public EmailAddress getTo() {
        return to;
    }

    public void setTo(EmailAddress to) {
        if(to != null)
            this.to = to;
        else
            throw new IllegalArgumentException("Argument to cannot be null");
    }

    public void appendToBody(String text) {
        if(text != null)
            body += text;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    @Override
    public String toString() {
        return "From: " + from +
                ", To: " + to +
                ", Subject: " + subject +
                ", Body: " + body +
                ", Read: " + isRead +
                ", Replied: " + isReplied +
                ", ReplyMessage: " + replyMessage;
    }

}