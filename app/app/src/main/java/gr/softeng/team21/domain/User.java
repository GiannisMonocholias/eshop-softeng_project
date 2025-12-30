package gr.softeng.team21.domain;

import gr.softeng.team21.memorydao.EmailDAOMemory;

public abstract class User {
    protected String username;
    protected   String firstname;
    protected  String password;
    protected  String lastname;
    protected  String phoneNumber;
    protected EmailAddress emailaddress;


    protected EmailDAOMemory emailDAOMemory;

    protected Address address;

    public User(){}

    public User (String username,String firstname,String password,String lastname, String phoneNumber,EmailAddress emailaddress) {
        this.username = username;
        this.firstname = firstname;
        this.password = password;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.emailaddress = emailaddress;
    }

    public Address getAddress ( ) {
        return address;
    }

    public void setAddress(Address address) {this.address = address;}

    public String getUsername ( ) {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getPassword ( ) {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getFirstname ( ) {
        return firstname;
    }

    public void setFirstname (String firstname) {
        this.firstname = firstname;
    }

    public String getLastname ( ) {
        return lastname;
    }

    public void setLastname (String lastname) {
        this.lastname = lastname;
    }

    public String getPhonenumber ( ) {
        return phoneNumber;
    }

    public void setPhonenumber (String phonenumber) {
        this.phoneNumber = phonenumber;
    }

    public EmailAddress getEmailAddress ( ) {
        return emailaddress;
    }

    public void setEmailaddress (EmailAddress emailaddress) {
        this.emailaddress = emailaddress;
    }

    public EmailDAOMemory getEmailProviderStub() {
        return emailDAOMemory;
    }

    protected void setEmailProviderStub(EmailDAOMemory emailDAOMemory) {
        this.emailDAOMemory = emailDAOMemory;
    }

    protected void replyToEmail(User sender, User recipient,EmailMessage original, String subject, String body){
        deliverEmail(sender, recipient, original,subject, body, true);
    }

    protected void sendEmail(User sender, User recipient, String subject, String body) {
        deliverEmail(sender,recipient,null,subject,body,false);
    }

    protected void deliverEmail(User sender, User recipient,EmailMessage original, String subject, String body, boolean isReplyMessage) {
        EmailMessage email = new EmailMessage();
        email.setFrom(sender.getEmailAddress());
        email.setTo(recipient.getEmailAddress());
        email.setSubject(subject);
        email.setBody(body);
        email.setReplyMessage(isReplyMessage);

        if(original != null)
            original.setReplied(true);

        recipient.getEmailProviderStub().saveInboxEmails(email);
        sender.getEmailProviderStub().saveSentEmails(email);
    }

    protected void setEmailRead(EmailMessage email) {
        email.setRead(true);
    }


    public void editData (String choice, String change, Address newaddress, EmailAddress email) {
        if ( choice == null )
            throw new IllegalArgumentException ( "Choice cannot be null!!!" );

        switch (choice) {
            case "1":
                this.setUsername ( change );
                break;
            case "2":
                this.setPassword ( change );
                break;
            case "3":
                this.setAddress ( newaddress );
                break;
            case "4":
                this.setEmailaddress ( email );
                break;
            case "5":
                this.setPhonenumber ( change );
                break;
            case "6":
                break;
            default:
                throw new IllegalArgumentException ( "Choice must be 1-6" );
        }
    }


}
