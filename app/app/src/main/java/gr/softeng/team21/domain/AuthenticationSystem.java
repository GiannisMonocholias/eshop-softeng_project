package gr.softeng.team21.domain;

import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.util.Date;

/**
 * Domain-level service for handling user authentication and registration.
 * Utilizes Dependency Injection to decouple from specific DAO implementations
 * and operates asynchronously returning CompletableFutures.
 * @author Γιάννης Μονοχολιάς
 */
public class AuthenticationSystem {

    private final UserCredentialsDAO repo;

    /**
     * Initializes the AuthenticationSystem with the required data access object.
     * @param repo The injected UserCredentialsDAO instance.
     */
    public AuthenticationSystem(UserCredentialsDAO repo) {
        this.repo = repo;
    }

    /**
     * Authenticates a user based on their credentials asynchronously.
     * @param username the given username to validate.
     * @param password the given password to validate.
     * @return A CompletableFuture containing the User if successful.
     *         Completes exceptionally with a SecurityException if credentials are invalid.
     */
    public CompletableFuture<User> login(String username, String password) {
        return repo.validateAndGetUser(username, password);
    }

    /**
     * Registers a new customer in the system asynchronously.
     * @param username  account's unique identifier
     * @param firstname  first name
     * @param password  password
     * @param lastname  last name
     * @param phoneNumber  phone number
     * @param emailaddress  email address
     * @param customer_id unique business id assigned to the customer
     * @param registdate date when registration took place
     * @return A CompletableFuture representing the registration process.
     */
    public CompletableFuture<Void> registerCustomer(String username, String firstname, String password, String lastname, String phoneNumber,
                                                    EmailAddress emailaddress, String customer_id, Date registdate) {
        Customer customer = new Customer(username, firstname, password, lastname, phoneNumber, emailaddress, customer_id, registdate);
        return repo.addUser(customer);
    }

    /**
     * Removes the account associated with the specified username asynchronously.
     * @param username the unique identifier of the account to be removed
     * @return A CompletableFuture representing the deletion process.
     */
    public CompletableFuture<Void> removeUser(String username) {
        return repo.removeUser(username);
    }
}