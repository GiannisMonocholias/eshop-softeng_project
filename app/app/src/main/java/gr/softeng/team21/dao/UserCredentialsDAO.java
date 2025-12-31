package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.User;

public interface UserCredentialsDAO {

     HashMap<String, User> getUsersCredentials();
     void addUser(User user);
     void removeUser(String username);
     User validateAndGetUser(String username, String password);
     void clear();
}
