package gr.softeng.team21.domain;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.memorydao.EmailDAOMemory;
import gr.softeng.team21.util.Date;

/**
 * An abstract base class representing a generic user in the system.
 * It provides common profile attributes (credentials, contact details) and
 * includes a built-in messaging mechanism for communication between users.
 * @author PAVLOS GRATSANIS,Γιάννης Μονοχολιάς
 */
public abstract class User {
    protected String username;
    protected String firstname;
    protected String password;
    protected String lastname;
    protected String phoneNumber;
    protected EmailAddress emailaddress;
    protected EmailDAO emailDAOMemory;
    protected Address address;

    /**
     * Default constructor for the User class.
     */
    public User(){}

    /**
     * Constructs a new User with essential profile information and initializes their email provider.
     * @param username     The unique account username.
     * @param firstname    The user's first name.
     * @param password     The account password.
     * @param lastname     The user's last name.
     * @param phoneNumber  The contact phone number.
     * @param emailaddress The user's email address.
     */
    public User (String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress) {
        this.username = username;
        this.firstname = firstname;
        this.password = password;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.emailaddress = emailaddress;
        this.emailDAOMemory = new EmailDAOMemory();
    }

    /**
     * @return the user's residential address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the address to set.
     */
    public void setAddress(Address address) {this.address = address;}

    /**
     * @return the unique username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return the account password. */
    public String getPassword() {
        return password;
    }

    /** @param password the password to set. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return the user's first name. */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the first name to set.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the user's last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the last name to set.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the contact phone number.
     */
    public String getPhonenumber() {
        return phoneNumber;
    }

    /**
     * @param phonenumber the phone number to set.
     */
    public void setPhonenumber(String phonenumber) {
        this.phoneNumber = phonenumber;
    }

    /**
     * @return the EmailAddress object.
     */
    public EmailAddress getEmailAddress() {
        return emailaddress;
    }

    /**
     * @param emailaddress the email address object to set.
     */
    public void setEmailaddress(EmailAddress emailaddress) {
        this.emailaddress = emailaddress;
    }

    /**
     * @return the email DAO provider for this user.
     */
    public EmailDAO getEmailProvider() {return emailDAOMemory;}

    /**
     * @param emailDAOMemory the email provider to set.
     */
    protected void setEmailProvider(EmailDAOMemory emailDAOMemory) {
        this.emailDAOMemory = emailDAOMemory;
    }

    /**
     * Sends a reply to an existing email message.
     * @param sender    The user sending the reply.
     * @param recipient The user receiving the reply.
     * @param original  The original email being replied to.
     * @param subject   The subject of the reply.
     * @param body      The main content of the reply.
     * @param dateSent  The date the reply is sent.
     */
    protected void replyToEmail(User sender, User recipient, EmailMessage original, String subject, String body, Date dateSent){
        deliverEmail(sender, recipient, original, subject, body, true, dateSent);
    }

    /**
     * Initiates a new email communication.
     * @param sender    The user sending the email.
     * @param recipient The user receiving the email.
     * @param subject   The subject of the message.
     * @param body      The content of the message.
     * @param dateSent  The date the email is sent.
     */
    public void sendEmail(User sender, User recipient, String subject, String body, Date dateSent) {
        deliverEmail(sender, recipient, null, subject, body, false, dateSent);
    }

    /**
     * Core logic for creating and delivering an email message between two users.
     * It updates both the sender's 'Sent' box and the recipient's 'Inbox'.
     * @param sender          The message sender.
     * @param recipient       The message recipient.
     * @param original        The original message (if this is a reply).
     * @param subject         The message subject.
     * @param body            The message body.
     * @param isReplyMessage  Flag indicating if this is a reply.
     * @param dateSent        The dispatch date.
     */
    protected void deliverEmail(User sender, User recipient, EmailMessage original, String subject, String body, boolean isReplyMessage, Date dateSent) {
        EmailMessage email = new EmailMessage();
        email.setFrom(sender.getEmailAddress());
        email.setTo(recipient.getEmailAddress());
        email.setSubject(subject);
        email.setBody(body);
        email.setReplyMessage(isReplyMessage);
        email.setDateSent(dateSent);

        if(original != null)
            original.setReplied(true);

        recipient.getEmailProvider().saveInboxEmails(email);
        sender.getEmailProvider().saveSentEmails(email);
    }

    /**
     * Marks a specific email message as read.
     * @param email The message to be updated.
     */
    protected void setEmailRead(EmailMessage email) {
        email.setRead(true);
    }

    /**
     * Updates specific fields of the user's profile based on a numeric choice.
     * @param choice     The menu option indicating which field to update (1-6).
     * @param change     The new value for String fields (username, password, phone).
     * @param newaddress The new address object (used if choice is "3").
     * @param email      The new email address object (used if choice is "4").
     * @throws IllegalArgumentException if choice is null or out of the 1-6 range.
     */
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