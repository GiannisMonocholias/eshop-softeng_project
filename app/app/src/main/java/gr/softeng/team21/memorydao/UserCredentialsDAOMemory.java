package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.User;

/**
 * In-memory implementation of the {@link UserCredentialsDAO} interface.
 * This class uses a Singleton pattern to provide a centralized point of access
 * for user credentials stored in memory, wrapped in CompletableFutures to match the async architecture.
 * @author Γιάννης Μονοχολιάς
 */
public class UserCredentialsDAOMemory implements UserCredentialsDAO {
    private static UserCredentialsDAOMemory instance;
    private static HashMap<String, User> credentialsMap;

    /**
     * Private default constructor to enforce the Singleton pattern.
     * Initializes the credentialsMap variable which stores user credentials.
     */
    private UserCredentialsDAOMemory() {
        credentialsMap = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the UserCredentialsDAOMemory.
     * Initializes the instance if it has not been initialized.
     * @return the unique instance of UserCredentialsDAOMemory.
     */
    public static UserCredentialsDAOMemory getInstance() {
        if (instance == null) {
            instance = new UserCredentialsDAOMemory();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation returns an immediately completed future containing a copy of the memory map.</p>
     */
    @Override
    public CompletableFuture<HashMap<String, User>> getUsersCredentials() {
        return CompletableFuture.completedFuture(new HashMap<>(credentialsMap));
    }

    /**
     * {@inheritDoc}
     * <p>Validates memory constraints before adding. Completes exceptionally if the user already exists.</p>
     */
    @Override
    public CompletableFuture<Void> addUser(User user) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (credentialsMap.containsKey(user.getUsername())) {
            future.completeExceptionally(new IllegalArgumentException("Username already exists"));
        } else {
            credentialsMap.put(user.getUsername(), user);
            future.complete(null);
        }
        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Validates existence in memory before removal. Completes exceptionally if the username is not found.</p>
     */
    @Override
    public CompletableFuture<Void> removeUser(String username) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (!credentialsMap.containsKey(username)) {
            future.completeExceptionally(new NoSuchElementException("Username does not exist"));
        } else {
            credentialsMap.remove(username);
            future.complete(null);
        }
        return future;
    }

    /**
     * {@inheritDoc}
     * <p>Performs synchronous memory validation and returns a completed future upon success,
     * or completes exceptionally if credentials do not match.</p>
     */
    @Override
    public CompletableFuture<User> validateAndGetUser(String username, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();
        User user = credentialsMap.get(username);

        if (user == null || !user.getPassword().equals(password)) {
            future.completeExceptionally(new SecurityException("Invalid credentials"));
        } else {
            future.complete(user);
        }

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> clear() {
        credentialsMap.clear();
        return CompletableFuture.completedFuture(null);
    }
}