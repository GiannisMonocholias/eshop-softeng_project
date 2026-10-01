package gr.softeng.team21.firebasedao;

import android.net.Uri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.concurrent.CompletableFuture;
import gr.softeng.team21.dao.ImageStorageDAO;

/**
 * Firebase implementation of the {@link ImageStorageDAO}.
 * Handles the actual uploading and deleting of image files using Firebase Cloud Storage.
 * @author Γιάννης Μονοχολιάς
 */
public class ImageStorageDAOFirebase implements ImageStorageDAO {

    private final FirebaseStorage storage;

    /**
     * Initializes the DAO with the default FirebaseStorage instance.
     */
    public ImageStorageDAOFirebase() {
        this.storage = FirebaseStorage.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<String> uploadProfileImage(String userId, Uri imageUri) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Define the storage path for the user's profile image
        StorageReference photoRef = storage.getReference().child("profile_images/" + userId + ".jpg");

        // Upload the file to Firebase Storage
        photoRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Upon successful upload, retrieve the public download URL
            photoRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> future.complete(uri.toString()))
                    .addOnFailureListener(future::completeExceptionally);
        }).addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> deleteProfileImage(String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Locate the image file in Firebase Storage
        StorageReference photoRef = storage.getReference().child("profile_images/" + userId + ".jpg");

        // Delete the file
        photoRef.delete()
                .addOnSuccessListener(aVoid -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}