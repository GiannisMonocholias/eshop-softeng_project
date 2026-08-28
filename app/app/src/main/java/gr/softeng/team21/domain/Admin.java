package gr.softeng.team21.domain;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.util.Date;

/**
 * This class represents the administrator of the e-shop.
 * It extends the {@link User} class and utilizes the Singleton design pattern
 * to ensure that only one administrator instance exists within the system.
 * @author Αλέξανρδος Δρακάκης
 */
public class Admin extends User {

    private static Admin instance;
    int salary;

    /**
     * Constructs a new Admin instance with the specified personal and professional details.
     * Note: While the constructor is public, instantiation should generally be handled
     * through the {@link #getInstance()} methods to maintain the Singleton pattern.
     * @param username     The administrator's username.
     * @param firstname    The administrator's first name.
     * @param password     The administrator's password.
     * @param lastname     The administrator's last name.
     * @param phoneNumber  The administrator's phone number.
     * @param emailaddress The administrator's email address.
     * @param salary       The administrator's initial salary.
     */
    public Admin(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, int salary){
        super(username, firstname, password, lastname, phoneNumber, emailaddress);
        this.salary = salary;
    }

    /**
     * Retrieves the Singleton instance of the Admin.
     * If the instance does not currently exist, it initializes it using default placeholder values.
     * @return The single, shared {@link Admin} instance.
     */
    public static Admin getInstance(){
        if(instance == null){
            EmailAddress defaultEmail = new EmailAddress("default_admin");
            instance = new Admin("default_admin", "Default", "default_pass", "Admin", "N/A", defaultEmail, 0);
        }
        return instance;
    }

    /**
     * Retrieves the Singleton instance of the Admin.
     * If the instance does not exist, it initializes it using the explicitly provided parameter values.
     * @param username     The administrator's username.
     * @param firstname    The administrator's first name.
     * @param password     The administrator's password.
     * @param lastname     The administrator's last name.
     * @param phoneNumber  The administrator's phone number.
     * @param emailaddress The administrator's email address.
     * @param salary       The administrator's salary.
     * @return The single, shared {@link Admin} instance.
     */
    public static Admin getInstance(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, int salary){
        if(instance == null){
            instance = new Admin(username, firstname, password, lastname, phoneNumber, emailaddress, salary);
        }
        return instance;
    }

    /**
     * Retrieves the current salary of the administrator.
     * @return The salary as an integer value.
     */
    public int getSalary(){
        return salary;
    }

    /**
     * Updates the salary of the administrator.
     * @param salary The new salary value to be assigned.
     */
    public void setSalary(int salary){
        this.salary = salary;
    }
}