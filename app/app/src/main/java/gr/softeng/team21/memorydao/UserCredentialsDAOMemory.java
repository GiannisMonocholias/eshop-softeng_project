package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.User;

public class UserCredentialsDAOMemory implements UserCredentialsDAO {
    private static UserCredentialsDAOMemory instance;
    private static HashMap<String, User> credentialsMap;

    private UserCredentialsDAOMemory() {
        credentialsMap = new HashMap<>();
    }

    public static UserCredentialsDAOMemory getInstance() {
        if (instance == null) {
            instance = new UserCredentialsDAOMemory();
        }
        return instance;
    }

    public HashMap<String, User> getUsersCredentials(){return  new HashMap<>(credentialsMap);}


    public void addUser(User user) {

        if (credentialsMap.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        credentialsMap.put(user.getUsername(), user);
    }

    public void removeUser(String username){

        if (!credentialsMap.containsKey(username)) {
            throw new NoSuchElementException("Username does not exists");
        }

        credentialsMap.remove(username);
    }


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

    public void clear(){
        credentialsMap.clear();
    }
}
