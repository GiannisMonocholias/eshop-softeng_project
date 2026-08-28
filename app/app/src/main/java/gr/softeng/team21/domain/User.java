package gr.softeng.team21.domain;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;

/**
 * An abstract base class representing a generic user in the system.
 * It provides common profile attributes (credentials, contact details).
 * As a pure domain entity, it does not handle data persistence or messaging logic directly.
 *
 * @author Γιάννης Μονοχολιάς, PAVLOS GRATSANIS
 */
public abstract class User {
    protected String username;
    protected String firstname;
    protected String password;
    protected String lastname;
    protected String phoneNumber;
    protected EmailAddress emailaddress;
    protected Address address;

    /**
     * Default constructor for the User class.
     */
    public User(){}

    /**
     * Constructs a new User with essential profile information.
     *
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
}