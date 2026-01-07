package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.User;

/**
 * In-memory implementation of the {@link UserCredentialsDAO} interface.
 * This class uses a Singleton pattern to provide a centralized point of access
 * for user credentials stored in memory.
 * @author Γιάννης Μονοχολιάς
 */
public class UserCredentialsDAOMemory implements UserCredentialsDAO {
    private static UserCredentialsDAOMemory instance;
    private static HashMap<String, User> credentialsMap;

    /**
     * Private default constructor.
     * it is used to avoid external initialization.
     * initializes the credentialsMap variable which stores user credentials
     */
    private UserCredentialsDAOMemory() {
        credentialsMap = new HashMap<>();
    }


    /**
     * Returns the singleton instance of the UserCredentialsDAOMemory.
     * Initializes the instance it it has not been initialized
     * @return the unique instance of UserCredentialsDAOMemory
     */
    public static UserCredentialsDAOMemory getInstance() {
        if (instance == null) {
            instance = new UserCredentialsDAOMemory();
        }
        return instance;
    }

    /**
     * Returns a copy of the current user credentials map.
     * @return A new HashMap containing all registered users.
     */
    public HashMap<String, User> getUsersCredentials(){return  new HashMap<>(credentialsMap);}


    /**
     * Adds a new user to the memory storage.
     * @param user The User object to be registered.
     * @throws IllegalArgumentException if the username is already occupied from another account
     */
    public void addUser(User user) {

        if (credentialsMap.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        credentialsMap.put(user.getUsername(), user);
    }


    /**
     * Removes the user associated with the specified username.
     * @param username The unique identifier of the user to remove.
     * @throws NoSuchElementException if the username is not found in the storage.
     */
    public void removeUser(String username){

        if (!credentialsMap.containsKey(username)) {
            throw new NoSuchElementException("Username does not exists");
        }

        credentialsMap.remove(username);
    }


    /**
     * Validates user credentials and retrieves the corresponding User object.
     * @param username The username to check.
     * @param password The password to validate.
     * @return The authenticated User object.
     * @throws SecurityException if the username is not found or the password is incorrect.
     */
    public User validateAndGetUser(String username, String password){

        User user = credentialsMap.get(username);

        if(user == null){
            throw new SecurityException("Invalid credentials");
        }
        if(!user.getPassword().equals(password)){
            throw new SecurityException("Invalid credentials");
        }
        return user;
    }

    /**
     * Clears all user credentials from the memory storage.
     */
    public void clear(){
        credentialsMap.clear();
    }
}
