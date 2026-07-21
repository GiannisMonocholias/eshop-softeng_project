package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.User;

/**
 * Interface for User Credentials Data Access Object.
 * Defines asynchronous operations for managing user registration, deletion, and authentication.
 * @author Γιάννης Μονοχολιάς
 */
public interface UserCredentialsDAO {

     /**
      * Retrieves a copy of the current user credentials map asynchronously.
      * @return A CompletableFuture containing a HashMap with all registered users.
      */
     CompletableFuture<HashMap<String, User>> getUsersCredentials();

     /**
      * Adds a new user to the storage asynchronously.
      * @param user The User object to be registered.
      * @return A CompletableFuture representing the completion of the operation.
      * Completes exceptionally with an IllegalArgumentException if the username is already occupied.
      */
     CompletableFuture<Void> addUser(User user);

     /**
      * Removes the user associated with the specified username asynchronously.
      * @param username The unique identifier of the user to remove.
      * @return A CompletableFuture representing the completion of the operation.
      * Completes exceptionally with a NoSuchElementException if the username is not found.
      */
     CompletableFuture<Void> removeUser(String username);

     /**
      * Validates user credentials and retrieves the corresponding User object asynchronously.
      * @param username The username to check.
      * @param password The password to validate.
      * @return A CompletableFuture containing the authenticated User object.
      * Completes exceptionally with a SecurityException if the username is not found or the password is incorrect.
      */
     CompletableFuture<User> validateAndGetUser(String username, String password);

     /**
      * Clears all user credentials from the storage asynchronously.
      * @return A CompletableFuture representing the completion of the clearing operation.
      */
     CompletableFuture<Void> clear();
}