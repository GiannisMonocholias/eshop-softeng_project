package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.User;

public interface UserCredentialsDAO {

     /**
      * Returns a copy of the current user credentials map.
      *  @return A new HashMap containing all registered users.
      */
     HashMap<String, User> getUsersCredentials();

     /**
      * Adds a new user to the memory storage.
      * @param user The User object to be registered.
      * @throws IllegalArgumentException if the username is already occupied from another account
      */
     void addUser(User user);

     /**
      * Removes the user associated with the specified username.
      * @param username The unique identifier of the user to remove.
      * @throws NoSuchElementException if the username is not found in the storage.
      */
     void removeUser(String username);

     /**
      * Validates user credentials and retrieves the corresponding User object.
      * @param username The username to check.
      * @param password The password to validate.
      * @return The authenticated User object.
      * @throws SecurityException if the username is not found or the password is incorrect.
      */
     User validateAndGetUser(String username, String password);

     /**
      * Clears all user credentials from the memory storage.
      */
     void clear();
}
