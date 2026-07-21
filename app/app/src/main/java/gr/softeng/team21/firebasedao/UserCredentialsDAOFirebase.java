package gr.softeng.team21.firebasedao;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.User;

/**
 * Firebase implementation of the {@link UserCredentialsDAO} interface.
 * Bridges Firebase's async Tasks to Java's CompletableFuture for non-blocking authentication.
 * Handles Firestore database operations for User Credentials.
 * @author Γιάννης Μονοχολιάς
 */
public class UserCredentialsDAOFirebase implements UserCredentialsDAO {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "credentials";

    /**
     * Initializes the Firebase Firestore instance.
     */
    public UserCredentialsDAOFirebase() {
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public CompletableFuture<HashMap<String, User>> getUsersCredentials() {
        CompletableFuture<HashMap<String, User>> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    HashMap<String, User> credentialsMap = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        credentialsMap.put(user.getUsername(), user);
                    }
                    future.complete(credentialsMap);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> addUser(User user) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (user == null || user.getUsername() == null) {
            future.completeExceptionally(new IllegalArgumentException("User or username cannot be null"));
            return future;
        }

        // Check if username already exists to prevent overwriting
        db.collection(COLLECTION_NAME).document(user.getUsername()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        future.completeExceptionally(new IllegalArgumentException("Username already exists"));
                    } else {
                        // Safe to add
                        db.collection(COLLECTION_NAME).document(user.getUsername())
                                .set(user)
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> removeUser(String username) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (username == null || username.isEmpty()) {
            future.completeExceptionally(new IllegalArgumentException("Username cannot be null or empty"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(username).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        future.completeExceptionally(new NoSuchElementException("Username does not exist"));
                    } else {
                        db.collection(COLLECTION_NAME).document(username).delete()
                                .addOnSuccessListener(aVoid -> future.complete(null))
                                .addOnFailureListener(future::completeExceptionally);
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<User> validateAndGetUser(String username, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();

        if (username == null || username.isEmpty() || password == null) {
            future.completeExceptionally(new SecurityException("Invalid credentials"));
            return future;
        }

        db.collection(COLLECTION_NAME).document(username).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null && user.getPassword().equals(password)) {
                            future.complete(user);
                        } else {
                            future.completeExceptionally(new SecurityException("Invalid credentials"));
                        }
                    } else {
                        future.completeExceptionally(new SecurityException("Invalid credentials"));
                    }
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Void> clear() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        db.collection(COLLECTION_NAME).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                    future.complete(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}