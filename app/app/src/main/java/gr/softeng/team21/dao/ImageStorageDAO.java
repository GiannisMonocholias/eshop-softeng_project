package gr.softeng.team21.dao;

import android.net.Uri;
import java.util.concurrent.CompletableFuture;

/**
 * Data Access Object (DAO) interface for managing profile images in cloud storage.
 * Decouples the presentation layer from the specific storage implementation (e.g., Firebase).
 * @author Γιάννης Μονοχολιάς
 */
public interface ImageStorageDAO {

    /**
     * Uploads a user's profile image to the cloud storage.
     *
     * @param userId   The unique identifier of the user.
     * @param imageUri The local URI of the image to be uploaded.
     * @return A CompletableFuture containing the public download URL of the uploaded image.
     */
    CompletableFuture<String> uploadProfileImage(String userId, Uri imageUri);

    /**
     * Deletes a user's profile image from the cloud storage.
     *
     * @param userId The unique identifier of the user whose image should be deleted.
     * @return A CompletableFuture representing the completion of the deletion process.
     */
    CompletableFuture<Void> deleteProfileImage(String userId);
}