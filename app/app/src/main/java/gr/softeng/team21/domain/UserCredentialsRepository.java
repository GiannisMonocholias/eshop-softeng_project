package gr.softeng.team21.domain;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class UserCredentialsRepository {
    private static UserCredentialsRepository instance;
    private HashMap<String, User> credentialsMap; // username → password (hashed στην πράξη)

    private UserCredentialsRepository() {
        credentialsMap = new HashMap<>();
    }

    public static UserCredentialsRepository getInstance() {
        if (instance == null) {
            instance = new UserCredentialsRepository();
        }
        return instance;
    }

    public HashMap<String, User> getUsersCredentials(){return  new HashMap<>(credentialsMap);}

    public void checkNullArguments(String string1, String string2){
        if(string1 == null || string2 == null)
            throw new IllegalArgumentException("Username and password cannot be null");
    }

    public void addUser(User user) {
        checkNullArguments(user.getUsername(),user.getPassword());

        if (credentialsMap.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        credentialsMap.put(user.getUsername(), user);
    }

    public void removeUser(String username){
        checkNullArguments(username,"");

        if (!credentialsMap.containsKey(username)) {
            throw new NoSuchElementException("Username does not exists");
        }

        credentialsMap.remove(username);
    }

    public User validateAndGetUser(String username, String password){
        checkNullArguments(username,password);

        User user = credentialsMap.get(username);

        if(user == null){
            throw new SecurityException("Invalid credentials");
        }
        if(!user.getPassword().equals(password)){
            throw new SecurityException("Invalid credentials");
        }
        return user;
    }

    public void clear(){
        credentialsMap.clear();
    }
}
