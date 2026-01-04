package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.User;

public interface UserCredentialsDAO {

     void addUser(User user);
     void removeUser(String username);
     HashMap<String, User> getUsersCredentials();
     User validateAndGetUser(String username, String password);
     void clear();
}
