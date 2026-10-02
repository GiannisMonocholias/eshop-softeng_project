package gr.softeng.team21.memorydao;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ImageStorageDAO;

/**
 * In-memory implementation of {@link ImageStorageDAO} for testing purposes.
 * Simulates uploading and deleting profile images without requiring network access.
 *
 * @author PAVLOS GRATSANIS
 */
public class ImageStorageDAOMemory implements ImageStorageDAO {

    private final Map<String, String> storageMock = new HashMap<>();

    @Override
    public CompletableFuture<String> uploadProfileImage(String userId, Uri imageUri) {
        CompletableFuture<String> future = new CompletableFuture<>();

        String fakeDownloadUrl = "https://mockstorage.com/profile_images/" + userId + ".jpg";

        storageMock.put(userId, fakeDownloadUrl);

        future.complete(fakeDownloadUrl);

        return future;
    }

    @Override
    public CompletableFuture<Void> deleteProfileImage(String userId) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        storageMock.remove(userId);

        future.complete(null);
        return future;
    }
}